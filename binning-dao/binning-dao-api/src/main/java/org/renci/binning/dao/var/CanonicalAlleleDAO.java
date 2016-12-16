package org.renci.binning.dao.var;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.var.model.CanonicalAllele;

public interface CanonicalAlleleDAO extends BaseDAO<CanonicalAllele, Integer> {

    public List<CanonicalAllele> findByLocatedVariantId(Long locatedVariantId) throws BinningDAOException;

    public Integer save(CanonicalAllele canonicalAllele) throws BinningDAOException;

}
