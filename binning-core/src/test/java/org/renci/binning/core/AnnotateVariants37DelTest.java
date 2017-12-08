package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariants37DelTest extends AbstractAnnotateVariants37Test {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariants37DelTest.class);

    public AnnotateVariants37DelTest() {
        super();
    }

    @Test
    public void testLocatedVariant488390638() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(488390638L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);

        Variants_61_2 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_020469.2")).findAny().get();

        assertTrue(variant.getLocatedVariant().getId().equals(488390638L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000009.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(136131058));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_020469.2"));
        assertTrue(variant.getRefSeqGene().equals("ABO"));
        assertTrue(variant.getHgncGene().equals("ABO"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1085));
        assertTrue(variant.getCodingSequencePosition().equals(1060));
        assertTrue(variant.getAminoAcidStart().equals(354));
        assertTrue(variant.getAminoAcidEnd().equals(356));
        assertTrue(variant.getOriginalAminoAcid().equals("P*"));
        assertTrue(variant.getFinalAminoAcid().equals("R"));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-6));
        assertTrue(variant.getId().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(147));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.11:g.136131058delG"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_020469.2:c.1060delC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_020469.2:g.1085delC"));
        assertTrue(variant.getHgvsProtein().equals("NP_065202.2:p.Pro354fs"));

    }

    @Test
    public void testLocatedVariant477725576() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(477725576L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(477725576L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(240982394));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001080835.1"));
        assertTrue(variant.getRefSeqGene().equals("PRR21"));
        assertTrue(variant.getHgncGene().equals("PRR21"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(6));
        assertTrue(variant.getCodingSequencePosition().equals(6));
        assertTrue(variant.getAminoAcidStart().equals(1));
        assertTrue(variant.getAminoAcidEnd().equals(391));
        assertTrue(variant.getOriginalAminoAcid().equals("MHACS..."));
        assertTrue(variant.getFinalAminoAcid().equals("SLFIH..."));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-1165));
        assertTrue(variant.getId().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(22939));
        assertTrue(variant.getReferenceAllele().equals("ATGCA"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.240982394_240982398delATGCA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001080835.1:c.2_6delTGCAT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001080835.1:g.2_6delTGCAT"));
        assertTrue(variant.getHgvsProtein().equals("NP_001074304.1:p.Met1fs"));
        //// assertTrue(variant.getNonCanonicalExon().equals(1));
        assertTrue(variant.getFeatureId().equals(0));

    }

    @Test
    public void testLocatedVariant385391408() throws Exception {
    }

    @Test
    public void testLocatedVariant431923846() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(431923846L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 4);

        Variants_61_2 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_201442.2")).findAny().get();

        assertTrue(variant.getLocatedVariant().getId().equals(431923846L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000012.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(7168184));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_201442.2"));
        assertTrue(variant.getRefSeqGene().equals("C1S"));
        assertTrue(variant.getHgncGene().equals("C1S"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getGene().getId().equals(2548));

    }

    @Test
    public void testLocatedVariant476843500() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(476843500L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(476843500L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(151774103));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001004432.2"));
        assertTrue(variant.getRefSeqGene().equals("LINGO4"));
        assertTrue(variant.getHgncGene().equals("LINGO4"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1268));
        assertTrue(variant.getCodingSequencePosition().equals(1078));
        assertTrue(variant.getAminoAcidStart().equals(352));
        assertTrue(variant.getAminoAcidEnd().equals(360));
        assertTrue(variant.getOriginalAminoAcid().equals("TLRLSGNPL"));
        assertTrue(variant.getFinalAminoAcid().equals("I"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-705));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(14758));
        assertTrue(variant.getReferenceAllele().equals("GGGGGTTGCCAGACAGCCTCAAGG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.151774103_151774126delGGGGGTTGCCAGACAGCCTCAAGG"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001004432.2:c.1055_1078delCCTTGAGGCTGTCTGGCAACCCCC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001004432.2:g.1245_1268delCCTTGAGGCTGTCTGGCAACCCCC"));
        assertTrue(variant.getHgvsProtein().equals("NP_001004432.1:p.Thr352_Leu360delinsIle"));
        //// assertTrue(variant.getNonCanonicalExon().equals(1));
        assertTrue(variant.getFeatureId().equals(2007292));

    }

    @Test
    public void testLocatedVariant441395481() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(441395481L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(441395481L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000012.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(8759578));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_020661.2"));
        assertTrue(variant.getRefSeqGene().equals("AICDA"));
        assertTrue(variant.getHgncGene().equals("AICDA"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(118));
        assertTrue(variant.getCodingSequencePosition().equals(39));
        assertTrue(variant.getAminoAcidStart().equals(8));
        assertTrue(variant.getAminoAcidEnd().equals(200));
        assertTrue(variant.getOriginalAminoAcid().equals("RRKFL..."));
        assertTrue(variant.getFinalAminoAcid().equals("NSKMS..."));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(13));
        assertTrue(variant.getId().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(604));
        assertTrue(variant.getReferenceAllele().equals("GTAAAGAAACTTCCTCCGG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000012.11:g.8759578_8759596delGTAAAGAAACTTCCTCCGG"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_020661.2:c.21_39delCCGGAGGAAGTTTCTTTAC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_020661.2:g.100_118delCCGGAGGAAGTTTCTTTAC"));
        assertTrue(variant.getHgvsProtein().equals("NP_065712.1:p.Arg8fs"));
        //// assertTrue(variant.getNonCanonicalExon().equals(4));
        assertTrue(variant.getFeatureId().equals(1869405));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(441395481L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000012.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(8759578));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("XM_005253432.1"));
        assertTrue(variant.getRefSeqGene().equals("AICDA"));
        assertTrue(variant.getHgncGene().equals("AICDA"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(139));
        assertTrue(variant.getCodingSequencePosition().equals(39));
        assertTrue(variant.getAminoAcidStart().equals(8));
        assertTrue(variant.getAminoAcidEnd().equals(190));
        assertTrue(variant.getOriginalAminoAcid().equals("RRKFL..."));
        assertTrue(variant.getFinalAminoAcid().equals("NSKMS..."));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(13));
        assertTrue(variant.getId().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(604));
        assertTrue(variant.getReferenceAllele().equals("GTAAAGAAACTTCCTCCGG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000012.11:g.8759578_8759596delGTAAAGAAACTTCCTCCGG"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005253432.1:c.21_39delCCGGAGGAAGTTTCTTTAC"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005253432.1:g.121_139delCCGGAGGAAGTTTCTTTAC"));
        assertTrue(variant.getHgvsProtein().equals("XP_005253489.1:p.Arg8fs"));
        //// assertTrue(variant.getNonCanonicalExon().equals(4));
        assertTrue(variant.getFeatureId().equals(1764521));

    }

    @Test
    public void testLocatedVariant8801171() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(8801171L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(8801171L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(17087245));
        assertTrue(variant.getId().getTranscript().equals("NM_001271733.1"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site"));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(17942));
        assertTrue(variant.getRefSeqGene().equals("MST1L"));
        assertTrue(variant.getHgncGene().equals("MST1L"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(1));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.17087245delC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001271733.1:c.338+1_338delG"));

    }

    @Test
    public void testLocatedVariant420518626() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(420518626L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(420518626L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99126522));
        assertTrue(variant.getId().getTranscript().equals("NM_001145114.1"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("CTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(8));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(3148));
        assertTrue(variant.getCodingSequencePosition().equals(3009));
        assertTrue(variant.getAminoAcidStart().equals(1003));
        assertTrue(variant.getAminoAcidEnd().equals(1003));
        assertTrue(variant.getOriginalAminoAcid().equals("E"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(-24));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.99126522_99126524delCTC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001145114.1:c.3007_3009delGAG"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001145114.1:g.3146_3148delGAG"));
        assertTrue(variant.getHgvsProtein().equals("NP_001138586.1:p.Glu1003del"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(420518626L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99126522));
        assertTrue(variant.getId().getTranscript().equals("NM_015179.3"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("CTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(8));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(3331));
        assertTrue(variant.getCodingSequencePosition().equals(3192));
        assertTrue(variant.getAminoAcidStart().equals(1064));
        assertTrue(variant.getAminoAcidEnd().equals(1064));
        assertTrue(variant.getOriginalAminoAcid().equals("E"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(-24));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.99126522_99126524delCTC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_015179.3:c.3190_3192delGAG"));
        assertTrue(variant.getHgvsTranscript().equals("NM_015179.3:g.3329_3331delGAG"));
        assertTrue(variant.getHgvsProtein().equals("NP_055994.2:p.Glu1064del"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(420518626L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99126522));
        assertTrue(variant.getId().getTranscript().equals("XM_005269659.1"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("CTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(8));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(2992));
        assertTrue(variant.getCodingSequencePosition().equals(2892));
        assertTrue(variant.getAminoAcidStart().equals(964));
        assertTrue(variant.getAminoAcidEnd().equals(964));
        assertTrue(variant.getOriginalAminoAcid().equals("E"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(-24));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.99126522_99126524delCTC"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005269659.1:c.2890_2892delGAG"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005269659.1:g.2990_2992delGAG"));
        assertTrue(variant.getHgvsProtein().equals("XP_005269716.1:p.Glu964del"));

    }

    @Test
    public void testLocatedVariant434113836() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(434113836L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(434113836L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99160113));
        assertTrue(variant.getId().getTranscript().equals("NM_001145114.1"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("GGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(31));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(457));
        assertTrue(variant.getCodingSequencePosition().equals(318));
        assertTrue(variant.getAminoAcidStart().equals(101));
        assertTrue(variant.getAminoAcidEnd().equals(106));
        assertTrue(variant.getOriginalAminoAcid().equals("DCTNVT"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(-52));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.99160113_99160130delGGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001145114.1:c.301_318delGACTGCACAAACGTCACC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001145114.1:g.440_457delGACTGCACAAACGTCACC"));
        assertTrue(variant.getHgvsProtein().equals("NP_001138586.1:p.Asp101_Thr106del"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(434113836L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99160113));
        assertTrue(variant.getId().getTranscript().equals("NM_015179.3"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("GGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(33));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(457));
        assertTrue(variant.getCodingSequencePosition().equals(318));
        assertTrue(variant.getAminoAcidStart().equals(101));
        assertTrue(variant.getAminoAcidEnd().equals(106));
        assertTrue(variant.getOriginalAminoAcid().equals("DCTNVT"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(-52));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.99160113_99160130delGGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_015179.3:c.301_318delGACTGCACAAACGTCACC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_015179.3:g.440_457delGACTGCACAAACGTCACC"));
        assertTrue(variant.getHgvsProtein().equals("NP_055994.2:p.Asp101_Thr106del"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(434113836L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99160113));
        assertTrue(variant.getId().getTranscript().equals("XM_005269659.1"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("GGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(30));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(418));
        assertTrue(variant.getCodingSequencePosition().equals(318));
        assertTrue(variant.getAminoAcidStart().equals(101));
        assertTrue(variant.getAminoAcidEnd().equals(106));
        assertTrue(variant.getOriginalAminoAcid().equals("DCTNVT"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(-52));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.99160113_99160130delGGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005269659.1:c.301_318delGACTGCACAAACGTCACC"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005269659.1:g.401_418delGACTGCACAAACGTCACC"));
        assertTrue(variant.getHgvsProtein().equals("XP_005269716.1:p.Asp101_Thr106del"));

    }

    @Test
    public void testLocatedVariant476428721() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(476428721L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(476428721L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(8378174));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("GCAGCAGCCGGGACCGCGGCCGGGCAG"));
        assertTrue(variant.getId().getTranscript().equals("XM_005263467.1"));
        assertTrue(variant.getGene().getId().equals(28139));
        assertTrue(variant.getRefSeqGene().equals("SLC45A1"));
        assertTrue(variant.getHgncGene().equals("SLC45A1"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(4));
        assertTrue(variant.getIntronExonDistance().equals(-47));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.8378174_8378200delGCAGCAGCCGGGACCGCGGCCGGGCAG"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005263467.1:g.4_30delGCAGCAGCCGGGACCGCGGCCGGGCAG"));
    }

    @Test
    public void testLocatedVariant476430841() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(476430841L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 4);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(476430841L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9005897));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("AAA"));
        assertTrue(variant.getId().getTranscript().equals("NM_001215.3"));
        assertTrue(variant.getGene().getId().equals(3852));
        assertTrue(variant.getRefSeqGene().equals("CA6"));
        assertTrue(variant.getHgncGene().equals("CA6"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(5));
        assertTrue(variant.getIntronExonDistance().equals(-47));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9005897_9005899delAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001215.3:c.1-47_3delAAA"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001215.3:g.5_7delAAA"));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(476430841L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9005897));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("AAA"));
        assertTrue(variant.getId().getTranscript().equals("NM_001270500.1"));
        assertTrue(variant.getGene().getId().equals(3852));
        assertTrue(variant.getRefSeqGene().equals("CA6"));
        assertTrue(variant.getHgncGene().equals("CA6"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(5));
        assertTrue(variant.getIntronExonDistance().equals(-47));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9005897_9005899delAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001270500.1:c.1-47_3delAAA"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001270500.1:g.5_7delAAA"));

        variant = variants.get(2);

        assertTrue(variant.getLocatedVariant().getId().equals(476430841L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9005897));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("AAA"));
        assertTrue(variant.getId().getTranscript().equals("NM_001270501.1"));
        assertTrue(variant.getGene().getId().equals(3852));
        assertTrue(variant.getRefSeqGene().equals("CA6"));
        assertTrue(variant.getHgncGene().equals("CA6"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(5));
        assertTrue(variant.getIntronExonDistance().equals(-47));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9005897_9005899delAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001270501.1:c.1-47_3delAAA"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001270501.1:g.5_7delAAA"));

        variant = variants.get(3);

        assertTrue(variant.getLocatedVariant().getId().equals(476430841L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9005897));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("AAA"));
        assertTrue(variant.getId().getTranscript().equals("NM_001270502.1"));
        assertTrue(variant.getGene().getId().equals(3852));
        assertTrue(variant.getRefSeqGene().equals("CA6"));
        assertTrue(variant.getHgncGene().equals("CA6"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(5));
        assertTrue(variant.getIntronExonDistance().equals(-102));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9005897_9005899delAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001270502.1:c.1-102_3delAAA"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001270502.1:g.5_7delAAA"));

    }

    @Test
    public void testLocatedVariant491937061() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(491937061L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(491937061L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(11876525));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getId().getTranscript().equals("NM_001256959.1"));
        assertTrue(variant.getGene().getId().equals(5235));
        assertTrue(variant.getRefSeqGene().equals("CLCN6"));
        assertTrue(variant.getHgncGene().equals("CLCN6"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-147));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.11876525delC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001256959.1:c.148-147_148delC"));
        assertTrue(variant.getHgvsTranscript() == null);

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(491937061L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(11876525));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getId().getTranscript().equals("NM_001286.3"));
        assertTrue(variant.getGene().getId().equals(5235));
        assertTrue(variant.getRefSeqGene().equals("CLCN6"));
        assertTrue(variant.getHgncGene().equals("CLCN6"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-147));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.11876525delC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001286.3:c.214-147_214delC"));
        assertTrue(variant.getHgvsTranscript() == null);

        variant = variants.get(2);

        assertTrue(variant.getLocatedVariant().getId().equals(491937061L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(11876525));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getId().getTranscript().equals("NR_046428.1"));
        assertTrue(variant.getGene().getId().equals(5235));
        assertTrue(variant.getRefSeqGene().equals("CLCN6"));
        assertTrue(variant.getHgncGene().equals("CLCN6"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-147));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.11876525delC"));
        assertTrue(variant.getHgvsCodingSequence() == null);
        assertTrue(variant.getHgvsTranscript() == null);

    }

    @Test
    public void testLocatedVariant378427953() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(378427953L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(378427953L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(29473876));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("TTTTTTT"));
        assertTrue(variant.getId().getTranscript().equals("NM_005626.4"));
        assertTrue(variant.getGene().getId().equals(29273));
        assertTrue(variant.getRefSeqGene().equals("SRSF4"));
        assertTrue(variant.getHgncGene().equals("SRSF4"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition() == null);
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.29473876_29473882delTTTTTTT"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant435110898() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(435110898L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(435110898L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(113739326));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("ATT"));
        assertTrue(variant.getId().getTranscript().equals("NR_038846.1"));
        assertTrue(variant.getGene().getId().equals(102461));
        assertTrue(variant.getRefSeqGene().equals("LOC643441"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.113739326_113739328delATT"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant384944453() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(384944453L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(384944453L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(161195724));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("TTTTTTTT"));
        assertTrue(variant.getId().getTranscript().equals("XM_005245536.1"));
        assertTrue(variant.getGene().getId().equals(30972));
        assertTrue(variant.getRefSeqGene().equals("TOMM40L"));
        assertTrue(variant.getHgncGene().equals("TOMM40L"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.161195724_161195731delTTTTTTTT"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

}
