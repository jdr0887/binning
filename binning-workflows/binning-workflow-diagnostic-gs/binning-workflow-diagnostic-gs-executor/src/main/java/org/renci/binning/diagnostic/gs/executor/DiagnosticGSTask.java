package org.renci.binning.diagnostic.gs.executor;

import static org.renci.binning.core.Constants.BINNING_HOME;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagnosticGSTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticGSTask.class);

    private BinningDAOBeanService binningDAOBeanService;

    private DiagnosticBinningJob binningJob;

    public DiagnosticGSTask() {
        super();
    }

    @Override
    public void run() {
        logger.debug("ENTERING run()");

        try {

            ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                    .buildProcessEngine();

            RepositoryService repositoryService = processEngine.getRepositoryService();

            repositoryService.createDeployment().addClasspathResource("org/renci/canvas/binning/diagnostic.bpmn20.xml").deploy();
            RuntimeService runtimeService = processEngine.getRuntimeService();

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("job", binningJob);
            variables.put("irods.home", "/projects/mapseq/apps/irods-4.2.0/icommands");
            // variables.put("process.data.dir", "/opt/Bin2/process_data");

            String binningHome = System.getenv(BINNING_HOME);
            variables.put(BINNING_HOME, binningHome);

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("diagnostic_binning", variables);
            // repositoryService.deleteDeployment(deployment.getId());

            HistoryService historyService = processEngine.getHistoryService();
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstance.getId()).singleResult();
            logger.info("Process instance end time: {}", historicProcessInstance.getEndTime());
            processEngine.close();

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

}
