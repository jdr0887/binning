package org.renci.binning.dao.jpa.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.annotation.AnnotationGeneExternalIdsDAO;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIds;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIdsPK;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIdsPK_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIds_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class AnnotationGeneExternalIdsDAOImpl extends BaseDAOImpl<AnnotationGeneExternalIds, AnnotationGeneExternalIdsPK>
        implements AnnotationGeneExternalIdsDAO {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationGeneExternalIdsDAOImpl.class);

    public AnnotationGeneExternalIdsDAOImpl() {
        super();
    }

    @Override
    public Class<AnnotationGeneExternalIds> getPersistentClass() {
        return AnnotationGeneExternalIds.class;
    }

    @Override
    public List<AnnotationGeneExternalIds> findByExternalId(Integer externalId) throws BinningDAOException {
        logger.debug("ENTERING findByNamespace(String)");
        List<AnnotationGeneExternalIds> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<AnnotationGeneExternalIds> crit = critBuilder.createQuery(getPersistentClass());
            Root<AnnotationGeneExternalIds> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(AnnotationGeneExternalIds_.key).get(AnnotationGeneExternalIdsPK_.id), externalId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<AnnotationGeneExternalIds> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<AnnotationGeneExternalIds> findByNamespace(String namespace) throws BinningDAOException {
        logger.debug("ENTERING findByNamespace(String)");
        List<AnnotationGeneExternalIds> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<AnnotationGeneExternalIds> crit = critBuilder.createQuery(getPersistentClass());
            Root<AnnotationGeneExternalIds> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(
                    critBuilder.equal(root.get(AnnotationGeneExternalIds_.key).get(AnnotationGeneExternalIdsPK_.namespace), namespace));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<AnnotationGeneExternalIds> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<AnnotationGeneExternalIds> findByNamespaceAndNamespaceVersion(String namespace, String version) throws BinningDAOException {
        logger.debug("ENTERING findByNamespaceAndNamespaceVersion(String, String)");
        List<AnnotationGeneExternalIds> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<AnnotationGeneExternalIds> crit = critBuilder.createQuery(getPersistentClass());
            Root<AnnotationGeneExternalIds> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(
                    critBuilder.equal(root.get(AnnotationGeneExternalIds_.key).get(AnnotationGeneExternalIdsPK_.namespace), namespace));
            predicates.add(
                    critBuilder.equal(root.get(AnnotationGeneExternalIds_.key).get(AnnotationGeneExternalIdsPK_.namespaceVer), version));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<AnnotationGeneExternalIds> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
