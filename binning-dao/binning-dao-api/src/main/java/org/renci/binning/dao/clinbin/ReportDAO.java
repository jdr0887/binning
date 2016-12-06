package org.renci.binning.dao.clinbin;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.Report;
import org.renci.binning.dao.clinbin.model.ReportPK;

public interface ReportDAO extends BaseDAO<Report, ReportPK> {

    public ReportPK save(Report report) throws BinningDAOException;

}
