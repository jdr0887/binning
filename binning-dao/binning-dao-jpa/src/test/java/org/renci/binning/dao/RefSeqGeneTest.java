package org.renci.binning.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIds;
import org.renci.binning.dao.jpa.annotation.AnnotationGeneExternalIdsDAOImpl;
import org.renci.binning.dao.jpa.refseq.RefSeqGeneDAOImpl;
import org.renci.binning.dao.refseq.model.RefSeqGene;

public class RefSeqGeneTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-canvas", null);
        em = emf.createEntityManager();
    }

    @Test
    public void testFindByRefSeqVersionAndTranscriptId() {

        RefSeqGeneDAOImpl refSeqGeneDAO = new RefSeqGeneDAOImpl();
        refSeqGeneDAO.setEntityManager(em);

        AnnotationGeneExternalIdsDAOImpl annotationGeneExternalIdsDAO = new AnnotationGeneExternalIdsDAOImpl();
        annotationGeneExternalIdsDAO.setEntityManager(em);

        try {
            List<RefSeqGene> refSeqGeneList = refSeqGeneDAO.findByRefSeqVersionAndTranscriptId("61", "NM_001013354.1");
            assertTrue(refSeqGeneList != null);
            assertTrue(refSeqGeneList.size() > 0);
            List<AnnotationGeneExternalIds> annotatedGene = annotationGeneExternalIdsDAO.findByExternalId(refSeqGeneList.get(0).getId());
            assertTrue(annotatedGene != null);

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
