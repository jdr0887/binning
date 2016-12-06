package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiseaseClass;

public interface DiseaseClassDAO extends BaseDAO<DiseaseClass, Integer> {

    public List<DiseaseClass> findAll() throws BinningDAOException;

}
