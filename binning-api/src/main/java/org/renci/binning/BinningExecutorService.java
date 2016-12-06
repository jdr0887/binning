package org.renci.binning;

public class BinningExecutorService {

    private BinningThreadPoolExecutor executor;

    public BinningExecutorService() {
        super();
    }

    public BinningThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(BinningThreadPoolExecutor executor) {
        this.executor = executor;
    }

}
