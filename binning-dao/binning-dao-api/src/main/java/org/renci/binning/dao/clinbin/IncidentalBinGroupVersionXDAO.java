package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinGroupVersionX;
import org.renci.binning.dao.clinbin.model.IncidentalBinGroupVersionXPK;

public interface IncidentalBinGroupVersionXDAO extends BaseDAO<IncidentalBinGroupVersionX, IncidentalBinGroupVersionXPK> {

    public List<IncidentalBinGroupVersionX> findByIncidentalBinIdAndGroupVersion(Integer id, Integer groupVersion)
            throws BinningDAOException;

}
