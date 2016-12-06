package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinX;

public interface IncidentalBinXDAO extends BaseDAO<IncidentalBinX, Integer> {

    public List<IncidentalBinX> findByHGMDVersion(Integer hgmdVersion) throws BinningDAOException;

}
