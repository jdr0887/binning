package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.RegionGroup;

public interface RegionGroupDAO extends BaseDAO<RegionGroup, Long> {

    public List<RegionGroup> findByRefSeqVersionAndTranscriptId(String refSeqVersion, String transcriptId) throws BinningDAOException;

}
