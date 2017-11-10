package org.renci.canvas.binning.core;

import java.util.List;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
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
                    ret = String.format("%s:%s.%d%s%s>%s", accession, accessionType, position, String.format("%+d", intronExonDistance),
                            ref, alt);
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

    protected String getLocationType(CANVASDAOBeanService daoBean, Range<Integer> locatedVariantRange,
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
        return locationTypeValue;
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

    protected Integer getIntronExonDistance(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons,
            List<TranscriptMapsExons> transcriptMapsExonsList, Range<Integer> proteinRange, Integer transcriptPosition) {

        Range<Integer> locatedVariantRange = locatedVariant.toRange();
        Range<Integer> transcriptMapsExonsContigRange = transcriptMapsExons.getContigRange();
        Range<Integer> transcriptMapsExonsTranscriptRange = transcriptMapsExons.getTranscriptRange();
        Range<Integer> proteinExonIntersection = proteinRange.intersectionWith(transcriptMapsExonsTranscriptRange);

        Integer exonIndex = transcriptMapsExonsList.indexOf(transcriptMapsExons);

        if ("+".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {

            if (exonIndex == 0) {

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
                    return transcriptPosition - proteinExonIntersection.getMinimum() + 1;
                }

            }

            if (exonIndex == transcriptMapsExonsList.size() - 1) {

                if (proteinRange.isBefore(transcriptPosition)) {
                    // we are in UTR3 region
                    return transcriptPosition - proteinExonIntersection.getMaximum();
                }

                if (proteinRange.contains(transcriptPosition)) {
                    if (transcriptMapsExonsTranscriptRange.getMaximum() == proteinRange.getMaximum()) {
                        // last interval is an exon
                        return transcriptPosition - proteinExonIntersection.getMinimum() + 1;
                    }
                    return transcriptPosition - proteinExonIntersection.getMaximum() - 1;
                }

            }

            Integer right = transcriptPosition - proteinExonIntersection.getMaximum() - 1;
            Integer left = transcriptPosition - proteinExonIntersection.getMinimum() + 1;
            return Math.abs(left) < Math.abs(right) ? left : right;

        }

        if ("-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {

            if (exonIndex == 0) {

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

                    Integer leftDistance = transcriptPosition - transcriptMapsExonsTranscriptRange.getMinimum() + 1;
                    Integer rightDistance = transcriptPosition - proteinExonIntersection.getMaximum() - 1;

                    Integer shortestDistance = Math.min(Math.abs(leftDistance), Math.abs(rightDistance));

                    if (Math.abs(leftDistance) == shortestDistance) {
                        return leftDistance;
                    }
                    return rightDistance;
                }

            }
            // if ("del".equals(variant.getLocatedVariant().getVariantType().getId())
            // && "-".equals(transcriptMapsExons.getTranscriptMaps().getStrand())) {
            // variant.setIntronExonDistance(
            // Math.abs(locatedVariantRange.getMaximum() - transcriptMapsExonsContigRange.getMaximum() - 2));
            // }

            if (exonIndex == transcriptMapsExonsList.size() - 1) {

                if (proteinRange.isBefore(transcriptPosition)) {
                    // we are in UTR3 region
                    return transcriptPosition - proteinExonIntersection.getMaximum();
                }

                if (proteinRange.contains(transcriptPosition)) {
                    if (transcriptMapsExonsTranscriptRange.getMaximum() == proteinRange.getMaximum()) {
                        // last interval is an exon
                        return transcriptPosition - proteinExonIntersection.getMinimum() + 1;
                    }
                    return transcriptPosition - proteinExonIntersection.getMaximum() - 1;
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

    protected Integer getTranscriptPosition(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons) {
        Integer ret = null;
        switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
            case "+":
                ret = transcriptMapsExons.getTranscriptEnd() + (locatedVariant.getPosition() - transcriptMapsExons.getContigEnd());
                // ret = (locatedVariant.getPosition() - transcriptMapsExons.getContigStart()) + transcriptMapsExons.getTranscriptStart();
                break;
            case "-":
                ret = transcriptMapsExons.getTranscriptEnd() + (transcriptMapsExons.getContigEnd() - locatedVariant.getPosition());
                // ret = transcriptMapsExons.getTranscriptStart() + (transcriptMapsExons.getContigStart() - locatedVariant.getPosition());
                // ret = transcriptMapsExons.getTranscriptStart() - (locatedVariant.getPosition() - transcriptMapsExons.getContigStart());
                break;
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
                    ret = transcriptMapsExonsTranscriptRange.getMaximum() - proteinRange.getMinimum() + locatedVariant.getPosition()
                            - transcriptMapsExons.getContigEnd() + 1;
                } else {
                    ret = transcriptMapsExonsTranscriptRange.getMaximum() - proteinRange.getMinimum() + locatedVariant.getPosition()
                            - transcriptMapsExons.getContigEnd() + 1;
                }
                break;
            case "-":
                if (proteinRange.contains(transcriptPosition)) {
                    ret = transcriptMapsExonsTranscriptRange.getMaximum() - proteinRange.getMinimum()
                            - (locatedVariant.getPosition() - transcriptMapsExons.getContigEnd()) + 1;
                    // ret = transcriptPosition - proteinRange.getMinimum() + 1;
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

            return Pair.of(originalDNASeq.substring(0, codingSequencePosition),
                    originalDNASeq.substring(codingSequencePosition + refAllele.length(), originalDNASeq.length()));
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
                }

            } else {
                return Double.valueOf(Math.ceil((codingSequencePosition - 1) / 3D)).intValue();
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

}
