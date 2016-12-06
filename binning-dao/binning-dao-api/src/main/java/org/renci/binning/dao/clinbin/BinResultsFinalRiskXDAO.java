package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskX;

public interface BinResultsFinalRiskXDAO extends BaseDAO<BinResultsFinalRiskX, Long> {

    public List<BinResultsFinalRiskX> findByParticipantAndIndicentalBinIdAndResultVersion(String participant, Integer incidentalBinId,
            Integer resultVersion) throws BinningDAOException;

}
