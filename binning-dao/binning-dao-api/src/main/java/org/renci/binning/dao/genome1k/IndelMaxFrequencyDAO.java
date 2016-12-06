package org.renci.binning.dao.genome1k;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.model.IndelMaxFrequency;
import org.renci.binning.dao.genome1k.model.IndelMaxFrequencyPK;

public interface IndelMaxFrequencyDAO extends BaseDAO<IndelMaxFrequency, IndelMaxFrequencyPK> {

    public List<IndelMaxFrequency> findByLocatedVariantId(Long locVarId) throws BinningDAOException;

    public List<IndelMaxFrequency> findByLocatedVariantIdAndVersion(Long locVarId, Integer version) throws BinningDAOException;

}
