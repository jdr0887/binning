package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DiagnosticBinningJobDAO;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.clinbin.model.DiagnosticStatusType;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob_;
import org.renci.binning.dao.clinbin.model.DiagnosticStatusType_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class DiagnosticBinningJobDAOImpl extends BaseDAOImpl<DiagnosticBinningJob, Integer> implements DiagnosticBinningJobDAO {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticBinningJobDAOImpl.class);

    public DiagnosticBinningJobDAOImpl() {
        super();
    }

    @Override
    public Class<DiagnosticBinningJob> getPersistentClass() {
        return DiagnosticBinningJob.class;
    }

    @Override
    public List<DiagnosticBinningJob> findAvailableJobs() throws BinningDAOException {
        logger.debug("ENTERING findProcessingJobs()");
        List<DiagnosticBinningJob> ret = new ArrayList<DiagnosticBinningJob>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DiagnosticBinningJob> crit = critBuilder.createQuery(getPersistentClass());
            Root<DiagnosticBinningJob> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<DiagnosticBinningJob, DiagnosticStatusType> diagnosticBinningJobDiagnosticStatusTypeJoin = root
                    .join(DiagnosticBinningJob_.status);
            predicates.add(critBuilder
                    .not(diagnosticBinningJobDiagnosticStatusTypeJoin.get(DiagnosticStatusType_.name).in("Complete", "Failed", "Paused")));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<DiagnosticBinningJob> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<DiagnosticBinningJob> findCompletedJobs() throws BinningDAOException {
        logger.debug("ENTERING findCompletedJobs()");
        List<DiagnosticBinningJob> ret = new ArrayList<DiagnosticBinningJob>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DiagnosticBinningJob> crit = critBuilder.createQuery(getPersistentClass());
            Root<DiagnosticBinningJob> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<DiagnosticBinningJob, DiagnosticStatusType> diagnosticBinningJobDiagnosticStatusTypeJoin = root
                    .join(DiagnosticBinningJob_.status);
            predicates.add(critBuilder.equal(diagnosticBinningJobDiagnosticStatusTypeJoin.get(DiagnosticStatusType_.name), "Complete"));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<DiagnosticBinningJob> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<DiagnosticBinningJob> findCompletedJobsByStudy(String study) throws BinningDAOException {
        logger.debug("ENTERING findCompletedJobsByStudy(String)");
        List<DiagnosticBinningJob> ret = new ArrayList<DiagnosticBinningJob>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DiagnosticBinningJob> crit = critBuilder.createQuery(getPersistentClass());
            Root<DiagnosticBinningJob> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(DiagnosticBinningJob_.study), study));
            Join<DiagnosticBinningJob, DiagnosticStatusType> diagnosticBinningJobDiagnosticStatusTypeJoin = root
                    .join(DiagnosticBinningJob_.status);
            predicates.add(critBuilder.equal(diagnosticBinningJobDiagnosticStatusTypeJoin.get(DiagnosticStatusType_.name), "Complete"));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<DiagnosticBinningJob> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Transactional
    @Override
    public synchronized Integer save(DiagnosticBinningJob entity) throws BinningDAOException {
        logger.debug("ENTERING save(DiagnosticBinningJob)");
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
}
