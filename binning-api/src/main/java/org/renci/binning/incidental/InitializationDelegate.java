package org.renci.binning.incidental;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitializationDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(InitializationDelegate.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    public InitializationDelegate() {
        super();
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("ENTERING execute(DelegateExecution)");
        Object o = execution.getVariables().get("job");
        if (o != null && o instanceof IncidentalBinningJob) {
            IncidentalBinningJob binningJob = (IncidentalBinningJob) o;

            Integer listVersion = binningJob.getListVersion();
            if (listVersion == null) {
                logger.warn("listVersion was null");
                binningJob.setListVersion(daoMgr.getDAOBean().getDXExonsDAO().findMaxListVersion());
            }

            binningJob.setFailureMessage("");
            binningJob.setStart(new Date());
            binningJob.setStop(null);

            daoMgr.getDAOBean().getIncidentalBinningJobDAO().save(binningJob);

        }
    }

}
