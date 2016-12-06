package org.renci.binning.dao.clinvar;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinvar.ReferenceClinicalAssertionsDAO;
import org.renci.binning.dao.clinvar.model.ReferenceClinicalAssertions;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class ReferenceClinicalAssertionsDAOTest {

    @Test
    public void testFindDiagnostic() {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        ReferenceClinicalAssertionsDAO referenceClinicalAssertionsDAO = daoMgr.getDAOBean().getReferenceClinicalAssertionsDAO();
        try {
            List<ReferenceClinicalAssertions> results = referenceClinicalAssertionsDAO.findDiagnostic(22L, "NCG_00064", 16);
            assertTrue(results != null);
            assertTrue(!results.isEmpty());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindByLocVarIdAndVersion() {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        ReferenceClinicalAssertionsDAO referenceClinicalAssertionsDAO = daoMgr.getDAOBean().getReferenceClinicalAssertionsDAO();
        try {
            List<ReferenceClinicalAssertions> results = referenceClinicalAssertionsDAO.findByLocatedVariantIdAndVersion(404269787L, 4);
            assertTrue(results != null);
            assertTrue(!results.isEmpty());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindByLocVarIdAndVersionAndAssertionStatusExclusionList() {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        ReferenceClinicalAssertionsDAO referenceClinicalAssertionsDAO = daoMgr.getDAOBean().getReferenceClinicalAssertionsDAO();
        try {
            List<ReferenceClinicalAssertions> results = referenceClinicalAssertionsDAO
                    .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(404269787L, 4L,
                            Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));
            assertTrue(results != null);
            assertTrue(!results.isEmpty());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

}
