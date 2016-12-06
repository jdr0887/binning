package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.RefSeqCodingSequence;

public interface RefSeqCodingSequenceDAO extends BaseDAO<RefSeqCodingSequence, Long> {

    public List<RefSeqCodingSequence> findByVersion(String version) throws BinningDAOException;

    public List<RefSeqCodingSequence> findByRefSeqVersionAndTranscriptId(String refSeqVersion, String transcriptId)
            throws BinningDAOException;

    public List<RefSeqCodingSequence> findByRefSeqVersionAndTranscriptId(String fetchPlan, String refSeqVersion, String transcriptId)
            throws BinningDAOException;

}
