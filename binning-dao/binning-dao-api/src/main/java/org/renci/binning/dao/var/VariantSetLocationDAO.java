package org.renci.binning.dao.var;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.VariantSetLocation;
import org.renci.binning.dao.var.model.VariantSetLocationPK;

public interface VariantSetLocationDAO extends BaseDAO<VariantSetLocation, VariantSetLocationPK> {

    public VariantSetLocationPK save(VariantSetLocation entity) throws BinningDAOException;

    public void delete(VariantSetLocation entity) throws BinningDAOException;

}
