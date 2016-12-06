package org.renci.binning.dao.ref;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.ref.GenomeRefSeqLocationDAO;
import org.renci.binning.dao.ref.model.GenomeRefSeqLocation;

public class GenomeRefSeqLocationTest {

    @Test
    public void testFindBySeqType() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        GenomeRefSeqLocationDAO genomeRefSeqLocationDAO = daoMgr.getDAOBean().getGenomeRefSeqLocationDAO();
        List<GenomeRefSeqLocation> genomeRefSeqLocationList = genomeRefSeqLocationDAO.findByRefIdAndVersionedAccesionAndPosition(2,
                "NC_000024.9", 21477867);
        genomeRefSeqLocationList.forEach(a -> System.out.println(a.toString()));
    }

}
