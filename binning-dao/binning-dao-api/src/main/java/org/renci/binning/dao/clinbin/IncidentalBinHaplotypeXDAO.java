package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinHaplotypeX;
import org.renci.binning.dao.clinbin.model.IncidentalBinHaplotypeXPK;

public interface IncidentalBinHaplotypeXDAO extends BaseDAO<IncidentalBinHaplotypeX, IncidentalBinHaplotypeXPK> {

    public List<IncidentalBinHaplotypeX> findAll() throws BinningDAOException;

    public List<IncidentalBinHaplotypeX> findByIncidentalBinIdAndVersionAndAssemblyIdAndHGMDVersionAndZygosityMode(Integer id,
            Integer version, Integer assemblyId, Integer hgmdVersion, List<String> zygosityModes) throws BinningDAOException;

    public List<IncidentalBinHaplotypeX> findByIncidentalBinIdAndVersion(Integer id, Integer version) throws BinningDAOException;

}
