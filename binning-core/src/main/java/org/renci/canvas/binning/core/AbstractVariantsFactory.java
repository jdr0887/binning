package org.renci.canvas.binning.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.refseq.model.TranscriptMaps;
import org.renci.canvas.dao.refseq.model.TranscriptMapsExons;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractVariantsFactory {

    private static final Logger logger = LoggerFactory.getLogger(AbstractVariantsFactory.class);

    public abstract String getRefSeqVersion();

    protected String toHGVS(String accession, String accessionType, String type, Integer position, String ref, String alt) {
        return toHGVS(accession, accessionType, type, position, ref, alt, null, Boolean.FALSE);
    }

    protected String toHGVS(String accession, String accessionType, String type, Integer position, String ref, String alt,
            Integer intronExonDistance) {
        return toHGVS(accession, accessionType, type, position, ref, alt, intronExonDistance, Boolean.FALSE);
    }

    protected String toHGVS(String accession, String accessionType, String type, Integer position, String ref, String alt,
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
                    ret = String.format("%s:%s.%d%+d%s>%s", accession, accessionType, position, intronExonDistance, ref, alt);
                } else {
                    ret = String.format("%s:%s.%d%s>%s", accession, accessionType, position, ref, alt);
                }
                break;
            case "sub":
                if (useComplement) {
                    ret = String.format("%s:%s.%s_%ddelins%s", accession, accessionType, Integer.valueOf(position - ref.length() + 1),
                            position, alt);
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
                    ret = String.format("%s:%s.%d%+d_%d+%ddel", accession, accessionType, end, intronExonDistance, end,
                            intronExonDistance + ref.length() - 1);
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

    protected String getLocationType(CANVASDAOBeanService daoBean, Range<Integer> locatedVariantRange,
            Range<Integer> transcriptMapsExonsContigRange, Range<Integer> transcriptMapsExonsTranscriptRange, Range<Integer> proteinRange,
            TranscriptMaps tMap, Integer transcriptPosition) throws CANVASDAOException {

        // UTR, UTR-5, UTR-3, intron, exon, intergenic, potential RNA-editing site, intron/exon boundary

        if (proteinRange != null) {

            if (proteinRange.isOverlappedBy(transcriptMapsExonsTranscriptRange)) {

                Range<Integer> proteinTranscriptIntersection = proteinRange.intersectionWith(transcriptMapsExonsTranscriptRange);

                if (proteinRange.isBefore(transcriptPosition)) {
                    return "UTR-3";
                }

                if (proteinRange.isAfter(transcriptPosition)) {
                    return "UTR-5";
                }

                if (proteinTranscriptIntersection.isAfter(transcriptPosition)) {
                    return "UTR-3";
                }
                if (proteinTranscriptIntersection.isBefore(transcriptPosition)) {
                    return "UTR-5";
                }

            }

            if (proteinRange.isBefore(transcriptPosition)) {
                return "UTR-3";
            }

            if (proteinRange.isAfter(transcriptPosition)) {
                return "UTR-5";
            }

        } 

        if (proteinRange == null && transcriptMapsExonsTranscriptRange != null
                && transcriptMapsExonsTranscriptRange.contains(transcriptPosition)) {
            return "UTR";
        }

        return "exon";
    }

    protected Integer getIntronExonDistance(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons,
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

    protected Integer getIntronExonDistanceNEW(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons,
            List<TranscriptMapsExons> transcriptMapsExonsList, Range<Integer> proteinRange, Integer transcriptPosition) {

        Range<Integer> locatedVariantRange = locatedVariant.toRange();
        Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
        Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();
        Range<Integer> proteinExonIntersection = proteinRange.intersectionWith(transcriptMapsExonsTranscriptRange);

        Integer leftDistance = transcriptPosition - Math.max(proteinRange.getMinimum(), transcriptMapsExonsTranscriptRange.getMinimum());
        Integer rightDistance = transcriptPosition - Math.min(proteinRange.getMaximum(), transcriptMapsExonsTranscriptRange.getMaximum());

        transcriptMapsExonsList.sort((a, b) -> Integer.compare(a.getId().getExonNum(), b.getId().getExonNum()));
        Integer exonIndex = transcriptMapsExonsList.indexOf(transcriptMapsExons);

        Boolean isFirst = exonIndex == 0 && (proteinRange.getMinimum() <= transcriptMapsExonsTranscriptRange.getMinimum()
                || proteinRange.isAfter(transcriptPosition));
        Boolean isLast = exonIndex == transcriptMapsExonsList.size() - 1 && proteinRange.isBefore(transcriptPosition);

        if (transcriptMapsExonsList.size() > 1) {

            if (isLast) {
                leftDistance = transcriptPosition - Math.max(proteinRange.getMaximum(), transcriptMapsExonsTranscriptRange.getMinimum());
                rightDistance = transcriptPosition - Math.min(proteinRange.getMinimum(), transcriptMapsExonsTranscriptRange.getMinimum());
            }

            if (isFirst) {
                leftDistance = transcriptPosition - Math.min(proteinRange.getMinimum(), transcriptMapsExonsTranscriptRange.getMinimum());
                rightDistance = transcriptPosition - Math.min(proteinRange.getMinimum(), transcriptMapsExonsTranscriptRange.getMaximum());
            }

        }

        Integer distance = null;

        if (isFirst) {
            distance = rightDistance;
        } else if (isLast) {
            distance = leftDistance;
        } else {

            if (Math.abs(leftDistance) < Math.abs(rightDistance)) {
                distance = leftDistance;
            } else {
                distance = rightDistance;
            }

        }

        if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {

            if (Arrays.asList("del").contains(locatedVariant.getVariantType().getId()) && transcriptMapsExonsList.size() > 1) {
                distance -= locatedVariant.getEndPosition() - locatedVariant.getPosition() - 2;
            } else if (!isFirst && !proteinExonIntersection.equals(transcriptMapsExonsTranscriptRange)) {
                distance--;
            } else if (proteinRange.containsRange(transcriptMapsExonsTranscriptRange)
                    && !proteinExonIntersection.equals(transcriptMapsExonsTranscriptRange)) {
                distance--;
            }

        }

        if (Arrays.asList("del").contains(locatedVariant.getVariantType().getId())
                && "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
            // distance -= locatedVariant.getEndPosition() - locatedVariant.getPosition() - 2;

            // if (Arrays.asList("del").contains(locatedVariant.getVariantType().getId()) && transcriptMapsExonsList.size() > 1) {
            // distance -= locatedVariant.getEndPosition() - locatedVariant.getPosition() - 2;
            // } else if (transcriptMapsExonsList.size() == 1) {
            // distance--;
            // }

            // if (/*
            // * (proteinExonIntersection.contains(transcriptPosition) &&
            // * !proteinExonIntersection.equals(transcriptMapsExonsTranscriptRange)) ||
            // */ Arrays.asList("snp", "sub").contains(locatedVariant.getVariantType().getId()) || isFirst) {
            // distance--;
            // } else if (Arrays.asList("del").contains(locatedVariant.getVariantType().getId())) {
            // distance -= locatedVariant.getEndPosition() - locatedVariant.getPosition() - 2;
            // }
        }

        if ("+".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {

            if (Arrays.asList("snp").contains(locatedVariant.getVariantType().getId())
                    && proteinExonIntersection.contains(transcriptPosition)) {
                // distance++;
            }

            // if (Arrays.asList("sub", "ins").contains(locatedVariant.getVariantType().getId()) || isFirst) {
            // distance += locatedVariant.getEndPosition() - locatedVariant.getPosition() - 1;
            // } else if (Arrays.asList("del").contains(locatedVariant.getVariantType().getId())) {
            // distance--;
            // } else if (proteinExonIntersection.contains(transcriptPosition)) {
            // distance++;
            // }
        }

        return distance;
    }

    protected Integer getIntronExonDistance(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons,
            List<TranscriptMapsExons> transcriptMapsExonsList, Range<Integer> proteinRange, Integer transcriptPosition) {

        Range<Integer> locatedVariantRange = locatedVariant.toRange();
        Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
        Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();
        Range<Integer> proteinExonIntersection = proteinRange.intersectionWith(transcriptMapsExonsTranscriptRange);

        transcriptMapsExonsList.sort((a, b) -> Integer.compare(a.getId().getExonNum(), b.getId().getExonNum()));
        Integer exonIndex = transcriptMapsExonsList.indexOf(transcriptMapsExons);

        Boolean isFirst = Boolean.FALSE;
        if (exonIndex == 0) {
            if (proteinRange.getMinimum() <= transcriptMapsExonsTranscriptRange.getMinimum()) {
                isFirst = Boolean.TRUE;
            }
            if (proteinRange.isAfter(transcriptPosition)) {
                isFirst = Boolean.TRUE;
            }
        }

        Boolean isLast = Boolean.FALSE;
        if (exonIndex == transcriptMapsExonsList.size() - 1) {
            if (proteinRange.getMinimum() <= transcriptMapsExonsTranscriptRange.getMinimum()) {
                isLast = Boolean.TRUE;
            }
            if (proteinRange.isBefore(transcriptPosition)) {
                isLast = Boolean.TRUE;
            }
        }

        if ("+".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {

            if (isFirst) {

                if (proteinRange.isAfter(transcriptPosition)) {
                    // we are in UTR5 region
                    return locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum()
                            + (transcriptMapsExonsTranscriptRange.getMaximum() - proteinExonIntersection.getMinimum()) - 1;
                }

                if (proteinRange.contains(transcriptPosition)) {
                    if (transcriptMapsExonsTranscriptRange.getMinimum() == proteinRange.getMinimum()) {
                        // first interval is an exon
                        return transcriptPosition - proteinExonIntersection.getMaximum() - 1;
                    }

                    Integer right = proteinExonIntersection.getMaximum() - transcriptPosition;
                    Integer left = transcriptPosition - proteinExonIntersection.getMinimum() + 1;

                    return Math.abs(left) < Math.abs(right) ? left : right;
                }

            }

            if (isLast) {

                if (proteinRange.isBefore(transcriptPosition) || proteinExonIntersection.isAfter(transcriptPosition)) {
                    // we are in UTR3 region
                    return transcriptPosition - proteinExonIntersection.getMaximum();
                }

                if (proteinRange.contains(transcriptPosition)) {
                    if (transcriptMapsExonsTranscriptRange.getMaximum() == proteinRange.getMaximum()) {
                        // last interval is an exon
                        return transcriptPosition - proteinExonIntersection.getMinimum() + 1;
                    }

                    Integer right = proteinExonIntersection.getMaximum() - transcriptPosition;
                    Integer left = transcriptPosition - proteinExonIntersection.getMinimum() + 1;

                    return Math.abs(left) < Math.abs(right) ? left : right;

                    // return proteinExonIntersection.getMaximum() - transcriptPosition
                }

            }

            Integer right = proteinExonIntersection.getMaximum() - transcriptPosition;
            Integer left = transcriptPosition - proteinExonIntersection.getMinimum() + 1;
            return Math.abs(left) < Math.abs(right) ? left : right;

        }

        if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {

            if (isFirst) {

                if (proteinRange.isAfter(transcriptPosition)) {
                    // we are in UTR5 region
                    return transcriptPosition - proteinRange.getMinimum();
                    // return locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum()
                    // + (transcriptMapsExonsTranscriptRange.getMaximum() - proteinExonIntersection.getMinimum()) - 1;
                }

                if (proteinRange.contains(transcriptPosition)) {
                    if (transcriptMapsExonsTranscriptRange.getMinimum() == proteinRange.getMinimum()) {
                        // first interval is an exon
                        return transcriptPosition - proteinExonIntersection.getMaximum() - 1;
                    }

                    Integer left = transcriptPosition - transcriptMapsExonsTranscriptRange.getMinimum() + 1;
                    Integer right = transcriptPosition - proteinExonIntersection.getMaximum() - 1;

                    return Math.abs(left) < Math.abs(right) ? left : right;
                }

            }
            // if ("del".equals(variant.getLocatedVariant().getVariantType().getId())
            // && "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
            // variant.setIntronExonDistance(
            // Math.abs(locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum() - 2));
            // }

            if (isLast) {

                if (proteinRange.isBefore(transcriptPosition)) {
                    // we are in UTR3 region
                    return transcriptPosition - proteinExonIntersection.getMaximum();
                }

                if (proteinRange.contains(transcriptPosition)) {
                    if (transcriptMapsExonsTranscriptRange.getMaximum() == proteinRange.getMaximum()) {
                        // last interval is an exon
                        return transcriptPosition - proteinExonIntersection.getMinimum() + 1;
                    }
                    Integer left = transcriptPosition - proteinExonIntersection.getMinimum() + 1;
                    Integer right = transcriptPosition - proteinExonIntersection.getMaximum() - 1;

                    return Math.abs(left) < Math.abs(right) ? left : right;
                }

            }

            Integer right = transcriptPosition - proteinExonIntersection.getMaximum() - 1;
            Integer left = transcriptPosition - proteinExonIntersection.getMinimum() + 1;

            // if ("sub".equals(locatedVariant.getVariantType().getId())) {
            // left = transcriptPosition - proteinExonIntersection.getMinimum();
            // }

            if ("del".equals(locatedVariant.getVariantType().getId()) || "sub".equals(locatedVariant.getVariantType().getId())) {
                left = Math.abs(locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum() - 2);
            }

            return Math.abs(left) < Math.abs(right) ? left : right;

        }

        return null;
    }

    protected Integer getNonCanonicalExon(List<TranscriptMapsExons> transcriptMapsExonsList, TranscriptMapsExons transcriptMapsExons,
            Range<Integer> proteinRange) {
        Integer ret = null;
        int utrAdjustedIndex = 0;
        switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
            case "+":
                // how many UTRs before exon
                transcriptMapsExonsList.sort((a, b) -> a.getId().getExonNum().compareTo(b.getId().getExonNum()));
                for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                    if (exon.getTranscriptRange().contains(proteinRange.getMinimum())) {
                        utrAdjustedIndex = transcriptMapsExonsList.indexOf(exon);
                        break;
                    }
                }
                ret = transcriptMapsExons.getId().getExonNum() - utrAdjustedIndex;
                break;
            case "-":
                // how many UTRs after exon
                transcriptMapsExonsList.sort((a, b) -> b.getId().getExonNum().compareTo(a.getId().getExonNum()));
                for (TranscriptMapsExons exon : transcriptMapsExonsList) {
                    ++utrAdjustedIndex;
                    if (exon.getTranscriptRange().contains(proteinRange.getMaximum())) {
                        break;
                    }
                }
                ret = transcriptMapsExonsList.indexOf(transcriptMapsExons) + utrAdjustedIndex;
                break;
        }
        return ret;
    }

    protected Integer getTranscriptPosition(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons,
            Range<Integer> proteinRange) {
        Range<Integer> exonContigRange = transcriptMapsExons.getContigRange();
        Range<Integer> exonTranscriptRange = transcriptMapsExons.getTranscriptRange();

        Integer transcriptPosition = locatedVariant.getPosition() - exonContigRange.getMinimum() + exonTranscriptRange.getMinimum();

        if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
            transcriptPosition = exonTranscriptRange.getMinimum() + (exonContigRange.getMaximum() - locatedVariant.getPosition());
        }

        if (proteinRange != null) {

            if (exonTranscriptRange.contains(proteinRange.getMaximum()) && transcriptPosition > proteinRange.getMaximum()) {
                transcriptPosition = locatedVariant.getPosition() - exonContigRange.getMinimum() + proteinRange.getMinimum();

                if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    transcriptPosition = exonTranscriptRange.getMaximum() - (locatedVariant.getPosition() - exonContigRange.getMinimum());
                    // transcriptPosition = proteinRange.getMinimum() + (exonContigRange.getMaximum() - locatedVariant.getPosition());
                }

            }

            if (exonTranscriptRange.contains(proteinRange.getMinimum()) && transcriptPosition < proteinRange.getMinimum()) {
                transcriptPosition = proteinRange.getMinimum() + (exonContigRange.getMaximum() - locatedVariant.getPosition());

                if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
                    transcriptPosition = exonTranscriptRange.getMinimum() + (exonContigRange.getMaximum() - locatedVariant.getPosition());
                }
            }

        }

        if (StringUtils.isNotEmpty(transcriptMapsExons.getGap())) {

            // one based
            Pair<AtomicInteger, AtomicInteger> currentRefReadPair = Pair.of(new AtomicInteger(exonContigRange.getMinimum() - 1),
                    new AtomicInteger(exonTranscriptRange.getMinimum() - 1));

            List<Pair<CIGARType, Integer>> blockList = parseGap(transcriptMapsExons.getGap());
            blockLoop: for (Pair<CIGARType, Integer> block : blockList) {
                for (int i = 0; i < block.getRight(); i++) {
                    switch (block.getLeft()) {
                        case MATCH:
                            currentRefReadPair.getLeft().incrementAndGet();
                            currentRefReadPair.getRight().incrementAndGet();
                            break;
                        case INSERT:
                            currentRefReadPair.getRight().incrementAndGet();
                            break;
                        case DELETION:
                            currentRefReadPair.getLeft().incrementAndGet();
                            break;
                    }

                    if (locatedVariant.getPosition().intValue() == currentRefReadPair.getLeft().get()) {
                        break blockLoop;
                    }
                }

            }

            transcriptPosition = currentRefReadPair.getRight().get();

        }

        return transcriptPosition;
    }

    private List<Pair<CIGARType, Integer>> parseGap(String gap) {
        List<Pair<CIGARType, Integer>> ret = new LinkedList<>();
        String[] gapTokens = gap.split(" ");
        for (String gapToken : gapTokens) {
            try {
                String typeValue = gapToken.substring(0, 1);
                String length = gapToken.substring(1, gapToken.length());
                CIGARType type = Arrays.asList(CIGARType.values()).stream().filter(a -> a.getName().equals(typeValue)).findFirst()
                        .orElse(null);
                ret.add(Pair.of(type, Integer.valueOf(length)));
            } catch (NumberFormatException e) {
                // don't care about NFE
            }
        }
        return ret;
    }

    protected Integer getCodingSequencePosition(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons,
            Integer transcriptPosition, Range<Integer> proteinRange) {
        Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
        Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();
        Integer ret = null;
        switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
            case "+":
                if (proteinRange.contains(transcriptPosition)) {
                    ret = transcriptPosition - proteinRange.getMinimum() + 1;
                } else {
                    ret = transcriptMapsExonsTranscriptRange.getMaximum() - proteinRange.getMinimum() + locatedVariant.getPosition()
                            - transcriptMapsExons.getContigEnd() + 1;
                }
                break;
            case "-":
                if (proteinRange.contains(transcriptPosition)) {
                    // ret = transcriptMapsExonsTranscriptRange.getMaximum() - proteinRange.getMinimum()
                    // - (locatedVariant.getPosition() - transcriptMapsExons.getContigEnd()) + 1;
                    ret = transcriptPosition - proteinRange.getMinimum() + 1;
                } else {
                    ret = (transcriptMapsExonsContigRange.getMaximum() - locatedVariant.getPosition() + 1);
                }
                break;
        }
        return ret;
    }

    protected Pair<String, String> getDNASequenceParts(String variantType, String strand, String transcriptDNASequence,
            Range<Integer> proteinRange, Integer codingSequencePosition, String refAllele, String altAllele) {

        String originalDNASeq = transcriptDNASequence.substring(proteinRange.getMinimum() - 1, proteinRange.getMaximum());

        if ("del".equals(variantType)) {

            if ("-".equals(strand)) {

                if ((codingSequencePosition - refAllele.length()) < 0) {
                    originalDNASeq = transcriptDNASequence.substring(proteinRange.getMinimum() - refAllele.length(),
                            proteinRange.getMaximum());
                    return Pair.of(originalDNASeq.substring(0, codingSequencePosition),
                            originalDNASeq.substring(codingSequencePosition + 1, originalDNASeq.length()));
                }

                return Pair.of(originalDNASeq.substring(0, codingSequencePosition - refAllele.length()),
                        originalDNASeq.substring(codingSequencePosition, originalDNASeq.length()));
            }

            if ((codingSequencePosition + refAllele.length() - 1) > originalDNASeq.length()) {
                originalDNASeq = transcriptDNASequence.substring(proteinRange.getMinimum() - 1,
                        proteinRange.getMaximum() + refAllele.length());
                return Pair.of(originalDNASeq.substring(0, codingSequencePosition),
                        originalDNASeq.substring(codingSequencePosition + refAllele.length(), originalDNASeq.length()));
            }

            return Pair.of(originalDNASeq.substring(0, codingSequencePosition - 1),
                    originalDNASeq.substring(codingSequencePosition + refAllele.length() - 1, originalDNASeq.length()));

        }

        if ("ins".equals(variantType)) {

            if ("-".equals(strand)) {
                return Pair.of(originalDNASeq.substring(0, codingSequencePosition - 1),
                        originalDNASeq.substring(codingSequencePosition - 1, originalDNASeq.length()));
            }

            if ((codingSequencePosition + altAllele.length()) > originalDNASeq.length()) {
                originalDNASeq = transcriptDNASequence.substring(proteinRange.getMinimum() - 1,
                        proteinRange.getMaximum() + altAllele.length());
                return Pair.of(originalDNASeq.substring(0, codingSequencePosition - 1),
                        originalDNASeq.substring(codingSequencePosition + altAllele.length() - 1, originalDNASeq.length()));
            }

            return Pair.of(originalDNASeq.substring(0, codingSequencePosition),
                    originalDNASeq.substring(codingSequencePosition, originalDNASeq.length()));
        }

        if ("sub".equals(variantType)) {

            if ("-".equals(strand)) {
                return Pair.of(originalDNASeq.substring(0, codingSequencePosition - refAllele.length()),
                        originalDNASeq.substring(codingSequencePosition, originalDNASeq.length()));
            }

            return Pair.of(originalDNASeq.substring(0, codingSequencePosition - 1),
                    originalDNASeq.substring(codingSequencePosition + refAllele.length() - 1, originalDNASeq.length()));
        }

        if ("snp".equals(variantType)) {

            if ("-".equals(strand)) {
                return Pair.of(originalDNASeq.substring(0, codingSequencePosition - refAllele.length()),
                        originalDNASeq.substring(codingSequencePosition, originalDNASeq.length()));
            }

            return Pair.of(originalDNASeq.substring(0, codingSequencePosition - 1),
                    originalDNASeq.substring(codingSequencePosition, originalDNASeq.length()));
        }

        return Pair.of(originalDNASeq.substring(0, codingSequencePosition - 1),
                originalDNASeq.substring(codingSequencePosition + refAllele.length() - 1, originalDNASeq.length()));

    }

    protected Integer getAminoAcidStart(String variantType, Integer codingSequencePosition, Boolean frameshift, Boolean inframe,
            String refAllele, String strand) {

        if ("snp".equals(variantType)) {

            if (frameshift && !inframe) {
                return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue();
            }

            return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue();
        }

        if ("sub".equals(variantType)) {
            if ("-".equals(strand)) {

                if (!frameshift && !inframe) {
                    return Double.valueOf(Math.ceil((codingSequencePosition - 1) / 3D)).intValue();
                }

                if (!frameshift && inframe) {
                    return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3) + 1;
                }

                if (frameshift && !inframe) {
                    return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3) + 1;
                }

            } else {

                if (frameshift && inframe) {
                    return Double.valueOf(Math.ceil((codingSequencePosition) / 3D)).intValue();
                }

                if (!frameshift && inframe) {
                    return Double.valueOf(Math.ceil((codingSequencePosition) / 3D)).intValue();
                }

                if (!frameshift && !inframe) {
                    return Double.valueOf(Math.ceil((codingSequencePosition) / 3D)).intValue();
                }

                return Double.valueOf(Math.ceil((codingSequencePosition - 1) / 3D)).intValue();
            }
        }

        if ("del".equals(variantType)) {

            if ("-".equals(strand)) {

                if (!frameshift && !inframe) {
                    return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3);
                }

                if (!frameshift && inframe) {
                    return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3) + 1;
                }

                if (frameshift && !inframe) {
                    return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3) + 1;
                    // return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3);
                }

            } else {
                return Math.max(1, Double.valueOf(Math.ceil((codingSequencePosition - 1) / 3D)).intValue());
            }

        }

        if ("ins".equals(variantType)) {

            if ("-".equals(strand)) {

                if (!frameshift && !inframe) {
                    return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3);
                }

                if (!frameshift && inframe) {
                    return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3);
                }

                if (frameshift && !inframe) {
                    return Double.valueOf(Math.ceil(codingSequencePosition / 3D)).intValue() - (refAllele.length() / 3);
                }

            } else {

                if (frameshift && !inframe) {
                    return Double.valueOf(Math.ceil((codingSequencePosition - 1) / 3D)).intValue() - (refAllele.length() / 3);
                }

                return Double.valueOf(Math.ceil((codingSequencePosition) / 3D)).intValue();
            }

        }
        return null;
    }

    protected Boolean isInframe(DNASequence originalDNASequence, Integer codingSequencePosition, LocatedVariant locatedVariant) {
        // I have to believe there is a better way to do this...
        Boolean ret = Boolean.FALSE;
        int count = 0;
        List<String> setOfThree = new ArrayList<>(3);
        for (NucleotideCompound nc : originalDNASequence.getAsList()) {
            count++;
            setOfThree.add(nc.getBase());
            if (setOfThree.size() == 3) {
                setOfThree.clear();
            }
            if (count == codingSequencePosition) {

                if ("sub".equals(locatedVariant.getVariantType().getId())) {
                    if (locatedVariant.getRef().length() == locatedVariant.getSeq().length()) {
                        ret = Boolean.TRUE;
                        break;
                    }
                } else if ("ins".equals(locatedVariant.getVariantType().getId())) {
                    if ((setOfThree.size() + (locatedVariant.getSeq().length() / 3)) <= 3) {
                        ret = Boolean.TRUE;
                        break;
                    }
                } else if (setOfThree.size() + (locatedVariant.getEndPosition() - locatedVariant.getPosition()) <= 3) {
                    ret = Boolean.TRUE;
                    break;
                }
            }
            if (count > codingSequencePosition) {
                break;
            }
        }
        return ret;
    }

}
