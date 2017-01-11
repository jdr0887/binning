package org.renci.binning.core;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.clinbin.model.Report;
import org.renci.binning.dao.clinbin.model.ReportPK;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateReportCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(GenerateReportCallableTest.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    public GenerateReportCallableTest() {
        super();
    }

    @Test
    public void test() throws BinningDAOException {

        DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4207);

        Long dxIdCount = daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO().findDXIdCount(diagnosticBinningJob.getParticipant());
        ReportPK reportPK = new ReportPK(diagnosticBinningJob.getParticipant(), dxIdCount.intValue());
        Report report = new Report(reportPK);

        Long analyzedVariantsCount = daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO()
                .findAnalyzedVariantsCount(diagnosticBinningJob.getParticipant());
        report.setNumberOfAnalyzedVariants(analyzedVariantsCount.intValue());

        report.setNumberOfHGMDKnownPathenogenic(daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO()
                .findByAssemblyIdAndHGMDDiseaseClassId(diagnosticBinningJob.getAssembly().getId(), 1).intValue());
        report.setNumberOfHGMDLikelyPathenogenic(daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO()
                .findByAssemblyIdAndHGMDDiseaseClassId(diagnosticBinningJob.getAssembly().getId(), 2).intValue());
        report.setNumberOfHGMDPossiblyPathenogenic(daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO()
                .findByAssemblyIdAndHGMDDiseaseClassId(diagnosticBinningJob.getAssembly().getId(), 3).intValue());
        report.setNumberOfHGMDVariantsOfUncertainSignificance(daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO()
                .findByAssemblyIdAndHGMDDiseaseClassId(diagnosticBinningJob.getAssembly().getId(), 4).intValue());
        report.setNumberOfHGMDLikelyBenign(daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO()
                .findByAssemblyIdAndHGMDDiseaseClassId(diagnosticBinningJob.getAssembly().getId(), 5).intValue());
        report.setNumberOfHGMDAlmostCertainlyBenign(daoMgr.getDAOBean().getBinResultsFinalDiagnosticDAO()
                .findByAssemblyIdAndHGMDDiseaseClassId(diagnosticBinningJob.getAssembly().getId(), 6).intValue());

        // location types
        report.setNumberOfTransriptDepLocatedVariants(daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findTranscriptDependentCount(diagnosticBinningJob.getAssembly().getId()).intValue());
        report.setNumberOfCodingLocatedVariants(
                daoMgr.getDAOBean().getVariants_61_2_DAO().findCodingCount(diagnosticBinningJob.getAssembly().getId()).intValue());
        report.setNumberOfNonCodingLocatedVariants(
                daoMgr.getDAOBean().getVariants_61_2_DAO().findNonCodingCount(diagnosticBinningJob.getAssembly().getId()).intValue());

        // variant types
        Integer substitutionTypeCount = daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantType(diagnosticBinningJob.getAssembly().getId(), "snp").intValue();
        substitutionTypeCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantType(diagnosticBinningJob.getAssembly().getId(), "sub").intValue();
        report.setNumberOfSubstitutionTypes(substitutionTypeCount);

        Integer indelTypeCount = daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantType(diagnosticBinningJob.getAssembly().getId(), "ins").intValue();
        indelTypeCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantType(diagnosticBinningJob.getAssembly().getId(), "del").intValue();
        report.setNumberOfIndelTypes(indelTypeCount);

        // variant effects
        report.setNumberOfIntergenicVariantEffects(daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "intergenic").intValue());
        report.setNumberOfIntronicVariantEffects(daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "intron").intValue());

        Integer untranslatedRegionCount = daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "UTR").intValue();
        untranslatedRegionCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "UTR-3").intValue();
        untranslatedRegionCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "UTR-5").intValue();
        untranslatedRegionCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "splice-site-UTR").intValue();
        untranslatedRegionCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "splice-site-UTR-5").intValue();
        untranslatedRegionCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "splice-site-UTR-3").intValue();
        report.setNumberOfUntranslatedVariantEffects(untranslatedRegionCount);

        Integer synonymousCount = daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "synonymous").intValue();
        synonymousCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "synonymous indel").intValue();
        report.setNumberOfSynonymousVariantEffects(synonymousCount);

        report.setNumberOfMissenseVariantEffects(daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "missense").intValue());

        report.setNumberOfNonShiftIndelVariantEffects(daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "non-frameshifting indel").intValue());

        report.setNumberOfShiftIndelVariantEffects(daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "frameshifting indel").intValue());

        Integer nonsenseCount = daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "nonsense").intValue();
        nonsenseCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "nonsense indel").intValue();
        report.setNumberOfNonsenseVariantEffects(nonsenseCount);

        report.setNumberOfStoplossVariantEffects(daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "stoploss").intValue());

        report.setNumberOfSpliceVariantEffects(daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "splice-site").intValue());

        Integer otherCount = daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "potential RNA-editing site").intValue();
        otherCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "boundary-crossing indel").intValue();
        otherCount += daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndVariantEffect(diagnosticBinningJob.getAssembly().getId(), "noncoding boundary-crossing indel")
                .intValue();
        report.setNumberOfOtherVariantEffects(otherCount);

        logger.info(report.toString());
    }

}
