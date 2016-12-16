package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.LocatedVariant;

public interface LocatedVariantDAO extends BaseDAO<LocatedVariant, Long> {

    public List<LocatedVariant> findIncrementable(Integer genomeRefId) throws BinningDAOException;

    public List<LocatedVariant> findByGeneSymbol(String symbol) throws BinningDAOException;

    public List<LocatedVariant> findByExample(LocatedVariant locatedVariant) throws BinningDAOException;

    public List<LocatedVariant> findByAssemblyId(Integer assemblyId) throws BinningDAOException;

    public List<LocatedVariant> findByCanonicalAlleleId(Integer canonicalAlleleId) throws BinningDAOException;

    public Long save(LocatedVariant locatedVariant) throws BinningDAOException;

    public void delete(LocatedVariant entity) throws BinningDAOException;

    public List<LocatedVariant> findByVersionAccessionAndRefId(String verAccession, Integer genomeRefId) throws BinningDAOException;

}
