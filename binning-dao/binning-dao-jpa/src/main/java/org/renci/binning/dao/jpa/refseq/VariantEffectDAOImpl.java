package org.renci.binning.dao.jpa.refseq;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.VariantEffectDAO;
import org.renci.binning.dao.refseq.model.VariantEffect;
import org.renci.binning.dao.refseq.model.VariantEffect_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class VariantEffectDAOImpl extends BaseDAOImpl<VariantEffect, String> implements VariantEffectDAO {

    private static final Logger logger = LoggerFactory.getLogger(VariantEffectDAOImpl.class);

    public VariantEffectDAOImpl() {
        super();
    }

    @Override
    public Class<VariantEffect> getPersistentClass() {
        return VariantEffect.class;
    }

    @Override
    public List<VariantEffect> findByName(String name) throws BinningDAOException {
        logger.debug("ENTERING findByName(String)");
        List<VariantEffect> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<VariantEffect> crit = critBuilder.createQuery(getPersistentClass());
            Root<VariantEffect> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (!name.endsWith("%")) {
                name += "%";
            }
            predicates.add(critBuilder.like(root.get(VariantEffect_.name), name));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<VariantEffect> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Transactional
    @Override
    public String save(VariantEffect entity) throws BinningDAOException {
        logger.debug("ENTERING save(VariantEffect)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getName() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getName();
    }

}
