package org.renci.binning.dao.refseq;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.refseq.RefSeqGeneDAO;
import org.renci.binning.dao.refseq.model.RefSeqGene;

public class RefSeqGeneTest {

    @Test
    public void testFindByRefSeqVersion() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        RefSeqGeneDAO refSeqGeneDAO = daoMgr.getDAOBean().getRefSeqGeneDAO();
        List<RefSeqGene> refSeqGeneList = refSeqGeneDAO.findByRefSeqVersion("61");
        refSeqGeneList.forEach(a -> System.out.println(a.toString()));
    }

    @Test
    public void testFindByRefSeqVersionAndTranscriptId() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        RefSeqGeneDAO refSeqGeneDAO = daoMgr.getDAOBean().getRefSeqGeneDAO();
        // List<RefSeqGene> refSeqGeneList = refSeqGeneDAO.findByRefSeqVersionAndTranscriptId("61", "NM_001101330.1");
        List<RefSeqGene> refSeqGeneList = refSeqGeneDAO.findByRefSeqVersionAndTranscriptId("61", "XR_108279.1");
        refSeqGeneList.forEach(a -> System.out.println(a.toString()));
    }

}
