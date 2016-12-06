package org.renci.binning.dao.clinbin;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalResultVersionX;

public interface IncidentalResultVersionXDAO extends BaseDAO<IncidentalResultVersionX, Integer> {

    public Integer findMaxResultVersion() throws BinningDAOException;

}
