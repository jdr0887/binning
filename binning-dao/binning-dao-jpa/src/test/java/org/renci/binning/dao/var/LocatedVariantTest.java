package org.renci.binning.dao.var;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.var.LocatedVariantDAO;
import org.renci.binning.dao.var.model.LocatedVariant;

public class LocatedVariantTest {

    @Test
    public void testFindByGeneSymbol() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        LocatedVariantDAO LocatedVariantDAO = daoMgr.getDAOBean().getLocatedVariantDAO();
        List<LocatedVariant> LocatedVariantList = LocatedVariantDAO.findByGeneSymbol("BRCA1");
        LocatedVariantList.forEach(a -> System.out.println(a.toString()));
    }

    @Test
    public void testIncrementable() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        LocatedVariantDAO locatedVariantDAO = daoMgr.getDAOBean().getLocatedVariantDAO();
        long startTime = System.currentTimeMillis();
        List<LocatedVariant> locatedVariantList = locatedVariantDAO.findIncrementable(35619);
        long endTime = System.currentTimeMillis();
        System.out.printf("duration = %d", (endTime - startTime) / 1000);
        File out = new File("/tmp", "out.txt");

        locatedVariantList.sort((a, b) -> {
            int ret = a.getGenomeRefSeq().getVerAccession().compareTo(b.getGenomeRefSeq().getVerAccession());
            if (ret == 0) {
                ret = a.getPosition().compareTo(b.getPosition());
            }
            return ret;
        });

        try (FileWriter outFW = new FileWriter(out); BufferedWriter outBW = new BufferedWriter(outFW)) {
            for (LocatedVariant LocatedVariant : locatedVariantList) {
                outBW.write(String.format("%d\t%s\t%d\t%s\t%s\t%d\t%s%n", LocatedVariant.getId(),
                        LocatedVariant.getGenomeRefSeq().getVerAccession(), LocatedVariant.getPosition(),
                        LocatedVariant.getVariantType().getName(), LocatedVariant.getSeq(), LocatedVariant.getEndPosition(),
                        LocatedVariant.getRef()));
                outBW.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
