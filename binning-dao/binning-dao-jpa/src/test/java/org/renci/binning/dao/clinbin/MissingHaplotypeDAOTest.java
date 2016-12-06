package org.renci.binning.dao.clinbin;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.MissingHaplotype;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class MissingHaplotypeDAOTest {

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Test
    public void testFindByParticipantAndIncidentalBinIdAndListVersion() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<MissingHaplotype> missingHaplotypeList = daoMgr.getDAOBean().getMissingHaplotypeDAO()
                .findByParticipantAndIncidentalBinIdAndListVersion("NCG_00185", 11, 1);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(missingHaplotypeList));
        System.out.println(missingHaplotypeList.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

}
