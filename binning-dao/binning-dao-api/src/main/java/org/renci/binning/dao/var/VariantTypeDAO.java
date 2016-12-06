package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.VariantType;

public interface VariantTypeDAO extends BaseDAO<VariantType, String> {

    public List<VariantType> findByName(String name) throws BinningDAOException;

    public String save(VariantType entity) throws BinningDAOException;

}
