package org.renci.binning.dao.jpa.var;

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
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.VariantSetDAO;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.Assembly_;
import org.renci.binning.dao.var.model.VariantSet;
import org.renci.binning.dao.var.model.VariantSet_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional
@OsgiServiceProvider(classes = { VariantSetDAO.class })
@javax.transaction.Transactional
@Singleton
public class VariantSetDAOImpl extends BaseDAOImpl<VariantSet, Integer> implements VariantSetDAO {

    private static final Logger logger = LoggerFactory.getLogger(VariantSetDAOImpl.class);

    public VariantSetDAOImpl() {
        super();
    }

    @Override
    public Class<VariantSet> getPersistentClass() {
        return VariantSet.class;
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public synchronized Integer save(VariantSet entity) throws BinningDAOException {
        logger.debug("ENTERING save(VariantSet)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getId() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getId();
    }

    @Override
    public List<VariantSet> findByAssemblyId(Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findByAssemblyId(Integer)");
        List<VariantSet> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<VariantSet> crit = critBuilder.createQuery(getPersistentClass());
            Root<VariantSet> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<VariantSet, Assembly> variantSetAssemblyJoin = root.join(VariantSet_.assemblies);
            predicates.add(critBuilder.equal(variantSetAssemblyJoin.get(Assembly_.id), assemblyId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<VariantSet> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public void delete(VariantSet entity) throws BinningDAOException {
        logger.debug("ENTERING delete(T)");
        VariantSet foundEntity = getEntityManager().find(getPersistentClass(), entity.getId());
        getEntityManager().remove(foundEntity);
    }

}
