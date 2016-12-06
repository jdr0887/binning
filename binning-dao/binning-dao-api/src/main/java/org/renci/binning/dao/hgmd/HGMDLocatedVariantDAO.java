package org.renci.binning.dao.hgmd;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariantPK;

public interface HGMDLocatedVariantDAO extends BaseDAO<HGMDLocatedVariant, HGMDLocatedVariantPK> {

    public List<HGMDLocatedVariant> findByLocatedVariantId(Long locatedVariantId) throws BinningDAOException;

}
