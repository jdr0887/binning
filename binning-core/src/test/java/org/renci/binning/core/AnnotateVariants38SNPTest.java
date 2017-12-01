package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariants38SNPTest extends AbstractAnnotateVariants38Test {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariants38SNPTest.class);

    public AnnotateVariants38SNPTest() {
        super();
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

    @Test
    public void testLocatedVariant541791343() throws Exception {
        // snp splice-site-UTR-3 on - strand

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(541791343L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_000277.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(541791343L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000012.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(102840399));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_000277.1"));
        assertTrue(variant.getRefSeqGene().equals("PAH"));
        assertTrue(variant.getHgncGene().equals("PAH"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1315));
        assertTrue(variant.getIntronExonDistance().equals(1));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site-UTR-3"));
        assertTrue(variant.getGene().getId().equals(21084));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000012.12:g.102840399C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000277.1:c.1315+1G>A"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

    @Test
    public void testLocatedVariant565806505() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(565806505L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_178463.3")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(565806505L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000020.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(62570676));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_178463.3"));
        assertTrue(variant.getRefSeqGene().equals("MIR1-1HG"));
        assertTrue(variant.getHgncGene().equals("MIR1-1HG"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));

    }

}
