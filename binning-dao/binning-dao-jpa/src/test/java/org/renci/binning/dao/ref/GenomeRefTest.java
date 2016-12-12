package org.renci.binning.dao.ref;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.ref.model.GenomeRef;

public class GenomeRefTest {

    @Test
    public void testFindAll() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        GenomeRefDAO genomeRefDAO = daoMgr.getDAOBean().getGenomeRefDAO();
        List<GenomeRef> genomeRefList = genomeRefDAO.findAll();
        genomeRefList.forEach(a -> System.out.println(a.toString()));
    }

}
