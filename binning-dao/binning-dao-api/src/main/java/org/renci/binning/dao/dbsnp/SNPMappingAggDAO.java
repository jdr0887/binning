package org.renci.binning.dao.dbsnp;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.dbsnp.model.SNPMappingAgg;
import org.renci.binning.dao.dbsnp.model.SNPMappingAggPK;

public interface SNPMappingAggDAO extends BaseDAO<SNPMappingAgg, SNPMappingAggPK> {

    public List<SNPMappingAgg> findByLocatedVariantId(Long locatedVariantId) throws BinningDAOException;

}
