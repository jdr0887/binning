package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.RefSeqGene;

public interface RefSeqGeneDAO extends BaseDAO<RefSeqGene, Long> {

    public List<RefSeqGene> findByRefSeqVersion(String refSeqVersion) throws BinningDAOException;

    public List<RefSeqGene> findByRefSeqVersionAndTranscriptId(String refSeqVersion, String transcriptId) throws BinningDAOException;

    public List<RefSeqGene> findByTranscriptId(String transcriptId) throws BinningDAOException;

}
