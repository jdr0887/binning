package org.renci.canvas.binning.core.grch37;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

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
import org.renci.canvas.dao.annotation.model.AnnotationGeneExternalId;
import org.renci.canvas.dao.hgnc.model.HGNCGene;
import org.renci.canvas.dao.refseq.model.Feature;
import org.renci.canvas.dao.refseq.model.LocationType;
import org.renci.canvas.dao.refseq.model.RefSeqCodingSequence;
import org.renci.canvas.dao.refseq.model.RefSeqGene;
import org.renci.canvas.dao.refseq.model.RegionGroupRegion;
import org.renci.canvas.dao.refseq.model.TranscriptMaps;
import org.renci.canvas.dao.refseq.model.TranscriptMapsExons;
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.refseq.model.Variants_61_2PK;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariantsFactory extends AbstractVariantsFactory {

    private static final Logger logger = LoggerFactory.getLogger(VariantsFactory.class);

    private static VariantsFactory instance;

    public static VariantsFactory getInstance() {
        if (instance == null) {
            instance = new VariantsFactory();
        }
        return instance;
    }

    private VariantsFactory() {
        super();
    }

    @Override
    public String getRefSeqVersion() {
        return "61";
    }

    public Variants_61_2 createIntronicVariant(CANVASDAOBeanService daoBean, LocatedVariant locatedVariant, List<TranscriptMaps> mapsList,
            TranscriptMaps tMap, List<TranscriptMapsExons> transcriptMapsExonsList) throws BinningException {
        logger.debug(
                "ENTERING createIntronicVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>)");

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

            variant.setLocationType(daoBean.getLocationTypeDAO().findById("intron"));
            variant.getId().setLocationType(variant.getLocationType().getId());

            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("intron"));
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

            RefSeqCodingSequence refSeqCDS = null;
            if (CollectionUtils.isNotEmpty(refSeqCodingSequenceList)) {
                refSeqCDS = refSeqCodingSequenceList.get(0);
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                if (CollectionUtils.isNotEmpty(rgrList)) {
                    RegionGroupRegion rgr = rgrList.get(0);
                    proteinRange = Range.between(rgr.getId().getRegionStart(), rgr.getId().getRegionEnd());
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
                                variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("splice-site"));
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
                                        variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("splice-site-UTR-5"));
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
                                        variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("splice-site-UTR-3"));
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
                variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("splice-site-UTR"));
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
            }

            List<AnnotationGeneExternalId> annotationGeneExternalIdsList = daoBean.getAnnotationGeneExternalIdDAO()
                    .findByExternalId(refSeqGene.getId());
            Optional<AnnotationGeneExternalId> optionalAnnotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getId().getNamespace())).findFirst();

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

            variant.setHgvsGenomic(toHGVS(tMap.getGenomeRefSeq().getId(), "g", variant.getVariantType().getId(),
                    locatedVariant.getPosition(), locatedVariant.getRef(), locatedVariant.getSeq()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        logger.info(variant.toString());
        return variant;
    }

    public Variants_61_2 createBorderCrossingVariant(CANVASDAOBeanService daoBean, LocatedVariant locatedVariant, TranscriptMaps tMap,
            List<TranscriptMaps> mapsList, List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons)
            throws BinningException {
        logger.debug(
                "ENTERING createBorderCrossingVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");
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
            Optional<AnnotationGeneExternalId> optionalAnnotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getId().getNamespace())).findFirst();

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

            RefSeqCodingSequence refSeqCDS = null;
            if (CollectionUtils.isNotEmpty(refSeqCodingSequenceList)) {
                refSeqCDS = refSeqCodingSequenceList.get(0);
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                if (CollectionUtils.isNotEmpty(rgrList)) {
                    RegionGroupRegion rgr = rgrList.get(0);
                    proteinRange = Range.between(rgr.getId().getRegionStart(), rgr.getId().getRegionEnd());
                }
            }

            LocationType locationType = daoBean.getLocationTypeDAO().findById("intron/exon boundary");
            variant.setLocationType(locationType);
            variant.getId().setLocationType(locationType.getId());

            if ("(LARGEDELETION)".equals(locatedVariant.getSeq())) {
                variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("noncoding boundary-crossing indel"));
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());
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
                        variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                    }
                    break;
                case "-":
                    variant.setTranscriptPosition(Math.max(1, transcriptMapsExonsTranscriptRange.getMinimum()
                            - (locatedVariantRange.getMinimum() - transcriptMapsExonsContigRange.getMinimum())));
                    if (exonOrProteinRange.contains(variant.getTranscriptPosition())) {
                        // really a utr3
                        variant.setTranscriptPosition(null);
                        variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("noncoding boundary-crossing indel"));
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

    public Variants_61_2 createExonicVariant(CANVASDAOBeanService daoBean, LocatedVariant locatedVariant, List<TranscriptMaps> mapsList,
            List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons) throws BinningException {
        logger.debug(
                "ENTERING createExonicVariant(String, LocatedVariant, List<TranscriptMaps> mapsList, TranscriptMaps, List<TranscriptMapsExons>, TranscriptMapsExons)");

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
            Optional<AnnotationGeneExternalId> optionalAnnotationGeneExternalIds = annotationGeneExternalIdsList.stream()
                    .filter(a -> !"OMIM".equals(a.getId().getNamespace())).findFirst();

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

            if ("del".equals(locatedVariant.getVariantType().getId()) && locatedVariant.getRef().equals(locatedVariant.getSeq())) {
                variant.setAlternateAllele("");
            }

            variant.setTranscriptPosition(getTranscriptPosition(locatedVariant, transcriptMapsExons));

            List<Feature> featureList = daoBean.getFeatureDAO().findByRefSeqVersionAndTranscriptIdAndTranscriptPosition(getRefSeqVersion(),
                    transcriptMapsExons.getTranscriptMaps().getTranscript().getId(), variant.getTranscriptPosition());
            featureList.sort((a, b) -> b.getRegionGroup().getId().compareTo(a.getRegionGroup().getId()));
            if (CollectionUtils.isNotEmpty(featureList)) {
                Feature feature = featureList.get(0);
                logger.info(feature.toString());
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

            RefSeqCodingSequence refSeqCDS = null;
            if (CollectionUtils.isNotEmpty(refSeqCodingSequenceList)) {
                refSeqCDS = refSeqCodingSequenceList.get(0);
                List<RegionGroupRegion> rgrList = daoBean.getRegionGroupRegionDAO().findByRefSeqCodingSequenceId(refSeqCDS.getId());
                if (CollectionUtils.isNotEmpty(rgrList)) {
                    proteinRange = rgrList.get(0).getId().getRegionRange();
                }
            }

            LocationType locationType = getLocationType(daoBean, locatedVariantRange, transcriptMapsExonsContigRange,
                    transcriptMapsExonsTranscriptRange, proteinRange, transcriptMapsExons.getTranscriptMaps(),
                    variant.getTranscriptPosition());

            variant.setLocationType(locationType);
            variant.getId().setLocationType(locationType.getId());

            List<String> utrLocationTypes = Arrays.asList("UTR", "UTR-5", "UTR-3");

            if (utrLocationTypes.contains(locationType.getId())) {

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

                variant.setVariantEffect(daoBean.getVariantEffectDAO().findById(variant.getId().getLocationType()));
                variant.getId().setVariantEffect(variant.getVariantEffect().getId());

            } else if (locationType.getId().equals("exon")) {

                variant.setNonCanonicalExon(transcriptMapsExons.getId().getExonNum());

                if (variant.getFeatureId() == null) {
                    variant.setFeatureId(0);
                }

                if (proteinRange != null) {

                    variant.setInframe(Boolean.FALSE);
                    variant.setFrameshift(Boolean.FALSE);

                    variant.setNonCanonicalExon(getNonCanonicalExon(transcriptMapsExonsList, transcriptMapsExons, proteinRange));

                    variant.setIntronExonDistance(getIntronExonDistance(locatedVariant, transcriptMapsExons, transcriptMapsExonsList,
                            proteinRange, variant.getTranscriptPosition()));

                    variant.setCodingSequencePosition(
                            getCodingSequencePosition(locatedVariant, transcriptMapsExons, variant.getTranscriptPosition(), proteinRange));

                    TranscriptionEngine engine = TranscriptionEngine.getDefault();
                    DNAToRNATranslator dna2RnaTranslator = engine.getDnaRnaTranslator();
                    RNAToAminoAcidTranslator rna2AminoAcidTranslator = engine.getRnaAminoAcidTranslator();

                    String originalDNASeq = transcriptMapsExons.getTranscriptMaps().getTranscript().getSeq();
                    originalDNASeq = originalDNASeq.substring(proteinRange.getMinimum() - 1, proteinRange.getMaximum());

                    DNASequence originalDNASequence = new DNASequence(originalDNASeq);
                    Sequence<NucleotideCompound> originalRNASequence = dna2RnaTranslator.createSequence(originalDNASequence);
                    Sequence<AminoAcidCompound> originalProteinSequence = rna2AminoAcidTranslator.createSequence(originalRNASequence);

                    String dnaSeqPart1, dnaSeqPart2;

                    if (variant.getReferenceAllele().length() == variant.getAlternateAllele().length()
                            && variant.getReferenceAllele().length() == 1) {

                        variant.setAminoAcidStart(Double.valueOf(Math.ceil(variant.getCodingSequencePosition() / 3D)).intValue());

                        if ((variant.getCodingSequencePosition() / 3D) % 3 != 0
                                && variant.getAminoAcidStart() >= originalProteinSequence.getLength()) {
                            variant.setAminoAcidStart(variant.getAminoAcidStart() - 1);
                        }

                        AminoAcidCompound originalAACompound = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());

                        variant.setOriginalAminoAcid(originalAACompound.getBase());

                        variant.setAminoAcidEnd(
                                variant.getAminoAcidStart() + (locatedVariant.getEndPosition() - locatedVariant.getPosition()));

                        dnaSeqPart1 = originalDNASeq.substring(0, variant.getCodingSequencePosition() - 1);
                        dnaSeqPart2 = originalDNASeq.substring(
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
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                        } else if (!variant.getOriginalAminoAcid().equals("*") && !variant.getFinalAminoAcid().equals("*")) {
                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("missense"));
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                        } else if (variant.getFinalAminoAcid().equals("*")) {
                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("nonsense"));
                            variant.getId().setVariantEffect(variant.getVariantEffect().getId());
                        } else if (variant.getOriginalAminoAcid().equals("*")) {
                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("stoploss"));
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

                        variant.setAminoAcidStart(getAminoAcidStart(variant.getVariantType().getId(), variant.getCodingSequencePosition(),
                                variant.getFrameshift(), variant.getInframe(), variant.getReferenceAllele(),
                                transcriptMapsExons.getTranscriptMaps().getStrand()));

                        AminoAcidCompound originalAACompound = originalProteinSequence.getCompoundAt(variant.getAminoAcidStart());

                        Pair<String, String> dnaSequenceParts = getDNASequenceParts(variant.getVariantType().getId(),
                                transcriptMapsExons.getTranscriptMaps().getStrand(), originalDNASeq, variant.getCodingSequencePosition(),
                                variant.getReferenceAllele());

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

                            if (refSeqCDS != null && Arrays.asList("snp", "del").contains(variant.getVariantType().getId())) {
                                variant.setHgvsProtein(String.format("%s:p.%s%dfs", refSeqCDS.getProteinId(),
                                        originalAACompound.getLongName(), variant.getAminoAcidStart()));
                            }

                            if ("del".equals(variant.getLocatedVariant().getVariantType().getId())
                                    && "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                                variant.setIntronExonDistance(
                                        Math.abs(locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum() - 2));
                            }
                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("frameshifting indel"));
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

                                if ("del".equals(variant.getVariantType().getId()) && !variant.getInframe()) {
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

                            variant.setVariantEffect(daoBean.getVariantEffectDAO().findById("non-frameshifting indel"));
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
