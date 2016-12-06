package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.Feature;

public interface FeatureDAO extends BaseDAO<Feature, Long> {

    public List<Feature> findByRefSeqVersionAndTranscriptId(String refSeqVersion, String transcriptId) throws BinningDAOException;

    public List<Feature> findByRefSeqCodingSequenceId(Integer refSeqCodingSequenceId) throws BinningDAOException;
}
