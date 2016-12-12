package org.renci.binning.dao.clinbin;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class DXExonsDAOTest {

    @Test
    public void testFindMaxListVersion() {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        DXExonsDAO dao = daoMgr.getDAOBean().getDXExonsDAO();
        try {
            System.out.println(dao.findMaxListVersion().toString());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

}
