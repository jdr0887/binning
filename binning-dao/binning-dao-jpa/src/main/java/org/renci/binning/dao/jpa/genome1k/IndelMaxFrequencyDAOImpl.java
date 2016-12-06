package org.renci.binning.dao.jpa.genome1k;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.IndelMaxFrequencyDAO;
import org.renci.binning.dao.genome1k.model.IndelMaxFrequency;
import org.renci.binning.dao.genome1k.model.IndelMaxFrequencyPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.genome1k.model.IndelMaxFrequencyPK_;
import org.renci.binning.dao.genome1k.model.IndelMaxFrequency_;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class IndelMaxFrequencyDAOImpl extends BaseDAOImpl<IndelMaxFrequency, IndelMaxFrequencyPK> implements IndelMaxFrequencyDAO {

    private static final Logger logger = LoggerFactory.getLogger(IndelMaxFrequencyDAOImpl.class);

    public IndelMaxFrequencyDAOImpl() {
        super();
    }

    @Override
    public Class<IndelMaxFrequency> getPersistentClass() {
        return IndelMaxFrequency.class;
    }

    @Override
    public List<IndelMaxFrequency> findByLocatedVariantIdAndVersion(Long locVarId, Integer version) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, Integer)");
        List<IndelMaxFrequency> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IndelMaxFrequency> crit = critBuilder.createQuery(getPersistentClass());
            Root<IndelMaxFrequency> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(IndelMaxFrequency_.key).get(IndelMaxFrequencyPK_.locatedVariant), locVarId));
            predicates.add(critBuilder.equal(root.get(IndelMaxFrequency_.key).get(IndelMaxFrequencyPK_.version), version));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<IndelMaxFrequency> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<IndelMaxFrequency> findByLocatedVariantId(Long locVarId) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantId(Long)");
        List<IndelMaxFrequency> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IndelMaxFrequency> crit = critBuilder.createQuery(getPersistentClass());
            Root<IndelMaxFrequency> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<IndelMaxFrequency, LocatedVariant> indelMaxFrequencyLocatedVariantJoin = root.join(IndelMaxFrequency_.locatedVariant);
            predicates.add(critBuilder.equal(indelMaxFrequencyLocatedVariantJoin.get(LocatedVariant_.id), locVarId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<IndelMaxFrequency> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
