package org.renci.binning.dao.dbsnp;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.dbsnp.model.SNPMappingAgg;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class SNPMappingAggDAOTest {

    @Test
    public void testFindByLocatedVariantId() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO().findByLocatedVariantId(123L);
        if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
            for (SNPMappingAgg snpMappingAgg : snpMappingAggList) {

            }
        }
    }

}
