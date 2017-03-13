package org.renci.canvas.binning.core.grch38;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.sequence.transcription.DNAToRNATranslator;
import org.biojava.nbio.core.sequence.transcription.RNAToAminoAcidTranslator;
import org.biojava.nbio.core.sequence.transcription.TranscriptionEngine;
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
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.refseq.model.Variants_80_4PK;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariantsFactory {

    private static final Logger logger = LoggerFactory.getLogger(VariantsFactory.class);

    public static String toHGVS(String accession, String accessionType, String type, Integer position, String ref, String alt) {
        return toHGVS(accession, accessionType, type, position, ref, alt, null, Boolean.FALSE);
    }

    public static String toHGVS(String accession, String accessionType, String type, Integer position, String ref, String alt,
            Integer intronExonDistance) {
        return toHGVS(accession, accessionType, type, position, ref, alt, intronExonDistance, Boolean.FALSE);
    }

    public static String toHGVS(String accession, String accessionType, String type, Integer position, String ref, String alt,
            Integer intronExonDistance, Boolean useComplement) {
        logger.debug("ENTERING toHGVS(String, String, String, Integer, String, String, Integer)");
        String ret = "";

        if (useComplement) {
            try {
                ref = new DNASequence(ref).getReverseComplement().getSequenceAsString();
                alt = new DNASequence(alt).getReverseComplement().getSequenceAsString();
            } catch (CompoundNotFoundException e) {
                e.printStackTrace();
            }
        }
        switch (type) {
            case "snp":
                if (intronExonDistance != null) {
                    ret = String.format("%s:%s.%d%s%s>%s", accession, accessionType, position, String.format("%+d", intronExonDistance),
                            ref, alt);
                } else {
                    ret = String.format("%s:%s.%d%s>%s", accession, accessionType, position, ref, alt);
                }
                break;
            case "sub":
                if (useComplement) {
                    ret = String.format("%s:%s.%s_%ddelins%s", accession, accessionType, Integer.valueOf(position - ref.length() + 1),
                            intronExonDistance, position, alt);
                } else {
                    ret = String.format("%s:%s.%s_%ddelins%s", accession, accessionType, position,
                            Integer.valueOf(position + ref.length() - 1), alt);
                }
                break;
            case "del":
                Integer start = null;
                Integer end = null;
                if (useComplement) {
                    end = position;
                    start = position - ref.length() + 1;
                } else {
                    end = position + ref.length() - 1;
                    start = position;
                }

                if (intronExonDistance == null) {
                    if (start.equals(end)) {
                        ret = String.format("%s:%s.%ddel%s", accession, accessionType, position, ref);
                    } else {
                        ret = String.format("%s:%s.%d_%ddel%s", accession, accessionType, start, end, ref);
                    }
                } else {
                    ret = String.format("%s:%s.%d%+d_%ddel%s", accession, accessionType, start, intronExonDistance, end, ref);
                }
                break;
            case "ins":
                if (useComplement) {
                    ret = String.format("%s:%s.%d_%dins%s", accession, accessionType, Integer.valueOf(position - 1), position, alt);
                } else {
                    ret = String.format("%s:%s.%d_%dins%s", accession, accessionType, position, Integer.valueOf(position + 1), alt);
                }
                break;
        }
        return ret;
    }

    public static Variants_80_4 createIntronicVariant(CANVASDAOBeanService daoBean, String refseqVersion, LocatedVariant locatedVariant,
            List<TranscriptMaps> mapsList, TranscriptMaps tMap, List<TranscriptMapsExons> transcriptMapsExonsList) throws BinningException {
        logger.debug(
                "ENTERING createIntronicVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>)");

        Variants_80_4PK variantKey = new Variants_80_4PK(locatedVariant.getId(), tMap.getGenomeRefSeq().getVerAccession(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getName(), tMap.getTranscript().getVersionId(), null, null,
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

            variant.setLocationType(daoBean.getLocationTypeDAO().findById("intron"));
            variant.getKey().setLocationType(variant.getLocationType().getName());
            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("intron"));
            variant.getKey().setVariantEffect(variant.getVariantEffect().getName());

            List<RefSeqGene> refSeqGeneList = daoBean.getRefSeqGeneDAO().findByRefSeqVersionAndTranscriptId(refseqVersion,
                    tMap.getTranscript().getVersionId());
            RefSeqGene refSeqGene = null;
            if (CollectionUtils.isNotEmpty(refSeqGeneList)) {
                refSeqGene = refSeqGeneList.get(0);
            }

            Range<Integer> proteinRange = null;
            List<RefSeqCodingSequence> refSeqCodingSequenceList = daoBean.getRefSeqCodingSequenceDAO()
                    .findByRefSeqVersionAndTranscriptId(refseqVersion, tMap.getTranscript().getVersionId());

            RefSeqCodingSequence refSeqCDS = null;
            if (CollectionUtils.isNotEmpty(refSeqCodingSequenceList)) {
                refSeqCDS = refSeqCodingSequenceList.get(0);
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                if (CollectionUtils.isNotEmpty(rgrList)) {
                    RegionGroupRegion rgr = rgrList.get(0);
                    proteinRange = Range.between(rgr.getKey().getRegionStart(), rgr.getKey().getRegionEnd());
                }
            }

            Range<Integer> locatedVariantRange = locatedVariant.toRange();

            variant.setRefSeqGene(refSeqGene.getName());
            if ("-".equals(tMap.getStrand())) {
                transcriptMapsExonsList.sort((a, b) -> b.getContigStart().compareTo(a.getContigStart()));
            }
            ListIterator<TranscriptMapsExons> transcriptMapsExonsIter = transcriptMapsExonsList.listIterator();

            TranscriptMapsExons previous = null;
            while (transcriptMapsExonsIter.hasNext()) {
                TranscriptMapsExons current = transcriptMapsExonsIter.next();
                logger.info(current.toString());
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
                                variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("splice-site"));
                                variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
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
                                        variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("splice-site-UTR-5"));
                                        variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                                    }
                                    break;
                                case "-":
                                    variant.setTranscriptPosition(previous.getTranscriptEnd() - proteinRange.getMinimum() + 1);
                                    if (variant.getIntronExonDistance() < 0) {
                                        variant.setTranscriptPosition(current.getTranscriptEnd() - proteinRange.getMinimum());
                                    }
                                    if (Math.abs(variant.getIntronExonDistance()) <= 2
                                            && current.getTranscriptRange().getMaximum() > proteinRange.getMaximum()) {
                                        // really a utr3
                                        variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("splice-site-UTR-3"));
                                        variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                                    }
                                    break;
                            }

                            variant.setHgvsCodingSequence(toHGVS(tMap.getTranscript().getVersionId(), "c",
                                    variant.getVariantType().getName(), variant.getTranscriptPosition(), locatedVariant.getRef(),
                                    locatedVariant.getSeq(), variant.getIntronExonDistance(), "-".equals(tMap.getStrand())));
                        }

                        break;
                    }

                }
                previous = current;
            }

            if (Math.abs(variant.getIntronExonDistance()) <= 2 && proteinRange == null) {
                variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("splice-site-UTR"));
                variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
            }

            List<AnnotationGeneExternalId> annotationGeneExternalIdsList = daoBean.getAnnotationGeneExternalIdDAO()
                    .findByExternalId(refSeqGene.getId());
            Optional<AnnotationGeneExternalId> optionalAnnotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getKey().getNamespace())).findFirst();

            if (!optionalAnnotationGeneExternalIds.isPresent()) {
                throw new BinningException("gene not found");
            }
            variant.setGene(optionalAnnotationGeneExternalIds.get().getGene());

            List<HGNCGene> hgncGeneList = daoBean.getHGNCGeneDAO()
                    .findByAnnotationGeneExternalIdsGeneIdsAndNamespace(variant.getGene().getId(), "HGNC");
            String hgncGeneValue = "None";
            if (CollectionUtils.isNotEmpty(hgncGeneList)) {
                HGNCGene hgncGene = hgncGeneList.get(0);
                hgncGeneValue = hgncGene.getSymbol();
            }
            variant.setHgncGene(hgncGeneValue);

            variant.setHgvsGenomic(toHGVS(tMap.getGenomeRefSeq().getVerAccession(), "g", variant.getVariantType().getName(),
                    locatedVariant.getPosition(), locatedVariant.getRef(), locatedVariant.getSeq()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return variant;
    }

    public static Variants_80_4 createBorderCrossingVariant(CANVASDAOBeanService daoBean, String refseqVersion,
            LocatedVariant locatedVariant, TranscriptMaps tMap, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createBorderCrossingVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");
        Variants_80_4PK variantKey = new Variants_80_4PK(locatedVariant.getId(), tMap.getGenomeRefSeq().getVerAccession(),
                locatedVariant.getPosition(), locatedVariant.getVariantType().getName(), tMap.getTranscript().getVersionId(), null, null,
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
            List<RefSeqGene> refSeqGeneList = daoBean.getRefSeqGeneDAO().findByRefSeqVersionAndTranscriptId(refseqVersion,
                    tMap.getTranscript().getVersionId());
            RefSeqGene refSeqGene = null;
            if (CollectionUtils.isNotEmpty(refSeqGeneList)) {
                refSeqGene = refSeqGeneList.get(0);
            }
            variant.setRefSeqGene(refSeqGene.getName());

            List<AnnotationGeneExternalId> annotationGeneExternalIdsList = daoBean.getAnnotationGeneExternalIdDAO()
                    .findByExternalId(refSeqGene.getId());
            Optional<AnnotationGeneExternalId> optionalAnnotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getKey().getNamespace())).findFirst();

            if (!optionalAnnotationGeneExternalIds.isPresent()) {
                throw new BinningException("gene not found");
            }
            variant.setGene(optionalAnnotationGeneExternalIds.get().getGene());

            List<HGNCGene> hgncGeneList = daoBean.getHGNCGeneDAO()
                    .findByAnnotationGeneExternalIdsGeneIdsAndNamespace(variant.getGene().getId(), "HGNC");
            String hgncGeneValue = "None";
            if (CollectionUtils.isNotEmpty(hgncGeneList)) {
                HGNCGene hgncGene = hgncGeneList.get(0);
                hgncGeneValue = hgncGene.getSymbol();
            }
            variant.setHgncGene(hgncGeneValue);
            variant.setHgvsGenomic(toHGVS(tMap.getGenomeRefSeq().getVerAccession(), "g", variant.getVariantType().getName(),
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
                    .findByRefSeqVersionAndTranscriptId(refseqVersion, tMap.getTranscript().getVersionId());

            RefSeqCodingSequence refSeqCDS = null;
            if (CollectionUtils.isNotEmpty(refSeqCodingSequenceList)) {
                refSeqCDS = refSeqCodingSequenceList.get(0);
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                if (CollectionUtils.isNotEmpty(rgrList)) {
                    RegionGroupRegion rgr = rgrList.get(0);
                    proteinRange = Range.between(rgr.getKey().getRegionStart(), rgr.getKey().getRegionEnd());
                }
            }

            LocationType locationType = daoBean.getLocationTypeDAO().findById("intron/exon boundary");
            variant.setLocationType(locationType);
            variant.getKey().setLocationType(locationType.getName());

            if ("(LARGEDELETION)".equals(locatedVariant.getSeq())) {
                variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("noncoding boundary-crossing indel"));
                variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
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
                        variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("noncoding boundary-crossing indel"));
                        variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                    }
                    break;
                case "-":
                    variant.setTranscriptPosition(Math.max(1, transcriptMapsExonsTranscriptRange.getMinimum()
                            - (locatedVariantRange.getMinimum() - transcriptMapsExonsContigRange.getMinimum())));
                    if (exonOrProteinRange.contains(variant.getTranscriptPosition())) {
                        // really a utr3
                        variant.setTranscriptPosition(null);
                        variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("noncoding boundary-crossing indel"));
                        variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return variant;
    }

    public static Variants_80_4 createExonicVariant(CANVASDAOBeanService daoBean, String refseqVersion, LocatedVariant locatedVariant,
            List<TranscriptMaps> mapsList, List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons)
            throws BinningException {
        logger.debug(
                "ENTERING createExonicVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");

        Variants_80_4PK variantKey = new Variants_80_4PK(locatedVariant.getId(),
                transcriptMapsExons.getTranscriptMaps().getGenomeRefSeq().getVerAccession(), locatedVariant.getPosition(),
                locatedVariant.getVariantType().getName(), transcriptMapsExons.getTranscriptMaps().getTranscript().getVersionId(), null,
                null, mapsList.indexOf(transcriptMapsExons.getTranscriptMaps()) + 1);
        Variants_80_4 variant = new Variants_80_4(variantKey);

        variant.setVariantType(locatedVariant.getVariantType());
        variant.setGenomeRefSeq(transcriptMapsExons.getTranscriptMaps().getGenomeRefSeq());
        variant.setLocatedVariant(locatedVariant);
        variant.setNumberOfTranscriptMaps(mapsList.size());
        variant.setStrand(transcriptMapsExons.getTranscriptMaps().getStrand());
        variant.setReferenceAllele(locatedVariant.getRef());
        variant.setAlternateAllele(locatedVariant.getSeq() != null ? locatedVariant.getSeq() : "");

        try {
            List<RefSeqGene> refSeqGeneList = daoBean.getRefSeqGeneDAO().findByRefSeqVersionAndTranscriptId(refseqVersion,
                    transcriptMapsExons.getTranscriptMaps().getTranscript().getVersionId());
            RefSeqGene refSeqGene = null;
            if (CollectionUtils.isNotEmpty(refSeqGeneList)) {
                refSeqGene = refSeqGeneList.get(0);
            }
            variant.setRefSeqGene(refSeqGene.getName());

            List<AnnotationGeneExternalId> annotationGeneExternalIdsList = daoBean.getAnnotationGeneExternalIdDAO()
                    .findByExternalId(refSeqGene.getId());
            Optional<AnnotationGeneExternalId> optionalAnnotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getKey().getNamespace())).findFirst();

            if (!optionalAnnotationGeneExternalIds.isPresent()) {
                throw new BinningException("gene not found");
            }
            variant.setGene(optionalAnnotationGeneExternalIds.get().getGene());

            List<HGNCGene> hgncGeneList = daoBean.getHGNCGeneDAO()
                    .findByAnnotationGeneExternalIdsGeneIdsAndNamespace(variant.getGene().getId(), "HGNC");
            String hgncGeneValue = "None";
            if (CollectionUtils.isNotEmpty(hgncGeneList)) {
                HGNCGene hgncGene = hgncGeneList.get(0);
                hgncGeneValue = hgncGene.getSymbol();
            }
            variant.setHgncGene(hgncGeneValue);

            if ("del".equals(locatedVariant.getVariantType().getName()) && locatedVariant.getRef().equals(locatedVariant.getSeq())) {
                variant.setAlternateAllele("");
            }

            Integer intronExonDistance = getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList);
            logger.debug("intronExonDistance: {}", intronExonDistance);
            variant.setIntronExonDistance(intronExonDistance);

            Integer transcriptPosition = getTranscriptPosition(locatedVariant, transcriptMapsExons);
            logger.debug("transcriptPosition: {}", transcriptPosition);
            variant.setTranscriptPosition(transcriptPosition);

            Integer featureId = getFeatureId(daoBean, refseqVersion, transcriptMapsExons.getTranscriptMaps(),
                    variant.getTranscriptPosition());
            logger.debug("featureId: {}", featureId);
            variant.setFeatureId(featureId);

            if (variant.getIntronExonDistance() != null && variant.getIntronExonDistance().equals(variant.getTranscriptPosition())) {
                variant.setIntronExonDistance(null);
            }

            variant.setHgvsGenomic(toHGVS(transcriptMapsExons.getTranscriptMaps().getGenomeRefSeq().getVerAccession(), "g",
                    variant.getVariantType().getName(), locatedVariant.getPosition(), locatedVariant.getRef(), locatedVariant.getSeq()));

            variant.setHgvsTranscript(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getVersionId(), "g",
                    variant.getVariantType().getName(), variant.getTranscriptPosition(), locatedVariant.getRef(), locatedVariant.getSeq(),
                    null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

            Range<Integer> locatedVariantRange = locatedVariant.toRange();
            Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
            Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();

            Range<Integer> proteinRange = null;
            List<RefSeqCodingSequence> refSeqCodingSequenceList = daoBean.getRefSeqCodingSequenceDAO().findByRefSeqVersionAndTranscriptId(
                    refseqVersion, transcriptMapsExons.getTranscriptMaps().getTranscript().getVersionId());

            RefSeqCodingSequence refSeqCDS = null;
            if (CollectionUtils.isNotEmpty(refSeqCodingSequenceList)) {
                refSeqCDS = refSeqCodingSequenceList.get(0);
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                if (CollectionUtils.isNotEmpty(rgrList)) {
                    RegionGroupRegion rgr = rgrList.get(0);
                    proteinRange = Range.between(rgr.getKey().getRegionStart(), rgr.getKey().getRegionEnd());
                }
            }

            LocationType locationType = getLocationType(daoBean, locatedVariantRange, transcriptMapsExonsContigRange,
                    transcriptMapsExonsTranscriptRange, proteinRange, transcriptMapsExons.getTranscriptMaps(),
                    variant.getTranscriptPosition());

            variant.setLocationType(locationType);
            variant.getKey().setLocationType(locationType.getName());

            List<String> utrLocationTypes = Arrays.asList("UTR", "UTR-5", "UTR-3");

            Integer exonIndex = transcriptMapsExonsList.indexOf(transcriptMapsExons);
            Integer rightDistance = null;
            Integer leftDistance = null;

            if (utrLocationTypes.contains(locationType.getName())) {

                if (proteinRange != null && proteinRange.isOverlappedBy(transcriptMapsExonsTranscriptRange)) {

                    Range<Integer> proteinExonIntersection = proteinRange.intersectionWith(transcriptMapsExonsTranscriptRange);
                    switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
                        case "+":

                            if (proteinRange.isAfter(transcriptPosition)) {
                                rightDistance = locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum()
                                        + (transcriptMapsExonsTranscriptRange.getMaximum() - proteinExonIntersection.getMinimum()) - 1;
                                leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                            } else if (proteinRange.isBefore(transcriptPosition)) {
                                rightDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                                leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMaximum();
                            } else {
                                rightDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMaximum() - 1;
                                leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                            }

                            if (exonIndex == 0) {
                                variant.setIntronExonDistance(rightDistance);
                            } else if (exonIndex == transcriptMapsExonsList.size() - 1
                                    || Math.abs(leftDistance) < Math.abs(rightDistance)) {
                                variant.setIntronExonDistance(leftDistance);
                            } else {
                                variant.setIntronExonDistance(rightDistance);
                            }

                            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getVersionId(),
                                    "c", variant.getVariantType().getName(),
                                    Math.abs(proteinRange.getMinimum() - transcriptPosition + variant.getIntronExonDistance() - 1),
                                    locatedVariant.getRef(), locatedVariant.getSeq(), variant.getIntronExonDistance()));

                            break;
                        case "-":

                            if (proteinRange.isAfter(transcriptPosition)) {
                                rightDistance = locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum()
                                        + (transcriptMapsExonsTranscriptRange.getMaximum() - proteinExonIntersection.getMinimum());
                                leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                            } else if (proteinRange.isBefore(transcriptPosition)) {
                                rightDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                                leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMaximum();
                            } else {
                                rightDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMaximum() - 1;
                                leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                            }

                            if (exonIndex == 0) {
                                variant.setIntronExonDistance(rightDistance);
                            } else if (exonIndex == transcriptMapsExonsList.size() - 1
                                    || Math.abs(leftDistance) < Math.abs(rightDistance)) {
                                variant.setIntronExonDistance(leftDistance);
                            } else {
                                variant.setIntronExonDistance(rightDistance);
                            }

                            variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getVersionId(),
                                    "c", variant.getVariantType().getName(), proteinRange.getMaximum(), locatedVariant.getRef(),
                                    locatedVariant.getSeq(), variant.getIntronExonDistance(), true));
                            break;
                    }

                }

                variant.setVariantEffect(daoBean.getVariantEffectDAO().findById(variant.getKey().getLocationType()));
                variant.getKey().setVariantEffect(variant.getVariantEffect().getName());

            } else if (locationType.getName().equals("exon")) {

                variant.setNonCanonicalExon(transcriptMapsExons.getKey().getExonNum());
                if (variant.getFeatureId() == null) {
                    variant.setFeatureId(0);
                }

                int utrAdjustedIndex = 0;

                if (proteinRange != null) {

                    Range<Integer> proteinExonIntersection = proteinRange.intersectionWith(transcriptMapsExonsTranscriptRange);
                    switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
                        case "+":

                            // how many UTRs before exon
                            transcriptMapsExonsList.sort((a, b) -> a.getKey().getExonNum().compareTo(b.getKey().getExonNum()));
                            for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                                if (exon.getTranscriptRange().contains(proteinRange.getMinimum())) {
                                    utrAdjustedIndex = transcriptMapsExonsList.indexOf(exon);
                                    break;
                                }
                            }
                            variant.setNonCanonicalExon(transcriptMapsExons.getKey().getExonNum() - utrAdjustedIndex);

                            if (proteinRange.contains(variant.getTranscriptPosition())) {

                                variant.setCodingSequencePosition(
                                        transcriptMapsExonsTranscriptRange.getMaximum() - proteinRange.getMinimum()
                                                + locatedVariant.getPosition() - transcriptMapsExons.getContigEnd() + 1);

                                if (proteinRange.getMinimum() > transcriptMapsExonsTranscriptRange.getMinimum()) {
                                    rightDistance = locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum()
                                            + (transcriptMapsExonsTranscriptRange.getMaximum() - proteinExonIntersection.getMinimum());
                                    leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                                } else if (proteinRange.getMinimum() < transcriptMapsExonsTranscriptRange.getMinimum()) {
                                    rightDistance = locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum() - 2;
                                    leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                                } else {
                                    rightDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMaximum() - 1;
                                    leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                                }

                                if (exonIndex == 0) {
                                    variant.setIntronExonDistance(rightDistance);
                                } else if (exonIndex == transcriptMapsExonsList.size() - 1
                                        || Math.abs(leftDistance) < Math.abs(rightDistance)) {
                                    variant.setIntronExonDistance(leftDistance);
                                } else {
                                    variant.setIntronExonDistance(rightDistance);
                                }

                            } else {

                                variant.setCodingSequencePosition(
                                        transcriptMapsExonsTranscriptRange.getMaximum() - proteinRange.getMinimum()
                                                + locatedVariant.getPosition() - transcriptMapsExons.getContigEnd() + 1);

                                variant.setIntronExonDistance(
                                        variant.getTranscriptPosition() - transcriptMapsExonsTranscriptRange.getMaximum() - 1);

                            }
                            break;
                        case "-":

                            // how many UTRs after exon
                            transcriptMapsExonsList.sort((a, b) -> b.getKey().getExonNum().compareTo(a.getKey().getExonNum()));
                            for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                                if (exon.getTranscriptRange().contains(proteinRange.getMaximum())) {
                                    utrAdjustedIndex++;
                                    break;
                                }
                            }

                            variant.setNonCanonicalExon(transcriptMapsExonsList.indexOf(transcriptMapsExons) + utrAdjustedIndex);

                            if (proteinRange.contains(variant.getTranscriptPosition())) {

                                variant.setCodingSequencePosition(variant.getTranscriptPosition() - proteinRange.getMinimum() + 1);

                                if (proteinRange.getMinimum() < transcriptMapsExonsTranscriptRange.getMinimum()) {
                                    // leftDistance = transcriptMapsExonsContigRange.getMaximum() -
                                    // locatedVariantRange.getMaximum()
                                    // - (transcriptMapsExonsTranscriptRange.getMaximum() -
                                    // proteinExonIntersection.getMinimum());
                                    leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMaximum() - 1;
                                    rightDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                                } else if (proteinRange.getMinimum() > transcriptMapsExonsTranscriptRange.getMinimum()) {
                                    leftDistance = locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum() - 2;
                                    rightDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                                } else {
                                    rightDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMaximum() - 1;
                                    leftDistance = variant.getTranscriptPosition() - proteinExonIntersection.getMinimum() + 1;
                                }

                                if (exonIndex == 0) {
                                    variant.setIntronExonDistance(rightDistance);
                                } else if (exonIndex == transcriptMapsExonsList.size() - 1
                                        || Math.abs(leftDistance) < Math.abs(rightDistance)) {
                                    variant.setIntronExonDistance(leftDistance);
                                } else {
                                    variant.setIntronExonDistance(rightDistance);
                                }

                            } else {
                                variant.setCodingSequencePosition(
                                        (transcriptMapsExonsContigRange.getMaximum() - locatedVariant.getPosition() + 1));

                                variant.setIntronExonDistance((transcriptMapsExonsContigRange.getMaximum() - locatedVariant.getPosition())
                                        - proteinRange.getMaximum());
                            }

                            break;
                    }

                    variant.setAminoAcidStart(Double.valueOf(Math.ceil(variant.getCodingSequencePosition() / 3D)).intValue());
                    variant.setInframe(Boolean.FALSE);
                    variant.setFrameshift(Boolean.FALSE);

                    TranscriptionEngine engine = TranscriptionEngine.getDefault();
                    DNAToRNATranslator dna2RnaTranslator = engine.getDnaRnaTranslator();
                    RNAToAminoAcidTranslator rna2AminoAcidTranslator = engine.getRnaAminoAcidTranslator();

                    String originalDNASeq = transcriptMapsExons.getTranscriptMaps().getTranscript().getSeq();
                    originalDNASeq = originalDNASeq.substring(proteinRange.getMinimum() - 1, proteinRange.getMaximum());

                    DNASequence originalDNASequence = new DNASequence(originalDNASeq);
                    Sequence<NucleotideCompound> originalRNASequence = dna2RnaTranslator.createSequence(originalDNASequence);
                    Sequence<AminoAcidCompound> originalProteinSequence = rna2AminoAcidTranslator.createSequence(originalRNASequence);
                    AminoAcidCompound originalAACompound = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());

                    if (variant.getReferenceAllele().length() == variant.getAlternateAllele().length()
                            && variant.getReferenceAllele().length() == 1) {

                        variant.setOriginalAminoAcid(originalAACompound.getBase());

                        variant.setAminoAcidEnd(
                                variant.getAminoAcidStart() + (locatedVariant.getEndPosition() - locatedVariant.getPosition()));

                        String dnaSeqPart1 = originalDNASeq.substring(0, variant.getCodingSequencePosition() - 1);
                        String dnaSeqPart2 = originalDNASeq.substring(
                                variant.getCodingSequencePosition() - 1 + (locatedVariant.getEndPosition() - locatedVariant.getPosition()));

                        String finalDNASeq = String.format("%s%s%s", dnaSeqPart1, variant.getAlternateAllele(), dnaSeqPart2);
                        if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                            String altAllele = new DNASequence(variant.getAlternateAllele()).getReverseComplement().getSequenceAsString();
                            finalDNASeq = String.format("%s%s%s", dnaSeqPart1, altAllele, dnaSeqPart2);
                        }

                        DNASequence finalDNASequence = new DNASequence(finalDNASeq);
                        Sequence<NucleotideCompound> finalRNASequence = dna2RnaTranslator.createSequence(finalDNASequence);
                        Sequence<AminoAcidCompound> finalProteinSequence = rna2AminoAcidTranslator.createSequence(finalRNASequence);
                        AminoAcidCompound finalAACompound = finalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                        variant.setFinalAminoAcid(finalAACompound.getBase());

                        if (variant.getOriginalAminoAcid().equals(variant.getFinalAminoAcid())) {
                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("synonymous"));
                            variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                        } else if (!variant.getOriginalAminoAcid().equals("*") && !variant.getFinalAminoAcid().equals("*")) {
                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("missense"));
                            variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                        } else if (variant.getFinalAminoAcid().equals("*")) {
                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("nonsense"));
                            variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                        } else if (variant.getOriginalAminoAcid().equals("*")) {
                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("stoploss"));
                            variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                        }

                        if (refSeqCDS != null && "snp".equals(variant.getVariantType().getName())
                                && !"synonymous".equals(variant.getVariantEffect().getName())) {
                            variant.setHgvsProtein(String.format("%s:p.%s%d%s", refSeqCDS.getProteinId(), originalAACompound.getLongName(),
                                    variant.getAminoAcidStart(), "*".equals(finalAACompound.getShortName()) ? finalAACompound.getShortName()
                                            : finalAACompound.getLongName()));
                        }

                    } else {

                        Integer dLength = Math.abs(variant.getReferenceAllele().length() - variant.getAlternateAllele().length());
                        if (dLength % 3 != 0) {
                            variant.setFrameshift(Boolean.TRUE);
                        }

                        String dnaSeqPart1 = originalDNASeq.substring(0, variant.getCodingSequencePosition() - 1);
                        String dnaSeqPart2 = originalDNASeq.substring(
                                variant.getCodingSequencePosition() - 1 + variant.getReferenceAllele().length(), originalDNASeq.length());

                        String finalDNASeq = String.format("%s%s%s", dnaSeqPart1, variant.getAlternateAllele(), dnaSeqPart2);
                        if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                            String altAllele = new DNASequence(variant.getAlternateAllele()).getReverseComplement().getSequenceAsString();
                            finalDNASeq = String.format("%s%s%s", dnaSeqPart1, altAllele, dnaSeqPart2);
                        }

                        DNASequence finalDNASequence = new DNASequence(finalDNASeq);
                        Sequence<NucleotideCompound> finalRNASequence = dna2RnaTranslator.createSequence(finalDNASequence);
                        Sequence<AminoAcidCompound> finalProteinSequence = rna2AminoAcidTranslator.createSequence(finalRNASequence);

                        if (variant.getFrameshift()) {

                            Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    variant.getAminoAcidStart() + 4);
                            variant.setOriginalAminoAcid(String.format("%s...", retvalo.getSequenceAsString()));

                            Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    variant.getAminoAcidStart() + 4);
                            variant.setFinalAminoAcid(String.format("%s...", retvalf.getSequenceAsString()));

                            variant.setAminoAcidEnd(originalProteinSequence.getLength() + variant.getAlternateAllele().length());

                            if (refSeqCDS != null && "snp".equals(variant.getVariantType().getName())) {
                                variant.setHgvsProtein(String.format("%s:p.%s%dfs", refSeqCDS.getProteinId(),
                                        originalAACompound.getLongName(), variant.getAminoAcidStart()));
                            }

                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("frameshifting indel"));
                            variant.getKey().setVariantEffect(variant.getVariantEffect().getName());

                        } else {

                            if ("del".equals(variant.getVariantType().getName())) {
                                variant.setInframe((variant.getCodingSequencePosition()) % 3 == 0);
                            } else {
                                variant.setInframe((variant.getCodingSequencePosition() - 1) % 3 == 0);
                            }

                            Integer aaEndFinal = null;
                            if (StringUtils.isEmpty(variant.getReferenceAllele())) {
                                variant.setAminoAcidEnd(variant.getAminoAcidStart() + 1);
                                aaEndFinal = variant.getAminoAcidStart() - 1 + variant.getAlternateAllele().length();
                            } else {
                                variant.setAminoAcidEnd(
                                        (variant.getCodingSequencePosition() + variant.getReferenceAllele().length() - 1) / 3);
                                aaEndFinal = (variant.getCodingSequencePosition() + variant.getReferenceAllele().length() - 1) / 3;
                            }

                            if ("del".equals(variant.getVariantType().getName())) {
                                variant.setAminoAcidStart(variant.getAminoAcidStart() - (variant.getReferenceAllele().length() / 3) + 1);
                                variant.setAminoAcidEnd(variant.getAminoAcidStart() - 1 + (variant.getReferenceAllele().length() / 3));
                            }

                            Sequence<AminoAcidCompound> retvalo = originalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                    variant.getAminoAcidEnd());
                            variant.setOriginalAminoAcid(retvalo.getSequenceAsString());

                            StringBuilder sb = new StringBuilder();
                            if (!"del".equals(variant.getVariantType().getName())) {

                                Sequence<AminoAcidCompound> retvalf = finalProteinSequence.getSubSequence(variant.getAminoAcidStart(),
                                        aaEndFinal);
                                variant.setFinalAminoAcid(retvalf.getSequenceAsString());
                                if (refSeqCDS != null) {
                                    retvalf.forEach(a -> sb.append(a.getLongName()));
                                    variant.setHgvsProtein(String.format("%s:p.%s%d_%s%ddelins%s", refSeqCDS.getProteinId(),
                                            originalAACompound.getLongName(), variant.getAminoAcidStart(),
                                            originalProteinSequence.getCompoundAt(variant.getAminoAcidEnd()).getLongName(),
                                            variant.getAminoAcidEnd(), sb.toString()));
                                }
                            } else {
                                if (refSeqCDS != null) {
                                    if (variant.getOriginalAminoAcid().length() > 1) {
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
                            }

                            // AminoAcidCompound finalAACompound =
                            // finalProteinSequence.getCompoundAt(variant.getAminoAcidStart());
                            // variant.setFinalAminoAcid(finalAACompound.getBase());

                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("non-frameshifting indel"));
                            variant.getKey().setVariantEffect(variant.getVariantEffect().getName());

                        }

                        if (finalProteinSequence.getSequenceAsString().contains("*")) {
                            // variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("nonsense
                            // indel"));
                            // variant.getKey().setVariantEffect(variant.getVariantEffect().getName());
                        }

                    }

                    variant.setHgvsCodingSequence(toHGVS(transcriptMapsExons.getTranscriptMaps().getTranscript().getVersionId(), "c",
                            variant.getVariantType().getName(), variant.getCodingSequencePosition(), locatedVariant.getRef(),
                            locatedVariant.getSeq(), null, "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())));

                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return variant;
    }

    private static LocationType getLocationType(CANVASDAOBeanService daoBean, Range<Integer> locatedVariantRange,
            Range<Integer> transcriptMapsExonsContigRange, Range<Integer> transcriptMapsExonsTranscriptRange, Range<Integer> proteinRange,
            TranscriptMaps tMap, Integer transcriptPosition) throws CANVASDAOException {

        // UTR, UTR-5, UTR-3, intron, exon, intergenic, potential RNA-editing site, intron/exon boundary

        String locationTypeValue = "UTR";

        if (proteinRange != null) {

            if (transcriptMapsExonsTranscriptRange != null && transcriptMapsExonsTranscriptRange.contains(transcriptPosition)) {
                locationTypeValue = "exon";
            }

            if (proteinRange.isBefore(transcriptPosition)) {
                locationTypeValue = "UTR-3";
            } else if (proteinRange.isAfter(transcriptPosition)) {
                locationTypeValue = "UTR-5";
            }

        }
        LocationType locationType = daoBean.getLocationTypeDAO().findById(locationTypeValue);
        return locationType;
    }

    private static Integer getFeatureId(CANVASDAOBeanService daoBean, String refseqVersion, TranscriptMaps tMap, Integer transcriptPosition)
            throws CANVASDAOException {
        List<Feature> featureList = daoBean.getFeatureDAO().findByRefSeqVersionAndTranscriptId(refseqVersion,
                tMap.getTranscript().getVersionId());
        Integer ret = null;
        if (CollectionUtils.isNotEmpty(featureList)) {
            for (Feature f : featureList) {
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRegionGroupId(f.getRegionGroup().getId());
                if (CollectionUtils.isNotEmpty(rgrList)) {
                    for (RegionGroupRegion rgr : rgrList) {
                        Range<Integer> rgrRange = Range.between(rgr.getKey().getRegionStart(), rgr.getKey().getRegionEnd());
                        if (rgrRange.contains(transcriptPosition)) {
                            ret = f.getId();
                        }
                    }
                }
            }
        }
        return ret;
    }

    private static Integer getIntronExonDistance(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons,
            List<TranscriptMapsExons> transcriptMapsExonsList) {
        Integer rightDistance = locatedVariant.getEndPosition() - transcriptMapsExons.getContigEnd() - 2;
        Integer leftDistance = locatedVariant.getPosition() - transcriptMapsExons.getContigStart() + 1;

        if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
            leftDistance = -leftDistance + 2;
            rightDistance = -rightDistance;
        }

        if (transcriptMapsExonsList.size() > 1) {
            int exonIndex = transcriptMapsExonsList.indexOf(transcriptMapsExons);
            if (exonIndex == 0) {
                return rightDistance;
            } else if (exonIndex == transcriptMapsExonsList.size() - 1 || Math.abs(leftDistance) < Math.abs(rightDistance)) {
                return leftDistance;
            } else {
                return rightDistance;
            }
        }
        return null;
    }

    private static Integer getTranscriptPosition(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons) {
        Integer ret = null;
        switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
            case "+":
                ret = transcriptMapsExons.getTranscriptEnd() + (locatedVariant.getPosition() - transcriptMapsExons.getContigEnd());
                break;
            case "-":
                ret = transcriptMapsExons.getTranscriptEnd() + (transcriptMapsExons.getContigEnd() - locatedVariant.getPosition());
                break;
        }
        return ret;
    }

}
