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
import org.renci.binning.dao.var.LibraryDAO;
import org.renci.binning.dao.var.model.Library;
import org.renci.binning.dao.var.model.Sample;
import org.renci.binning.dao.var.model.Library_;
import org.renci.binning.dao.var.model.Sample_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class LibraryDAOImpl extends BaseDAOImpl<Library, Integer> implements LibraryDAO {

    private static final Logger logger = LoggerFactory.getLogger(LibraryDAOImpl.class);

    public LibraryDAOImpl() {
        super();
    }

    @Override
    public Class<Library> getPersistentClass() {
        return Library.class;
    }

    @Override
    public synchronized Integer save(Library entity) throws BinningDAOException {
        logger.debug("ENTERING save(Library)");
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
    public void delete(Library entity) throws BinningDAOException {
        logger.debug("ENTERING delete(T)");
        Library foundEntity = getEntityManager().find(getPersistentClass(), entity.getId());
        getEntityManager().remove(foundEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Library> findByNameAndSampleId(String name, Integer sampleId) throws BinningDAOException {
        logger.debug("ENTERING findByNameAndSampleId(String, Integer)");
        List<Library> ret = new ArrayList<Library>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Library> crit = critBuilder.createQuery(getPersistentClass());
            Root<Library> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(Library_.name), name));
            Join<Library, Sample> librarySampleJoin = root.join(Library_.sample);
            predicates.add(critBuilder.equal(librarySampleJoin.get(Sample_.id), sampleId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<Library> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
