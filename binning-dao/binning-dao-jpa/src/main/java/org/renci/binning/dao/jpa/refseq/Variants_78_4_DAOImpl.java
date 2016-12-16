package org.renci.binning.dao.jpa.refseq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Coalesce;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.annotation.model.AnnotationGene;
import org.renci.binning.dao.annotation.model.AnnotationGene_;
import org.renci.binning.dao.clinbin.model.DX;
import org.renci.binning.dao.clinbin.model.DX_;
import org.renci.binning.dao.clinbin.model.DiagnosticGene;
import org.renci.binning.dao.clinbin.model.DiagnosticGene_;
import org.renci.binning.dao.clinbin.model.MaxFrequency;
import org.renci.binning.dao.clinbin.model.MaxFrequency_;
import org.renci.binning.dao.exac.model.MaxVariantFrequency;
import org.renci.binning.dao.exac.model.MaxVariantFrequency_;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariantPK_;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.Variants_78_4_DAO;
import org.renci.binning.dao.refseq.model.LocationType;
import org.renci.binning.dao.refseq.model.LocationType_;
import org.renci.binning.dao.refseq.model.VariantEffect;
import org.renci.binning.dao.refseq.model.VariantEffect_;
import org.renci.binning.dao.refseq.model.Variants_78_4;
import org.renci.binning.dao.refseq.model.Variants_78_4PK;
import org.renci.binning.dao.refseq.model.Variants_78_4_;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant_;
import org.renci.binning.dao.var.model.Assembly_;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.renci.binning.dao.var.model.VariantType_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { Variants_78_4_DAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class Variants_78_4_DAOImpl extends BaseDAOImpl<Variants_78_4, Variants_78_4PK> implements Variants_78_4_DAO {

    private static final Logger logger = LoggerFactory.getLogger(Variants_78_4_DAOImpl.class);

    public Variants_78_4_DAOImpl() {
        super();
    }

    @Override
    public Class<Variants_78_4> getPersistentClass() {
        return Variants_78_4.class;
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public Variants_78_4PK save(Variants_78_4 entity) throws BinningDAOException {
        logger.debug("ENTERING save(Variants_78_4PK)");
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
    public List<Variants_78_4> findByGeneId(Integer geneId) throws BinningDAOException {
        logger.debug("ENTERING findByGeneId(Integer)");
        List<Variants_78_4> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            predicates.add(critBuilder.equal(variantsAnnotationGeneJoin.get(AnnotationGene_.id), geneId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findByLocatedVariantId(Long id) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantId(Long)");
        List<Variants_78_4> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.equal(root.join(Variants_78_4_.locatedVariant).get(LocatedVariant_.id), id));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);

            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findKnownPathenogenic(Integer hgmdVersion, Integer dxId, Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findKnownPathenogenic(Integer, Integer, Integer)");
        List<Variants_78_4> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT)
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx, JoinType.LEFT).get(DX_.id), dxId));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            predicates.add(critBuilder.equal(variantsLocatedVariantJoin.join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT)
                    .join(AssemblyLocatedVariant_.assembly, JoinType.LEFT).get(Assembly_.id), assemblyId));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = variantsLocatedVariantJoin
                    .join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.equal(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);

            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findByHGMDVersionAndMaxFrequencyThresholdAndGeneIdAndVariantEffectList(Integer hgmdVersion, Double threshold,
            Integer geneId, List<String> variantEffectList) throws BinningDAOException {
        logger.debug(
                "ENTERING findByHGMDVersionAndMaxFrequencyThresholdAndGeneIdAndVariantEffectList(Integer, Double, Integer, List<String>)");
        List<Variants_78_4> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, VariantEffect> variantsVariantEffectJoin = root.join(Variants_78_4_.variantEffect, JoinType.LEFT);
            predicates.add(variantsVariantEffectJoin.get(VariantEffect_.name).in(variantEffectList));

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            predicates.add(critBuilder.equal(variantsAnnotationGeneJoin.get(AnnotationGene_.id), geneId));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.lt(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), threshold));

            // Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = variantsLocatedVariantJoin
            // .join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            // predicates.add(critBuilder.notEqual(
            // critBuilder.function("hgmd_enum_to_varchar", String.class,
            // variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
            // "DM"));
            // predicates.add(critBuilder
            // .notEqual(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
            // hgmdVersion));

            Subquery<HGMDLocatedVariant> subquery = crit.subquery(HGMDLocatedVariant.class);
            Root<HGMDLocatedVariant> hgmdLocatedVariantRoot = subquery.from(HGMDLocatedVariant.class);
            subquery.select(hgmdLocatedVariantRoot);
            List<Predicate> subQueryPredicates = new ArrayList<Predicate>();
            subQueryPredicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, hgmdLocatedVariantRoot.get(HGMDLocatedVariant_.tag)), "DM"));
            subQueryPredicates.add(
                    critBuilder.equal(hgmdLocatedVariantRoot.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version), hgmdVersion));
            subQueryPredicates.add(critBuilder.equal(variantsLocatedVariantJoin.get(LocatedVariant_.id),
                    hgmdLocatedVariantRoot.get(HGMDLocatedVariant_.locatedVariant).get(LocatedVariant_.id)));
            subquery.where(subQueryPredicates.toArray(new Predicate[subQueryPredicates.size()]));

            predicates.add(critBuilder.not(critBuilder.exists(subquery)));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findByAssemblyIdAndSampleNameAndHGMDVersionAndMaxFrequencyThresholdAndGeneId(Integer assemblyId,
            String sampleName, Integer hgmdVersion, Double threshold, Integer geneId) throws BinningDAOException {
        logger.debug(
                "ENTERING findByAssemblyIdAndSampleNameAndHGMDVersionAndMaxFrequencyThresholdAndGeneId(Integer, String, Integer, Double, Integer)");
        List<Variants_78_4> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            predicates.add(critBuilder.equal(variantsLocatedVariantJoin.join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT)
                    .join(AssemblyLocatedVariant_.assembly, JoinType.LEFT).get(Assembly_.id), assemblyId));
            predicates.add(critBuilder.lt(
                    variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs, JoinType.LEFT).get(MaxFrequency_.maxAlleleFreq), threshold));

            predicates.add(critBuilder.equal(root.join(Variants_78_4_.gene, JoinType.LEFT).get(AnnotationGene_.id), geneId));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = variantsLocatedVariantJoin
                    .join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.equal(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findLikelyPathenogenic(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findLikelyPathenogenic(Integer)");
        List<Variants_78_4> ret = new ArrayList<Variants_78_4>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = variantsAnnotationGeneJoin
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(
                    critBuilder.equal(annotationGeneDiagnosticGeneJoin.get(DiagnosticGene_.diagnosticListVersion), diagnosticListVersion));

            Join<DiagnosticGene, DX> annotationGeneDiagnosticCodeJoin = annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx,
                    JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticCodeJoin.get(DX_.id), dxId));

            List<String> variantEffectNames = new ArrayList<>();
            variantEffectNames.add("nonsense");
            variantEffectNames.add("splice-site");
            variantEffectNames.add("boundary-crossing indel");
            variantEffectNames.add("stoploss");
            variantEffectNames.add("nonsense indel");
            variantEffectNames.add("frameshifting indel");

            Join<Variants_78_4, VariantEffect> variantsVariantEffectJoin = root.join(Variants_78_4_.variantEffect);
            predicates.add(variantsVariantEffectJoin.get(VariantEffect_.name).in(variantEffectNames));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.lt(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.01));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findPossiblyPathenogenic(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findLikelyPathenogenic(Integer)");
        List<Variants_78_4> ret = new ArrayList<Variants_78_4>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = variantsAnnotationGeneJoin
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(
                    critBuilder.equal(annotationGeneDiagnosticGeneJoin.get(DiagnosticGene_.diagnosticListVersion), diagnosticListVersion));

            Join<DiagnosticGene, DX> annotationGeneDiagnosticCodeJoin = annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx,
                    JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticCodeJoin.get(DX_.id), dxId));

            List<String> variantEffectNames = new ArrayList<>();
            variantEffectNames.add("missense");
            variantEffectNames.add("non-frameshifting indel");

            Join<Variants_78_4, VariantEffect> variantsVariantEffectJoin = root.join(Variants_78_4_.variantEffect);
            predicates.add(variantsVariantEffectJoin.get(VariantEffect_.name).in(variantEffectNames));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.lt(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.01));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findUncertainSignificance(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findLikelyPathogenic(Integer)");
        List<Variants_78_4> ret = new ArrayList<Variants_78_4>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = variantsAnnotationGeneJoin
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(
                    critBuilder.equal(annotationGeneDiagnosticGeneJoin.get(DiagnosticGene_.diagnosticListVersion), diagnosticListVersion));

            Join<DiagnosticGene, DX> annotationGeneDiagnosticCodeJoin = annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx,
                    JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticCodeJoin.get(DX_.id), dxId));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.lt(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.01));

            List<String> variantEffectNames = new ArrayList<>();
            variantEffectNames.add("synonymous");
            variantEffectNames.add("synonymous indel");
            variantEffectNames.add("intron");
            variantEffectNames.add("splice-site-UTR-3");
            variantEffectNames.add("splice-site-UTR-5");
            variantEffectNames.add("splice-site-UTR");
            variantEffectNames.add("potential RNA-editing site");
            variantEffectNames.add("noncoding boundary-crossing indel");

            List<String> locationTypeNames = new ArrayList<>();
            locationTypeNames.add("UTR-5");
            locationTypeNames.add("UTR-3");
            locationTypeNames.add("UTR");

            Join<Variants_78_4, LocationType> variantsLocationTypeJoin = root.join(Variants_78_4_.locationType, JoinType.LEFT);
            Join<Variants_78_4, VariantEffect> variantsVariantEffectJoin = root.join(Variants_78_4_.variantEffect, JoinType.LEFT);
            predicates.add(critBuilder.or(variantsVariantEffectJoin.get(VariantEffect_.name).in(variantEffectNames),
                    variantsLocationTypeJoin.get(LocationType_.name).in(locationTypeNames)));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = variantsAnnotationGeneJoin
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(
                    critBuilder.equal(annotationGeneDiagnosticGeneJoin.get(DiagnosticGene_.diagnosticListVersion), diagnosticListVersion));

            Join<DiagnosticGene, DX> annotationGeneDiagnosticCodeJoin = annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx,
                    JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticCodeJoin.get(DX_.id), dxId));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.lt(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.05));

            List<String> locationTypeNames = new ArrayList<>();
            locationTypeNames.add("UTR-5");
            locationTypeNames.add("UTR-3");
            locationTypeNames.add("UTR");
            locationTypeNames.add("exon");
            locationTypeNames.add("intron/exon boundary");

            Join<Variants_78_4, LocationType> variantsLocationTypeJoin = root.join(Variants_78_4_.locationType, JoinType.LEFT);
            predicates.add(variantsLocationTypeJoin.get(LocationType_.name).in(locationTypeNames));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public List<Variants_78_4> findLikelyBenign(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findLikelyBenign(Integer)");
        List<Variants_78_4> ret = new ArrayList<Variants_78_4>();

        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = variantsAnnotationGeneJoin
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(
                    critBuilder.equal(annotationGeneDiagnosticGeneJoin.get(DiagnosticGene_.diagnosticListVersion), diagnosticListVersion));

            Join<DiagnosticGene, DX> annotationGeneDiagnosticCodeJoin = annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx,
                    JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticCodeJoin.get(DX_.id), dxId));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.lt(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.1));

            List<String> locationTypeNames = new ArrayList<>();
            locationTypeNames.add("UTR-5");
            locationTypeNames.add("UTR-3");
            locationTypeNames.add("UTR");
            locationTypeNames.add("exon");
            locationTypeNames.add("intron/exon boundary");

            Join<Variants_78_4, LocationType> variantsLocationTypeJoin = root.join(Variants_78_4_.locationType, JoinType.LEFT);
            predicates.add(variantsLocationTypeJoin.get(LocationType_.name).in(locationTypeNames));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = variantsAnnotationGeneJoin
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(
                    critBuilder.equal(annotationGeneDiagnosticGeneJoin.get(DiagnosticGene_.diagnosticListVersion), diagnosticListVersion));

            Join<DiagnosticGene, DX> annotationGeneDiagnosticCodeJoin = annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx,
                    JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticCodeJoin.get(DX_.id), dxId));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.lt(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.05));

            List<String> locationTypeNames = new ArrayList<>();
            locationTypeNames.add("UTR-5");
            locationTypeNames.add("UTR-3");
            locationTypeNames.add("UTR");
            locationTypeNames.add("exon");
            locationTypeNames.add("intron/exon boundary");

            Join<Variants_78_4, LocationType> variantsLocationTypeJoin = root.join(Variants_78_4_.locationType, JoinType.LEFT);
            predicates.add(critBuilder.not(variantsLocationTypeJoin.get(LocationType_.name).in(locationTypeNames)));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public List<Variants_78_4> findAlmostCertainlyBenign(Integer hgmdVersion, Integer dxId, Integer diagnosticListVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findLikelyPathogenic(Integer)");
        List<Variants_78_4> ret = new ArrayList<Variants_78_4>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = variantsAnnotationGeneJoin
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(
                    critBuilder.equal(annotationGeneDiagnosticGeneJoin.get(DiagnosticGene_.diagnosticListVersion), diagnosticListVersion));

            Join<DiagnosticGene, DX> annotationGeneDiagnosticCodeJoin = annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx,
                    JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticCodeJoin.get(DX_.id), dxId));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.greaterThanOrEqualTo(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.1));

            List<String> locationTypeNames = new ArrayList<>();
            locationTypeNames.add("UTR-5");
            locationTypeNames.add("UTR-3");
            locationTypeNames.add("UTR");
            locationTypeNames.add("exon");
            locationTypeNames.add("intron/exon boundary");

            Join<Variants_78_4, LocationType> variantsLocationTypeJoin = root.join(Variants_78_4_.locationType, JoinType.LEFT);
            predicates.add(variantsLocationTypeJoin.get(LocationType_.name).in(locationTypeNames));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<Variants_78_4, AnnotationGene> variantsAnnotationGeneJoin = root.join(Variants_78_4_.gene, JoinType.LEFT);
            Join<AnnotationGene, DiagnosticGene> annotationGeneDiagnosticGeneJoin = variantsAnnotationGeneJoin
                    .join(AnnotationGene_.diagnosticGenes, JoinType.LEFT);
            predicates.add(
                    critBuilder.equal(annotationGeneDiagnosticGeneJoin.get(DiagnosticGene_.diagnosticListVersion), diagnosticListVersion));

            Join<DiagnosticGene, DX> annotationGeneDiagnosticCodeJoin = annotationGeneDiagnosticGeneJoin.join(DiagnosticGene_.dx,
                    JoinType.LEFT);
            predicates.add(critBuilder.equal(annotationGeneDiagnosticCodeJoin.get(DX_.id), dxId));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);
            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs,
                    JoinType.LEFT);
            predicates.add(critBuilder.greaterThanOrEqualTo(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.1));

            List<String> locationTypeNames = new ArrayList<>();
            locationTypeNames.add("UTR-5");
            locationTypeNames.add("UTR-3");
            locationTypeNames.add("UTR");
            locationTypeNames.add("exon");
            locationTypeNames.add("intron/exon boundary");

            Join<Variants_78_4, LocationType> variantsLocationTypeJoin = root.join(Variants_78_4_.locationType, JoinType.LEFT);
            predicates.add(critBuilder.not(variantsLocationTypeJoin.get(LocationType_.name).in(locationTypeNames)));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.notEqual(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;

    }

    @Override
    public List<Variants_78_4> findByGeneName(String name) throws BinningDAOException {
        logger.debug("ENTERING findByGeneName(String)");
        TypedQuery<Variants_78_4> query = getEntityManager().createNamedQuery("Variants_78_4.findByGeneName", Variants_78_4.class);
        query.setParameter("geneName", name);
        List<Variants_78_4> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<Variants_78_4> findByGeneNameAndMaxAlleleFrequency(String name, Double threshold) throws BinningDAOException {
        logger.debug("ENTERING findByGeneNameAndMaxAlleleFrequency(String, Double)");
        List<Variants_78_4> ret = new ArrayList<Variants_78_4>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.equal(root.get(Variants_78_4_.hgncGene), name));

            Join<Variants_78_4, LocatedVariant> variantsLocatedVariantJoin = root.join(Variants_78_4_.locatedVariant, JoinType.LEFT);

            Join<LocatedVariant, MaxVariantFrequency> LocatedVariantMaxVariantFrequencyJoin = variantsLocatedVariantJoin
                    .join(LocatedVariant_.maxVariantFrequencies);

            Coalesce<Double> maxVariantFrequencyCoalesce = critBuilder.coalesce();
            maxVariantFrequencyCoalesce.value(LocatedVariantMaxVariantFrequencyJoin.get(MaxVariantFrequency_.maxAlleleFrequency));
            maxVariantFrequencyCoalesce.value(0D);

            predicates.add(critBuilder.lessThanOrEqualTo(maxVariantFrequencyCoalesce, threshold));

            Join<LocatedVariant, MaxFrequency> LocatedVariantMaxFreqJoin = variantsLocatedVariantJoin.join(LocatedVariant_.maxFreqs);

            Coalesce<Double> maxFreqCoalesce = critBuilder.coalesce();
            maxFreqCoalesce.value(LocatedVariantMaxFreqJoin.get(MaxFrequency_.maxAlleleFreq));
            maxFreqCoalesce.value(0D);

            predicates.add(critBuilder.lessThanOrEqualTo(maxFreqCoalesce, threshold));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findByAssemblyId(Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findByAssemblyId(Integer)");
        List<Variants_78_4> ret = new ArrayList<Variants_78_4>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Variants_78_4> crit = critBuilder.createQuery(getPersistentClass());
            Root<Variants_78_4> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<AssemblyLocatedVariant, Assembly> assemblyLocatedVariantAssemblyJoin = root
                    .join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT)
                    .join(AssemblyLocatedVariant_.assembly, JoinType.LEFT);
            predicates.add(critBuilder.equal(assemblyLocatedVariantAssemblyJoin.get(Assembly_.id), assemblyId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<Variants_78_4> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<Variants_78_4> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Variants_78_4> findByTranscriptAccession(String accession) throws BinningDAOException {
        logger.debug("ENTERING findByTranscriptAccession(String)");
        TypedQuery<Variants_78_4> query = getEntityManager().createNamedQuery("Variants_78_4.findByTranscriptAccession",
                Variants_78_4.class);
        query.setParameter("transcr", accession);
        List<Variants_78_4> ret = query.getResultList();
        return ret;
    }

    @Override
    public Long findByAssemblyIdAndVariantEffect(Integer assemblyId, String variantEffect) throws BinningDAOException {
        logger.debug("ENTERING findByAssemblyIdAndVariantEffect(Integer, String)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> crit = critBuilder.createQuery(Long.class);
        Root<Variants_78_4> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(critBuilder.equal(root.join(Variants_78_4_.variantEffect, JoinType.LEFT).get(VariantEffect_.name), variantEffect));
        predicates.add(critBuilder
                .equal(root.join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT)
                        .join(AssemblyLocatedVariant_.assembly, JoinType.LEFT).get(Assembly_.id), assemblyId));
        crit.select(critBuilder.countDistinct(root.join(Variants_78_4_.locatedVariant).get(LocatedVariant_.id)));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Long> query = getEntityManager().createQuery(crit);
        Long ret = query.getSingleResult();
        return ret;
    }

    @Override
    public Long findByAssemblyIdAndVariantType(Integer assemblyId, String variantType) throws BinningDAOException {
        logger.debug("ENTERING findByAssemblyIdAndLocationType(Integer, String)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> crit = critBuilder.createQuery(Long.class);
        Root<Variants_78_4> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(critBuilder.equal(root.join(Variants_78_4_.variantType, JoinType.LEFT).get(VariantType_.name), variantType));
        predicates.add(critBuilder
                .equal(root.join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT)
                        .join(AssemblyLocatedVariant_.assembly, JoinType.LEFT).get(Assembly_.id), assemblyId));
        crit.select(critBuilder.countDistinct(root.join(Variants_78_4_.locatedVariant).get(LocatedVariant_.id)));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Long> query = getEntityManager().createQuery(crit);
        Long ret = query.getSingleResult();
        return ret;
    }

    @Override
    public Long findTranscriptDependentCount(Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findTranscriptDependentCount(Integer)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> crit = critBuilder.createQuery(Long.class);
        Root<Variants_78_4> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();

        predicates.add(root.join(Variants_78_4_.locationType, JoinType.LEFT).get(LocationType_.name)
                .in(Arrays.asList("exon", "intron/exon boundary")));
        predicates.add(critBuilder
                .equal(root.join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT)
                        .join(AssemblyLocatedVariant_.assembly, JoinType.LEFT).get(Assembly_.id), assemblyId));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        crit.select(critBuilder.count(root.get(Variants_78_4_.locationType)));
        crit.groupBy(root.get(Variants_78_4_.locatedVariant));
        crit.having(critBuilder.gt(critBuilder.count(root.get(Variants_78_4_.locationType)), 1));

        List<Long> results = new ArrayList<>();
        TypedQuery<Long> query = getEntityManager().createQuery(crit);
        results.addAll(query.getResultList());
        Long sum = results.stream().mapToLong(Long::intValue).sum();
        return sum;
    }

    @Override
    public Long findCodingCount(Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findCodingCount(Integer)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> crit = critBuilder.createQuery(Long.class);
        Root<Variants_78_4> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();

        predicates.add(root.join(Variants_78_4_.locationType, JoinType.LEFT).get(LocationType_.name)
                .in(Arrays.asList("exon", "intron/exon boundary")));
        predicates.add(critBuilder
                .equal(root.join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT)
                        .join(AssemblyLocatedVariant_.assembly, JoinType.LEFT).get(Assembly_.id), assemblyId));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        crit.select(critBuilder.count(root.get(Variants_78_4_.locationType)));
        crit.groupBy(root.get(Variants_78_4_.locatedVariant));
        crit.having(critBuilder.lessThanOrEqualTo(critBuilder.count(root.get(Variants_78_4_.locationType)), 1L));

        List<Long> results = new ArrayList<>();
        TypedQuery<Long> query = getEntityManager().createQuery(crit);
        results.addAll(query.getResultList());
        Long sum = results.stream().mapToLong(Long::intValue).sum();
        return sum;
    }

    @Override
    public Long findNonCodingCount(Integer assemblyId) throws BinningDAOException {
        logger.debug("ENTERING findNonCodingCount(Integer)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> crit = critBuilder.createQuery(Long.class);
        Root<Variants_78_4> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();

        predicates.add(root.join(Variants_78_4_.locationType, JoinType.LEFT).get(LocationType_.name)
                .in(Arrays.asList("UTR", "UTR-3", "UTR-5", "intron", "intergenic", "potential RNA-editing site")));
        predicates.add(critBuilder
                .equal(root.join(Variants_78_4_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT)
                        .join(AssemblyLocatedVariant_.assembly, JoinType.LEFT).get(Assembly_.id), assemblyId));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        crit.select(critBuilder.count(root.get(Variants_78_4_.locationType)));
        crit.groupBy(root.get(Variants_78_4_.locatedVariant));

        List<Long> results = new ArrayList<>();
        TypedQuery<Long> query = getEntityManager().createQuery(crit);
        results.addAll(query.getResultList());
        Long sum = results.stream().mapToLong(Long::intValue).sum();
        return sum;
    }

}
