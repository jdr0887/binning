package org.renci.binning.dao.genome1k;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.model.OneThousandGenomeSNPFrequencySubpopulation;

public interface OneThousandGenomeSNPFrequencySubpopulationDAO extends BaseDAO<OneThousandGenomeSNPFrequencySubpopulation, Long> {

    public List<OneThousandGenomeSNPFrequencySubpopulation> findByLocatedVariantIdAndVersion(Long locVarId, Integer version)
            throws BinningDAOException;

}
