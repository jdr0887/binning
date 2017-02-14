package org.renci.canvas.binning.core.diagnostic;

import java.util.Date;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinalizeDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(FinalizeDelegate.class);

    public FinalizeDelegate() {
        super();
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
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

        DiagnosticBinningJob binningJob = daoBean.getDiagnosticBinningJobDAO().findById(binningJobId);
        binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Complete"));
        binningJob.setStop(new Date());
        daoBean.getDiagnosticBinningJobDAO().save(binningJob);
        logger.info(binningJob.toString());
    }

}
