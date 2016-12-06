package org.renci.binning.dao.genome1k;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.model.OneThousandGenomeSNPFrequencyPopulation;

public interface OneThousandGenomeSNPFrequencyPopulationDAO extends BaseDAO<OneThousandGenomeSNPFrequencyPopulation, Long> {

    public List<OneThousandGenomeSNPFrequencyPopulation> findByLocatedVariantIdAndVersion(Long locVarId, Integer version)
            throws BinningDAOException;

}
