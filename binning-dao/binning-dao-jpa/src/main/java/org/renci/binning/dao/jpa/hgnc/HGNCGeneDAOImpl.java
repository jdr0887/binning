package org.renci.binning.dao.jpa.hgnc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.annotation.model.AnnotationGene;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIds;
import org.renci.binning.dao.hgnc.HGNCGeneDAO;
import org.renci.binning.dao.hgnc.model.HGNCGene;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIdsPK_;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIds_;
import org.renci.binning.dao.annotation.model.AnnotationGene_;
import org.renci.binning.dao.hgnc.model.HGNCGene_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class HGNCGeneDAOImpl extends BaseDAOImpl<HGNCGene, Integer> implements HGNCGeneDAO {

    private static final Logger logger = LoggerFactory.getLogger(HGNCGeneDAOImpl.class);

    public HGNCGeneDAOImpl() {
        super();
    }

    @Override
    public Class<HGNCGene> getPersistentClass() {
        return HGNCGene.class;
    }

    @Override
    public List<HGNCGene> findByName(String name) throws BinningDAOException {
        logger.debug("ENTERING findByName(String)");
        List<HGNCGene> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<HGNCGene> crit = critBuilder.createQuery(getPersistentClass());
            Root<HGNCGene> fromHGNCGene = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(fromHGNCGene.get(HGNCGene_.name), name));
            crit.select(fromHGNCGene);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.orderBy(critBuilder.asc(fromHGNCGene.get(HGNCGene_.id)));
            TypedQuery<HGNCGene> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<HGNCGene> findBySymbol(String symbol) throws BinningDAOException {
        logger.debug("ENTERING findBySymbol(String)");
        List<HGNCGene> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<HGNCGene> crit = critBuilder.createQuery(getPersistentClass());
            Root<HGNCGene> fromHGNCGene = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(fromHGNCGene.get(HGNCGene_.symbol), symbol));

            crit.select(fromHGNCGene);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.orderBy(critBuilder.asc(fromHGNCGene.get(HGNCGene_.id)));
            TypedQuery<HGNCGene> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<HGNCGene> findByAnnotationGeneExternalIdsNamespace(String namespace) throws BinningDAOException {
        logger.debug("ENTERING findByRefSeqVersion(String)");
        List<HGNCGene> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<HGNCGene> crit = critBuilder.createQuery(getPersistentClass());

            Root<HGNCGene> fromHGNCGene = crit.from(getPersistentClass());
            Root<AnnotationGeneExternalIds> fromAnnotationGeneExternalIds = crit.from(AnnotationGeneExternalIds.class);

            Predicate condition1 = critBuilder.equal(fromHGNCGene.get(HGNCGene_.id),
                    fromAnnotationGeneExternalIds.get(AnnotationGeneExternalIds_.key).get(AnnotationGeneExternalIdsPK_.id));
            Predicate condition2 = critBuilder.equal(
                    fromAnnotationGeneExternalIds.get(AnnotationGeneExternalIds_.key).get(AnnotationGeneExternalIdsPK_.namespace),
                    namespace);

            crit.select(fromHGNCGene);
            crit.where(condition1, condition2);
            crit.orderBy(critBuilder.asc(fromHGNCGene.get(HGNCGene_.id)));
            TypedQuery<HGNCGene> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<HGNCGene> findByAnnotationGeneExternalIdsGeneIdsAndNamespace(Integer geneId, String namespace) throws BinningDAOException {
        logger.debug("ENTERING findByAnnotationGeneExternalIdsGeneIdsAndNamespace(Integer, String)");
        List<HGNCGene> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<HGNCGene> crit = critBuilder.createQuery(getPersistentClass());

            Root<HGNCGene> fromHGNCGene = crit.from(getPersistentClass());
            Root<AnnotationGeneExternalIds> fromAnnotationGeneExternalIds = crit.from(AnnotationGeneExternalIds.class);

            Predicate condition1 = critBuilder.equal(fromHGNCGene.get(HGNCGene_.id),
                    fromAnnotationGeneExternalIds.get(AnnotationGeneExternalIds_.key).get(AnnotationGeneExternalIdsPK_.id));
            Predicate condition2 = critBuilder.equal(
                    fromAnnotationGeneExternalIds.get(AnnotationGeneExternalIds_.key).get(AnnotationGeneExternalIdsPK_.namespace),
                    namespace);

            Join<AnnotationGeneExternalIds, AnnotationGene> fromAnnotationGeneExternalIdsJoin = fromAnnotationGeneExternalIds
                    .join(AnnotationGeneExternalIds_.gene);
            Predicate condition3 = critBuilder.equal(fromAnnotationGeneExternalIdsJoin.get(AnnotationGene_.id), geneId);

            crit.select(fromHGNCGene);
            crit.where(condition1, condition2, condition3);
            crit.orderBy(critBuilder.asc(fromHGNCGene.get(HGNCGene_.id)));
            TypedQuery<HGNCGene> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
