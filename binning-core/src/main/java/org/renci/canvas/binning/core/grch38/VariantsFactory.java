package org.renci.canvas.binning.core.grch38;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.sequence.template.SequenceView;
import org.biojava.nbio.core.sequence.transcription.DNAToRNATranslator;
import org.biojava.nbio.core.sequence.transcription.RNAToAminoAcidTranslator;
import org.biojava.nbio.core.sequence.transcription.TranscriptionEngine;
import org.renci.canvas.binning.core.AbstractVariantsFactory;
import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.annotation.model.AnnotationGeneExternalId;
import org.renci.canvas.dao.hgnc.model.HGNCGene;
import org.renci.canvas.dao.refseq.model.Feature;
import org.renci.canvas.dao.refseq.model.LocationType;
import org.renci.canvas.dao.refseq.model.RefSeqCodingSequence;
import org.renci.canvas.dao.refseq.model.RefSeqGene;
import org.renci.canvas.dao.refseq.model.RegionGroupRegion;
import org.renci.canvas.dao.refseq.model.TranscriptMaps;
import org.renci.canvas.dao.refseq.model.TranscriptMapsExons;
import org.renci.canvas.dao.refseq.model.VariantEffect;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.refseq.model.Variants_80_4PK;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariantsFactory extends AbstractVariantsFactory {

    private static final Logger logger = LoggerFactory.getLogger(VariantsFactory.class);

    private static VariantsFactory instance;

    private CANVASDAOBeanService daoBean;

    private DNAToRNATranslator dna2RnaTranslator;

    private RNAToAminoAcidTranslator rna2AminoAcidTranslator;

    private List<LocationType> allLocationTypes;

    private List<VariantEffect> allVariantEffects;

    public static VariantsFactory getInstance(CANVASDAOBeanService daoBean) {
        if (instance == null) {
            instance = new VariantsFactory(daoBean);
        }
        return instance;
    }

    private VariantsFactory(CANVASDAOBeanService daoBean) {
        super();
        this.daoBean = daoBean;
        try {
            this.allLocationTypes = daoBean.getLocationTypeDAO().findAll();
            this.allVariantEffects = daoBean.getVariantEffectDAO().findAll();
        } catch (CANVASDAOException e) {
            e.printStackTrace();
        }

        TranscriptionEngine engine = TranscriptionEngine.getDefault();
        this.dna2RnaTranslator = engine.getDnaRnaTranslator();
        this.rna2AminoAcidTranslator = engine.getRnaAminoAcidTranslator();
    }

    @Override
    public String getRefSeqVersion() {
        return "80";
    }

    public Set<Variants_80_4> annotateVariant(LocatedVariant locatedVariant, String refseqVersion, Integer genomeRefId,
            CANVASDAOBeanService daoBean) throws BinningException {
        Set<Variants_80_4> variants = new HashSet<>();

        try {

            if ("snp".equals(locatedVariant.getVariantType().getId())) {
                // either intergenic, intron, or exonic

                List<TranscriptMaps> transcriptMapsList = daoBean.getTranscriptMapsDAO()
                        .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRefId, refseqVersion,
                                locatedVariant.getGenomeRefSeq().getId(), locatedVariant.getPosition());

                if (CollectionUtils.isNotEmpty(transcriptMapsList)) {

                    List<TranscriptMaps> distinctTranscriptMapsList = transcriptMapsList.stream().map(a -> a.getTranscript().getId())
                            .distinct()
                            .map(a -> transcriptMapsList.stream().filter(b -> b.getTranscript().getId().equals(a)).findFirst().get())
                            .collect(Collectors.toList());

                    logger.debug("distinctTranscriptMapsList.size(): {}", distinctTranscriptMapsList.size());
                    distinctTranscriptMapsList.sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                    for (TranscriptMaps tMap : distinctTranscriptMapsList) {

                        logger.debug(tMap.toString());

                        List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRefId, refseqVersion, tMap.getTranscript().getId());

                        List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                .findByTranscriptMapsId(tMap.getId());

                        TranscriptMapsExons transcriptMapsExons = transcriptMapsExonsList.stream()
                                .filter(a -> a.getContigRange().contains(locatedVariant.getPosition())).findFirst().orElse(null);

                        if (transcriptMapsExons != null) {
                            logger.debug(transcriptMapsExons.toString());
                            variants.add(createExonicSNPMutation(locatedVariant, mapsList, transcriptMapsExonsList, transcriptMapsExons));
                        } else {
                            // if not found in an exon, but is within a transcript map contig range, must be intron
                            variants.add(createIntronicVariant(locatedVariant, mapsList, tMap, transcriptMapsExonsList));
                        }

                    }

                } else {
                    // snp not found in any transcript contig range, must be intergenic
                    Variants_80_4 variant = createIntergenicVariant(locatedVariant);
                    variants.add(variant);
                }

            } else {
                // handling indels

                final List<TranscriptMaps> transcriptMapsList = daoBean.getTranscriptMapsDAO()
                        .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRefId, refseqVersion,
                                locatedVariant.getGenomeRefSeq().getId(), locatedVariant.getPosition());

                if (CollectionUtils.isNotEmpty(transcriptMapsList)) {

                    logger.debug("transcriptMapsList.size(): {}", transcriptMapsList.size());
                    List<TranscriptMaps> distinctTranscriptMapsList = transcriptMapsList.stream().map(a -> a.getTranscript().getId())
                            .distinct()
                            .map(a -> transcriptMapsList.stream().filter(b -> b.getTranscript().getId().equals(a)).findAny().get())
                            .collect(Collectors.toList());

                    logger.debug("distinctTranscriptMapsList.size(): {}", distinctTranscriptMapsList.size());
                    distinctTranscriptMapsList.sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                    for (TranscriptMaps tMap : distinctTranscriptMapsList) {

                        logger.debug(tMap.toString());

                        List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRefId, refseqVersion, tMap.getTranscript().getId());

                        List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                .findByTranscriptMapsId(tMap.getId());

                        Variants_80_4 variant = null;

                        // note (possible FIXME...) This only handles the 1st match. What if a deletion spans two exons?
                        Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.stream()
                                .filter(a -> a.getContigRange().contains(locatedVariant.getPosition())).findAny();

                        if (optionalTranscriptMapsExons.isPresent()) {

                            TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                            logger.debug(transcriptMapsExons.toString());

                            if (transcriptMapsExons.getContigRange().containsRange(locatedVariant.toRange())) {
                                if (Arrays.asList("ins", "sub").contains(locatedVariant.getVariantType().getId())) {
                                    variant = createExonicInsertionMutation(locatedVariant, mapsList, transcriptMapsExonsList,
                                            transcriptMapsExons);
                                } else {
                                    variant = createExonicDeletionMutation(locatedVariant, mapsList, transcriptMapsExonsList,
                                            transcriptMapsExons);
                                }
                            } else if (locatedVariant.toRange().isOverlappedBy(transcriptMapsExons.getContigRange())) {
                                variant = createBorderCrossingVariant(locatedVariant, tMap, mapsList, transcriptMapsExonsList,
                                        transcriptMapsExons);
                            }

                        } else {

                            optionalTranscriptMapsExons = transcriptMapsExonsList.stream().filter(
                                    a -> a.getContigRange().contains(locatedVariant.getPosition() + locatedVariant.getRef().length()))
                                    .findAny();

                            if (optionalTranscriptMapsExons.isPresent()) {

                                TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                                logger.debug(transcriptMapsExons.toString());

                                if (locatedVariant.toRange().isOverlappedBy(transcriptMapsExons.getContigRange()) && (!locatedVariant
                                        .getEndPosition().equals(transcriptMapsExons.getContigRange().getMinimum())
                                        && !locatedVariant.getEndPosition().equals(transcriptMapsExons.getContigRange().getMaximum()))) {
                                    variant = createBorderCrossingVariant(locatedVariant, tMap, mapsList, transcriptMapsExonsList,
                                            transcriptMapsExons);
                                } else {
                                    variant = createIntronicVariant(locatedVariant, mapsList, tMap, transcriptMapsExonsList);
                                }

                            } else {
                                variant = createIntronicVariant(locatedVariant, mapsList, tMap, transcriptMapsExonsList);
                            }
                        }
                        variants.add(variant);

                    }

                } else {

                    // FIXME - will not get here if `pos` is in ANY transcript (so can miss transcripts where pos in one, but end_pos in
                    // another)...

                    // try searching by adjusting for length of locatedVariant.getSeq()...could be intron/exon
                    // boundary crossing

                    final List<TranscriptMaps> boundaryCrossingRightTranscriptMapsList = daoBean.getTranscriptMapsDAO()
                            .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRefId, refseqVersion,
                                    locatedVariant.getGenomeRefSeq().getId(),
                                    locatedVariant.getPosition() + locatedVariant.getRef().length() - 1);

                    if (CollectionUtils.isNotEmpty(boundaryCrossingRightTranscriptMapsList)) {

                        List<TranscriptMaps> distinctBoundaryCrossingTranscriptMapsList = boundaryCrossingRightTranscriptMapsList
                                .stream().map(a -> a.getTranscript().getId()).distinct().map(a -> boundaryCrossingRightTranscriptMapsList
                                        .stream().filter(b -> b.getTranscript().getId().equals(a)).findAny().get())
                                .collect(Collectors.toList());

                        distinctBoundaryCrossingTranscriptMapsList
                                .sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                        for (TranscriptMaps tMap : distinctBoundaryCrossingTranscriptMapsList) {
                            logger.debug(tMap.toString());

                            List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                    .findByTranscriptMapsId(tMap.getId());

                            List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO().findByGenomeRefIdAndRefSeqVersionAndTranscriptId(
                                    genomeRefId, refseqVersion, tMap.getTranscript().getId());

                            Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.stream().filter(
                                    a -> a.getContigRange().contains(locatedVariant.getPosition() + locatedVariant.getRef().length() - 1))
                                    .findAny();

                            Variants_80_4 variant = null;

                            if (optionalTranscriptMapsExons.isPresent()) {

                                // we have a border crossing variant starting in an exon
                                TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                                logger.debug(transcriptMapsExons.toString());
                                variant = createBorderCrossingVariant(locatedVariant, tMap, mapsList, transcriptMapsExonsList,
                                        transcriptMapsExons);

                            } else {
                                // we have a border crossing variant starting in an intron
                                variant = createBorderCrossingVariant(locatedVariant, tMap, mapsList, transcriptMapsExonsList, null);
                            }
                            variants.add(variant);

                        }

                    }

                    if (CollectionUtils.isEmpty(boundaryCrossingRightTranscriptMapsList)) {

                        final List<TranscriptMaps> boundaryCrossingLeftTranscriptMapsList = daoBean.getTranscriptMapsDAO()
                                .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRefId, refseqVersion,
                                        locatedVariant.getGenomeRefSeq().getId(),
                                        locatedVariant.getPosition() - locatedVariant.getRef().length());

                        if (CollectionUtils.isNotEmpty(boundaryCrossingLeftTranscriptMapsList)) {

                            List<TranscriptMaps> distinctBoundaryCrossingTranscriptMapsList = boundaryCrossingLeftTranscriptMapsList
                                    .stream().map(a -> a.getTranscript().getId()).distinct().map(a -> boundaryCrossingLeftTranscriptMapsList
                                            .stream().filter(b -> b.getTranscript().getId().equals(a)).findAny().get())
                                    .collect(Collectors.toList());

                            distinctBoundaryCrossingTranscriptMapsList
                                    .sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                            for (TranscriptMaps tMap : distinctBoundaryCrossingTranscriptMapsList) {

                                List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                        .findByTranscriptMapsId(tMap.getId());

                                List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                        .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRefId, refseqVersion,
                                                tMap.getTranscript().getId());

                                Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.stream().filter(
                                        a -> a.getContigRange().contains(locatedVariant.getPosition() - locatedVariant.getRef().length()))
                                        .findAny();
                                Variants_80_4 variant = null;
                                if (optionalTranscriptMapsExons.isPresent()) {
                                    TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                                    logger.debug(transcriptMapsExons.toString());
                                    variant = createBorderCrossingVariant(locatedVariant, tMap, mapsList, transcriptMapsExonsList,
                                            transcriptMapsExons);
                                } else {
                                    // we have a border crossing variant starting in an intron
                                    variant = createBorderCrossingVariant(locatedVariant, tMap, mapsList, transcriptMapsExonsList, null);
                                }
                                variants.add(variant);
                            }
                        } else {
                            // not found in any transcript contig range, must be intergenic
                            Variants_80_4 variant = createIntergenicVariant(locatedVariant);
                            variants.add(variant);
                        }

                    }

                }

            }

            for (Variants_80_4 variant : variants) {
                logger.debug(variant.toString());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }
        return variants;

    }

    private Variants_80_4 createProvisionalVariant(LocatedVariant locatedVariant, String transcript, Integer transcriptMapsIndex) {
        logger.debug("ENTERING createProvisionalVariant(LocatedVariant, String, Integer)");

        Variants_80_4PK variantKey = new Variants_80_4PK(locatedVariant.getId(), locatedVariant.getGenomeRefSeq().getId(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getId(), transcript, transcriptMapsIndex);

        Variants_80_4 variant = new Variants_80_4(variantKey);

        variant.setVariantType(locatedVariant.getVariantType());
        variant.setGenomeRefSeq(locatedVariant.getGenomeRefSeq());
        variant.setLocatedVariant(locatedVariant);
        variant.setReferenceAllele(locatedVariant.getRef());
        variant.setAlternateAllele(locatedVariant.getSeq() != null ? locatedVariant.getSeq() : "");

        variant.setHgvsGenomic(toHGVS(locatedVariant.getGenomeRefSeq().getId(), "g", locatedVariant.getVariantType().getId(),
                locatedVariant.getPosition(), locatedVariant.getRef(), locatedVariant.getSeq()));

        return variant;
    }

    private void setVariantGeneInfo(Variants_80_4 variant, String transcriptId) throws BinningException, CANVASDAOException {

        RefSeqGene refSeqGene = daoBean.getRefSeqGeneDAO().findByRefSeqVersionAndTranscriptId(getRefSeqVersion(), transcriptId).stream()
                .findAny().orElse(null);
        if (refSeqGene == null) {
            throw new BinningException(String.format("RefSeqGene not found: %s", transcriptId));
        }
        variant.setRefSeqGene(refSeqGene.getName());

        AnnotationGeneExternalId annotationGeneExternalIds = daoBean.getAnnotationGeneExternalIdDAO().findByExternalId(refSeqGene.getId())
                .stream().filter(a -> !"OMIM".equals(a.getId().getNamespace())).findFirst().orElse(null);
        if (annotationGeneExternalIds == null) {
            throw new BinningException(String.format("AnnotationGeneExternalId: %s", refSeqGene.getId()));
        }
        variant.setGene(annotationGeneExternalIds.getGene());

        List<HGNCGene> hgncGeneList = daoBean.getHGNCGeneDAO().findByAnnotationGeneExternalIdsGeneIdsAndNamespace(variant.getGene().getId(),
                "HGNC");
        variant.setHgncGene(hgncGeneList.stream().map(a -> a.getSymbol()).findFirst().orElse("None"));

    }

    public Variants_80_4 createIntronicVariant(LocatedVariant locatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps tMap,
            List<TranscriptMapsExons> transcriptMapsExonsList) throws BinningException {
        logger.debug(
                "ENTERING createIntronicVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>)");
        Variants_80_4 variant = createProvisionalVariant(locatedVariant, tMap.getTranscript().getId(), mapsList.indexOf(tMap) + 1);

        try {

            if ("del".equals(locatedVariant.getVariantType().getId())) {
                variant.setAlternateAllele("");
            }

            variant.setNumberOfTranscriptMaps(mapsList.size());
            variant.setStrand(tMap.getStrand());

            variant.setLocationType(allLocationTypes.stream().filter(a -> a.getId().equals("intron")).findFirst().get());
            variant.getId().setLocationType(variant.getLocationType().getId());

            variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("intron")).findFirst().get());
            variant.getId().setVariantEffect(variant.getVariantEffect().getId());

            setVariantGeneInfo(variant, tMap.getTranscript().getId());

            Range<Integer> proteinRange = null;

            RefSeqCodingSequence refSeqCDS = daoBean.getRefSeqCodingSequenceDAO()
                    .findByRefSeqVersionAndTranscriptId(getRefSeqVersion(), tMap.getTranscript().getId()).stream().findFirst().orElse(null);
            if (refSeqCDS != null) {
                RegionGroupRegion rgr = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId()).stream()
                        .findFirst().orElse(null);
                if (rgr != null) {
                    proteinRange = rgr.getId().getRegionRange();
                }
            }

            ListIterator<TranscriptMapsExons> transcriptMapsExonsIter = transcriptMapsExonsList.stream()
                    .sorted((a, b) -> Integer.compare(a.getId().getExonNum(), b.getId().getExonNum())).collect(Collectors.toList())
                    .listIterator();

            TranscriptMapsExons previous = null;
            while (transcriptMapsExonsIter.hasNext()) {
                TranscriptMapsExons current = transcriptMapsExonsIter.next();
                logger.debug(current.toString());
                if (previous != null && current != null) {
                    Range<Integer> intronContigRange = Range.between(previous.getContigRange().getMaximum(),
                            current.getContigRange().getMinimum());

                    Range<Integer> previousTranscriptRange = previous.getTranscriptRange();
                    Range<Integer> currentTranscriptRange = current.getTranscriptRange();
                    Range<Integer> previousContigRange = previous.getContigRange();
                    Range<Integer> currentContigRange = current.getContigRange();

                    if (intronContigRange.contains(locatedVariant.getPosition())) {

                        if (proteinRange != null) {

                            Integer rightDistance = null;
                            Integer leftDistance = null;

                            if ("-".equals(tMap.getStrand())) {

                                rightDistance = previousContigRange.getMinimum() - locatedVariant.getPosition();
                                leftDistance = currentContigRange.getMaximum() - locatedVariant.getPosition();

                                if (Math.abs(leftDistance) < Math.abs(rightDistance)) {
                                    variant.setTranscriptPosition(currentTranscriptRange.getMinimum());
                                    variant.setIntronExonDistance(leftDistance);
                                } else {
                                    variant.setTranscriptPosition(previousTranscriptRange.getMaximum());
                                    variant.setIntronExonDistance(rightDistance);
                                }

                                if (Math.abs(variant.getIntronExonDistance()) <= 2) {

                                    variant.setVariantEffect(
                                            allVariantEffects.stream().filter(a -> a.getId().equals("splice-site")).findFirst().get());
                                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());

                                    if (variant.getTranscriptPosition() > proteinRange.getMaximum()) {
                                        variant.setVariantEffect(allVariantEffects.stream()
                                                .filter(a -> a.getId().equals("splice-site-UTR-3")).findFirst().get());
                                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                                    }

                                    if (variant.getTranscriptPosition() < proteinRange.getMinimum()) {
                                        variant.setVariantEffect(allVariantEffects.stream()
                                                .filter(a -> a.getId().equals("splice-site-UTR-5")).findFirst().get());
                                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                                    }

                                }

                                variant.setHgvsCodingSequence(toHGVS(tMap.getTranscript().getId(), "c", variant.getVariantType().getId(),
                                        Math.abs(proteinRange.getMinimum() - variant.getTranscriptPosition() - 1), locatedVariant.getRef(),
                                        locatedVariant.getSeq(), variant.getIntronExonDistance(), "-".equals(tMap.getStrand())));

                            } else {

                                rightDistance = locatedVariant.getPosition() - currentContigRange.getMinimum();
                                leftDistance = locatedVariant.getPosition() - previousContigRange.getMaximum();

                                if (Math.abs(leftDistance) < Math.abs(rightDistance)) {
                                    variant.setTranscriptPosition(previousTranscriptRange.getMaximum());
                                    variant.setIntronExonDistance(leftDistance);
                                } else {
                                    variant.setTranscriptPosition(currentTranscriptRange.getMinimum());
                                    variant.setIntronExonDistance(rightDistance);
                                }

                                if (Math.abs(variant.getIntronExonDistance()) <= 2) {

                                    variant.setVariantEffect(
                                            allVariantEffects.stream().filter(a -> a.getId().equals("splice-site")).findFirst().get());
                                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());

                                    if (variant.getTranscriptPosition() > proteinRange.getMaximum()) {
                                        variant.setVariantEffect(allVariantEffects.stream()
                                                .filter(a -> a.getId().equals("splice-site-UTR-3")).findFirst().get());
                                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                                    }

                                    if (variant.getTranscriptPosition() < proteinRange.getMinimum()) {
                                        variant.setVariantEffect(allVariantEffects.stream()
                                                .filter(a -> a.getId().equals("splice-site-UTR-5")).findFirst().get());
                                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                                    }

                                }

                                variant.setHgvsCodingSequence(toHGVS(tMap.getTranscript().getId(), "c", variant.getVariantType().getId(),
                                        variant.getTranscriptPosition() - proteinRange.getMinimum(), locatedVariant.getRef(),
                                        locatedVariant.getSeq(), variant.getIntronExonDistance(), "-".equals(tMap.getStrand())));

                            }

                        }
                    }

                }
                previous = current;
            }

            if (variant.getIntronExonDistance() != null && Math.abs(variant.getIntronExonDistance()) <= 2 && proteinRange == null) {
                variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("splice-site-UTR")).findFirst().get());
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return variant;
    }

    public Variants_80_4 createIntergenicVariant(LocatedVariant locatedVariant) throws BinningException {
        logger.debug("ENTERING createIntergenicVariant(LocatedVariant)");

        Variants_80_4 variant = createProvisionalVariant(locatedVariant, "", 0);

        if ("del".equals(locatedVariant.getVariantType().getId())) {
            variant.setAlternateAllele("");
        }

        LocationType intergenicLocationType = allLocationTypes.stream().filter(a -> a.getId().equals("intergenic")).findFirst().get();
        variant.setLocationType(intergenicLocationType);

        VariantEffect variantEffect = allVariantEffects.stream().filter(a -> a.getId().equals("intergenic")).findFirst().get();
        variant.setVariantEffect(variantEffect);

        return variant;
    }

    public Variants_80_4 createBorderCrossingVariant(LocatedVariant locatedVariant, TranscriptMaps tMap, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createBorderCrossingVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");

        Variants_80_4 variant = createProvisionalVariant(locatedVariant, tMap.getTranscript().getId(), mapsList.indexOf(tMap) + 1);

        try {

            if ("del".equals(locatedVariant.getVariantType().getId())) {
                variant.setAlternateAllele("");
            }

            variant.setNumberOfTranscriptMaps(mapsList.size());
            variant.setStrand(tMap.getStrand());

            setVariantGeneInfo(variant, tMap.getTranscript().getId());

            variant.setIntronExonDistance(0);
            variant.setHgvsCodingSequence("?");
            variant.setHgvsTranscript("?");
            variant.setHgvsProtein("?");

            Range<Integer> locatedVariantRange = locatedVariant.toRange();
            Range<Integer> transcriptMapsExonsContigRange = null;
            Range<Integer> transcriptMapsExonsTranscriptRange = null;
            if (transcriptMapsExons != null) {
                transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
                transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();

                if (transcriptMapsExonsContigRange.contains(locatedVariantRange.getMinimum())) {
                    if ("+".equals(tMap.getStrand())) {
                        variant.setTranscriptPosition((locatedVariantRange.getMinimum() - transcriptMapsExonsContigRange.getMinimum())
                                + transcriptMapsExonsTranscriptRange.getMinimum());
                    } else {
                        variant.setTranscriptPosition(transcriptMapsExonsTranscriptRange.getMaximum()
                                - (locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMinimum()) + 1);
                    }
                } else {
                    if ("+".equals(tMap.getStrand())) {
                        variant.setTranscriptPosition(transcriptMapsExonsTranscriptRange.getMinimum());
                    } else {
                        variant.setTranscriptPosition(transcriptMapsExonsTranscriptRange.getMaximum()
                                - (locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMinimum()) + 1);
                    }
                }

            }

            Range<Integer> proteinRange = null;
            RefSeqCodingSequence refSeqCDS = daoBean.getRefSeqCodingSequenceDAO()
                    .findByRefSeqVersionAndTranscriptId(getRefSeqVersion(), tMap.getTranscript().getId()).stream().findFirst().orElse(null);
            if (refSeqCDS != null) {
                RegionGroupRegion rgr = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId()).stream()
                        .findFirst().orElse(null);
                if (rgr != null) {
                    proteinRange = rgr.getId().getRegionRange();
                }
            }

            variant.setLocationType(allLocationTypes.stream().filter(a -> a.getId().equals("intron/exon boundary")).findFirst().get());
            variant.getId().setLocationType(variant.getLocationType().getId());

            if (locatedVariant.getSeq().contains("LARGEDELETION")) {
                return variant;
            }

            Integer p = Math.max(1, (locatedVariantRange.getMinimum() - transcriptMapsExonsContigRange.getMinimum())
                    + transcriptMapsExonsTranscriptRange.getMinimum());

            if (proteinRange != null && proteinRange.contains(p)) {
                variant.setVariantEffect(
                        allVariantEffects.stream().filter(a -> a.getId().equals("boundary-crossing indel")).findFirst().get());
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
            } else {
                variant.setVariantEffect(
                        allVariantEffects.stream().filter(a -> a.getId().equals("noncoding boundary-crossing indel")).findFirst().get());
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return variant;
    }

    public Variants_80_4 createExonicSNPMutation(LocatedVariant locatedVariant, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createExonicSNPMutation(String, LocatedVariant, List<TranscriptMaps>, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");

        Variants_80_4 variant = createProvisionalVariant(locatedVariant, transcriptMapsExons.getTranscriptMaps().getTranscript().getId(),
                mapsList.indexOf(transcriptMapsExons.getTranscriptMaps()) + 1);

        try {

            variant.setNumberOfTranscriptMaps(mapsList.size());
            variant.setStrand(transcriptMapsExons.getTranscriptMaps().getStrand());

            setVariantGeneInfo(variant, transcriptMapsExons.getTranscriptMaps().getTranscript().getId());

            Range<Integer> proteinRange = null;
            RefSeqCodingSequence refSeqCDS = daoBean.getRefSeqCodingSequenceDAO()
                    .findByRefSeqVersionAndTranscriptId(getRefSeqVersion(), transcriptMapsExons.getTranscriptMaps().getTranscript().getId())
                    .stream().findFirst().orElse(null);
            if (refSeqCDS != null) {
                RegionGroupRegion rgr = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId()).stream()
                        .findFirst().orElse(null);
                if (rgr != null) {
                    proteinRange = rgr.getId().getRegionRange();
                }
            }

            Range<Integer> locatedVariantRange = locatedVariant.toRange();
            Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
            Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();

            logger.debug(transcriptMapsExons.getTranscriptMaps().getTranscript().toString());

            Integer transcriptPosition = getTranscriptPosition(locatedVariant, transcriptMapsExons, proteinRange);
            variant.setTranscriptPosition(transcriptPosition);

            Feature feature = daoBean.getFeatureDAO()
                    .findByRefSeqVersionAndTranscriptIdAndTranscriptPosition(getRefSeqVersion(),
                            transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), variant.getTranscriptPosition())
                    .stream().sorted((a, b) -> b.getRegionGroup().getId().compareTo(a.getRegionGroup().getId())).findFirst().orElse(null);
            if (feature != null) {
                logger.debug(feature.toString());
                variant.setFeatureId(feature.getId());
            }

            variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList));

            if (variant.getIntronExonDistance() != null && variant.getIntronExonDistance().equals(variant.getTranscriptPosition())) {
                variant.setIntronExonDistance(null);
            }

            variant.setHgvsTranscript(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "g",
                    variant.getVariantType().getId(), variant.getTranscriptPosition(), locatedVariant.getRef(), locatedVariant.getSeq(),
                    null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

            String locationType = getLocationType(daoBean, locatedVariantRange, transcriptMapsExonsContigRange,
                    transcriptMapsExonsTranscriptRange, proteinRange, transcriptMapsExons.getTranscriptMaps(),
                    variant.getTranscriptPosition());

            variant.setLocationType(allLocationTypes.stream().filter(a -> a.getId().equals(locationType)).findFirst().get());
            variant.getId().setLocationType(variant.getLocationType().getId());

            List<String> utrLocationTypes = Arrays.asList("UTR", "UTR-5", "UTR-3");

            if (utrLocationTypes.contains(variant.getLocationType().getId())) {

                if (proteinRange != null && proteinRange.isOverlappedBy(transcriptMapsExonsTranscriptRange)) {

                    variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList,
                            proteinRange, variant.getTranscriptPosition()));

                    Integer position = null;

                    switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
                        case "+":
                            position = Math
                                    .abs(proteinRange.getMinimum() - variant.getTranscriptPosition() + variant.getIntronExonDistance() - 1);
                            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                                    variant.getVariantType().getId(), position, locatedVariant.getRef(), locatedVariant.getSeq(),
                                    variant.getIntronExonDistance()));
                            break;
                        case "-":
                            position = variant.getLocationType().getId().equals("UTR-3") ? proteinRange.getMaximum()
                                    : proteinRange.getMinimum();
                            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                                    variant.getVariantType().getId(), position, locatedVariant.getRef(), locatedVariant.getSeq(),
                                    variant.getIntronExonDistance(), true));
                            break;
                    }

                }

                variant.setVariantEffect(
                        allVariantEffects.stream().filter(a -> a.getId().equals(variant.getId().getLocationType())).findFirst().get());
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                return variant;
            }

            // must be in exon location type
            variant.setNonCanonicalExon(transcriptMapsExons.getId().getExonNum());

            if (variant.getFeatureId() == null) {
                variant.setFeatureId(0);
            }

            if (proteinRange != null) {

                variant.setInframe(Boolean.FALSE);
                variant.setFrameshift(Boolean.FALSE);

                if ("+".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    transcriptMapsExonsList.sort((a, b) -> Integer.compare(a.getId().getExonNum(), b.getId().getExonNum()));
                } else {
                    transcriptMapsExonsList.sort((a, b) -> Integer.compare(b.getId().getExonNum(), a.getId().getExonNum()));
                }

                // variant.setNonCanonicalExon(transcriptMapsExonsList.indexOf(transcriptMapsExons) + 1);
                variant.setNonCanonicalExon(getNonCanonicalExon(transcriptMapsExonsList, transcriptMapsExons, proteinRange));

                Integer intronExonDistance = getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList,
                        proteinRange, variant.getTranscriptPosition());
                variant.setIntronExonDistance(intronExonDistance);

                Integer codingSequencePosition = getCodingSequencePosition(locatedVariant, transcriptMapsExons,
                        variant.getTranscriptPosition(), proteinRange);
                variant.setCodingSequencePosition(codingSequencePosition);

                String transcriptDNASequence = transcriptMapsExons.getTranscriptMaps().getTranscript().getSeq();
                String originalDNASeq = transcriptDNASequence.substring(proteinRange.getMinimum() - 1, proteinRange.getMaximum());

                DNASequence originalDNASequence = new DNASequence(originalDNASeq);
                Sequence<NucleotideCompound> originalRNASequence = dna2RnaTranslator.createSequence(originalDNASequence);
                Sequence<AminoAcidCompound> originalProteinSequence = rna2AminoAcidTranslator.createSequence(originalRNASequence);

                variant.setInframe(variant.getCodingSequencePosition() % 3 == 0);

                Integer aaStart = getAminoAcidStart(variant.getVariantType().getId(), variant.getCodingSequencePosition(),
                        variant.getFrameshift(), variant.getInframe(), variant.getReferenceAllele(),
                        transcriptMapsExons.getTranscriptMaps().getStrand());

                variant.setAminoAcidStart(aaStart);
                variant.setAminoAcidEnd(aaStart + 1);

                AminoAcidCompound originalAACompound = null;

                if (aaStart > originalProteinSequence.getLength()) {
                    variant.setOriginalAminoAcid("*");
                } else {
                    originalAACompound = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                    variant.setOriginalAminoAcid(originalAACompound.getBase());
                }

                Pair<String, String> dnaSequenceParts = getDNASequenceParts(variant.getVariantType().getId(),
                        transcriptMapsExons.getTranscriptMaps().getStrand(), transcriptDNASequence, proteinRange,
                        variant.getCodingSequencePosition(), variant.getReferenceAllele(), variant.getAlternateAllele());

                String finalDNASeq = String.format("%s%s%s", dnaSequenceParts.getLeft(), variant.getAlternateAllele(),
                        dnaSequenceParts.getRight());

                if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    String altAllele = new DNASequence(variant.getAlternateAllele()).getReverseComplement().getSequenceAsString();
                    finalDNASeq = String.format("%s%s%s", dnaSequenceParts.getLeft(), altAllele, dnaSequenceParts.getRight());
                }

                DNASequence finalDNASequence = new DNASequence(finalDNASeq);
                Sequence<NucleotideCompound> finalRNASequence = dna2RnaTranslator.createSequence(finalDNASequence);
                Sequence<AminoAcidCompound> finalProteinSequence = rna2AminoAcidTranslator.createSequence(finalRNASequence);

                if (variant.getAminoAcidStart() > finalProteinSequence.getLength()) {
                    variant.setFinalAminoAcid("*");
                } else {
                    AminoAcidCompound finalAACompound = finalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                    variant.setFinalAminoAcid(finalAACompound.getBase());
                }

                if (variant.getOriginalAminoAcid().equals(variant.getFinalAminoAcid())) {
                    variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("synonymous")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                } else if (!variant.getOriginalAminoAcid().equals("*") && !variant.getFinalAminoAcid().equals("*")) {
                    variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("missense")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                } else if (variant.getFinalAminoAcid().equals("*")) {
                    variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("nonsense")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                } else if (variant.getOriginalAminoAcid().equals("*")) {
                    variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("stoploss")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                }

                if (refSeqCDS != null) {
                    String finalAACompoundLongName = AminoAcidCompoundSet.getAminoAcidCompoundSet()
                            .getCompoundForString(Arrays.asList(variant.getFinalAminoAcid().split("(?!^)")).stream().findFirst().get())
                            .getLongName();

                    if (!Arrays.asList("synonymous", "stoploss").contains(variant.getVariantEffect().getId())) {

                        variant.setHgvsProtein(String.format("%s:p.%s%d%s", refSeqCDS.getProteinId(), originalAACompound.getLongName(),
                                variant.getAminoAcidStart(),
                                "*".equals(variant.getFinalAminoAcid()) ? variant.getFinalAminoAcid() : finalAACompoundLongName));
                    }

                    if ("stoploss".equals(variant.getVariantEffect().getId())) {
                        variant.setHgvsProtein(String.format("%s:p.*%d%s", refSeqCDS.getProteinId(), variant.getAminoAcidStart(),
                                "*".equals(variant.getFinalAminoAcid()) ? variant.getFinalAminoAcid() : finalAACompoundLongName));
                    }

                }

                variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                        variant.getVariantType().getId(), variant.getCodingSequencePosition(), locatedVariant.getRef(),
                        locatedVariant.getSeq(), null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return variant;
    }

    public Variants_80_4 createExonicInsertionMutation(LocatedVariant locatedVariant, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createExonicInsertionMutation(LocatedVariant, List<TranscriptMaps>, List<TranscriptMapsExons>, TranscriptMapsExons)");

        Variants_80_4 variant = createProvisionalVariant(locatedVariant, transcriptMapsExons.getTranscriptMaps().getTranscript().getId(),
                mapsList.indexOf(transcriptMapsExons.getTranscriptMaps()) + 1);

        try {

            variant.setNumberOfTranscriptMaps(mapsList.size());
            variant.setStrand(transcriptMapsExons.getTranscriptMaps().getStrand());

            setVariantGeneInfo(variant, transcriptMapsExons.getTranscriptMaps().getTranscript().getId());

            Range<Integer> locatedVariantRange = locatedVariant.toRange();
            Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
            Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();

            Range<Integer> proteinRange = null;
            RefSeqCodingSequence refSeqCDS = daoBean.getRefSeqCodingSequenceDAO()
                    .findByRefSeqVersionAndTranscriptId(getRefSeqVersion(), transcriptMapsExons.getTranscriptMaps().getTranscript().getId())
                    .stream().findFirst().orElse(null);
            if (refSeqCDS != null) {
                RegionGroupRegion rgr = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId()).stream()
                        .findFirst().orElse(null);
                if (rgr != null) {
                    proteinRange = rgr.getId().getRegionRange();
                }
            }

            Integer transcriptPosition = getTranscriptPosition(locatedVariant, transcriptMapsExons, proteinRange);
            variant.setTranscriptPosition(transcriptPosition);

            Feature feature = daoBean.getFeatureDAO()
                    .findByRefSeqVersionAndTranscriptIdAndTranscriptPosition(getRefSeqVersion(),
                            transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), variant.getTranscriptPosition())
                    .stream().sorted((a, b) -> b.getRegionGroup().getId().compareTo(a.getRegionGroup().getId())).findFirst().orElse(null);
            if (feature != null) {
                logger.debug(feature.toString());
                variant.setFeatureId(feature.getId());
            }

            variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList));

            if (variant.getIntronExonDistance() != null && variant.getIntronExonDistance().equals(variant.getTranscriptPosition())) {
                variant.setIntronExonDistance(null);
            }

            variant.setHgvsTranscript(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "g",
                    variant.getVariantType().getId(), variant.getTranscriptPosition(), locatedVariant.getRef(), locatedVariant.getSeq(),
                    null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

            String locationType = getLocationType(daoBean, locatedVariantRange, transcriptMapsExonsContigRange,
                    transcriptMapsExonsTranscriptRange, proteinRange, transcriptMapsExons.getTranscriptMaps(),
                    variant.getTranscriptPosition());

            variant.setLocationType(allLocationTypes.stream().filter(a -> a.getId().equals(locationType)).findFirst().get());
            variant.getId().setLocationType(variant.getLocationType().getId());

            List<String> utrLocationTypes = Arrays.asList("UTR", "UTR-5", "UTR-3");

            if (utrLocationTypes.contains(variant.getLocationType().getId())) {

                if (proteinRange != null && proteinRange.isOverlappedBy(transcriptMapsExonsTranscriptRange)) {

                    variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList,
                            proteinRange, variant.getTranscriptPosition()));

                    switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
                        case "+":

                            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                                    variant.getVariantType().getId(), Math.abs(proteinRange.getMinimum() - variant.getTranscriptPosition()
                                            + variant.getIntronExonDistance() - 1),
                                    locatedVariant.getRef(), locatedVariant.getSeq(), variant.getIntronExonDistance()));
                            break;
                        case "-":
                            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                                    variant.getVariantType().getId(), proteinRange.getMaximum(), locatedVariant.getRef(),
                                    locatedVariant.getSeq(), variant.getIntronExonDistance(), true));
                            break;
                    }

                }

                variant.setVariantEffect(
                        allVariantEffects.stream().filter(a -> a.getId().equals(variant.getId().getLocationType())).findFirst().get());
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                return variant;

            }

            // must be in exon
            variant.setNonCanonicalExon(transcriptMapsExons.getId().getExonNum());

            if (variant.getFeatureId() == null) {
                variant.setFeatureId(0);
            }

            if (proteinRange != null) {

                variant.setInframe(Boolean.FALSE);
                variant.setFrameshift(Boolean.FALSE);

                if ("+".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    transcriptMapsExonsList.sort((a, b) -> Integer.compare(a.getId().getExonNum(), b.getId().getExonNum()));
                } else {
                    transcriptMapsExonsList.sort((a, b) -> Integer.compare(b.getId().getExonNum(), a.getId().getExonNum()));
                }

                variant.setNonCanonicalExon(getNonCanonicalExon(transcriptMapsExonsList, transcriptMapsExons, proteinRange));

                variant.setCodingSequencePosition(
                        getCodingSequencePosition(locatedVariant, transcriptMapsExons, variant.getTranscriptPosition(), proteinRange));

                variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList,
                        proteinRange, variant.getTranscriptPosition()));

                String transcriptDNASequence = transcriptMapsExons.getTranscriptMaps().getTranscript().getSeq();
                String originalDNASeq = transcriptDNASequence.substring(proteinRange.getMinimum() - 1, proteinRange.getMaximum());

                DNASequence originalDNASequence = new DNASequence(originalDNASeq);
                Sequence<NucleotideCompound> originalRNASequence = dna2RnaTranslator.createSequence(originalDNASequence);
                Sequence<AminoAcidCompound> originalProteinSequence = rna2AminoAcidTranslator.createSequence(originalRNASequence);

                variant.setFrameshift((variant.getAlternateAllele().length() - variant.getReferenceAllele().length()) % 3 != 0);
                variant.setInframe(
                        isInframe(originalDNASequence, variant.getCodingSequencePosition(), locatedVariant) && !variant.getFrameshift());

                Integer aaStart = getAminoAcidStart(variant.getVariantType().getId(), variant.getCodingSequencePosition(),
                        variant.getFrameshift(), variant.getInframe(), variant.getReferenceAllele(),
                        transcriptMapsExons.getTranscriptMaps().getStrand());

                variant.setAminoAcidStart(aaStart);

                if (variant.getReferenceAllele().length() == variant.getAlternateAllele().length()) {
                    variant.setAminoAcidEnd(aaStart);
                } else {
                    variant.setAminoAcidEnd(aaStart + Math.max(1, locatedVariant.getRef().length() / 3));
                }

                if (variant.getAminoAcidStart() > originalProteinSequence.getLength()) {
                    variant.setOriginalAminoAcid("*");
                } else if (variant.getAminoAcidStart() == originalProteinSequence.getLength()) {
                    if (variant.getAminoAcidStart() == variant.getAminoAcidEnd()) {
                        AminoAcidCompound retvalo = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                        variant.setOriginalAminoAcid(retvalo.getBase());
                    } else {
                        AminoAcidCompound retvalo = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                        variant.setOriginalAminoAcid(String.format("%s*", retvalo.getBase()));
                    }
                } else {

                    Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                            variant.getAminoAcidEnd());
                    variant.setOriginalAminoAcid(retvalo.getSequenceAsString());
                }

                Pair<String, String> dnaSequenceParts = getDNASequenceParts(variant.getVariantType().getId(),
                        transcriptMapsExons.getTranscriptMaps().getStrand(), transcriptDNASequence, proteinRange,
                        variant.getCodingSequencePosition(), variant.getReferenceAllele(), variant.getAlternateAllele());

                String finalDNASeq = String.format("%s%s%s", dnaSequenceParts.getLeft(), variant.getAlternateAllele(),
                        dnaSequenceParts.getRight());
                if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    String altAllele = new DNASequence(variant.getAlternateAllele()).getReverseComplement().getSequenceAsString();
                    finalDNASeq = String.format("%s%s%s", dnaSequenceParts.getLeft(), altAllele, dnaSequenceParts.getRight());
                }

                DNASequence finalDNASequence = new DNASequence(finalDNASeq);
                Sequence<NucleotideCompound> finalRNASequence = dna2RnaTranslator.createSequence(finalDNASequence);
                Sequence<AminoAcidCompound> finalProteinSequence = rna2AminoAcidTranslator.createSequence(finalRNASequence);

                if (variant.getFrameshift()) {

                    if (!"*".equals(variant.getOriginalAminoAcid())) {
                        if (originalProteinSequence.getLength() > variant.getAminoAcidStart() + 4) {
                            Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    variant.getAminoAcidStart() + 4);
                            variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));
                        } else {
                            Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    originalProteinSequence.getLength());
                            variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));
                        }
                    }

                    if (finalProteinSequence.getLength() > variant.getAminoAcidStart() + 4) {
                        Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                variant.getAminoAcidStart() + 4);
                        variant.setFinalAminoAcid(String.format("%s...", retvalf.getSequenceAsString()));
                    } else {
                        if (variant.getAminoAcidStart() < finalProteinSequence.getLength()) {
                            Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    finalProteinSequence.getLength());
                            variant.setFinalAminoAcid(String.format("%s...", retvalf.getSequenceAsString()));
                        } else {
                            variant.setFinalAminoAcid("*");
                        }
                    }

                    if ("*".equals(variant.getOriginalAminoAcid())) {
                        variant.setHgvsProtein(String.format("%s:p.*%dfs", refSeqCDS.getProteinId(), variant.getAminoAcidStart()));
                    }

                    if ((variant.getCodingSequencePosition() + variant.getAlternateAllele().length() - 1) > originalDNASeq.length()) {
                        StringBuilder sb = new StringBuilder();
                        Arrays.asList(variant.getOriginalAminoAcid().split("(?!^)")).stream().forEach(
                                a -> sb.append(AminoAcidCompoundSet.getAminoAcidCompoundSet().getCompoundForString(a).getLongName()));
                        variant.setHgvsProtein(
                                String.format("%s:p.Ter%d%sext*?", refSeqCDS.getProteinId(), variant.getAminoAcidStart(), sb.toString()));
                    }

                    if (variant.getOriginalAminoAcid().equals(variant.getFinalAminoAcid())) {
                        variant.setVariantEffect(
                                allVariantEffects.stream().filter(a -> a.getId().equals("synonymous indel")).findFirst().get());
                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                    } else {
                        variant.setVariantEffect(
                                allVariantEffects.stream().filter(a -> a.getId().equals("frameshifting indel")).findFirst().get());
                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                    }

                    variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                            variant.getVariantType().getId(), variant.getCodingSequencePosition(), locatedVariant.getRef(),
                            locatedVariant.getSeq(), null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

                    return variant;
                }

                StringBuilder longNames = new StringBuilder();

                if (refSeqCDS != null) {

                    String firstOriginalAACompoundValue = AminoAcidCompoundSet.getAminoAcidCompoundSet()
                            .getCompoundForString(Arrays.asList(variant.getOriginalAminoAcid().split("(?!^)")).stream().findFirst().get())
                            .getLongName();

                    if ((variant.getAminoAcidStart() > finalProteinSequence.getLength()
                            && variant.getAminoAcidEnd() > finalProteinSequence.getLength())
                            || (variant.getCodingSequencePosition() + variant.getReferenceAllele().length() - 1) > originalDNASeq
                                    .length()) {

                        if ("*".equals(variant.getOriginalAminoAcid())) {
                            firstOriginalAACompoundValue = variant.getOriginalAminoAcid();
                        }

                        variant.setHgvsProtein(String.format("%s:p.Ter%d%sext*?", refSeqCDS.getProteinId(), variant.getAminoAcidStart(),
                                firstOriginalAACompoundValue));

                    } else if (variant.getAminoAcidStart() == originalProteinSequence.getLength()
                            && variant.getAminoAcidEnd() == finalProteinSequence.getLength()) {

                        variant.setFinalAminoAcid(String.format("%s*", finalProteinSequence
                                .getSubSequence(variant.getAminoAcidStart(), variant.getAminoAcidEnd()).getSequenceAsString()));

                        finalProteinSequence.getSubSequence(variant.getAminoAcidStart() + 1, variant.getAminoAcidEnd())
                                .forEach(a -> longNames.append(a.getLongName()));

                        variant.setHgvsProtein(
                                String.format("%s:p.(*%s%sext*%s)", refSeqCDS.getProteinId(), variant.getAminoAcidStart() + 1,
                                        longNames.toString(), variant.getAminoAcidEnd() - variant.getAminoAcidStart()));

                    } else if (variant.getAminoAcidEnd() > finalProteinSequence.getLength()) {

                        Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                finalProteinSequence.getLength());
                        variant.setFinalAminoAcid(String.format("%s*", retvalf.getSequenceAsString()));
                        retvalf.forEach(a -> longNames.append(a.getLongName()));
                        variant.setHgvsProtein(
                                String.format("%s:p.%s%d_*%ddelins%s*", refSeqCDS.getProteinId(), firstOriginalAACompoundValue,
                                        variant.getAminoAcidStart(), variant.getAminoAcidEnd(), longNames.toString()));

                    } else if (variant.getAminoAcidStart() == variant.getAminoAcidEnd()) {

                        AminoAcidCompound originalAACompound = null;
                        AminoAcidCompound finalAACompound = null;
                        int tmpStart = variant.getAminoAcidStart() - 1;
                        for (int i = variant.getAminoAcidStart() - 1; i < originalProteinSequence.getLength(); i++) {
                            AminoAcidCompound tmpOriginalAACompound = originalProteinSequence.getCompoundAt(i);
                            AminoAcidCompound tmpFinalAACompound = finalProteinSequence.getCompoundAt(i);
                            if (tmpOriginalAACompound.getBase().equals(tmpFinalAACompound.getBase())) {
                                tmpStart++;
                                continue;
                            }
                            originalAACompound = tmpOriginalAACompound;
                            finalAACompound = tmpFinalAACompound;
                            break;
                        }

                        if (tmpStart == originalProteinSequence.getLength()) {

                            // meaning that we have traversed to the end of the protein finding no changes
                            variant.setOriginalAminoAcid(originalProteinSequence.getCompoundAt(variant.getAminoAcidStart()).getBase());
                            variant.setFinalAminoAcid(finalProteinSequence.getCompoundAt(variant.getAminoAcidStart()).getBase());

                            variant.setHgvsProtein(String.format("%s:p.%s%ddelins%s", refSeqCDS.getProteinId(),
                                    originalProteinSequence.getCompoundAt(variant.getAminoAcidStart()).getLongName(),
                                    variant.getAminoAcidStart(),
                                    finalProteinSequence.getCompoundAt(variant.getAminoAcidStart()).getLongName()));

                        } else {

                            if (finalAACompound != null && originalAACompound != null) {

                                variant.setOriginalAminoAcid(originalAACompound.getBase());
                                variant.setFinalAminoAcid(finalAACompound.getBase());
                                variant.setAminoAcidStart(tmpStart);
                                variant.setAminoAcidEnd(tmpStart);

                                variant.setHgvsProtein(String.format("%s:p.%s%d%s", refSeqCDS.getProteinId(),
                                        originalAACompound.getLongName(), tmpStart, finalAACompound.getLongName()));

                            } else {
                                SequenceView<AminoAcidCompound> originalView = originalProteinSequence
                                        .getSubSequence(variant.getAminoAcidStart(), tmpStart);
                                variant.setOriginalAminoAcid(originalView.getSequenceAsString());

                                SequenceView<AminoAcidCompound> finalView = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        tmpStart);
                                variant.setFinalAminoAcid(finalView.getSequenceAsString());
                                variant.setHgvsProtein(String.format("%s:p.%s%d%s", refSeqCDS.getProteinId(),
                                        originalView.getAsList().stream().map(a -> a.getLongName()).collect(Collectors.joining()), tmpStart,
                                        finalView.getAsList().stream().map(a -> a.getLongName()).collect(Collectors.joining())));
                            }

                        }

                    } else if (variant.getAminoAcidStart() < finalProteinSequence.getLength()
                            && variant.getAminoAcidEnd() < finalProteinSequence.getLength()) {

                        Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                variant.getAminoAcidEnd() + (variant.getAlternateAllele().length() / 3));
                        variant.setFinalAminoAcid(retvalf.getSequenceAsString());

                        retvalf.forEach(a -> {
                            if ("*".equals(a.getShortName())) {
                                longNames.append(a.getShortName());
                            } else {
                                longNames.append(a.getLongName());
                            }
                        });

                        variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddelins%s", refSeqCDS.getProteinId(),
                                firstOriginalAACompoundValue, variant.getAminoAcidStart(),
                                originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(), variant.getAminoAcidEnd(),
                                longNames.toString()));

                    }

                }

                if (finalProteinSequence.getSequenceAsString().contains("*")) {
                    variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("nonsense indel")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                } else if (variant.getOriginalAminoAcid().equals(variant.getFinalAminoAcid())) {
                    variant.setVariantEffect(
                            allVariantEffects.stream().filter(a -> a.getId().equals("synonymous indel")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                } else {
                    variant.setVariantEffect(
                            allVariantEffects.stream().filter(a -> a.getId().equals("non-frameshifting indel")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                }

            }

            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                    variant.getVariantType().getId(), variant.getCodingSequencePosition(), locatedVariant.getRef(), locatedVariant.getSeq(),
                    null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return variant;
    }

    public Variants_80_4 createExonicDeletionMutation(LocatedVariant locatedVariant, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createExonicDeletionMutation(LocatedVariant, List<TranscriptMaps>, List<TranscriptMapsExons>, TranscriptMapsExons)");

        Variants_80_4 variant = createProvisionalVariant(locatedVariant, transcriptMapsExons.getTranscriptMaps().getTranscript().getId(),
                mapsList.indexOf(transcriptMapsExons.getTranscriptMaps()) + 1);

        try {

            variant.setNumberOfTranscriptMaps(mapsList.size());
            variant.setStrand(transcriptMapsExons.getTranscriptMaps().getStrand());

            setVariantGeneInfo(variant, transcriptMapsExons.getTranscriptMaps().getTranscript().getId());

            variant.setAlternateAllele("");

            Range<Integer> locatedVariantRange = locatedVariant.toRange();
            Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
            Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();

            Range<Integer> proteinRange = null;
            RefSeqCodingSequence refSeqCDS = daoBean.getRefSeqCodingSequenceDAO()
                    .findByRefSeqVersionAndTranscriptId(getRefSeqVersion(), transcriptMapsExons.getTranscriptMaps().getTranscript().getId())
                    .stream().findFirst().orElse(null);
            if (refSeqCDS != null) {
                RegionGroupRegion rgr = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId()).stream()
                        .findFirst().orElse(null);
                if (rgr != null) {
                    proteinRange = rgr.getId().getRegionRange();
                }
            }

            Integer transcriptPosition = getTranscriptPosition(locatedVariant, transcriptMapsExons, proteinRange);
            variant.setTranscriptPosition(transcriptPosition);

            Feature feature = daoBean.getFeatureDAO()
                    .findByRefSeqVersionAndTranscriptIdAndTranscriptPosition(getRefSeqVersion(),
                            transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), variant.getTranscriptPosition())
                    .stream().sorted((a, b) -> b.getRegionGroup().getId().compareTo(a.getRegionGroup().getId())).findFirst().orElse(null);
            if (feature != null) {
                logger.debug(feature.toString());
                variant.setFeatureId(feature.getId());
            }

            variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList));

            if (variant.getIntronExonDistance() != null && variant.getIntronExonDistance().equals(variant.getTranscriptPosition())) {
                variant.setIntronExonDistance(null);
            }

            variant.setHgvsTranscript(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "g",
                    variant.getVariantType().getId(), variant.getTranscriptPosition(), locatedVariant.getRef(), locatedVariant.getSeq(),
                    null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

            String locationType = getLocationType(daoBean, locatedVariantRange, transcriptMapsExonsContigRange,
                    transcriptMapsExonsTranscriptRange, proteinRange, transcriptMapsExons.getTranscriptMaps(),
                    variant.getTranscriptPosition());

            variant.setLocationType(allLocationTypes.stream().filter(a -> a.getId().equals(locationType)).findFirst().get());
            variant.getId().setLocationType(variant.getLocationType().getId());

            List<String> utrLocationTypes = Arrays.asList("UTR", "UTR-5", "UTR-3");

            if (utrLocationTypes.contains(variant.getLocationType().getId())) {

                if (proteinRange != null && proteinRange.isOverlappedBy(transcriptMapsExonsTranscriptRange)) {

                    variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList,
                            proteinRange, variant.getTranscriptPosition()));

                    switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
                        case "+":
                            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                                    variant.getVariantType().getId(), Math.abs(proteinRange.getMinimum() - variant.getTranscriptPosition()
                                            + variant.getIntronExonDistance() - 1),
                                    locatedVariant.getRef(), locatedVariant.getSeq(), variant.getIntronExonDistance()));
                            break;
                        case "-":
                            if (transcriptPosition < proteinRange.getMaximum()) {
                                variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                                        variant.getVariantType().getId(), variant.getTranscriptPosition() - proteinRange.getMinimum() + 2,
                                        locatedVariant.getRef(), locatedVariant.getSeq(), null, false));
                            } else {
                                variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                                        variant.getVariantType().getId(), proteinRange.getMaximum(), locatedVariant.getRef(),
                                        locatedVariant.getSeq(), variant.getIntronExonDistance(), true));
                            }

                            break;
                    }

                }

                variant.setVariantEffect(
                        allVariantEffects.stream().filter(a -> a.getId().equals(variant.getId().getLocationType())).findFirst().get());
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                return variant;
            }

            variant.setNonCanonicalExon(transcriptMapsExons.getId().getExonNum());

            if (variant.getFeatureId() == null) {
                variant.setFeatureId(0);
            }

            if (proteinRange != null) {

                variant.setInframe(Boolean.FALSE);
                variant.setFrameshift(Boolean.FALSE);

                if ("+".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    transcriptMapsExonsList.sort((a, b) -> Integer.compare(a.getId().getExonNum(), b.getId().getExonNum()));
                } else {
                    transcriptMapsExonsList.sort((a, b) -> Integer.compare(b.getId().getExonNum(), a.getId().getExonNum()));
                }

                variant.setNonCanonicalExon(getNonCanonicalExon(transcriptMapsExonsList, transcriptMapsExons, proteinRange));

                variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList,
                        proteinRange, variant.getTranscriptPosition()));

                variant.setCodingSequencePosition(
                        getCodingSequencePosition(locatedVariant, transcriptMapsExons, variant.getTranscriptPosition(), proteinRange));

                String transcriptDNASequence = transcriptMapsExons.getTranscriptMaps().getTranscript().getSeq();
                String originalDNASeq = transcriptDNASequence.substring(proteinRange.getMinimum() - 1, proteinRange.getMaximum());

                DNASequence originalDNASequence = new DNASequence(originalDNASeq);
                Sequence<NucleotideCompound> originalRNASequence = dna2RnaTranslator.createSequence(originalDNASequence);
                Sequence<AminoAcidCompound> originalProteinSequence = rna2AminoAcidTranslator.createSequence(originalRNASequence);

                variant.setFrameshift(variant.getReferenceAllele().length() % 3 != 0);
                variant.setInframe(
                        isInframe(originalDNASequence, variant.getCodingSequencePosition(), locatedVariant) && !variant.getFrameshift());

                Integer aaStart = getAminoAcidStart(variant.getVariantType().getId(), variant.getCodingSequencePosition(),
                        variant.getFrameshift(), variant.getInframe(), variant.getReferenceAllele(),
                        transcriptMapsExons.getTranscriptMaps().getStrand());

                variant.setAminoAcidStart(aaStart);

                if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    variant.setAminoAcidEnd(aaStart + Math.max(1, (locatedVariant.getRef().length() - 1) / 3));
                } else {
                    variant.setAminoAcidEnd(aaStart + Math.max(1, locatedVariant.getRef().length() / 3));
                }

                if (variant.getInframe() && locatedVariant.getRef().length() < 3) {
                    variant.setAminoAcidEnd(aaStart);
                }

                if (variant.getAminoAcidStart() > originalProteinSequence.getLength()) {
                    variant.setOriginalAminoAcid("*");
                } else {

                    if (variant.getAminoAcidEnd() > originalProteinSequence.getLength()) {
                        AminoAcidCompound retvalo = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                        variant.setOriginalAminoAcid(String.format("%s*", retvalo.getBase()));
                    } else {
                        Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                variant.getAminoAcidEnd());
                        variant.setOriginalAminoAcid(retvalo.getSequenceAsString());
                    }

                }

                Pair<String, String> dnaSequenceParts = getDNASequenceParts(variant.getVariantType().getId(),
                        transcriptMapsExons.getTranscriptMaps().getStrand(), transcriptDNASequence, proteinRange,
                        variant.getCodingSequencePosition(), variant.getReferenceAllele(), variant.getAlternateAllele());

                String finalDNASeq = String.format("%s%s%s", dnaSequenceParts.getLeft(), variant.getAlternateAllele(),
                        dnaSequenceParts.getRight());
                if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    String altAllele = new DNASequence(variant.getAlternateAllele()).getReverseComplement().getSequenceAsString();
                    finalDNASeq = String.format("%s%s%s", dnaSequenceParts.getLeft(), altAllele, dnaSequenceParts.getRight());
                }

                DNASequence finalDNASequence = new DNASequence(finalDNASeq);
                Sequence<NucleotideCompound> finalRNASequence = dna2RnaTranslator.createSequence(finalDNASequence);
                Sequence<AminoAcidCompound> finalProteinSequence = rna2AminoAcidTranslator.createSequence(finalRNASequence);

                if (variant.getFrameshift()) {

                    if (!"*".equals(variant.getOriginalAminoAcid())) {
                        if (originalProteinSequence.getLength() > variant.getAminoAcidStart() + 4) {
                            Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    variant.getAminoAcidStart() + 4);
                            variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));
                        } else if (originalProteinSequence.getLength() < variant.getAminoAcidEnd()) {
                            Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    originalProteinSequence.getLength());
                            variant.setOriginalAminoAcid(String.format("%s*", retvalo.getSequenceAsString()));
                        } else {
                            Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    originalProteinSequence.getLength());
                            variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));
                        }
                    }

                    if (finalProteinSequence.getLength() > variant.getAminoAcidStart() + 4) {
                        Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                variant.getAminoAcidStart() + 4);
                        variant.setFinalAminoAcid(String.format("%s...", retvalf.getSequenceAsString()));
                    } else {
                        if (variant.getAminoAcidStart() < finalProteinSequence.getLength()) {
                            Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    finalProteinSequence.getLength());
                            variant.setFinalAminoAcid(String.format("%s...", retvalf.getSequenceAsString()));

                        } else if (variant.getAminoAcidStart() == finalProteinSequence.getLength()) {
                            Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    finalProteinSequence.getLength());
                            variant.setFinalAminoAcid(retvalf.getSequenceAsString());
                        } else {
                            variant.setFinalAminoAcid("*");
                        }
                    }

                    variant.setAminoAcidEnd(aaStart + Math.max(1, locatedVariant.getSeq().length() / 3));

                    if (refSeqCDS != null) {
                        String firstOriginalAACompoundValue = AminoAcidCompoundSet.getAminoAcidCompoundSet()
                                .getCompoundForString(
                                        Arrays.asList(variant.getOriginalAminoAcid().split("(?!^)")).stream().findFirst().get())
                                .getLongName();

                        variant.setHgvsProtein(String.format("%s:p.%s%dfs", refSeqCDS.getProteinId(), firstOriginalAACompoundValue,
                                variant.getAminoAcidStart()));
                    }

                    if (variant.getOriginalAminoAcid().equals(variant.getFinalAminoAcid())) {
                        variant.setVariantEffect(
                                allVariantEffects.stream().filter(a -> a.getId().equals("synonymous indel")).findFirst().get());
                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                    } else {
                        variant.setVariantEffect(
                                allVariantEffects.stream().filter(a -> a.getId().equals("frameshifting indel")).findFirst().get());
                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                    }

                    variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                            variant.getVariantType().getId(), variant.getCodingSequencePosition(), locatedVariant.getRef(),
                            locatedVariant.getSeq(), null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

                    return variant;

                }

                if (variant.getAminoAcidStart() == originalProteinSequence.getLength()) {
                    String firstOriginalAACompoundValue = AminoAcidCompoundSet.getAminoAcidCompoundSet()
                            .getCompoundForString(Arrays.asList(variant.getOriginalAminoAcid().split("(?!^)")).stream().findFirst().get())
                            .getLongName();

                    variant.setOriginalAminoAcid(String.format("%s*", firstOriginalAACompoundValue));
                }

                if ((variant.getAminoAcidEnd() - variant.getAminoAcidStart()) >= 1) {

                    List<AminoAcidCompound> aaCompounds = new LinkedList<>();

                    int tmpStart = variant.getAminoAcidStart() - 1;
                    AminoAcidCompound tmpOriginalAACompound = originalProteinSequence.getCompoundAt(tmpStart);
                    AminoAcidCompound tmpFinalAACompound = finalProteinSequence.getCompoundAt(tmpStart);
                    int count = 0;
                    while (tmpOriginalAACompound.getBase().equals(tmpFinalAACompound.getBase())) {

                        ++count;
                        if (count == 5) {
                            break;
                        }

                        ++tmpStart;

                        tmpOriginalAACompound = originalProteinSequence.getCompoundAt(tmpStart);
                        tmpFinalAACompound = finalProteinSequence.getCompoundAt(tmpStart);

                        if (finalProteinSequence.getLength() == tmpStart
                                || !tmpOriginalAACompound.getBase().equals(tmpFinalAACompound.getBase())) {
                            aaCompounds.add(tmpFinalAACompound);
                            break;
                        }

                    }

                    StringBuilder shortNames = new StringBuilder();
                    aaCompounds.forEach(a -> shortNames.append(a.getBase()));
                    variant.setFinalAminoAcid(shortNames.toString());

                }

                StringBuilder shortNames = new StringBuilder();
                StringBuilder longNames = new StringBuilder();

                if (refSeqCDS != null) {

                    List<AminoAcidCompound> aaCompounds = new LinkedList<>();

                    int tmpStart = variant.getAminoAcidStart() - 1;
                    AminoAcidCompound tmpOriginalAACompound = originalProteinSequence.getCompoundAt(tmpStart);
                    AminoAcidCompound tmpFinalAACompound = finalProteinSequence.getCompoundAt(tmpStart);
                    while (tmpOriginalAACompound.getBase().equals(tmpFinalAACompound.getBase())) {

                        ++tmpStart;
                        tmpOriginalAACompound = originalProteinSequence.getCompoundAt(tmpStart);
                        tmpFinalAACompound = finalProteinSequence.getCompoundAt(tmpStart);
                        if (finalProteinSequence.getLength() == tmpStart
                                || !tmpOriginalAACompound.getBase().equals(tmpFinalAACompound.getBase())) {
                            aaCompounds.add(tmpFinalAACompound);
                            break;
                        }

                    }

                    String firstOriginalAACompoundValue = AminoAcidCompoundSet.getAminoAcidCompoundSet()
                            .getCompoundForString(Arrays.asList(variant.getOriginalAminoAcid().split("(?!^)")).stream().findFirst().get())
                            .getLongName();

                    AminoAcidCompound nextOriginalAACompound = originalProteinSequence
                            .getCompoundAt(variant.getAminoAcidStart() + (locatedVariant.getRef().length() / 3));

                    if (nextOriginalAACompound.getBase().equals(tmpFinalAACompound.getBase())) {

                        if ((variant.getAminoAcidStart().equals(variant.getAminoAcidEnd() - 1))) {
                            variant.setHgvsProtein(String.format("%s:p.%s%ddel", refSeqCDS.getProteinId(), firstOriginalAACompoundValue,
                                    variant.getAminoAcidStart()));
                        } else {
                            variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddel", refSeqCDS.getProteinId(),
                                    firstOriginalAACompoundValue, variant.getAminoAcidStart(),
                                    originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd() - 1).getLongName(),
                                    variant.getAminoAcidEnd() - 1));
                        }

                    } else {

                        aaCompounds.forEach(a -> shortNames.append(a.getBase()));

                        aaCompounds.forEach(a -> longNames.append(a.getLongName()));

                        if ("*".equals(variant.getFinalAminoAcid())) {

                            variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddelins%s", refSeqCDS.getProteinId(),
                                    firstOriginalAACompoundValue, variant.getAminoAcidStart(),
                                    originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(),
                                    variant.getAminoAcidEnd(), variant.getFinalAminoAcid()));

                        } else if ((variant.getAminoAcidEnd() - variant.getAminoAcidStart()) == 1) {

                            variant.setHgvsProtein(
                                    String.format("%s:p.%s%ddel", refSeqCDS.getProteinId(), tmpOriginalAACompound.getLongName(), tmpStart));

                        } else if ("*".equals(variant.getFinalAminoAcid())) {
                            variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddelins%s", refSeqCDS.getProteinId(),
                                    firstOriginalAACompoundValue, variant.getAminoAcidStart(),
                                    originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(),
                                    variant.getAminoAcidEnd(), variant.getFinalAminoAcid()));

                        } else {
                            variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddelins%s", refSeqCDS.getProteinId(),
                                    firstOriginalAACompoundValue, variant.getAminoAcidStart(),
                                    originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(),
                                    variant.getAminoAcidEnd(), longNames.toString()));
                        }

                    }
                }

                if (variant.getOriginalAminoAcid().equals(variant.getFinalAminoAcid())) {
                    variant.setVariantEffect(
                            allVariantEffects.stream().filter(a -> a.getId().equals("synonymous indel")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                } else if (finalProteinSequence.getSequenceAsString().contains("*")) {
                    variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("nonsense indel")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                } else {
                    variant.setVariantEffect(
                            allVariantEffects.stream().filter(a -> a.getId().equals("non-frameshifting indel")).findFirst().get());
                    variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                }

            }

            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                    variant.getVariantType().getId(), variant.getCodingSequencePosition(), locatedVariant.getRef(), locatedVariant.getSeq(),
                    null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return variant;
    }

}
