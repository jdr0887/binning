package org.renci.binning.dao.jpa.clinbin;

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
import org.renci.binning.dao.clinbin.BinResultsFinalDiagnosticDAO;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnosticPK;
import org.renci.binning.dao.clinbin.model.DX;
import org.renci.binning.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnosticPK_;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic_;
import org.renci.binning.dao.clinbin.model.DX_;
import org.renci.binning.dao.clinbin.model.DiagnosticResultVersion_;
import org.renci.binning.dao.clinbin.model.DiseaseClass_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class BinResultsFinalDiagnosticDAOImpl extends BaseDAOImpl<BinResultsFinalDiagnostic, BinResultsFinalDiagnosticPK>
        implements BinResultsFinalDiagnosticDAO {

    private static final Logger logger = LoggerFactory.getLogger(BinResultsFinalDiagnosticDAOImpl.class);

    public BinResultsFinalDiagnosticDAOImpl() {
        super();
    }

    @Override
    public Class<BinResultsFinalDiagnostic> getPersistentClass() {
        return BinResultsFinalDiagnostic.class;
    }

    @Override
    public List<BinResultsFinalDiagnostic> findByLocatedVariantId(Long locatedVariantId) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantId(Long)");
        List<BinResultsFinalDiagnostic> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<BinResultsFinalDiagnostic> crit = critBuilder.createQuery(getPersistentClass());
            Root<BinResultsFinalDiagnostic> root = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.equal(root.get(BinResultsFinalDiagnostic_.key).get(BinResultsFinalDiagnosticPK_.locatedVariant),
                    locatedVariantId));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);

            TypedQuery<BinResultsFinalDiagnostic> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<BinResultsFinalDiagnostic> findByDXIdAndParticipantAndVersion(Long dxId, String participant, Integer version)
            throws BinningDAOException {
        logger.debug("ENTERING findByDXIdAndParticipantAndListVersion(Long, String, Integer)");

        List<BinResultsFinalDiagnostic> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<BinResultsFinalDiagnostic> crit = critBuilder.createQuery(getPersistentClass());
            Root<BinResultsFinalDiagnostic> root = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<BinResultsFinalDiagnostic, BinResultsFinalDiagnosticPK> binResultsFinalDiagnosticBinResultsFinalDiagnosticPKJoin = root
                    .join(BinResultsFinalDiagnostic_.key);
            predicates.add(critBuilder.equal(
                    binResultsFinalDiagnosticBinResultsFinalDiagnosticPKJoin.get(BinResultsFinalDiagnosticPK_.participant), participant));

            Join<BinResultsFinalDiagnostic, DX> binResultsFinalDiagnosticDXJoin = root.join(BinResultsFinalDiagnostic_.dx);
            predicates.add(critBuilder.equal(binResultsFinalDiagnosticDXJoin.get(DX_.id), dxId));

            Join<BinResultsFinalDiagnostic, DiagnosticResultVersion> coverageExonJoin = root
                    .join(BinResultsFinalDiagnostic_.diagnosticResultVersion);
            predicates.add(critBuilder.equal(coverageExonJoin.get(DiagnosticResultVersion_.diagnosticResultVersion), version));

            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<BinResultsFinalDiagnostic> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public Long findDXIdCount(String participant) throws BinningDAOException {
        logger.debug("ENTERING findDXIdCount(String)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> crit = critBuilder.createQuery(Long.class);
        Root<BinResultsFinalDiagnostic> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(
                critBuilder.equal(root.get(BinResultsFinalDiagnostic_.key).get(BinResultsFinalDiagnosticPK_.participant), participant));
        crit.select(critBuilder.countDistinct(root.get(BinResultsFinalDiagnostic_.dx).get(DX_.id)));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Long> query = getEntityManager().createQuery(crit);
        Long ret = query.getSingleResult();
        return ret;
    }

    @Override
    public Long findByAssemblyIdAndDiseaseClassId(Integer assemblyId, Integer diseaseClassId) throws BinningDAOException {
        logger.debug("ENTERING findByAssemblyIdAndDiseaseClassId(Integer, Integer)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> crit = critBuilder.createQuery(Long.class);
        Root<BinResultsFinalDiagnostic> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(critBuilder.equal(root.get(BinResultsFinalDiagnostic_.key).get(BinResultsFinalDiagnosticPK_.assembly), assemblyId));
        predicates.add(critBuilder.equal(root.join(BinResultsFinalDiagnostic_.diseaseClass).get(DiseaseClass_.id), diseaseClassId));
        crit.select(critBuilder.countDistinct(root.get(BinResultsFinalDiagnostic_.key).get(BinResultsFinalDiagnosticPK_.locatedVariant)));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Long> query = getEntityManager().createQuery(crit);
        Long ret = query.getSingleResult();
        return ret;
    }

    @Override
    public Long findAnalyzedVariantsCount(String participant) throws BinningDAOException {
        logger.debug("ENTERING findAnalyzedVariantsCount(String)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> crit = critBuilder.createQuery(Long.class);
        Root<BinResultsFinalDiagnostic> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(
                critBuilder.equal(root.get(BinResultsFinalDiagnostic_.key).get(BinResultsFinalDiagnosticPK_.participant), participant));
        crit.select(critBuilder.countDistinct(root.get(BinResultsFinalDiagnostic_.key).get(BinResultsFinalDiagnosticPK_.locatedVariant)));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Long> query = getEntityManager().createQuery(crit);
        Long ret = query.getSingleResult();
        return ret;
    }

    @Transactional
    @Override
    public void deleteByAssemblyId(Integer assemblyId) throws BinningDAOException {
        Query qDelete = getEntityManager()
                .createQuery("delete from " + getPersistentClass().getSimpleName() + " a where a.assembly.id = :assemblyId");
        qDelete.setParameter("assemblyId", assemblyId);
        qDelete.executeUpdate();
    }

    @Transactional
    @Override
    public synchronized BinResultsFinalDiagnosticPK save(BinResultsFinalDiagnostic entity) throws BinningDAOException {
        logger.debug("ENTERING save(BinResultsFinalDiagnostic)");
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

}
