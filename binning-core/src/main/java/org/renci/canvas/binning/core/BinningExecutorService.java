package org.renci.canvas.binning.core;

public interface BinningExecutorService {

    public BinningThreadPoolExecutor getExecutor();

    public void setExecutor(BinningThreadPoolExecutor executor);

}
