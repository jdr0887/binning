package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DXCoverage;
import org.renci.binning.dao.clinbin.model.DXCoveragePK;

public interface DXCoverageDAO extends BaseDAO<DXCoverage, DXCoveragePK> {

    public List<DXCoverage> findByDXIdAndParticipantAndListVersion(Long dxId, String participant, Integer listVersion)
            throws BinningDAOException;

    public List<DXCoverage> findByParticipantAndListVersion(String participant, Integer listVersion) throws BinningDAOException;

    public DXCoveragePK save(DXCoverage dxCoverage) throws BinningDAOException;

}
