package org.renci.binning.dao.jpa.genome1k;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.SNPPopulationMaxFrequencyDAO;
import org.renci.binning.dao.genome1k.model.SNPPopulationMaxFrequency;
import org.renci.binning.dao.genome1k.model.SNPPopulationMaxFrequencyPK;
import org.renci.binning.dao.genome1k.model.SNPPopulationMaxFrequencyPK_;
import org.renci.binning.dao.genome1k.model.SNPPopulationMaxFrequency_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { SNPPopulationMaxFrequencyDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class SNPPopulationMaxFrequencyDAOImpl extends BaseDAOImpl<SNPPopulationMaxFrequency, SNPPopulationMaxFrequencyPK>
        implements SNPPopulationMaxFrequencyDAO {

    private static final Logger logger = LoggerFactory.getLogger(SNPPopulationMaxFrequencyDAOImpl.class);

    public SNPPopulationMaxFrequencyDAOImpl() {
        super();
    }

    @Override
    public Class<SNPPopulationMaxFrequency> getPersistentClass() {
        return SNPPopulationMaxFrequency.class;
    }

    @Override
    public List<SNPPopulationMaxFrequency> findByLocatedVariantIdAndVersion(Long locVarId, Integer version) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, Integer)");
        List<SNPPopulationMaxFrequency> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<SNPPopulationMaxFrequency> crit = critBuilder.createQuery(getPersistentClass());
            Root<SNPPopulationMaxFrequency> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(
                    critBuilder.equal(root.get(SNPPopulationMaxFrequency_.key).get(SNPPopulationMaxFrequencyPK_.locatedVariant), locVarId));
            predicates.add(critBuilder.equal(root.get(SNPPopulationMaxFrequency_.key).get(SNPPopulationMaxFrequencyPK_.version), version));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<SNPPopulationMaxFrequency> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<SNPPopulationMaxFrequency> findByLocatedVariantId(Long locVarId) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantId(Long)");
        List<SNPPopulationMaxFrequency> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<SNPPopulationMaxFrequency> crit = critBuilder.createQuery(getPersistentClass());
            Root<SNPPopulationMaxFrequency> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<SNPPopulationMaxFrequency, LocatedVariant> snpPopulationMaxFrequencyLocatedVariantJoin = root
                    .join(SNPPopulationMaxFrequency_.locatedVariant);
            predicates.add(critBuilder.equal(snpPopulationMaxFrequencyLocatedVariantJoin.get(LocatedVariant_.id), locVarId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<SNPPopulationMaxFrequency> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
