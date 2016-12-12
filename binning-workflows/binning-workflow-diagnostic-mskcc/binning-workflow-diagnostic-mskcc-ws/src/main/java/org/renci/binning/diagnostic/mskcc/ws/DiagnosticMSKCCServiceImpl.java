package org.renci.binning.diagnostic.mskcc.ws;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.renci.binning.core.BinningExecutorService;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DX;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagnosticMSKCCServiceImpl implements DiagnosticMSKCCService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticMSKCCServiceImpl.class);

    private BinningDAOBeanService binningDAOBeanService;

    private BinningExecutorService binningExecutorService;

    public DiagnosticMSKCCServiceImpl() {
        super();
    }

    @Override
    public Integer submit(String participant, String gender, Integer dxId, Integer listVersion) {
        logger.debug("ENTERING submit(String, String, Integer, Integer)");
        DiagnosticBinningJob binningJob = new DiagnosticBinningJob();
        try {
            binningJob.setStudy("GS");
            binningJob.setGender(gender);
            binningJob.setParticipant(participant);
            binningJob.setListVersion(listVersion);
            binningJob.setStatus(binningDAOBeanService.getDiagnosticStatusTypeDAO().findById("Requested"));
            DX dx = binningDAOBeanService.getDXDAO().findById(dxId);
            binningJob.setDx(dx);
            List<DiagnosticBinningJob> foundBinningJobs = binningDAOBeanService.getDiagnosticBinningJobDAO().findByExample(binningJob);
            if (CollectionUtils.isNotEmpty(foundBinningJobs)) {
                binningJob = foundBinningJobs.get(0);
            } else {
                binningJob.setId(binningDAOBeanService.getDiagnosticBinningJobDAO().save(binningJob));
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
