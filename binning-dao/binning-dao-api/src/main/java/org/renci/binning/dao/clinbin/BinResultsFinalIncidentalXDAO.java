package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalX;

public interface BinResultsFinalIncidentalXDAO extends BaseDAO<BinResultsFinalIncidentalX, Long> {

    public List<BinResultsFinalIncidentalX> findByParticipantAndIncidentalBinIdAndResultVersion(String participant, Integer incidentalBinId,
            Integer version) throws BinningDAOException;

    public List<BinResultsFinalIncidentalX> findByParticipantAndIncidentalBinIdAndResultVersionAndCarrierStatusId(String participant,
            Integer incidentalBinId, Integer version, Integer carrierStatusId) throws BinningDAOException;

}
