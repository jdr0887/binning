package org.renci.binning.dao.exac;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.exac.model.MaxVariantFrequency;

public interface MaxVariantFrequencyDAO extends BaseDAO<MaxVariantFrequency, Long> {

    public List<MaxVariantFrequency> findByLocatedVariantIdAndVersion(Long locVarId, String version) throws BinningDAOException;

    public List<MaxVariantFrequency> findByLocatedVariantId(Long locVarId) throws BinningDAOException;

    public List<MaxVariantFrequency> findByLocatedVariantIdAndFrequencyThreshold(Long locVarId, Double threshold)
            throws BinningDAOException;

    public List<MaxVariantFrequency> findByGeneNameAndMaxAlleleFrequency(String name, Double threshold) throws BinningDAOException;

}
