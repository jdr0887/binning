package org.renci.canvas.binning.core.grch38;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.canvas.dao.clinbin.model.BinResultsFinalDiagnosticPK;
import org.renci.canvas.dao.clinbin.model.DX;
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
import org.renci.canvas.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariant;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantPK;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQCPK;
import org.renci.canvas.dao.var.model.CanonicalAllele;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinResultsFinalDiagnosticFactory {

    private static final Logger logger = LoggerFactory.getLogger(BinResultsFinalDiagnosticFactory.class);

    private static BinResultsFinalDiagnosticFactory instance;

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

    private CANVASDAOBeanService daoBean;

    public static BinResultsFinalDiagnosticFactory getInstance(CANVASDAOBeanService daoBean) {
        if (instance == null) {
            instance = new BinResultsFinalDiagnosticFactory(daoBean);
        }
        return instance;
    }

    private BinResultsFinalDiagnosticFactory(CANVASDAOBeanService daoBean) {
        super();
        this.daoBean = daoBean;
        try {
            this.allDiseaseClasses = daoBean.getDiseaseClassDAO().findAll();
            this.maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();
        } catch (CANVASDAOException e) {
            e.printStackTrace();
        }
    }

    public List<BinResultsFinalDiagnostic> findHGMDKnownPathogenic(DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants)
            throws CANVASDAOException {
        logger.debug("ENTERING findHGMDKnownPathogenic(CANVASDAOBeanService, DiagnosticBinningJob, List<Variants_61_2>)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(1)).findAny().get();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        // we done't have hgmd data for 38, get from 37
                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                                .findByLocatedVariantId(locatedVariant37.getId());

                        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {

                            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.parallelStream()
                                    .filter((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM")).findAny();

                            if (optionalHGMDLocatedVariant.isPresent()) {

                                HGMDLocatedVariant hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                                logger.info(hgmdLocatedVariant.toString());

                                // should be able to find MaxFrequency by 38 LocatedVariant
                                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                                if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                                    MaxFrequency maxFrequency = maxFrequencyList.get(0);
                                    logger.info(locatedVariant37.toString());

                                    List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                            .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                                    if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                        DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                        logger.info(diagnosticGene.toString());

                                        List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                                .findByLocatedVariantId(locatedVariant37.getId());

                                        SNPMappingAgg snpMappingAgg = null;
                                        if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                            snpMappingAgg = snpMappingAggList.get(0);
                                            logger.info(snpMappingAgg.toString());
                                        }

                                        BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                                diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency,
                                                hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                                        results.add(binResultsFinalDiagnostic);

                                    }

                                }

                            }

                        }

                    }
                }

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarKnownPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(1)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

                        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.parallelStream()
                                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                            if (containsKnownPathogenic) {

                                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                                if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                                    MaxFrequency maxFrequency = maxFrequencyList.get(0);
                                    logger.info(maxFrequency.toString());

                                    List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                            .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                                    if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                        DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                        logger.info(diagnosticGene.toString());

                                        SNPMappingAgg snpMappingAgg = null;
                                        List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                                .findByLocatedVariantId(locatedVariant37.getId());
                                        if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                            snpMappingAgg = snpMappingAggList.get(0);
                                            logger.info(snpMappingAgg.toString());
                                        }

                                        foundReferenceClinicalAssersions
                                                .sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                                        ReferenceClinicalAssertion rca = foundReferenceClinicalAssersions.get(0);
                                        logger.info(rca.toString());

                                        BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                                diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                                maxNCGenesFrequenciesVersion, snpMappingAgg);
                                        results.add(binResultsFinalDiagnostic);

                                    }

                                }

                            }

                        }

                    }

                }
            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDLikelyPathogenic(DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants)
            throws CANVASDAOException {
        logger.debug("ENTERING findHGMDLikelyPathogenic(CANVASDAOBeanService, DiagnosticBinningJob, List<Variants_61_2>)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(2)).findAny().get();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                                .findByLocatedVariantId(locatedVariant37.getId());

                        HGMDLocatedVariant hgmdLocatedVariant = null;
                        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                            boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                                    .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            Optional<HGMDLocatedVariant> optionalHGMDLocVar = hgmdLocatedVariantList.parallelStream()
                                    .filter((s) -> !s.getId().getVersion().equals(2) || !s.getTag().equals("DM")).findFirst();
                            if (optionalHGMDLocVar.isPresent()) {
                                hgmdLocatedVariant = optionalHGMDLocVar.get();
                                logger.info(hgmdLocatedVariant.toString());
                            }
                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            if (maxFrequency.getMaxAlleleFreq() < 0.01) {

                                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                                if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                    DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                    logger.info(diagnosticGene.toString());

                                    if (allowableVariantEffects.contains(variant.getVariantEffect().getId())) {

                                        SNPMappingAgg snpMappingAgg = null;
                                        List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                                .findByLocatedVariantId(locatedVariant37.getId());
                                        if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                            snpMappingAgg = snpMappingAggList.get(0);
                                            logger.info(snpMappingAgg.toString());
                                        }

                                        BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                                diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency,
                                                hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                                        results.add(binResultsFinalDiagnostic);

                                    }

                                }

                            }

                        }

                    }
                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarLikelyPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarLikelyPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(2)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

                        ReferenceClinicalAssertion rca = null;
                        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            foundReferenceClinicalAssersions
                                    .sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                            rca = foundReferenceClinicalAssersions.get(0);
                            logger.info(rca.toString());
                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            if (maxFrequency.getMaxAlleleFreq() < 0.01) {

                                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                                if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                    DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                    logger.info(diagnosticGene.toString());

                                    if (clinvarLikelyPathogenicAllowableVariantEffects.contains(variant.getVariantEffect().getId())) {

                                        SNPMappingAgg snpMappingAgg = null;
                                        List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                                .findByLocatedVariantId(locatedVariant37.getId());
                                        if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                            snpMappingAgg = snpMappingAggList.get(0);
                                            logger.info(snpMappingAgg.toString());
                                        }

                                        BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                                diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                                maxNCGenesFrequenciesVersion, snpMappingAgg);
                                        results.add(binResultsFinalDiagnostic);

                                    }

                                }

                            }
                        }

                    }

                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDPossiblyPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(3)).findAny().get();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                                .findByLocatedVariantId(locatedVariant37.getId());

                        HGMDLocatedVariant hgmdLocatedVariant = null;
                        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                            boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                                    .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                                    .filter(a -> !a.getId().getVersion().equals(2)).findFirst();
                            if (optionalHGMDLocatedVariant.isPresent()) {
                                hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                                logger.info(hgmdLocatedVariant.toString());
                            }
                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                    .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                            if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                logger.info(diagnosticGene.toString());

                                if (maxFrequency.getMaxAlleleFreq() < 0.01
                                        && possiblyPathogenicAllowableVariantEffects.contains(variant.getVariantEffect().getId())) {

                                    List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                            .findByLocatedVariantId(locatedVariant37.getId());

                                    SNPMappingAgg snpMappingAgg = null;
                                    if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                        snpMappingAgg = snpMappingAggList.get(0);
                                        logger.info(snpMappingAgg.toString());
                                    }

                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);

                                }

                            }

                        }

                    }
                }

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarPossiblyPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(3)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

                        ReferenceClinicalAssertion rca = null;
                        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {
                            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            foundReferenceClinicalAssersions
                                    .sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                            rca = foundReferenceClinicalAssersions.get(0);
                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());
                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                    .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                            if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                logger.info(diagnosticGene.toString());

                                if (maxFrequency.getMaxAlleleFreq() < 0.01
                                        && possiblyPathogenicAllowableVariantEffects.contains(variant.getVariantEffect().getId())) {

                                    List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                            .findByLocatedVariantId(locatedVariant37.getId());

                                    SNPMappingAgg snpMappingAgg = null;
                                    if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                        snpMappingAgg = snpMappingAggList.get(0);
                                        logger.info(snpMappingAgg.toString());
                                    }

                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);

                                }
                            }

                        }

                    }
                }

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDUncertainSignificance(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDUncertainSignificance(DiagnosticBinningJob, List<Variants_80_4>)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(4)).findAny().get();

            for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                                .findByLocatedVariantId(locatedVariant37.getId());

                        HGMDLocatedVariant hgmdLocatedVariant = null;
                        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                            boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                                    .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                                    .filter(a -> !a.getId().getVersion().equals(2)).findFirst();
                            if (optionalHGMDLocatedVariant.isPresent()) {
                                hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                            }
                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                    .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                            if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                logger.info(diagnosticGene.toString());

                                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                        .findByLocatedVariantId(locatedVariant37.getId());

                                SNPMappingAgg snpMappingAgg = null;
                                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                    snpMappingAgg = snpMappingAggList.get(0);
                                    logger.info(snpMappingAgg.toString());
                                }

                                if (maxFrequency.getMaxAlleleFreq() < 0.01 && (uncertainSignificanceFilteredVariantEffects
                                        .contains(variant.getVariantEffect().getId())
                                        || uncertainSignificanceAllowableLocationTypes.contains(variant.getLocationType().getId()))) {

                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);

                                }

                                List<String> allowableLocationTypes = Arrays.asList("exon", "intron/exon boundary");
                                allowableLocationTypes.addAll(uncertainSignificanceAllowableLocationTypes);

                                if (maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                            }

                        }

                    }

                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarUncertainSignificance(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(4)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

                        ReferenceClinicalAssertion rca = null;
                        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            foundReferenceClinicalAssersions
                                    .sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                            rca = foundReferenceClinicalAssersions.get(0);

                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                    .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                            if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                logger.info(diagnosticGene.toString());

                                if (uncertainSignificanceFilteredVariantEffects.contains(variant.getVariantEffect().getId())) {
                                    continue;
                                }

                                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                        .findByLocatedVariantId(locatedVariant37.getId());

                                SNPMappingAgg snpMappingAgg = null;
                                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                    snpMappingAgg = snpMappingAggList.get(0);
                                    logger.info(snpMappingAgg.toString());
                                }

                                if (maxFrequency.getMaxAlleleFreq() < 0.01 && (uncertainSignificanceAllowableVariantEffects
                                        .contains(variant.getVariantEffect().getId())
                                        || uncertainSignificanceAllowableLocationTypes.contains(variant.getLocationType().getId()))) {

                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                                List<String> allowableLocationTypes = Arrays.asList("exon", "intron/exon boundary");
                                allowableLocationTypes.addAll(uncertainSignificanceAllowableLocationTypes);

                                if (maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                            }

                        }

                    }
                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDLikelyBenign(DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants)
            throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(5)).findAny().get();

            for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                                .findByLocatedVariantId(locatedVariant37.getId());

                        HGMDLocatedVariant hgmdLocatedVariant = null;
                        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                            boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                                    .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                                    .filter(a -> !a.getId().getVersion().equals(2)).findFirst();
                            if (optionalHGMDLocatedVariant.isPresent()) {
                                hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                            }
                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                                continue;
                            }

                            List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                    .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                            if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                logger.info(diagnosticGene.toString());

                                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                        .findByLocatedVariantId(locatedVariant37.getId());

                                SNPMappingAgg snpMappingAgg = null;
                                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                    snpMappingAgg = snpMappingAggList.get(0);
                                    logger.info(snpMappingAgg.toString());
                                }

                                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                                if (maxFrequency.getMaxAlleleFreq() < 0.05
                                        && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                            }

                        }

                    }

                }
            }

        }

        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarLikelyBenign(DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants)
            throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(5)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

                        ReferenceClinicalAssertion rca = null;
                        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            foundReferenceClinicalAssersions
                                    .sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                            rca = foundReferenceClinicalAssersions.get(0);

                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());
                        if (CollectionUtils.isEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                                continue;
                            }

                            List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                    .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                            if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {

                                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                                logger.info(diagnosticGene.toString());

                                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                        .findByLocatedVariantId(locatedVariant37.getId());
                                SNPMappingAgg snpMappingAgg = null;
                                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                    snpMappingAgg = snpMappingAggList.get(0);
                                    logger.info(snpMappingAgg.toString());
                                }

                                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                                if (maxFrequency.getMaxAlleleFreq() < 0.05
                                        && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                            }
                        }

                    }
                }

            }

        }

        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDAlmostCertainlyBenign(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(6)).findAny().get();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                                .findByLocatedVariantId(locatedVariant37.getId());

                        HGMDLocatedVariant hgmdLocatedVariant = null;
                        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                            boolean containsKnownPathogenic = hgmdLocatedVariantList.parallelStream()
                                    .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                                    .filter(a -> !a.getId().getVersion().equals(2)).findFirst();
                            if (optionalHGMDLocatedVariant.isPresent()) {
                                hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                            }
                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                                continue;
                            }

                            List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                    .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                            if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {
                                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                        .findByLocatedVariantId(locatedVariant37.getId());

                                SNPMappingAgg snpMappingAgg = null;
                                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                    snpMappingAgg = snpMappingAggList.get(0);
                                    logger.info(snpMappingAgg.toString());
                                }

                                if (maxFrequency.getMaxAlleleFreq() >= 0.1
                                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                                if (maxFrequency.getMaxAlleleFreq() >= 0.05
                                        && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }
                            }

                        }

                    }
                }

            }

        }

        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarAlmostCertainlyBenign(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(6)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);

                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

                        ReferenceClinicalAssertion rca = null;
                        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                            boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                            if (containsKnownPathogenic) {
                                continue;
                            }
                            foundReferenceClinicalAssersions
                                    .sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                            rca = foundReferenceClinicalAssersions.get(0);

                        }

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isNotEmpty(maxFrequencyList)) {

                            MaxFrequency maxFrequency = maxFrequencyList.get(0);
                            logger.info(maxFrequency.toString());

                            if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                                continue;
                            }

                            List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                    .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                            if (CollectionUtils.isNotEmpty(diagnosticGeneList)) {
                                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                                        .findByLocatedVariantId(locatedVariant37.getId());
                                SNPMappingAgg snpMappingAgg = null;
                                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                                    snpMappingAgg = snpMappingAggList.get(0);
                                    logger.info(snpMappingAgg.toString());
                                }

                                if (maxFrequency.getMaxAlleleFreq() >= 0.1
                                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }

                                if (maxFrequency.getMaxAlleleFreq() >= 0.05
                                        && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(
                                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, rca,
                                            maxNCGenesFrequenciesVersion, snpMappingAgg);
                                    results.add(binResultsFinalDiagnostic);
                                }
                            }
                        }

                    }

                }

            }

        }

        return results;
    }

    private BinResultsFinalDiagnostic createBinResultsFinalDiagnostic(DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant,
            DiseaseClass clinvarDiseaseClass, DiagnosticGene diagnosticGene, MaxFrequency maxFrequency, ReferenceClinicalAssertion rca,
            Integer maxNCGenesFrequenciesVersion, SNPMappingAgg snpMappingAgg) throws CANVASDAOException {
        logger.debug(
                "ENTERING createClinVarBinResultsFinalDiagnostic(DiagnosticBinningJob, Variants_61_2, DiseaseClass, DiagnosticGene, MaxFrequency, ReferenceClinicalAssertion, Integer, SNPMappingAgg)");

        AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO()
                .findById(new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO()
                .findById(new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                .findById(new NCGenesFrequenciesPK(variant.getLocatedVariant().getId(), maxNCGenesFrequenciesVersion.toString()));

        UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

        DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();
        DX dx = diagnosticBinningJob.getDx();

        BinResultsFinalDiagnosticPK binResultsFinalDiagnosticPK = new BinResultsFinalDiagnosticPK(diagnosticBinningJob.getParticipant(),
                diagnosticResultVersion.getId(), dx.getId(), diagnosticBinningJob.getAssembly().getId(),
                variant.getLocatedVariant().getId(), variant.getId().getMapNumber(), variant.getId().getTranscript());

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = new BinResultsFinalDiagnostic(binResultsFinalDiagnosticPK);
        binResultsFinalDiagnostic.setLocatedVariant(variant.getLocatedVariant());
        binResultsFinalDiagnostic.setAssembly(diagnosticBinningJob.getAssembly());
        binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
        binResultsFinalDiagnostic.setDiagnosticResultVersion(diagnosticResultVersion);
        binResultsFinalDiagnostic.setDx(dx);
        binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());
        binResultsFinalDiagnostic.setClinvarDiseaseClass(clinvarDiseaseClass);

        // variant stuff
        binResultsFinalDiagnostic.setChromosome(variant.getId().getGenomeRefSeq());
        binResultsFinalDiagnostic.setAlternateAllele(variant.getAlternateAllele());
        binResultsFinalDiagnostic.setAminoAcidStart(variant.getAminoAcidStart());
        binResultsFinalDiagnostic.setAminoAcidEnd(variant.getAminoAcidEnd());
        binResultsFinalDiagnostic.setCodingSequencePosition(variant.getCodingSequencePosition());
        binResultsFinalDiagnostic.setFinalAminoAcid(variant.getFinalAminoAcid());
        binResultsFinalDiagnostic.setFrameshift(variant.getFrameshift());
        binResultsFinalDiagnostic.setGeneId(variant.getGene().getId());
        binResultsFinalDiagnostic.setHgncGene(variant.getHgncGene());
        binResultsFinalDiagnostic.setHgvsCodingSequence(variant.getHgvsCodingSequence());
        binResultsFinalDiagnostic.setHgvsGenomic(variant.getHgvsGenomic());
        binResultsFinalDiagnostic.setHgvsProtein(variant.getHgvsProtein());
        binResultsFinalDiagnostic.setHgvsTranscript(variant.getHgvsTranscript());
        binResultsFinalDiagnostic.setInframe(variant.getInframe());
        binResultsFinalDiagnostic.setIntronExonDistance(variant.getIntronExonDistance());
        binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
        binResultsFinalDiagnostic.setNummaps(variant.getNumberOfTranscriptMaps());
        binResultsFinalDiagnostic.setOriginalAminoAcid(variant.getOriginalAminoAcid());
        binResultsFinalDiagnostic.setStrand(variant.getStrand());
        binResultsFinalDiagnostic.setPosition(variant.getId().getPosition());
        binResultsFinalDiagnostic.setReferenceAllele(variant.getReferenceAllele());
        binResultsFinalDiagnostic.setRefseqGene(variant.getRefSeqGene());
        binResultsFinalDiagnostic.setTranscriptPosition(variant.getTranscriptPosition());
        binResultsFinalDiagnostic.setType(variant.getVariantType().getId());
        binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());

        if (rca != null) {
            binResultsFinalDiagnostic.setClinvarAccession(rca.getAccession());
            binResultsFinalDiagnostic.setClinvarAssertion(rca.getAssertion());
        }

        if (assemblyLocatedVariant != null) {
            binResultsFinalDiagnostic.setHomozygous(assemblyLocatedVariant.getHomozygous());
            binResultsFinalDiagnostic.setGenotypeQual(assemblyLocatedVariant.getGenotypeQuality());
        }

        if (assemblyLocatedVariantQC != null) {
            binResultsFinalDiagnostic.setAltDepth(assemblyLocatedVariantQC.getAltDepth());
            binResultsFinalDiagnostic.setRefDepth(assemblyLocatedVariantQC.getRefDepth());
            binResultsFinalDiagnostic.setDepth(assemblyLocatedVariantQC.getDepth());
            binResultsFinalDiagnostic.setFracReadsWithDels(assemblyLocatedVariantQC.getFracReadsWithDels());
            binResultsFinalDiagnostic.setHrun(assemblyLocatedVariantQC.getHomopolymerRun());
            binResultsFinalDiagnostic.setGenotypeQual(assemblyLocatedVariantQC.getQualityByDepth());
            binResultsFinalDiagnostic.setStrandScore(assemblyLocatedVariantQC.getStrandScore());
            binResultsFinalDiagnostic.setReadPosRankSum(assemblyLocatedVariantQC.getReadPosRankSum());
        }

        if (ncgenesFrequencies != null) {
            binResultsFinalDiagnostic.setNCGenesAlternateFrequency(
                    ncgenesFrequencies.getAltAlleleFrequency() != null ? ncgenesFrequencies.getAltAlleleFrequency() : 0D);
            binResultsFinalDiagnostic.setNCGenesHWEP(ncgenesFrequencies.getHweP() != null ? ncgenesFrequencies.getHweP() : 1D);
        }

        if (maxFrequency != null) {
            binResultsFinalDiagnostic.setMaxAlleleFrequency(maxFrequency.getMaxAlleleFreq());
        }

        if (diagnosticGene != null) {
            binResultsFinalDiagnostic.setTier(diagnosticGene.getTier());
            binResultsFinalDiagnostic.setInheritance(diagnosticGene.getInheritance());
        }

        if (snpMappingAgg != null) {
            binResultsFinalDiagnostic.setRsId(snpMappingAgg.getRsIds());
        }

        if (unimportantExon != null) {
            binResultsFinalDiagnostic.setExonTruncationCount(unimportantExon.getCount() != null ? unimportantExon.getCount() : 0);
        }

        return binResultsFinalDiagnostic;
    }

    private BinResultsFinalDiagnostic createBinResultsFinalDiagnostic(DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant,
            DiseaseClass hgmdDiseaseClass, DiagnosticGene diagnosticGene, MaxFrequency maxFrequency, HGMDLocatedVariant hgmdLocatedVariant,
            Integer maxNCGenesFrequenciesVersion, SNPMappingAgg snpMappingAgg) throws CANVASDAOException {
        logger.debug(
                "ENTERING createHGMDBinResultsFinalDiagnostic(DiagnosticBinningJob, Variants_80_4, DiseaseClass, DiagnosticGene, MaxFrequency, HGMDLocatedVariant, Integer, SNPMappingAgg)");

        AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO()
                .findById(new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO()
                .findById(new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                .findById(new NCGenesFrequenciesPK(variant.getLocatedVariant().getId(), maxNCGenesFrequenciesVersion.toString()));

        UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

        DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();
        DX dx = diagnosticBinningJob.getDx();

        BinResultsFinalDiagnosticPK binResultsFinalDiagnosticPK = new BinResultsFinalDiagnosticPK(diagnosticBinningJob.getParticipant(),
                diagnosticResultVersion.getId(), dx.getId(), diagnosticBinningJob.getAssembly().getId(),
                variant.getLocatedVariant().getId(), variant.getId().getMapNumber(), variant.getId().getTranscript());

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = new BinResultsFinalDiagnostic(binResultsFinalDiagnosticPK);
        binResultsFinalDiagnostic.setLocatedVariant(variant.getLocatedVariant());
        binResultsFinalDiagnostic.setAssembly(diagnosticBinningJob.getAssembly());
        binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
        binResultsFinalDiagnostic.setDiagnosticResultVersion(diagnosticResultVersion);
        binResultsFinalDiagnostic.setDx(dx);
        binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());
        binResultsFinalDiagnostic.setHgmdDiseaseClass(hgmdDiseaseClass);

        // variant stuff
        binResultsFinalDiagnostic.setChromosome(variant.getId().getGenomeRefSeq());
        binResultsFinalDiagnostic.setAlternateAllele(variant.getAlternateAllele());
        binResultsFinalDiagnostic.setAminoAcidStart(variant.getAminoAcidStart());
        binResultsFinalDiagnostic.setAminoAcidEnd(variant.getAminoAcidEnd());
        binResultsFinalDiagnostic.setCodingSequencePosition(variant.getCodingSequencePosition());
        binResultsFinalDiagnostic.setFinalAminoAcid(variant.getFinalAminoAcid());
        binResultsFinalDiagnostic.setFrameshift(variant.getFrameshift());
        binResultsFinalDiagnostic.setGeneId(variant.getGene().getId());
        binResultsFinalDiagnostic.setHgncGene(variant.getHgncGene());
        binResultsFinalDiagnostic.setHgvsCodingSequence(variant.getHgvsCodingSequence());
        binResultsFinalDiagnostic.setHgvsGenomic(variant.getHgvsGenomic());
        binResultsFinalDiagnostic.setHgvsProtein(variant.getHgvsProtein());
        binResultsFinalDiagnostic.setHgvsTranscript(variant.getHgvsTranscript());
        binResultsFinalDiagnostic.setInframe(variant.getInframe());
        binResultsFinalDiagnostic.setIntronExonDistance(variant.getIntronExonDistance());
        binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
        binResultsFinalDiagnostic.setNummaps(variant.getNumberOfTranscriptMaps());
        binResultsFinalDiagnostic.setOriginalAminoAcid(variant.getOriginalAminoAcid());
        binResultsFinalDiagnostic.setStrand(variant.getStrand());
        binResultsFinalDiagnostic.setPosition(variant.getId().getPosition());
        binResultsFinalDiagnostic.setReferenceAllele(variant.getReferenceAllele());
        binResultsFinalDiagnostic.setRefseqGene(variant.getRefSeqGene());
        binResultsFinalDiagnostic.setTranscriptPosition(variant.getTranscriptPosition());
        binResultsFinalDiagnostic.setType(variant.getVariantType().getId());
        binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());

        if (hgmdLocatedVariant != null) {
            binResultsFinalDiagnostic.setHgmdAccessionNumber(hgmdLocatedVariant.getId().getAccession());
            binResultsFinalDiagnostic.setHgmdTag(hgmdLocatedVariant.getTag());
        }

        if (assemblyLocatedVariant != null) {
            binResultsFinalDiagnostic.setHomozygous(assemblyLocatedVariant.getHomozygous());
            binResultsFinalDiagnostic.setGenotypeQual(assemblyLocatedVariant.getGenotypeQuality());
        }

        if (assemblyLocatedVariantQC != null) {
            binResultsFinalDiagnostic.setAltDepth(assemblyLocatedVariantQC.getAltDepth());
            binResultsFinalDiagnostic.setRefDepth(assemblyLocatedVariantQC.getRefDepth());
            binResultsFinalDiagnostic.setDepth(assemblyLocatedVariantQC.getDepth());
            binResultsFinalDiagnostic.setFracReadsWithDels(assemblyLocatedVariantQC.getFracReadsWithDels());
            binResultsFinalDiagnostic.setHrun(assemblyLocatedVariantQC.getHomopolymerRun());
            binResultsFinalDiagnostic.setGenotypeQual(assemblyLocatedVariantQC.getQualityByDepth());
            binResultsFinalDiagnostic.setStrandScore(assemblyLocatedVariantQC.getStrandScore());
            binResultsFinalDiagnostic.setReadPosRankSum(assemblyLocatedVariantQC.getReadPosRankSum());
        }

        if (ncgenesFrequencies != null) {
            binResultsFinalDiagnostic.setNCGenesAlternateFrequency(
                    ncgenesFrequencies.getAltAlleleFrequency() != null ? ncgenesFrequencies.getAltAlleleFrequency() : 0D);
            binResultsFinalDiagnostic.setNCGenesHWEP(ncgenesFrequencies.getHweP() != null ? ncgenesFrequencies.getHweP() : 1D);
        }

        if (maxFrequency != null) {
            binResultsFinalDiagnostic.setMaxAlleleFrequency(maxFrequency.getMaxAlleleFreq());
        }

        if (diagnosticGene != null) {
            binResultsFinalDiagnostic.setTier(diagnosticGene.getTier());
            binResultsFinalDiagnostic.setInheritance(diagnosticGene.getInheritance());
        }

        if (snpMappingAgg != null) {
            binResultsFinalDiagnostic.setRsId(snpMappingAgg.getRsIds());
        }

        if (unimportantExon != null) {
            binResultsFinalDiagnostic.setExonTruncationCount(unimportantExon.getCount() != null ? unimportantExon.getCount() : 0);
        }

        return binResultsFinalDiagnostic;
    }

}
