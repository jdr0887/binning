package org.renci.binning.diagnostic.mskcc.executor;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.clinbin.model.Report;
import org.renci.binning.diagnostic.mskcc.commons.GenerateReportCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateReportDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(GenerateReportDelegate.class);

    public GenerateReportDelegate() {
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

            binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Generating Report"));
            daoBean.getDiagnosticBinningJobDAO().save(binningJob);

            Report report = Executors.newSingleThreadExecutor().submit(new GenerateReportCallable(daoBean, binningJob)).get();
            logger.info(report.toString());
            daoBean.getReportDAO().save(report);

            binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Generated Report"));
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
