package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.MaxFrequency;
import org.renci.binning.dao.clinbin.model.MaxFrequencyPK;

public interface MaxFrequencyDAO extends BaseDAO<MaxFrequency, MaxFrequencyPK> {

    public List<MaxFrequency> findByGeneNameAndMaxAlleleFrequency(String name, Double threshold) throws BinningDAOException;

    public List<MaxFrequency> findByLocatedVariantId(Long locatedVariantId) throws BinningDAOException;

    public MaxFrequencyPK save(MaxFrequency maxFrequency) throws BinningDAOException;

}
