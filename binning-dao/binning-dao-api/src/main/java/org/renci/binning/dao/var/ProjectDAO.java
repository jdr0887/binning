package org.renci.binning.dao.var;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.Project;

public interface ProjectDAO extends BaseDAO<Project, String> {

    public String save(Project entity) throws BinningDAOException;

    public void delete(Project entity) throws BinningDAOException;

}
