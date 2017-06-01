package org.renci.binning.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.collections.CollectionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.canvas.binning.core.grch38.BinResultsFinalDiagnosticFactory;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.DiagnosticGene;
import org.renci.canvas.dao.clinbin.model.DiseaseClass;
import org.renci.canvas.dao.clinbin.model.MaxFrequency;
import org.renci.canvas.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.canvas.dao.jpa.CANVASDAOBeanServiceImpl;
import org.renci.canvas.dao.jpa.annotation.AnnotationGeneExternalIdDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.BinResultsFinalDiagnosticDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.DiagnosticBinningJobDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.DiagnosticGeneDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.DiagnosticResultVersionDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.DiseaseClassDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.MaxFrequencyDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.NCGenesFrequenciesDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.UnimportantExonDAOImpl;
import org.renci.canvas.dao.jpa.clinvar.ReferenceClinicalAssertionDAOImpl;
import org.renci.canvas.dao.jpa.dbsnp.SNPMappingAggDAOImpl;
import org.renci.canvas.dao.jpa.hgmd.HGMDLocatedVariantDAOImpl;
import org.renci.canvas.dao.jpa.hgnc.HGNCGeneDAOImpl;
import org.renci.canvas.dao.jpa.refseq.FeatureDAOImpl;
import org.renci.canvas.dao.jpa.refseq.LocationTypeDAOImpl;
import org.renci.canvas.dao.jpa.refseq.RefSeqCodingSequenceDAOImpl;
import org.renci.canvas.dao.jpa.refseq.RefSeqGeneDAOImpl;
import org.renci.canvas.dao.jpa.refseq.RegionGroupRegionDAOImpl;
import org.renci.canvas.dao.jpa.refseq.TranscriptMapsDAOImpl;
import org.renci.canvas.dao.jpa.refseq.TranscriptMapsExonsDAOImpl;
import org.renci.canvas.dao.jpa.refseq.VariantEffectDAOImpl;
import org.renci.canvas.dao.jpa.refseq.Variants_80_4_DAOImpl;
import org.renci.canvas.dao.jpa.var.AssemblyLocatedVariantDAOImpl;
import org.renci.canvas.dao.jpa.var.AssemblyLocatedVariantQCDAOImpl;
import org.renci.canvas.dao.jpa.var.CanonicalAlleleDAOImpl;
import org.renci.canvas.dao.jpa.var.LocatedVariantDAOImpl;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.CanonicalAllele;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateDiagnosticBinsCallable38Test {

    private static final Logger logger = LoggerFactory.getLogger(UpdateDiagnosticBinsCallable38Test.class);

    private static EntityManagerFactory emf;

    private static EntityManager em;

    private static CANVASDAOBeanServiceImpl daoBean = new CANVASDAOBeanServiceImpl();

    public UpdateDiagnosticBinsCallable38Test() {
        super();
    }

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("canvas_test", null);
        em = emf.createEntityManager();

        TranscriptMapsDAOImpl transcriptMapsDAO = new TranscriptMapsDAOImpl();
        transcriptMapsDAO.setEntityManager(em);
        daoBean.setTranscriptMapsDAO(transcriptMapsDAO);

        TranscriptMapsExonsDAOImpl transcriptMapsExonsDAO = new TranscriptMapsExonsDAOImpl();
        transcriptMapsExonsDAO.setEntityManager(em);
        daoBean.setTranscriptMapsExonsDAO(transcriptMapsExonsDAO);

        LocationTypeDAOImpl locationTypeDAO = new LocationTypeDAOImpl();
        locationTypeDAO.setEntityManager(em);
        daoBean.setLocationTypeDAO(locationTypeDAO);

        VariantEffectDAOImpl variantEffectDAO = new VariantEffectDAOImpl();
        variantEffectDAO.setEntityManager(em);
        daoBean.setVariantEffectDAO(variantEffectDAO);

        RefSeqGeneDAOImpl refSeqGeneDAO = new RefSeqGeneDAOImpl();
        refSeqGeneDAO.setEntityManager(em);
        daoBean.setRefSeqGeneDAO(refSeqGeneDAO);

        HGNCGeneDAOImpl hgncGeneDAO = new HGNCGeneDAOImpl();
        hgncGeneDAO.setEntityManager(em);
        daoBean.setHGNCGeneDAO(hgncGeneDAO);

        AnnotationGeneExternalIdDAOImpl annotationGeneExternalIdDAO = new AnnotationGeneExternalIdDAOImpl();
        annotationGeneExternalIdDAO.setEntityManager(em);
        daoBean.setAnnotationGeneExternalIdDAO(annotationGeneExternalIdDAO);

        RegionGroupRegionDAOImpl regionGroupRegionDAO = new RegionGroupRegionDAOImpl();
        regionGroupRegionDAO.setEntityManager(em);
        daoBean.setRegionGroupRegionDAO(regionGroupRegionDAO);

        RefSeqCodingSequenceDAOImpl refSeqCodingSequenceDAO = new RefSeqCodingSequenceDAOImpl();
        refSeqCodingSequenceDAO.setEntityManager(em);
        daoBean.setRefSeqCodingSequenceDAO(refSeqCodingSequenceDAO);

        FeatureDAOImpl featureDAO = new FeatureDAOImpl();
        featureDAO.setEntityManager(em);
        daoBean.setFeatureDAO(featureDAO);

        LocatedVariantDAOImpl locatedVariantDAO = new LocatedVariantDAOImpl();
        locatedVariantDAO.setEntityManager(em);
        daoBean.setLocatedVariantDAO(locatedVariantDAO);

        DiagnosticBinningJobDAOImpl diagnosticBinningJobDAO = new DiagnosticBinningJobDAOImpl();
        diagnosticBinningJobDAO.setEntityManager(em);
        daoBean.setDiagnosticBinningJobDAO(diagnosticBinningJobDAO);

        Variants_80_4_DAOImpl variants_80_4_DAO = new Variants_80_4_DAOImpl();
        variants_80_4_DAO.setEntityManager(em);
        daoBean.setVariants_80_4_DAO(variants_80_4_DAO);

        DiseaseClassDAOImpl diseaseClassDAO = new DiseaseClassDAOImpl();
        diseaseClassDAO.setEntityManager(em);
        daoBean.setDiseaseClassDAO(diseaseClassDAO);

        NCGenesFrequenciesDAOImpl ncgenesFrequenciesDAO = new NCGenesFrequenciesDAOImpl();
        ncgenesFrequenciesDAO.setEntityManager(em);
        daoBean.setNCGenesFrequenciesDAO(ncgenesFrequenciesDAO);

        DiagnosticResultVersionDAOImpl diagnosticResultVersionDAO = new DiagnosticResultVersionDAOImpl();
        diagnosticResultVersionDAO.setEntityManager(em);
        daoBean.setDiagnosticResultVersionDAO(diagnosticResultVersionDAO);

        ReferenceClinicalAssertionDAOImpl referenceClinicalAssertionDAO = new ReferenceClinicalAssertionDAOImpl();
        referenceClinicalAssertionDAO.setEntityManager(em);
        daoBean.setReferenceClinicalAssertionDAO(referenceClinicalAssertionDAO);

        CanonicalAlleleDAOImpl canonicalAlleleDAO = new CanonicalAlleleDAOImpl();
        canonicalAlleleDAO.setEntityManager(em);
        daoBean.setCanonicalAlleleDAO(canonicalAlleleDAO);

        MaxFrequencyDAOImpl maxFrequencyDAO = new MaxFrequencyDAOImpl();
        maxFrequencyDAO.setEntityManager(em);
        daoBean.setMaxFrequencyDAO(maxFrequencyDAO);

        DiagnosticGeneDAOImpl diagnosticGeneDAO = new DiagnosticGeneDAOImpl();
        diagnosticGeneDAO.setEntityManager(em);
        daoBean.setDiagnosticGeneDAO(diagnosticGeneDAO);

        SNPMappingAggDAOImpl snpMappingAggDAO = new SNPMappingAggDAOImpl();
        snpMappingAggDAO.setEntityManager(em);
        daoBean.setSNPMappingAggDAO(snpMappingAggDAO);

        HGMDLocatedVariantDAOImpl hgmdLocatedVariantDAO = new HGMDLocatedVariantDAOImpl();
        hgmdLocatedVariantDAO.setEntityManager(em);
        daoBean.setHGMDLocatedVariantDAO(hgmdLocatedVariantDAO);

        AssemblyLocatedVariantDAOImpl assemblyLocatedVariantDAO = new AssemblyLocatedVariantDAOImpl();
        assemblyLocatedVariantDAO.setEntityManager(em);
        daoBean.setAssemblyLocatedVariantDAO(assemblyLocatedVariantDAO);

        AssemblyLocatedVariantQCDAOImpl assemblyLocatedVariantQCDAO = new AssemblyLocatedVariantQCDAOImpl();
        assemblyLocatedVariantQCDAO.setEntityManager(em);
        daoBean.setAssemblyLocatedVariantQCDAO(assemblyLocatedVariantQCDAO);

        UnimportantExonDAOImpl unimportantExonDAO = new UnimportantExonDAOImpl();
        unimportantExonDAO.setEntityManager(em);
        daoBean.setUnimportantExonDAO(unimportantExonDAO);

        BinResultsFinalDiagnosticDAOImpl binResultsFinalDiagnosticDAO = new BinResultsFinalDiagnosticDAOImpl();
        binResultsFinalDiagnosticDAO.setEntityManager(em);
        daoBean.setBinResultsFinalDiagnosticDAO(binResultsFinalDiagnosticDAO);

    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void scratch() throws CANVASDAOException {

        DiagnosticBinningJob diagnosticBinningJob = daoBean.getDiagnosticBinningJobDAO().findById(5003);

        // LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(552701360L);
        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(560126601L);

        List<Variants_80_4> foundVariants = daoBean.getVariants_80_4_DAO().findByLocatedVariantId(locatedVariant.getId());
        if (CollectionUtils.isNotEmpty(foundVariants)) {
            for (Variants_80_4 variant : foundVariants) {

                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                        .findByLocatedVariantId(variant.getLocatedVariant().getId());

                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {

                    CanonicalAllele canonicalAllele = foundCanonicalAlleles.get(0);
                    Optional<LocatedVariant> optionalLocatedVariant = canonicalAllele.getLocatedVariants().stream()
                            .filter(a -> a.getGenomeRef().getId().equals(2)).findAny();

                    if (optionalLocatedVariant.isPresent()) {

                        LocatedVariant locatedVariant37 = optionalLocatedVariant.get();
                        logger.info(locatedVariant37.toString());

                        List<MaxFrequency> maxFrequencyList = daoBean.getMaxFrequencyDAO()
                                .findByLocatedVariantId(variant.getLocatedVariant().getId());

                        if (CollectionUtils.isEmpty(maxFrequencyList)) {
                            // if no MaxFrequency...can't bin
                            continue;
                        }

                        MaxFrequency maxFrequency = maxFrequencyList.get(0);
                        logger.debug(maxFrequency.toString());

                        List<DiagnosticGene> diagnosticGeneList = daoBean.getDiagnosticGeneDAO()
                                .findByGeneIdAndDXId(variant.getGene().getId(), diagnosticBinningJob.getDx().getId());
                        if (CollectionUtils.isEmpty(diagnosticGeneList)) {
                            // if doesn't match a DiagnosticGene...don't use it
                            continue;
                        }

                        DiagnosticGene diagnosticGene = diagnosticGeneList.get(0);
                        logger.debug(diagnosticGene.toString());

                        BinResultsFinalDiagnostic binResultsFinalDiagnostic = findHGMDKnownPathogenic(diagnosticBinningJob, variant,
                                locatedVariant37, maxFrequency, diagnosticGene);
                        if (binResultsFinalDiagnostic != null) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getId(), 1);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info(binResultsFinalDiagnostic.toString());
                                // daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                            continue;
                        }

                        // hgmd - likely pathogenic(2)
                        binResultsFinalDiagnostic = findHGMDLikelyPathogenic(diagnosticBinningJob, variant, locatedVariant37, maxFrequency,
                                diagnosticGene);
                        if (binResultsFinalDiagnostic != null) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getId(), 2);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info(binResultsFinalDiagnostic.toString());
                                // daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                            continue;
                        }

                        // hgmd - possibly pathogenic(3)
                        binResultsFinalDiagnostic = findHGMDPossiblyPathogenic(diagnosticBinningJob, variant, locatedVariant37,
                                maxFrequency, diagnosticGene);
                        if (binResultsFinalDiagnostic != null) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getId(), 3);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info(binResultsFinalDiagnostic.toString());
                                // daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                            continue;
                        }

                        // hgmd - uncertain significance(4)
                        binResultsFinalDiagnostic = findHGMDUncertainSignificance(diagnosticBinningJob, variant, locatedVariant37,
                                maxFrequency, diagnosticGene);
                        if (binResultsFinalDiagnostic != null) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getId(), 4);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info(binResultsFinalDiagnostic.toString());
                                // daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                            continue;
                        }

                        // hgmd - likely benign(5)
                        binResultsFinalDiagnostic = findHGMDLikelyBenign(diagnosticBinningJob, variant, locatedVariant37, maxFrequency,
                                diagnosticGene);
                        if (binResultsFinalDiagnostic != null) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getId(), 5);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info(binResultsFinalDiagnostic.toString());
                                // daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                            continue;
                        }

                        // hgmd almost certainly benign(6)
                        binResultsFinalDiagnostic = findHGMDAlmostCertainlyBenign(diagnosticBinningJob, variant, locatedVariant37,
                                maxFrequency, diagnosticGene);
                        if (binResultsFinalDiagnostic != null) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getId(), 6);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info(binResultsFinalDiagnostic.toString());
                                // daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                            continue;
                        }

                        // BinResultsFinalDiagnostic binResultsFinalDiagnostic = binResultsFinalDiagnosticFactory
                        // .findClinVarKnownPathogenic(diagnosticBinningJob, variant);

                        // BinResultsFinalDiagnostic binResultsFinalDiagnostic = binResultsFinalDiagnosticFactory
                        // .findHGMDKnownPathogenic(diagnosticBinningJob, variant, locatedVariant37);

                        // BinResultsFinalDiagnostic binResultsFinalDiagnostic = binResultsFinalDiagnosticFactory
                        // .findHGMDAlmostCertainlyBenign(diagnosticBinningJob, variant, locatedVariant37);
                        //
                        // assertTrue(binResultsFinalDiagnostic != null);

                    }
                }
            }
        }
    }

    public BinResultsFinalDiagnostic findHGMDUncertainSignificance(DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant,
            LocatedVariant locatedVariant37, MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDUncertainSignificance(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(4);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

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

        List<String> uncertainSignificanceAllowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR");

        List<String> uncertainSignificanceAllowableVariantEffects = Arrays.asList("synonymous", "synonymous indel", "intron",
                "splice-site-UTR-3", "splice-site-UTR-5", "splice-site-UTR", "potential RNA-editing site",
                "noncoding boundary-crossing indel");

        if (maxFrequency.getMaxAlleleFreq() < 0.01
                && (uncertainSignificanceAllowableVariantEffects.contains(variant.getVariantEffect().getId())
                        || uncertainSignificanceAllowableLocationTypes.contains(variant.getLocationType().getId()))) {

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null, null,
                    null);

        }

        List<String> allowableLocationTypes = new ArrayList<>(Arrays.asList("exon", "intron/exon boundary"));
        allowableLocationTypes.addAll(uncertainSignificanceAllowableLocationTypes);

        if (binResultsFinalDiagnostic == null && maxFrequency.getMaxAlleleFreq() >= 0.01 && maxFrequency.getMaxAlleleFreq() < 0.05
                && allowableLocationTypes.contains(variant.getLocationType().getId())) {

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null, null,
                    null);

        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDKnownPathogenic(DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant,
            LocatedVariant locatedVariant37, MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDKnownPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(1);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                .findByLocatedVariantId(locatedVariant37.getId());

        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {

            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.parallelStream()
                    .filter((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM")).findAny();

            if (optionalHGMDLocatedVariant.isPresent()) {

                HGMDLocatedVariant hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                logger.debug(hgmdLocatedVariant.toString());

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null,
                        null, null);

            }

        }

        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDLikelyPathogenic(DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant,
            LocatedVariant locatedVariant37, MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDLikelyPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        List<String> allowableVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel", "stoploss",
                "nonsense indel", "frameshifting indel");

        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(2);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

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

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null, null,
                    null);

        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDPossiblyPathogenic(DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant,
            LocatedVariant locatedVariant37, MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findKnownPathogenic(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        List<String> possiblyPathogenicAllowableVariantEffects = Arrays.asList("missense", "non-frameshifting indel");

        DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(3);
        Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

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

            binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                    diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null, null,
                    null);

        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDLikelyBenign(DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant,
            LocatedVariant locatedVariant37, MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDLikelyBenign(Variants_80_4, LocatedVariant)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        try {
            List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(5);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

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

            if (maxFrequency.getMaxAlleleFreq() >= 0.05 && maxFrequency.getMaxAlleleFreq() < 0.1
                    && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null,
                        null, null);

            }

            if (binResultsFinalDiagnostic == null && maxFrequency.getMaxAlleleFreq() < 0.05
                    && !allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null,
                        null, null);

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return binResultsFinalDiagnostic;
    }

    public BinResultsFinalDiagnostic findHGMDAlmostCertainlyBenign(DiagnosticBinningJob diagnosticBinningJob, Variants_80_4 variant,
            LocatedVariant locatedVariant37, MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        logger.debug("ENTERING findHGMDAlmostCertainlyBenign(Variants_80_4, LocatedVariant, MaxFrequency, DiagnosticGene)");
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = null;

        try {
            List<String> allowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon", "intron/exon boundary");

            DiseaseClass diseaseClass = daoBean.getDiseaseClassDAO().findById(6);
            Integer maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();

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

            if (maxFrequency.getMaxAlleleFreq() >= 0.1 && allowableLocationTypes.contains(variant.getLocationType().getId())) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null,
                        null, null);

            }

            if (binResultsFinalDiagnostic == null
                    && (maxFrequency.getMaxAlleleFreq() >= 0.05 && !allowableLocationTypes.contains(variant.getLocationType().getId()))) {

                binResultsFinalDiagnostic = BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant,
                        diseaseClass, diagnosticGene, maxFrequency, hgmdLocatedVariant, maxNCGenesFrequenciesVersion, null, null, null,
                        null, null);

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return binResultsFinalDiagnostic;
    }

}
