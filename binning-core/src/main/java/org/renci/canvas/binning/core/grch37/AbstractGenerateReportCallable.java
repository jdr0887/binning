package org.renci.canvas.binning.core.grch37;

import java.util.concurrent.Callable;

import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.Report;
import org.renci.canvas.dao.clinbin.model.ReportPK;
import org.renci.canvas.dao.refseq.Variants_61_2_DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGenerateReportCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGenerateReportCallable.class);

    private CANVASDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public AbstractGenerateReportCallable(CANVASDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public Void call() throws BinningException {
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
            report.setNumberOfHGMDKnownPathogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 1).intValue());
            report.setNumberOfHGMDLikelyPathogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 2).intValue());
            report.setNumberOfHGMDPossiblyPathogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 3).intValue());
            report.setNumberOfHGMDVariantsOfUncertainSignificance(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 4).intValue());
            report.setNumberOfHGMDLikelyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 5).intValue());
            report.setNumberOfHGMDAlmostCertainlyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndHGMDDiseaseClassId(binningJob.getAssembly().getId(), 6).intValue());

            // clinvar
            report.setNumberOfClinVarKnownPathogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 1).intValue());
            report.setNumberOfClinVarLikelyPathogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 2).intValue());
            report.setNumberOfClinVarPossiblyPathogenic(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 3).intValue());
            report.setNumberOfClinVarVariantsOfUncertainSignificance(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 4).intValue());
            report.setNumberOfClinVarLikelyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 5).intValue());
            report.setNumberOfClinVarAlmostCertainlyBenign(daoBean.getBinResultsFinalDiagnosticDAO()
                    .findByAssemblyIdAndClinVarDiseaseClassId(binningJob.getAssembly().getId(), 6).intValue());

            Variants_61_2_DAO variants_61_2_DAO = daoBean.getVariants_61_2_DAO();

            // location types
            report.setNumberOfTransriptDepLocatedVariants(
                    variants_61_2_DAO.findTranscriptDependentCount(binningJob.getAssembly().getId()).intValue());

            report.setNumberOfCodingLocatedVariants(variants_61_2_DAO.findCodingCount(binningJob.getAssembly().getId()).intValue());

            report.setNumberOfNonCodingLocatedVariants(variants_61_2_DAO.findNonCodingCount(binningJob.getAssembly().getId()).intValue());

            // variant types
            Integer substitutionTypeCount = variants_61_2_DAO.findByAssemblyIdAndVariantType(binningJob.getAssembly().getId(), "snp")
                    .intValue();
            substitutionTypeCount += variants_61_2_DAO.findByAssemblyIdAndVariantType(binningJob.getAssembly().getId(), "sub").intValue();
            report.setNumberOfSubstitutionTypes(substitutionTypeCount);

            Integer indelTypeCount = variants_61_2_DAO.findByAssemblyIdAndVariantType(binningJob.getAssembly().getId(), "ins").intValue();
            indelTypeCount += variants_61_2_DAO.findByAssemblyIdAndVariantType(binningJob.getAssembly().getId(), "del").intValue();
            report.setNumberOfIndelTypes(indelTypeCount);

            // variant effects
            report.setNumberOfIntergenicVariantEffects(
                    variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "intergenic").intValue());
            report.setNumberOfIntronicVariantEffects(
                    variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "intron").intValue());

            Integer untranslatedRegionCount = variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "UTR")
                    .intValue();
            untranslatedRegionCount += variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "UTR-3")
                    .intValue();
            untranslatedRegionCount += variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "UTR-5")
                    .intValue();
            untranslatedRegionCount += variants_61_2_DAO
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "splice-site-UTR").intValue();
            untranslatedRegionCount += variants_61_2_DAO
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "splice-site-UTR-5").intValue();
            untranslatedRegionCount += variants_61_2_DAO
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "splice-site-UTR-3").intValue();
            report.setNumberOfUntranslatedVariantEffects(untranslatedRegionCount);

            Integer synonymousCount = variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "synonymous")
                    .intValue();
            synonymousCount += variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "synonymous indel")
                    .intValue();
            report.setNumberOfSynonymousVariantEffects(synonymousCount);

            report.setNumberOfMissenseVariantEffects(
                    variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "missense").intValue());

            report.setNumberOfNonShiftIndelVariantEffects(variants_61_2_DAO
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "non-frameshifting indel").intValue());

            report.setNumberOfShiftIndelVariantEffects(
                    variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "frameshifting indel").intValue());

            Integer nonsenseCount = variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "nonsense")
                    .intValue();
            nonsenseCount += variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "nonsense indel")
                    .intValue();
            report.setNumberOfNonsenseVariantEffects(nonsenseCount);

            report.setNumberOfStoplossVariantEffects(
                    variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "stoploss").intValue());

            report.setNumberOfSpliceVariantEffects(
                    variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "splice-site").intValue());

            Integer otherCount = variants_61_2_DAO
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "potential RNA-editing site").intValue();
            otherCount += variants_61_2_DAO.findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "boundary-crossing indel")
                    .intValue();
            otherCount += variants_61_2_DAO
                    .findByAssemblyIdAndVariantEffect(binningJob.getAssembly().getId(), "noncoding boundary-crossing indel").intValue();
            report.setNumberOfOtherVariantEffects(otherCount);

            logger.info(report.toString());
            daoBean.getReportDAO().save(report);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return null;
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
