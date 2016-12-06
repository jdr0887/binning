package org.renci.binning.dao.jpa.refseq;

import java.util.List;

import javax.persistence.TypedQuery;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.Variants_48_2_DAO;
import org.renci.binning.dao.refseq.model.Variants_48_2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class Variants_48_2_DAOImpl extends BaseDAOImpl<Variants_48_2, Long> implements Variants_48_2_DAO {

    private static final Logger logger = LoggerFactory.getLogger(Variants_48_2_DAOImpl.class);

    public Variants_48_2_DAOImpl() {
        super();
    }

    @Override
    public Class<Variants_48_2> getPersistentClass() {
        return Variants_48_2.class;
    }

    @Override
    public List<Variants_48_2> findByLocatedVariantId(Long id) throws BinningDAOException {
        logger.debug("ENTERING findByName()");
        TypedQuery<Variants_48_2> query = getEntityManager().createNamedQuery("Variants_48_2.findByLocatedVariantId", Variants_48_2.class);
        query.setParameter("LocatedVariantId", id);
        List<Variants_48_2> ret = query.getResultList();
        return ret;
    }

}
