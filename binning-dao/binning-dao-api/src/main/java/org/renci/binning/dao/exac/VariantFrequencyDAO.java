package org.renci.binning.dao.exac;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.exac.model.VariantFrequency;

public interface VariantFrequencyDAO extends BaseDAO<VariantFrequency, Long> {

    public List<VariantFrequency> findByLocatedVariantIdAndVersion(Long locVarId, String version) throws BinningDAOException;

}
