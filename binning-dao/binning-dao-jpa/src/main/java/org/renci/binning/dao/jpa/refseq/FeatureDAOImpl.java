package org.renci.binning.dao.jpa.refseq;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.FeatureDAO;
import org.renci.binning.dao.refseq.model.Feature;
import org.renci.binning.dao.refseq.model.Feature_;
import org.renci.binning.dao.refseq.model.RefSeqCodingSequence_;
import org.renci.binning.dao.refseq.model.RegionGroup_;
import org.renci.binning.dao.refseq.model.Transcript_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class FeatureDAOImpl extends BaseDAOImpl<Feature, Long> implements FeatureDAO {

    private static final Logger logger = LoggerFactory.getLogger(FeatureDAOImpl.class);

    public FeatureDAOImpl() {
        super();
    }

    @Override
    public Class<Feature> getPersistentClass() {
        return Feature.class;
    }

    @Override
    public List<Feature> findByRefSeqCodingSequenceId(Integer refSeqCodingSequenceId) throws BinningDAOException {
        logger.debug("ENTERING findByRefSeqCodingSequenceId(Integer)");
        List<Feature> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Feature> crit = critBuilder.createQuery(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<Feature> fromFeature = crit.from(getPersistentClass());
            predicates.add(critBuilder.equal(
                    fromFeature.join(Feature_.regionGroup).join(RegionGroup_.refSeqCodingSequence).get(RefSeqCodingSequence_.id),
                    refSeqCodingSequenceId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<Feature> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<Feature> findByRefSeqVersionAndTranscriptId(String refSeqVersion, String versionId) throws BinningDAOException {
        logger.debug("ENTERING findByRefSeqVersionAndTranscriptId(String, String)");
        List<Feature> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Feature> crit = critBuilder.createQuery(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<Feature> fromFeature = crit.from(getPersistentClass());
            predicates.add(critBuilder.equal(fromFeature.get(Feature_.refseqVer), refSeqVersion));
            predicates.add(critBuilder
                    .equal(fromFeature.join(Feature_.regionGroup).join(RegionGroup_.transcript).get(Transcript_.versionId), versionId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<Feature> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
