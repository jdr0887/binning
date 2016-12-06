package org.renci.binning.dao.jpa.ref;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.ref.GenomeRefSeqLocationDAO;
import org.renci.binning.dao.ref.model.GenomeRefSeqLocation;
import org.renci.binning.dao.ref.model.GenomeRefSeqLocationPK_;
import org.renci.binning.dao.ref.model.GenomeRefSeqLocation_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class GenomeRefSeqLocationDAOImpl extends BaseDAOImpl<GenomeRefSeqLocation, String> implements GenomeRefSeqLocationDAO {

    private static final Logger logger = LoggerFactory.getLogger(GenomeRefSeqLocationDAOImpl.class);

    public GenomeRefSeqLocationDAOImpl() {
        super();
    }

    @Override
    public Class<GenomeRefSeqLocation> getPersistentClass() {
        return GenomeRefSeqLocation.class;
    }

    @Override
    public List<GenomeRefSeqLocation> findByRefIdAndVersionedAccesionAndPosition(Integer refId, String verAccession, Integer position)
            throws BinningDAOException {
        logger.debug("ENTERING findBySeqType(String)");
        List<GenomeRefSeqLocation> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<GenomeRefSeqLocation> crit = critBuilder.createQuery(getPersistentClass());
            Root<GenomeRefSeqLocation> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(GenomeRefSeqLocation_.key).get(GenomeRefSeqLocationPK_.refId), refId));
            predicates.add(critBuilder.equal(root.get(GenomeRefSeqLocation_.key).get(GenomeRefSeqLocationPK_.verAccession), verAccession));
            predicates.add(critBuilder.equal(root.get(GenomeRefSeqLocation_.position), position));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<GenomeRefSeqLocation> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
