package org.renci.binning.diagnostic.ncgenes.executor;

import static org.renci.binning.core.Constants.BINNING_HOME;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.diagnostic.ncgenes.commons.LoadVCFCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadVCFDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(LoadVCFDelegate.class);

    public LoadVCFDelegate() {
        super();
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.debug("ENTERING execute(DelegateExecution)");

        Map<String, Object> variables = execution.getVariables();

        BinningDAOBeanService daoBean = null;
        Object o = variables.get("daoBean");
        if (o != null && o instanceof BinningDAOBeanService) {
            daoBean = (BinningDAOBeanService) o;
        }

        DiagnosticBinningJob binningJob = null;
        o = variables.get("job");
        if (o != null && o instanceof DiagnosticBinningJob) {
            binningJob = (DiagnosticBinningJob) o;
        }
        logger.info(binningJob.toString());

        try {
            binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("VCF loading"));
            daoBean.getDiagnosticBinningJobDAO().save(binningJob);

            Executors.newSingleThreadExecutor().submit(new LoadVCFCallable(daoBean, binningJob, variables.get(BINNING_HOME).toString()))
                    .get();

            binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("VCF loaded"));
            daoBean.getDiagnosticBinningJobDAO().save(binningJob);

        } catch (Exception e) {
            try {
                binningJob.setStop(new Date());
                binningJob.setFailureMessage(e.getMessage());
                binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Failed"));
                daoBean.getDiagnosticBinningJobDAO().save(binningJob);
            } catch (BinningDAOException e1) {
                e1.printStackTrace();
            }
        }

    }

}
