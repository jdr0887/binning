package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;

public interface IncidentalBinningJobDAO extends BaseDAO<IncidentalBinningJob, Integer> {

    public List<IncidentalBinningJob> findAvailableJobs() throws BinningDAOException;

    public List<IncidentalBinningJob> findCompletedJobs() throws BinningDAOException;

    public List<IncidentalBinningJob> findCompletedJobsByStudy(String study) throws BinningDAOException;

    public List<IncidentalBinningJob> findByExample(IncidentalBinningJob binningJob) throws BinningDAOException;

    public Integer save(IncidentalBinningJob entity) throws BinningDAOException;

}
