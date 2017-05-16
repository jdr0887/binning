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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinResultsFinalDiagnosticFactory {

    private static final Logger logger = LoggerFactory.getLogger(BinResultsFinalDiagnosticFactory.class);

    private static BinResultsFinalDiagnosticFactory instance;

    private List<Integer> knownPathogenicClinVarAssertionRankings = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

    public static BinResultsFinalDiagnosticFactory getInstance() {
        if (instance == null) {
            instance = new BinResultsFinalDiagnosticFactory();
        }
        return instance;
    }

    private BinResultsFinalDiagnosticFactory() {
        super();
    }

    public List<BinResultsFinalDiagnostic> findHGMDKnownPathogenic(CANVASDAOBeanService daoBean, DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDKnownPathogenic(CANVASDAOBeanService, DiagnosticBinningJob, List<Variants_61_2>)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(1);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(hgmdLocatedVariantList)) {
                    continue;
                }

                HGMDLocatedVariant hgmdLocatedVariant = null;

                boolean containsKnownPathogenic = hgmdLocatedVariantList.stream()
                        .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                if (!containsKnownPathogenic) {
                    continue;
                }
                hgmdLocatedVariant = hgmdLocatedVariantList.stream()
                        .filter((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM")).findAny().get();

                if (hgmdLocatedVariant == null) {
                    continue;
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                    logger.debug(snpMappingAgg.toString());
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());

                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }

                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                        variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                        snpMappingAgg);
                results.add(binResultsFinalDiagnostic);

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarKnownPathogenic(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(1);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

            DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO()
                    .findById(diagnosticBinningJob.getListVersion());

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                        .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(variant.getLocatedVariant().getId(),
                                diagnosticResultVersion.getClinvarVersion().getId(),
                                Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));

                if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                    boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                            .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                    if (!containsKnownPathogenic) {
                        continue;
                    }

                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                    logger.debug(snpMappingAgg.toString());
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());

                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }

                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                        variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion, snpMappingAgg);
                results.add(binResultsFinalDiagnostic);

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDLikelyPathogenic(CANVASDAOBeanService daoBean, DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDLikelyPathogenic(CANVASDAOBeanService, DiagnosticBinningJob, List<Variants_61_2>)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(2);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    boolean containsKnownPathogenic = hgmdLocatedVariantList.stream()
                            .anyMatch((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM"));
                    if (containsKnownPathogenic) {
                        continue;
                    }
                    Optional<HGMDLocatedVariant> optionalHGMDLocVar = hgmdLocatedVariantList.stream()
                            .filter((s) -> !s.getId().getVersion().equals(2) || !s.getTag().equals("DM")).findFirst();
                    if (optionalHGMDLocVar.isPresent()) {
                        hgmdLocatedVariant = optionalHGMDLocVar.get();
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01) {

                    List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                            diagnosticBinningJob.getDx().getId());

                    if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                        continue;
                    }

                    DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                    if (!allowableVariantEffects.contains(variant.getVariantEffect().getId())) {
                        continue;
                    }

                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                            variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);

                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarLikelyPathogenic(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findClinVarLikelyPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(2);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

            DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO()
                    .findById(diagnosticBinningJob.getListVersion());

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                        .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(variant.getLocatedVariant().getId(),
                                diagnosticResultVersion.getClinvarVersion().getId(),
                                Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));

                if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                    boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                            .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                    if (containsKnownPathogenic) {
                        continue;
                    }

                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01) {

                    List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                            diagnosticBinningJob.getDx().getId());

                    if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                        continue;
                    }

                    DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                    if (!allowableVariantEffects.contains(variant.getVariantEffect().getId())) {
                        continue;
                    }

                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean,
                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);

                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDPossiblyPathogenic(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("missense", "non-frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(3);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

            varLoop: for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getId().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getId().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }
                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());

                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01 && allowableVariantEffects.contains(variant.getVariantEffect().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                            variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarPossiblyPathogenic(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("missense", "non-frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(3);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

            DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO()
                    .findById(diagnosticBinningJob.getListVersion());

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                        .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(variant.getLocatedVariant().getId(),
                                diagnosticResultVersion.getClinvarVersion().getId(),
                                Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));

                if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                    boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                            .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                    if (containsKnownPathogenic) {
                        continue;
                    }

                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }
                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01 && allowableVariantEffects.contains(variant.getVariantEffect().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean,
                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDUncertainSignificance(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(4);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> filteredVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel", "missense", "non-frameshifting indel");

        List<String> allowableVariantEffects = Arrays.asList("synonymous", "synonymous indel", "intron", "splice-site-UTR-3",
                "splice-site-UTR-5", "splice-site-UTR", "potential RNA-editing site", "noncoding boundary-crossing indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            varLoop: for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getId().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getId().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (filteredVariantEffects.contains(variant.getVariantEffect().getId())) {
                    continue;
                }

                List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR");

                if (maxFrequency.getMaxAlleleFreq() < 0.01 && (allowableVariantEffects.contains(variant.getVariantEffect().getId())
                        || allowableLocationTypes.contains(variant.getLocationType().getId()))) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                            variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

            varLoop: for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getId().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getId().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

                if (maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                            variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarUncertainSignificance(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(4);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> filteredVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel", "missense", "non-frameshifting indel");

        List<String> allowableVariantEffects = Arrays.asList("synonymous", "synonymous indel", "intron", "splice-site-UTR-3",
                "splice-site-UTR-5", "splice-site-UTR", "potential RNA-editing site", "noncoding boundary-crossing indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO()
                    .findById(diagnosticBinningJob.getListVersion());

            for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                        .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(variant.getLocatedVariant().getId(),
                                diagnosticResultVersion.getClinvarVersion().getId(),
                                Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));

                if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                    boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                            .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                    if (containsKnownPathogenic) {
                        continue;
                    }

                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (filteredVariantEffects.contains(variant.getVariantEffect().getId())) {
                    continue;
                }

                List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR");

                if (maxFrequency.getMaxAlleleFreq() < 0.01 && (allowableVariantEffects.contains(variant.getVariantEffect().getId())
                        || allowableLocationTypes.contains(variant.getLocationType().getId()))) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean,
                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

            for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                        .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(variant.getLocatedVariant().getId(),
                                diagnosticResultVersion.getClinvarVersion().getId(),
                                Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));

                if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                    boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                            .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                    if (containsKnownPathogenic) {
                        continue;
                    }

                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

                if (maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean,
                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDLikelyBenign(CANVASDAOBeanService daoBean, DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(5);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {
            varLoop: for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getId().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getId().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                    continue varLoop;
                }

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                            variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() < 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                            variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarLikelyBenign(CANVASDAOBeanService daoBean, DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(5);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO()
                    .findById(diagnosticBinningJob.getListVersion());

            for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());

                List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                        .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(variant.getLocatedVariant().getId(),
                                diagnosticResultVersion.getClinvarVersion().getId(),
                                Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));

                if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                    boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                            .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                    if (containsKnownPathogenic) {
                        continue;
                    }

                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);

                if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                    continue;
                }

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                        && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean,
                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() < 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean,
                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    public List<BinResultsFinalDiagnostic> findHGMDAlmostCertainlyBenign(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(6);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {
            varLoop: for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                HGMDLocatedVariant hgmdLocatedVariant = null;
                if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {
                    for (HGMDLocatedVariant hgmdLocVar : hgmdLocatedVariantList) {
                        if (hgmdLocVar.getTag().equals("DM") && hgmdLocVar.getId().getVersion().equals(2)) {
                            continue varLoop;
                        }
                        if (!hgmdLocVar.getId().getVersion().equals(2)) {
                            continue;
                        }
                        hgmdLocatedVariant = hgmdLocVar;
                        break;
                    }
                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue varLoop;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);
                if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                    continue varLoop;
                }

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() >= 0.1 && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                            variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createHGMDBinResultsFinalDiagnostic(daoBean, diagnosticBinningJob,
                            variant, diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    public List<BinResultsFinalDiagnostic> findClinVarAlmostCertainlyBenign(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, List<Variants_80_4> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(6);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO()
                    .findById(diagnosticBinningJob.getListVersion());

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());

                List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                        .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(variant.getLocatedVariant().getId(),
                                diagnosticResultVersion.getClinvarVersion().getId(),
                                Arrays.asList("no assertion criteria provided", "no assertion provided", "not classified by submitter"));

                if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

                    boolean containsKnownPathogenic = foundReferenceClinicalAssersions.stream()
                            .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));
                    if (containsKnownPathogenic) {
                        continue;
                    }

                }

                SNPMappingAgg snpMappingAgg = null;
                List<SNPMappingAgg> snpMappingAggList = daoBean.getSNPMappingAggDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());
                if (CollectionUtils.isNotEmpty(snpMappingAggList)) {
                    snpMappingAgg = snpMappingAggList.get(0);
                }

                List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isEmpty(maxFrequencyList)) {
                    continue;
                }

                MaxFrequency maxFrequency = maxFrequencyList.get(0);
                if (maxFrequency.getMaxAlleleFreq() < 0.01) {
                    continue;
                }

                List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO().findByGeneIdAndDXId(variant.getGene().getId(),
                        diagnosticBinningJob.getDx().getId());
                if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                    continue;
                }
                DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);

                if (maxFrequency.getMaxAlleleFreq() >= 0.1 && allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean,
                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createClinVarBinResultsFinalDiagnostic(daoBean,
                            diagnosticBinningJob, variant, diseaseClass, diagnosticGene, maxFrequency, maxNCGenesFrequenciesVersion,
                            snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    private BinResultsFinalDiagnostic createClinVarBinResultsFinalDiagnostic(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant, DiseaseClass clinvarDiseaseClass,
            DiagnosticGene diagnosticGene, MaxFrequency maxFrequency, Integer maxNCGenesFrequenciesVersion, SNPMappingAgg snpMappingAgg)
            throws CANVASDAOException {
        logger.debug(
                "ENTERING createClinVarBinResultsFinalDiagnostic(DiagnosticBinningJob, Variants_61_2, DiseaseClass, DiagnosticGene, MaxFrequency, Integer, SNPMappingAgg)");

        AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO()
                .findById(new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO()
                .findById(new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                .findById(new NCGenesFrequenciesPK(variant.getLocatedVariant().getId(), maxNCGenesFrequenciesVersion.toString()));

        UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

        DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO()
                .findById(diagnosticBinningJob.getListVersion());

        BinResultsFinalDiagnosticPK binResultsFinalDiagnosticPK = new BinResultsFinalDiagnosticPK(diagnosticBinningJob.getParticipant(),
                diagnosticBinningJob.getListVersion(), diagnosticBinningJob.getDx().getId(), diagnosticBinningJob.getAssembly().getId(),
                variant.getLocatedVariant().getId(), variant.getId().getMapNumber(), variant.getId().getTranscript());

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = new BinResultsFinalDiagnostic(binResultsFinalDiagnosticPK, variant, null,
                clinvarDiseaseClass, maxFrequency, diagnosticGene, null, assemblyLocatedVariant, assemblyLocatedVariantQC,
                ncgenesFrequencies, snpMappingAgg, unimportantExon);

        binResultsFinalDiagnostic.setLocatedVariant(variant.getLocatedVariant());
        binResultsFinalDiagnostic.setAssembly(diagnosticBinningJob.getAssembly());
        binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
        binResultsFinalDiagnostic.setDiagnosticResultVersion(diagnosticResultVersion);
        binResultsFinalDiagnostic.setDx(diagnosticBinningJob.getDx());
        binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());

        return binResultsFinalDiagnostic;
    }

    private BinResultsFinalDiagnostic createHGMDBinResultsFinalDiagnostic(CANVASDAOBeanService daoBean,
            DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant, DiseaseClass hgmdDiseaseClass, DiagnosticGene diagnosticGene,
            MaxFrequency maxFrequency, HGMDLocatedVariant hgmdLocatedVariant, Integer maxNCGenesFrequenciesVersion,
            SNPMappingAgg snpMappingAgg) throws CANVASDAOException {
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

        DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO()
                .findById(diagnosticBinningJob.getListVersion());

        BinResultsFinalDiagnosticPK binResultsFinalDiagnosticPK = new BinResultsFinalDiagnosticPK(diagnosticBinningJob.getParticipant(),
                diagnosticBinningJob.getListVersion(), diagnosticBinningJob.getDx().getId(), diagnosticBinningJob.getAssembly().getId(),
                variant.getLocatedVariant().getId(), variant.getId().getMapNumber(), variant.getId().getTranscript());

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = new BinResultsFinalDiagnostic(binResultsFinalDiagnosticPK, variant,
                hgmdDiseaseClass, null, maxFrequency, diagnosticGene, hgmdLocatedVariant, assemblyLocatedVariant, assemblyLocatedVariantQC,
                ncgenesFrequencies, snpMappingAgg, unimportantExon);

        binResultsFinalDiagnostic.setLocatedVariant(variant.getLocatedVariant());
        binResultsFinalDiagnostic.setAssembly(diagnosticBinningJob.getAssembly());
        binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
        binResultsFinalDiagnostic.setDiagnosticResultVersion(diagnosticResultVersion);
        binResultsFinalDiagnostic.setDx(diagnosticBinningJob.getDx());
        binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());

        return binResultsFinalDiagnostic;
    }

}
