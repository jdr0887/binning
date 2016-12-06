package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnosticPK;

public interface BinResultsFinalDiagnosticDAO extends BaseDAO<BinResultsFinalDiagnostic, BinResultsFinalDiagnosticPK> {

    public List<BinResultsFinalDiagnostic> findByDXIdAndParticipantAndVersion(Long dxId, String participant, Integer version)
            throws BinningDAOException;

    public Long findDXIdCount(String participant) throws BinningDAOException;

    public BinResultsFinalDiagnosticPK save(BinResultsFinalDiagnostic binResultsFinalDiagnostic) throws BinningDAOException;

    public void deleteByAssemblyId(Integer assemblyId) throws BinningDAOException;

    public Long findAnalyzedVariantsCount(String participant) throws BinningDAOException;

    public List<BinResultsFinalDiagnostic> findByLocatedVariantId(Long locatedVariantId) throws BinningDAOException;

    public Long findByAssemblyIdAndDiseaseClassId(Integer assemblyId, Integer diseaseClassId) throws BinningDAOException;

}
