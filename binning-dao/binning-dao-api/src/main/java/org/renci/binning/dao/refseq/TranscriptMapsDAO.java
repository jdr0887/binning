package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.TranscriptMaps;

public interface TranscriptMapsDAO extends BaseDAO<TranscriptMaps, Integer> {

    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersionAndTranscriptId(Integer genomeRefId, String refSeqVersion,
            String versionId) throws BinningDAOException;

    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersionAndTranscriptId(String fetchGroup, Integer genomeRefId,
            String refSeqVersion, String versionId) throws BinningDAOException;

    public List<TranscriptMaps> findByTranscriptId(String versionId) throws BinningDAOException;

    public List<TranscriptMaps> findByTranscriptId(String fetchGroup, String versionId) throws BinningDAOException;

    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersion(Integer genomeRefId, String refSeqVersion) throws BinningDAOException;

    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersion(String fetchGroup, Integer genomeRefId, String refSeqVersion)
            throws BinningDAOException;

    public TranscriptMaps findById(String fetchGroup, Integer id) throws BinningDAOException;

    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(Integer genomeRefId,
            String refseqVersion, String refSeqAccession, Integer position) throws BinningDAOException;

}
