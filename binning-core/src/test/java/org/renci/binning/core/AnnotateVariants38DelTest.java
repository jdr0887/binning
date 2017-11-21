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
