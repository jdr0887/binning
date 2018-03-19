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
    public void testLocatedVariant575289850() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(575289850L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001079911.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(575289850L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000004.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(87663355));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001079911.2"));
        assertTrue(variant.getRefSeqGene().equals("DMP1"));
        assertTrue(variant.getHgncGene().equals("DMP1"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1633));
        assertTrue(variant.getIntronExonDistance().equals(35));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000004.12:g.87663355G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001079911.2:g.1633G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001079911.2:c.1494+35G>A"));

    }

    @Test
    public void testLocatedVariant559726800() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(559726800L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_213607.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(559726800L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000017.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(44902821));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_213607.2"));
        assertTrue(variant.getRefSeqGene().equals("CCDC103"));
        assertTrue(variant.getHgncGene().equals("CCDC103"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(890));
        assertTrue(variant.getIntronExonDistance().equals(4));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000017.11:g.44902821G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_213607.2:g.890G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_213607.2:c.729+4G>A"));

    }

    @Test
    public void testLocatedVariant569046823() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(569046823L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_002246.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(569046823L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000002.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(26728581));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_002246.2"));
        assertTrue(variant.getRefSeqGene().equals("KCNK3"));
        assertTrue(variant.getHgncGene().equals("KCNK3"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1361));
        assertTrue(variant.getIntronExonDistance().equals(13));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.12:g.26728581G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_002246.2:g.1361G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_002246.2:c.1185+13G>A"));

    }

    @Test
    public void testLocatedVariant559726791() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(559726791L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001258399.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(559726791L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000017.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(44901658));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001258399.1"));
        assertTrue(variant.getRefSeqGene().equals("CCDC103"));
        assertTrue(variant.getHgncGene().equals("CCDC103"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(437));
        assertTrue(variant.getIntronExonDistance().equals(2));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000017.11:g.44901658T>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001258399.1:c.280+2T>C"));

    }

    @Test
    public void testLocatedVariant579238003() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(579238003L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("XR_926175.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(579238003L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000006.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(31356323));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("XR_926175.1"));
        assertTrue(variant.getRefSeqGene().equals("HLA-B"));
        assertTrue(variant.getHgncGene().equals("HLA-B"));
        assertTrue(variant.getLocationType().getId().equals("UTR"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(473));
        assertTrue(variant.getIntronExonDistance().equals(120));
        assertTrue(variant.getId().getVariantEffect().equals("UTR"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000006.12:g.31356323G>T"));
        assertTrue(variant.getHgvsTranscript().equals("XR_926175.1:g.473C>A"));

    }

    @Test
    public void testLocatedVariant558858366() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(558858366L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_017558.4")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(558858366L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000016.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(71203684));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_017558.4"));
        assertTrue(variant.getRefSeqGene().equals("HYDIN"));
        assertTrue(variant.getHgncGene().equals("HYDIN"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(162));
        assertTrue(variant.getIntronExonDistance().equals(-16766));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000016.10:g.71203684T>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_017558.4:c.-23-16766A>G"));

    }

    @Test
    public void testLocatedVariant564422786() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(564422786L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_057176.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(564422786L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(55008652));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_057176.2"));
        assertTrue(variant.getRefSeqGene().equals("BSND"));
        assertTrue(variant.getHgncGene().equals("BSND"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(1230));
        assertTrue(variant.getIntronExonDistance().equals(24));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.11:g.55008652A>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_057176.2:c.963+24A>C"));
        assertTrue(variant.getHgvsTranscript().equals("NM_057176.2:g.1230A>C"));

    }

    @Test
    public void testLocatedVariant564422644() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(564422644L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_057176.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(564422644L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000001.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(54999070));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_057176.2"));
        assertTrue(variant.getRefSeqGene().equals("BSND"));
        assertTrue(variant.getHgncGene().equals("BSND"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(537));
        assertTrue(variant.getIntronExonDistance().equals(-117));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.11:g.54999070T>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_057176.2:c.411-117T>C"));
        assertTrue(variant.getHgvsTranscript().equals("NM_057176.2:g.537T>C"));

    }

    @Test
    public void testLocatedVariant559081628() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(559081628L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_002661.4")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(559081628L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000016.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(81783128));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_002661.4"));
        assertTrue(variant.getRefSeqGene().equals("PLCG2"));
        assertTrue(variant.getHgncGene().equals("PLCG2"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(168));
        assertTrue(variant.getIntronExonDistance().equals(-2815));
        assertTrue(variant.getId().getVariantEffect().equals("intron"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000016.10:g.81783128C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_002661.4:c.-46-2815C>T"));

    }

    @Test
    public void testLocatedVariant571354208() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(571354208L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_020166.4")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(571354208L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000003.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(183099540));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_020166.4"));
        assertTrue(variant.getRefSeqGene().equals("MCCC1"));
        assertTrue(variant.getHgncGene().equals("MCCC1"));
        assertTrue(variant.getLocationType().getId().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(48));
        assertTrue(variant.getIntronExonDistance().equals(-100));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000003.12:g.183099540C>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_020166.4:c.148-100G>C"));
        assertTrue(variant.getHgvsTranscript().equals("NM_020166.4:g.48G>C"));

    }

    @Test
    public void testLocatedVariant562030574() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(562030574L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001256017.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(562030574L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000019.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(55014228));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001256017.2"));
        assertTrue(variant.getRefSeqGene().equals("GP6"));
        assertTrue(variant.getHgncGene().equals("GP6"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1687));
        assertTrue(variant.getIntronExonDistance().equals(693));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getGene().getId().equals(10678));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000019.10:g.55014228T>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001256017.2:c.994+693A>G"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001256017.2:g.1687A>G"));

    }

    @Test
    public void testLocatedVariant556491808() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(556491808L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_145112.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(556491808L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000014.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(65076384));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_145112.2"));
        assertTrue(variant.getRefSeqGene().equals("MAX"));
        assertTrue(variant.getHgncGene().equals("MAX"));
        assertTrue(variant.getLocationType().getId().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(753));
        assertTrue(variant.getIntronExonDistance().equals(92));
        assertTrue(variant.getId().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getGene().getId().equals(15427));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000014.9:g.65076384C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_145112.2:c.661+92G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_145112.2:g.753G>A"));

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
        assertTrue(variant.getIntronExonDistance().equals(1));
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
        assertTrue(variant.getIntronExonDistance().equals(3));
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
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001135091.1:c.924+25C>T"));
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
        assertTrue(variant.getHgvsCodingSequence().equals("NM_025080.3:c.-10-1G>A"));
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
        assertTrue(variant.getHgvsCodingSequence().equals("NM_020869.3:c.-88-1G>C"));
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
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001081491.1:c.3146-2A>T"));
        assertTrue(variant.getHgvsTranscript() == null);
        assertTrue(variant.getHgvsProtein() == null);

    }

    @Test
    public void testLocatedVariant497776734() throws Exception {
        // snp splice-site on - strand

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
        assertTrue(variant.getTranscriptPosition().equals(1270));
        assertTrue(variant.getIntronExonDistance().equals(-1));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site"));
        assertTrue(variant.getGene().getId().equals(8151));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.12:g.137048861C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001246.3:c.1215-1G>A"));
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
        assertTrue(variant.getTranscriptPosition().equals(1787));
        assertTrue(variant.getIntronExonDistance().equals(1));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site"));
        assertTrue(variant.getGene().getId().equals(21084));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000012.12:g.102840399C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000277.1:c.1314+1G>A"));
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

    @Test
    public void testLocatedVariant595484703() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(595484703L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001301244.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(595484703L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000015.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(63043705));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_001301244.1"));
        assertTrue(variant.getRefSeqGene().equals("TPM1"));
        assertTrue(variant.getHgncGene().equals("TPM1"));
        assertTrue(variant.getLocationType().getId().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getId().getVariantEffect().equals("splice-site"));

    }

    @Test
    public void testLocatedVariant553562444() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(553562444L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_173600.2")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(553562444L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000012.12"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(40484948));
        assertTrue(variant.getVariantType().getId().equals("snp"));
        assertTrue(variant.getId().getTranscript().equals("NM_173600.2"));
        assertTrue(variant.getRefSeqGene().equals("MUC19"));
        assertTrue(variant.getHgncGene().equals("MUC19"));
        assertTrue(variant.getLocationType().getId().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(12050));
        assertTrue(variant.getCodingSequencePosition().equals(11996));
        assertTrue(variant.getAminoAcidStart().equals(3999));
        assertTrue(variant.getAminoAcidEnd().equals(4000));
        assertTrue(variant.getOriginalAminoAcid().equals("A"));
        assertTrue(variant.getFinalAminoAcid().equals("V"));
        assertTrue(variant.getIntronExonDistance().equals(5896));
        assertTrue(variant.getId().getVariantEffect().equals("missense"));
        assertTrue(variant.getGene().getId().equals(18181));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000012.12:g.40484948C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_173600.2:c.11996C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_173600.2:g.12050C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_775871.2:p.Ala3999Val"));

    }

    @Test
    public void testLocatedVariant527545933() throws Exception {

        LocatedVariant locatedVariant = daoBean.getLocatedVariantDAO().findById(527545933L);
        logger.info(locatedVariant.toString());

        List<Variants_80_4> variants = annotateLocatedVariant(locatedVariant);

        Variants_80_4 variant = variants.stream().filter(a -> a.getId().getTranscript().equals("NM_001243182.1")).findFirst().get();
        logger.info(variant.toString());

        assertTrue(variant.getLocatedVariant().getId().equals(527545933L));
        assertTrue(variant.getGenomeRefSeq().getId().equals("NC_000013.11"));

    }

}
