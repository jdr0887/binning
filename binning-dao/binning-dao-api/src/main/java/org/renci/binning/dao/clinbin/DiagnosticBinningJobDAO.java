package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;

public interface DiagnosticBinningJobDAO extends BaseDAO<DiagnosticBinningJob, Integer> {

    public List<DiagnosticBinningJob> findAvailableJobs() throws BinningDAOException;

    public List<DiagnosticBinningJob> findCompletedJobs() throws BinningDAOException;

    public List<DiagnosticBinningJob> findCompletedJobsByStudy(String study) throws BinningDAOException;

    public Integer save(DiagnosticBinningJob entity) throws BinningDAOException;

}
