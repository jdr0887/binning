package org.renci.binning.dao.var;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQCPK;

public interface AssemblyLocatedVariantQCDAO extends BaseDAO<AssemblyLocatedVariantQC, AssemblyLocatedVariantQCPK> {

    public AssemblyLocatedVariantQCPK save(AssemblyLocatedVariantQC entity) throws BinningDAOException;

    public void delete(AssemblyLocatedVariantQC entity) throws BinningDAOException;

    public void deleteByAssemblyId(Integer assemblyId) throws BinningDAOException;

}
