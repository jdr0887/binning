package org.renci.binning.dao.jpa.exac;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Coalesce;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.exac.MaxVariantFrequencyDAO;
import org.renci.binning.dao.exac.model.MaxVariantFrequency;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.exac.model.MaxVariantFrequency_;
import org.renci.binning.dao.refseq.model.Variants_61_2_;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class MaxVariantFrequencyDAOImpl extends BaseDAOImpl<MaxVariantFrequency, Long> implements MaxVariantFrequencyDAO {

    private static final Logger logger = LoggerFactory.getLogger(MaxVariantFrequencyDAOImpl.class);

    public MaxVariantFrequencyDAOImpl() {
        super();
    }

    @Override
    public Class<MaxVariantFrequency> getPersistentClass() {
        return MaxVariantFrequency.class;
    }

    @Override
    public List<MaxVariantFrequency> findByLocatedVariantIdAndVersion(Long locVarId, String version) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, String)");
        TypedQuery<MaxVariantFrequency> query = getEntityManager()
                .createNamedQuery("exac.MaxVariantFrequency.findByLocatedVariantIdAndVersion", MaxVariantFrequency.class);
        query.setParameter("LocatedVariantId", locVarId);
        query.setParameter("version", version);
        List<MaxVariantFrequency> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<MaxVariantFrequency> findByLocatedVariantId(Long locVarId) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantId(Long)");
        List<MaxVariantFrequency> ret = new ArrayList<MaxVariantFrequency>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<MaxVariantFrequency> crit = critBuilder.createQuery(getPersistentClass());
            Root<MaxVariantFrequency> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<MaxVariantFrequency, LocatedVariant> maxVariantFrequencyLocatedVariantJoin = root
                    .join(MaxVariantFrequency_.locatedVariant);
            predicates.add(critBuilder.equal(maxVariantFrequencyLocatedVariantJoin.get(LocatedVariant_.id), locVarId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<MaxVariantFrequency> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<MaxVariantFrequency> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<MaxVariantFrequency> findByLocatedVariantIdAndFrequencyThreshold(Long locVarId, Double threshold)
            throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantId(Long)");
        List<MaxVariantFrequency> ret = new ArrayList<MaxVariantFrequency>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<MaxVariantFrequency> crit = critBuilder.createQuery(getPersistentClass());
            Root<MaxVariantFrequency> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<MaxVariantFrequency, LocatedVariant> maxVariantFrequencyLocatedVariantJoin = root
                    .join(MaxVariantFrequency_.locatedVariant);
            predicates.add(critBuilder.equal(maxVariantFrequencyLocatedVariantJoin.get(LocatedVariant_.id), locVarId));
            Coalesce<Double> maxVariantFrequencyCoalesce = critBuilder.coalesce();
            maxVariantFrequencyCoalesce.value(root.get(MaxVariantFrequency_.maxAlleleFrequency));
            maxVariantFrequencyCoalesce.value(0D);
            predicates.add(critBuilder.lessThanOrEqualTo(maxVariantFrequencyCoalesce, threshold));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<MaxVariantFrequency> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<MaxVariantFrequency> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<MaxVariantFrequency> findByGeneNameAndMaxAlleleFrequency(String name, Double threshold) throws BinningDAOException {
        logger.debug("ENTERING findByGeneNameAndMaxAlleleFrequency(String, Double)");
        List<MaxVariantFrequency> ret = new ArrayList<MaxVariantFrequency>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<MaxVariantFrequency> crit = critBuilder.createQuery(getPersistentClass());
            Root<MaxVariantFrequency> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<MaxVariantFrequency, LocatedVariant> maxVariantFrequencyLocatedVariantJoin = root
                    .join(MaxVariantFrequency_.locatedVariant);
            Join<LocatedVariant, Variants_61_2> LocatedVariantVariantsJoin = maxVariantFrequencyLocatedVariantJoin
                    .join(LocatedVariant_.variants_61_2);
            predicates.add(critBuilder.equal(LocatedVariantVariantsJoin.get(Variants_61_2_.hgncGene), name));
            Coalesce<Double> maxVariantFrequencyCoalesce = critBuilder.coalesce();
            maxVariantFrequencyCoalesce.value(root.get(MaxVariantFrequency_.maxAlleleFrequency));
            maxVariantFrequencyCoalesce.value(0D);
            predicates.add(critBuilder.lessThanOrEqualTo(maxVariantFrequencyCoalesce, threshold));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<MaxVariantFrequency> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<MaxVariantFrequency> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
