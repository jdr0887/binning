package org.renci.canvas.binning.core.diagnostic;

import java.util.Date;
import java.util.Map;

import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinalizeDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(FinalizeDelegate.class);

    public FinalizeDelegate() {
        super();
    }

    @Override
    public void execute(DelegateExecution execution) {
        logger.debug("ENTERING execute(DelegateExecution)");

        Map<String, Object> variables = execution.getVariables();

        BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
        ServiceReference<CANVASDAOBeanService> daoBeanServiceReference = bundleContext
                .getServiceReference(org.renci.canvas.dao.CANVASDAOBeanService.class);
        CANVASDAOBeanService daoBean = bundleContext.getService(daoBeanServiceReference);

        Integer binningJobId = null;
        Object o = variables.get("binningJobId");
        if (o != null && o instanceof Integer) {
            binningJobId = (Integer) o;
        }

        try {
            DiagnosticBinningJob diagnosticBinningJob = daoBean.getDiagnosticBinningJobDAO().findById(binningJobId);

            Long nullClinVarCount = daoBean.getBinResultsFinalDiagnosticDAO().findNullClinVarDiseaseClassCount(diagnosticBinningJob);
            Long nullHGMDCount = daoBean.getBinResultsFinalDiagnosticDAO().findNullHGMDDiseaseClassCount(diagnosticBinningJob);

            if (nullClinVarCount > 0 || nullHGMDCount > 0) {
                logger.error("nullClinVarCount: {}", nullClinVarCount);
                logger.error("nullHGMDCount: {}", nullHGMDCount);
                diagnosticBinningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Failed"));
                diagnosticBinningJob.setFailureMessage("There were entries with null DiseaseClass");
            } else {
                diagnosticBinningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Complete"));
                diagnosticBinningJob.setStop(new Date());
            }

            daoBean.getDiagnosticBinningJobDAO().save(diagnosticBinningJob);
            logger.info(diagnosticBinningJob.toString());
        } catch (CANVASDAOException e) {
            logger.error(e.getMessage(), e);
            throw new FlowableException(e.getMessage());
        }

    }

}
