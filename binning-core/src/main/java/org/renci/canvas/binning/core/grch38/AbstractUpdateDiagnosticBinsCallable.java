package org.renci.canvas.binning.core.grch38;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.apache.commons.collections.CollectionUtils;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.DiagnosticGene;
import org.renci.canvas.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.canvas.dao.clinbin.model.DiseaseClass;
import org.renci.canvas.dao.clinbin.model.MaxFrequency;
import org.renci.canvas.dao.clinbin.model.NCGenesFrequencies;
import org.renci.canvas.dao.clinbin.model.NCGenesFrequenciesPK;
import org.renci.canvas.dao.clinbin.model.UnimportantExon;
import org.renci.canvas.dao.clinbin.model.UnimportantExonPK;
import org.renci.canvas.dao.clinvar.model.ReferenceClinicalAssertion;
import org.renci.canvas.dao.dbsnp.model.SNPMappingAgg;
import org.renci.canvas.dao.dbsnp.model.SNPMappingAggPK;
import org.renci.canvas.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariant;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantPK;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQCPK;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUpdateDiagnosticBinsCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUpdateDiagnosticBinsCallable.class);

    private final List<Integer> knownPathogenicClinVarAssertionRankings = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);

    private final List<String> clinVarAssertionStatusExcludes = Arrays.asList("no assertion criteria provided", "no assertion provided",
            "not classified by submitter");

    private final List<String> clinvarLikelyPathogenicAllowableVariantEffects = Arrays.asList("nonsense", "splice-site",
            "boundary-crossing indel", "stoploss", "nonsense indel", "frameshifting indel");

    private final List<String> possiblyPathogenicAllowableVariantEffects = Arrays.asList("missense", "non-frameshifting indel");

    private final List<String> uncertainSignificanceAllowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR");

    private final List<String> uncertainSignificanceFilteredVariantEffects = Arrays.asList("nonsense", "splice-site",
            "boundary-crossing indel", "stoploss", "nonsense indel", "frameshifting indel", "missense", "non-frameshifting indel");

    private final List<String> uncertainSignificanceAllowableVariantEffects = Arrays.asList("synonymous", "synonymous indel", "intron",
            "splice-site-UTR-3", "splice-site-UTR-5", "splice-site-UTR", "potential RNA-editing site", "noncoding boundary-crossing indel");

    private List<DiseaseClass> allDiseaseClasses;

    private Integer maxNCGenesFrequenciesVersion;

    protected CANVASDAOBeanService daoBean;

    protected DiagnosticBinningJob diagnosticBinningJob;

    public AbstractUpdateDiagnosticBinsCallable(CANVASDAOBeanService daoBean, DiagnosticBinningJob diagnosticBinningJob) {
        super();
        this.daoBean = daoBean;
        this.diagnosticBinningJob = diagnosticBinningJob;
        try {
            this.allDiseaseClasses = daoBean.getDiseaseClassDAO().findAll();
            this.maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();
        } catch (CANVASDAOException e) {
            e.printStackTrace();
        }
    }

    public BinResultsFinalDiagnostic findHGMDKnownPathogenic(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDKnownPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(1)).findAny().get();

        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                .findByLocatedVariantId(locatedVariant37.getId());

        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {

            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.parallelStream()
                    .filter((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM")).findAny();

            if (optionalHGMDLocatedVariant.isPresent()) {

                HGMDLocatedVariant hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                logger.debug(hgmdLocatedVariant.toString());

                SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

                NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                        .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

                AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                        new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

                AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                        new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

                UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                        .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                        ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

        }

        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findClinVarKnownPathogenic(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarKnownPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(1)).findAny().get();
        DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.parallelStream()
                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
            if (containsKnownPathogenic) {

                SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

                NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                        .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

                foundReferenceClinicalAssersions.sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                ReferenceClinicalAssertion rca = foundReferenceClinicalAssersions.get(0);
                logger.info(rca.toString());

                AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                        new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

                AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                        new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

                UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                        .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                        assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

        }

        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDLikelyPathogenic(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDLikelyPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        List<String> allowableVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel");

        DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(2)).findAny().get();

        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                .findByLocatedVariantId(locatedVariant37.getId());

        HGMDLocatedVariant hgmdLocatedVariant = null;
        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
            boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                    .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
            if (containsKnownPathogenic) {
                return null;
            }
            Optional<HGMDLocatedVariant> optionalHGMDLocVar = hgmdLocatedVariantList.parallelStream()
                    .filter((s) -> !s.getId().getVersion().equals(2) || !s.getTag().equals("DM")).findFirst();
            if (optionalHGMDLocVar.isPresent()) {
                hgmdLocatedVariant = optionalHGMDLocVar.get();
                logger.debug(hgmdLocatedVariant.toString());
            }
        }

        if (maxFrequency.getMaxAlleleFreq() >= 0.01) {
            return null;
        }

        if (allowableVariantEffects.contains(variant.getVariantEffect().getId())) {

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                    ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findClinVarLikelyPathogenic(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarLikelyPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(2)).findAny().get();
        DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

        ReferenceClinicalAssertion rca = null;
        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
            if (containsKnownPathogenic) {
                return null;
            }
            foundReferenceClinicalAssersions.sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
            rca = foundReferenceClinicalAssersions.get(0);
            logger.debug(rca.toString());
        }

        if (maxFrequency.getMaxAlleleFreq() >= 0.01) {
            return null;
        }

        if (clinvarLikelyPathogenicAllowableVariantEffects.contains(variant.getVariantEffect().getId())) {

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                    assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

        }

        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDPossiblyPathogenic(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(3)).findAny().get();

        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                .findByLocatedVariantId(locatedVariant37.getId());

        HGMDLocatedVariant hgmdLocatedVariant = null;
        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
            boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                    .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
            if (containsKnownPathogenic) {
                return null;
            }
            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                    .filter(a -> !a.getId().getVersion().equals(2)).findFirst();
            if (optionalHGMDLocatedVariant.isPresent()) {
                hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                logger.debug(hgmdLocatedVariant.toString());
            }
        }

        if (maxFrequency.getMaxAlleleFreq() < 0.01
                && possiblyPathogenicAllowableVariantEffects.contains(variant.getVariantEffect().getId())) {

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                    ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findClinVarPossiblyPathogenic(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(3)).findAny().get();
        DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

        ReferenceClinicalAssertion rca = null;
        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {
            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
            if (containsKnownPathogenic) {
                return null;
            }
            foundReferenceClinicalAssersions.sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
            rca = foundReferenceClinicalAssersions.get(0);
        }

        if (maxFrequency.getMaxAlleleFreq() < 0.01
                && possiblyPathogenicAllowableVariantEffects.contains(variant.getVariantEffect().getId())) {

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                    assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDUncertainSignificance(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDUncertainSignificance(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(4)).findAny().get();

        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                .findByLocatedVariantId(locatedVariant37.getId());

        HGMDLocatedVariant hgmdLocatedVariant = null;
        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
            boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                    .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
            if (containsKnownPathogenic) {
                return null;
            }
            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                    .filter(a -> !a.getId().getVersion().equals(2)).findFirst();
            if (optionalHGMDLocatedVariant.isPresent()) {
                hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
            }
        }

        SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

        NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

        AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO()
                .findById(new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO()
                .findById(new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

        if (maxFrequency.getMaxAlleleFreq() < 0.01
                && (uncertainSignificanceAllowableVariantEffects.contains(variant.getVariantEffect().getId())
                        || uncertainSignificanceAllowableLocationTypes.contains(variant.getLocationType().getId()))) {

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                    ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

        }

        List<String> allowableLocationTypes = new ArrayList<>(Arrays.asList("exon", "intron/exon boundary"));
        allowableLocationTypes.addAll(uncertainSignificanceAllowableLocationTypes);

        if (binResultsFinalDiagnostic == null && maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                && allowableLocationTypes.contains(variant.getLocationType().getId())) {

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                    ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findClinVarUncertainSignificance(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarUncertainSignificance(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        try {
            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(4)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                    .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                            diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

            ReferenceClinicalAssertion rca = null;
            if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                        .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                if (containsKnownPathogenic) {
                    return null;
                }
                foundReferenceClinicalAssersions.sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                rca = foundReferenceClinicalAssersions.get(0);

            }

            if (uncertainSignificanceFilteredVariantEffects.contains(variant.getVariantEffect().getId())) {
                return null;
            }

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            if (maxFrequency.getMaxAlleleFreq() < 0.01
                    && (uncertainSignificanceAllowableVariantEffects.contains(variant.getVariantEffect().getId())
                            || uncertainSignificanceAllowableLocationTypes.contains(variant.getLocationType().getId()))) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                        assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

            List<String> allowableLocationTypes = new ArrayList<>(Arrays.asList("exon", "intron/exon boundary"));
            allowableLocationTypes.addAll(uncertainSignificanceAllowableLocationTypes);

            if (binResultsFinalDiagnostic == null && maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                    && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                        assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDLikelyBenign(Variants_80_4 variant, LocatedVariant locatedVariant37, MaxFrequency maxFrequency,
            DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDLikelyBenign(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        try {
            List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(5)).findAny().get();

            List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                    .findByLocatedVariantId(locatedVariant37.getId());

            HGMDLocatedVariant hgmdLocatedVariant = null;
            if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                        .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                if (containsKnownPathogenic) {
                    return null;
                }
                Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                        .filter(a -> !a.getId().getVersion().equals(2)).findFirst();
                if (optionalHGMDLocatedVariant.isPresent()) {
                    hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                }
            }

            if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                return null;
            }

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                    && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                        ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

            if (binResultsFinalDiagnostic == null && maxFrequency.getMaxAlleleFreq() < 0.05
                    && !allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                        ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findClinVarLikelyBenign(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarLikelyBenign(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        try {
            List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(5)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                    .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                            diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

            ReferenceClinicalAssertion rca = null;
            if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                        .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                if (containsKnownPathogenic) {
                    return null;
                }
                foundReferenceClinicalAssersions.sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                rca = foundReferenceClinicalAssersions.get(0);

            }

            if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                return null;
            }

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                    && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                        assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

            if (binResultsFinalDiagnostic == null && maxFrequency.getMaxAlleleFreq() < 0.05
                    && !allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                        assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDAlmostCertainlyBenign(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDAlmostCertainlyBenign(Variants_80_4, LocatedVariant, MaxFrequency, DiagnosticGene)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        try {
            List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(6)).findAny().get();

            List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                    .findByLocatedVariantId(locatedVariant37.getId());

            HGMDLocatedVariant hgmdLocatedVariant = null;
            if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                        .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                if (containsKnownPathogenic) {
                    return null;
                }
                Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                        .filter(a -> !a.getId().getVersion().equals(2)).findFirst();
                if (optionalHGMDLocatedVariant.isPresent()) {
                    hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                }
            }

            if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                return null;
            }

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            if (maxFrequency.getMaxAlleleFreq() >= 0.1 && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                        ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

            if (binResultsFinalDiagnostic == null
                    && (maxFrequency.getMaxAlleleFreq() >= 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getId()))) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg,
                        ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findClinVarAlmostCertainlyBenign(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarAlmostCertainlyBenign(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        try {
            List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(6)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                    .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                            diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

            ReferenceClinicalAssertion rca = null;
            if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                        .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                if (containsKnownPathogenic) {
                    return null;
                }
                foundReferenceClinicalAssersions.sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                rca = foundReferenceClinicalAssersions.get(0);

            }

            if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                return null;
            }

            SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

            NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                    .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

            AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO().findById(
                    new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO().findById(
                    new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

            UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                    .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

            if (maxFrequency.getMaxAlleleFreq() >= 0.1 && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                        assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }

            if (binResultsFinalDiagnostic == null && maxFrequency.getMaxAlleleFreq() >= 0.05
                    && !allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, rca, maxNCGenesFrequenciesVersion, snpMappingAgg, ncgenesFrequencies,
                        assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return binResultsFinalDiagnostic;
    }

}
