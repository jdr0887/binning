package org.renci.binning.incidental;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.renci.binning.BinningException;
import org.renci.binning.GATKDepthInterval;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DXCoverage;
import org.renci.binning.dao.clinbin.model.DXCoveragePK;
import org.renci.binning.dao.clinbin.model.DXExons;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLoadCoverageCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLoadCoverageCallable.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    public abstract File getAllIntervalsFile(Integer listVersion);

    public abstract File getDepthFile(String participant, Integer listVersion) throws BinningException;

    public abstract void processIntervals(SortedSet<GATKDepthInterval> allIntervalSet, File depthFile, String participant,
            Integer listVersion) throws BinningException;

    private Map<String, Object> variables;

    public AbstractLoadCoverageCallable(Map<String, Object> variables) {
        super();
        this.variables = variables;
    }

    @Override
    public Void call() throws BinningException {
        logger.debug("ENTERING call()");

        Object o = variables.get("job");
        if (o != null && o instanceof IncidentalBinningJob) {
            IncidentalBinningJob binningJob = (IncidentalBinningJob) o;
            logger.info(binningJob.toString());

            try {

                binningJob.setStatus(daoMgr.getDAOBean().getIncidentalStatusTypeDAO().findById("Coverage loading"));
                daoMgr.getDAOBean().getIncidentalBinningJobDAO().save(binningJob);

                File allIntervalsFile = getAllIntervalsFile(binningJob.getListVersion());

                List<String> allIntervals = FileUtils.readLines(allIntervalsFile);
                if (allIntervals.contains("Targets")) {
                    allIntervals.remove("Targets");
                }

                SortedSet<GATKDepthInterval> allIntervalSet = new TreeSet<GATKDepthInterval>();
                allIntervals.forEach(a -> allIntervalSet.add(new GATKDepthInterval(a)));

                File depthFile = getDepthFile(binningJob.getParticipant(), binningJob.getListVersion());

                processIntervals(allIntervalSet, depthFile, binningJob.getParticipant(), binningJob.getListVersion());

                // load exon coverage
                for (GATKDepthInterval interval : allIntervalSet) {
                    logger.debug(interval.toString());
                    String chromosome = interval.getContig();
                    Integer start = interval.getStartPosition();
                    Integer end = interval.getEndPosition();
                    if (end == null) {
                        end = start;
                    }

                    List<DXExons> dxExonList = daoMgr.getDAOBean().getDXExonsDAO()
                            .findByListVersionAndChromosomeAndRange(binningJob.getListVersion(), chromosome, start, end);
                    if (CollectionUtils.isNotEmpty(dxExonList)) {

                        ExecutorService es = Executors.newFixedThreadPool(4);
                        for (DXExons dxExon : dxExonList) {
                            es.submit(() -> {

                                try {
                                    logger.info(dxExon.toString());

                                    DXCoveragePK key = new DXCoveragePK(dxExon.getId(), binningJob.getParticipant());
                                    DXCoverage dxCoverage = daoMgr.getDAOBean().getDXCoverageDAO().findById(key);
                                    if (dxCoverage == null) {
                                        dxCoverage = new DXCoverage(key);
                                    }

                                    dxCoverage.setExon(dxExon);
                                    dxCoverage.setFractionGreaterThan1(interval.getSamplePercentAbove1() * 0.01);
                                    dxCoverage.setFractionGreaterThan2(interval.getSamplePercentAbove2() * 0.01);
                                    dxCoverage.setFractionGreaterThan5(interval.getSamplePercentAbove5() * 0.01);
                                    dxCoverage.setFractionGreaterThan8(interval.getSamplePercentAbove8() * 0.01);
                                    dxCoverage.setFractionGreaterThan10(interval.getSamplePercentAbove10() * 0.01);
                                    dxCoverage.setFractionGreaterThan15(interval.getSamplePercentAbove15() * 0.01);
                                    dxCoverage.setFractionGreaterThan20(interval.getSamplePercentAbove20() * 0.01);
                                    dxCoverage.setFractionGreaterThan30(interval.getSamplePercentAbove30() * 0.01);
                                    dxCoverage.setFractionGreaterThan50(interval.getSamplePercentAbove50() * 0.01);
                                    logger.info(dxCoverage.toString());
                                    daoMgr.getDAOBean().getDXCoverageDAO().save(dxCoverage);
                                } catch (BinningDAOException e) {
                                    logger.error(e.getMessage(), e);
                                }

                            });
                        }
                        es.shutdown();
                        es.awaitTermination(1L, TimeUnit.HOURS);
                    }

                }

                binningJob.setStatus(daoMgr.getDAOBean().getIncidentalStatusTypeDAO().findById("Coverage loaded"));
                daoMgr.getDAOBean().getIncidentalBinningJobDAO().save(binningJob);

            } catch (Exception e) {
                try {
                    binningJob.setStop(new Date());
                    binningJob.setFailureMessage(e.getMessage());
                    binningJob.setStatus(daoMgr.getDAOBean().getIncidentalStatusTypeDAO().findById("Failed"));
                    daoMgr.getDAOBean().getIncidentalBinningJobDAO().save(binningJob);
                } catch (BinningDAOException e1) {
                    e1.printStackTrace();
                }
                logger.error(e.getMessage(), e);
                throw new BinningException(e);
            }
        }
        return null;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

}
