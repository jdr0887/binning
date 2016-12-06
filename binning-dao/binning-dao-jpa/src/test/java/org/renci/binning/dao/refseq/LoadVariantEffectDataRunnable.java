package org.renci.binning.dao.refseq;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.refseq.model.VariantEffect;

public class LoadVariantEffectDataRunnable implements Runnable {

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Override
    public void run() {

        try {
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("synonymous indel", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("exon", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("frameshifting indel", 1));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("intergenic", 5));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("intron", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("missense", 3));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("non-frameshifting indel", 3));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("nonsense", 1));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("splice-site", 1));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("stoploss", 2));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("synonymous", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("UTR", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("UTR-3", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("UTR-5", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("nonsense indel", 1));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("splice-site-UTR-3", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("splice-site-UTR-5", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("splice-site-UTR", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("potential RNA-editing site", 4));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("boundary-crossing indel", 1));
            daoMgr.getDAOBean().getVariantEffectDAO().save(new VariantEffect("noncoding boundary-crossing indel", 4));
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        LoadVariantEffectDataRunnable runnable = new LoadVariantEffectDataRunnable();
        runnable.run();
    }

}
