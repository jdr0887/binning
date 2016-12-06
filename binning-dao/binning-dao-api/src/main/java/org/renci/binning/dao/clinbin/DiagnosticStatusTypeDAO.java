package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticStatusType;

public interface DiagnosticStatusTypeDAO extends BaseDAO<DiagnosticStatusType, String> {

    public List<DiagnosticStatusType> findAll() throws BinningDAOException;

}
