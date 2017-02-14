package org.renci.canvas.binning.core.diagnostic;

import java.util.concurrent.Callable;

import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.Report;
import org.renci.canvas.dao.clinbin.model.ReportPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGenerateReportCallable implements Callable<Report> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGenerateReportCallable.class);

    private CANVASDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public AbstractGenerateReportCallable(CANVASDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
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

            // hgmd
            report.setNumberOfHGMDKnownPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 1).intValue());
            report.setNumberOfHGMDLikelyPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 2).intValue());
            report.setNumberOfHGMDPossiblyPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 3).intValue());
            report.setNumberOfHGMDVariantsOfUncertainSignificance(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 4).intValue());
            report.setNumberOfHGMDLikelyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 5).intValue());
            report.setNumberOfHGMDAlmostCertainlyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 6).intValue());

            // clinvar
            report.setNumberOfClinVarKnownPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 1).intValue());
            report.setNumberOfClinVarLikelyPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 2).intValue());
            report.setNumberOfClinVarPossiblyPathenogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 3).intValue());
            report.setNumberOfClinVarVariantsOfUncertainSignificance(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 4).intValue());
            report.setNumberOfClinVarLikelyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 5).intValue());
            report.setNumberOfClinVarAlmostCertainlyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 6).intValue());

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

    public CANVASDAOBeanService getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(CANVASDAOBeanService daoBean) {
        this.daoBean = daoBean;
    }

    public DiagnosticBinningJob getBinningJob() {
        return binningJob;
    }

    public void setBinningJob(DiagnosticBinningJob binningJob) {
        this.binningJob = binningJob;
    }

}
