package org.renci.canvas.binning.core;

import java.util.List;

import org.apache.commons.lang3.Range;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.refseq.model.LocationType;
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

    protected LocationType getLocationType(CANVASDAOBeanService daoBean, Range<Integer> locatedVariantRange,
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

    protected Integer getTranscriptPosition(LocatedVariant locatedVariant, TranscriptMapsExons transcriptMapsExons) {
        Integer ret = null;
        switch (transcriptMapsExons.getTranscriptMaps().getStrand()) {
            case "+":
                // ret = transcriptMapsExons.getTranscriptEnd() + (locatedVariant.getPosition() - transcriptMapsExons.getContigEnd());
                ret = (locatedVariant.getPosition() - transcriptMapsExons.getContigStart()) + transcriptMapsExons.getTranscriptStart();
                break;
            case "-":
                // ret = transcriptMapsExons.getTranscriptEnd() + (transcriptMapsExons.getContigEnd() - locatedVariant.getPosition());
                ret = transcriptMapsExons.getTranscriptStart() - (locatedVariant.getPosition() - transcriptMapsExons.getContigStart());
                break;
        }
        return ret;
    }

}
