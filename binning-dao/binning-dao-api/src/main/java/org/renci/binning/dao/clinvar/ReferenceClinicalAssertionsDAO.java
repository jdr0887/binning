package org.renci.binning.dao.clinvar;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinvar.model.ReferenceClinicalAssertions;

public interface ReferenceClinicalAssertionsDAO extends BaseDAO<ReferenceClinicalAssertions, Long> {

    public List<ReferenceClinicalAssertions> findDiagnostic(Long dxId, String participant, Integer resultVersion)
            throws BinningDAOException;

    public List<ReferenceClinicalAssertions> findIncidental(Long incidentalBinId, String participant, Integer resultVersion)
            throws BinningDAOException;

    public List<ReferenceClinicalAssertions> findRisk(Long incidentalBinId, String participant, Integer resultVersion)
            throws BinningDAOException;

    public List<ReferenceClinicalAssertions> findByLocatedVariantIdAndVersion(Long locVarId, Integer version) throws BinningDAOException;

    public List<ReferenceClinicalAssertions> findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(Long locVarId, Long version,
            List<String> assertionStatusExcludes) throws BinningDAOException;

}
