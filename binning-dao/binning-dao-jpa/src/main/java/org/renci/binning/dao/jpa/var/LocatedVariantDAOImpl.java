package org.renci.binning.dao.jpa.var;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.renci.binning.dao.var.LocatedVariantDAO;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.VariantType;
import org.renci.binning.dao.ref.model.GenomeRefSeq_;
import org.renci.binning.dao.ref.model.GenomeRef_;
import org.renci.binning.dao.refseq.model.Variants_61_2_;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant_;
import org.renci.binning.dao.var.model.Assembly_;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.renci.binning.dao.var.model.VariantType_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class LocatedVariantDAOImpl extends BaseDAOImpl<LocatedVariant, Long> implements LocatedVariantDAO {

    private static final Logger logger = LoggerFactory.getLogger(LocatedVariantDAOImpl.class);

    public LocatedVariantDAOImpl() {
        super();
    }

    @Override
    public Class<LocatedVariant> getPersistentClass() {
        return LocatedVariant.class;
    }

    @Override
    public List<LocatedVariant> findByAssemblyId(Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findByAssemblyId(Integer)");
        List<LocatedVariant> ret = new ArrayList<LocatedVariant>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LocatedVariant> crit = critBuilder.createQuery(getPersistentClass());
            Root<LocatedVariant> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<AssemblyLocatedVariant, Assembly> assemblyLocatedVariantAssemblyJoin = root
                    .join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT).join(AssemblyLocatedVariant_.assembly, JoinType.LEFT);
            predicates.add(critBuilder.equal(assemblyLocatedVariantAssemblyJoin.get(Assembly_.id), assemblyId));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);

            TypedQuery<LocatedVariant> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<LocatedVariant> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<LocatedVariant> findByVersionAccessionAndRefId(String verAccession, Integer genomeRefId) throws BinningDAOException {
        logger.debug("ENTERING findByVersionAccessionAndRefId(String, Integer)");
        List<LocatedVariant> ret = new ArrayList<LocatedVariant>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LocatedVariant> crit = critBuilder.createQuery(getPersistentClass());
            Root<LocatedVariant> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<LocatedVariant, GenomeRefSeq> locatedVariantGenomeRefSeqJoin = root.join(LocatedVariant_.genomeRefSeq);
            predicates.add(critBuilder.equal(locatedVariantGenomeRefSeqJoin.get(GenomeRefSeq_.verAccession), verAccession));

            Join<LocatedVariant, GenomeRef> locatedVariantGenomeRefJoin = root.join(LocatedVariant_.genomeRef);
            predicates.add(critBuilder.equal(locatedVariantGenomeRefJoin.get(GenomeRef_.id), genomeRefId));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);

            TypedQuery<LocatedVariant> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<LocatedVariant> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<LocatedVariant> findIncrementable(Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findIncrementable(Integer)");
        List<LocatedVariant> ret = new ArrayList<LocatedVariant>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LocatedVariant> crit = critBuilder.createQuery(getPersistentClass());
            Root<LocatedVariant> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.lt(critBuilder.length(root.get(LocatedVariant_.ref)), 10000));
            Join<AssemblyLocatedVariant, Assembly> assemblyLocatedVariantAssemblyJoin = root
                    .join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT).join(AssemblyLocatedVariant_.assembly, JoinType.LEFT);
            predicates.add(critBuilder.equal(assemblyLocatedVariantAssemblyJoin.get(Assembly_.id), assemblyId));

            predicates.add(critBuilder.isEmpty(root.get(LocatedVariant_.variants_61_2)));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<LocatedVariant> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<LocatedVariant> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<LocatedVariant> findByGeneSymbol(String symbol) throws BinningDAOException {
        logger.debug("ENTERING findByGeneSymbol(String)");
        List<LocatedVariant> ret = new ArrayList<LocatedVariant>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LocatedVariant> crit = critBuilder.createQuery(getPersistentClass());
            Root<LocatedVariant> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<LocatedVariant, Variants_61_2> locatedVariantVariants_61_2Join = root.join(LocatedVariant_.variants_61_2);
            predicates.add(critBuilder.equal(locatedVariantVariants_61_2Join.get(Variants_61_2_.hgncGene), symbol));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<LocatedVariant> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<LocatedVariant> openjpaQuery = OpenJPAPersistence.cast(query);
            // openjpaQuery.getFetchPlan().addFetchGroup("includeVariants_61_2");
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<LocatedVariant> findByExample(LocatedVariant locatedVariant) throws BinningDAOException {
        logger.debug("ENTERING findByExample(LocatedVariant)");
        List<LocatedVariant> ret = new ArrayList<LocatedVariant>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<LocatedVariant> crit = critBuilder.createQuery(getPersistentClass());
            Root<LocatedVariant> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (locatedVariant.getEndPosition() != null) {
                predicates.add(critBuilder.equal(root.get(LocatedVariant_.endPosition), locatedVariant.getEndPosition()));
            }

            if (locatedVariant.getPosition() != null) {
                predicates.add(critBuilder.equal(root.get(LocatedVariant_.position), locatedVariant.getPosition()));
            }

            if (locatedVariant.getVariantType() != null) {
                Join<LocatedVariant, VariantType> LocatedVariantVariantTypeJoin = root.join(LocatedVariant_.variantType);
                predicates.add(
                        critBuilder.equal(LocatedVariantVariantTypeJoin.get(VariantType_.name), locatedVariant.getVariantType().getName()));
            }

            if (StringUtils.isNotEmpty(locatedVariant.getSeq())) {
                predicates.add(critBuilder.equal(root.get(LocatedVariant_.seq), locatedVariant.getSeq()));
            }

            // if (StringUtils.isNotEmpty(locatedVariant.getRef())) {
            // predicates.add(critBuilder.equal(root.get(LocatedVariant_.ref), locatedVariant.getRef()));
            // }

            if (locatedVariant.getGenomeRef() != null) {
                Join<LocatedVariant, GenomeRef> LocatedVariantGenomeRefJoin = root.join(LocatedVariant_.genomeRef);
                predicates.add(critBuilder.equal(LocatedVariantGenomeRefJoin.get(GenomeRef_.id), locatedVariant.getGenomeRef().getId()));
            }

            if (locatedVariant.getGenomeRefSeq() != null) {
                Join<LocatedVariant, GenomeRefSeq> LocatedVariantGenomeRefSeqJoin = root.join(LocatedVariant_.genomeRefSeq);
                predicates.add(critBuilder.equal(LocatedVariantGenomeRefSeqJoin.get(GenomeRefSeq_.verAccession),
                        locatedVariant.getGenomeRefSeq().getVerAccession()));
            }

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<LocatedVariant> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<LocatedVariant> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public synchronized Long save(LocatedVariant entity) throws BinningDAOException {
        logger.debug("ENTERING save(LocatedVariant)");
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
    public void delete(LocatedVariant entity) throws BinningDAOException {
        logger.debug("ENTERING delete(LocatedVariant)");
        LocatedVariant foundEntity = getEntityManager().find(getPersistentClass(), entity.getId());
        getEntityManager().remove(foundEntity);
    }

}
