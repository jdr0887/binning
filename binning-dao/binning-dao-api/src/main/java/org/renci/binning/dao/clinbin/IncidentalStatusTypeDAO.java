package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalStatusType;

public interface IncidentalStatusTypeDAO extends BaseDAO<IncidentalStatusType, String> {

    public List<IncidentalStatusType> findAll() throws BinningDAOException;

}
