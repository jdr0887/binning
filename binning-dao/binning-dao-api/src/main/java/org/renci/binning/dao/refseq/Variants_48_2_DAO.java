package org.renci.binning.dao.refseq;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.refseq.model.Variants_48_2;

public interface Variants_48_2_DAO extends BaseDAO<Variants_48_2, Long> {

    public List<Variants_48_2> findByLocatedVariantId(Long id) throws BinningDAOException;

}
