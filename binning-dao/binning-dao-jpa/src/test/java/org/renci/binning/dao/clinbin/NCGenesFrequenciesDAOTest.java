package org.renci.binning.dao.clinbin;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.NCGenesFrequenciesPK;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class NCGenesFrequenciesDAOTest {

    @Test
    public void testFindMaxListVersion() {
        try {
            BinningDAOManager daoMgr = BinningDAOManager.getInstance();
            System.out.println(daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion());
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindByPK() {
        try {
            BinningDAOManager daoMgr = BinningDAOManager.getInstance();
            System.out.println(daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findById(new NCGenesFrequenciesPK(421185709L, "80")));
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }

}
