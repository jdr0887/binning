package org.renci.binning.dao.jpa.refseq;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.ref.model.GenomeRefSeq_;
import org.renci.binning.dao.refseq.TranscriptMapsDAO;
import org.renci.binning.dao.refseq.model.Transcript;
import org.renci.binning.dao.refseq.model.TranscriptMaps;
import org.renci.binning.dao.refseq.model.TranscriptMapsExons;
import org.renci.binning.dao.refseq.model.TranscriptMapsExons_;
import org.renci.binning.dao.refseq.model.TranscriptMaps_;
import org.renci.binning.dao.refseq.model.TranscriptRefSeqVers;
import org.renci.binning.dao.refseq.model.TranscriptRefSeqVers_;
import org.renci.binning.dao.refseq.model.Transcript_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { TranscriptMapsDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class TranscriptMapsDAOImpl extends BaseDAOImpl<TranscriptMaps, Integer> implements TranscriptMapsDAO {

    private static final Logger logger = LoggerFactory.getLogger(TranscriptMapsDAOImpl.class);

    public TranscriptMapsDAOImpl() {
        super();
    }

    @Override
    public Class<TranscriptMaps> getPersistentClass() {
        return TranscriptMaps.class;
    }

    @Override
    public List<TranscriptMaps> findByTranscriptId(String versionId) throws BinningDAOException {
        logger.debug("ENTERING findByTranscriptId(String)");
        return findByTranscriptId("includeManyToOnes", versionId);
    }

    @Override
    public List<TranscriptMaps> findByTranscriptId(String fetchGroup, String versionId) throws BinningDAOException {
        logger.debug("ENTERING findByTranscriptId(String, String)");
        List<TranscriptMaps> ret = new ArrayList<TranscriptMaps>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMaps> crit = critBuilder.createQuery(getPersistentClass());
            Root<TranscriptMaps> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptJoin = root.join(TranscriptMaps_.transcript);
            predicates.add(critBuilder.equal(transcriptMapsTranscriptJoin.get(Transcript_.versionId), versionId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<TranscriptMaps> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<TranscriptMaps> openjpaQuery = OpenJPAPersistence.cast(query);
            if (StringUtils.isNotEmpty(fetchGroup)) {
                openjpaQuery.getFetchPlan().addFetchGroup(fetchGroup);
            }
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(Integer genomeRefId,
            String refseqVersion, String refSeqAccession, Integer position) throws BinningDAOException {
        // this get crazy...you've been warned
        List<TranscriptMaps> ret = new ArrayList<TranscriptMaps>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMaps> crit = critBuilder.createQuery(getPersistentClass());
            Root<TranscriptMaps> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.equal(root.get(TranscriptMaps_.genomeRefId), genomeRefId));

            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptJoin = root.join(TranscriptMaps_.transcript, JoinType.LEFT);
            Join<Transcript, TranscriptRefSeqVers> transcriptTranscriptRefSeqVersJoin = transcriptMapsTranscriptJoin
                    .join(Transcript_.refseqVersions, JoinType.LEFT);
            predicates.add(critBuilder.equal(transcriptTranscriptRefSeqVersJoin.get(TranscriptRefSeqVers_.refseqVer), refseqVersion));

            Join<TranscriptMaps, GenomeRefSeq> transcriptMapsGenomeRefSeqJoin = root.join(TranscriptMaps_.genomeRefSeq, JoinType.LEFT);
            predicates.add(critBuilder.equal(transcriptMapsGenomeRefSeqJoin.get(GenomeRefSeq_.verAccession), refSeqAccession));

            Join<TranscriptMaps, TranscriptMapsExons> plusContigStartTranscriptMapsTranscriptMapsExonsJoin = root
                    .join(TranscriptMaps_.exons, JoinType.LEFT);
            Subquery<Integer> plusContigStartSubquery = crit.subquery(Integer.class);
            Root<TranscriptMapsExons> plusContigStartFromTranscriptMapsExons = plusContigStartSubquery.from(TranscriptMapsExons.class);
            plusContigStartSubquery
                    .where(critBuilder.equal(plusContigStartTranscriptMapsTranscriptMapsExonsJoin.get(TranscriptMapsExons_.key),
                            plusContigStartFromTranscriptMapsExons.get(TranscriptMapsExons_.key)));
            plusContigStartSubquery.select(critBuilder.min(plusContigStartFromTranscriptMapsExons.get(TranscriptMapsExons_.contigStart)));

            Join<TranscriptMaps, TranscriptMapsExons> plusContigEndtranscriptMapsTranscriptMapsExonsJoin = root.join(TranscriptMaps_.exons,
                    JoinType.LEFT);
            Subquery<Integer> plusContigEndSubquery = crit.subquery(Integer.class);
            Root<TranscriptMapsExons> plusContigEndFromTranscriptMapsExons = plusContigEndSubquery.from(TranscriptMapsExons.class);
            plusContigEndSubquery.where(critBuilder.equal(plusContigEndtranscriptMapsTranscriptMapsExonsJoin.get(TranscriptMapsExons_.key),
                    plusContigEndFromTranscriptMapsExons.get(TranscriptMapsExons_.key)));
            plusContigEndSubquery.select(critBuilder.max(plusContigEndFromTranscriptMapsExons.get(TranscriptMapsExons_.contigEnd)));

            Predicate betweenContigStartAndEndPredicate = critBuilder.between(critBuilder.literal(position), plusContigStartSubquery,
                    plusContigEndSubquery);

            // Predicate left = critBuilder.and(critBuilder
            // .equal(critBuilder.function("refseq_strand_to_varchar", String.class, root.get(TranscriptMaps_.strand)),
            // "+"),
            // betweenContigStartAndEndPredicate);

            Predicate left = critBuilder.and(critBuilder.equal(root.get(TranscriptMaps_.strand), "+"), betweenContigStartAndEndPredicate);

            predicates.add(left);

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<TranscriptMaps> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());

        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        // union
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMaps> crit = critBuilder.createQuery(getPersistentClass());
            Root<TranscriptMaps> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.equal(root.get(TranscriptMaps_.genomeRefId), genomeRefId));

            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptJoin = root.join(TranscriptMaps_.transcript, JoinType.LEFT);
            Join<Transcript, TranscriptRefSeqVers> transcriptTranscriptRefSeqVersJoin = transcriptMapsTranscriptJoin
                    .join(Transcript_.refseqVersions, JoinType.LEFT);
            predicates.add(critBuilder.equal(transcriptTranscriptRefSeqVersJoin.get(TranscriptRefSeqVers_.refseqVer), refseqVersion));

            Join<TranscriptMaps, GenomeRefSeq> transcriptMapsGenomeRefSeqJoin = root.join(TranscriptMaps_.genomeRefSeq, JoinType.LEFT);
            predicates.add(critBuilder.equal(transcriptMapsGenomeRefSeqJoin.get(GenomeRefSeq_.verAccession), refSeqAccession));

            Join<TranscriptMaps, TranscriptMapsExons> minusContigStartTranscriptMapsTranscriptMapsExonsJoin = root
                    .join(TranscriptMaps_.exons, JoinType.LEFT);
            Subquery<Integer> minusContigStartSubquery = crit.subquery(Integer.class);
            Root<TranscriptMapsExons> minusContigStartFromTranscriptMapsExons = minusContigStartSubquery.from(TranscriptMapsExons.class);
            minusContigStartSubquery
                    .where(critBuilder.equal(minusContigStartTranscriptMapsTranscriptMapsExonsJoin.get(TranscriptMapsExons_.key),
                            minusContigStartFromTranscriptMapsExons.get(TranscriptMapsExons_.key)));
            minusContigStartSubquery.select(critBuilder.max(minusContigStartFromTranscriptMapsExons.get(TranscriptMapsExons_.contigStart)));

            Join<TranscriptMaps, TranscriptMapsExons> minusContigEndTranscriptMapsTranscriptMapsExonsJoin = root.join(TranscriptMaps_.exons,
                    JoinType.LEFT);
            Subquery<Integer> minusContigEndSubquery = crit.subquery(Integer.class);
            Root<TranscriptMapsExons> minusContigEndFromTranscriptMapsExons = minusContigEndSubquery.from(TranscriptMapsExons.class);
            minusContigEndSubquery
                    .where(critBuilder.equal(minusContigEndTranscriptMapsTranscriptMapsExonsJoin.get(TranscriptMapsExons_.key),
                            minusContigEndFromTranscriptMapsExons.get(TranscriptMapsExons_.key)));
            minusContigEndSubquery.select(critBuilder.min(minusContigEndFromTranscriptMapsExons.get(TranscriptMapsExons_.contigEnd)));

            Predicate betweenContigEndAndStartPredicate = critBuilder.between(critBuilder.literal(position), minusContigEndSubquery,
                    minusContigStartSubquery);

            // Predicate right = critBuilder.and(critBuilder
            // .equal(critBuilder.function("refseq_strand_to_varchar", String.class, root.get(TranscriptMaps_.strand)),
            // "-"),
            // betweenContigEndAndStartPredicate);

            Predicate right = critBuilder.and(critBuilder.equal(root.get(TranscriptMaps_.strand), "-"), betweenContigEndAndStartPredicate);

            predicates.add(right);

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<TranscriptMaps> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());

        } catch (Exception e) {
            throw new BinningDAOException(e);
        }

        return ret;
    }

    @Override
    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersionAndTranscriptId(Integer genomeRefId, String refSeqVersion,
            String versionId) throws BinningDAOException {
        logger.debug("ENTERING findByGenomeRefIdAndRefSeqVersionAndTranscriptId(Integer, String, String)");
        return findByGenomeRefIdAndRefSeqVersionAndTranscriptId("includeManyToOnes", genomeRefId, refSeqVersion, versionId);
    }

    @Override
    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersionAndTranscriptId(String fetchGroup, Integer genomeRefId,
            String refSeqVersion, String versionId) throws BinningDAOException {
        logger.debug("ENTERING findByGenomeRefIdAndRefSeqVersionAndTranscriptId(String, Integer, String, String)");
        List<TranscriptMaps> ret = new ArrayList<TranscriptMaps>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMaps> crit = critBuilder.createQuery(getPersistentClass());
            Root<TranscriptMaps> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(TranscriptMaps_.genomeRefId), genomeRefId));
            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptJoin = root.join(TranscriptMaps_.transcript);
            predicates.add(critBuilder.equal(transcriptMapsTranscriptJoin.get(Transcript_.versionId), versionId));
            Join<Transcript, TranscriptRefSeqVers> transcriptTranscriptRefseqVersJoin = transcriptMapsTranscriptJoin
                    .join(Transcript_.refseqVersions);
            predicates.add(critBuilder.equal(transcriptTranscriptRefseqVersJoin.get(TranscriptRefSeqVers_.refseqVer), refSeqVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.orderBy(critBuilder.asc(root.get(TranscriptMaps_.mapCount)));
            crit.distinct(true);
            TypedQuery<TranscriptMaps> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<TranscriptMaps> openjpaQuery = OpenJPAPersistence.cast(query);
            if (StringUtils.isNotEmpty(fetchGroup)) {
                openjpaQuery.getFetchPlan().addFetchGroup(fetchGroup);
            }
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersion(Integer genomeRefId, String refSeqVersion) throws BinningDAOException {
        logger.debug("ENTERING findByGenomeRefIdAndRefSeqVersion(Integer, String)");
        return findByGenomeRefIdAndRefSeqVersion("includeManyToOnes", genomeRefId, refSeqVersion);
    }

    @Override
    public TranscriptMaps findById(String fetchGroup, Integer id) throws BinningDAOException {
        logger.debug("ENTERING findById(String, Integer)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TranscriptMaps> crit = critBuilder.createQuery(getPersistentClass());
        Root<TranscriptMaps> root = crit.from(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(critBuilder.equal(root.get(TranscriptMaps_.id), id));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<TranscriptMaps> query = getEntityManager().createQuery(crit);
        OpenJPAQuery<TranscriptMaps> openjpaQuery = OpenJPAPersistence.cast(query);
        openjpaQuery.getFetchPlan().addFetchGroup(fetchGroup);
        TranscriptMaps ret = query.getSingleResult();
        return ret;
    }

    @Override
    public List<TranscriptMaps> findByGenomeRefIdAndRefSeqVersion(String fetchGroup, Integer genomeRefId, String refSeqVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findByGenomeRefIdAndRefSeqVersion(String, Integer, String)");
        List<TranscriptMaps> ret = new ArrayList<TranscriptMaps>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMaps> crit = critBuilder.createQuery(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<TranscriptMaps> root = crit.from(getPersistentClass());
            predicates.add(critBuilder.equal(root.get(TranscriptMaps_.genomeRefId), genomeRefId));
            Join<TranscriptMaps, GenomeRefSeq> transcriptMapsGenomeRefSeqJoin = root.join(TranscriptMaps_.genomeRefSeq);
            predicates.add(critBuilder.equal(transcriptMapsGenomeRefSeqJoin.get(GenomeRefSeq_.seqType), "Chromosome"));
            predicates.add(
                    critBuilder.equal(critBuilder.substring(transcriptMapsGenomeRefSeqJoin.get(GenomeRefSeq_.verAccession), 1, 3), "NC_"));
            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptJoin = root.join(TranscriptMaps_.transcript);
            predicates.add(critBuilder.or(
                    critBuilder.equal(critBuilder.substring(transcriptMapsTranscriptJoin.get(Transcript_.versionId), 1, 3), "NM_"),
                    critBuilder.equal(critBuilder.substring(transcriptMapsTranscriptJoin.get(Transcript_.versionId), 1, 3), "NR_")));
            Join<Transcript, TranscriptRefSeqVers> transcriptTranscriptRefseqVersJoin = transcriptMapsTranscriptJoin
                    .join(Transcript_.refseqVersions);
            predicates.add(critBuilder.equal(transcriptTranscriptRefseqVersJoin.get(TranscriptRefSeqVers_.refseqVer), refSeqVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<TranscriptMaps> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<TranscriptMaps> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup(fetchGroup);
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
