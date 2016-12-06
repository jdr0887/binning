package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DXExons;

public interface DXExonsDAO extends BaseDAO<DXExons, Integer> {

    public Integer findMaxListVersion() throws BinningDAOException;

    public List<DXExons> findByListVersion(Integer listVersion) throws BinningDAOException;

    public List<DXExons> findByListVersionAndChromosome(Integer listVersion, String chromosome) throws BinningDAOException;

    public List<DXExons> findByListVersionAndChromosomeAndRange(Integer listVersion, String chromosome, Integer start, Integer end)
            throws BinningDAOException;

}
