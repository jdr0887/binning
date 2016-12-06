package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.Transcript;

public interface TranscriptDAO extends BaseDAO<Transcript, String> {

    public List<Transcript> findByGenomeRefIdAndRefSeqVersion(Integer genomeRefId, String refSeqVersion) throws BinningDAOException;

}
