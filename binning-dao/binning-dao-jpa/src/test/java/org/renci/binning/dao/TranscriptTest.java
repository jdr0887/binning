package org.renci.binning.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.binning.dao.jpa.refseq.TranscriptMapsDAOImpl;
import org.renci.binning.dao.jpa.refseq.TranscriptMapsExonsDAOImpl;
import org.renci.binning.dao.refseq.model.TranscriptMaps;
import org.renci.binning.dao.refseq.model.TranscriptMapsExons;

public class TranscriptTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-canvas", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testTranscriptionSave() {

        TranscriptMapsDAOImpl transcriptMapsDAO = new TranscriptMapsDAOImpl();
        transcriptMapsDAO.setEntityManager(em);

        TranscriptMapsExonsDAOImpl transcriptMapsExonsDAO = new TranscriptMapsExonsDAOImpl();
        transcriptMapsExonsDAO.setEntityManager(em);

        try {
            List<TranscriptMaps> mapResults = transcriptMapsDAO.findByGenomeRefIdAndRefSeqVersion(1, "48");
            if (mapResults != null && mapResults.size() > 0) {
                for (TranscriptMaps transcriptMaps : mapResults) {
                    System.out.println(transcriptMaps.toString());
                    List<TranscriptMapsExons> mapsExonsResults = transcriptMapsExonsDAO.findByTranscriptMapsId(transcriptMaps.getId());
                    if (mapsExonsResults != null && mapsExonsResults.size() > 0) {
                        for (TranscriptMapsExons transcriptMapsExons : mapsExonsResults) {
                            System.out.println(transcriptMapsExons.toString());
                        }
                    }
                }
            }
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

}
