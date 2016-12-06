package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DXCoverageDAO;
import org.renci.binning.dao.clinbin.model.DX;
import org.renci.binning.dao.clinbin.model.DXCoverage;
import org.renci.binning.dao.clinbin.model.DXCoveragePK;
import org.renci.binning.dao.clinbin.model.DXExons;
import org.renci.binning.dao.clinbin.model.DiagnosticGene;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.clinbin.model.DXCoveragePK_;
import org.renci.binning.dao.clinbin.model.DXCoverage_;
import org.renci.binning.dao.clinbin.model.DXExons_;
import org.renci.binning.dao.clinbin.model.DX_;
import org.renci.binning.dao.clinbin.model.DiagnosticGene_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class DXCoverageDAOImpl extends BaseDAOImpl<DXCoverage, DXCoveragePK> implements DXCoverageDAO {

    private static final Logger logger = LoggerFactory.getLogger(DXCoverageDAOImpl.class);

    public DXCoverageDAOImpl() {
        super();
    }

    @Override
    public Class<DXCoverage> getPersistentClass() {
        return DXCoverage.class;
    }

    @Override
    public List<DXCoverage> findByDXIdAndParticipantAndListVersion(Long dxId, String participant, Integer listVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findByDXIdAndParticipantAndListVersion(Long, String, Integer)");
        List<DXCoverage> ret = new ArrayList<>();
        try {

            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DXCoverage> crit = critBuilder.createQuery(getPersistentClass());
            Root<DXCoverage> root = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(DXCoverage_.key).get(DXCoveragePK_.participant), participant));

            Join<DXCoverage, DXExons> coverageExonJoin = root.join(DXCoverage_.exon);
            predicates.add(critBuilder.equal(coverageExonJoin.get(DXExons_.listVersion), listVersion));

            Join<DXExons, DiagnosticGene> coverageDiagnosticGeneJoin = coverageExonJoin.join(DXExons_.gene);
            Join<DiagnosticGene, DX> diagnosticGeneDXJoin = coverageDiagnosticGeneJoin.join(DiagnosticGene_.dx);
            predicates.add(critBuilder.equal(diagnosticGeneDXJoin.get(DX_.id), dxId));
            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<DXCoverage> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<DXCoverage> findByParticipantAndListVersion(String participant, Integer listVersion) throws BinningDAOException {
        logger.debug("ENTERING findByParticipantAndListVersion(String, Integer)");
        List<DXCoverage> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DXCoverage> crit = critBuilder.createQuery(getPersistentClass());
            Root<DXCoverage> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(DXCoverage_.key).get(DXCoveragePK_.participant), participant));
            predicates.add(critBuilder.equal(root.join(DXCoverage_.exon, JoinType.LEFT).get(DXExons_.listVersion), listVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<DXCoverage> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    @Transactional
    public DXCoveragePK save(DXCoverage entity) throws BinningDAOException {
        logger.debug("ENTERING save(DXCoverage)");
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
