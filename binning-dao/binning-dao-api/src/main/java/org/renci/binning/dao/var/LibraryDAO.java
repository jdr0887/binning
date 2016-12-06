package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.Library;

public interface LibraryDAO extends BaseDAO<Library, Integer> {

    public Integer save(Library entity) throws BinningDAOException;

    public void delete(Library entity) throws BinningDAOException;

    public List<Library> findByNameAndSampleId(String name, Integer sampleId) throws BinningDAOException;

}
