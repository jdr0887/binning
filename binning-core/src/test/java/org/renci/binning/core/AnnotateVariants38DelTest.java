package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariants38DelTest extends AbstractAnnotateVariants38Test {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariants38DelTest.class);

    public AnnotateVariants38DelTest() {
        super();
    }

    @Test
    public void testLocatedVariant492043345() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(492043345L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001310161.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(492043345L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(31794023));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001310161.1"));
        assertTrue(variant.getRefSeqGene().equals("PAX6"));
        assertTrue(variant.getHgncGene().equals("PAX6"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1078));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("boundary-crossing indel"));
        assertTrue(variant.getGene().getId().equals(21213));
        assertTrue(variant.getReferenceAllele().equals("TCTCGGTACCTGTAT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.31794023_31794037delTCTCGGTACCTGTAT"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant515077037() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(515077037L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001313726.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(515077037L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(26553245));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001313726.1"));
        assertTrue(variant.getRefSeqGene().equals("ANO3"));
        assertTrue(variant.getHgncGene().equals("ANO3"));
        assertTrue(variant.getLocationType().getId().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1527));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getId().getVariantEffect().equals("boundary-crossing indel"));
        assertTrue(variant.getGene().getId().equals(1013));
        assertTrue(variant.getReferenceAllele().equals("CAAGCCAAGAAA"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.26553245_26553256delCAAGCCAAGAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

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

}
