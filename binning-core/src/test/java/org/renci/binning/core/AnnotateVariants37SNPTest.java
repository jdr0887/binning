package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariants37SNPTest extends AbstractAnnotateVariants37Test {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariants37SNPTest.class);

    public AnnotateVariants37SNPTest() {
        super();
    }

    @Test
    public void testLocatedVariant491939773() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(491939773L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491939773L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(2869956));
        assertTrue(variant.getId().getTranscript().equals("NM_000218.2"));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getGene().getId().equals(13676));
        assertTrue(variant.getRefSeqGene().equals("KCNQ1"));
        assertTrue(variant.getHgncGene().equals("KCNQ1"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(2862));
        assertTrue(variant.getIntronExonDistance().equals(723));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.9:g.2869956C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000218.2:c.2031+723C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_000218.2:g.2862C>T"));

        variant = variants.get(1);
        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491939773L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(2869956));
        assertTrue(variant.getId().getTranscript().equals("NR_040711.2"));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getGene().getId().equals(13676));
        assertTrue(variant.getRefSeqGene().equals("KCNQ1"));
        assertTrue(variant.getHgncGene().equals("KCNQ1"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getFeatureId().equals(1849762));
        assertTrue(variant.getTranscriptPosition().equals(2647));
        assertTrue(variant.getIntronExonDistance().equals(960));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.9:g.2869956C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NR_040711.2:g.2647C>T"));

    }

    @Test
    public void testLocatedVariant489265654() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(489265654L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(489265654L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000017.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(59529156));
        assertTrue(variant.getId().getTranscript().equals("XM_005257838.1"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR-5"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getGene().getId().equals(29997));
        assertTrue(variant.getRefSeqGene().equals("TBX4"));
        assertTrue(variant.getHgncGene().equals("TBX4"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000017.10:g.59529156T>A"));
    }

    @Test
    public void testLocatedVariant380297473() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(380297473L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(380297473L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000012.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(132981749));
        assertTrue(variant.getId().getTranscript().equals("XM_005266193.1"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getGene().getId().equals(101342));
        assertTrue(variant.getRefSeqGene().equals("LOC101928629"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000012.11:g.132981749T>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005266193.1:c.72+2T>G"));
    }

    @Test
    public void testLocatedVariant427005787() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(427005787L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(427005787L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(62341477));
        assertTrue(variant.getId().getTranscript().equals("XM_005274188.1"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR-3"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(32502));
        assertTrue(variant.getRefSeqGene().equals("TUT1"));
        assertTrue(variant.getHgncGene().equals("TUT1"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.9:g.62341477T>C"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(427005787L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(62341477));
        assertTrue(variant.getId().getTranscript().equals("XR_247209.1"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(32502));
        assertTrue(variant.getRefSeqGene().equals("TUT1"));
        assertTrue(variant.getHgncGene().equals("TUT1"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.9:g.62341477T>C"));

    }

    @Test
    public void testLocatedVariant492018867() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(492018867L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(492018867L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(219874360));
        assertTrue(variant.getId().getTranscript().equals("NM_194302.3"));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getGene().getId().equals(4273));
        assertTrue(variant.getRefSeqGene().equals("CCDC108"));
        assertTrue(variant.getHgncGene().equals("CFAP65"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-178));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.219874360G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_194302.3:c.4453-178C>T"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(492018867L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(219874360));
        assertTrue(variant.getId().getTranscript().equals("NR_046086.1"));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getGene().getId().equals(102540));
        assertTrue(variant.getRefSeqGene().equals("LOC100129175"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-319));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.219874360G>A"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(492018867L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(219874360));
        assertTrue(variant.getId().getTranscript().equals("XM_005246442.1"));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getGene().getId().equals(4273));
        assertTrue(variant.getRefSeqGene().equals("CCDC108"));
        assertTrue(variant.getHgncGene().equals("CFAP65"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-178));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.219874360G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005246442.1:c.4420-178C>T"));

    }

    @Test
    public void testLocatedVariant52648159() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(52648159L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(52648159L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000009.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(139510318));
        assertTrue(variant.getId().getTranscript().equals("XR_245344.1"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(100680));
        assertTrue(variant.getRefSeqGene().equals("LOC101928581"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.11:g.139510318T>C"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(52648159L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000009.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(139510318));
        assertTrue(variant.getId().getTranscript().equals("XR_250563.1"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(100680));
        assertTrue(variant.getRefSeqGene().equals("LOC101928581"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.11:g.139510318T>C"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(52648159L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000009.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(139510318));
        assertTrue(variant.getId().getTranscript().equals("XR_252675.1"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR"));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(100680));
        assertTrue(variant.getRefSeqGene().equals("LOC101928581"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.11:g.139510318T>C"));

    }

    @Test
    public void testLocatedVariant29831130() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(29831130L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(29831130L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(1284841));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XM_005244819.1"));
        assertTrue(variant.getGene().getId().equals(100928));
        assertTrue(variant.getRefSeqGene().equals("LOC101929103"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(1));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(3613));
        assertTrue(variant.getCodingSequencePosition().equals(1845));
        assertTrue(variant.getAminoAcidStart().equals(615));
        assertTrue(variant.getAminoAcidEnd().equals(616));
        assertTrue(variant.getOriginalAminoAcid().equals("R"));
        assertTrue(variant.getFinalAminoAcid().equals("R"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-13));
        assertTrue(variant.getId().getVariantEffect().equals("synonymous"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.1284841A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005244819.1:c.1845T>C"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005244819.1:g.3613T>C"));
    }

    @Test
    public void testLocatedVariant491902641() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(491902641L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 13);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_001048171.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1891350));
        assertTrue(variant.getTranscriptPosition().equals(1588));
        assertTrue(variant.getCodingSequencePosition().equals(1372));
        assertTrue(variant.getAminoAcidStart().equals(458));
        assertTrue(variant.getAminoAcidEnd().equals(459));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001048171.1:c.1372G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001048171.1:g.1588G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001041636.1:p.Gly458Ser"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_001048172.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1891648));
        assertTrue(variant.getTranscriptPosition().equals(1479));
        assertTrue(variant.getCodingSequencePosition().equals(1333));
        assertTrue(variant.getAminoAcidStart().equals(445));
        assertTrue(variant.getAminoAcidEnd().equals(446));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001048172.1:c.1333G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001048172.1:g.1479G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001041637.1:p.Gly445Ser"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_001048173.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1891829));
        assertTrue(variant.getTranscriptPosition().equals(1476));
        assertTrue(variant.getCodingSequencePosition().equals(1330));
        assertTrue(variant.getAminoAcidStart().equals(444));
        assertTrue(variant.getAminoAcidEnd().equals(445));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001048173.1:c.1330G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001048173.1:g.1476G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001041638.1:p.Gly444Ser"));

        variant = variants.get(3);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_001048174.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1892001));
        assertTrue(variant.getTranscriptPosition().equals(1395));
        assertTrue(variant.getCodingSequencePosition().equals(1330));
        assertTrue(variant.getAminoAcidStart().equals(444));
        assertTrue(variant.getAminoAcidEnd().equals(445));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001048174.1:c.1330G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001048174.1:g.1395G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001041639.1:p.Gly444Ser"));

        variant = variants.get(4);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_001128425.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1879103));
        assertTrue(variant.getTranscriptPosition().equals(1630));
        assertTrue(variant.getCodingSequencePosition().equals(1414));
        assertTrue(variant.getAminoAcidStart().equals(472));
        assertTrue(variant.getAminoAcidEnd().equals(473));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001128425.1:c.1414G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001128425.1:g.1630G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001121897.1:p.Gly472Ser"));

        variant = variants.get(5);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_012222.2"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1891181));
        assertTrue(variant.getTranscriptPosition().equals(1621));
        assertTrue(variant.getCodingSequencePosition().equals(1405));
        assertTrue(variant.getAminoAcidStart().equals(469));
        assertTrue(variant.getAminoAcidEnd().equals(470));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_012222.2:c.1405G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_012222.2:g.1621G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_036354.1:p.Gly469Ser"));

        variant = variants.get(6);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005270880.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1737087));
        assertTrue(variant.getTranscriptPosition().equals(1557));
        assertTrue(variant.getCodingSequencePosition().equals(1375));
        assertTrue(variant.getAminoAcidStart().equals(459));
        assertTrue(variant.getAminoAcidEnd().equals(460));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270880.1:c.1375G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270880.1:g.1557G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270937.1:p.Gly459Ser"));

        variant = variants.get(7);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005270881.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1737096));
        assertTrue(variant.getTranscriptPosition().equals(1408));
        assertTrue(variant.getCodingSequencePosition().equals(1363));
        assertTrue(variant.getAminoAcidStart().equals(455));
        assertTrue(variant.getAminoAcidEnd().equals(456));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270881.1:c.1363G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270881.1:g.1408G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270938.1:p.Gly455Ser"));

        variant = variants.get(8);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005270882.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1737105));
        assertTrue(variant.getTranscriptPosition().equals(1480));
        assertTrue(variant.getCodingSequencePosition().equals(1363));
        assertTrue(variant.getAminoAcidStart().equals(455));
        assertTrue(variant.getAminoAcidEnd().equals(456));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270882.1:c.1363G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270882.1:g.1480G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270939.1:p.Gly455Ser"));

        variant = variants.get(9);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005270883.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(1304));
        assertTrue(variant.getCodingSequencePosition().equals(1246));
        assertTrue(variant.getAminoAcidStart().equals(416));
        assertTrue(variant.getAminoAcidEnd().equals(417));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270883.1:c.1246G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270883.1:g.1304G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270940.1:p.Gly416Ser"));

        variant = variants.get(10);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005270884.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1737114));
        assertTrue(variant.getTranscriptPosition().equals(1470));
        assertTrue(variant.getCodingSequencePosition().equals(1054));
        assertTrue(variant.getAminoAcidStart().equals(352));
        assertTrue(variant.getAminoAcidEnd().equals(353));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270884.1:c.1054G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270884.1:g.1470G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270941.1:p.Gly352Ser"));

        variant = variants.get(11);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005270885.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1737123));
        assertTrue(variant.getTranscriptPosition().equals(1372));
        assertTrue(variant.getCodingSequencePosition().equals(1054));
        assertTrue(variant.getAminoAcidStart().equals(352));
        assertTrue(variant.getAminoAcidEnd().equals(353));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270885.1:c.1054G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270885.1:g.1372G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270942.1:p.Gly352Ser"));

        variant = variants.get(12);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005270886.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(1249));
        assertTrue(variant.getCodingSequencePosition().equals(952));
        assertTrue(variant.getAminoAcidStart().equals(318));
        assertTrue(variant.getAminoAcidEnd().equals(319));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-63));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270886.1:c.952G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270886.1:g.1249G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270943.1:p.Gly318Ser"));

    }

    @Test
    public void testLocatedVariant391895876() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(391895876L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 7);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("NM_000038.5"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(8940));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000038.5:c.8532+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("NM_000038.5:g.8940A>G"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("NM_001127510.2"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(9048));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001127510.2:c.8532+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001127510.2:g.9048A>G"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("NM_001127511.2"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(9014));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001127511.2:c.8478+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001127511.2:g.9014A>G"));

        variant = variants.get(3);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XM_005271974.1"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(9002));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005271974.1:c.8532+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005271974.1:g.9002A>G"));

        variant = variants.get(4);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XM_005271975.1"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(9235));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005271975.1:c.8532+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005271975.1:g.9235A>G"));

        variant = variants.get(5);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XM_005271976.1"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(8740));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005271976.1:c.8355+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005271976.1:g.8740A>G"));

        variant = variants.get(6);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XM_005271977.1"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(8637));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005271977.1:c.8229+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005271977.1:g.8637A>G"));

    }

    @Test
    public void testLocatedVariant404841675() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(404841675L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 5);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_000179.2"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        // assertTrue(variant.getNonCanonicalExon().equals(5));
        assertTrue(variant.getFeatureId().equals(1873395));
        assertTrue(variant.getTranscriptPosition().equals(3588));
        assertTrue(variant.getCodingSequencePosition().equals(3436));
        assertTrue(variant.getAminoAcidStart().equals(1146));
        assertTrue(variant.getAminoAcidEnd().equals(1147));
        assertTrue(variant.getOriginalAminoAcid().equals("Q"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-3));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000179.2:c.3436C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_000179.2:g.3588C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_000170.1:p.Gln1146*"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_001281492.1"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        // assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getFeatureId().equals(1980408));
        assertTrue(variant.getTranscriptPosition().equals(3198));
        assertTrue(variant.getCodingSequencePosition().equals(3046));
        assertTrue(variant.getAminoAcidStart().equals(1016));
        assertTrue(variant.getAminoAcidEnd().equals(1017));
        assertTrue(variant.getOriginalAminoAcid().equals("Q"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-3));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001281492.1:c.3046C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001281492.1:g.3198C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001268421.1:p.Gln1016*"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_001281493.1"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        // assertTrue(variant.getNonCanonicalExon().equals(2));
        assertTrue(variant.getFeatureId().equals(1980423));
        assertTrue(variant.getTranscriptPosition().equals(3418));
        assertTrue(variant.getCodingSequencePosition().equals(2530));
        assertTrue(variant.getAminoAcidStart().equals(844));
        assertTrue(variant.getAminoAcidEnd().equals(845));
        assertTrue(variant.getOriginalAminoAcid().equals("Q"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-3));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001281493.1:c.2530C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001281493.1:g.3418C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001268422.1:p.Gln844*"));

        variant = variants.get(3);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("NM_001281494.1"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        // assertTrue(variant.getNonCanonicalExon().equals(2));
        assertTrue(variant.getFeatureId().equals(1980438));
        assertTrue(variant.getTranscriptPosition().equals(3323));
        assertTrue(variant.getCodingSequencePosition().equals(2530));
        assertTrue(variant.getAminoAcidStart().equals(844));
        assertTrue(variant.getAminoAcidEnd().equals(845));
        assertTrue(variant.getOriginalAminoAcid().equals("Q"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-3));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001281494.1:c.2530C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001281494.1:g.3323C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001268423.1:p.Gln844*"));

        variant = variants.get(4);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005264271.1"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        // assertTrue(variant.getNonCanonicalExon().equals(4));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(3234));
        assertTrue(variant.getCodingSequencePosition().equals(3139));
        assertTrue(variant.getAminoAcidStart().equals(1047));
        assertTrue(variant.getAminoAcidEnd().equals(1048));
        assertTrue(variant.getOriginalAminoAcid().equals("Q"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-3));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005264271.1:c.3139C>T"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005264271.1:g.3234C>T"));
        assertTrue(variant.getHgvsProtein().equals("XP_005264328.1:p.Gln1047*"));

    }

    @Test
    public void testLocatedVariant386029980() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(386029980L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(386029980L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(11707985));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getId().getTranscript().equals("XR_244788.1"));
        assertTrue(variant.getGene().getId().equals(9154));
        assertTrue(variant.getRefSeqGene().equals("FBXO2"));
        assertTrue(variant.getHgncGene().equals("FBXO2"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-14));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.11707985G>A"));

    }

    @Test
    public void testLocatedVariant484291964() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(484291964L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(484291964L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(65898));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getId().getTranscript().equals("XM_005259648.1"));
        assertTrue(variant.getGene().getId().equals(20051));
        assertTrue(variant.getRefSeqGene().equals("OR4F17"));
        assertTrue(variant.getHgncGene().equals("OR4F17"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(16));
        assertTrue(variant.getIntronExonDistance().equals(-74));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.65898T>C"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005259648.1:g.16T>C"));

    }

    @Test
    public void testLocatedVariant385947625() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(385947625L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 5);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(2));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("NR_028326.1"));
        assertTrue(variant.getGene().getId().equals(72605));
        assertTrue(variant.getRefSeqGene().equals("LINC01001"));
        assertTrue(variant.getHgncGene().equals("LINC01001"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1906792));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(4613));
        assertTrue(variant.getIntronExonDistance().equals(1448));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("NR_028326.1:g.4613G>C"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("NR_039983.2"));
        assertTrue(variant.getGene().getId().equals(102469));
        assertTrue(variant.getRefSeqGene().equals("LOC729737"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1902186));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(5473));
        assertTrue(variant.getIntronExonDistance().equals(4923));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("NR_039983.2:g.5473G>C"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_246728.1"));
        assertTrue(variant.getGene().getId().equals(100855));
        assertTrue(variant.getRefSeqGene().equals("LOC101930220"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1734910));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1721));
        assertTrue(variant.getIntronExonDistance().equals(1593));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_246728.1:g.1721G>C"));

        variant = variants.get(3);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(8));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_253608.1"));
        assertTrue(variant.getGene().getId().equals(101604));
        assertTrue(variant.getRefSeqGene().equals("LOC100133182"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1794079));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1580));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_253608.1:g.1580G>C"));

        variant = variants.get(4);

        // assertTrue(variant.getId().getMapNumber().equals(3));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(7));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_254450.1"));
        assertTrue(variant.getGene().getId().equals(101709));
        assertTrue(variant.getRefSeqGene().equals("LOC101929817"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1788322));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1220));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_254450.1:g.1220G>C"));

    }

    @Test
    public void testLocatedVariant491873904() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(491873904L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(491873904L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(721323));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_108279.2"));
        assertTrue(variant.getGene().getId().equals(100921));
        assertTrue(variant.getRefSeqGene().equals("LOC100287934"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1735318));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(224));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.721323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_108279.2:g.224A>G"));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(491873904L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(721323));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_171142.2"));
        assertTrue(variant.getGene().getId().equals(100921));
        assertTrue(variant.getRefSeqGene().equals("LOC100287934"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1790602));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(224));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.721323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_171142.2:g.224A>G"));

        variant = variants.get(2);

        assertTrue(variant.getLocatedVariant().getId().equals(491873904L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(721323));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_250120.1"));
        assertTrue(variant.getGene().getId().equals(100219));
        assertTrue(variant.getRefSeqGene().equals("LOC101930503"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(41728));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.721323A>G"));

    }

    @Test
    public void testLocatedVariant32253987() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(32253987L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(32253987L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(846842));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getId().getTranscript().equals("XR_108282.2"));
        assertTrue(variant.getGene().getId().equals(98944));
        assertTrue(variant.getRefSeqGene().equals("LOC284600"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1735321));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(28));
        assertTrue(variant.getIntronExonDistance().equals(-12));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.846842G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XR_108282.2:g.28G>A"));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(32253987L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(846842));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getId().getTranscript().equals("XR_112078.2"));
        assertTrue(variant.getGene().getId().equals(98944));
        assertTrue(variant.getRefSeqGene().equals("LOC284600"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1732659));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(28));
        assertTrue(variant.getIntronExonDistance().equals(-12));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.846842G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XR_112078.2:g.28G>A"));

        variant = variants.get(2);

        assertTrue(variant.getLocatedVariant().getId().equals(32253987L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(846842));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getId().getTranscript().equals("XR_171144.2"));
        assertTrue(variant.getGene().getId().equals(98944));
        assertTrue(variant.getRefSeqGene().equals("LOC284600"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1790606));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(28));
        assertTrue(variant.getIntronExonDistance().equals(-12));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.846842G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XR_171144.2:g.28G>A"));

    }

    @Test
    public void testLocatedVariant487626896() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(487626896L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(487626896L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(931116));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005275882.1"));
        assertTrue(variant.getGene().getId().equals(98949));
        assertTrue(variant.getRefSeqGene().equals("LOC101930320"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1999));
        assertTrue(variant.getIntronExonDistance().equals(388));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.931116G>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005275882.1:c.1611+388C>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005275882.1:g.1999C>A"));

    }

    @Test
    public void testLocatedVariant385962999() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(385962999L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(385962999L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(901873));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getId().getTranscript().equals("XM_005244809.1"));
        assertTrue(variant.getGene().getId().equals(22155));
        assertTrue(variant.getRefSeqGene().equals("PLEKHN1"));
        assertTrue(variant.getHgncGene().equals("PLEKHN1"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(2));
        assertTrue(variant.getIntronExonDistance().equals(-39));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.901873G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005244809.1:c.1-39G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005244809.1:g.2G>A"));

    }

    @Test
    public void testLocatedVariant31046600() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(31046600L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(31046600L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(22649389));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_003960117.2"));
        assertTrue(variant.getGene().getId().equals(98888));
        assertTrue(variant.getRefSeqGene().equals("LOC101060363"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getFeatureId().equals(1790651));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(436));
        assertTrue(variant.getCodingSequencePosition().equals(436));
        assertTrue(variant.getAminoAcidStart().equals(146));
        assertTrue(variant.getAminoAcidEnd().equals(147));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-57));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.22649389C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_003960117.2:c.436G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_003960117.2:g.436G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_003960166.1:p.Gly146Ser"));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(31046600L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(22649389));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005246048.1"));
        assertTrue(variant.getGene().getId().equals(98888));
        assertTrue(variant.getRefSeqGene().equals("LOC101060363"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getFeatureId().equals(1736054));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(436));
        assertTrue(variant.getCodingSequencePosition().equals(436));
        assertTrue(variant.getAminoAcidStart().equals(146));
        assertTrue(variant.getAminoAcidEnd().equals(147));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-57));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.22649389C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005246048.1:c.436G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005246048.1:g.436G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005246105.1:p.Gly146Ser"));

        variant = variants.get(2);

        assertTrue(variant.getLocatedVariant().getId().equals(31046600L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(22649389));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getId().getTranscript().equals("XM_005275876.1"));
        assertTrue(variant.getGene().getId().equals(98888));
        assertTrue(variant.getRefSeqGene().equals("LOC101060363"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getFeatureId().equals(1732595));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(436));
        assertTrue(variant.getCodingSequencePosition().equals(436));
        assertTrue(variant.getAminoAcidStart().equals(146));
        assertTrue(variant.getAminoAcidEnd().equals(147));
        assertTrue(variant.getOriginalAminoAcid().equals("G"));
        assertTrue(variant.getFinalAminoAcid().equals("S"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-57));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.22649389C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005275876.1:c.436G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005275876.1:g.436G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005275933.1:p.Gly146Ser"));

    }

    @Test
    public void testLocatedVariant476568240() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(476568240L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(476568240L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(33772384));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getId().getTranscript().equals("NM_001080438.1"));
        assertTrue(variant.getGene().getId().equals(13));
        assertTrue(variant.getRefSeqGene().equals("A3GALT2"));
        assertTrue(variant.getHgncGene().equals("A3GALT2"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getFeatureId().equals(1612015));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1006));
        assertTrue(variant.getCodingSequencePosition().equals(1006));
        assertTrue(variant.getAminoAcidStart().equals(336));
        assertTrue(variant.getAminoAcidEnd().equals(337));
        assertTrue(variant.getOriginalAminoAcid().equals("R"));
        assertTrue(variant.getFinalAminoAcid().equals("W"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-18));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.33772384G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001080438.1:c.1006C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001080438.1:g.1006C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001073907.1:p.Arg336Trp"));

    }

    @Test
    public void testLocatedVariant29835813() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(29835813L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(29835813L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(13140452));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getId().getTranscript().equals("XM_003118846.3"));
        assertTrue(variant.getGene().getId().equals(100932));
        assertTrue(variant.getRefSeqGene().equals("PRAMEF25"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(6));
        assertTrue(variant.getCodingSequencePosition().equals(3));
        assertTrue(variant.getAminoAcidStart().equals(1));
        assertTrue(variant.getAminoAcidEnd().equals(2));
        assertTrue(variant.getOriginalAminoAcid().equals("M"));
        assertTrue(variant.getFinalAminoAcid().equals("I"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(3));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.13140452G>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_003118846.3:c.3G>C"));
        assertTrue(variant.getHgvsTranscript().equals("XM_003118846.3:g.6G>C"));
        assertTrue(variant.getHgvsProtein().equals("XP_003118894.3:p.Met1Ile"));

    }

    @Test
    public void testLocatedVariant386017450() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(386017450L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(386017450L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9335209));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XM_005263498.1"));
        assertTrue(variant.getGene().getId().equals(29177));
        assertTrue(variant.getRefSeqGene().equals("SPSB1"));
        assertTrue(variant.getHgncGene().equals("SPSB1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(8));
        assertTrue(variant.getCodingSequencePosition().equals(8));
        assertTrue(variant.getAminoAcidStart().equals(3));
        assertTrue(variant.getAminoAcidEnd().equals(4));
        assertTrue(variant.getOriginalAminoAcid().equals("K"));
        assertTrue(variant.getFinalAminoAcid().equals("R"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-36));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9335209A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005263498.1:c.8A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005263498.1:g.8A>G"));
        assertTrue(variant.getHgvsProtein().equals("XP_005263555.1:p.Lys3Arg"));

    }

}
