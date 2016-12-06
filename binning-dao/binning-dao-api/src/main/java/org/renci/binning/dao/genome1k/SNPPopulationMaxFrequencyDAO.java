package org.renci.binning.dao.genome1k;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.model.SNPPopulationMaxFrequency;
import org.renci.binning.dao.genome1k.model.SNPPopulationMaxFrequencyPK;

public interface SNPPopulationMaxFrequencyDAO extends BaseDAO<SNPPopulationMaxFrequency, SNPPopulationMaxFrequencyPK> {

    public List<SNPPopulationMaxFrequency> findByLocatedVariantId(Long locVarId) throws BinningDAOException;

    public List<SNPPopulationMaxFrequency> findByLocatedVariantIdAndVersion(Long locVarId, Integer version) throws BinningDAOException;

}
