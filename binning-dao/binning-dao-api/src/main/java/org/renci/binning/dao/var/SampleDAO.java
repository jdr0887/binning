package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.Sample;

public interface SampleDAO extends BaseDAO<Sample, Integer> {

    public Integer save(Sample entity) throws BinningDAOException;

    public void delete(Sample entity) throws BinningDAOException;

    public List<Sample> findByNameAndProjectName(String name, String projectName) throws BinningDAOException;

    public List<Sample> findByName(String name) throws BinningDAOException;

}
