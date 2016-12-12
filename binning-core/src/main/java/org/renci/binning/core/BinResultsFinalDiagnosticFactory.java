package org.renci.binning.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnosticPK;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.clinbin.model.DiagnosticGene;
import org.renci.binning.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.binning.dao.clinbin.model.DiseaseClass;
import org.renci.binning.dao.clinbin.model.MaxFrequency;
import org.renci.binning.dao.clinbin.model.NCGenesFrequencies;
import org.renci.binning.dao.clinbin.model.NCGenesFrequenciesPK;
import org.renci.binning.dao.clinbin.model.UnimportantExon;
import org.renci.binning.dao.clinbin.model.UnimportantExonPK;
import org.renci.binning.dao.clinvar.model.AssertionRanking;
import org.renci.binning.dao.clinvar.model.ReferenceClinicalAssertions;
import org.renci.binning.dao.dbsnp.model.SNPMappingAgg;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantPK;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQCPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinResultsFinalDiagnosticFactory {

    private static final Logger logger = LoggerFactory.getLogger(BinResultsFinalDiagnosticFactory.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    public static List<BinResultsFinalDiagnostic> findHGMDKnownPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(1);
            Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

            for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(hgmdLocatedVariantList)) {
                    continue;
                }

                HGMDLocatedVariant hgmdLocatedVariant = null;

                boolean containsKnownPathogenic = hgmdLocatedVariantList.stream()
                        .anyMatch((s) -> s.getKey().getVersion().equals(2) && s.getTag().equals("DM"));
                if (!containsKnownPathogenic) {
                    continue;
                }
                hgmdLocatedVariant = hgmdLocatedVariantList.stream()
                        .filter((s) -> s.getKey().getVersion().equals(2) && s.getTag().equals("DM")).findAny().get();

                if (hgmdLocatedVariant == null) {
                    continue;
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                    logger.debug(snpMappingAgg.toString());
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }

                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                results.add(binResultsFinalDiagnostic);

            }
        }
        return results;
    }

    public static List<BinResultsFinalDiagnostic> findClinVarKnownPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findClinVarKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(1);
            Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

            DiagnosticResultVersion diagnosticResultVersion = daoMgr.getDAOBean().getDiagnosticResultVersionDAO()
                    .findById(diagnosticBinningJob.getListVersion());

            for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());

                List<ReferenceClinicalAssertions> foundReferenceClinicalAssersions = daoMgr.getDAOBean().getReferenceClinicalAssertionsDAO()
                        .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(variant.getLocatedVariant().getId(),
                                diagnosticResultVersion.getClinvarVersion().getId(),
                                Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));

                List<AssertionRanking> foundAssertionRankings = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {
                    for (ReferenceClinicalAssertions rca : foundReferenceClinicalAssersions) {
                        foundAssertionRankings.addAll(daoMgr.getDAOBean().getAssertionRankingDAO().findByAssertion(rca.getAccession()));
                    }
                }

                boolean containsKnownPathogenic = foundAssertionRankings.stream()
                        .anyMatch((s) -> s.getKey().getRank().equals(1) || s.getKey().getRank().equals(2));
                if (!containsKnownPathogenic) {
                    continue;
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                    logger.debug(snpMappingAgg.toString());
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }

                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, null, maxNCGenesFrequenciesVersion, snpMappingAgg);
                results.add(binResultsFinalDiagnostic);

            }
        }
        return results;
    }

    public static List<BinResultsFinalDiagnostic> findHGMDLikelyPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(2);
            Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

            for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    boolean containsKnownPathogenic = hgmdLocatedVariantList.stream()
                            .anyMatch((s) -> s.getKey().getVersion().equals(2) && s.getTag().equals("DM"));
                    if (containsKnownPathogenic) {
                        continue;
                    }
                    Optional<HGMDLocatedVariant> optionalHGMDLocVar = hgmdLocatedVariantList.stream()
                            .filter((s) -> !s.getKey().getVersion().equals(2) || !s.getTag().equals("DM")).findFirst();
                    if (optionalHGMDLocVar.isPresent()) {
                        hgmdLocatedVariant = optionalHGMDLocVar.get();
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01) {

                    List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                            .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                    if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                        continue;
                    }

                    DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                    if (!allowableVariantEffects.contains(variant.getVariantEffect().getName())) {
                        continue;
                    }

                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);

                }

            }

        }
        return results;
    }

    public static List<BinResultsFinalDiagnostic> findClinVarLikelyPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(2);
            Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

            for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    boolean containsKnownPathogenic = hgmdLocatedVariantList.stream()
                            .anyMatch((s) -> s.getKey().getVersion().equals(2) && s.getTag().equals("DM"));
                    if (containsKnownPathogenic) {
                        continue;
                    }
                    Optional<HGMDLocatedVariant> optionalHGMDLocVar = hgmdLocatedVariantList.stream()
                            .filter((s) -> !s.getKey().getVersion().equals(2) || !s.getTag().equals("DM")).findFirst();
                    if (optionalHGMDLocVar.isPresent()) {
                        hgmdLocatedVariant = optionalHGMDLocVar.get();
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01) {

                    List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                            .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                    if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                        continue;
                    }

                    DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                    if (!allowableVariantEffects.contains(variant.getVariantEffect().getName())) {
                        continue;
                    }

                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);

                }

            }

        }
        return results;
    }

    public static List<BinResultsFinalDiagnostic> findHGMDPossiblyPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("missense", "non-frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(3);
            Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

            varLoop: for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }
                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01 && allowableVariantEffects.contains(variant.getVariantEffect().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }
        }
        return results;
    }

    public static List<BinResultsFinalDiagnostic> findClinVarPossiblyPathogenic(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("missense", "non-frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(3);
            Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

            varLoop: for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }
                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());

                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01 && allowableVariantEffects.contains(variant.getVariantEffect().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }
        }
        return results;
    }

    public static List<BinResultsFinalDiagnostic> findHGMDUncertainSignificance(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(4);
        Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> filteredVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel", "missense", "non-frameshifting indel");

        List<String> allowableVariantEffects = Arrays.asList("synonymous", "synonymous indel", "intron", "splice-site-UTR-3",
                "splice-site-UTR-5", "splice-site-UTR", "potential RNA-editing site", "noncoding boundary-crossing indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            varLoop: for (Variants_61_2 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (filteredVariantEffects.contains(variant.getVariantEffect().getName())) {
                    continue;
                }

                List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR");

                if (maxFrequency.getMaxAlleleFreq() < 0.01 && (allowableVariantEffects.contains(variant.getVariantEffect().getName())
                        || allowableLocationTypes.contains(variant.getLocationType().getName()))) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

            varLoop: for (Variants_61_2 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

                if (maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                        && allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }
        return results;
    }

    public static List<BinResultsFinalDiagnostic> findClinVarUncertainSignificance(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(4);
        Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> filteredVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel", "missense", "non-frameshifting indel");

        List<String> allowableVariantEffects = Arrays.asList("synonymous", "synonymous indel", "intron", "splice-site-UTR-3",
                "splice-site-UTR-5", "splice-site-UTR", "potential RNA-editing site", "noncoding boundary-crossing indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            varLoop: for (Variants_61_2 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (filteredVariantEffects.contains(variant.getVariantEffect().getName())) {
                    continue;
                }

                List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR");

                if (maxFrequency.getMaxAlleleFreq() < 0.01 && (allowableVariantEffects.contains(variant.getVariantEffect().getName())
                        || allowableLocationTypes.contains(variant.getLocationType().getName()))) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

            varLoop: for (Variants_61_2 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

                if (maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                        && allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }
        return results;
    }

    public static List<BinResultsFinalDiagnostic> findHGMDLikelyBenign(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(5);
        Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {
            varLoop: for (Variants_61_2 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                    continue varLoop;
                }

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                        && allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() < 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    public static List<BinResultsFinalDiagnostic> findClinVarLikelyBenign(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(5);
        Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {
            varLoop: for (Variants_61_2 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                    continue varLoop;
                }

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                        && allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() < 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    public static List<BinResultsFinalDiagnostic> findHGMDAlmostCertainlyBenign(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(6);
        Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {
            varLoop: for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);
                if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                    continue varLoop;
                }

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() >= 0.1 && allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    public static List<BinResultsFinalDiagnostic> findClinVarAlmostCertainlyBenign(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        DiseaseClass diseaseClass = daoMgr.getDAOBean().getDiseaseClassDAO().findById(6);
        Integer maxNCGenesFrequenciesVersion = daoMgr.getDAOBean().getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {
            varLoop: for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoMgr.getDAOBean().getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getKey().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoMgr.getDAOBean().getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoMgr.getDAOBean().getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);
                if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                    continue varLoop;
                }

                List<DiagnosticGene> diagnosticGeneList = daoMgr.getDAOBean().getDiagnosticGeneDAO()
                        .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() >= 0.1 && allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getName())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    private static BinResultsFinalDiagnostic createBinResultsFinalDiagnostic(DiagnosticBinningJob diagnosticBinningJob,
            Variants_61_2 variant, DiseaseClass diseaseClass, DiagnosticGene diagnosticGene, MaxFrequency maxFrequency,
            HGMDLocatedVariant hgmdLocatedVariant, Integer maxNCGenesFrequenciesVersion, SNPMappingAgg snpMappingAgg)
            throws BinningDAOException {
        logger.debug(
                "ENTERING createBinResultsFinalDiagnostic(DiagnosticBinningJob, Variants_61_2, DiseaseClass, DiagnosticGene, MaxFrequency, HGMDLocatedVariant, Integer, SNPMappingAgg)");

        AssemblyLocatedVariant assemblyLocatedVariant = daoMgr.getDAOBean().getAssemblyLocatedVariantDAO()
                .findById(new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoMgr.getDAOBean().getAssemblyLocatedVariantQCDAO()
                .findById(new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        NCGenesFrequencies ncgenesFrequencies = daoMgr.getDAOBean().getNCGenesFrequenciesDAO()
                .findById(new NCGenesFrequenciesPK(variant.getLocatedVariant().getId(), maxNCGenesFrequenciesVersion.toString()));

        UnimportantExon unimportantExon = daoMgr.getDAOBean().getUnimportantExonDAO()
                .findById(new UnimportantExonPK(variant.getKey().getTranscript(), variant.getNonCanonicalExon()));

        DiagnosticResultVersion diagnosticResultVersion = daoMgr.getDAOBean().getDiagnosticResultVersionDAO()
                .findById(diagnosticBinningJob.getListVersion());

        BinResultsFinalDiagnosticPK binResultsFinalDiagnosticPK = new BinResultsFinalDiagnosticPK(diagnosticBinningJob.getParticipant(),
                diagnosticBinningJob.getListVersion(), diagnosticBinningJob.getDx().getId(), diagnosticBinningJob.getAssembly().getId(),
                variant.getLocatedVariant().getId(), variant.getKey().getMapNumber(), variant.getKey().getTranscript());

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = new BinResultsFinalDiagnostic(binResultsFinalDiagnosticPK, variant,
                diseaseClass, maxFrequency, diagnosticGene, hgmdLocatedVariant, assemblyLocatedVariant, assemblyLocatedVariantQC,
                ncgenesFrequencies, snpMappingAgg, unimportantExon);

        binResultsFinalDiagnostic.setLocatedVariant(variant.getLocatedVariant());
        binResultsFinalDiagnostic.setAssembly(diagnosticBinningJob.getAssembly());
        binResultsFinalDiagnostic.setDiseaseClass(diseaseClass);
        binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
        binResultsFinalDiagnostic.setDiagnosticResultVersion(diagnosticResultVersion);
        binResultsFinalDiagnostic.setDx(diagnosticBinningJob.getDx());
        binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());

        return binResultsFinalDiagnostic;
    }

}
