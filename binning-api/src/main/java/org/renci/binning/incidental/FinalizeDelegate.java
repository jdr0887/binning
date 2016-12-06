package org.renci.binning.incidental;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinalizeDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(FinalizeDelegate.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.debug("ENTERING execute(DelegateExecution)");
        Object o = execution.getVariables().get("job");
        if (o != null && o instanceof IncidentalBinningJob) {
            IncidentalBinningJob binningJob = (IncidentalBinningJob) o;
            binningJob.setStatus(daoMgr.getDAOBean().getIncidentalStatusTypeDAO().findById("Complete"));
            binningJob.setStop(new Date());
            daoMgr.getDAOBean().getIncidentalBinningJobDAO().save(binningJob);
        }
    }

}
