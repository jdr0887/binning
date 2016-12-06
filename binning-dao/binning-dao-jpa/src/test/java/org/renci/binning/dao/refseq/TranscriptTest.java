package org.renci.binning.dao.refseq;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.refseq.TranscriptDAO;
import org.renci.binning.dao.refseq.model.Transcript;

public class TranscriptTest {

    @Test
    public void testFindByGenomeRefIdAndRefSeqVersion() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        TranscriptDAO transcriptDAO = daoMgr.getDAOBean().getTranscriptDAO();
        List<Transcript> transcriptList = transcriptDAO.findByGenomeRefIdAndRefSeqVersion(2, "61");
        System.out.println(transcriptList.size());
        // transcriptList.forEach(a -> System.out.printf("%s%n", a.toString()));
    }

}
