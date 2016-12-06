package org.renci.binning.dao.var;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.var.model.VariantType;

public class LoadVariantTypeDataRunnable implements Runnable {

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Override
    public void run() {

        try {
            daoMgr.getDAOBean().getVariantTypeDAO().save(new VariantType("snp"));
            daoMgr.getDAOBean().getVariantTypeDAO().save(new VariantType("ins"));
            daoMgr.getDAOBean().getVariantTypeDAO().save(new VariantType("del"));
            daoMgr.getDAOBean().getVariantTypeDAO().save(new VariantType("sub"));
            daoMgr.getDAOBean().getVariantTypeDAO().save(new VariantType("ref"));
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        LoadVariantTypeDataRunnable runnable = new LoadVariantTypeDataRunnable();
        runnable.run();
    }

}
