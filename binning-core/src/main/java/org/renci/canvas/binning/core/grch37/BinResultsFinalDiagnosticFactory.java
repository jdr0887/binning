package org.renci.canvas.binning.core.grch37;

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
import org.renci.canvas.dao.dbsnp.model.SNPMappingAgg;
import org.renci.canvas.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariant;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantPK;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQCPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinResultsFinalDiagnosticFactory {

    private static final Logger logger = LoggerFactory.getLogger(BinResultsFinalDiagnosticFactory.class);

    private static BinResultsFinalDiagnosticFactory instance;

    private CANVASDAOBeanService daoBean;

    private List<DiseaseClass> allDiseaseClasses;

    private Integer maxNCGenesFrequenciesVersion;

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

    public List<BinResultsFinalDiagnostic> findKnownPathogenic(DiagnosticBinningJob diagnosticBinningJob, List<Variants_61_2> variants)
            throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(CANVASDAOBeanService, DiagnosticBinningJob, List<Variants_61_2>)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(1)).findAny().get();

            for (Variants_61_2 variant : variants) {
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

                BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                results.add(binResultsFinalDiagnostic);

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findLikelyPathogenic(DiagnosticBinningJob diagnosticBinningJob, List<Variants_61_2> variants)
            throws CANVASDAOException {
        logger.debug("ENTERING findHGMDLikelyPathogenic(CANVASDAOBeanService, DiagnosticBinningJob, List<Variants_61_2>)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(2);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

            for (Variants_61_2 variant : variants) {
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

                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);

                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findPossiblyPathogenic(DiagnosticBinningJob diagnosticBinningJob, List<Variants_61_2> variants)
            throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableVariantEffects = Arrays.asList("missense", "non-frameshifting indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(3);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

            varLoop: for (Variants_61_2 variant : variants) {
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
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }
        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findUncertainSignificance(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(4);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

        List<String> filteredVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel", "missense", "non-frameshifting indel");

        List<String> allowableVariantEffects = Arrays.asList("synonymous", "synonymous indel", "intron", "splice-site-UTR-3",
                "splice-site-UTR-5", "splice-site-UTR", "potential RNA-editing site", "noncoding boundary-crossing indel");

        if (CollectionUtils.isNotEmpty(variants)) {

            varLoop: for (Variants_61_2 variant : variants) {
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
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

            varLoop: for (Variants_61_2 variant : variants) {
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
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }
        return results;
    }

    public List<BinResultsFinalDiagnostic> findLikelyBenign(DiagnosticBinningJob diagnosticBinningJob, List<Variants_61_2> variants)
            throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(5)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();
            logger.info(diagnosticResultVersion.toString());

            varLoop: for (Variants_61_2 variant : variants) {
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
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() < 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    public List<BinResultsFinalDiagnostic> findAlmostCertainlyBenign(DiagnosticBinningJob diagnosticBinningJob,
            List<Variants_61_2> variants) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(DiagnosticBinningJob)");
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

        if (CollectionUtils.isNotEmpty(variants)) {

            DiseaseClass diseaseClass = allDiseaseClasses.stream().filter(a -> a.getId().equals(6)).findAny().get();
            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();
            logger.info(diagnosticResultVersion.toString());

            varLoop: for (Variants_61_2 variant : variants) {
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
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

                if (maxFrequency.getMaxAlleleFreq() >= 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getId())) {
                    BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                            diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, snpMappingAgg);
                    results.add(binResultsFinalDiagnostic);
                }

            }

        }

        return results;
    }

    private BinResultsFinalDiagnostic createBinResultsFinalDiagnostic(DiagnosticBinningJob diagnosticBinningJob, Variants_61_2 variant,
            DiseaseClass hgmdDiseaseClass, DiagnosticGene diagnosticGene, MaxFrequency maxFrequency, HGMDLocatedVariant hgmdLocatedVariant,
            Integer maxNCGenesFrequenciesVersion, SNPMappingAgg snpMappingAgg) throws CANVASDAOException {
        logger.debug(
                "ENTERING createHGMDBinResultsFinalDiagnostic(DiagnosticBinningJob, Variants_61_2, DiseaseClass, DiagnosticGene, MaxFrequency, HGMDLocatedVariant, Integer, SNPMappingAgg)");

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
                diagnosticResultVersion.getId(), diagnosticBinningJob.getDx().getId(), diagnosticBinningJob.getAssembly().getId(),
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
