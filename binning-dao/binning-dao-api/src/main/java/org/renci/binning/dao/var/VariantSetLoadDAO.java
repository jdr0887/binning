package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.VariantSetLoad;

public interface VariantSetLoadDAO extends BaseDAO<VariantSetLoad, Integer> {

    public Integer save(VariantSetLoad vsl) throws BinningDAOException;

    public List<VariantSetLoad> findByExample(VariantSetLoad variantSetLoad) throws BinningDAOException;

}
