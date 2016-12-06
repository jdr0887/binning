package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.VariantEffect;

public interface VariantEffectDAO extends BaseDAO<VariantEffect, String> {

    public List<VariantEffect> findByName(String name) throws BinningDAOException;

    public String save(VariantEffect variantEffect) throws BinningDAOException;

}
