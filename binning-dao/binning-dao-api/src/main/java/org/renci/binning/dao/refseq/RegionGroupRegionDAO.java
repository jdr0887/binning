package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.RegionGroupRegion;

public interface RegionGroupRegionDAO extends BaseDAO<RegionGroupRegion, Integer> {

    public List<RegionGroupRegion> findByRegionGroupId(Integer regionGroupId) throws BinningDAOException;

    public List<RegionGroupRegion> findByRefSeqCodingSequenceId(Integer refSeqCodingSequenceId) throws BinningDAOException;

}
