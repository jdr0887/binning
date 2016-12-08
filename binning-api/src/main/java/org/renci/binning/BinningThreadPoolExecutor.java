package org.renci.binning;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinningThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger logger = LoggerFactory.getLogger(BinningThreadPoolExecutor.class);

    public BinningThreadPoolExecutor() {
        super(1, 1, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        logger.info(String.format("ActiveCount: %d, TaskCount: %d, CompletedTaskCount: %d", getActiveCount(), getTaskCount(),
                getCompletedTaskCount()));
    }

}
