package org.renci.binning.dao.jpa.var;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.AssemblyLocatedVariantDAO;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantPK;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant_;
import org.renci.binning.dao.var.model.Assembly_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AssemblyLocatedVariantDAOImpl extends BaseDAOImpl<AssemblyLocatedVariant, AssemblyLocatedVariantPK>
        implements AssemblyLocatedVariantDAO {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyLocatedVariantDAOImpl.class);

    public AssemblyLocatedVariantDAOImpl() {
        super();
    }

    @Override
    public Class<AssemblyLocatedVariant> getPersistentClass() {
        return AssemblyLocatedVariant.class;
    }

    @Override
    public synchronized AssemblyLocatedVariantPK save(AssemblyLocatedVariant entity) throws BinningDAOException {
        logger.debug("ENTERING save(AssemblyLocatedVariant)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getKey() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getKey();
    }

    @Override
    public void delete(AssemblyLocatedVariant entity) throws BinningDAOException {
        logger.debug("ENTERING delete(AssemblyLocatedVariant)");
        AssemblyLocatedVariant foundEntity = getEntityManager().find(getPersistentClass(), entity.getKey());
        getEntityManager().remove(foundEntity);
    }

    @Override
    public void deleteByAssemblyId(Integer assemblyId) throws BinningDAOException {
        Query qDelete = getEntityManager()
                .createQuery("delete from " + getPersistentClass().getSimpleName() + " a where a.assembly.id = :assemblyId");
        qDelete.setParameter("assemblyId", assemblyId);
        qDelete.executeUpdate();
    }

    @Override
    public List<AssemblyLocatedVariant> findByAssemblyId(Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findByAssemblyId(Integer)");
        List<AssemblyLocatedVariant> ret = new ArrayList<AssemblyLocatedVariant>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<AssemblyLocatedVariant> crit = critBuilder.createQuery(getPersistentClass());
            Root<AssemblyLocatedVariant> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<AssemblyLocatedVariant, Assembly> assemblyLocatedVariantAssemblyJoin = root.join(AssemblyLocatedVariant_.assembly);
            predicates.add(critBuilder.equal(assemblyLocatedVariantAssemblyJoin.get(Assembly_.id), assemblyId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<AssemblyLocatedVariant> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
