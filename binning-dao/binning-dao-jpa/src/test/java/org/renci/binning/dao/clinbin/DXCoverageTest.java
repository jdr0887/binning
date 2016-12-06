package org.renci.binning.dao.clinbin;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DXCoverage;
import org.renci.binning.dao.clinbin.model.DXCoveragePK;
import org.renci.binning.dao.clinbin.model.DXExons;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class DXCoverageTest {

    @Test
    public void testSave() {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        try {
            DXExons exons = daoMgr.getDAOBean().getDXExonsDAO().findById(1);

            DXCoveragePK key = new DXCoveragePK(exons.getId(), "asdf");
            DXCoverage coverage = new DXCoverage(key);
            coverage.setKey(key);
            coverage.setExon(exons);
            coverage.setFractionGreaterThan1(0.629);
            coverage.setFractionGreaterThan5(0.848);
            coverage.setFractionGreaterThan10(0.629);
            coverage.setFractionGreaterThan15(0.629);
            coverage.setFractionGreaterThan30(0.629);
            coverage.setFractionGreaterThan50(0.629);

            DXCoveragePK ret = daoMgr.getDAOBean().getDXCoverageDAO().save(coverage);
            System.out.println(ret.toString());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

}
