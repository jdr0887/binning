package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Range;
import org.junit.Test;
import org.renci.canvas.binning.core.grch37.VariantsFactory;
import org.renci.canvas.dao.jpa.CANVASDAOManager;
import org.renci.canvas.dao.refseq.model.TranscriptMaps;
import org.renci.canvas.dao.refseq.model.TranscriptMapsExons;
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariantsTest {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariantsTest.class);

    private static final CANVASDAOManager daoMgr = CANVASDAOManager.getInstance();

    private List<Variants_61_2> annotateLocatedVariant(LocatedVariant locatedVariant) throws Exception {
        List<Variants_61_2> variants = new ArrayList<>();
        List<TranscriptMaps> transcriptMapsList = daoMgr.getDAOBean().getTranscriptMapsDAO()
                .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(2, "61",
                        locatedVariant.getGenomeRefSeq().getVerAccession(), locatedVariant.getPosition());
        logger.info("transcriptMapsList.size(): {}", transcriptMapsList.size());

        if (CollectionUtils.isNotEmpty(transcriptMapsList)) {
            // handling non boundary crossing variants (intron/exon/utr*)

            Map<String, List<TranscriptMaps>> transcriptMap = new HashMap<String, List<TranscriptMaps>>();
            for (TranscriptMaps tMap : transcriptMapsList) {
                if (!transcriptMap.containsKey(tMap.getTranscript().getVersionId())) {
                    transcriptMap.put(tMap.getTranscript().getVersionId(), new ArrayList<TranscriptMaps>());
                }
                transcriptMap.get(tMap.getTranscript().getVersionId()).add(tMap);
            }
            TranscriptMaps remove = null;
            for (String key : transcriptMap.keySet()) {
                if (transcriptMap.get(key).size() > 1) {
                    transcriptMap.get(key).sort((a, b) -> a.getMapCount().compareTo(b.getMapCount()));
                    remove = transcriptMap.get(key).get(0);
                    logger.info("removing duplicate: {}", remove.toString());
                    transcriptMapsList.remove(remove);
                }
            }
            transcriptMapsList.sort((a, b) -> b.getTranscript().getVersionId().compareTo(a.getTranscript().getVersionId()));

            for (TranscriptMaps tMap : transcriptMapsList) {

                List<TranscriptMapsExons> transcriptMapsExonsList = daoMgr.getDAOBean().getTranscriptMapsExonsDAO()
                        .findByTranscriptMapsId(tMap.getId());

                TranscriptMapsExons transcriptMapsExons = null;
                if (CollectionUtils.isNotEmpty(transcriptMapsExonsList)) {
                    for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                        Range<Integer> contigRange = Range.between(exon.getContigStart(), exon.getContigEnd());
                        if (contigRange.contains(locatedVariant.getPosition())) {
                            transcriptMapsExons = exon;
                            logger.info(transcriptMapsExons.toString());
                            break;
                        }
                    }
                }

                List<TranscriptMaps> mapsList = daoMgr.getDAOBean().getTranscriptMapsDAO()
                        .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(2, "61", tMap.getTranscript().getVersionId());

                Variants_61_2 variant = null;

                if (transcriptMapsExons == null) {
                    variant = VariantsFactory.createIntronicVariant(daoMgr.getDAOBean(), "61", locatedVariant, mapsList, tMap,
                            transcriptMapsExonsList);
                } else {

                    if ((transcriptMapsExons.getContigEnd().equals(locatedVariant.getPosition()) && "-".equals(tMap.getStrand()))
                            || (transcriptMapsExons.getContigStart().equals(locatedVariant.getPosition())
                                    && "+".equals(tMap.getStrand()))) {
                        variant = VariantsFactory.createBorderCrossingVariant(daoMgr.getDAOBean(), "61", locatedVariant, tMap,
                                mapsList, transcriptMapsExonsList, transcriptMapsExons);
                    } else {
                        variant = VariantsFactory.createExonicVariant(daoMgr.getDAOBean(), "61", locatedVariant, mapsList,
                                transcriptMapsExonsList, transcriptMapsExons);
                    }

                }
                variants.add(variant);
            }

        } else {

            // try searching by adjusting for length of locatedVariant.getSeq()...could be intron/exon boundary crossing
            transcriptMapsList = daoMgr.getDAOBean().getTranscriptMapsDAO()
                    .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(2, "61",
                            locatedVariant.getGenomeRefSeq().getVerAccession(),
                            locatedVariant.getPosition() + locatedVariant.getRef().length() - 1);

            if (CollectionUtils.isNotEmpty(transcriptMapsList)) {

                Map<String, List<TranscriptMaps>> transcriptMap = new HashMap<String, List<TranscriptMaps>>();
                for (TranscriptMaps tMap : transcriptMapsList) {
                    if (!transcriptMap.containsKey(tMap.getTranscript().getVersionId())) {
                        transcriptMap.put(tMap.getTranscript().getVersionId(), new ArrayList<TranscriptMaps>());
                    }
                    transcriptMap.get(tMap.getTranscript().getVersionId()).add(tMap);
                }
                TranscriptMaps remove = null;
                for (String key : transcriptMap.keySet()) {
                    if (transcriptMap.get(key).size() > 1) {
                        transcriptMap.get(key).sort((a, b) -> a.getMapCount().compareTo(b.getMapCount()));
                        remove = transcriptMap.get(key).get(0);
                        logger.info("removing duplicate: {}", remove.toString());
                        transcriptMapsList.remove(remove);
                    }
                }
                transcriptMapsList.sort((a, b) -> b.getTranscript().getVersionId().compareTo(a.getTranscript().getVersionId()));

                for (TranscriptMaps tMap : transcriptMapsList) {

                    List<TranscriptMapsExons> transcriptMapsExonsList = daoMgr.getDAOBean().getTranscriptMapsExonsDAO()
                            .findByTranscriptMapsId(tMap.getId());

                    TranscriptMapsExons transcriptMapsExons = null;
                    if (CollectionUtils.isNotEmpty(transcriptMapsExonsList)) {
                        for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                            Range<Integer> contigRange = Range.between(exon.getContigStart(), exon.getContigEnd());
                            if (contigRange.contains(locatedVariant.getPosition() + locatedVariant.getRef().length() - 1)) {
                                transcriptMapsExons = exon;
                                logger.info(transcriptMapsExons.toString());
                                break;
                            }
                        }
                    }

                    List<TranscriptMaps> mapsList = daoMgr.getDAOBean().getTranscriptMapsDAO()
                            .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(2, "61", tMap.getTranscript().getVersionId());

                    Variants_61_2 variant = VariantsFactory.createBorderCrossingVariant(daoMgr.getDAOBean(), "61", locatedVariant,
                            tMap, mapsList, transcriptMapsExonsList, transcriptMapsExons);
                    variants.add(variant);
                }
            } else {
                transcriptMapsList = daoMgr.getDAOBean().getTranscriptMapsDAO()
                        .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(2, "61",
                                locatedVariant.getGenomeRefSeq().getVerAccession(),
                                locatedVariant.getPosition() - locatedVariant.getRef().length());
                if (CollectionUtils.isNotEmpty(transcriptMapsList)) {

                    Map<String, List<TranscriptMaps>> transcriptMap = new HashMap<String, List<TranscriptMaps>>();
                    for (TranscriptMaps tMap : transcriptMapsList) {
                        if (!transcriptMap.containsKey(tMap.getTranscript().getVersionId())) {
                            transcriptMap.put(tMap.getTranscript().getVersionId(), new ArrayList<TranscriptMaps>());
                        }
                        transcriptMap.get(tMap.getTranscript().getVersionId()).add(tMap);
                    }
                    TranscriptMaps remove = null;
                    for (String key : transcriptMap.keySet()) {
                        if (transcriptMap.get(key).size() > 1) {
                            transcriptMap.get(key).sort((a, b) -> a.getMapCount().compareTo(b.getMapCount()));
                            remove = transcriptMap.get(key).get(0);
                            logger.info("removing duplicate: {}", remove.toString());
                            transcriptMapsList.remove(remove);
                        }
                    }
                    transcriptMapsList.sort((a, b) -> b.getTranscript().getVersionId().compareTo(a.getTranscript().getVersionId()));

                    for (TranscriptMaps tMap : transcriptMapsList) {

                        List<TranscriptMapsExons> transcriptMapsExonsList = daoMgr.getDAOBean().getTranscriptMapsExonsDAO()
                                .findByTranscriptMapsId(tMap.getId());

                        TranscriptMapsExons transcriptMapsExons = null;
                        if (CollectionUtils.isNotEmpty(transcriptMapsExonsList)) {
                            for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                                Range<Integer> contigRange = Range.between(exon.getContigStart(), exon.getContigEnd());
                                if (contigRange.contains(locatedVariant.getPosition() - locatedVariant.getRef().length())) {
                                    transcriptMapsExons = exon;
                                    logger.info(transcriptMapsExons.toString());
                                    break;
                                }
                            }
                        }

                        List<TranscriptMaps> mapsList = daoMgr.getDAOBean().getTranscriptMapsDAO()
                                .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(2, "61", tMap.getTranscript().getVersionId());

                        Variants_61_2 variant = VariantsFactory.createBorderCrossingVariant(daoMgr.getDAOBean(), "61", locatedVariant,
                                tMap, mapsList, transcriptMapsExonsList, transcriptMapsExons);
                        variants.add(variant);
                    }
                }
            }

        }

        return variants;
    }

    @Test
    public void testLocatedVariant491939773() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(491939773L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491939773L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000013.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(32912703));
        assertTrue(variant.getKey().getTranscript().equals("NM_000059.3"));
        assertTrue(variant.getKey().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getGene().getId().equals(2256));
        assertTrue(variant.getRefSeqGene().equals("BRCA2"));
        assertTrue(variant.getHgncGene().equals("BRCA2"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getNonCanonicalExon().equals(10));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(4438));
        assertTrue(variant.getCodingSequencePosition().equals(4211));
        assertTrue(variant.getAminoAcidStart().equals(1404));
        assertTrue(variant.getAminoAcidEnd().equals(1405));
        assertTrue(variant.getOriginalAminoAcid().equals("S"));
        assertTrue(variant.getFinalAminoAcid().equals("*"));
        assertTrue(variant.getFrameshift().equals(Boolean.FALSE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(2302));
        assertTrue(variant.getHgvsGenomic().equals("NC_000013.10:g.32912703C>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000059.3:c.4211C>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_000059.3:g.4438C>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_000050.2:p.Ser1404*"));

    }

    @Test
    public void testLocatedVariant489265654() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(489265654L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(489265654L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000017.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(59529156));
        assertTrue(variant.getKey().getTranscript().equals("XM_005257838.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("splice-site-UTR-5"));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getGene().getId().equals(29997));
        assertTrue(variant.getRefSeqGene().equals("TBX4"));
        assertTrue(variant.getHgncGene().equals("TBX4"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000017.10:g.59529156T>A"));
    }

    @Test
    public void testLocatedVariant8801171() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(8801171L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(8801171L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(17087245));
        assertTrue(variant.getKey().getTranscript().equals("NM_001271733.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("splice-site"));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(17942));
        assertTrue(variant.getRefSeqGene().equals("MST1L"));
        assertTrue(variant.getHgncGene().equals("MST1L"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(1));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.17087245delC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001271733.1:c.338+1_338delG"));

    }

    @Test
    public void testLocatedVariant380297473() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(380297473L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(380297473L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000012.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(132981749));
        assertTrue(variant.getKey().getTranscript().equals("XM_005266193.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("splice-site"));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getGene().getId().equals(101342));
        assertTrue(variant.getRefSeqGene().equals("LOC101928629"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000012.11:g.132981749T>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005266193.1:c.72+2T>G"));
    }

    @Test
    public void testLocatedVariant427005787() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(427005787L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(427005787L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000011.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(62341477));
        assertTrue(variant.getKey().getTranscript().equals("XM_005274188.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("splice-site-UTR-3"));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(32502));
        assertTrue(variant.getRefSeqGene().equals("TUT1"));
        assertTrue(variant.getHgncGene().equals("TUT1"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.9:g.62341477T>C"));

        variant = variants.get(1);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(427005787L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000011.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(62341477));
        assertTrue(variant.getKey().getTranscript().equals("XR_247209.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("splice-site-UTR"));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(32502));
        assertTrue(variant.getRefSeqGene().equals("TUT1"));
        assertTrue(variant.getHgncGene().equals("TUT1"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000011.9:g.62341477T>C"));

    }

    @Test
    public void testLocatedVariant492018867() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(492018867L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(492018867L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000019.9"));
        // assertTrue(variant.getLocatedVariant().getPosition().equals(39056271));
        // assertTrue(variant.getKey().getTranscript().equals("NM_000540.2"));
        // assertTrue(variant.getKey().getVariantEffect().equals("splice-site-UTR-3"));
        // assertTrue(variant.getVariantType().getName().equals("snp"));
        // assertTrue(variant.getReferenceAllele().equals("T"));
        // assertTrue(variant.getAlternateAllele().equals("C"));
        // assertTrue(variant.getGene().getId().equals(32502));
        // assertTrue(variant.getRefSeqGene().equals("RYR1"));
        // assertTrue(variant.getHgncGene().equals("FPR3"));
        // assertTrue(variant.getLocationType().getName().equals("exon"));
        // assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getIntronExonDistance().equals(-2));
        // assertTrue(variant.getHgvsGenomic().equals("NC_000011.9:g.62341477T>C"));
        //
        // variant = variants.get(1);
        // logger.info(variant.toString());
        // // assertTrue(variant.getKey().getMapNumber().equals(1));
        // assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        // assertTrue(variant.getLocatedVariant().getId().equals(427005787L));
        // assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000011.9"));
        // assertTrue(variant.getLocatedVariant().getPosition().equals(62341477));
        // assertTrue(variant.getKey().getTranscript().equals("XR_247209.1"));
        // assertTrue(variant.getKey().getVariantEffect().equals("splice-site-UTR"));
        // assertTrue(variant.getVariantType().getName().equals("snp"));
        // assertTrue(variant.getReferenceAllele().equals("T"));
        // assertTrue(variant.getAlternateAllele().equals("C"));
        // assertTrue(variant.getGene().getId().equals(32502));
        // assertTrue(variant.getRefSeqGene().equals("TUT1"));
        // assertTrue(variant.getHgncGene().equals("TUT1"));
        // assertTrue(variant.getLocationType().getName().equals("intron"));
        // assertTrue(variant.getStrand().equals("-"));
        // assertTrue(variant.getIntronExonDistance().equals(-2));
        // assertTrue(variant.getHgvsGenomic().equals("NC_000011.9:g.62341477T>C"));

    }

    @Test
    public void testLocatedVariant52648159() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(52648159L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(52648159L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000009.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(139510318));
        assertTrue(variant.getKey().getTranscript().equals("XR_245344.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("splice-site-UTR"));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(100680));
        assertTrue(variant.getRefSeqGene().equals("LOC101928581"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.11:g.139510318T>C"));

        variant = variants.get(1);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(52648159L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000009.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(139510318));
        assertTrue(variant.getKey().getTranscript().equals("XR_250563.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("splice-site-UTR"));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(100680));
        assertTrue(variant.getRefSeqGene().equals("LOC101928581"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.11:g.139510318T>C"));

        variant = variants.get(2);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(52648159L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000009.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(139510318));
        assertTrue(variant.getKey().getTranscript().equals("XR_252675.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("splice-site-UTR"));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getGene().getId().equals(100680));
        assertTrue(variant.getRefSeqGene().equals("LOC101928581"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-2));
        assertTrue(variant.getHgvsGenomic().equals("NC_000009.11:g.139510318T>C"));

    }

    @Test
    public void testLocatedVariant420518626() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(420518626L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(420518626L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99126522));
        assertTrue(variant.getKey().getTranscript().equals("NM_001145114.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("CTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(8));
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
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(420518626L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99126522));
        assertTrue(variant.getKey().getTranscript().equals("NM_015179.3"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("CTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(8));
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
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(420518626L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99126522));
        assertTrue(variant.getKey().getTranscript().equals("XM_005269659.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("CTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(8));
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

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(434113836L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(434113836L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99160113));
        assertTrue(variant.getKey().getTranscript().equals("NM_001145114.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("GGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(31));
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
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(434113836L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99160113));
        assertTrue(variant.getKey().getTranscript().equals("NM_015179.3"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("GGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(33));
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
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(434113836L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(99160113));
        assertTrue(variant.getKey().getTranscript().equals("XM_005269659.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("GGTGACGTTTGTGCAGTC"));
        assertTrue(variant.getGene().getId().equals(26768));
        assertTrue(variant.getRefSeqGene().equals("RRP12"));
        assertTrue(variant.getHgncGene().equals("RRP12"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(30));
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
    public void testLocatedVariant435249599() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(435249599L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(435249599L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(98416578));
        assertTrue(variant.getKey().getTranscript().equals("NM_152309.2"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("CAC"));
        assertTrue(variant.getGene().getId().equals(21825));
        assertTrue(variant.getRefSeqGene().equals("PIK3AP1"));
        assertTrue(variant.getHgncGene().equals("PIK3AP1"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(15));
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
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(435249599L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(98416578));
        assertTrue(variant.getKey().getTranscript().equals("XM_005269498.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("CAC"));
        assertTrue(variant.getGene().getId().equals(21825));
        assertTrue(variant.getRefSeqGene().equals("PIK3AP1"));
        assertTrue(variant.getHgncGene().equals("PIK3AP1"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(15));
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
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(435249599L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000010.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(98416578));
        assertTrue(variant.getKey().getTranscript().equals("XM_005269499.1"));
        assertTrue(variant.getKey().getVariantEffect().equals("non-frameshifting indel"));
        assertTrue(variant.getVariantType().getName().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("CAC"));
        assertTrue(variant.getGene().getId().equals(21825));
        assertTrue(variant.getRefSeqGene().equals("PIK3AP1"));
        assertTrue(variant.getHgncGene().equals("PIK3AP1"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(15));
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
    public void testLocatedVariant423435870() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(423435870L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 4);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(423435870L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000023.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134772007));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("TG"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005262507.1"));
        assertTrue(variant.getGene().getId().equals(100832));
        assertTrue(variant.getRefSeqGene().equals("LOC644717"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(1));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(2936));
        assertTrue(variant.getCodingSequencePosition().equals(2936));
        assertTrue(variant.getAminoAcidStart().equals(979));
        assertTrue(variant.getAminoAcidEnd().equals(993));
        assertTrue(variant.getOriginalAminoAcid().equals("QCQLD..."));
        assertTrue(variant.getFinalAminoAcid().equals("PMPTR..."));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-41));
        assertTrue(variant.getKey().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000023.10:g.134772007T>TG"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005262507.1:c.2936A>CA"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005262507.1:g.2936A>CA"));
        assertTrue(variant.getHgvsProtein().equals("XP_005262564.1:p.Gln979fs"));

        variant = variants.get(1);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(423435870L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000023.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134772007));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("TG"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005276106.1"));
        assertTrue(variant.getGene().getId().equals(100832));
        assertTrue(variant.getRefSeqGene().equals("LOC644717"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(1));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(2654));
        assertTrue(variant.getCodingSequencePosition().equals(2654));
        assertTrue(variant.getAminoAcidStart().equals(885));
        assertTrue(variant.getAminoAcidEnd().equals(899));
        assertTrue(variant.getOriginalAminoAcid().equals("QCQLD..."));
        assertTrue(variant.getFinalAminoAcid().equals("PMPTR..."));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-41));
        assertTrue(variant.getKey().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000023.10:g.134772007T>TG"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005276106.1:c.2654A>CA"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005276106.1:g.2654A>CA"));
        assertTrue(variant.getHgvsProtein().equals("XP_005276163.1:p.Gln885fs"));

        variant = variants.get(2);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(423435870L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000023.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134772007));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("TG"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005276719.1"));
        assertTrue(variant.getGene().getId().equals(100832));
        assertTrue(variant.getRefSeqGene().equals("LOC644717"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(1));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(2936));
        assertTrue(variant.getCodingSequencePosition().equals(2936));
        assertTrue(variant.getAminoAcidStart().equals(979));
        assertTrue(variant.getAminoAcidEnd().equals(993));
        assertTrue(variant.getOriginalAminoAcid().equals("QCQLD..."));
        assertTrue(variant.getFinalAminoAcid().equals("PMPTR..."));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-41));
        assertTrue(variant.getKey().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000023.10:g.134772007T>TG"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005276719.1:c.2936A>CA"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005276719.1:g.2936A>CA"));
        assertTrue(variant.getHgvsProtein().equals("XP_005276776.1:p.Gln979fs"));

        variant = variants.get(3);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(423435870L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000023.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134772007));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("TG"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005278157.1"));
        assertTrue(variant.getGene().getId().equals(100832));
        assertTrue(variant.getRefSeqGene().equals("LOC644717"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(1));
        assertTrue(variant.getFeatureId().equals(0));
        assertTrue(variant.getTranscriptPosition().equals(2936));
        assertTrue(variant.getCodingSequencePosition().equals(2936));
        assertTrue(variant.getAminoAcidStart().equals(979));
        assertTrue(variant.getAminoAcidEnd().equals(993));
        assertTrue(variant.getOriginalAminoAcid().equals("QCQLD..."));
        assertTrue(variant.getFinalAminoAcid().equals("PMPTR..."));
        assertTrue(variant.getFrameshift().equals(Boolean.TRUE));
        assertTrue(variant.getInframe().equals(Boolean.FALSE));
        assertTrue(variant.getIntronExonDistance().equals(-41));
        assertTrue(variant.getKey().getVariantEffect().equals("frameshifting indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000023.10:g.134772007T>TG"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005278157.1:c.2936A>CA"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005278157.1:g.2936A>CA"));
        assertTrue(variant.getHgvsProtein().equals("XP_005278214.1:p.Gln979fs"));

    }

    @Test
    public void testLocatedVariant29831130() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(29831130L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(29831130L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(1284841));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005244819.1"));
        assertTrue(variant.getGene().getId().equals(100928));
        assertTrue(variant.getRefSeqGene().equals("LOC101929103"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(1));
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
        assertTrue(variant.getKey().getVariantEffect().equals("synonymous"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.1284841A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005244819.1:c.1845T>C"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005244819.1:g.3613T>C"));
    }

    @Test
    public void testLocatedVariant491902641() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(491902641L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 13);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001048171.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001048171.1:c.1372G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001048171.1:g.1588G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001041636.1:p.Gly458Ser"));

        variant = variants.get(1);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001048172.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001048172.1:c.1333G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001048172.1:g.1479G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001041637.1:p.Gly445Ser"));

        variant = variants.get(2);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001048173.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001048173.1:c.1330G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001048173.1:g.1476G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001041638.1:p.Gly444Ser"));

        variant = variants.get(3);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001048174.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001048174.1:c.1330G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001048174.1:g.1395G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001041639.1:p.Gly444Ser"));

        variant = variants.get(4);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001128425.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001128425.1:c.1414G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001128425.1:g.1630G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_001121897.1:p.Gly472Ser"));

        variant = variants.get(5);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_012222.2"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_012222.2:c.1405G>A"));
        assertTrue(variant.getHgvsTranscript().equals("NM_012222.2:g.1621G>A"));
        assertTrue(variant.getHgvsProtein().equals("NP_036354.1:p.Gly469Ser"));

        variant = variants.get(6);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005270880.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270880.1:c.1375G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270880.1:g.1557G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270937.1:p.Gly459Ser"));

        variant = variants.get(7);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005270881.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270881.1:c.1363G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270881.1:g.1408G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270938.1:p.Gly455Ser"));

        variant = variants.get(8);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005270882.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270882.1:c.1363G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270882.1:g.1480G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270939.1:p.Gly455Ser"));

        variant = variants.get(9);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005270883.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270883.1:c.1246G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270883.1:g.1304G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270940.1:p.Gly416Ser"));

        variant = variants.get(10);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005270884.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270884.1:c.1054G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270884.1:g.1470G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270941.1:p.Gly352Ser"));

        variant = variants.get(11);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005270885.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270885.1:c.1054G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270885.1:g.1372G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270942.1:p.Gly352Ser"));

        variant = variants.get(12);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(491902641L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45796916));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005270886.1"));
        assertTrue(variant.getGene().getId().equals(18198));
        assertTrue(variant.getRefSeqGene().equals("MUTYH"));
        assertTrue(variant.getHgncGene().equals("MUTYH"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getNonCanonicalExon().equals(3));
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.45796916C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005270886.1:c.952G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005270886.1:g.1249G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005270943.1:p.Gly318Ser"));

    }

    @Test
    public void testLocatedVariant391895876() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(391895876L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 7);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("NM_000038.5"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getName().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(8940));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000038.5:c.8532+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("NM_000038.5:g.8940A>G"));

        variant = variants.get(1);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001127510.2"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getName().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(9048));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001127510.2:c.8532+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001127510.2:g.9048A>G"));

        variant = variants.get(2);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001127511.2"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getName().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(9014));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001127511.2:c.8478+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001127511.2:g.9014A>G"));

        variant = variants.get(3);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005271974.1"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getName().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(9002));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005271974.1:c.8532+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005271974.1:g.9002A>G"));

        variant = variants.get(4);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005271975.1"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getName().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(9235));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005271975.1:c.8532+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005271975.1:g.9235A>G"));

        variant = variants.get(5);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005271976.1"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getName().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(8740));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005271976.1:c.8355+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005271976.1:g.8740A>G"));

        variant = variants.get(6);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(391895876L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000005.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(112180146));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005271977.1"));
        assertTrue(variant.getGene().getId().equals(1125));
        assertTrue(variant.getRefSeqGene().equals("APC"));
        assertTrue(variant.getHgncGene().equals("APC"));
        assertTrue(variant.getLocationType().getName().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(8637));
        assertTrue(variant.getIntronExonDistance().equals(323));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000005.9:g.112180146A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005271977.1:c.8229+323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005271977.1:g.8637A>G"));

    }

    @Test
    public void testLocatedVariant404841675() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(404841675L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 5);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_000179.2"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getNonCanonicalExon().equals(5));
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
        assertTrue(variant.getKey().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_000179.2:c.3436C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_000179.2:g.3588C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_000170.1:p.Gln1146*"));

        variant = variants.get(1);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001281492.1"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getNonCanonicalExon().equals(3));
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
        assertTrue(variant.getKey().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001281492.1:c.3046C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001281492.1:g.3198C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001268421.1:p.Gln1016*"));

        variant = variants.get(2);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001281493.1"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getNonCanonicalExon().equals(2));
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
        assertTrue(variant.getKey().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001281493.1:c.2530C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001281493.1:g.3418C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001268422.1:p.Gln844*"));

        variant = variants.get(3);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001281494.1"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getNonCanonicalExon().equals(2));
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
        assertTrue(variant.getKey().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001281494.1:c.2530C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001281494.1:g.3323C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001268423.1:p.Gln844*"));

        variant = variants.get(4);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(404841675L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000002.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48030822));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005264271.1"));
        assertTrue(variant.getGene().getId().equals(17880));
        assertTrue(variant.getRefSeqGene().equals("MSH6"));
        assertTrue(variant.getHgncGene().equals("MSH6"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getNonCanonicalExon().equals(4));
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
        assertTrue(variant.getKey().getVariantEffect().equals("nonsense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000002.11:g.48030822C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005264271.1:c.3139C>T"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005264271.1:g.3234C>T"));
        assertTrue(variant.getHgvsProtein().equals("XP_005264328.1:p.Gln1047*"));

    }

    @Test
    public void testLocatedVariant386029980() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(386029980L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(386029980L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(11707985));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getKey().getTranscript().equals("XR_244788.1"));
        assertTrue(variant.getGene().getId().equals(9154));
        assertTrue(variant.getRefSeqGene().equals("FBXO2"));
        assertTrue(variant.getHgncGene().equals("FBXO2"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(-14));
        assertTrue(variant.getKey().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.11707985G>A"));

    }

    @Test
    public void testLocatedVariant484291964() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(484291964L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(484291964L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(65898));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("T"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005259648.1"));
        assertTrue(variant.getGene().getId().equals(20051));
        assertTrue(variant.getRefSeqGene().equals("OR4F17"));
        assertTrue(variant.getHgncGene().equals("OR4F17"));
        assertTrue(variant.getLocationType().getName().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(16));
        assertTrue(variant.getIntronExonDistance().equals(-74));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.65898T>C"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005259648.1:g.16T>C"));

    }

    @Test
    public void testLocatedVariant476428721() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(476428721L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(476428721L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(8378174));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("GCAGCAGCCGGGACCGCGGCCGGGCAG"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005263467.1"));
        assertTrue(variant.getGene().getId().equals(28139));
        assertTrue(variant.getRefSeqGene().equals("SLC45A1"));
        assertTrue(variant.getHgncGene().equals("SLC45A1"));
        assertTrue(variant.getLocationType().getName().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(4));
        assertTrue(variant.getIntronExonDistance().equals(-47));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.8378174_8378200delGCAGCAGCCGGGACCGCGGCCGGGCAG"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005263467.1:g.4_30delGCAGCAGCCGGGACCGCGGCCGGGCAG"));
    }

    @Test
    public void testLocatedVariant385947625() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(385947625L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 5);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(2));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(2));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("NR_028326.1"));
        assertTrue(variant.getGene().getId().equals(72605));
        assertTrue(variant.getRefSeqGene().equals("LINC01001"));
        assertTrue(variant.getHgncGene().equals("LINC01001"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1906792));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(4613));
        assertTrue(variant.getIntronExonDistance().equals(1448));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("NR_028326.1:g.4613G>C"));

        variant = variants.get(1);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("NR_039983.2"));
        assertTrue(variant.getGene().getId().equals(102469));
        assertTrue(variant.getRefSeqGene().equals("LOC729737"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1902186));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(5473));
        assertTrue(variant.getIntronExonDistance().equals(4923));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("NR_039983.2:g.5473G>C"));

        variant = variants.get(2);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(1));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_246728.1"));
        assertTrue(variant.getGene().getId().equals(100855));
        assertTrue(variant.getRefSeqGene().equals("LOC101930220"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1734910));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1721));
        assertTrue(variant.getIntronExonDistance().equals(1593));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_246728.1:g.1721G>C"));

        variant = variants.get(3);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(1));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(8));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_253608.1"));
        assertTrue(variant.getGene().getId().equals(101604));
        assertTrue(variant.getRefSeqGene().equals("LOC100133182"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1794079));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1580));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_253608.1:g.1580G>C"));

        variant = variants.get(4);
        logger.info(variant.toString());
        // assertTrue(variant.getKey().getMapNumber().equals(3));
        assertTrue(variant.getNumberOfTranscriptMaps().equals(7));
        assertTrue(variant.getLocatedVariant().getId().equals(385947625L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(134774));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_254450.1"));
        assertTrue(variant.getGene().getId().equals(101709));
        assertTrue(variant.getRefSeqGene().equals("LOC101929817"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1788322));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1220));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.134774C>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_254450.1:g.1220G>C"));

    }

    @Test
    public void testLocatedVariant491873904() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(491873904L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(491873904L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(721323));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_108279.2"));
        assertTrue(variant.getGene().getId().equals(100921));
        assertTrue(variant.getRefSeqGene().equals("LOC100287934"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1735318));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(224));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.721323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_108279.2:g.224A>G"));

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(491873904L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(721323));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_171142.2"));
        assertTrue(variant.getGene().getId().equals(100921));
        assertTrue(variant.getRefSeqGene().equals("LOC100287934"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1790602));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(224));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.721323A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XR_171142.2:g.224A>G"));

        variant = variants.get(2);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(491873904L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(721323));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_250120.1"));
        assertTrue(variant.getGene().getId().equals(100219));
        assertTrue(variant.getRefSeqGene().equals("LOC101930503"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(41728));
        assertTrue(variant.getKey().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.721323A>G"));

    }

    @Test
    public void testLocatedVariant32253987() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(32253987L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(32253987L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(846842));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getKey().getTranscript().equals("XR_108282.2"));
        assertTrue(variant.getGene().getId().equals(98944));
        assertTrue(variant.getRefSeqGene().equals("LOC284600"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1735321));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(28));
        assertTrue(variant.getIntronExonDistance().equals(-12));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.846842G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XR_108282.2:g.28G>A"));

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(32253987L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(846842));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getKey().getTranscript().equals("XR_112078.2"));
        assertTrue(variant.getGene().getId().equals(98944));
        assertTrue(variant.getRefSeqGene().equals("LOC284600"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1732659));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(28));
        assertTrue(variant.getIntronExonDistance().equals(-12));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.846842G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XR_112078.2:g.28G>A"));

        variant = variants.get(2);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(32253987L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(846842));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getKey().getTranscript().equals("XR_171144.2"));
        assertTrue(variant.getGene().getId().equals(98944));
        assertTrue(variant.getRefSeqGene().equals("LOC284600"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1790606));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(28));
        assertTrue(variant.getIntronExonDistance().equals(-12));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.846842G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XR_171144.2:g.28G>A"));

    }

    @Test
    public void testLocatedVariant10401977() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(10401977L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(10401977L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(856704));
        assertTrue(variant.getVariantType().getName().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_241051.1"));
        assertTrue(variant.getGene().getId().equals(98945));
        assertTrue(variant.getRefSeqGene().equals("LOC101928801"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1735322));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1666));
        assertTrue(variant.getIntronExonDistance().equals(1186));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.856704_856705insG"));
        assertTrue(variant.getHgvsTranscript().equals("XR_241051.1:g.1665_1666insC"));

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(10401977L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(856704));
        assertTrue(variant.getVariantType().getName().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_248814.1"));
        assertTrue(variant.getGene().getId().equals(98945));
        assertTrue(variant.getRefSeqGene().equals("LOC101928801"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1732660));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1666));
        assertTrue(variant.getIntronExonDistance().equals(1186));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.856704_856705insG"));
        assertTrue(variant.getHgvsTranscript().equals("XR_248814.1:g.1665_1666insC"));

        variant = variants.get(2);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(10401977L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(856704));
        assertTrue(variant.getVariantType().getName().equals("ins"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XR_250680.1"));
        assertTrue(variant.getGene().getId().equals(98945));
        assertTrue(variant.getRefSeqGene().equals("LOC101928801"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR"));
        assertTrue(variant.getFeatureId().equals(1790607));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1666));
        assertTrue(variant.getIntronExonDistance().equals(1186));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.856704_856705insG"));
        assertTrue(variant.getHgvsTranscript().equals("XR_250680.1:g.1665_1666insC"));

    }

    @Test
    public void testLocatedVariant487626896() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(487626896L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(487626896L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(931116));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005275882.1"));
        assertTrue(variant.getGene().getId().equals(98949));
        assertTrue(variant.getRefSeqGene().equals("LOC101930320"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("UTR-3"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition().equals(1999));
        assertTrue(variant.getIntronExonDistance().equals(388));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-3"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.931116G>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005275882.1:c.1611+388C>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005275882.1:g.1999C>A"));

    }

    @Test
    public void testLocatedVariant385962999() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(385962999L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(385962999L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(901873));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005244809.1"));
        assertTrue(variant.getGene().getId().equals(22155));
        assertTrue(variant.getRefSeqGene().equals("PLEKHN1"));
        assertTrue(variant.getHgncGene().equals("PLEKHN1"));
        assertTrue(variant.getLocationType().getName().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(2));
        assertTrue(variant.getIntronExonDistance().equals(-39));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.901873G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005244809.1:c.1-39G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005244809.1:g.2G>A"));

    }

    @Test
    public void testLocatedVariant476430841() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(476430841L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 4);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(476430841L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9005897));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("AAA"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001215.3"));
        assertTrue(variant.getGene().getId().equals(3852));
        assertTrue(variant.getRefSeqGene().equals("CA6"));
        assertTrue(variant.getHgncGene().equals("CA6"));
        assertTrue(variant.getLocationType().getName().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(5));
        assertTrue(variant.getIntronExonDistance().equals(-47));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9005897_9005899delAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001215.3:c.1-47_3delAAA"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001215.3:g.5_7delAAA"));

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(476430841L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9005897));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("AAA"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001270500.1"));
        assertTrue(variant.getGene().getId().equals(3852));
        assertTrue(variant.getRefSeqGene().equals("CA6"));
        assertTrue(variant.getHgncGene().equals("CA6"));
        assertTrue(variant.getLocationType().getName().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(5));
        assertTrue(variant.getIntronExonDistance().equals(-47));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9005897_9005899delAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001270500.1:c.1-47_3delAAA"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001270500.1:g.5_7delAAA"));

        variant = variants.get(2);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(476430841L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9005897));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("AAA"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001270501.1"));
        assertTrue(variant.getGene().getId().equals(3852));
        assertTrue(variant.getRefSeqGene().equals("CA6"));
        assertTrue(variant.getHgncGene().equals("CA6"));
        assertTrue(variant.getLocationType().getName().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(5));
        assertTrue(variant.getIntronExonDistance().equals(-47));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9005897_9005899delAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001270501.1:c.1-47_3delAAA"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001270501.1:g.5_7delAAA"));

        variant = variants.get(3);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(476430841L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9005897));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("AAA"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001270502.1"));
        assertTrue(variant.getGene().getId().equals(3852));
        assertTrue(variant.getRefSeqGene().equals("CA6"));
        assertTrue(variant.getHgncGene().equals("CA6"));
        assertTrue(variant.getLocationType().getName().equals("UTR-5"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getTranscriptPosition().equals(5));
        assertTrue(variant.getIntronExonDistance().equals(-102));
        assertTrue(variant.getKey().getVariantEffect().equals("UTR-5"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9005897_9005899delAAA"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001270502.1:c.1-102_3delAAA"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001270502.1:g.5_7delAAA"));

    }

    @Test
    public void testLocatedVariant491937061() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(491937061L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(491937061L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(11876525));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001256959.1"));
        assertTrue(variant.getGene().getId().equals(5235));
        assertTrue(variant.getRefSeqGene().equals("CLCN6"));
        assertTrue(variant.getHgncGene().equals("CLCN6"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-147));
        assertTrue(variant.getKey().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.11876525delC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001256959.1:c.148-147_148delC"));
        assertTrue(variant.getHgvsTranscript() == null);

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(491937061L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(11876525));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001286.3"));
        assertTrue(variant.getGene().getId().equals(5235));
        assertTrue(variant.getRefSeqGene().equals("CLCN6"));
        assertTrue(variant.getHgncGene().equals("CLCN6"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-147));
        assertTrue(variant.getKey().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.11876525delC"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001286.3:c.214-147_214delC"));
        assertTrue(variant.getHgvsTranscript() == null);

        variant = variants.get(2);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(491937061L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(11876525));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getKey().getTranscript().equals("NR_046428.1"));
        assertTrue(variant.getGene().getId().equals(5235));
        assertTrue(variant.getRefSeqGene().equals("CLCN6"));
        assertTrue(variant.getHgncGene().equals("CLCN6"));
        assertTrue(variant.getLocationType().getName().equals("intron"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(-147));
        assertTrue(variant.getKey().getVariantEffect().equals("intron"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.11876525delC"));
        assertTrue(variant.getHgvsCodingSequence() == null);
        assertTrue(variant.getHgvsTranscript() == null);

    }

    @Test
    public void testLocatedVariant31046600() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(31046600L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 3);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(31046600L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(22649389));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_003960117.2"));
        assertTrue(variant.getGene().getId().equals(98888));
        assertTrue(variant.getRefSeqGene().equals("LOC101060363"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.22649389C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_003960117.2:c.436G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_003960117.2:g.436G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_003960166.1:p.Gly146Ser"));

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(31046600L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(22649389));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005246048.1"));
        assertTrue(variant.getGene().getId().equals(98888));
        assertTrue(variant.getRefSeqGene().equals("LOC101060363"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.22649389C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005246048.1:c.436G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005246048.1:g.436G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005246105.1:p.Gly146Ser"));

        variant = variants.get(2);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(31046600L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(22649389));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("C"));
        assertTrue(variant.getAlternateAllele().equals("T"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005275876.1"));
        assertTrue(variant.getGene().getId().equals(98888));
        assertTrue(variant.getRefSeqGene().equals("LOC101060363"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.22649389C>T"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005275876.1:c.436G>A"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005275876.1:g.436G>A"));
        assertTrue(variant.getHgvsProtein().equals("XP_005275933.1:p.Gly146Ser"));

    }

    @Test
    public void testLocatedVariant476568240() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(476568240L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(476568240L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(33772384));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("A"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001080438.1"));
        assertTrue(variant.getGene().getId().equals(13));
        assertTrue(variant.getRefSeqGene().equals("A3GALT2"));
        assertTrue(variant.getHgncGene().equals("A3GALT2"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.33772384G>A"));
        assertTrue(variant.getHgvsCodingSequence().equals("NM_001080438.1:c.1006C>T"));
        assertTrue(variant.getHgvsTranscript().equals("NM_001080438.1:g.1006C>T"));
        assertTrue(variant.getHgvsProtein().equals("NP_001073907.1:p.Arg336Trp"));

    }

    @Test
    public void testLocatedVariant378427953() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(378427953L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(378427953L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(29473876));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("TTTTTTT"));
        assertTrue(variant.getKey().getTranscript().equals("NM_005626.4"));
        assertTrue(variant.getGene().getId().equals(29273));
        assertTrue(variant.getRefSeqGene().equals("SRSF4"));
        assertTrue(variant.getHgncGene().equals("SRSF4"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getTranscriptPosition() == null);
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.29473876_29473882delTTTTTTT"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant386017450() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(386017450L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(386017450L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(9335209));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("A"));
        assertTrue(variant.getAlternateAllele().equals("G"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005263498.1"));
        assertTrue(variant.getGene().getId().equals(29177));
        assertTrue(variant.getRefSeqGene().equals("SPSB1"));
        assertTrue(variant.getHgncGene().equals("SPSB1"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.9335209A>G"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_005263498.1:c.8A>G"));
        assertTrue(variant.getHgvsTranscript().equals("XM_005263498.1:g.8A>G"));
        assertTrue(variant.getHgvsProtein().equals("XP_005263555.1:p.Lys3Arg"));

    }

    @Test
    public void testLocatedVariant29835813() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(29835813L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(29835813L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(13140452));
        assertTrue(variant.getVariantType().getName().equals("snp"));
        assertTrue(variant.getReferenceAllele().equals("G"));
        assertTrue(variant.getAlternateAllele().equals("C"));
        assertTrue(variant.getKey().getTranscript().equals("XM_003118846.3"));
        assertTrue(variant.getGene().getId().equals(100932));
        assertTrue(variant.getRefSeqGene().equals("PRAMEF25"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("exon"));
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
        assertTrue(variant.getKey().getVariantEffect().equals("missense"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.13140452G>C"));
        assertTrue(variant.getHgvsCodingSequence().equals("XM_003118846.3:c.3G>C"));
        assertTrue(variant.getHgvsTranscript().equals("XM_003118846.3:g.6G>C"));
        assertTrue(variant.getHgvsProtein().equals("XP_003118894.3:p.Met1Ile"));

    }

    @Test
    public void testLocatedVariant435110898() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(435110898L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(435110898L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(113739326));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("ATT"));
        assertTrue(variant.getKey().getTranscript().equals("NR_038846.1"));
        assertTrue(variant.getGene().getId().equals(102461));
        assertTrue(variant.getRefSeqGene().equals("LOC643441"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.113739326_113739328delATT"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant384944453() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(384944453L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(384944453L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(161195724));
        assertTrue(variant.getVariantType().getName().equals("del"));
        assertTrue(variant.getReferenceAllele().equals("TTTTTTTT"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005245536.1"));
        assertTrue(variant.getGene().getId().equals(30972));
        assertTrue(variant.getRefSeqGene().equals("TOMM40L"));
        assertTrue(variant.getHgncGene().equals("TOMM40L"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.161195724_161195731delTTTTTTTT"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant438862902() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(438862902L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(438862902L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000013.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(45373639));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals("GGA"));
        assertTrue(variant.getAlternateAllele().equals("TGG"));
        assertTrue(variant.getKey().getTranscript().equals("NR_038433.2"));
        assertTrue(variant.getGene().getId().equals(58116));
        assertTrue(variant.getRefSeqGene().equals("LINC00330"));
        assertTrue(variant.getHgncGene().equals("LINC00330"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000013.10:g.45373639_45373641delinsTGG"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant387000526() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(387000526L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(387000526L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(176176504));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GTCGGGGCTCCCTGGCGAAGGGAGTCTGTCTGTCCCAAGCCGCTTTCGTTCCAGAAAACGTGCCAGAGGGCAGGCCGTCTTAATACAGTGAAAGAATTGAGGAGACTGGAATGAATGGTATAGGTGGTGGGGTGGTTTGAAAGACGGGAACGTAGCGGGCGGAAACTCTCGAAAAGCGACTGCGCAGGCGCGCGCCGGGCGCGGACGCTCACGGGTCACCCAGGGTATCCGCCCCAAGCGGAGAATATCGGCGCAAGCGCGGAGTAGCCGTGTCACGCATGCGTGCGAACGCACCACATTCCGGAACGGGAAGAAGGAGCTGTCTCTCTCTTCTGAGGGGTAGGCAGGTGCTTCCGAATCGCGGAATGTAGGTGCTCATAAGCTGGAGACCCACGGAAAGAACCTAGAAGCTCCGAGCGGTTGCAGACAGCACTGCATTGAGCCAACGACTCCTGCTCTACTGATGGTGATTCCACAGCTTTGTGAGGAATAAGTTAAGATGAAGTCAGTGAAGTTTTCTGCGAATTGCAAACATTGTAGCAAGTGTTACCAATTTTATTAGTTCGACCACGACTTCACTCCTATATTTTGAAAAGCTGTTTGCCAAATTGAACATAACAATAATGGGAAAACAGATTTTGCTCAATCAGACCCAGCTCAGTCGGAACCTAGGTTTATTGTGGATCTGATATAATTCTAGCACAAAACAAGGAAACCTAGCATTTAATACCACCAGAAAATATACAATTTCTGGTAGGCTTTTTTAGAATATTGAAAATTGATTATAGGCGGGCGCGGTGGCTCATGCCTGTCATCGCAGCACCTTGGGAGGCCTAGGGGGTCGGATCACCTGAGCCTGGAGGTGAGGATCAGGCTGAACCTGAGGTTAGAGACCAGCCTGGGCAAGATAGCGAAACCCCGTCTCTACAAAAAGTACAAAAAATTAGCTGGGCATGGTGGCAGCAGGCCTGTAGTCCCAGCTACTCGGG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getKey().getTranscript().equals("XR_248664.1"));
        assertTrue(variant.getGene().getId().equals(98800));
        assertTrue(variant.getRefSeqGene().equals("LOC101930532"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.176176504_176177492delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(387000526L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000001.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(176176504));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GTCGGGGCTCCCTGGCGAAGGGAGTCTGTCTGTCCCAAGCCGCTTTCGTTCCAGAAAACGTGCCAGAGGGCAGGCCGTCTTAATACAGTGAAAGAATTGAGGAGACTGGAATGAATGGTATAGGTGGTGGGGTGGTTTGAAAGACGGGAACGTAGCGGGCGGAAACTCTCGAAAAGCGACTGCGCAGGCGCGCGCCGGGCGCGGACGCTCACGGGTCACCCAGGGTATCCGCCCCAAGCGGAGAATATCGGCGCAAGCGCGGAGTAGCCGTGTCACGCATGCGTGCGAACGCACCACATTCCGGAACGGGAAGAAGGAGCTGTCTCTCTCTTCTGAGGGGTAGGCAGGTGCTTCCGAATCGCGGAATGTAGGTGCTCATAAGCTGGAGACCCACGGAAAGAACCTAGAAGCTCCGAGCGGTTGCAGACAGCACTGCATTGAGCCAACGACTCCTGCTCTACTGATGGTGATTCCACAGCTTTGTGAGGAATAAGTTAAGATGAAGTCAGTGAAGTTTTCTGCGAATTGCAAACATTGTAGCAAGTGTTACCAATTTTATTAGTTCGACCACGACTTCACTCCTATATTTTGAAAAGCTGTTTGCCAAATTGAACATAACAATAATGGGAAAACAGATTTTGCTCAATCAGACCCAGCTCAGTCGGAACCTAGGTTTATTGTGGATCTGATATAATTCTAGCACAAAACAAGGAAACCTAGCATTTAATACCACCAGAAAATATACAATTTCTGGTAGGCTTTTTTAGAATATTGAAAATTGATTATAGGCGGGCGCGGTGGCTCATGCCTGTCATCGCAGCACCTTGGGAGGCCTAGGGGGTCGGATCACCTGAGCCTGGAGGTGAGGATCAGGCTGAACCTGAGGTTAGAGACCAGCCTGGGCAAGATAGCGAAACCCCGTCTCTACAAAAAGTACAAAAAATTAGCTGGGCATGGTGGCAGCAGGCCTGTAGTCCCAGCTACTCGGG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getKey().getTranscript().equals("XR_250905.1"));
        assertTrue(variant.getGene().getId().equals(101813));
        assertTrue(variant.getRefSeqGene().equals("LOC101930156"));
        assertTrue(variant.getHgncGene().equals("None"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000001.10:g.176176504_176177492delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant392541331() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(392541331L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(392541331L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000006.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(30643316));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GAGATGGAGTCTCACTCTGCTGCCCAGGCTGGAGTGTAATGGCGCAATCTCAGCTCACTGCAAGCTCCACCTCCCGGGTTCATGCCATTCTCCTGCCTCAGCCTCCCGAGTAGCTGGGACTACAGGTGCCCACCACTATGCCCCGCTAATTTTTTGTATTTTTAGTAGAGACGGGGTTTCACCGTGTTAGCCAGGATGGTCTCAATCTCCTGACCTTATGATCCGCCTGCCTCGGCCTCCCAAAGTGCTGGGATCACAGGCGTGAGCCACCGTGCCCGGCCTTTAGGCCTTTAACGATATAAAATCCATTGTCTATCAGAGGGGAACCTTTTCCAGGAAACTGACTCTTGTACATACTTACTTCATTTTGCAGCAATTTCAGATTTAGTATTCGTAGCCCCAGCTCTTTAAGTAAGTATCCCTGGATTAGCCACATGGGTTGTGTCATACACTACCTAGCTGCCTTCATGGCAGCAGGCTTCTGAATACTAGAACCCTTCAACTCAAAGTGTCCTCTGTAATATTTTAACCCTTTTCTTCTATTCATTCATTTGTTGTCATTCATTCTAGAAATAATTCCGTGTCTACTAGTTGACAGGTACAGGATATTGCAGTGAATCCAGCTGATGTAGTCAGCCCTCATGGCACTTCCAGTCTAGTGGACACTTCAACTGCCCTTTCTCATGTCACCTGCTTGTCCTGCGTGAAACCCACGTGCAGCTTCCCAGACCCCTTTTGACATGTCAGTGCCGAGTTCCTGGTTCATCCCCCATCATTTTCCTCTCCCCCAGCCACCAGAGCCTCCCCTCACATACCCTTTTTTTTTCCCAAAGAAGGAGAAGCAGACGAG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getKey().getTranscript().equals("NM_001134870.1"));
        assertTrue(variant.getGene().getId().equals(13888));
        assertTrue(variant.getRefSeqGene().equals("PPP1R18"));
        assertTrue(variant.getHgncGene().equals("PPP1R18"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000006.11:g.30643316_30644165delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(392541331L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000006.11"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(30643316));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GAGATGGAGTCTCACTCTGCTGCCCAGGCTGGAGTGTAATGGCGCAATCTCAGCTCACTGCAAGCTCCACCTCCCGGGTTCATGCCATTCTCCTGCCTCAGCCTCCCGAGTAGCTGGGACTACAGGTGCCCACCACTATGCCCCGCTAATTTTTTGTATTTTTAGTAGAGACGGGGTTTCACCGTGTTAGCCAGGATGGTCTCAATCTCCTGACCTTATGATCCGCCTGCCTCGGCCTCCCAAAGTGCTGGGATCACAGGCGTGAGCCACCGTGCCCGGCCTTTAGGCCTTTAACGATATAAAATCCATTGTCTATCAGAGGGGAACCTTTTCCAGGAAACTGACTCTTGTACATACTTACTTCATTTTGCAGCAATTTCAGATTTAGTATTCGTAGCCCCAGCTCTTTAAGTAAGTATCCCTGGATTAGCCACATGGGTTGTGTCATACACTACCTAGCTGCCTTCATGGCAGCAGGCTTCTGAATACTAGAACCCTTCAACTCAAAGTGTCCTCTGTAATATTTTAACCCTTTTCTTCTATTCATTCATTTGTTGTCATTCATTCTAGAAATAATTCCGTGTCTACTAGTTGACAGGTACAGGATATTGCAGTGAATCCAGCTGATGTAGTCAGCCCTCATGGCACTTCCAGTCTAGTGGACACTTCAACTGCCCTTTCTCATGTCACCTGCTTGTCCTGCGTGAAACCCACGTGCAGCTTCCCAGACCCCTTTTGACATGTCAGTGCCGAGTTCCTGGTTCATCCCCCATCATTTTCCTCTCCCCCAGCCACCAGAGCCTCCCCTCACATACCCTTTTTTTTTCCCAAAGAAGGAGAAGCAGACGAG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getKey().getTranscript().equals("NM_133471.3"));
        assertTrue(variant.getGene().getId().equals(13888));
        assertTrue(variant.getRefSeqGene().equals("PPP1R18"));
        assertTrue(variant.getHgncGene().equals("PPP1R18"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000006.11:g.30643316_30644165delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant402249392() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(402249392L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 2);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(402249392L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000019.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(18208489));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "AGGGGCGGGGCCAGGCCGTGTCAGCTGACCGGCCTGGCCAATGGCACCTGGCGGCGGGGCCTGGCAGGCCGGGGCGGGGCCGGGGGCGGGCCTGGCGGCGCGGACTCCCGGG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005259824.1"));
        assertTrue(variant.getGene().getId().equals(15410));
        assertTrue(variant.getRefSeqGene().equals("MAST3"));
        assertTrue(variant.getHgncGene().equals("MAST3"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000019.9:g.18208489_18208600delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

        variant = variants.get(1);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(402249392L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000019.9"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(18208489));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "AGGGGCGGGGCCAGGCCGTGTCAGCTGACCGGCCTGGCCAATGGCACCTGGCGGCGGGGCCTGGCAGGCCGGGGCGGGGCCGGGGGCGGGCCTGGCGGCGCGGACTCCCGGG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005259825.1"));
        assertTrue(variant.getGene().getId().equals(15410));
        assertTrue(variant.getRefSeqGene().equals("MAST3"));
        assertTrue(variant.getHgncGene().equals("MAST3"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("+"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000019.9:g.18208489_18208600delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant403213080() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(403213080L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(403213080L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000021.8"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(33973624));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GAGACAAGAGTTTCGCTCTGTTGCCCAGGATAGAGTGCAGTGGCAGTGATCTTGGCTCACTGTAAGCTCCACCTCCCGGGTTCACGCCATTCTCCTGCCTCAGCCTCCCGAGTAGCTGGGACTACAGGCGCCTGCCACCATGCCCGGCTAATTTTTTGTATTTTTAGTAGAGACGGGGTTTCACCGTGTTAGCCTGGATGGTCTTGATCTCCTGACCTCGTGATCTGCCTGCCTCGGCCTCCCAAAGTGCTGGGATTACAGGCGTGAGCCACCGCGCCCGGCCAAAAGTTTAGTATTTTATCAAAGAAAACCATGACATACTAAATAGTCCTGAAACAAAAACGATTTACACATTTTC"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getKey().getTranscript().equals("XM_005261007.1"));
        assertTrue(variant.getGene().getId().equals(3776));
        assertTrue(variant.getRefSeqGene().equals("C21orf59"));
        assertTrue(variant.getHgncGene().equals("C21orf59"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000021.8:g.33973624_33973981delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

    @Test
    public void testLocatedVariant403858749() throws Exception {

        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(403858749L);
        logger.info(locatedVariant.toString());

        List<Variants_61_2> variants = annotateLocatedVariant(locatedVariant);
        assertTrue(variants.size() == 1);

        variants.sort((a, b) -> a.getKey().getTranscript().compareTo(b.getKey().getTranscript()));

        Variants_61_2 variant = variants.get(0);
        logger.info(variant.toString());
        assertTrue(variant.getLocatedVariant().getId().equals(403858749L));
        assertTrue(variant.getGenomeRefSeq().getVerAccession().equals("NC_000023.10"));
        assertTrue(variant.getLocatedVariant().getPosition().equals(48969879));
        assertTrue(variant.getVariantType().getName().equals("sub"));
        assertTrue(variant.getReferenceAllele().equals(
                "GAGACAGAGTCTAACTCTGTCCCCTGGGCTGGAGTGCAGTGGCGCAATCTGGGCTCACAGCAACCTCTGCCTCCTGGGTTCAAGCGATTCTCTTGCCTCAGCCTCCTGAGTAGCTAGGATTATAGGCGTGCACCACCATACCTCGGTAATTTTTTGTATTTTTATACTTTTAGTAGAGACAGGGTTTCACCTTGTTGGCCAGGCTGATCTTGAACTCCTGGGCTCAAGCGATCCATCCACCTCAGCCTCCTAAAGTGCTAGGATTACAGGCATGCACCACTGTGCCCAAGCTTATAGTATCTATATTCCAAGAGTGAGCTCACAATCAAATGAGTCCTTTAACTTGTCCTGTACCCTGAGCAGAAAAGCAAGGCTTAAAAGTGAAATGGTTCCCACCTCAGTCAACTCAGTTCCCACCTTAACTCGCAGCTCATCATACCACTTATGATTTCATCTTTCTATTAAG"));
        assertTrue(variant.getAlternateAllele().equals("(LARGEDELETION)"));
        assertTrue(variant.getKey().getTranscript().equals("NM_015698.5"));
        assertTrue(variant.getGene().getId().equals(10717));
        assertTrue(variant.getRefSeqGene().equals("GPKOW"));
        assertTrue(variant.getHgncGene().equals("GPKOW"));
        assertTrue(variant.getLocationType().getName().equals("intron/exon boundary"));
        assertTrue(variant.getStrand().equals("-"));
        assertTrue(variant.getIntronExonDistance().equals(0));
        assertTrue(variant.getKey().getVariantEffect().equals("noncoding boundary-crossing indel"));
        assertTrue(variant.getHgvsGenomic().equals("NC_000023.10:g.48969879_48970344delins(LARGEDELETION)"));
        assertTrue(variant.getHgvsCodingSequence().equals("?"));
        assertTrue(variant.getHgvsTranscript().equals("?"));
        assertTrue(variant.getHgvsProtein().equals("?"));

    }

}
