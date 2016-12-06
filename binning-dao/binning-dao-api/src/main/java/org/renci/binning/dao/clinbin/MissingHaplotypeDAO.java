package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.MissingHaplotype;
import org.renci.binning.dao.clinbin.model.MissingHaplotypePK;

public interface MissingHaplotypeDAO extends BaseDAO<MissingHaplotype, MissingHaplotypePK> {

    public List<MissingHaplotype> findByParticipantAndIncidentalBinIdAndListVersion(String participantId, Integer incidentalBinId,
            Integer listVersion) throws BinningDAOException;

    public MissingHaplotypePK save(MissingHaplotype missingHaplotype) throws BinningDAOException;

}
