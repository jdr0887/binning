package org.renci.binning.diagnostic.test.executor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.collections4.CollectionUtils;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.clinbin.model.MaxFrequency;
import org.renci.binning.diagnostic.test.commons.UpdateFrequenciesCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateFrequenciesDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(UpdateFrequenciesDelegate.class);

    public UpdateFrequenciesDelegate() {
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
            binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Updating frequency table"));
            daoBean.getDiagnosticBinningJobDAO().save(binningJob);

            List<MaxFrequency> results = Executors.newSingleThreadExecutor().submit(new UpdateFrequenciesCallable(daoBean, binningJob))
                    .get();

            if (CollectionUtils.isNotEmpty(results)) {
                logger.info(String.format("saving %d new MaxFrequency instances", results.size()));
                for (MaxFrequency maxFrequency : results) {
                    logger.info(maxFrequency.toString());
                    daoBean.getMaxFrequencyDAO().save(maxFrequency);
                }
            }

            binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Updated frequency table"));
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
