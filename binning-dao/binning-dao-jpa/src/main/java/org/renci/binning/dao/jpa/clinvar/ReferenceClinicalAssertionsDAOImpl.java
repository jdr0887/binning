package org.renci.binning.dao.jpa.clinvar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnosticPK;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalX;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalXPK;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskX;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskXPK;
import org.renci.binning.dao.clinbin.model.DX;
import org.renci.binning.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.binning.dao.clinbin.model.IncidentalBinX;
import org.renci.binning.dao.clinbin.model.IncidentalResultVersionX;
import org.renci.binning.dao.clinvar.ReferenceClinicalAssertionsDAO;
import org.renci.binning.dao.clinvar.model.ReferenceClinicalAssertions;
import org.renci.binning.dao.clinvar.model.Versions;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnosticPK_;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic_;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalXPK_;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalX_;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskXPK_;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskX_;
import org.renci.binning.dao.clinbin.model.DX_;
import org.renci.binning.dao.clinbin.model.DiagnosticResultVersion_;
import org.renci.binning.dao.clinbin.model.IncidentalBinX_;
import org.renci.binning.dao.clinbin.model.IncidentalResultVersionX_;
import org.renci.binning.dao.clinvar.model.ReferenceClinicalAssertions_;
import org.renci.binning.dao.clinvar.model.Versions_;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class ReferenceClinicalAssertionsDAOImpl extends BaseDAOImpl<ReferenceClinicalAssertions, Long>
        implements ReferenceClinicalAssertionsDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceClinicalAssertionsDAOImpl.class);

    public ReferenceClinicalAssertionsDAOImpl() {
        super();
    }

    @Override
    public Class<ReferenceClinicalAssertions> getPersistentClass() {
        return ReferenceClinicalAssertions.class;
    }

    @Override
    public List<ReferenceClinicalAssertions> findDiagnostic(Long dxId, String participant, Integer resultVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findDiagnostic(Long, String, Integer)");

        List<ReferenceClinicalAssertions> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<ReferenceClinicalAssertions> crit = critBuilder.createQuery(getPersistentClass());
            Root<ReferenceClinicalAssertions> root = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();

            SetJoin<Versions, DiagnosticResultVersion> versionsDiagnosticResultVersionJoin = root
                    .join(ReferenceClinicalAssertions_.versions, JoinType.LEFT).join(Versions_.diagnosticResultVersions, JoinType.LEFT);

            predicates.add(critBuilder.equal(versionsDiagnosticResultVersionJoin.get(DiagnosticResultVersion_.diagnosticResultVersion),
                    resultVersion));

            Join<DiagnosticResultVersion, BinResultsFinalDiagnostic> diagnosticResultVersionBinResultsFinalDiagnosticJoin = versionsDiagnosticResultVersionJoin
                    .join(DiagnosticResultVersion_.binResultsFinalDiagnostics);

            Join<BinResultsFinalDiagnostic, BinResultsFinalDiagnosticPK> binResultsFinalDiagnosticBinResultsFinalDiagnosticPKJoin = diagnosticResultVersionBinResultsFinalDiagnosticJoin
                    .join(BinResultsFinalDiagnostic_.key);

            predicates.add(critBuilder.equal(
                    binResultsFinalDiagnosticBinResultsFinalDiagnosticPKJoin.get(BinResultsFinalDiagnosticPK_.participant), participant));

            Join<BinResultsFinalDiagnostic, DX> binResultsFinalDiagnosticDXJoin = diagnosticResultVersionBinResultsFinalDiagnosticJoin
                    .join(BinResultsFinalDiagnostic_.dx);

            predicates.add(critBuilder.equal(binResultsFinalDiagnosticDXJoin.get(DX_.id), dxId));

            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<ReferenceClinicalAssertions> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<ReferenceClinicalAssertions> findIncidental(Long incidentalBinId, String participant, Integer resultVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findByIncidental(Long, String, Integer)");

        List<ReferenceClinicalAssertions> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<ReferenceClinicalAssertions> crit = critBuilder.createQuery(getPersistentClass());
            Root<ReferenceClinicalAssertions> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            SetJoin<Versions, IncidentalResultVersionX> versionsIncidentalResultVersionXJoin = root
                    .join(ReferenceClinicalAssertions_.versions, JoinType.LEFT).join(Versions_.incidentalResultVersions, JoinType.LEFT);

            predicates.add(critBuilder.equal(versionsIncidentalResultVersionXJoin.get(IncidentalResultVersionX_.binningResultVersion),
                    resultVersion));

            Join<IncidentalResultVersionX, BinResultsFinalIncidentalX> incidentalResultVersionXBinResultsFinalIncidentalXJoin = versionsIncidentalResultVersionXJoin
                    .join(IncidentalResultVersionX_.binResultsFinalIncidentals, JoinType.LEFT);
            Join<BinResultsFinalIncidentalX, BinResultsFinalIncidentalXPK> binResultsFinalIncidentalXBinResultsFinalIncidentalXPKJoin = incidentalResultVersionXBinResultsFinalIncidentalXJoin
                    .join(BinResultsFinalIncidentalX_.key, JoinType.LEFT);
            predicates.add(critBuilder.equal(
                    binResultsFinalIncidentalXBinResultsFinalIncidentalXPKJoin.get(BinResultsFinalIncidentalXPK_.participant),
                    participant));

            Join<BinResultsFinalIncidentalX, IncidentalBinX> binResultsFinalIncidentalXIncidentalBinXJoin = incidentalResultVersionXBinResultsFinalIncidentalXJoin
                    .join(BinResultsFinalIncidentalX_.incidentalBin);

            predicates.add(critBuilder.equal(binResultsFinalIncidentalXIncidentalBinXJoin.get(IncidentalBinX_.id), incidentalBinId));

            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<ReferenceClinicalAssertions> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<ReferenceClinicalAssertions> findRisk(Long incidentalBinId, String participant, Integer resultVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findRisk(Long, String, Integer)");

        List<ReferenceClinicalAssertions> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<ReferenceClinicalAssertions> crit = critBuilder.createQuery(getPersistentClass());
            Root<ReferenceClinicalAssertions> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            SetJoin<Versions, IncidentalResultVersionX> versionsIncidentalResultVersionXJoin = root
                    .join(ReferenceClinicalAssertions_.versions, JoinType.LEFT).join(Versions_.incidentalResultVersions, JoinType.LEFT);
            predicates.add(critBuilder.equal(versionsIncidentalResultVersionXJoin.get(IncidentalResultVersionX_.binningResultVersion),
                    resultVersion));

            Join<IncidentalResultVersionX, BinResultsFinalRiskX> incidentalResultVersionXBinResultsFinalRiskXJoin = versionsIncidentalResultVersionXJoin
                    .join(IncidentalResultVersionX_.binResultsFinalRisks, JoinType.LEFT);
            Join<BinResultsFinalRiskX, BinResultsFinalRiskXPK> binResultsFinalRiskXBinResultsFinalRiskXPKJoin = incidentalResultVersionXBinResultsFinalRiskXJoin
                    .join(BinResultsFinalRiskX_.key, JoinType.LEFT);
            predicates.add(critBuilder.equal(binResultsFinalRiskXBinResultsFinalRiskXPKJoin.get(BinResultsFinalRiskXPK_.participant),
                    participant));

            Join<BinResultsFinalRiskX, IncidentalBinX> binResultsFinalIncidentalXIncidentalBinXJoin = incidentalResultVersionXBinResultsFinalRiskXJoin
                    .join(BinResultsFinalRiskX_.incidentalBin);

            predicates.add(critBuilder.equal(binResultsFinalIncidentalXIncidentalBinXJoin.get(IncidentalBinX_.id), incidentalBinId));

            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<ReferenceClinicalAssertions> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<ReferenceClinicalAssertions> findByLocatedVariantIdAndVersion(Long locVarId, Integer version) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, Integer)");
        List<ReferenceClinicalAssertions> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<ReferenceClinicalAssertions> crit = critBuilder.createQuery(getPersistentClass());
            Root<ReferenceClinicalAssertions> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.equal(root.join(ReferenceClinicalAssertions_.locatedVariant, JoinType.LEFT).get(LocatedVariant_.id),
                    locVarId));

            predicates.add(critBuilder.equal(root.join(ReferenceClinicalAssertions_.versions, JoinType.LEFT).get(Versions_.id), version));

            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<ReferenceClinicalAssertions> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<ReferenceClinicalAssertions> findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(Long locVarId, Long version,
            List<String> assertionStatusExcludes) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(Long, Integer, List<String>)");
        List<ReferenceClinicalAssertions> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<ReferenceClinicalAssertions> crit = critBuilder.createQuery(getPersistentClass());
            Root<ReferenceClinicalAssertions> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.equal(root.join(ReferenceClinicalAssertions_.locatedVariant, JoinType.LEFT).get(LocatedVariant_.id),
                    locVarId));

            predicates.add(critBuilder.not(root.get(ReferenceClinicalAssertions_.assertionStatus).in(assertionStatusExcludes)));

            predicates.add(critBuilder.equal(root.join(ReferenceClinicalAssertions_.versions, JoinType.LEFT).get(Versions_.id), version));

            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<ReferenceClinicalAssertions> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
