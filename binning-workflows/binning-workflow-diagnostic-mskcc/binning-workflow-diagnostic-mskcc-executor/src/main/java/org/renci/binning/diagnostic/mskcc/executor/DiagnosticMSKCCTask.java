package org.renci.binning.diagnostic.mskcc.executor;

import static org.renci.binning.core.Constants.BINNING_HOME;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagnosticMSKCCTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticMSKCCTask.class);

    private BinningDAOBeanService binningDAOBeanService;

    private DiagnosticBinningJob binningJob;

    private ProcessEngine processEngine;

    public DiagnosticMSKCCTask() {
        super();
    }

    public DiagnosticMSKCCTask(BinningDAOBeanService binningDAOBeanService, DiagnosticBinningJob binningJob, ProcessEngine processEngine) {
        super();
        this.binningDAOBeanService = binningDAOBeanService;
        this.binningJob = binningJob;
        this.processEngine = processEngine;
    }

    @Override
    public void run() {
        logger.debug("ENTERING run()");

        try {
            RepositoryService repositoryService = processEngine.getRepositoryService();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            HistoryService historyService = processEngine.getHistoryService();

            repositoryService.createDeployment().addClasspathResource("org/renci/binning/diagnostic/mskcc/executor/mskcc.bpmn20.xml")
                    .deploy();

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("daoBean", binningDAOBeanService);
            variables.put("job", binningJob);
            variables.put("irods.home", "/projects/mapseq/apps/irods-4.2.0/icommands");
            // variables.put("process.data.dir", "/opt/Bin2/process_data");

            String binningHome = System.getenv(BINNING_HOME);
            variables.put(BINNING_HOME, binningHome);

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("mskcc_diagnostic_binning", variables);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public BinningDAOBeanService getBinningDAOBeanService() {
        return binningDAOBeanService;
    }

    public void setBinningDAOBeanService(BinningDAOBeanService binningDAOBeanService) {
        this.binningDAOBeanService = binningDAOBeanService;
    }

    public DiagnosticBinningJob getBinningJob() {
        return binningJob;
    }

    public void setBinningJob(DiagnosticBinningJob binningJob) {
        this.binningJob = binningJob;
    }

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

}
