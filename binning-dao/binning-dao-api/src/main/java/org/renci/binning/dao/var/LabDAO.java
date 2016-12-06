package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.Lab;

public interface LabDAO extends BaseDAO<Lab, String> {

    public String save(Lab entity) throws BinningDAOException;

    public void delete(Lab entity) throws BinningDAOException;

    public List<Lab> findByName(String name) throws BinningDAOException;

}
