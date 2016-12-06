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
import org.renci.binning.dao.clinbin.IncidentalBinningJobDAO;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.dao.clinbin.model.IncidentalStatusType;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob_;
import org.renci.binning.dao.clinbin.model.IncidentalStatusType_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class IncidentalBinningJobDAOImpl extends BaseDAOImpl<IncidentalBinningJob, Integer> implements IncidentalBinningJobDAO {

    private static final Logger logger = LoggerFactory.getLogger(IncidentalBinningJobDAOImpl.class);

    public IncidentalBinningJobDAOImpl() {
        super();
    }

    @Override
    public Class<IncidentalBinningJob> getPersistentClass() {
        return IncidentalBinningJob.class;
    }

    @Override
    public List<IncidentalBinningJob> findAvailableJobs() throws BinningDAOException {
        logger.debug("ENTERING findProcessingJobs()");
        List<IncidentalBinningJob> ret = new ArrayList<IncidentalBinningJob>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IncidentalBinningJob> crit = critBuilder.createQuery(getPersistentClass());
            Root<IncidentalBinningJob> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<IncidentalBinningJob, IncidentalStatusType> incidentalBinningJobIncidentalStatusTypeJoin = root
                    .join(IncidentalBinningJob_.status);
            predicates.add(critBuilder
                    .not(incidentalBinningJobIncidentalStatusTypeJoin.get(IncidentalStatusType_.name).in("Complete", "Failed", "Paused")));
            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<IncidentalBinningJob> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<IncidentalBinningJob> findCompletedJobs() throws BinningDAOException {
        logger.debug("ENTERING findProcessingJobs()");
        List<IncidentalBinningJob> ret = new ArrayList<IncidentalBinningJob>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IncidentalBinningJob> crit = critBuilder.createQuery(getPersistentClass());
            Root<IncidentalBinningJob> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<IncidentalBinningJob, IncidentalStatusType> incidentalBinningJobIncidentalStatusTypeJoin = root
                    .join(IncidentalBinningJob_.status);
            predicates.add(critBuilder.equal(incidentalBinningJobIncidentalStatusTypeJoin.get(IncidentalStatusType_.name), "Complete"));
            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<IncidentalBinningJob> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<IncidentalBinningJob> findCompletedJobsByStudy(String study) throws BinningDAOException {
        logger.debug("ENTERING findCompletedJobsByStudy(String)");
        List<IncidentalBinningJob> ret = new ArrayList<IncidentalBinningJob>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IncidentalBinningJob> crit = critBuilder.createQuery(getPersistentClass());
            Root<IncidentalBinningJob> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(IncidentalBinningJob_.study), study));
            Join<IncidentalBinningJob, IncidentalStatusType> incidentalBinningJobIncidentalStatusTypeJoin = root
                    .join(IncidentalBinningJob_.status);
            predicates.add(critBuilder.equal(incidentalBinningJobIncidentalStatusTypeJoin.get(IncidentalStatusType_.name), "Complete"));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<IncidentalBinningJob> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Transactional
    @Override
    public synchronized Integer save(IncidentalBinningJob entity) throws BinningDAOException {
        logger.debug("ENTERING save(IncidentalBinningJob)");
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
