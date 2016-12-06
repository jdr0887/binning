package org.renci.binning.dao.jpa.refseq;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.RegionGroupRegionDAO;
import org.renci.binning.dao.refseq.model.RefSeqCodingSequence;
import org.renci.binning.dao.refseq.model.RegionGroup;
import org.renci.binning.dao.refseq.model.RegionGroupRegion;
import org.renci.binning.dao.refseq.model.RefSeqCodingSequence_;
import org.renci.binning.dao.refseq.model.RegionGroupRegion_;
import org.renci.binning.dao.refseq.model.RegionGroup_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class RegionGroupRegionDAOImpl extends BaseDAOImpl<RegionGroupRegion, Integer> implements RegionGroupRegionDAO {

    private static final Logger logger = LoggerFactory.getLogger(RegionGroupRegionDAOImpl.class);

    public RegionGroupRegionDAOImpl() {
        super();
    }

    @Override
    public Class<RegionGroupRegion> getPersistentClass() {
        return RegionGroupRegion.class;
    }

    @Override
    public List<RegionGroupRegion> findByRegionGroupId(Integer regionGroupId) throws BinningDAOException {
        logger.debug("ENTERING findByRegionGroupId(Long)");
        List<RegionGroupRegion> ret = new ArrayList<RegionGroupRegion>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<RegionGroupRegion> crit = critBuilder.createQuery(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<RegionGroupRegion> fromRegionGroupRegion = crit.from(getPersistentClass());
            Join<RegionGroupRegion, RegionGroup> regionGroupRegionGroupRegionJoin = fromRegionGroupRegion
                    .join(RegionGroupRegion_.regionGroup);
            predicates.add(critBuilder.equal(regionGroupRegionGroupRegionJoin.get(RegionGroup_.regionGroupId), regionGroupId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<RegionGroupRegion> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<RegionGroupRegion> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<RegionGroupRegion> findByRefSeqCodingSequenceId(Integer refSeqCodingSequenceId) throws BinningDAOException {
        logger.debug("ENTERING findByRegionGroupId(Long)");
        List<RegionGroupRegion> ret = new ArrayList<RegionGroupRegion>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<RegionGroupRegion> crit = critBuilder.createQuery(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<RegionGroupRegion> fromRegionGroupRegion = crit.from(getPersistentClass());

            Join<RegionGroupRegion, RegionGroup> regionGroupRegionGroupRegionJoin = fromRegionGroupRegion
                    .join(RegionGroupRegion_.regionGroup);
            Join<RegionGroup, RefSeqCodingSequence> regionGroupRefSeqCodingSequenceJoin = regionGroupRegionGroupRegionJoin
                    .join(RegionGroup_.refSeqCodingSequence);
            predicates.add(critBuilder.equal(regionGroupRefSeqCodingSequenceJoin.get(RefSeqCodingSequence_.id), refSeqCodingSequenceId));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<RegionGroupRegion> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<RegionGroupRegion> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
