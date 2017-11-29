package org.renci.canvas.binning.core.grch37;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.template.Sequence;
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
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.refseq.model.Variants_61_2PK;
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
        return "61";
    }

    public Set<Variants_61_2> annotateVariant(LocatedVariant locatedVariant, String refseqVersion, Integer genomeRefId,
            CANVASDAOBeanService daoBean) {
        logger.info(locatedVariant.toString());
        Set<Variants_61_2> variants = new HashSet<>();

        try {

            if ("snp".equals(locatedVariant.getVariantType().getId())) {
                // either intergenic, intron, or exonic

                List<TranscriptMaps> transcriptMapsList = daoBean.getTranscriptMapsDAO()
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

                        logger.info(tMap.toString());

                        List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRefId, refseqVersion, tMap.getTranscript().getId());

                        List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                .findByTranscriptMapsId(tMap.getId());

                        Variants_61_2 variant = null;

                        Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.stream()
                                .filter(a -> a.getContigRange().contains(locatedVariant.getPosition())).findAny();

                        if (optionalTranscriptMapsExons.isPresent()) {
                            // found in an exon
                            TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                            logger.debug(transcriptMapsExons.toString());
                            variant = createExonicVariant(locatedVariant, mapsList, transcriptMapsExonsList, transcriptMapsExons);
                        } else {
                            // if not found in an exon, but is within a transcript map contig range, must be intron
                            variant = createIntronicVariant(locatedVariant, mapsList, tMap, transcriptMapsExonsList);
                        }
                        variants.add(variant);

                    }

                } else {
                    // not found in any transcript contig range, must be intergenic
                    Variants_61_2 variant = createIntergenicVariant(locatedVariant);
                    variants.add(variant);
                }

            } else {
                // handling indels

                List<TranscriptMaps> transcriptMapsList = daoBean.getTranscriptMapsDAO()
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

                        logger.info(tMap.toString());

                        List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRefId, refseqVersion, tMap.getTranscript().getId());

                        List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                .findByTranscriptMapsId(tMap.getId());

                        Variants_61_2 variant = null;

                        // note (possible FIXME...) This only handles the 1st match. What if a deletion spans two exons?
                        Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.stream()
                                .filter(a -> a.getContigRange().contains(locatedVariant.getPosition())).findAny();

                        if (optionalTranscriptMapsExons.isPresent()) {

                            TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                            logger.debug(transcriptMapsExons.toString());

                            // FIXME - not at all sure about this set of conditionals-- it's not even clear from indentation what goes with
                            // what...
                            if (transcriptMapsExons.getContigRange().containsRange(locatedVariant.toRange())) {
                                variant = createExonicVariant(locatedVariant, mapsList, transcriptMapsExonsList, transcriptMapsExons);
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
                                logger.info(transcriptMapsExons.toString());

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
                            logger.info(tMap.toString());

                            List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                    .findByTranscriptMapsId(tMap.getId());

                            List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO().findByGenomeRefIdAndRefSeqVersionAndTranscriptId(
                                    genomeRefId, refseqVersion, tMap.getTranscript().getId());

                            Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.stream().filter(
                                    a -> a.getContigRange().contains(locatedVariant.getPosition() + locatedVariant.getRef().length() - 1))
                                    .findAny();

                            Variants_61_2 variant = null;

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
                                Variants_61_2 variant = null;
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
                        }

                    }

                }

            }

            for (Variants_61_2 variant : variants) {
                logger.info(variant.toString());
            }

        } catch (CANVASDAOException | BinningException e) {
            logger.error(e.getMessage(), e);
        }
        return variants;

    }

    public Variants_61_2 createIntronicVariant(LocatedVariant locatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps tMap,
            List<TranscriptMapsExons> transcriptMapsExonsList) throws BinningException {
        logger.debug(
                "ENTERING createIntronicVariant(LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>)");

        Variants_61_2PK variantKey = new Variants_61_2PK(locatedVariant.getId(), tMap.getGenomeRefSeq().getId(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getId(), tMap.getTranscript().getId(), null, null,
                mapsList.indexOf(tMap) + 1);

        Variants_61_2 variant = new Variants_61_2(variantKey);
        variant.setGenomeRefSeq(tMap.getGenomeRefSeq());
        variant.setNumberOfTranscriptMaps(mapsList.size());
        variant.setStrand(tMap.getStrand());
        variant.setLocatedVariant(locatedVariant);
        variant.setVariantType(locatedVariant.getVariantType());
        variant.setReferenceAllele(locatedVariant.getRef());
        variant.setAlternateAllele(locatedVariant.getSeq() != null ? locatedVariant.getSeq() : "");
        try {

            variant.setLocationType(allLocationTypes.stream().filter(a -> a.getId().equals("intron")).findFirst().get());
            variant.getId().setLocationType(variant.getLocationType().getId());

            variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("intron")).findFirst().get());
            variant.getId().setVariantEffect(variant.getVariantEffect().getId());

            List<RefSeqGene> refSeqGeneList = daoBean.getRefSeqGeneDAO().findByRefSeqVersionAndTranscriptId(getRefSeqVersion(),
                    tMap.getTranscript().getId());

            if (CollectionUtils.isEmpty(refSeqGeneList)) {
                throw new BinningException(String.format("refseq gene not found: %s", tMap.getTranscript().getId()));
            }

            RefSeqGene refSeqGene = refSeqGeneList.get(0);
            variant.setRefSeqGene(refSeqGene.getName());

            Range<Integer> proteinRange = null;
            List<RefSeqCodingSequence> refSeqCodingSequenceList = daoBean.getRefSeqCodingSequenceDAO()
                    .findByRefSeqVersionAndTranscriptId(getRefSeqVersion(), tMap.getTranscript().getId());

            RefSeqCodingSequence refSeqCDS = refSeqCodingSequenceList.stream().findFirst().orElse(null);
            if (refSeqCDS != null) {
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                RegionGroupRegion rgr = rgrList.stream().findFirst().orElse(null);
                if (rgr != null) {
                    proteinRange = rgr.getId().getRegionRange();
                }
            }

            variant.setRefSeqGene(refSeqGene.getName());
            if ("-".equals(tMap.getStrand())) {
                transcriptMapsExonsList.sort((a, b) -> b.getContigStart().compareTo(a.getContigStart()));
            }
            ListIterator<TranscriptMapsExons> transcriptMapsExonsIter = transcriptMapsExonsList.listIterator();

            TranscriptMapsExons previous = null;
            while (transcriptMapsExonsIter.hasNext()) {
                TranscriptMapsExons current = transcriptMapsExonsIter.next();
                logger.debug(current.toString());
                if (previous != null && current != null) {
                    Range<Integer> intronRange = Range.between(previous.getContigEnd(), current.getContigStart());
                    Range<Integer> previousTranscriptRange = previous.getTranscriptRange();
                    Range<Integer> currentTranscriptRange = current.getTranscriptRange();

                    if (intronRange.contains(locatedVariant.getPosition())) {

                        Integer rightDistance = locatedVariant.getPosition() - previous.getContigEnd();
                        Integer leftDistance = locatedVariant.getPosition() - current.getContigStart();

                        if ("-".equals(tMap.getStrand())) {
                            leftDistance = -leftDistance;
                            rightDistance = -rightDistance;
                        }

                        if (Math.abs(leftDistance) < Math.abs(rightDistance)) {
                            variant.setIntronExonDistance(leftDistance);
                        } else {
                            variant.setIntronExonDistance(rightDistance);
                        }

                        if (proteinRange != null) {

                            if (Math.abs(variant.getIntronExonDistance()) <= 2) {
                                variant.setVariantEffect(
                                        allVariantEffects.stream().filter(a -> a.getId().equals("splice-site")).findFirst().get());
                                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                            }

                            switch (tMap.getStrand()) {
                                case "+":

                                    variant.setTranscriptPosition(current.getTranscriptStart() - proteinRange.getMinimum() + 1);
                                    if (variant.getIntronExonDistance() > 0) {
                                        variant.setTranscriptPosition(current.getTranscriptStart() - proteinRange.getMinimum());
                                    }

                                    if (Math.abs(variant.getIntronExonDistance()) <= 2
                                            && current.getTranscriptRange().getMinimum() < proteinRange.getMinimum()) {
                                        // really a utr5
                                        variant.setVariantEffect(allVariantEffects.stream()
                                                .filter(a -> a.getId().equals("splice-site-UTR-5")).findFirst().get());
                                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                                    }
                                    break;
                                case "-":
                                    variant.setTranscriptPosition(previous.getTranscriptEnd() - proteinRange.getMinimum() + 1);
                                    if (variant.getIntronExonDistance() < 0) {
                                        variant.setTranscriptPosition(currentTranscriptRange.getMinimum() - proteinRange.getMinimum() + 1);
                                    }
                                    if (Math.abs(variant.getIntronExonDistance()) <= 2
                                            && current.getTranscriptRange().getMaximum() > proteinRange.getMaximum()) {
                                        // really a utr3
                                        variant.setVariantEffect(allVariantEffects.stream()
                                                .filter(a -> a.getId().equals("splice-site-UTR-3")).findFirst().get());
                                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                                    }
                                    break;
                            }

                            variant.setHgvsCodingSequence(toHGVS(tMap.getTranscript().getId(), "c", variant.getVariantType().getId(),
                                    variant.getTranscriptPosition(), locatedVariant.getRef(), locatedVariant.getSeq(),
                                    variant.getIntronExonDistance(), "-".equals(tMap.getStrand())));
                        }

                        break;
                    }

                }
                previous = current;
            }

            if (Math.abs(variant.getIntronExonDistance()) <= 2 && proteinRange == null) {
                variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("splice-site-UTR")).findFirst().get());
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
            }

            List<AnnotationGeneExternalId> annotationGeneExternalIdsList = daoBean.getAnnotationGeneExternalIdDAO()
                    .findByExternalId(refSeqGene.getId());
            AnnotationGeneExternalId annotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getId().getNamespace())).findFirst().orElse(null);
            if (annotationGeneExternalIds == null) {
                throw new BinningException("gene not found");
            }
            variant.setGene(annotationGeneExternalIds.getGene());

            List<HGNCGene> hgncGeneList = daoBean.getHGNCGeneDAO()
                    .findByAnnotationGeneExternalIdsGeneIdsAndNamespace(variant.getGene().getId(), "HGNC");
            variant.setHgncGene(hgncGeneList.stream().map(a -> a.getSymbol()).findFirst().orElse("None"));

            variant.setHgvsGenomic(toHGVS(tMap.getGenomeRefSeq().getId(), "g", variant.getVariantType().getId(),
                    locatedVariant.getPosition(), locatedVariant.getRef(), locatedVariant.getSeq()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        logger.info(variant.toString());
        return variant;
    }

    public Variants_61_2 createIntergenicVariant(LocatedVariant locatedVariant) throws BinningException {
        logger.debug("ENTERING createIntergenicVariant(LocatedVariant)");

        LocationType intergenicLocationType = allLocationTypes.stream().filter(a -> a.getId().equals("intergenic")).findFirst().get();
        VariantEffect variantEffect = allVariantEffects.stream().filter(a -> a.getId().equals("intergenic")).findFirst().get();

        Variants_61_2PK variantKey = new Variants_61_2PK(locatedVariant.getId(), locatedVariant.getGenomeRefSeq().getId(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getId(), "", intergenicLocationType.getId(),
                variantEffect.getId(), 0);

        Variants_61_2 variant = new Variants_61_2(variantKey);

        variant.setVariantType(locatedVariant.getVariantType());
        variant.setGenomeRefSeq(locatedVariant.getGenomeRefSeq());
        variant.setLocatedVariant(locatedVariant);
        variant.setVariantEffect(variantEffect);
        variant.setReferenceAllele(locatedVariant.getRef());
        variant.setAlternateAllele(locatedVariant.getSeq() != null ? locatedVariant.getSeq() : "");
        variant.setLocationType(intergenicLocationType);
        variant.setHgvsGenomic(toHGVS(locatedVariant.getGenomeRefSeq().getId(), "g", locatedVariant.getVariantType().getId(),
                locatedVariant.getPosition(), locatedVariant.getRef(), locatedVariant.getSeq()));

        logger.info(variant.toString());
        return variant;
    }

    public Variants_61_2 createBorderCrossingVariant(LocatedVariant locatedVariant, TranscriptMaps tMap, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createBorderCrossingVariant(LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");
        Variants_61_2PK variantKey = new Variants_61_2PK(locatedVariant.getId(), tMap.getGenomeRefSeq().getId(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getId(), tMap.getTranscript().getId(), null, null,
                mapsList.indexOf(tMap) + 1);
        Variants_61_2 variant = new Variants_61_2(variantKey);

        variant.setVariantType(locatedVariant.getVariantType());
        variant.setGenomeRefSeq(tMap.getGenomeRefSeq());
        variant.setLocatedVariant(locatedVariant);
        variant.setNumberOfTranscriptMaps(mapsList.size());
        variant.setStrand(tMap.getStrand());
        variant.setReferenceAllele(locatedVariant.getRef());
        variant.setAlternateAllele(locatedVariant.getSeq() != null ? locatedVariant.getSeq() : "");

        try {

            List<RefSeqGene> refSeqGeneList = daoBean.getRefSeqGeneDAO().findByRefSeqVersionAndTranscriptId(getRefSeqVersion(),
                    tMap.getTranscript().getId());

            if (CollectionUtils.isEmpty(refSeqGeneList)) {
                throw new BinningException(String.format("refseq gene not found: %s", tMap.getTranscript().getId()));
            }

            RefSeqGene refSeqGene = refSeqGeneList.get(0);
            variant.setRefSeqGene(refSeqGene.getName());

            List<AnnotationGeneExternalId> annotationGeneExternalIdsList = daoBean.getAnnotationGeneExternalIdDAO()
                    .findByExternalId(refSeqGene.getId());
            AnnotationGeneExternalId annotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getId().getNamespace())).findFirst().orElse(null);
            if (annotationGeneExternalIds == null) {
                throw new BinningException("gene not found");
            }
            variant.setGene(annotationGeneExternalIds.getGene());

            List<HGNCGene> hgncGeneList = daoBean.getHGNCGeneDAO()
                    .findByAnnotationGeneExternalIdsGeneIdsAndNamespace(variant.getGene().getId(), "HGNC");
            variant.setHgncGene(hgncGeneList.stream().map(a -> a.getSymbol()).findFirst().orElse("None"));

            variant.setHgvsGenomic(toHGVS(tMap.getGenomeRefSeq().getId(), "g", variant.getVariantType().getId(),
                    locatedVariant.getPosition(), locatedVariant.getRef(), locatedVariant.getSeq()));

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
            }

            Range<Integer> proteinRange = null;
            List<RefSeqCodingSequence> refSeqCodingSequenceList = daoBean.getRefSeqCodingSequenceDAO()
                    .findByRefSeqVersionAndTranscriptId(getRefSeqVersion(), tMap.getTranscript().getId());

            RefSeqCodingSequence refSeqCDS = refSeqCodingSequenceList.stream().findFirst().orElse(null);
            if (refSeqCDS != null) {
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                RegionGroupRegion rgr = rgrList.stream().findFirst().orElse(null);
                if (rgr != null) {
                    proteinRange = rgr.getId().getRegionRange();
                }
            }

            variant.setLocationType(allLocationTypes.stream().filter(a -> a.getId().equals("intron/exon boundary")).findFirst().get());
            variant.getId().setLocationType(variant.getLocationType().getId());
            variant.setVariantEffect(
                    allVariantEffects.stream().filter(a -> a.getId().equals("noncoding boundary-crossing indel")).findFirst().get());
            variant.getId().setVariantEffect(variant.getVariantEffect().getId());

            if ("(LARGEDELETION)".equals(locatedVariant.getSeq())) {
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

        logger.info(variant.toString());
        return variant;
    }

    public Variants_61_2 createExonicVariant(LocatedVariant locatedVariant, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createExonicVariant(LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");

        Variants_61_2PK variantKey = new Variants_61_2PK(locatedVariant.getId(),
                transcriptMapsExons.getTranscriptMaps().getGenomeRefSeq().getId(), locatedVariant.getPosition(),
                locatedVariant.getVariantType().getId(), transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), null, null,
                mapsList.indexOf(transcriptMapsExons.getTranscriptMaps()) + 1);
        Variants_61_2 variant = new Variants_61_2(variantKey);

        variant.setVariantType(locatedVariant.getVariantType());
        variant.setGenomeRefSeq(transcriptMapsExons.getTranscriptMaps().getGenomeRefSeq());
        variant.setLocatedVariant(locatedVariant);
        variant.setNumberOfTranscriptMaps(mapsList.size());
        variant.setStrand(transcriptMapsExons.getTranscriptMaps().getStrand());
        variant.setReferenceAllele(locatedVariant.getRef());
        variant.setAlternateAllele(locatedVariant.getSeq() != null ? locatedVariant.getSeq() : "");

        try {

            List<RefSeqGene> refSeqGeneList = daoBean.getRefSeqGeneDAO().findByRefSeqVersionAndTranscriptId(getRefSeqVersion(),
                    transcriptMapsExons.getTranscriptMaps().getTranscript().getId());

            if (CollectionUtils.isEmpty(refSeqGeneList)) {
                throw new BinningException(
                        String.format("refseq gene not found: %s", transcriptMapsExons.getTranscriptMaps().getTranscript().getId()));
            }

            RefSeqGene refSeqGene = refSeqGeneList.get(0);
            variant.setRefSeqGene(refSeqGene.getName());

            List<AnnotationGeneExternalId> annotationGeneExternalIdsList = daoBean.getAnnotationGeneExternalIdDAO()
                    .findByExternalId(refSeqGene.getId());
            AnnotationGeneExternalId annotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getId().getNamespace())).findFirst().orElse(null);
            if (annotationGeneExternalIds == null) {
                throw new BinningException("gene not found");
            }
            variant.setGene(annotationGeneExternalIds.getGene());

            List<HGNCGene> hgncGeneList = daoBean.getHGNCGeneDAO()
                    .findByAnnotationGeneExternalIdsGeneIdsAndNamespace(variant.getGene().getId(), "HGNC");
            variant.setHgncGene(hgncGeneList.stream().map(a -> a.getSymbol()).findFirst().orElse("None"));

            if ("del".equals(locatedVariant.getVariantType().getId()) && locatedVariant.getRef().equals(locatedVariant.getSeq())) {
                variant.setAlternateAllele("");
            }

            variant.setTranscriptPosition(getTranscriptPosition(locatedVariant, transcriptMapsExons));

            List<Feature> featureList = daoBean.getFeatureDAO().findByRefSeqVersionAndTranscriptIdAndTranscriptPosition(getRefSeqVersion(),
                    transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), variant.getTranscriptPosition());

            featureList.sort((a, b) -> b.getRegionGroup().getId().compareTo(a.getRegionGroup().getId()));
            if (CollectionUtils.isNotEmpty(featureList)) {
                Feature feature = featureList.get(0);
                logger.debug(feature.toString());
                variant.setFeatureId(feature.getId());
            }

            variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList));

            if (variant.getIntronExonDistance() != null && variant.getIntronExonDistance().equals(variant.getTranscriptPosition())) {
                variant.setIntronExonDistance(null);
            }

            variant.setHgvsGenomic(toHGVS(transcriptMapsExons.getTranscriptMaps().getGenomeRefSeq().getId(), "g",
                    variant.getVariantType().getId(), locatedVariant.getPosition(), locatedVariant.getRef(), locatedVariant.getSeq()));

            variant.setHgvsTranscript(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "g",
                    variant.getVariantType().getId(), variant.getTranscriptPosition(), locatedVariant.getRef(), locatedVariant.getSeq(),
                    null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

            Range<Integer> locatedVariantRange = locatedVariant.toRange();
            Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
            Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();

            Range<Integer> proteinRange = null;

            List<RefSeqCodingSequence> refSeqCodingSequenceList = daoBean.getRefSeqCodingSequenceDAO().findByRefSeqVersionAndTranscriptId(
                    getRefSeqVersion(), transcriptMapsExons.getTranscriptMaps().getTranscript().getId());

            RefSeqCodingSequence refSeqCDS = refSeqCodingSequenceList.stream().findFirst().orElse(null);
            if (refSeqCDS != null) {
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                RegionGroupRegion rgr = rgrList.stream().findFirst().orElse(null);
                if (rgr != null) {
                    proteinRange = rgr.getId().getRegionRange();
                }
            }

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

            } else if ("exon".equals(variant.getLocationType().getId())) {

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

                    variant.setNonCanonicalExon(transcriptMapsExonsList.indexOf(transcriptMapsExons) + 1);
                    // variant.setNonCanonicalExon(getNonCanonicalExon(transcriptMapsExonsList, transcriptMapsExons, proteinRange));

                    variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList,
                            proteinRange, variant.getTranscriptPosition()));

                    variant.setCodingSequencePosition(
                            getCodingSequencePosition(locatedVariant, transcriptMapsExons, variant.getTranscriptPosition(), proteinRange));

                    String transcriptDNASequence = transcriptMapsExons.getTranscriptMaps().getTranscript().getSeq();
                    String originalDNASeq = transcriptDNASequence.substring(proteinRange.getMinimum() - 1, proteinRange.getMaximum());

                    DNASequence originalDNASequence = new DNASequence(originalDNASeq);
                    Sequence<NucleotideCompound> originalRNASequence = dna2RnaTranslator.createSequence(originalDNASequence);
                    Sequence<AminoAcidCompound> originalProteinSequence = rna2AminoAcidTranslator.createSequence(originalRNASequence);

                    if ("snp".equals(variant.getVariantType().getId())) {

                        Integer aaStart = getAminoAcidStart(variant.getVariantType().getId(), variant.getCodingSequencePosition(),
                                variant.getFrameshift(), variant.getInframe(), variant.getReferenceAllele(),
                                transcriptMapsExons.getTranscriptMaps().getStrand());

                        if (Double.valueOf(Math.ceil((variant.getTranscriptPosition()) / 3D))
                                .intValue() == (Double.valueOf(Math.ceil((proteinRange.getMaximum()) / 3D)).intValue())) {
                            --aaStart;
                        }

                        Integer aaEnd = aaStart + Math.max(1, locatedVariant.getSeq().length() / 3);

                        if ((variant.getCodingSequencePosition() / 3) == originalProteinSequence.getLength()) {
                            aaStart = originalProteinSequence.getLength();
                            aaEnd = originalProteinSequence.getLength();
                        }

                        variant.setAminoAcidStart(aaStart);
                        variant.setAminoAcidEnd(aaEnd);

                        AminoAcidCompound originalAACompound = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());

                        variant.setOriginalAminoAcid(originalAACompound.getBase());

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
                        AminoAcidCompound finalAACompound = finalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                        variant.setFinalAminoAcid(finalAACompound.getBase());

                        if (variant.getOriginalAminoAcid().equals(variant.getFinalAminoAcid())) {
                            variant.setVariantEffect(
                                    allVariantEffects.stream().filter(a -> a.getId().equals("synonymous")).findFirst().get());
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                        } else if (!variant.getOriginalAminoAcid().equals("*") && !variant.getFinalAminoAcid().equals("*")) {
                            variant.setVariantEffect(
                                    allVariantEffects.stream().filter(a -> a.getId().equals("missense")).findFirst().get());
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                        } else if (variant.getFinalAminoAcid().equals("*")) {
                            variant.setVariantEffect(
                                    allVariantEffects.stream().filter(a -> a.getId().equals("nonsense")).findFirst().get());
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                        } else if (variant.getOriginalAminoAcid().equals("*")) {
                            variant.setVariantEffect(
                                    allVariantEffects.stream().filter(a -> a.getId().equals("stoploss")).findFirst().get());
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                        }

                        if (refSeqCDS != null && !"synonymous".equals(variant.getVariantEffect().getId())) {
                            variant.setHgvsProtein(String.format("%s:p.%s%d%s", refSeqCDS.getProteinId(), originalAACompound.getLongName(),
                                    variant.getAminoAcidStart(), "*".equals(finalAACompound.getShortName()) ? finalAACompound.getShortName()
                                            : finalAACompound.getLongName()));
                        }

                    } else {

                        variant.setFrameshift(
                                (Math.abs(variant.getReferenceAllele().length() - variant.getAlternateAllele().length())) % 3 != 0);

                        if ("ins".equals(variant.getVariantType().getId())
                                && "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                            variant.setInframe((variant.getCodingSequencePosition() - 1) % 3 == 0 && !variant.getFrameshift());
                        } else {
                            variant.setInframe(variant.getCodingSequencePosition() % 3 == 0 && !variant.getFrameshift());
                        }

                        Integer aaStart = getAminoAcidStart(variant.getVariantType().getId(), variant.getCodingSequencePosition(),
                                variant.getFrameshift(), variant.getInframe(), variant.getReferenceAllele(),
                                transcriptMapsExons.getTranscriptMaps().getStrand());

                        if (Double.valueOf(Math.ceil((variant.getTranscriptPosition()) / 3D))
                                .intValue() == (Double.valueOf(Math.ceil((proteinRange.getMaximum()) / 3D)).intValue())) {
                            --aaStart;
                        }

                        if ("del".equals(variant.getVariantType().getId())
                                && variant.getCodingSequencePosition().equals(variant.getTranscriptPosition())) {
                            --aaStart;
                        }

                        if (aaStart > originalProteinSequence.getLength()) {
                            aaStart = originalProteinSequence.getLength();
                        }

                        variant.setAminoAcidStart(aaStart);

                        AminoAcidCompound originalAACompound = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());

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

                            if (variant.getAminoAcidStart() == originalProteinSequence.getLength()) {
                                variant.setOriginalAminoAcid(String.format("%s*", originalAACompound.getBase()));
                            } else if (originalProteinSequence.getLength() > variant.getAminoAcidStart() + 4) {
                                Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        variant.getAminoAcidStart() + 4);
                                variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));
                            } else {
                                Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        originalProteinSequence.getLength());
                                variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));
                            }

                            if ("del".equals(variant.getLocatedVariant().getVariantType().getId())) {
                                variant.setAminoAcidEnd(originalProteinSequence.getLength() + 2);
                            } else {
                                variant.setAminoAcidEnd(originalProteinSequence.getLength() + variant.getAlternateAllele().length());
                            }

                            if (finalProteinSequence.getLength() > variant.getAminoAcidStart() + 4) {
                                Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        variant.getAminoAcidStart() + 4);
                                variant.setFinalAminoAcid(String.format("%s...", retvalf.getSequenceAsString()));
                            } else if (variant.getAminoAcidEnd() > finalProteinSequence.getLength()) {
                                Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        finalProteinSequence.getLength());
                                variant.setFinalAminoAcid(String.format("%s", retvalf.getSequenceAsString()));
                            }

                            if (refSeqCDS != null) {
                                if (Arrays.asList("snp", "del").contains(variant.getVariantType().getId())) {
                                    variant.setHgvsProtein(String.format("%s:p.%s%dfs", refSeqCDS.getProteinId(),
                                            originalAACompound.getLongName(), variant.getAminoAcidStart()));
                                } else if ((variant.getCodingSequencePosition() + variant.getAlternateAllele().length()
                                        - 1) > originalDNASeq.length()) {
                                    variant.setHgvsProtein(String.format("%s:p.Ter%d%sext*?", refSeqCDS.getProteinId(),
                                            variant.getAminoAcidStart(), originalAACompound.getLongName()));
                                }
                            }

                            variant.setVariantEffect(
                                    allVariantEffects.stream().filter(a -> a.getId().equals("frameshifting indel")).findFirst().get());
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());

                        } else {

                            if (StringUtils.isEmpty(variant.getReferenceAllele())) {
                                variant.setAminoAcidEnd(variant.getAminoAcidStart() + 1);
                            } else {
                                if (variant.getInframe()) {
                                    variant.setAminoAcidEnd(
                                            (variant.getCodingSequencePosition() + variant.getReferenceAllele().length() - 1) / 3);
                                } else {
                                    variant.setAminoAcidEnd(
                                            (variant.getCodingSequencePosition() + variant.getReferenceAllele().length()) / 3);
                                }
                            }

                            if ("del".equals(variant.getVariantType().getId())) {
                                variant.setAminoAcidEnd(variant.getAminoAcidStart() + (variant.getReferenceAllele().length() / 3) - 1);
                                if (!variant.getInframe()) {
                                    variant.setAminoAcidEnd(variant.getAminoAcidEnd() + 1);
                                }
                            }

                            if (variant.getAminoAcidStart() == originalProteinSequence.getLength()) {
                                variant.setOriginalAminoAcid(String.format("%s*", originalAACompound.getBase()));
                            } else {
                                Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        variant.getAminoAcidEnd());
                                variant.setOriginalAminoAcid(retvalo.getSequenceAsString());
                            }

                            StringBuilder shortNames = new StringBuilder();
                            StringBuilder longNames = new StringBuilder();

                            if (refSeqCDS != null) {

                                if ((variant.getCodingSequencePosition() + variant.getReferenceAllele().length() - 1) > originalDNASeq
                                        .length()) {

                                    variant.setHgvsProtein(String.format("%s:p.Ter%d%sext*?", refSeqCDS.getProteinId(),
                                            variant.getAminoAcidStart(), originalAACompound.getLongName()));

                                } else if ("del".equals(variant.getVariantType().getId()) && !variant.getInframe()) {

                                    if (variant.getAminoAcidStart() == originalProteinSequence.getLength()) {

                                    } else {

                                        List<AminoAcidCompound> aaCompounds = new LinkedList<>();

                                        int tmpStart = variant.getAminoAcidStart() - 1;
                                        AminoAcidCompound tmpOriginalAACompound = originalProteinSequence.getCompoundAt(tmpStart);
                                        AminoAcidCompound tmpFinalAACompound = finalProteinSequence.getCompoundAt(tmpStart);
                                        while (tmpOriginalAACompound.getBase().equals(tmpFinalAACompound.getBase())) {

                                            ++tmpStart;
                                            tmpOriginalAACompound = originalProteinSequence.getCompoundAt(tmpStart);
                                            tmpFinalAACompound = finalProteinSequence.getCompoundAt(tmpStart);
                                            if (!tmpOriginalAACompound.getBase().equals(tmpFinalAACompound.getBase())) {
                                                aaCompounds.add(tmpFinalAACompound);
                                                break;
                                            }

                                        }

                                        aaCompounds.forEach(a -> shortNames.append(a.getBase()));
                                        variant.setFinalAminoAcid(shortNames.toString());

                                        aaCompounds.forEach(a -> longNames.append(a.getLongName()));

                                        // variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddel", refSeqCDS.getProteinId(),
                                        // originalAACompound.getLongName(), variant.getAminoAcidStart(),
                                        // originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(),
                                        // variant.getAminoAcidEnd()));

                                        variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddelins%s", refSeqCDS.getProteinId(),
                                                originalAACompound.getLongName(), variant.getAminoAcidStart(),
                                                originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(),
                                                variant.getAminoAcidEnd(), longNames.toString()));

                                    }

                                } else if (Arrays.asList("sub", "ins").contains(variant.getVariantType().getId())) {

                                    Integer aaEnd = variant.getAminoAcidEnd() + (variant.getAlternateAllele().length() / 3);
                                    if (aaEnd >= finalProteinSequence.getLength()) {
                                        Sequence<AminoAcidCompound> retvalf = finalProteinSequence
                                                .getSubSequence(variant.getAminoAcidStart(), finalProteinSequence.getLength());
                                        variant.setFinalAminoAcid(String.format("%s*", retvalf.getSequenceAsString()));
                                        retvalf.forEach(a -> longNames.append(a.getLongName()));
                                        variant.setHgvsProtein(String.format("%s:p.%s%d_*%ddelins%s*", refSeqCDS.getProteinId(),
                                                originalAACompound.getLongName(), variant.getAminoAcidStart(), variant.getAminoAcidEnd(),
                                                longNames.toString()));

                                    } else {

                                        Sequence<AminoAcidCompound> retvalf = finalProteinSequence
                                                .getSubSequence(variant.getAminoAcidStart(), aaEnd);
                                        variant.setFinalAminoAcid(retvalf.getSequenceAsString());
                                        retvalf.forEach(a -> longNames.append(a.getLongName()));
                                        variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddelins%s", refSeqCDS.getProteinId(),
                                                originalAACompound.getLongName(), variant.getAminoAcidStart(),
                                                originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(),
                                                variant.getAminoAcidEnd(), longNames.toString()));

                                    }

                                } else if (variant.getOriginalAminoAcid().length() > 1) {
                                    variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddel", refSeqCDS.getProteinId(),
                                            originalProteinSequence.getCompoundAt(variant.getAminoAcidStart()).getLongName(),
                                            variant.getAminoAcidStart(),
                                            originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(),
                                            variant.getAminoAcidEnd()));
                                } else {
                                    variant.setHgvsProtein(String.format("%s:p.%s%ddel", refSeqCDS.getProteinId(),
                                            originalProteinSequence.getCompoundAt(variant.getAminoAcidStart()).getLongName(),
                                            variant.getAminoAcidStart()));
                                }
                            }

                            // AminoAcidCompound finalAACompound =
                            // finalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                            // variant.setFinalAminoAcid(finalAACompound.getBase());

                            if (variant.getOriginalAminoAcid().equals(variant.getFinalAminoAcid())) {
                                variant.setVariantEffect(
                                        allVariantEffects.stream().filter(a -> a.getId().equals("synonymous indel")).findFirst().get());
                                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                            } else if (finalProteinSequence.getSequenceAsString().contains("*")) {
                                variant.setVariantEffect(
                                        allVariantEffects.stream().filter(a -> a.getId().equals("nonsense indel")).findFirst().get());
                                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                            } else if (variant.getOriginalAminoAcid().equals("*")) {
                                variant.setVariantEffect(
                                        allVariantEffects.stream().filter(a -> a.getId().equals("stoploss")).findFirst().get());
                                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                            } else {
                                variant.setVariantEffect(allVariantEffects.stream().filter(a -> a.getId().equals("non-frameshifting indel"))
                                        .findFirst().get());
                                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                            }
                        }

                        if (finalProteinSequence.getSequenceAsString().contains("*")) {
                            // variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("nonsense
                            // indel"));
                            // variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                        }

                    }

                    variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), "c",
                            variant.getVariantType().getId(), variant.getCodingSequencePosition(), locatedVariant.getRef(),
                            locatedVariant.getSeq(), null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        logger.info(variant.toString());
        return variant;
    }

}
