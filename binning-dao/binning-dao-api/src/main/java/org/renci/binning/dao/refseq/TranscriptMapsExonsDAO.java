package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.TranscriptMapsExons;

public interface TranscriptMapsExonsDAO extends BaseDAO<TranscriptMapsExons, Integer> {

    public List<TranscriptMapsExons> findByGenomeRefIdAndRefSeqVersion(Integer genomeRefId, String refSeqVersion) throws BinningDAOException;

    public List<TranscriptMapsExons> findByGenomeRefIdAndRefSeqVersionAndAccession(Integer genomeRefId, String refSeqVersion,
            String accession) throws BinningDAOException;

    public List<TranscriptMapsExons> findByTranscriptMapsId(Integer id) throws BinningDAOException;

    public List<TranscriptMapsExons> findByTranscriptVersionIdAndTranscriptMapsMapCount(String versionId, Integer mapCount)
            throws BinningDAOException;

    public List<TranscriptMapsExons> findByGenomeRefSeqAccessionAndInExonRange(String refSeqAccession, Integer start)
            throws BinningDAOException;

    public Integer findMaxContig(Integer genomeRefId, String refSeqVersion, String refSeqAccession, Integer transcriptMapId)
            throws BinningDAOException;

}
