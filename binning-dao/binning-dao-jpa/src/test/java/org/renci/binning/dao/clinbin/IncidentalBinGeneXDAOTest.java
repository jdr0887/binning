package org.renci.binning.dao.clinbin;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinGeneX;
import org.renci.binning.dao.clinbin.model.IncidentalBinGroupVersionX;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.dao.clinbin.model.IncidentalResultVersionX;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.refseq.model.Variants_61_2;

public class IncidentalBinGeneXDAOTest {

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Test
    public void testFindByIncidentalBinIdAndVersionAndZygosityModes() throws BinningDAOException {

        // List<String> zygosityModeList = Arrays.asList("AD", "AR", "CX", "XLD", "XLR", "V", "Y", "RISK");
        List<String> zygosityModeList = Arrays.asList("AD", "AR", "CX", "XLD", "XLR", "V", "Y", "RISK");
        long startTime = System.currentTimeMillis();

        IncidentalBinningJob incidentalBinningJob = daoMgr.getDAOBean().getIncidentalBinningJobDAO().findById(1530);
        System.out.println(incidentalBinningJob.toString());

        IncidentalResultVersionX incidentalResultVersion = daoMgr.getDAOBean().getIncidentalResultVersionXDAO()
                .findById(incidentalBinningJob.getListVersion());
        System.out.println(incidentalResultVersion.toString());
        System.out.println(incidentalBinningJob.getIncidentalBinX().toString());

        List<IncidentalBinGroupVersionX> incidentalBinGroupVersionXList = daoMgr.getDAOBean().getIncidentalBinGroupVersionXDAO()
                .findByIncidentalBinIdAndGroupVersion(incidentalBinningJob.getIncidentalBinX().getId(),
                        incidentalResultVersion.getIbinGroupVersion());

        Integer incidentalBinVersion = incidentalBinGroupVersionXList.get(0).getKey().getIncidentalBinVersion();

        List<IncidentalBinGeneX> incidentalBinGenes = daoMgr.getDAOBean().getIncidentalBinGeneXDAO()
                .findByIncidentalBinIdAndVersionAndZygosityModes(incidentalBinningJob.getIncidentalBinX().getId(), incidentalBinVersion,
                        zygosityModeList);

        assertTrue(CollectionUtils.isNotEmpty(incidentalBinGenes));

        for (IncidentalBinGeneX gene : incidentalBinGenes) {
            List<Variants_61_2> variants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                    .findByAssemblyIdAndSampleNameAndHGMDVersionAndMaxFrequencyThresholdAndGeneId(
                            incidentalBinningJob.getAssembly().getId(), incidentalBinningJob.getParticipant(),
                            incidentalResultVersion.getHgmdVersion(), 0.05, gene.getGene().getId());
            if (CollectionUtils.isNotEmpty(variants)) {
                variants.forEach(a -> System.out.println(a.toString()));
            }
        }
        long endTime = System.currentTimeMillis();

        // incidentalBinGenes.forEach(a -> System.out.println(a.toString()));
        System.out.println(incidentalBinGenes.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

}
