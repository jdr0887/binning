package org.renci.binning.core;

public class BinningExecutorServiceImpl implements BinningExecutorService {

    private BinningThreadPoolExecutor executor;

    public BinningExecutorServiceImpl() {
        super();
    }

    @Override
    public BinningThreadPoolExecutor getExecutor() {
        return executor;
    }

    @Override
    public void setExecutor(BinningThreadPoolExecutor executor) {
        this.executor = executor;
    }

}
