package org.renci.binning.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.refseq.RefSeqCodingSequenceDAOImpl;
import org.renci.binning.dao.refseq.model.RefSeqCodingSequence;
import org.renci.binning.dao.refseq.model.RegionGroup;
import org.renci.binning.dao.refseq.model.RegionGroupRegion;

public class RefSeqCodingSequenceTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-canvas", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testTranscriptionSave() {
        RefSeqCodingSequenceDAOImpl refSeqCodingSequenceDAO = new RefSeqCodingSequenceDAOImpl();
        refSeqCodingSequenceDAO.setEntityManager(em);
        try {
            List<RefSeqCodingSequence> results = refSeqCodingSequenceDAO.findByVersion("48");
            if (results != null && !results.isEmpty()) {
                for (RefSeqCodingSequence cds : results) {
                    System.out.println(cds.toString());
                    Set<RegionGroup> rgSet = cds.getLocations();
                    if (rgSet != null && !rgSet.isEmpty()) {
                        for (RegionGroup rg : rgSet) {
                            System.out.println(rg.toString());
                            Set<RegionGroupRegion> rgrSet = rg.getRegionGroupRegions();
                            if (rgrSet != null && !rgrSet.isEmpty()) {
                                for (RegionGroupRegion rgr : rgrSet) {
                                    System.out.println(rgr.toString());
                                }
                            }
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
