package org.renci.binning.dao.clinbin;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticResultVersion;

public interface DiagnosticResultVersionDAO extends BaseDAO<DiagnosticResultVersion, Integer> {

    public Integer findMaxResultVersion() throws BinningDAOException;

}
