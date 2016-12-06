package org.renci.binning.incidental.uncseq.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateIncidentalBinsDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(UpdateIncidentalBinsDelegate.class);

    public UpdateIncidentalBinsDelegate() {
        super();
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("ENTERING execute(DelegateExecution)");
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Void> future = es.submit(new UpdateIncidentalBinsCallable(execution.getVariables()));
        es.shutdown();
        es.awaitTermination(1L, TimeUnit.HOURS);
    }

}
