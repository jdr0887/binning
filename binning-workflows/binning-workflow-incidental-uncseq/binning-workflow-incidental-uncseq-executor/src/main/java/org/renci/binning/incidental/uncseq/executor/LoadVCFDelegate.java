package org.renci.binning.incidental.uncseq.executor;

import static org.renci.binning.Constants.BINNING_HOME;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.incidental.uncseq.commons.LoadVCFCallable;
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

        IncidentalBinningJob binningJob = null;
        o = variables.get("job");
        if (o != null && o instanceof IncidentalBinningJob) {
            binningJob = (IncidentalBinningJob) o;
        }
        logger.info(binningJob.toString());

        try {
            binningJob.setStatus(daoBean.getIncidentalStatusTypeDAO().findById("VCF loading"));
            daoBean.getIncidentalBinningJobDAO().save(binningJob);

            Executors.newSingleThreadExecutor().submit(new LoadVCFCallable(daoBean, binningJob, variables.get(BINNING_HOME).toString()))
                    .get();

            binningJob.setStatus(daoBean.getIncidentalStatusTypeDAO().findById("VCF loaded"));
            daoBean.getIncidentalBinningJobDAO().save(binningJob);

        } catch (Exception e) {
            try {
                binningJob.setStop(new Date());
                binningJob.setFailureMessage(e.getMessage());
                binningJob.setStatus(daoBean.getIncidentalStatusTypeDAO().findById("Failed"));
                daoBean.getIncidentalBinningJobDAO().save(binningJob);
            } catch (BinningDAOException e1) {
                e1.printStackTrace();
            }
        }

    }

}
