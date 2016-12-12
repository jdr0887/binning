package org.renci.binning.dao.jpa.refseq;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.RegionGroupDAO;
import org.renci.binning.dao.refseq.model.Feature;
import org.renci.binning.dao.refseq.model.Feature_;
import org.renci.binning.dao.refseq.model.RegionGroup;
import org.renci.binning.dao.refseq.model.RegionGroup_;
import org.renci.binning.dao.refseq.model.Transcript;
import org.renci.binning.dao.refseq.model.Transcript_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { RegionGroupDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class RegionGroupDAOImpl extends BaseDAOImpl<RegionGroup, Long> implements RegionGroupDAO {

    private static final Logger logger = LoggerFactory.getLogger(RegionGroupDAOImpl.class);

    public RegionGroupDAOImpl() {
        super();
    }

    @Override
    public Class<RegionGroup> getPersistentClass() {
        return RegionGroup.class;
    }

    @Override
    public List<RegionGroup> findByRefSeqVersionAndTranscriptId(String refSeqVersion, String transcriptId) throws BinningDAOException {
        logger.debug("ENTERING findByRefSeqVersionAndTranscriptId(String, String)");
        List<RegionGroup> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<RegionGroup> crit = critBuilder.createQuery(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<RegionGroup> fromRegionGroup = crit.from(getPersistentClass());
            Join<RegionGroup, Transcript> regionGroupTranscriptJoin = fromRegionGroup.join(RegionGroup_.transcript);
            predicates.add(critBuilder.equal(regionGroupTranscriptJoin.get(Transcript_.versionId), transcriptId));
            SetJoin<RegionGroup, Feature> regionGroupFeatureJoin = fromRegionGroup.join(RegionGroup_.features);
            predicates.add(critBuilder.equal(regionGroupFeatureJoin.get(Feature_.refseqVer), refSeqVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<RegionGroup> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
