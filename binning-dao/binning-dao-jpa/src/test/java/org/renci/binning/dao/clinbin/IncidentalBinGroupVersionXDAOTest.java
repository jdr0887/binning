package org.renci.binning.dao.clinbin;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinGroupVersionX;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class IncidentalBinGroupVersionXDAOTest {

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Test
    public void testFindByIncidentalBinIdAndGroupVersion() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<IncidentalBinGroupVersionX> groupVersions = daoMgr.getDAOBean().getIncidentalBinGroupVersionXDAO()
                .findByIncidentalBinIdAndGroupVersion(1, 5);
        long endTime = System.currentTimeMillis();
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

}
