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

public class InitializeDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(InitializeDelegate.class);

    public InitializeDelegate() {
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
            DiagnosticBinningJob binningJob = daoBean.getDiagnosticBinningJobDAO().findById(binningJobId);
            // Integer listVersion = binningJob.getListVersion();
            // if (listVersion == null) {
            // logger.warn("listVersion was null");
            // binningJob.setListVersion(daoBean.getDXExonsDAO().findMaxListVersion());
            // }

            binningJob.setStart(new Date());
            binningJob.setStop(null);
            binningJob.setFailureMessage("");

            daoBean.getDiagnosticBinningJobDAO().save(binningJob);
            logger.info(binningJob.toString());
        } catch (CANVASDAOException e) {
            logger.error(e.getMessage(), e);
            throw new FlowableException(e.getMessage());
        }

    }

}
