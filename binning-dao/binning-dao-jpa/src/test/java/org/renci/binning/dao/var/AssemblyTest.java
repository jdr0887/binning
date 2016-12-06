package org.renci.binning.dao.var;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.var.model.Assembly;

public class AssemblyTest {

    @Test
    public void testFindBySampleName() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        List<Assembly> assemblyList = daoMgr.getDAOBean().getAssemblyDAO().findBySampleName("NCG_00497");
        assemblyList.forEach(a -> System.out.println(a.toString()));
    }

}
