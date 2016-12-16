package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.Variants_78_4;
import org.renci.binning.dao.refseq.model.Variants_78_4PK;

public interface Variants_78_4_DAO extends BaseDAO<Variants_78_4, Variants_78_4PK> {

    public List<Variants_78_4> findByLocatedVariantId(Long id) throws BinningDAOException;

    public List<Variants_78_4> findByGeneName(String name) throws BinningDAOException;

    public List<Variants_78_4> findByGeneId(Integer geneId) throws BinningDAOException;

    public List<Variants_78_4> findByTranscriptAccession(String accession) throws BinningDAOException;

    public List<Variants_78_4> findByGeneNameAndMaxAlleleFrequency(String name, Double threshold) throws BinningDAOException;

    public List<Variants_78_4> findByAssemblyId(Integer id) throws BinningDAOException;

    public List<Variants_78_4> findByAssemblyIdAndSampleNameAndHGMDVersionAndMaxFrequencyThresholdAndGeneId(Integer assemblyId,
            String sampleName, Integer hgmdVersion, Double threshold, Integer geneId) throws BinningDAOException;

    public List<Variants_78_4> findByHGMDVersionAndMaxFrequencyThresholdAndGeneIdAndVariantEffectList(Integer hgmdVersion, Double threshold,
            Integer geneId, List<String> variantEffectList) throws BinningDAOException;

    public Variants_78_4PK save(Variants_78_4 variant) throws BinningDAOException;

    public List<Variants_78_4> findKnownPathenogenic(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_78_4> findLikelyPathenogenic(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_78_4> findPossiblyPathenogenic(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_78_4> findUncertainSignificance(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_78_4> findLikelyBenign(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_78_4> findAlmostCertainlyBenign(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public Long findByAssemblyIdAndVariantEffect(Integer assemblyId, String variantEffect) throws BinningDAOException;

    public Long findByAssemblyIdAndVariantType(Integer assemblyId, String variantType) throws BinningDAOException;

    public Long findTranscriptDependentCount(Integer assemblyId) throws BinningDAOException;

    public Long findCodingCount(Integer assemblyId) throws BinningDAOException;

    public Long findNonCodingCount(Integer assemblyId) throws BinningDAOException;

}
