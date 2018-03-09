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
    public void testLocatedVariant522364463() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(522364463L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_015627.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(522364463L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(25562853));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_015627.2"));
        assertTrue(variant.getRefSeqGene().equals("LDLRAP1"));
        assertTrue(variant.getHgncGene().equals("LDLRAP1"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(646));
        assertTrue(variant.getIntronExonDistance().equals(137));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getGene().getId().equals(14604));
        assertTrue(variant.getReferenceAllele().equals("GA"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.11:g.25562853_25562854delGA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_015627.2:c.532+137_532+138del"));

    }

    @Test
    public void testLocatedVariant546992721() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(546992721L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_000492.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(546992721L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000007.14"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(117559592));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_000492.3"));
        assertTrue(variant.getRefSeqGene().equals("CFTR"));
        assertTrue(variant.getHgncGene().equals("CFTR"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1653));
        assertTrue(variant.getIntronExonDistance().equals(-64));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(4960));
        assertTrue(variant.getReferenceAllele().equals("CTT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000007.14:g.117559592_117559594delCTT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000492.3:c.1521_1523delCTT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_000492.3:g.1653_1655delCTT"));
        assertTrue(variant.getHgvsProtein().equals("NP_000483.3:p.Phe508del"));

    }

    @Test
    public void testLocatedVariant522371209() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(522371209L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("XM_006720981.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(522371209L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000016.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(2038418));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("XM_006720981.1"));
        assertTrue(variant.getRefSeqGene().equals("SLC9A3R2"));
        assertTrue(variant.getHgncGene().equals("SLC9A3R2"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));

    }

    @Test
    public void testLocatedVariant546704464() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(546704464L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_194322.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(546704464L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(26477955));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_194322.2"));
        assertTrue(variant.getRefSeqGene().equals("OTOF"));
        assertTrue(variant.getHgncGene().equals("OTOF"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(88));
        assertTrue(variant.getIntronExonDistance().equals(158));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getGene().getId().equals(20932));
        assertTrue(variant.getReferenceAllele().equals("GG"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.12:g.26477955_26477956delGG"));
        assertTrue(variant.getHgvsTranscript().equals("NM_194322.2:g.87_88delCC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_194322.2:c.-60_-59delGG"));

    }

    @Test
    public void testLocatedVariant492043345() throws Exception {
        // boundary crossing, negative strand
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
        // boundary crossing, positive strand

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
        // non-frameshifting indel, positive strand

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
        assertTrue(variant.getAminoAcidEnd().equals(102));
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
    public void testLocatedVariant515077154() throws Exception {
        // non-frameshifting indel, negative strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(515077154L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001135091.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(515077154L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(26563179));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001135091.1"));
        assertTrue(variant.getRefSeqGene().equals("MUC15"));
        assertTrue(variant.getHgncGene().equals("MUC15"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1135));
        assertTrue(variant.getCodingSequencePosition().equals(862));
        assertTrue(variant.getAminoAcidStart().equals(287));
        assertTrue(variant.getAminoAcidEnd().equals(288));
        assertTrue(variant.getOriginalAminoAcid().equals("LC"));
        assertTrue(variant.getFinalAminoAcid().equals("C"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-64));
        assertTrue(variant.getId().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getGene().getId().equals(18178));
        assertTrue(variant.getReferenceAllele().equals("ACA"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.26563179_26563181delACA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001135091.1:c.860_862delTGT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001135091.1:g.1133_1135delTGT"));
        assertTrue(variant.getHgvsProtein().equals("NP_001128563.1:p.Leu287del"));

    }

    @Test
    public void testLocatedVariant515118839() throws Exception {
        // nonsense indel, negative strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(515118839L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001317231.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(515118839L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(47618878));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001317231.1"));
        assertTrue(variant.getRefSeqGene().equals("MTCH2"));
        assertTrue(variant.getHgncGene().equals("MTCH2"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1056));
        assertTrue(variant.getCodingSequencePosition().equals(867));
        assertTrue(variant.getAminoAcidStart().equals(289));
        assertTrue(variant.getAminoAcidEnd().equals(290));
        assertTrue(variant.getOriginalAminoAcid().equals("VP"));
        assertTrue(variant.getFinalAminoAcid().equals("P"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(42));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense indel"));
        assertTrue(variant.getGene().getId().equals(18022));
        assertTrue(variant.getReferenceAllele().equals("GAC"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.47618878_47618880delGAC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001317231.1:c.865_867delGTC"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001317231.1:g.1054_1056delGTC"));
        assertTrue(variant.getHgvsProtein().equals("NP_001304160.1:p.Val289del"));

    }

    @Test
    public void testLocatedVariant500931807() throws Exception {
        // nonsense indel, positive strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(500931807L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001300787.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(500931807L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(61958184));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001300787.1"));
        assertTrue(variant.getRefSeqGene().equals("BEST1"));
        assertTrue(variant.getHgncGene().equals("BEST1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1145));
        assertTrue(variant.getCodingSequencePosition().equals(573));
        assertTrue(variant.getAminoAcidStart().equals(191));
        assertTrue(variant.getAminoAcidEnd().equals(192));
        assertTrue(variant.getOriginalAminoAcid().equals("CL"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(39));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense indel"));
        assertTrue(variant.getGene().getId().equals(2105));
        assertTrue(variant.getReferenceAllele().equals("TCT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.61958184_61958186delTCT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001300787.1:c.573_575delTCT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001300787.1:g.1145_1147delTCT"));
        assertTrue(variant.getHgvsProtein().equals("NP_001287716.1:p.Cys191_Leu192delins*"));

    }

    @Test
    public void testLocatedVariant() throws Exception {
        // synonymous indel, negative strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(500931807L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001300787.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(500931807L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000011.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(61958184));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getId().getTranscript().equals("NM_001300787.1"));
        assertTrue(variant.getRefSeqGene().equals("BEST1"));
        assertTrue(variant.getHgncGene().equals("BEST1"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1145));
        assertTrue(variant.getCodingSequencePosition().equals(573));
        assertTrue(variant.getAminoAcidStart().equals(191));
        assertTrue(variant.getAminoAcidEnd().equals(192));
        assertTrue(variant.getOriginalAminoAcid().equals("CL"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.TRUE));
        assertTrue(variant.getIntronExonDistance().equals(39));
        assertTrue(variant.getId().getVariantEffect().equals("nonsense indel"));
        assertTrue(variant.getGene().getId().equals(2105));
        assertTrue(variant.getReferenceAllele().equals("TCT"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.10:g.61958184_61958186delTCT"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001300787.1:c.573_575delTCT"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001300787.1:g.1145_1147delTCT"));
        assertTrue(variant.getHgvsProtein().equals("NP_001287716.1:p.Cys191_Leu192delins*"));

    }

    @Test
    public void testLocatedVariant547749568() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(547749568L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NR_038242.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(547749568L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000012.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(95282804));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
    }

    @Test
    public void testLocatedVariant595743242() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(595743242L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("XM_006716262.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(595743242L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000008.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(12185357));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
    }

    @Test
    public void testLocatedVariant526126533() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(526126533L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_181612.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(526126533L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000021.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(30541663));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
    }

    @Test
    public void testLocatedVariant526377012() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(526377012L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("XM_006720601.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(526377012L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000015.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(78266243));
        assertTrue(variant.getVariantType().getId().equals("del"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
    }

}
