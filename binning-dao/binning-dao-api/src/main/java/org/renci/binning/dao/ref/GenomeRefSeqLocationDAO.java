package org.renci.binning.dao.ref;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.ref.model.GenomeRefSeqLocation;

public interface GenomeRefSeqLocationDAO extends BaseDAO<GenomeRefSeqLocation, String> {

    public List<GenomeRefSeqLocation> findByRefIdAndVersionedAccesionAndPosition(Integer refId, String refVerAccession, Integer position)
            throws BinningDAOException;

}
