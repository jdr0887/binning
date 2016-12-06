package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantPK;

public interface AssemblyLocatedVariantDAO extends BaseDAO<AssemblyLocatedVariant, AssemblyLocatedVariantPK> {

    public AssemblyLocatedVariantPK save(AssemblyLocatedVariant entity) throws BinningDAOException;

    public void delete(AssemblyLocatedVariant entity) throws BinningDAOException;

    public List<AssemblyLocatedVariant> findByAssemblyId(Integer assemblyId) throws BinningDAOException;

    public void deleteByAssemblyId(Integer assemblyId) throws BinningDAOException;

}
