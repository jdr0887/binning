package org.renci.binning;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BinningThreadPoolExecutor extends ThreadPoolExecutor {

    public BinningThreadPoolExecutor() {
        super(1, 1, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }

}
