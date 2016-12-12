package org.renci.binning.dao.refseq;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.refseq.model.TranscriptMaps;
import org.renci.binning.dao.var.model.LocatedVariant;

public class TranscriptMapsTest {

    @Test
    public void findByGenomeRefIdAndRefSeqVersionAndTranscriptId() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        TranscriptMapsDAO transcriptMapsDAO = daoMgr.getDAOBean().getTranscriptMapsDAO();
        List<TranscriptMaps> transcriptMapsList = new ArrayList<>();
        // transcriptMapsList.addAll(transcriptMapsDAO.findByGenomeRefIdAndRefSeqVersionAndTranscriptId(2, "61",
        // "NM_000821.5"));
        transcriptMapsList.addAll(transcriptMapsDAO.findByGenomeRefIdAndRefSeqVersionAndTranscriptId(2, "61", "XR_253608.1"));
        // transcriptMapsList.addAll(transcriptMapsDAO.findByGenomeRefIdAndRefSeqVersionAndTranscriptId(2, "61",
        // "XR_250120.1"));
        transcriptMapsList.forEach(a -> {
            System.out.println("----------------");
            System.out.println(a.toString());
            // System.out.println(a.getGenomeRefSeq().toString());
            // System.out.println(a.getTranscript().toString());
            // System.out.println(a.getExons().size());
        });
    }

    @Test
    public void testFindByGenomeRefIdAndRefSeqVersion() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        TranscriptMapsDAO transcriptMapsDAO = daoMgr.getDAOBean().getTranscriptMapsDAO();
        List<TranscriptMaps> transcriptMapsList = transcriptMapsDAO.findByGenomeRefIdAndRefSeqVersion(2, "61");
        System.out.println(transcriptMapsList.size());
        // Set<Transcript> transcriptSet = new HashSet<Transcript>();
        // transcriptMapsList.forEach(a -> {
        // if (!transcriptSet.contains(a.getTranscript())) {
        // transcriptSet.add(a.getTranscript());
        // }
        // });
        // System.out.println(transcriptSet.size());

        TranscriptMaps transcriptMaps = transcriptMapsList.get(0);
        System.out.println(transcriptMaps.toString());

        List<LocatedVariant> LocatedVariants = daoMgr.getDAOBean().getLocatedVariantDAO()
                .findByVersionAccessionAndRefId(transcriptMaps.getGenomeRefSeq().getVerAccession(), transcriptMaps.getGenomeRefId());
        System.out.println(LocatedVariants.size());

    }

    @Test
    public void testFindByVersionId() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        TranscriptMapsDAO transcriptMapsDAO = daoMgr.getDAOBean().getTranscriptMapsDAO();
        // List<TranscriptMaps> transcriptMapsList = transcriptMapsDAO.findByTranscriptId("NM_002105.2");
        // List<TranscriptMaps> transcriptMapsList = transcriptMapsDAO.findByTranscriptId("NM_000821.5");
        List<TranscriptMaps> transcriptMapsList = transcriptMapsDAO.findByTranscriptId("NM_016521.2");
        // List<TranscriptMaps> transcriptMapsList = transcriptMapsDAO.findByTranscriptId("XM_005274819.1");
        transcriptMapsList.forEach(a -> {
            System.out.println(a.getTranscript().toString());
            System.out.println(a.getGenomeRefSeq().toString());
            System.out.println(a.toString());
            System.out.println("----------------");
        });
    }

    @Test
    public void findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        TranscriptMapsDAO transcriptMapsDAO = daoMgr.getDAOBean().getTranscriptMapsDAO();
        List<TranscriptMaps> transcriptMapsExonsList = transcriptMapsDAO
                .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(2, "61", "NC_000001.10", 65898);
        assertTrue(transcriptMapsExonsList.size() == 1);
        transcriptMapsExonsList.forEach(a -> {
            System.out.printf("%s%n", a.getTranscript().toString());
            System.out.printf("%s%n", a.getGenomeRefSeq().toString());
            System.out.printf("%s%n", a.toString());
        });

        transcriptMapsExonsList = transcriptMapsDAO.findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(2, "61",
                "NC_000001.10", 134774);
        assertTrue(transcriptMapsExonsList.size() == 5);
        transcriptMapsExonsList.forEach(a -> {
            System.out.printf("%s%n", a.getTranscript().toString());
            System.out.printf("%s%n", a.getGenomeRefSeq().toString());
            System.out.printf("%s%n", a.toString());
        });

        transcriptMapsExonsList = transcriptMapsDAO.findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(2, "61",
                "NC_000001.10", 721323);
        assertTrue(transcriptMapsExonsList.size() == 3);
        transcriptMapsExonsList.forEach(a -> {
            System.out.printf("%s%n", a.getTranscript().toString());
            System.out.printf("%s%n", a.getGenomeRefSeq().toString());
            System.out.printf("%s%n", a.toString());
        });

    }

}
