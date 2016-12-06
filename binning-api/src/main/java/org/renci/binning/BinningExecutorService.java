package org.renci.binning;

public interface BinningExecutorService {

    public BinningThreadPoolExecutor getExecutor();

    public void setExecutor(BinningThreadPoolExecutor executor);

}