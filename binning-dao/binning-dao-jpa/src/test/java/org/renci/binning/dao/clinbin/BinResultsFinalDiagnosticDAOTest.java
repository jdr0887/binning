package org.renci.binning.dao.clinbin;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.BinResultsFinalDiagnosticDAO;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class BinResultsFinalDiagnosticDAOTest {

    @Test
    public void testFindByParticipantAndListVersion() {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        BinResultsFinalDiagnosticDAO binResultsFinalDiagnosticDAO = daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO();
        try {
            List<BinResultsFinalDiagnostic> results = binResultsFinalDiagnosticDAO.findByDXIdAndParticipantAndVersion(22L, "NCG_00064", 16);
            assertTrue(results != null);
            assertTrue(!results.isEmpty());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindDXIdCount() {
        try {
            BinningDAOManager daoMgr = BinningDAOManager.getInstance();
            BinResultsFinalDiagnosticDAO binResultsFinalDiagnosticDAO = daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO();
            Long ret = binResultsFinalDiagnosticDAO.findDXIdCount("NCG_00064");
            assertTrue(ret != null);
            System.out.println(ret.toString());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAnalyzedVariantsCount() {
        try {
            BinningDAOManager daoMgr = BinningDAOManager.getInstance();
            BinResultsFinalDiagnosticDAO binResultsFinalDiagnosticDAO = daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO();
            Long ret = binResultsFinalDiagnosticDAO.findAnalyzedVariantsCount("NCG_00064");
            assertTrue(ret != null);
            System.out.println(ret.toString());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

}
