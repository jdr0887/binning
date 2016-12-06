package org.renci.binning.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.refseq.FeatureDAOImpl;
import org.renci.binning.dao.jpa.refseq.RegionGroupDAOImpl;
import org.renci.binning.dao.jpa.refseq.TranscriptMapsDAOImpl;
import org.renci.binning.dao.jpa.refseq.TranscriptMapsExonsDAOImpl;
import org.renci.binning.dao.refseq.model.Feature;
import org.renci.binning.dao.refseq.model.TranscriptMaps;
import org.renci.binning.dao.refseq.model.TranscriptMapsExons;

public class TranscriptMapsExonsTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-canvas", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testFeatures() {

        TranscriptMapsDAOImpl transcriptMapsDAO = new TranscriptMapsDAOImpl();
        transcriptMapsDAO.setEntityManager(em);

        TranscriptMapsExonsDAOImpl transcriptMapsExonsDAO = new TranscriptMapsExonsDAOImpl();
        transcriptMapsExonsDAO.setEntityManager(em);

        RegionGroupDAOImpl regionGroupDAO = new RegionGroupDAOImpl();
        regionGroupDAO.setEntityManager(em);

        FeatureDAOImpl featureDAO = new FeatureDAOImpl();
        featureDAO.setEntityManager(em);

        String refSeqVersion = "61";
        Integer genomeRefId = 2;

        // XM_005277470.1
        try {
            // List<TranscriptMapsExons> mapsExons = new ArrayList<TranscriptMapsExons>();
            // List<TranscriptMapsExons> mapsExonsResults = transcriptMapsExonsDAO
            // .findByGenomeRefIdAndRefSeqVersionAndAccession(genomeRefId, refSeqVersion, "NM_033663.3");
            // if (mapsExonsResults != null && !mapsExonsResults.isEmpty()) {
            // mapsExons.addAll(mapsExonsResults);
            // }

            // for (TranscriptMapsExons tme : mapsExons) {
            // TranscriptMaps tMaps = tme.getTranscriptMaps();
            // Transcript transcript = tMaps.getTranscript();
            // Set<RegionGroup> regionGroups = transcript.getRegionGroups();
            // if (regionGroups != null && !regionGroups.isEmpty()) {
            // for (RegionGroup regionGroup : regionGroups) {
            // Set<Feature> features = regionGroup.getFeatures();
            // if (features != null && !features.isEmpty()) {
            // for (Feature feature : features) {
            // System.out.println(feature.toString());
            // }
            // }
            // }
            // }
            // }
            //
            // List<RegionGroup> regionGroups = regionGroupDAO.findByRefSeqVersionAndTranscriptId(refSeqVersion,
            // "NM_033663.3");
            // if (regionGroups != null && !regionGroups.isEmpty()) {
            // for (RegionGroup regionGroup : regionGroups) {
            // Set<Feature> features = regionGroup.getFeatures();
            // if (features != null && !features.isEmpty()) {
            // for (Feature feature : features) {
            // System.out.println(feature.toString());
            // }
            // }
            // }
            // }

            List<Feature> features = featureDAO.findByRefSeqVersionAndTranscriptId(refSeqVersion, "NM_033663.3");
            if (features != null && !features.isEmpty()) {
                for (Feature feature : features) {
                    System.out.println(feature.toString());
                }
            }

        } catch (BinningDAOException e) {
            e.printStackTrace();
        }

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
