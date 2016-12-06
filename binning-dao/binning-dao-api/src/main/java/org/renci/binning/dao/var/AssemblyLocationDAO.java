package org.renci.binning.dao.var;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.AssemblyLocation;
import org.renci.binning.dao.var.model.AssemblyLocationPK;

public interface AssemblyLocationDAO extends BaseDAO<AssemblyLocation, AssemblyLocationPK> {

    public AssemblyLocationPK save(AssemblyLocation entity) throws BinningDAOException;

    public void delete(AssemblyLocation entity) throws BinningDAOException;

}
