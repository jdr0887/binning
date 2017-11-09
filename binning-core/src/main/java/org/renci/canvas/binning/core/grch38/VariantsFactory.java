package org.renci.canvas.binning.core.grch38;

import java.util.*;
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

    public Set<Variants_80_4> annotateVariant(LocatedVariant locatedVariant, String refseqVersion, Integer genomeRefId, CANVASDAOBeanService daoBean) {

        logger.info(locatedVariant.toString());
        Set<Variants_80_4> variants = new HashSet<>();

        try {

            final List<TranscriptMaps> transcriptMapsList = daoBean.getTranscriptMapsDAO()
                    .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRefId,
                            refseqVersion, locatedVariant.getGenomeRefSeq().getId(), locatedVariant.getPosition());

            // FIXME - this still only finds transcripts where the variant at least starts or ends-- not variants that span the whole transcript...
            // FIXME - would be better to have a proper overlap query
            transcriptMapsList.addAll(daoBean.getTranscriptMapsDAO()
                    .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRefId,
                            refseqVersion, locatedVariant.getGenomeRefSeq().getId(), locatedVariant.getEndPosition()-1));

            // now we need to use distinct() to eliminate duplicates (e.g., variant starts and ends within transcript)
            List<TranscriptMaps> distinctTranscriptMapsList = transcriptMapsList.stream()
                    .map(a -> a.getTranscript().getId())
                    .distinct().map(a -> transcriptMapsList.parallelStream()
                            .filter(b -> b.getTranscript().getId().equals(a)).findAny().get())
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(distinctTranscriptMapsList)) {

                distinctTranscriptMapsList.sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                for (TranscriptMaps tMap : distinctTranscriptMapsList) {

                    logger.info(tMap.toString());

                    // I think this is only used to provide mapnum and nummaps
                    List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                            .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRefId, refseqVersion,
                                    tMap.getTranscript().getId());
                    mapsList.sort((a, b) -> b.getId().compareTo(a.getId()));

                    List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                            .findByTranscriptMapsId(tMap.getId());

                    long varEnd = locatedVariant.getEndPosition()-1;
                    boolean foundExonOverlap = false;
                    for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                        // FIXME - might be able to `break` depending on strandedness..
                        long exonMin = Math.min(exon.getContigStart(), exon.getContigEnd());
                        long exonMax = Math.max(exon.getContigStart(), exon.getContigEnd());
                        if (varEnd < exonMin) continue;
                        if (locatedVariant.getPosition() > exonMax) continue;
                        // we have overlap...
                        foundExonOverlap = true;
                        if (locatedVariant.getPosition() >= exonMin
                                && varEnd <= exonMax) {
                            // completely contained
                            // FIXME - have to shallow copy transcriptsMapsExonsList because something in there reorders it...
                            variants.add(createExonicVariant(locatedVariant, mapsList,
                                    new ArrayList<TranscriptMapsExons>(transcriptMapsExonsList), exon));
                        } else {
                            variants.add(createBorderCrossingVariant(locatedVariant, tMap, mapsList, transcriptMapsExonsList, exon));
                        }
                    }

                    if (!foundExonOverlap) {
                        variants.add(createIntronicVariant(locatedVariant, mapsList, tMap, transcriptMapsExonsList));
                    }
                }
            }

            if (CollectionUtils.isEmpty(variants)) {
                // not found in or across any transcript, must be intergenic
                Variants_80_4 variant = createIntergenicVariant(locatedVariant);
                variants.add(variant);
            }

            for (Variants_80_4 variant : variants) {
                logger.info(variant.toString());
            }

        } catch (CANVASDAOException | BinningException e) {
            logger.error(e.getMessage(), e);
        }
        return variants;

    }

    public Variants_80_4 createIntronicVariant(LocatedVariant locatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps tMap,
            List<TranscriptMapsExons> transcriptMapsExonsList) throws BinningException {
        logger.debug(
                "ENTERING createIntronicVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>)");

        Variants_80_4PK variantKey = new Variants_80_4PK(locatedVariant.getId(), tMap.getGenomeRefSeq().getId(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getId(), tMap.getTranscript().getId(), null, null,
                mapsList.indexOf(tMap) + 1);

        Variants_80_4 variant = new Variants_80_4(variantKey);
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

    public Variants_80_4 createIntergenicVariant(LocatedVariant locatedVariant) throws BinningException {
        logger.debug("ENTERING createIntergenicVariant(LocatedVariant)");

        LocationType intergenicLocationType = allLocationTypes.stream().filter(a -> a.getId().equals("intergenic")).findFirst().get();
        VariantEffect variantEffect = allVariantEffects.stream().filter(a -> a.getId().equals("intergenic")).findFirst().get();

        Variants_80_4PK variantKey = new Variants_80_4PK(locatedVariant.getId(), locatedVariant.getGenomeRefSeq().getId(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getId(), "", intergenicLocationType.getId(),
                variantEffect.getId(), 0);

        Variants_80_4 variant = new Variants_80_4(variantKey);

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

    public Variants_80_4 createBorderCrossingVariant(LocatedVariant locatedVariant, TranscriptMaps tMap, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createBorderCrossingVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");
        Variants_80_4PK variantKey = new Variants_80_4PK(locatedVariant.getId(), tMap.getGenomeRefSeq().getId(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getId(), tMap.getTranscript().getId(), null, null,
                mapsList.indexOf(tMap) + 1);

        Variants_80_4 variant = new Variants_80_4(variantKey);

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

            Range<Integer> exonOrProteinRange = transcriptMapsExonsTranscriptRange;

            if (proteinRange != null && proteinRange.isOverlappedBy(transcriptMapsExonsTranscriptRange)) {
                exonOrProteinRange = proteinRange;
            }

            switch (tMap.getStrand()) {
                case "+":
                    variant.setTranscriptPosition(
                            Math.max(1, (locatedVariantRange.getMinimum() - transcriptMapsExonsContigRange.getMinimum())
                                    + transcriptMapsExonsTranscriptRange.getMinimum()));
                    if (exonOrProteinRange.contains(variant.getTranscriptPosition())) {
                        // really a utr5
                        variant.setTranscriptPosition(null);
                        variant.setVariantEffect(allVariantEffects.stream()
                                .filter(a -> a.getId().equals("noncoding boundary-crossing indel")).findFirst().get());
                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                    }
                    break;
                case "-":
                    variant.setTranscriptPosition(Math.max(1, transcriptMapsExonsTranscriptRange.getMinimum()
                            - (locatedVariantRange.getMinimum() - transcriptMapsExonsContigRange.getMinimum())));
                    if (exonOrProteinRange.contains(variant.getTranscriptPosition())) {
                        // really a utr3
                        variant.setTranscriptPosition(null);
                        variant.setVariantEffect(allVariantEffects.stream()
                                .filter(a -> a.getId().equals("noncoding boundary-crossing indel")).findFirst().get());
                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        logger.info(variant.toString());
        return variant;
    }

    public Variants_80_4 createExonicVariant(LocatedVariant locatedVariant, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createExonicVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");

        Variants_80_4PK variantKey = new Variants_80_4PK(locatedVariant.getId(),
                transcriptMapsExons.getTranscriptMaps().getGenomeRefSeq().getId(), locatedVariant.getPosition(),
                locatedVariant.getVariantType().getId(), transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), null, null,
                mapsList.indexOf(transcriptMapsExons.getTranscriptMaps()) + 1);
        Variants_80_4 variant = new Variants_80_4(variantKey);

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

                    if (variant.getReferenceAllele().length() == variant.getAlternateAllele().length()
                            && variant.getReferenceAllele().length() == 1) {

                        Integer aaStart = getAminoAcidStart(variant.getVariantType().getId(), variant.getCodingSequencePosition(),
                                variant.getFrameshift(), variant.getInframe(), variant.getReferenceAllele(),
                                transcriptMapsExons.getTranscriptMaps().getStrand());

                        if (Double.valueOf(Math.ceil((variant.getTranscriptPosition()) / 3D))
                                .intValue() == (Double.valueOf(Math.ceil((proteinRange.getMaximum()) / 3D)).intValue())) {
                            --aaStart;
                        }

                        Integer aaEnd = aaStart + (locatedVariant.getEndPosition() - locatedVariant.getPosition());

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

                        if (refSeqCDS != null && "snp".equals(variant.getVariantType().getId())
                                && !"synonymous".equals(variant.getVariantEffect().getId())) {
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

                            if (originalProteinSequence.getLength() > variant.getAminoAcidStart() + 4) {
                                Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        variant.getAminoAcidStart() + 4);
                                variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));
                            } else {
                                Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        originalProteinSequence.getLength());
                                variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));
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
                                }
                            }

                            if ("del".equals(variant.getLocatedVariant().getVariantType().getId())) {
                                variant.setAminoAcidEnd(originalProteinSequence.getLength() + 2);
                            } else {
                                variant.setAminoAcidEnd(originalProteinSequence.getLength() + variant.getAlternateAllele().length());
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

                            variant.setVariantEffect(
                                    allVariantEffects.stream().filter(a -> a.getId().equals("non-frameshifting indel")).findFirst().get());
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());

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
