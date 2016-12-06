package org.renci.binning;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections.CollectionUtils;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IncidentalMain implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(IncidentalMain.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    public IncidentalMain() {
        super();
    }

    @Override
    public void run() {
        logger.debug("ENTERING run()");

        try {
            List<IncidentalBinningJob> availableBinningJobs = daoMgr.getDAOBean().getIncidentalBinningJobDAO().findAvailableJobs();

            if (CollectionUtils.isNotEmpty(availableBinningJobs)) {

                ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                        .buildProcessEngine();

                RepositoryService repositoryService = processEngine.getRepositoryService();
                repositoryService.createDeployment().addClasspathResource("org/renci/canvas/binning/incidental.bpmn20.xml").deploy();
                RuntimeService runtimeService = processEngine.getRuntimeService();

                for (IncidentalBinningJob job : availableBinningJobs) {
                    if (job.getStatus().getName().equals("Requested")) {
                        job.setStart(new Date());
                        daoMgr.getDAOBean().getIncidentalBinningJobDAO().save(job);
                    }

                    Map<String, Object> variables = new HashMap<String, Object>();
                    variables.put("job", job);
                    variables.put("irods.home", "/projects/mapseq/apps/irods-4.2.0/icommands");
                    variables.put("process.data.dir", "/opt/Bin2/process_data");
                    variables.put("binning.annotation.dir", "/storage/binning/annotation");
                    // variables.put("all.intervals.file",
                    // "/storage/binning/annotation/Intervals/allintervals.v%d.txt");

                    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("diagnostic_binning", variables);
                    // repositoryService.deleteDeployment(deployment.getId());

                    HistoryService historyService = processEngine.getHistoryService();
                    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(processInstance.getId()).singleResult();
                    logger.info("Process instance end time: {}", historicProcessInstance.getEndTime());
                }
                processEngine.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        IncidentalMain m = new IncidentalMain();
        m.run();
    }

}
