package org.renci.binning.diagnostic.ncgenes.ws;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.renci.binning.core.BinningExecutorService;
import org.renci.binning.core.diagnostic.DiagnosticBinningJobInfo;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DX;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagnosticNCGenesServiceImpl implements DiagnosticNCGenesService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticNCGenesServiceImpl.class);

    private BinningDAOBeanService binningDAOBeanService;

    private BinningExecutorService binningExecutorService;

    public DiagnosticNCGenesServiceImpl() {
        super();
    }

    @Override
    public Response submit(DiagnosticBinningJobInfo info) {
        logger.debug("ENTERING submit(DiagnosticBinningJobInfo)");
        logger.info(info.toString());
        DiagnosticBinningJob binningJob = new DiagnosticBinningJob();
        try {
            binningJob.setStudy("NCGENES Study");
            binningJob.setGender(info.getGender());
            binningJob.setParticipant(info.getParticipant());
            binningJob.setListVersion(info.getListVersion());
            binningJob.setStatus(binningDAOBeanService.getDiagnosticStatusTypeDAO().findById("Requested"));
            DX dx = binningDAOBeanService.getDXDAO().findById(info.getDxId());
            binningJob.setDx(dx);
            List<DiagnosticBinningJob> foundBinningJobs = binningDAOBeanService.getDiagnosticBinningJobDAO().findByExample(binningJob);
            if (CollectionUtils.isNotEmpty(foundBinningJobs)) {
                binningJob = foundBinningJobs.get(0);
            } else {
                binningJob.setId(binningDAOBeanService.getDiagnosticBinningJobDAO().save(binningJob));
            }
            info.setId(binningJob.getId());
            logger.info(binningJob.toString());

            // DiagnosticGSTask task = new DiagnosticGSTask();
            // task.setBinningDAOBeanService(binningDAOBeanService);
            // task.setBinningJob(binningJob);
            // binningExecutorService.getExecutor().submit(task);

        } catch (BinningDAOException e) {
            logger.error(e.getMessage(), e);
            return Response.serverError().build();
        }
        return Response.ok(info).build();
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
