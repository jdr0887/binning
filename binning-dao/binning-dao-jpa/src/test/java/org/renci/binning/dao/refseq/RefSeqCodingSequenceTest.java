package org.renci.binning.dao.refseq;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.refseq.RefSeqCodingSequenceDAOImpl;
import org.renci.binning.dao.refseq.model.RefSeqCodingSequence;

public class RefSeqCodingSequenceTest {

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("test-canvas", null);
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testFindByRefSeqVersionAndTranscriptId() throws BinningDAOException {
        RefSeqCodingSequenceDAOImpl refSeqCodingSequenceDAO = new RefSeqCodingSequenceDAOImpl();
        refSeqCodingSequenceDAO.setEntityManager(em);
        List<RefSeqCodingSequence> refSeqCodingSequenceList = refSeqCodingSequenceDAO.findByRefSeqVersionAndTranscriptId("61",
                "NM_001101330.1");
        refSeqCodingSequenceList.forEach(a -> System.out.println(a.toString()));
    }

}
