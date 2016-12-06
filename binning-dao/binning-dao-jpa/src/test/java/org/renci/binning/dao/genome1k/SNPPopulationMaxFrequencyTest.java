package org.renci.binning.dao.genome1k;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.SNPPopulationMaxFrequencyDAO;
import org.renci.binning.dao.genome1k.model.SNPPopulationMaxFrequency;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class SNPPopulationMaxFrequencyTest {

    @Test
    public void testFindByLocatedVariantIdAndVersion() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        SNPPopulationMaxFrequencyDAO snpPopulationMaxFrequencyDAO = daoMgr.getDAOBean().getSNPPopulationMaxFrequencyDAO();
        List<SNPPopulationMaxFrequency> snpPopulationMaxFrequencyList = snpPopulationMaxFrequencyDAO
                .findByLocatedVariantIdAndVersion(16297027L, 2);
        if (CollectionUtils.isNotEmpty(snpPopulationMaxFrequencyList)) {
            snpPopulationMaxFrequencyList.forEach(a -> System.out.println(a.toString()));
        }
    }

}
