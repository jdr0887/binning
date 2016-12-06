package org.renci.binning.dao.esp;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.esp.model.ESPSNPFrequencyPopulation;

public interface ESPSNPFrequencyPopulationDAO extends BaseDAO<ESPSNPFrequencyPopulation, Long> {

    public List<ESPSNPFrequencyPopulation> findByLocatedVariantIdAndVersion(Long locVarId, Integer version) throws BinningDAOException;

}
