package org.renci.binning.dao.clinbin;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.UnimportantExonPK;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class UnimportantExonDAOTest {

    @Test
    public void testFindByPK() {
        try {
            BinningDAOManager daoMgr = BinningDAOManager.getInstance();
            System.out.println(daoMgr.getDAOBean().getUnimportantExonDAO().findById(new UnimportantExonPK("NM_000021.3", 6)));
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

}
