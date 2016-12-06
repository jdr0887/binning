package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.VariantSet;

public interface VariantSetDAO extends BaseDAO<VariantSet, Integer> {

    public Integer save(VariantSet entity) throws BinningDAOException;

    public void delete(VariantSet entity) throws BinningDAOException;

    public List<VariantSet> findByAssemblyId(Integer assemblyId) throws BinningDAOException;

}
