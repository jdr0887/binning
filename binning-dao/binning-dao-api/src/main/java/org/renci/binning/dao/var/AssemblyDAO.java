package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.Assembly;

public interface AssemblyDAO extends BaseDAO<Assembly, Integer> {

    public Integer save(Assembly entity) throws BinningDAOException;

    public void delete(Assembly entity) throws BinningDAOException;

    public List<Assembly> findByVariantSetId(Integer variantSetId) throws BinningDAOException;

    public List<Assembly> findBySampleName(String name) throws BinningDAOException;

    public List<Assembly> findByLibraryId(Integer id) throws BinningDAOException;

}
