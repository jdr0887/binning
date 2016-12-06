package org.renci.binning.diagnostic;

import java.util.concurrent.Callable;

import org.renci.binning.BinningException;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.clinbin.model.Report;
import org.renci.binning.dao.clinbin.model.ReportPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGenerateReportCallable implements Callable<Report> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGenerateReportCallable.class);

    private BinningDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public AbstractGenerateReportCallable(BinningDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public Report call() throws BinningException {
        logger.debug("ENTERING run()");

        Report report = null;
        try {

            Long dxIdCount = daoBean.getBinResultsFinalDiagnosticDAO().findDXIdCount(binningJob.getParticipant());
            ReportPK reportPK = new ReportPK(binningJob.getParticipant(), dxIdCount.intValue());

            Report foundReport = daoBean.getReportDAO().findById(reportPK);
            if (foundReport != null) {
                report = foundReport;
            } else {
                report = new Report(reportPK);
            }

            Long analyzedVariantsCount = daoBean.getBinResultsFinalDiagnosticDAO().findAnalyzedVariantsCount(binningJob.getParticipant());
            report.setNumberOfAnalyzedVariants(analyzedVariantsCount.intValue());

            report.setNumberOfKnownPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndDiseaseClassId(binningJob.getAssembly().getId(), 1).intValue());
            report.setNumberOfLikelyPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndDiseaseClassId(binningJob.getAssembly().getId(), 2).intValue());
            report.setNumberOfPossiblyPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndDiseaseClassId(binningJob.getAssembly().getId(), 3).intValue());
            report.setNumberOfVariantsOfUncertainSignificance(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndDiseaseClassId(binningJob.getAssembly().getId(), 4).intValue());
            report.setNumberOfLikelyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndDiseaseClassId(binningJob.getAssembly().getId(), 5).intValue());
            report.setNumberOfAlmostCertainlyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndDiseaseClassId(binningJob.getAssembly().getId(), 6).intValue());

            // location types
            report.setNumberOfTransriptDepLocatedVariants(
                    daoBean.getVariants_61_2_DAO().findTranscriptDependentCount(binningJob.getAssembly().getId()).intValue());
            report.setNumberOfCodingLocatedVariants(
                    daoBean.getVariants_61_2_DAO().findCodingCount(binningJob.getAssembly().getId()).intValue());
            report.setNumberOfNonCodingLocatedVariants(
                    daoBean.getVariants_61_2_DAO().findNonCodingCount(binningJob.getAssembly().getId()).intValue());

            // variant types
            Integer substitutionTypeCount = daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantType(binningJob.getAssembly().getId(), "snp").intValue();
            substitutionTypeCount += daoBean.getVariants_61_2_DAO().findByAssemblyIdAndVariantType(binningJob.getAssembly().getId(), "sub")
                    .intValue();
            report.setNumberOfSubstitutionTypes(substitutionTypeCount);

            Integer indelTypeCount = daoBean.getVariants_61_2_DAO().findByAssemblyIdAndVariantType(binningJob.getAssembly().getId(), "ins")
                    .intValue();
            indelTypeCount += daoBean.getVariants_61_2_DAO().findByAssemblyIdAndVariantType(binningJob.getAssembly().getId(), "del")
                    .intValue();
            report.setNumberOfIndelTypes(indelTypeCount);

            // variant effects
            report.setNumberOfIntergenicVariantEffects(daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "intergenic").intValue());
            report.setNumberOfIntronicVariantEffects(
                    daoBean.getVariants_61_2_DAO().findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "intron").intValue());

            Integer untranslatedRegionCount = daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "UTR").intValue();
            untranslatedRegionCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "UTR-3").intValue();
            untranslatedRegionCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "UTR-5").intValue();
            untranslatedRegionCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "splice-site-UTR").intValue();
            untranslatedRegionCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "splice-site-UTR-5").intValue();
            untranslatedRegionCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "splice-site-UTR-3").intValue();
            report.setNumberOfUntranslatedVariantEffects(untranslatedRegionCount);

            Integer synonymousCount = daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "synonymous").intValue();
            synonymousCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "synonymous indel").intValue();
            report.setNumberOfSynonymousVariantEffects(synonymousCount);

            report.setNumberOfMissenseVariantEffects(daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "missense").intValue());

            report.setNumberOfNonShiftIndelVariantEffects(daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "non-frameshifting indel").intValue());

            report.setNumberOfShiftIndelVariantEffects(daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "frameshifting indel").intValue());

            Integer nonsenseCount = daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "nonsense").intValue();
            nonsenseCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "nonsense indel").intValue();
            report.setNumberOfNonsenseVariantEffects(nonsenseCount);

            report.setNumberOfStoplossVariantEffects(daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "stoploss").intValue());

            report.setNumberOfSpliceVariantEffects(daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "splice-site").intValue());

            Integer otherCount = daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "potential RNA-editing site").intValue();
            otherCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "boundary-crossing indel").intValue();
            otherCount += daoBean.getVariants_61_2_DAO()
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "noncoding boundary-crossing indel").intValue();
            report.setNumberOfOtherVariantEffects(otherCount);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return report;
    }

    public BinningDAOBeanService getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(BinningDAOBeanService daoBean) {
        this.daoBean = daoBean;
    }

    public DiagnosticBinningJob getBinningJob() {
        return binningJob;
    }

    public void setBinningJob(DiagnosticBinningJob binningJob) {
        this.binningJob = binningJob;
    }

}
