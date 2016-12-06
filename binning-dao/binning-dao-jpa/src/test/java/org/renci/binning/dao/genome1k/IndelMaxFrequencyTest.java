package org.renci.binning.dao.genome1k;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.IndelMaxFrequencyDAO;
import org.renci.binning.dao.genome1k.model.IndelMaxFrequency;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class IndelMaxFrequencyTest {

    @Test
    public void testFindByLocatedVariantIdAndVersion() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        IndelMaxFrequencyDAO indelMaxFrequencyDAO = daoMgr.getDAOBean().getIndelMaxFrequencyDAO();
        List<IndelMaxFrequency> indelMaxFrequencyList = indelMaxFrequencyDAO.findByLocatedVariantIdAndVersion(16297027L, 2);
        if (CollectionUtils.isNotEmpty(indelMaxFrequencyList)) {
            indelMaxFrequencyList.forEach(a -> System.out.println(a.toString()));
        }
    }

}
