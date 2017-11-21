package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.canvas.binning.core.grch38.VariantsFactory;
import org.renci.canvas.dao.jpa.CANVASDAOBeanServiceImpl;
import org.renci.canvas.dao.jpa.annotation.AnnotationGeneExternalIdDAOImpl;
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
import org.renci.canvas.dao.jpa.var.LocatedVariantDAOImpl;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariants38Test {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariants38Test.class);

    private static EntityManagerFactory emf;

    private static EntityManager em;

    private static CANVASDAOBeanServiceImpl daoBean = new CANVASDAOBeanServiceImpl();

    public AnnotateVariants38Test() {
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

        Variants_80_4_DAOImpl variants_80_4_DAO = new Variants_80_4_DAOImpl();
        variants_80_4_DAO.setEntityManager(em);
        daoBean.setVariants_80_4_DAO(variants_80_4_DAO);

    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

    private List<Variants_80_4> annotateLocatedVariant(LocatedVariant locatedVariant) throws Exception {
        List<Variants_80_4> variants = new ArrayList<>();
        VariantsFactory variantsFactory = VariantsFactory.getInstance(daoBean);
        variants.addAll(variantsFactory.annotateVariant(locatedVariant, "80", 4, daoBean));
        return variants;
    }

    @Test
    public void testLocatedVariant492040380() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(492040380L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_000249.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(492040380L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000003.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(37001040));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_000249.3"));
        assertTrue(variant.getRefSeqGene().equals("MLH1"));
        assertTrue(variant.getHgncGene().equals("MLH1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(491));
        assertTrue(variant.getCodingSequencePosition().equals(293));
        assertTrue(variant.getAminoAcidStart().equals(98));
        assertTrue(variant.getAminoAcidEnd().equals(101));
        assertTrue(variant.getOriginalAminoAcid().equals("GFRGE"));
        assertTrue(variant.getFinalAminoAcid().equals("E"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-14));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(17331));
        assertTrue(variant.getReferenceAllele().equals("GCTTTCGAGGTG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000003.12:g.37001040_37001051delGCTTTCGAGGTG"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000249.3:c.293_304delGCTTTCGAGGTG"));
        assertTrue(variant.getHgvsTranscript().equals("NM_000249.3:g.491_502delGCTTTCGAGGTG"));
        assertTrue(variant.getHgvsProtein().equals("NP_000240.1:p.Gly98_Gly101del"));

    }

    @Test
    public void testLocatedVariant514754385() throws Exception {
        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(514754385L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 12);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_033056.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(514754385L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(53822755));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getId().getTranscript().equals("NM_033056.3"));
        assertTrue(variant.getRefSeqGene().equals("PCDH15"));
        assertTrue(variant.getHgncGene().equals("PCDH15"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(5366));
        assertTrue(variant.getCodingSequencePosition().equals(4971));
        assertTrue(variant.getAminoAcidStart().equals(1657));
        assertTrue(variant.getAminoAcidEnd().equals(1658));
        assertTrue(variant.getOriginalAminoAcid().equals("SS"));
        assertTrue(variant.getFinalAminoAcid().equals("SSS"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(604));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(21269));
        assertTrue(variant.getAlternateAllele().equals("GAG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.11:g.53822755_53822756insGAG"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_033056.3:c.4970_4971insCTC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_033056.3:g.5365_5366insCTC"));
        assertTrue(variant.getHgvsProtein().equals("NP_149045.3:p.Ser1657_Ser1658delinsSerSerSer"));

    }

    @Test
    public void testLocatedVariant509290127() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(509290127L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001042465.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(509290127L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(71818686));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getId().getTranscript().equals("NM_001042465.2"));
        assertTrue(variant.getRefSeqGene().equals("PSAP"));
        assertTrue(variant.getHgncGene().equals("PSAP"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1633));
        assertTrue(variant.getCodingSequencePosition().equals(1479));
        assertTrue(variant.getAminoAcidStart().equals(493));
        assertTrue(variant.getAminoAcidEnd().equals(493));
        assertTrue(variant.getOriginalAminoAcid().equals("L"));
        assertTrue(variant.getFinalAminoAcid().equals("L"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(37));
        assertTrue(variant.getId().getVariantEffect().equals("synonymous indel"));
        assertTrue(variant.getGene().getId().equals(23025));
        assertTrue(variant.getAlternateAllele().equals("GAG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.11:g.71818686_71818688delinsGAG"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001042465.2:c.1477_1479delinsCTC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001042465.2:g.1631_1633delinsCTC"));
        assertTrue(variant.getHgvsProtein().equals("NP_001035930.1:p.Leu493delinsLeu"));

    }

    @Test
    public void testLocatedVariant521572416() throws Exception {
        // snp missense
        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(521572416L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001135091.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(521572416L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(26565752));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001135091.1"));
        assertTrue(variant.getRefSeqGene().equals("MUC15"));
        assertTrue(variant.getHgncGene().equals("MUC15"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(461));
        assertTrue(variant.getCodingSequencePosition().equals(188));
        assertTrue(variant.getAminoAcidStart().equals(63));
        assertTrue(variant.getAminoAcidEnd().equals(64));
        assertTrue(variant.getOriginalAminoAcid().equals("A"));
        assertTrue(variant.getFinalAminoAcid().equals("E"));
        assertTrue(variant.getIntronExonDistance().equals(145));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getGene().getId().equals(18178));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.26565752G>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001135091.1:c.188C>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001135091.1:g.461C>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001128563.1:p.Ala63Glu"));

    }

    @Test
    public void testLocatedVariant515075694() throws Exception {
        // snp stoploss
        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(515075694L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_020346.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(515075694L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(22377739));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_020346.2"));
        assertTrue(variant.getRefSeqGene().equals("SLC17A6"));
        assertTrue(variant.getHgncGene().equals("SLC17A6"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(2161));
        assertTrue(variant.getCodingSequencePosition().equals(1748));
        assertTrue(variant.getAminoAcidStart().equals(583));
        assertTrue(variant.getAminoAcidEnd().equals(584));
        assertTrue(variant.getOriginalAminoAcid().equals("*"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getId().getVariantEffect().equals("stoploss"));
        assertTrue(variant.getGene().getId().equals(27913));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.22377739A>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_020346.2:c.1748A>C"));
        assertTrue(variant.getHgvsTranscript().equals("NM_020346.2:g.2161A>C"));
        assertTrue(variant.getHgvsProtein().equals("NP_065079.1:p.*583Ser"));

    }

    @Test
    public void testLocatedVariant492019779() throws Exception {
        // snp synonymous

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(492019779L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_145232.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(492019779L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000019.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(51098958));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_145232.3"));
        assertTrue(variant.getRefSeqGene().equals("CTU1"));
        assertTrue(variant.getHgncGene().equals("CTU1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(755));
        assertTrue(variant.getCodingSequencePosition().equals(690));
        assertTrue(variant.getAminoAcidStart().equals(230));
        assertTrue(variant.getAminoAcidEnd().equals(231));
        assertTrue(variant.getOriginalAminoAcid().equals("L"));
        assertTrue(variant.getFinalAminoAcid().equals("L"));
        assertTrue(variant.getIntronExonDistance().equals(182));
        assertTrue(variant.getId().getVariantEffect().equals("synonymous"));
        assertTrue(variant.getGene().getId().equals(6156));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000019.10:g.51098958G>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_145232.3:c.690C>G"));
        assertTrue(variant.getHgvsTranscript().equals("NM_145232.3:g.755C>G"));

    }

    @Test
    public void testLocatedVariant498120961() throws Exception {
        // snp nonsense

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(498120961L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001288713.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(498120961L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(21229451));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001288713.1"));
        assertTrue(variant.getRefSeqGene().equals("NELL1"));
        assertTrue(variant.getHgncGene().equals("NELL1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1803));
        assertTrue(variant.getCodingSequencePosition().equals(1630));
        assertTrue(variant.getAminoAcidStart().equals(544));
        assertTrue(variant.getAminoAcidEnd().equals(545));
        assertTrue(variant.getOriginalAminoAcid().equals("R"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getIntronExonDistance().equals(-4));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getGene().getId().equals(18892));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.21229451A>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001288713.1:c.1630A>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001288713.1:g.1803A>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001275642.1:p.Arg544*"));

    }

    @Test
    public void testLocatedVariant498121318() throws Exception {
        // snp intron

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(498121318L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001135091.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(498121318L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(26563091));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001135091.1"));
        assertTrue(variant.getRefSeqGene().equals("MUC15"));
        assertTrue(variant.getHgncGene().equals("MUC15"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(25));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getGene().getId().equals(18178));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.26563091G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001135091.1:c.925+25C>T"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

    @Test
    public void testLocatedVariant496659078() throws Exception {
        // snp splice-site

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(496659078L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001288713.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(496659078L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(21229455));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001288713.1"));
        assertTrue(variant.getRefSeqGene().equals("NELL1"));
        assertTrue(variant.getHgncGene().equals("NELL1"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(1));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site"));
        assertTrue(variant.getGene().getId().equals(18892));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.21229455G>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001288713.1:c.1633+1G>T"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

    @Test
    public void testLocatedVariant498132029() throws Exception {
        // snp splice-site-UTR-3 on + strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(498132029L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_145017.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(498132029L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(61490585));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_145017.2"));
        assertTrue(variant.getRefSeqGene().equals("PPP1R32"));
        assertTrue(variant.getHgncGene().equals("PPP1R32"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(1));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR-3"));
        assertTrue(variant.getGene().getId().equals(3200));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.61490585G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_145017.2:c.1292+1G>A"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

    @Test
    public void testLocatedVariant498132749() throws Exception {
        // snp splice-site-UTR-5 on + strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(498132749L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_025080.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(498132749L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(62337966));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_025080.3"));
        assertTrue(variant.getRefSeqGene().equals("ASRGL1"));
        assertTrue(variant.getHgncGene().equals("ASRGL1"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-1));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR-5"));
        assertTrue(variant.getGene().getId().equals(1541));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.62337966G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_025080.3:c.-11-1G>A"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

    @Test
    public void testLocatedVariant498121899() throws Exception {
        // snp splice-site-UTR-5 on - strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(498121899L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_020869.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(498121899L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(30952569));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_020869.3"));
        assertTrue(variant.getRefSeqGene().equals("DCDC5"));
        assertTrue(variant.getHgncGene().equals("DCDC5"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-1));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR-5"));
        assertTrue(variant.getGene().getId().equals(6545));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.30952569C>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_020869.3:c.-87-1G>C"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

    @Test
    public void testLocatedVariant498134022() throws Exception {
        // snp splice-site-UTR-3 on - strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(498134022L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001081491.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(498134022L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(62796183));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001081491.1"));
        assertTrue(variant.getRefSeqGene().equals("NXF1"));
        assertTrue(variant.getHgncGene().equals("NXF1"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR-3"));
        assertTrue(variant.getGene().getId().equals(19527));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.62796183T>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001081491.1:c.3147-2A>T"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

    @Test
    public void testLocatedVariant497776734() throws Exception {
        // snp splice-site-UTR-3 on - strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(497776734L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001246.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(497776734L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000009.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(137048861));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001246.3"));
        assertTrue(variant.getRefSeqGene().equals("ENTPD2"));
        assertTrue(variant.getHgncGene().equals("ENTPD2"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1216));
        assertTrue(variant.getIntronExonDistance().equals(-1));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR-3"));
        assertTrue(variant.getGene().getId().equals(8151));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.12:g.137048861C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001246.3:c.1216-1G>A"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

}
