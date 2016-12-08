package org.renci.binning.incidental.ncgenes.ws;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.renci.binning.BinningExecutorService;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinX;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.incidental.ncgenes.ws.IncidentalNCGenesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncidentalNCGenesServiceImpl implements IncidentalNCGenesService {

    private static final Logger logger = LoggerFactory.getLogger(IncidentalNCGenesServiceImpl.class);

    private BinningDAOBeanService binningDAOBeanService;

    private BinningExecutorService binningExecutorService;

    public IncidentalNCGenesServiceImpl() {
        super();
    }

    @Override
    public Integer submit(String participant, String gender, Integer incidentalBinId, Integer listVersion) {
        logger.debug("ENTERING submit(String, String, Integer, Integer)");
        IncidentalBinningJob binningJob = new IncidentalBinningJob();
        try {
            binningJob.setStudy("GS");
            binningJob.setGender(gender);
            binningJob.setParticipant(participant);
            binningJob.setListVersion(listVersion);
            binningJob.setStatus(binningDAOBeanService.getIncidentalStatusTypeDAO().findById("Requested"));
            IncidentalBinX incidentalBin = binningDAOBeanService.getIncidentalBinXDAO().findById(incidentalBinId);
            binningJob.setIncidentalBinX(incidentalBin);
            List<IncidentalBinningJob> foundBinningJobs = binningDAOBeanService.getIncidentalBinningJobDAO().findByExample(binningJob);
            if (CollectionUtils.isNotEmpty(foundBinningJobs)) {
                binningJob = foundBinningJobs.get(0);
            } else {
                binningJob.setId(binningDAOBeanService.getIncidentalBinningJobDAO().save(binningJob));
            }
            logger.info(binningJob.toString());

            // DiagnosticGSTask task = new DiagnosticGSTask();
            // task.setBinningDAOBeanService(binningDAOBeanService);
            // task.setBinningJob(binningJob);
            // binningExecutorService.getExecutor().submit(task);

        } catch (BinningDAOException e) {
            logger.error(e.getMessage(), e);
        }
        return binningJob.getId();
    }

    public BinningExecutorService getBinningExecutorService() {
        return binningExecutorService;
    }

    public void setBinningExecutorService(BinningExecutorService binningExecutorService) {
        this.binningExecutorService = binningExecutorService;
    }

    public BinningDAOBeanService getBinningDAOBeanService() {
        return binningDAOBeanService;
    }

    public void setBinningDAOBeanService(BinningDAOBeanService binningDAOBeanService) {
        this.binningDAOBeanService = binningDAOBeanService;
    }

}
