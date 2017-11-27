package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariants38InsTest extends AbstractAnnotateVariants38Test {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariants38InsTest.class);

    public AnnotateVariants38InsTest() {
        super();
    }

    @Test
    public void testLocatedVariant495918232() throws Exception {
        // ins, synonmous indel
        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(495918232L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_199352.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(495918232L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(63163825));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getId().getTranscript().equals("NM_199352.3"));
        assertTrue(variant.getRefSeqGene().equals("SLC22A25"));
        assertTrue(variant.getHgncGene().equals("SLC22A25"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1643));
        assertTrue(variant.getCodingSequencePosition().equals(1643));
        assertTrue(variant.getAminoAcidStart().equals(548));
        assertTrue(variant.getAminoAcidEnd().equals(549));
        assertTrue(variant.getOriginalAminoAcid().equals("*"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getId().getVariantEffect().equals("synonymous indel"));
        assertTrue(variant.getGene().getId().equals(27956));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.63163825_63163826insC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_199352.3:c.1642_1643insG"));
        assertTrue(variant.getHgvsTranscript().equals("NM_199352.3:g.1642_1643insG"));
        assertTrue(variant.getHgvsProtein().equals("NP_955384.3:p.*548fs"));

    }

    @Test
    public void testLocatedVariant546967178() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(546967178L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001277115.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(546967178L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000007.14"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(21543345));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getId().getTranscript().equals("NM_001277115.1"));
        assertTrue(variant.getRefSeqGene().equals("DNAH11"));
        assertTrue(variant.getHgncGene().equals("DNAH11"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(131));
        assertTrue(variant.getCodingSequencePosition().equals(100));
        assertTrue(variant.getAminoAcidStart().equals(34));
        assertTrue(variant.getAminoAcidEnd().equals(34));
        assertTrue(variant.getOriginalAminoAcid().equals("E"));
        assertTrue(variant.getFinalAminoAcid().equals("L"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(100));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(7165));
        assertTrue(variant.getReferenceAllele().equals("GA"));
        assertTrue(variant.getAlternateAllele().equals("TT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000007.14:g.21543345_21543346delinsTT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001277115.1:c.100_101delinsTT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001277115.1:g.131_132delinsTT"));
        assertTrue(variant.getHgvsProtein().equals("NP_001264044.1:p.Glu34Leu"));

    }

    @Test
    public void testLocatedVariant546749775() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(546749775L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001122633.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(546749775L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(210591913));
        assertTrue(variant.getVariantType().getId().equals("sub"));
        assertTrue(variant.getId().getTranscript().equals("NM_001122633.2"));
        assertTrue(variant.getRefSeqGene().equals("CPS1"));
        assertTrue(variant.getHgncGene().equals("CPS1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1130));
        assertTrue(variant.getCodingSequencePosition().equals(1048));
        assertTrue(variant.getAminoAcidStart().equals(350));
        assertTrue(variant.getAminoAcidEnd().equals(350));
        assertTrue(variant.getOriginalAminoAcid().equals("T"));
        assertTrue(variant.getFinalAminoAcid().equals("A"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-57));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(5786));
        assertTrue(variant.getReferenceAllele().equals("ACC"));
        assertTrue(variant.getAlternateAllele().equals("GCT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.12:g.210591913_210591915delinsGCT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001122633.2:c.1048_1050delinsGCT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001122633.2:g.1130_1132delinsGCT"));
        assertTrue(variant.getHgvsProtein().equals("NP_001116105.1:p.Thr350Ala"));

    }

    @Test
    public void testLocatedVariant514754385() throws Exception {
        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(514754385L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

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
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(37));
        assertTrue(variant.getId().getVariantEffect().equals("synonymous indel"));
        assertTrue(variant.getGene().getId().equals(23025));
        assertTrue(variant.getReferenceAllele().equals("CAA"));
        assertTrue(variant.getAlternateAllele().equals("GAG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000010.11:g.71818686_71818688delinsGAG"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001042465.2:c.1477_1479delinsCTC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001042465.2:g.1631_1633delinsCTC"));
        assertTrue(variant.getHgvsProtein().equals("NP_001035930.1:p.Leu493delinsLeu"));

    }

    @Test
    public void testLocatedVariant515074163() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(515074163L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001288713.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(515074163L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(20928476));
        assertTrue(variant.getVariantType().getId().equals("ins"));
        assertTrue(variant.getId().getTranscript().equals("NM_001288713.1"));
        assertTrue(variant.getRefSeqGene().equals("NELL1"));
        assertTrue(variant.getHgncGene().equals("NELL1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1251));
        assertTrue(variant.getCodingSequencePosition().equals(1078));
        assertTrue(variant.getAminoAcidStart().equals(360));
        assertTrue(variant.getAminoAcidEnd().equals(361));
        assertTrue(variant.getOriginalAminoAcid().equals("RP"));
        assertTrue(variant.getFinalAminoAcid().equals("RRK*P"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-4));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense indel"));
        assertTrue(variant.getGene().getId().equals(18892));
        assertTrue(variant.getAlternateAllele().equals("GACGTAAGT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.20928476_20928477insGACGTAAGT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001288713.1:c.1078_1079insGACGTAAGT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001288713.1:g.1251_1252insGACGTAAGT"));
        assertTrue(variant.getHgvsProtein().equals("NP_001275642.1:p.Arg360_Pro361delinsArgArgLys*Pro"));

    }

}