package org.renci.binning.dao.genome1k;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.model.OneThousandGenomeIndelFrequency;

public interface OneThousandGenomeIndelFrequencyDAO extends BaseDAO<OneThousandGenomeIndelFrequency, Long> {

    public List<OneThousandGenomeIndelFrequency> findByLocatedVariantIdAndVersion(Long locVarId, Integer version)
            throws BinningDAOException;

}
