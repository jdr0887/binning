package org.renci.canvas.binning.core.diagnostic;

import java.io.File;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.binning.core.GATKDepthInterval;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.DXCoverage;
import org.renci.canvas.dao.clinbin.model.DXCoveragePK;
import org.renci.canvas.dao.clinbin.model.DXExons;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLoadCoverageCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLoadCoverageCallable.class);

    public abstract File getAllIntervalsFile(Integer listVersion);

    public abstract File getDepthFile(String participant, Integer listVersion) throws BinningException;

    public abstract void processIntervals(SortedSet<GATKDepthInterval> allIntervalSet, File depthFile, String participant,
            Integer listVersion) throws BinningException;

    private CANVASDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public AbstractLoadCoverageCallable(CANVASDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public Void call() throws BinningException {
        logger.debug("ENTERING call()");

        try {

            File allIntervalsFile = getAllIntervalsFile(binningJob.getListVersion());

            List<String> allIntervals = FileUtils.readLines(allIntervalsFile, "UTF-8");
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

                DXExons example = new DXExons(binningJob.getListVersion(), null, null, null, chromosome, start, end, null);
                List<DXExons> dxExonList = daoBean.getDXExonsDAO().findByExample(example);
                if (CollectionUtils.isNotEmpty(dxExonList)) {

                    ExecutorService es = Executors.newFixedThreadPool(4);
                    for (DXExons dxExon : dxExonList) {
                        es.submit(() -> {

                            try {
                                logger.info(dxExon.toString());

                                DXCoveragePK key = new DXCoveragePK(dxExon.getId(), binningJob.getParticipant());
                                DXCoverage dxCoverage = daoBean.getDXCoverageDAO().findById(key);
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
                                daoBean.getDXCoverageDAO().save(dxCoverage);
                            } catch (CANVASDAOException e) {
                                logger.error(e.getMessage(), e);
                            }

                        });
                    }
                    es.shutdown();
                    es.awaitTermination(1L, TimeUnit.HOURS);
                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }
        return null;
    }

    public CANVASDAOBeanService getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(CANVASDAOBeanService daoBean) {
        this.daoBean = daoBean;
    }

    public DiagnosticBinningJob getBinningJob() {
        return binningJob;
    }

    public void setBinningJob(DiagnosticBinningJob binningJob) {
        this.binningJob = binningJob;
    }

}
