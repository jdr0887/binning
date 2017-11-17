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
import org.renci.canvas.dao.refseq.model.Variants_61_2;
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
    public void testLocatedVariant570972984() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(570972984L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

    }

    @Test
    public void testLocatedVariant522377766() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(522377766L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        // assertTrue(variants.size() == 1);

    }

    @Test
    public void testLocatedVariant595706057() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(595706057L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_002473.5")).findFirst().get();
        logger.info(variant.toString());
        
        assertTrue(variant.getLocatedVariant().getId().equals(595706057L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000022.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(36289295));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getId().getTranscript().equals("NM_002473.5"));
        assertTrue(variant.getRefSeqGene().equals("MYH9"));
        assertTrue(variant.getHgncGene().equals("MYH9"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getId().getVariantEffect().equals("boundary-crossing indel"));
        assertTrue(variant.getGene().getId().equals(18264));
        assertTrue(variant.getReferenceAllele().equals("GAGCTG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000022.11:g.36289295_36289300delinsAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant492040380() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(492040380L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 12);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_000249.3")).findFirst().get();

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
    public void testLocatedVariant575353004() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(575353004L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

    }

    @Test
    public void testLocatedVariant535444544() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(535444544L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 6);

    }

    @Test
    public void testLocatedVariant561693182() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(561693182L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

    }

    @Test
    public void testLocatedVariant553807156() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(553807156L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

    }

    @Test
    public void testLocatedVariant541353144() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(541353144L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 6);

    }

    @Test
    public void testLocatedVariant586786468() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(586786468L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

    }

    @Test
    public void testLocatedVariant588229844() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(588229844L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

    }

    @Test
    public void testLocatedVariant588246290() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(588246290L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

    }

    @Test
    public void testLocatedVariant541791595() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(541791595L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_80_4 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        // assertTrue(variant.getLocatedVariant().getId().equals(588216920L));
        // assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.11"));
        // assertTrue(variant.getLocatedVariant().getPosition().equals(151800924));
        // assertTrue(variant.getVariantType().getId().equals("del"));
        // assertTrue(variant.getId().getTranscript().equals("NM_001004432.3"));
        // assertTrue(variant.getLocationType().getId().equals("exon"));
        // assertTrue(variant.getId().getVariantEffect().equals("frameshifting indel"));
        // assertTrue(variant.getRefSeqGene().equals("LINGO4"));
        // assertTrue(variant.getHgncGene().equals("LINGO4"));
        // assertTrue(variant.getTranscriptPosition().equals(2007));
        // assertTrue(variant.getCodingSequencePosition().equals(1781));
        // assertTrue(variant.getAminoAcidStart().equals(591));
        // assertTrue(variant.getAminoAcidEnd().equals(595));
        // assertTrue(variant.getOriginalAminoAcid().equals("KLF..."));
        // assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        // assertTrue(variant.getInframe().equals(Boolean.FALSE));
        // assertTrue(variant.getIntronExonDistance().equals(1782));
        // assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getReferenceAllele().equals("AGAAGAGCTTGGC"));
        // assertTrue(variant.getHgvsGenomic().equals("NC_000001.11:g.151800924_151800936delAGAAGAGCTTGGC"));
        // assertTrue(variant.getHgvsCodingSequence().equals("NM_001004432.3:c.1769_1781delGCCAAGCTCTTCT"));
        // assertTrue(variant.getHgvsTranscript().equals("NM_001004432.3:g.1995_2007delGCCAAGCTCTTCT"));
        // assertTrue(variant.getHgvsProtein().equals("NP_001004432.1:p.Lys591fs"));
        // assertTrue(variant.getGene().getId().equals(14758));
        // assertTrue(variant.getNonCanonicalExon().equals(1));

    }

    @Test
    public void testLocatedVariant562476950() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(562476950L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_80_4 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(562476950L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(12920412));
        assertTrue(variant.getId().getTranscript().equals("NM_001012277.4"));
        assertTrue(variant.getId().getVariantEffect().equals("synonymous"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getGene().getId().equals(22694));
        assertTrue(variant.getRefSeqGene().equals("PRAMEF7"));
        assertTrue(variant.getHgncGene().equals("PRAMEF7"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.11:g.12920412G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001012277.4:c.1424G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001012277.4:g.1490G>A"));

    }

    @Test
    public void testLocatedVariant587669830() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(587669830L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_80_4 variant = variants.get(0);
        logger.info(variant.toString());

        variant = variants.get(1);
        logger.info(variant.toString());

    }

    @Test
    public void testLocatedVariant532811053() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(532811053L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_80_4 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(532811053L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(77558665));
        assertTrue(variant.getId().getTranscript().equals("NM_012093.3"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getReferenceAllele().equals(""));
        assertTrue(variant.getAlternateAllele().equals("ATT"));
        assertTrue(variant.getGene().getId().equals(646));
        assertTrue(variant.getRefSeqGene().equals("AK5"));
        assertTrue(variant.getHgncGene().equals("AK5"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-6));
        assertTrue(variant.getCodingSequencePosition().equals(1606));
        assertTrue(variant.getAminoAcidStart().equals(536));
        assertTrue(variant.getAminoAcidEnd().equals(537));
        assertTrue(variant.getOriginalAminoAcid().equals("F*"));
        assertTrue(variant.getFinalAminoAcid().equals("YF*"));
        assertTrue(variant.getTranscriptPosition().equals(2632));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.11:g.77558665_77558666insATT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_012093.3:c.1606_1607insATT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_012093.3:g.2632_2633insATT"));
        assertTrue(variant.getHgvsProtein().equals("NP_036225.2:p.Phe536_*537delinsTyrPhe*"));

        variant = variants.get(1);
        logger.info(variant.toString());
        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(532811053L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(77558665));
        assertTrue(variant.getId().getTranscript().equals("NM_174858.2"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getReferenceAllele().equals(""));
        assertTrue(variant.getAlternateAllele().equals("ATT"));
        assertTrue(variant.getGene().getId().equals(646));
        assertTrue(variant.getRefSeqGene().equals("AK5"));
        assertTrue(variant.getHgncGene().equals("AK5"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-6));
        assertTrue(variant.getCodingSequencePosition().equals(1684));
        assertTrue(variant.getAminoAcidStart().equals(562));
        assertTrue(variant.getAminoAcidEnd().equals(563));
        assertTrue(variant.getOriginalAminoAcid().equals("F*"));
        assertTrue(variant.getFinalAminoAcid().equals("YF*"));
        assertTrue(variant.getTranscriptPosition().equals(2021));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.11:g.77558665_77558666insATT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_174858.2:c.1684_1685insATT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_174858.2:g.2021_2022insATT"));
        assertTrue(variant.getHgvsProtein().equals("NP_777283.1:p.Phe562_*563delinsTyrPhe*"));

    }

}
