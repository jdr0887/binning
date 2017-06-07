package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.collections.CollectionUtils;
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
import org.renci.canvas.dao.jpa.var.LocatedVariantDAOImpl;
import org.renci.canvas.dao.refseq.model.TranscriptMaps;
import org.renci.canvas.dao.refseq.model.TranscriptMapsExons;
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

    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

    private List<Variants_80_4> annotateLocatedVariant(LocatedVariant locatedVariant) throws Exception {
        List<Variants_80_4> variants = new ArrayList<>();

        VariantsFactory variantsFactory = VariantsFactory.getInstance(daoBean);

        List<TranscriptMaps> transcriptMapsList = daoBean.getTranscriptMapsDAO()
                .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(4, "80", locatedVariant.getGenomeRefSeq().getId(),
                        locatedVariant.getPosition());
        logger.info("transcriptMapsList.size(): {}", transcriptMapsList.size());

        if (CollectionUtils.isNotEmpty(transcriptMapsList)) {
            // handling non boundary crossing variants (intron/exon/utr*)

            transcriptMapsList.sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

            for (TranscriptMaps tMap : transcriptMapsList) {

                List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                        .findByTranscriptMapsId(tMap.getId());

                List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO().findByGenomeRefIdAndRefSeqVersionAndTranscriptId(2, "80",
                        tMap.getTranscript().getId());

                Variants_80_4 variant = null;

                Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.parallelStream()
                        .filter(a -> a.getContigRange().contains(locatedVariant.getPosition())).findAny();
                if (optionalTranscriptMapsExons.isPresent()) {
                    TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                    logger.info(transcriptMapsExons.toString());

                    if ((transcriptMapsExons.getContigEnd().equals(locatedVariant.getPosition()) && "-".equals(tMap.getStrand()))
                            || (transcriptMapsExons.getContigStart().equals(locatedVariant.getPosition())
                                    && "+".equals(tMap.getStrand()))) {
                        variant = variantsFactory.createBorderCrossingVariant(locatedVariant, tMap, mapsList, transcriptMapsExonsList,
                                transcriptMapsExons);
                    } else {
                        variant = variantsFactory.createExonicVariant(locatedVariant, mapsList, transcriptMapsExonsList,
                                transcriptMapsExons);
                    }

                } else {
                    variant = variantsFactory.createIntronicVariant(locatedVariant, mapsList, tMap, transcriptMapsExonsList);
                }
                variants.add(variant);

            }

        } else {

            // try searching by adjusting for length of locatedVariant.getSeq()...could be intron/exon boundary crossing
            transcriptMapsList = daoBean.getTranscriptMapsDAO().findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(4,
                    "80", locatedVariant.getGenomeRefSeq().getId(), locatedVariant.getPosition() + locatedVariant.getRef().length() - 1);

            if (CollectionUtils.isNotEmpty(transcriptMapsList)) {

                transcriptMapsList.sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                for (TranscriptMaps tMap : transcriptMapsList) {

                    List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                            .findByTranscriptMapsId(tMap.getId());

                    Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.parallelStream()
                            .filter(a -> a.getContigRange().contains(locatedVariant.getPosition() + locatedVariant.getRef().length() - 1))
                            .findAny();
                    if (optionalTranscriptMapsExons.isPresent()) {
                        TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                        logger.info(transcriptMapsExons.toString());
                        List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO().findByGenomeRefIdAndRefSeqVersionAndTranscriptId(4,
                                "80", tMap.getTranscript().getId());
                        Variants_80_4 variant = variantsFactory.createBorderCrossingVariant(locatedVariant, tMap, mapsList,
                                transcriptMapsExonsList, transcriptMapsExons);
                        variants.add(variant);
                    }

                }
            } else {
                transcriptMapsList = daoBean.getTranscriptMapsDAO().findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(
                        4, "80", locatedVariant.getGenomeRefSeq().getId(), locatedVariant.getPosition() - locatedVariant.getRef().length());
                if (CollectionUtils.isNotEmpty(transcriptMapsList)) {

                    transcriptMapsList.sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                    for (TranscriptMaps tMap : transcriptMapsList) {

                        List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                .findByTranscriptMapsId(tMap.getId());

                        Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.parallelStream()
                                .filter(a -> a.getContigRange().contains(locatedVariant.getPosition() - locatedVariant.getRef().length()))
                                .findAny();
                        if (optionalTranscriptMapsExons.isPresent()) {
                            TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                            logger.info(transcriptMapsExons.toString());
                            List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                    .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(4, "80", tMap.getTranscript().getId());
                            Variants_80_4 variant = variantsFactory.createBorderCrossingVariant(locatedVariant, tMap, mapsList,
                                    transcriptMapsExonsList, transcriptMapsExons);
                            variants.add(variant);
                        }

                    }
                }
            }

        }

        if (CollectionUtils.isEmpty(variants)) {
            // not found in or across any transcript, must be intergenic
            Variants_80_4 variant = variantsFactory.createIntergenicVariant(locatedVariant);
            variants.add(variant);
        }

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
    public void testLocatedVariant575353004() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(575353004L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

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
        assertTrue(variants.size() == 6);

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
        assertTrue(variants.size() == 2);

    }

    @Test
    public void testLocatedVariant588231648() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(588231648L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

    }

    @Test
    public void testLocatedVariant588227131() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(588227131L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

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
    public void testLocatedVariant588216920() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(588216920L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_80_4 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(588216920L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(151800924));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001004432.3"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getId().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getRefSeqGene().equals("LINGO4"));
        assertTrue(variant.getHgncGene().equals("LINGO4"));
        assertTrue(variant.getTranscriptPosition().equals(2007));
        assertTrue(variant.getCodingSequencePosition().equals(1781));
        assertTrue(variant.getAminoAcidStart().equals(591));
        assertTrue(variant.getAminoAcidEnd().equals(595));
        assertTrue(variant.getOriginalAminoAcid().equals("KLF..."));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(1782));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getReferenceAllele().equals("AGAAGAGCTTGGC"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.11:g.151800924_151800936delAGAAGAGCTTGGC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001004432.3:c.1769_1781delGCCAAGCTCTTCT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001004432.3:g.1995_2007delGCCAAGCTCTTCT"));
        assertTrue(variant.getHgvsProtein().equals("NP_001004432.1:p.Lys591fs"));
        assertTrue(variant.getGene().getId().equals(14758));
        assertTrue(variant.getNonCanonicalExon().equals(1));

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
    public void testLocatedVariant588227120() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(588227120L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        Variants_80_4 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(588227120L));
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
