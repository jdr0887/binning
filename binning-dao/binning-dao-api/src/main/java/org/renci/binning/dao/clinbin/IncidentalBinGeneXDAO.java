package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinGeneX;
import org.renci.binning.dao.clinbin.model.IncidentalBinGeneXPK;

public interface IncidentalBinGeneXDAO extends BaseDAO<IncidentalBinGeneX, IncidentalBinGeneXPK> {

    public List<IncidentalBinGeneX> findByIncidentalBinIdAndVersionAndZygosityModes(Integer incidentalBinId, Integer incidentalBinVersion,
            List<String> zygosityModeList) throws BinningDAOException;

    public List<IncidentalBinGeneX> findByIncidentalBinIdAndVersion(Integer incidentalBinId, Integer incidentalBinVersion)
            throws BinningDAOException;

}
