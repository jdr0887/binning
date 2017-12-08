package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariants37InsTest extends AbstractAnnotateVariants37Test {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariants37InsTest.class);

    public AnnotateVariants37InsTest() {
        super();
    }

    @Test
    public void testLocatedVariant385391408() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(385391408L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(385391408L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(1875858));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getId().getTranscript().equals("XM_005244811.1"));
        assertTrue(variant.getRefSeqGene().equals("KIAA1751"));
        assertTrue(variant.getHgncGene().equals("CFAP74"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(2554));
        assertTrue(variant.getCodingSequencePosition().equals(2398));
        assertTrue(variant.getAminoAcidStart().equals(799));
        assertTrue(variant.getAminoAcidEnd().equals(800));
        assertTrue(variant.getOriginalAminoAcid().equals("DV"));
        assertTrue(variant.getFinalAminoAcid().equals("DL"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(33));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(13879));
        assertTrue(variant.getReferenceAllele().equals("CA"));
        assertTrue(variant.getAlternateAllele().equals("GG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.1875858_1875859delinsGG"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005244811.1:c.2397_2398delinsCC"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005244811.1:g.2553_2554delinsCC"));
        assertTrue(variant.getHgvsProtein().equals("XP_005244868.1:p.Asp799_Val800delinsAspLeu"));
        //// assertTrue(variant.getNonCanonicalExon().equals(19));
        assertTrue(variant.getFeatureId().equals(0));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(385391408L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(1875858));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getId().getTranscript().equals("XM_005244812.1"));
        assertTrue(variant.getRefSeqGene().equals("KIAA1751"));
        assertTrue(variant.getHgncGene().equals("CFAP74"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(2554));
        assertTrue(variant.getCodingSequencePosition().equals(2398));
        assertTrue(variant.getAminoAcidStart().equals(799));
        assertTrue(variant.getAminoAcidEnd().equals(800));
        assertTrue(variant.getOriginalAminoAcid().equals("DV"));
        assertTrue(variant.getFinalAminoAcid().equals("DL"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(33));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(13879));
        assertTrue(variant.getReferenceAllele().equals("CA"));
        assertTrue(variant.getAlternateAllele().equals("GG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.1875858_1875859delinsGG"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005244812.1:c.2397_2398delinsCC"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005244812.1:g.2553_2554delinsCC"));
        assertTrue(variant.getHgvsProtein().equals("XP_005244869.1:p.Asp799_Val800delinsAspLeu"));
        //// assertTrue(variant.getNonCanonicalExon().equals(19));
        assertTrue(variant.getFeatureId().equals(0));

    }

    @Test
    public void testLocatedVariant386432694() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(386432694L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(386432694L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(78024350));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getId().getTranscript().equals("NM_012093.3"));
        assertTrue(variant.getRefSeqGene().equals("AK5"));
        assertTrue(variant.getHgncGene().equals("AK5"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(2632));
        assertTrue(variant.getCodingSequencePosition().equals(1606));
        assertTrue(variant.getAminoAcidStart().equals(536));
        assertTrue(variant.getAminoAcidEnd().equals(537));
        assertTrue(variant.getOriginalAminoAcid().equals("F*"));
        assertTrue(variant.getFinalAminoAcid().equals("YF*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-6));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(646));
        assertTrue(variant.getAlternateAllele().equals("ATT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.78024350_78024351insATT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_012093.3:c.1606_1607insATT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_012093.3:g.2632_2633insATT"));
        assertTrue(variant.getHgvsProtein().equals("NP_036225.2:p.Phe536_*537delinsTyrPhe*"));
        // // assertTrue(variant.getNonCanonicalExon().equals(13));
        assertTrue(variant.getFeatureId().equals(0));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(386432694L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(78024350));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getId().getTranscript().equals("NM_174858.2"));
        assertTrue(variant.getRefSeqGene().equals("AK5"));
        assertTrue(variant.getHgncGene().equals("AK5"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(2021));
        assertTrue(variant.getCodingSequencePosition().equals(1684));
        assertTrue(variant.getAminoAcidStart().equals(562));
        assertTrue(variant.getAminoAcidEnd().equals(563));
        assertTrue(variant.getOriginalAminoAcid().equals("F*"));
        assertTrue(variant.getFinalAminoAcid().equals("YF*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-6));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(646));
        assertTrue(variant.getAlternateAllele().equals("ATT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.78024350_78024351insATT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_174858.2:c.1684_1685insATT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_174858.2:g.2021_2022insATT"));
        assertTrue(variant.getHgvsProtein().equals("NP_777283.1:p.Phe562_*563delinsTyrPhe*"));
        // // assertTrue(variant.getNonCanonicalExon().equals(14));
        assertTrue(variant.getFeatureId().equals(0));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(386432694L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(78024350));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getId().getTranscript().equals("XM_005270739.1"));
        assertTrue(variant.getRefSeqGene().equals("AK5"));
        assertTrue(variant.getHgncGene().equals("AK5"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1868));
        assertTrue(variant.getCodingSequencePosition().equals(1612));
        assertTrue(variant.getAminoAcidStart().equals(538));
        assertTrue(variant.getAminoAcidEnd().equals(539));
        assertTrue(variant.getOriginalAminoAcid().equals("F*"));
        assertTrue(variant.getFinalAminoAcid().equals("YF*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-6));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(646));
        assertTrue(variant.getAlternateAllele().equals("ATT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.78024350_78024351insATT"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270739.1:c.1612_1613insATT"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270739.1:g.1868_1869insATT"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270796.1:p.Phe538_*539delinsTyrPhe*"));
        // // assertTrue(variant.getNonCanonicalExon().equals(14));
        assertTrue(variant.getFeatureId().equals(0));

    }

    @Test
    public void testLocatedVariant435249599() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(435249599L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(435249599L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(98416578));
        assertTrue(variant.getId().getTranscript().equals("NM_152309.2"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("CAC"));
        assertTrue(variant.getGene().getId().equals(21825));
        assertTrue(variant.getRefSeqGene().equals("PIK3AP1"));
        assertTrue(variant.getHgncGene().equals("PIK3AP1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(15));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(672));
        assertTrue(variant.getCodingSequencePosition().equals(544));
        assertTrue(variant.getAminoAcidStart().equals(182));
        assertTrue(variant.getAminoAcidEnd().equals(183));
        assertTrue(variant.getOriginalAminoAcid().equals("QP"));
        assertTrue(variant.getFinalAminoAcid().equals("VQP"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(-24));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.98416578_98416579insCAC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_152309.2:c.543_544insGTG"));
        assertTrue(variant.getHgvsTranscript().equals("NM_152309.2:g.671_672insGTG"));
        assertTrue(variant.getHgvsProtein().equals("NP_689522.2:p.Gln182_Pro183delinsValGlnPro"));

        variant = variants.get(1);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(435249599L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(98416578));
        assertTrue(variant.getId().getTranscript().equals("XM_005269498.1"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("CAC"));
        assertTrue(variant.getGene().getId().equals(21825));
        assertTrue(variant.getRefSeqGene().equals("PIK3AP1"));
        assertTrue(variant.getHgncGene().equals("PIK3AP1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(15));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(195));
        assertTrue(variant.getCodingSequencePosition().equals(10));
        assertTrue(variant.getAminoAcidStart().equals(4));
        assertTrue(variant.getAminoAcidEnd().equals(5));
        assertTrue(variant.getOriginalAminoAcid().equals("QP"));
        assertTrue(variant.getFinalAminoAcid().equals("VQP"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(10));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.98416578_98416579insCAC"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005269498.1:c.9_10insGTG"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005269498.1:g.194_195insGTG"));
        assertTrue(variant.getHgvsProtein().equals("XP_005269555.1:p.Gln4_Pro5delinsValGlnPro"));

        variant = variants.get(2);

        // assertTrue(variant.getId().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(435249599L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(98416578));
        assertTrue(variant.getId().getTranscript().equals("XM_005269499.1"));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("CAC"));
        assertTrue(variant.getGene().getId().equals(21825));
        assertTrue(variant.getRefSeqGene().equals("PIK3AP1"));
        assertTrue(variant.getHgncGene().equals("PIK3AP1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        // // assertTrue(variant.getNonCanonicalExon().equals(15));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(219));
        assertTrue(variant.getCodingSequencePosition().equals(10));
        assertTrue(variant.getAminoAcidStart().equals(4));
        assertTrue(variant.getAminoAcidEnd().equals(5));
        assertTrue(variant.getOriginalAminoAcid().equals("QP"));
        assertTrue(variant.getFinalAminoAcid().equals("VQP"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(10));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.10:g.98416578_98416579insCAC"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005269499.1:c.9_10insGTG"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005269499.1:g.218_219insGTG"));
        assertTrue(variant.getHgvsProtein().equals("XP_005269556.1:p.Gln4_Pro5delinsValGlnPro"));

    }

    @Test
    public void testLocatedVariant10401977() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(10401977L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(10401977L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(856704));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_241051.1"));
        assertTrue(variant.getGene().getId().equals(98945));
        assertTrue(variant.getRefSeqGene().equals("LOC101928801"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1735322));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1666));
        assertTrue(variant.getIntronExonDistance().equals(1186));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.856704_856705insG"));
        assertTrue(variant.getHgvsTranscript().equals("XR_241051.1:g.1665_1666insC"));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(10401977L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(856704));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_248814.1"));
        assertTrue(variant.getGene().getId().equals(98945));
        assertTrue(variant.getRefSeqGene().equals("LOC101928801"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1732660));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1666));
        assertTrue(variant.getIntronExonDistance().equals(1186));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.856704_856705insG"));
        assertTrue(variant.getHgvsTranscript().equals("XR_248814.1:g.1665_1666insC"));

        variant = variants.get(2);

        assertTrue(variant.getLocatedVariant().getId().equals(10401977L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(856704));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getId().getTranscript().equals("XR_250680.1"));
        assertTrue(variant.getGene().getId().equals(98945));
        assertTrue(variant.getRefSeqGene().equals("LOC101928801"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1790607));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1666));
        assertTrue(variant.getIntronExonDistance().equals(1186));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.856704_856705insG"));
        assertTrue(variant.getHgvsTranscript().equals("XR_250680.1:g.1665_1666insC"));

    }

    @Test
    public void testLocatedVariant438862902() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(438862902L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(438862902L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000013.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45373639));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals("GGA"));
        assertTrue(variant.getAlternateAllele().equals("TGG"));
        assertTrue(variant.getId().getTranscript().equals("NR_038433.2"));
        assertTrue(variant.getGene().getId().equals(58116));
        assertTrue(variant.getRefSeqGene().equals("LINC00330"));
        assertTrue(variant.getHgncGene().equals("LINC00330"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000013.10:g.45373639_45373641delinsTGG"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant387000526() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(387000526L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(387000526L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(176176504));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GTCGGGGCTCCCTGGCGAAGGGAGTCTGTCTGTCCCAAGCCGCTTTCGTTCCAGAAAACGTGCCAGAGGGCAGGCCGTCTTAATACAGTGAAAGAATTGAGGAGACTGGAATGAATGGTATAGGTGGTGGGGTGGTTTGAAAGACGGGAACGTAGCGGGCGGAAACTCTCGAAAAGCGACTGCGCAGGCGCGCGCCGGGCGCGGACGCTCACGGGTCACCCAGGGTATCCGCCCCAAGCGGAGAATATCGGCGCAAGCGCGGAGTAGCCGTGTCACGCATGCGTGCGAACGCACCACATTCCGGAACGGGAAGAAGGAGCTGTCTCTCTCTTCTGAGGGGTAGGCAGGTGCTTCCGAATCGCGGAATGTAGGTGCTCATAAGCTGGAGACCCACGGAAAGAACCTAGAAGCTCCGAGCGGTTGCAGACAGCACTGCATTGAGCCAACGACTCCTGCTCTACTGATGGTGATTCCACAGCTTTGTGAGGAATAAGTTAAGATGAAGTCAGTGAAGTTTTCTGCGAATTGCAAACATTGTAGCAAGTGTTACCAATTTTATTAGTTCGACCACGACTTCACTCCTATATTTTGAAAAGCTGTTTGCCAAATTGAACATAACAATAATGGGAAAACAGATTTTGCTCAATCAGACCCAGCTCAGTCGGAACCTAGGTTTATTGTGGATCTGATATAATTCTAGCACAAAACAAGGAAACCTAGCATTTAATACCACCAGAAAATATACAATTTCTGGTAGGCTTTTTTAGAATATTGAAAATTGATTATAGGCGGGCGCGGTGGCTCATGCCTGTCATCGCAGCACCTTGGGAGGCCTAGGGGGTCGGATCACCTGAGCCTGGAGGTGAGGATCAGGCTGAACCTGAGGTTAGAGACCAGCCTGGGCAAGATAGCGAAACCCCGTCTCTACAAAAAGTACAAAAAATTAGCTGGGCATGGTGGCAGCAGGCCTGTAGTCCCAGCTACTCGGG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getId().getTranscript().equals("XR_248664.1"));
        assertTrue(variant.getGene().getId().equals(98800));
        assertTrue(variant.getRefSeqGene().equals("LOC101930532"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.176176504_176177492delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(387000526L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(176176504));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GTCGGGGCTCCCTGGCGAAGGGAGTCTGTCTGTCCCAAGCCGCTTTCGTTCCAGAAAACGTGCCAGAGGGCAGGCCGTCTTAATACAGTGAAAGAATTGAGGAGACTGGAATGAATGGTATAGGTGGTGGGGTGGTTTGAAAGACGGGAACGTAGCGGGCGGAAACTCTCGAAAAGCGACTGCGCAGGCGCGCGCCGGGCGCGGACGCTCACGGGTCACCCAGGGTATCCGCCCCAAGCGGAGAATATCGGCGCAAGCGCGGAGTAGCCGTGTCACGCATGCGTGCGAACGCACCACATTCCGGAACGGGAAGAAGGAGCTGTCTCTCTCTTCTGAGGGGTAGGCAGGTGCTTCCGAATCGCGGAATGTAGGTGCTCATAAGCTGGAGACCCACGGAAAGAACCTAGAAGCTCCGAGCGGTTGCAGACAGCACTGCATTGAGCCAACGACTCCTGCTCTACTGATGGTGATTCCACAGCTTTGTGAGGAATAAGTTAAGATGAAGTCAGTGAAGTTTTCTGCGAATTGCAAACATTGTAGCAAGTGTTACCAATTTTATTAGTTCGACCACGACTTCACTCCTATATTTTGAAAAGCTGTTTGCCAAATTGAACATAACAATAATGGGAAAACAGATTTTGCTCAATCAGACCCAGCTCAGTCGGAACCTAGGTTTATTGTGGATCTGATATAATTCTAGCACAAAACAAGGAAACCTAGCATTTAATACCACCAGAAAATATACAATTTCTGGTAGGCTTTTTTAGAATATTGAAAATTGATTATAGGCGGGCGCGGTGGCTCATGCCTGTCATCGCAGCACCTTGGGAGGCCTAGGGGGTCGGATCACCTGAGCCTGGAGGTGAGGATCAGGCTGAACCTGAGGTTAGAGACCAGCCTGGGCAAGATAGCGAAACCCCGTCTCTACAAAAAGTACAAAAAATTAGCTGGGCATGGTGGCAGCAGGCCTGTAGTCCCAGCTACTCGGG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getId().getTranscript().equals("XR_250905.1"));
        assertTrue(variant.getGene().getId().equals(101813));
        assertTrue(variant.getRefSeqGene().equals("LOC101930156"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.176176504_176177492delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant392541331() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(392541331L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(392541331L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000006.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(30643316));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GAGATGGAGTCTCACTCTGCTGCCCAGGCTGGAGTGTAATGGCGCAATCTCAGCTCACTGCAAGCTCCACCTCCCGGGTTCATGCCATTCTCCTGCCTCAGCCTCCCGAGTAGCTGGGACTACAGGTGCCCACCACTATGCCCCGCTAATTTTTTGTATTTTTAGTAGAGACGGGGTTTCACCGTGTTAGCCAGGATGGTCTCAATCTCCTGACCTTATGATCCGCCTGCCTCGGCCTCCCAAAGTGCTGGGATCACAGGCGTGAGCCACCGTGCCCGGCCTTTAGGCCTTTAACGATATAAAATCCATTGTCTATCAGAGGGGAACCTTTTCCAGGAAACTGACTCTTGTACATACTTACTTCATTTTGCAGCAATTTCAGATTTAGTATTCGTAGCCCCAGCTCTTTAAGTAAGTATCCCTGGATTAGCCACATGGGTTGTGTCATACACTACCTAGCTGCCTTCATGGCAGCAGGCTTCTGAATACTAGAACCCTTCAACTCAAAGTGTCCTCTGTAATATTTTAACCCTTTTCTTCTATTCATTCATTTGTTGTCATTCATTCTAGAAATAATTCCGTGTCTACTAGTTGACAGGTACAGGATATTGCAGTGAATCCAGCTGATGTAGTCAGCCCTCATGGCACTTCCAGTCTAGTGGACACTTCAACTGCCCTTTCTCATGTCACCTGCTTGTCCTGCGTGAAACCCACGTGCAGCTTCCCAGACCCCTTTTGACATGTCAGTGCCGAGTTCCTGGTTCATCCCCCATCATTTTCCTCTCCCCCAGCCACCAGAGCCTCCCCTCACATACCCTTTTTTTTTCCCAAAGAAGGAGAAGCAGACGAG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getId().getTranscript().equals("NM_001134870.1"));
        assertTrue(variant.getGene().getId().equals(13888));
        assertTrue(variant.getRefSeqGene().equals("PPP1R18"));
        assertTrue(variant.getHgncGene().equals("PPP1R18"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000006.11:g.30643316_30644165delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(392541331L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000006.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(30643316));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GAGATGGAGTCTCACTCTGCTGCCCAGGCTGGAGTGTAATGGCGCAATCTCAGCTCACTGCAAGCTCCACCTCCCGGGTTCATGCCATTCTCCTGCCTCAGCCTCCCGAGTAGCTGGGACTACAGGTGCCCACCACTATGCCCCGCTAATTTTTTGTATTTTTAGTAGAGACGGGGTTTCACCGTGTTAGCCAGGATGGTCTCAATCTCCTGACCTTATGATCCGCCTGCCTCGGCCTCCCAAAGTGCTGGGATCACAGGCGTGAGCCACCGTGCCCGGCCTTTAGGCCTTTAACGATATAAAATCCATTGTCTATCAGAGGGGAACCTTTTCCAGGAAACTGACTCTTGTACATACTTACTTCATTTTGCAGCAATTTCAGATTTAGTATTCGTAGCCCCAGCTCTTTAAGTAAGTATCCCTGGATTAGCCACATGGGTTGTGTCATACACTACCTAGCTGCCTTCATGGCAGCAGGCTTCTGAATACTAGAACCCTTCAACTCAAAGTGTCCTCTGTAATATTTTAACCCTTTTCTTCTATTCATTCATTTGTTGTCATTCATTCTAGAAATAATTCCGTGTCTACTAGTTGACAGGTACAGGATATTGCAGTGAATCCAGCTGATGTAGTCAGCCCTCATGGCACTTCCAGTCTAGTGGACACTTCAACTGCCCTTTCTCATGTCACCTGCTTGTCCTGCGTGAAACCCACGTGCAGCTTCCCAGACCCCTTTTGACATGTCAGTGCCGAGTTCCTGGTTCATCCCCCATCATTTTCCTCTCCCCCAGCCACCAGAGCCTCCCCTCACATACCCTTTTTTTTTCCCAAAGAAGGAGAAGCAGACGAG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getId().getTranscript().equals("NM_133471.3"));
        assertTrue(variant.getGene().getId().equals(13888));
        assertTrue(variant.getRefSeqGene().equals("PPP1R18"));
        assertTrue(variant.getHgncGene().equals("PPP1R18"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000006.11:g.30643316_30644165delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant402249392() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(402249392L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(402249392L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000019.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(18208489));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "AGGGGCGGGGCCAGGCCGTGTCAGCTGACCGGCCTGGCCAATGGCACCTGGCGGCGGGGCCTGGCAGGCCGGGGCGGGGCCGGGGGCGGGCCTGGCGGCGCGGACTCCCGGG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getId().getTranscript().equals("XM_005259824.1"));
        assertTrue(variant.getGene().getId().equals(15410));
        assertTrue(variant.getRefSeqGene().equals("MAST3"));
        assertTrue(variant.getHgncGene().equals("MAST3"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000019.9:g.18208489_18208600delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

        variant = variants.get(1);

        assertTrue(variant.getLocatedVariant().getId().equals(402249392L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000019.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(18208489));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "AGGGGCGGGGCCAGGCCGTGTCAGCTGACCGGCCTGGCCAATGGCACCTGGCGGCGGGGCCTGGCAGGCCGGGGCGGGGCCGGGGGCGGGCCTGGCGGCGCGGACTCCCGGG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getId().getTranscript().equals("XM_005259825.1"));
        assertTrue(variant.getGene().getId().equals(15410));
        assertTrue(variant.getRefSeqGene().equals("MAST3"));
        assertTrue(variant.getHgncGene().equals("MAST3"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000019.9:g.18208489_18208600delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant403213080() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(403213080L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(403213080L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000021.8"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(33973624));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GAGACAAGAGTTTCGCTCTGTTGCCCAGGATAGAGTGCAGTGGCAGTGATCTTGGCTCACTGTAAGCTCCACCTCCCGGGTTCACGCCATTCTCCTGCCTCAGCCTCCCGAGTAGCTGGGACTACAGGCGCCTGCCACCATGCCCGGCTAATTTTTTGTATTTTTAGTAGAGACGGGGTTTCACCGTGTTAGCCTGGATGGTCTTGATCTCCTGACCTCGTGATCTGCCTGCCTCGGCCTCCCAAAGTGCTGGGATTACAGGCGTGAGCCACCGCGCCCGGCCAAAAGTTTAGTATTTTATCAAAGAAAACCATGACATACTAAATAGTCCTGAAACAAAAACGATTTACACATTTTC"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getId().getTranscript().equals("XM_005261007.1"));
        assertTrue(variant.getGene().getId().equals(3776));
        assertTrue(variant.getRefSeqGene().equals("C21orf59"));
        assertTrue(variant.getHgncGene().equals("C21orf59"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000021.8:g.33973624_33973981delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant403858749() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(403858749L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getId().getTranscript().compareTo(b.getId().getTranscript()));

        Variants_61_2 variant = variants.get(0);

        assertTrue(variant.getLocatedVariant().getId().equals(403858749L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000023.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48969879));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GAGACAGAGTCTAACTCTGTCCCCTGGGCTGGAGTGCAGTGGCGCAATCTGGGCTCACAGCAACCTCTGCCTCCTGGGTTCAAGCGATTCTCTTGCCTCAGCCTCCTGAGTAGCTAGGATTATAGGCGTGCACCACCATACCTCGGTAATTTTTTGTATTTTTATACTTTTAGTAGAGACAGGGTTTCACCTTGTTGGCCAGGCTGATCTTGAACTCCTGGGCTCAAGCGATCCATCCACCTCAGCCTCCTAAAGTGCTAGGATTACAGGCATGCACCACTGTGCCCAAGCTTATAGTATCTATATTCCAAGAGTGAGCTCACAATCAAATGAGTCCTTTAACTTGTCCTGTACCCTGAGCAGAAAAGCAAGGCTTAAAAGTGAAATGGTTCCCACCTCAGTCAACTCAGTTCCCACCTTAACTCGCAGCTCATCATACCACTTATGATTTCATCTTTCTATTAAG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getId().getTranscript().equals("NM_015698.5"));
        assertTrue(variant.getGene().getId().equals(10717));
        assertTrue(variant.getRefSeqGene().equals("GPKOW"));
        assertTrue(variant.getHgncGene().equals("GPKOW"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000023.10:g.48969879_48970344delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

}
