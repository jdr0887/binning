package org.renci.binning.dao.clinbin;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinX;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class IncidentalBinXDAOTest {

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Test
    public void testFindByHGMDVersion() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<IncidentalBinX> variants = daoMgr.getDAOBean().getIncidentalBinXDAO().findByHGMDVersion(1);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

}
