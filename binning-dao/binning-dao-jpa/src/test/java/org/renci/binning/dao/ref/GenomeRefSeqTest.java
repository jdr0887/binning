package org.renci.binning.dao.ref;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.ref.model.GenomeRefSeq;

public class GenomeRefSeqTest {

    @Test
    public void testFindAll() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        GenomeRefSeqDAO genomeRefSeqDAO = daoMgr.getDAOBean().getGenomeRefSeqDAO();
        List<GenomeRefSeq> genomeRefSeqList = genomeRefSeqDAO.findAll();
        genomeRefSeqList.forEach(a -> System.out.println(a.toString()));
    }

    @Test
    public void testFindBySeqType() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        GenomeRefSeqDAO genomeRefSeqDAO = daoMgr.getDAOBean().getGenomeRefSeqDAO();
        List<GenomeRefSeq> genomeRefSeqList = genomeRefSeqDAO.findBySeqType("Chromosome");
        genomeRefSeqList.forEach(a -> System.out.println(a.toString()));
    }

    @Test
    public void testFindByRefIdAndContigAndSeqType() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        GenomeRefSeqDAO genomeRefSeqDAO = daoMgr.getDAOBean().getGenomeRefSeqDAO();
        List<GenomeRefSeq> genomeRefSeqList = genomeRefSeqDAO.findByRefIdAndContigAndSeqTypeAndAccessionPrefix(4, "1", "Chromosome", "NC_");
        genomeRefSeqList.forEach(a -> System.out.println(a.toString()));
    }

}
