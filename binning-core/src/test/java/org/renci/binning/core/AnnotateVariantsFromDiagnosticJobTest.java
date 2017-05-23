package org.renci.binning.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.renci.canvas.binning.core.grch37.VariantsFactory;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.canvas.dao.jpa.CANVASDAOManager;
import org.renci.canvas.dao.ref.model.GenomeRef;
import org.renci.canvas.dao.refseq.model.TranscriptMaps;
import org.renci.canvas.dao.refseq.model.TranscriptMapsExons;
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariantsFromDiagnosticJobTest {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariantsFromDiagnosticJobTest.class);

    private static final CANVASDAOManager daoMgr = CANVASDAOManager.getInstance();

    @Test
    public void testDiagnostic() throws CANVASDAOException {

        VariantsFactory variantsFactory = VariantsFactory.getInstance(daoMgr.getDAOBean());

        DiagnosticBinningJob binningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4093);
        logger.info(binningJob.toString());

        DiagnosticResultVersion diagnosticResultVersion = binningJob.getDiagnosticResultVersion();
        logger.info(diagnosticResultVersion.toString());

        String refseqVersion = diagnosticResultVersion.getRefseqVersion().toString();

        GenomeRef genomeRef = diagnosticResultVersion.getGenomeRef();
        logger.info(genomeRef.toString());

        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO().findIncrementable(genomeRef.getId());
        logger.info(String.format("locatedVariantList.size(): %d", locatedVariantList.size()));

        locatedVariantList.sort((a, b) -> {
            int ret = a.getGenomeRefSeq().getId().compareTo(b.getGenomeRefSeq().getId());
            if (ret == 0) {
                ret = a.getPosition().compareTo(b.getPosition());
            }
            return ret;
        });

        List<Variants_61_2> variants = new ArrayList<>();
        try {
            ExecutorService es = Executors.newFixedThreadPool(2);

            for (LocatedVariant locatedVariant : locatedVariantList) {

                es.submit(() -> {

                    try {
                        List<TranscriptMaps> transcriptMapsList = daoMgr.getDAOBean().getTranscriptMapsDAO()
                                .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRef.getId(), refseqVersion,
                                        locatedVariant.getGenomeRefSeq().getId(), locatedVariant.getPosition());

                        if (CollectionUtils.isNotEmpty(transcriptMapsList)) {

                            logger.info(String.format("transcriptMapsList.size(): %s", transcriptMapsList.size()));

                            for (TranscriptMaps tMap : transcriptMapsList) {

                                List<TranscriptMapsExons> transcriptMapsExonsList = daoMgr.getDAOBean().getTranscriptMapsExonsDAO()
                                        .findByTranscriptMapsId(tMap.getId());

                                TranscriptMapsExons transcriptMapsExons = null;
                                if (CollectionUtils.isNotEmpty(transcriptMapsExonsList)) {
                                    for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                                        Range<Integer> contigRange = Range.between(exon.getContigStart(), exon.getContigEnd());
                                        if (contigRange.contains(locatedVariant.getPosition())) {
                                            transcriptMapsExons = exon;
                                            break;
                                        }
                                    }
                                }

                                List<TranscriptMaps> mapsList = daoMgr.getDAOBean().getTranscriptMapsDAO()
                                        .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRef.getId(), refseqVersion,
                                                tMap.getTranscript().getId());

                                Variants_61_2 variant = null;
                                if (transcriptMapsExons == null) {
                                    variant = variantsFactory.createIntronicVariant(locatedVariant, mapsList, tMap,
                                            transcriptMapsExonsList);
                                } else {
                                    variant = variantsFactory.createExonicVariant(locatedVariant, mapsList, transcriptMapsExonsList,
                                            transcriptMapsExons);
                                }
                                logger.info(variant.toString());
                                // daoMgr.getDAOBean().getVariants_61_2_DAO().save(variant);

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
            es.shutdown();
            es.awaitTermination(1L, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File variantsFile = new File("/tmp", "variants.txt");
        try (FileWriter fw = new FileWriter(variantsFile); BufferedWriter bw = new BufferedWriter(fw)) {
            variants.forEach(a -> {
                try {

                    bw.write(String.format(
                            "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n",
                            a.getLocatedVariant().getId(), a.getGenomeRefSeq().getId(), a.getId().getPosition(), a.getVariantType().getId(),
                            a.getReferenceAllele(), StringUtils.isNotEmpty(a.getAlternateAllele()) ? a.getAlternateAllele() : "",
                            a.getId().getTranscript(), a.getId().getMapNumber(), a.getNumberOfTranscriptMaps(), a.getGene().getId(),
                            a.getRefSeqGene(), a.getHgncGene(), a.getLocationType().getId(),
                            a.getNonCanonicalExon() != null ? a.getNonCanonicalExon() : "",
                            a.getFeatureId() != null ? a.getFeatureId() : "", a.getStrand(),
                            a.getTranscriptPosition() != null ? a.getTranscriptPosition() : "",
                            a.getCodingSequencePosition() != null ? a.getCodingSequencePosition() : "",
                            a.getAminoAcidStart() != null ? a.getAminoAcidStart() : "",
                            a.getAminoAcidEnd() != null ? a.getAminoAcidEnd() : "",
                            a.getOriginalAminoAcid() != null ? a.getOriginalAminoAcid() : "",
                            a.getFinalAminoAcid() != null ? a.getFinalAminoAcid() : "", a.getFrameshift() != null ? a.getFrameshift() : "",
                            a.getInframe() != null ? a.getInframe() : "",
                            a.getIntronExonDistance() != null ? a.getIntronExonDistance() : "", a.getVariantEffect().getId(),
                            StringUtils.isNotEmpty(a.getHgvsGenomic()) ? a.getHgvsGenomic() : "",
                            StringUtils.isNotEmpty(a.getHgvsCodingSequence()) ? a.getHgvsCodingSequence() : "",
                            StringUtils.isNotEmpty(a.getHgvsTranscript()) ? a.getHgvsTranscript() : "",
                            StringUtils.isNotEmpty(a.getHgvsProtein()) ? a.getHgvsProtein() : ""));
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
