package org.renci.binning.dao.clinbin;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.NCGenesFrequencies;
import org.renci.binning.dao.clinbin.model.NCGenesFrequenciesPK;

public interface NCGenesFrequenciesDAO extends BaseDAO<NCGenesFrequencies, NCGenesFrequenciesPK> {

    public Integer findMaxVersion() throws BinningDAOException;

}
