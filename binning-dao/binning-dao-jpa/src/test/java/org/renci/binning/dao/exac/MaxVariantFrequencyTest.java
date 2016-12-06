package org.renci.binning.dao.exac;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.exac.model.MaxVariantFrequency;
import org.renci.binning.dao.jpa.exac.MaxVariantFrequencyDAOImpl;

public class MaxVariantFrequencyTest {

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
    public void testFindByGeneNameAndMaxAlleleFrequency() {
        MaxVariantFrequencyDAOImpl maxVariantFrequencyDAO = new MaxVariantFrequencyDAOImpl();
        maxVariantFrequencyDAO.setEntityManager(em);

        try {
            List<MaxVariantFrequency> results = maxVariantFrequencyDAO.findByGeneNameAndMaxAlleleFrequency("BRCA1", 0.05);
            assertTrue(CollectionUtils.isNotEmpty(results));
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

}
