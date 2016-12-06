package org.renci.binning.dao.jpa.var;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.AssemblyDAO;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.Library;
import org.renci.binning.dao.var.model.Sample;
import org.renci.binning.dao.var.model.VariantSet;
import org.renci.binning.dao.var.model.Assembly_;
import org.renci.binning.dao.var.model.Library_;
import org.renci.binning.dao.var.model.Sample_;
import org.renci.binning.dao.var.model.VariantSet_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AssemblyDAOImpl extends BaseDAOImpl<Assembly, Integer> implements AssemblyDAO {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyDAOImpl.class);

    public AssemblyDAOImpl() {
        super();
    }

    @Override
    public Class<Assembly> getPersistentClass() {
        return Assembly.class;
    }

    @Override
    public synchronized Integer save(Assembly entity) throws BinningDAOException {
        logger.debug("ENTERING save(Assembly)");
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
    public void delete(Assembly entity) throws BinningDAOException {
        logger.debug("ENTERING delete(T)");
        Assembly foundEntity = getEntityManager().find(getPersistentClass(), entity.getId());
        getEntityManager().remove(foundEntity);
    }

    public List<Assembly> findByVariantSetId(Integer variantSetId) throws BinningDAOException {
        logger.debug("ENTERING findByVariantSetId(Integer)");
        List<Assembly> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Assembly> crit = critBuilder.createQuery(getPersistentClass());
            Root<Assembly> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<Assembly, VariantSet> assemblyVariantSetJoin = root.join(Assembly_.variantSet);
            predicates.add(critBuilder.equal(assemblyVariantSetJoin.get(VariantSet_.id), variantSetId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<Assembly> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<Assembly> findByLibraryId(Integer libraryId) throws BinningDAOException {
        logger.debug("ENTERING findByLibraryId(Integer)");
        List<Assembly> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Assembly> crit = critBuilder.createQuery(getPersistentClass());
            Root<Assembly> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<Assembly, Library> assemblyLibraryJoin = root.join(Assembly_.library);
            predicates.add(critBuilder.equal(assemblyLibraryJoin.get(Library_.id), libraryId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<Assembly> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    public List<Assembly> findBySampleName(String name) throws BinningDAOException {
        logger.debug("ENTERING findBySampleName(String)");
        List<Assembly> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Assembly> crit = critBuilder.createQuery(getPersistentClass());
            Root<Assembly> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<Assembly, Library> assemblyLibraryJoin = root.join(Assembly_.library);
            Join<Library, Sample> librarySampleJoin = assemblyLibraryJoin.join(Library_.sample);
            predicates.add(critBuilder.equal(librarySampleJoin.get(Sample_.name), name));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<Assembly> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
