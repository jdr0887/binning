package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.renci.binning.dao.refseq.model.Variants_61_2PK;

public interface Variants_61_2_DAO extends BaseDAO<Variants_61_2, Variants_61_2PK> {

    public List<Variants_61_2> findByLocatedVariantId(Long id) throws BinningDAOException;

    public List<Variants_61_2> findByGeneName(String name) throws BinningDAOException;

    public List<Variants_61_2> findByGeneId(Integer geneId) throws BinningDAOException;

    public List<Variants_61_2> findByTranscriptAccession(String accession) throws BinningDAOException;

    public List<Variants_61_2> findByGeneNameAndMaxAlleleFrequency(String name, Double threshold) throws BinningDAOException;

    public List<Variants_61_2> findByAssemblyId(Integer id) throws BinningDAOException;

    public List<Variants_61_2> findByAssemblyIdAndSampleNameAndHGMDVersionAndMaxFrequencyThresholdAndGeneId(Integer assemblyId,
            String sampleName, Integer hgmdVersion, Double threshold, Integer geneId) throws BinningDAOException;

    public List<Variants_61_2> findByHGMDVersionAndMaxFrequencyThresholdAndGeneIdAndVariantEffectList(Integer hgmdVersion, Double threshold,
            Integer geneId, List<String> variantEffectList) throws BinningDAOException;

    public Variants_61_2PK save(Variants_61_2 variant) throws BinningDAOException;

    public List<Variants_61_2> findKnownPathenogenic(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_61_2> findLikelyPathenogenic(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_61_2> findPossiblyPathenogenic(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_61_2> findUncertainSignificance(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_61_2> findLikelyBenign(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public List<Variants_61_2> findAlmostCertainlyBenign(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException;

    public Long findByAssemblyIdAndVariantEffect(Integer assemblyId, String variantEffect) throws BinningDAOException;

    public Long findByAssemblyIdAndVariantType(Integer assemblyId, String variantType) throws BinningDAOException;

    public Long findTranscriptDependentCount(Integer assemblyId) throws BinningDAOException;

    public Long findCodingCount(Integer assemblyId) throws BinningDAOException;

    public Long findNonCodingCount(Integer assemblyId) throws BinningDAOException;

}
