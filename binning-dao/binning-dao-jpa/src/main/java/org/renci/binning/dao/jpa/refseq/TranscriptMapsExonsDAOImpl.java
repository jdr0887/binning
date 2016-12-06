package org.renci.binning.dao.jpa.refseq;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.refseq.TranscriptMapsExonsDAO;
import org.renci.binning.dao.refseq.model.Transcript;
import org.renci.binning.dao.refseq.model.TranscriptMaps;
import org.renci.binning.dao.refseq.model.TranscriptMapsExons;
import org.renci.binning.dao.refseq.model.TranscriptRefSeqVers;
import org.renci.binning.dao.ref.model.GenomeRefSeq_;
import org.renci.binning.dao.refseq.model.TranscriptMapsExons_;
import org.renci.binning.dao.refseq.model.TranscriptMaps_;
import org.renci.binning.dao.refseq.model.TranscriptRefSeqVers_;
import org.renci.binning.dao.refseq.model.Transcript_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class TranscriptMapsExonsDAOImpl extends BaseDAOImpl<TranscriptMapsExons, Integer> implements TranscriptMapsExonsDAO {

    private static final Logger logger = LoggerFactory.getLogger(TranscriptMapsExonsDAOImpl.class);

    public TranscriptMapsExonsDAOImpl() {
        super();
    }

    @Override
    public Class<TranscriptMapsExons> getPersistentClass() {
        return TranscriptMapsExons.class;
    }

    @Override
    public List<TranscriptMapsExons> findByTranscriptMapsId(Integer id) throws BinningDAOException {
        logger.debug("ENTERING findByTranscriptMapsId(Integer)");
        List<TranscriptMapsExons> ret = new ArrayList<TranscriptMapsExons>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMapsExons> crit = critBuilder.createQuery(getPersistentClass());
            Root<TranscriptMapsExons> root = crit.from(getPersistentClass());
            Join<TranscriptMapsExons, TranscriptMaps> join = root.join(TranscriptMapsExons_.transcriptMaps);
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(join.get(TranscriptMaps_.id), id));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<TranscriptMapsExons> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<TranscriptMapsExons> findByTranscriptVersionIdAndTranscriptMapsMapCount(String versionId, Integer mapCount)
            throws BinningDAOException {
        logger.debug("ENTERING findByTranscriptVersionIdAndTranscriptMapsMapCount(String, Integer)");
        List<TranscriptMapsExons> ret = new ArrayList<TranscriptMapsExons>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMapsExons> crit = critBuilder.createQuery(getPersistentClass());
            Root<TranscriptMapsExons> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<TranscriptMapsExons, TranscriptMaps> transcriptMapsExonsTranscriptMapsJoin = root
                    .join(TranscriptMapsExons_.transcriptMaps);
            predicates.add(critBuilder.equal(transcriptMapsExonsTranscriptMapsJoin.get(TranscriptMaps_.mapCount), mapCount));

            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptjoin = transcriptMapsExonsTranscriptMapsJoin
                    .join(TranscriptMaps_.transcript);
            predicates.add(critBuilder.equal(transcriptMapsTranscriptjoin.get(Transcript_.versionId), versionId));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<TranscriptMapsExons> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<TranscriptMapsExons> findByGenomeRefIdAndRefSeqVersion(Integer genomeRefId, String refSeqVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findByGenomeRefIdAndRefSeqVersion(Integer, String)");
        List<TranscriptMapsExons> ret = new ArrayList<TranscriptMapsExons>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMapsExons> crit = critBuilder.createQuery(getPersistentClass());
            Root<TranscriptMapsExons> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<TranscriptMapsExons, TranscriptMaps> transcriptMapsExonsTranscriptMapsJoin = root
                    .join(TranscriptMapsExons_.transcriptMaps);
            predicates.add(critBuilder.equal(transcriptMapsExonsTranscriptMapsJoin.get(TranscriptMaps_.genomeRefId), genomeRefId));

            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptJoin = transcriptMapsExonsTranscriptMapsJoin
                    .join(TranscriptMaps_.transcript);
            Join<Transcript, TranscriptRefSeqVers> TranscriptTranscriptRefseqVersJoin = transcriptMapsTranscriptJoin
                    .join(Transcript_.refseqVersions);
            predicates.add(critBuilder.equal(TranscriptTranscriptRefseqVersJoin.get(TranscriptRefSeqVers_.refseqVer), refSeqVersion));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<TranscriptMapsExons> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<TranscriptMapsExons> findByGenomeRefSeqAccessionAndInExonRange(String refSeqAccession, Integer start)
            throws BinningDAOException {
        List<TranscriptMapsExons> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMapsExons> crit = critBuilder.createQuery(getPersistentClass());
            Root<TranscriptMapsExons> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<TranscriptMapsExons, TranscriptMaps> transcriptMapsExonsTranscriptMapsJoin = root
                    .join(TranscriptMapsExons_.transcriptMaps);
            Join<TranscriptMaps, GenomeRefSeq> transcriptMapsGenomeRefSeqJoin = transcriptMapsExonsTranscriptMapsJoin
                    .join(TranscriptMaps_.genomeRefSeq);
            predicates.add(critBuilder.equal(transcriptMapsGenomeRefSeqJoin.get(GenomeRefSeq_.verAccession), refSeqAccession));

            predicates.add(critBuilder.between(critBuilder.literal(start), root.get(TranscriptMapsExons_.contigStart),
                    root.get(TranscriptMapsExons_.contigEnd)));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<TranscriptMapsExons> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<TranscriptMapsExons> findByGenomeRefIdAndRefSeqVersionAndAccession(Integer genomeRefId, String refSeqVersion,
            String accession) throws BinningDAOException {
        logger.debug("ENTERING findByGenomeRefIdAndRefSeqVersionAndAccession(Integer, String, String)");
        List<TranscriptMapsExons> ret = new ArrayList<TranscriptMapsExons>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TranscriptMapsExons> crit = critBuilder.createQuery(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<TranscriptMapsExons> root = crit.from(getPersistentClass());
            Join<TranscriptMapsExons, TranscriptMaps> transcriptMapsExonsTranscriptMapsJoin = root
                    .join(TranscriptMapsExons_.transcriptMaps);
            predicates.add(critBuilder.equal(transcriptMapsExonsTranscriptMapsJoin.get(TranscriptMaps_.genomeRefId), genomeRefId));
            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptJoin = transcriptMapsExonsTranscriptMapsJoin
                    .join(TranscriptMaps_.transcript);
            predicates.add(critBuilder.equal(transcriptMapsTranscriptJoin.get(Transcript_.versionId), accession));
            Join<Transcript, TranscriptRefSeqVers> transcriptTranscriptRefseqVersJoin = transcriptMapsTranscriptJoin
                    .join(Transcript_.refseqVersions);
            predicates.add(critBuilder.equal(transcriptTranscriptRefseqVersJoin.get(TranscriptRefSeqVers_.refseqVer), refSeqVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<TranscriptMapsExons> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public Integer findMaxContig(Integer genomeRefId, String refSeqVersion, String refSeqAccession, Integer transcriptMapsId)
            throws BinningDAOException {
        logger.debug("ENTERING findByGenomeRefIdAndRefSeqVersionAndAccession(Integer, String, String)");
        Integer ret = null;
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Integer> crit = critBuilder.createQuery(Integer.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            Root<TranscriptMapsExons> root = crit.from(getPersistentClass());

            Join<TranscriptMapsExons, TranscriptMaps> transcriptMapsExonsTranscriptMapsJoin = root
                    .join(TranscriptMapsExons_.transcriptMaps);
            predicates.add(critBuilder.equal(transcriptMapsExonsTranscriptMapsJoin.get(TranscriptMaps_.genomeRefId), genomeRefId));
            predicates.add(critBuilder.equal(transcriptMapsExonsTranscriptMapsJoin.get(TranscriptMaps_.id), transcriptMapsId));

            Join<TranscriptMaps, GenomeRefSeq> transcriptMapsGenomeRefSeqJoin = transcriptMapsExonsTranscriptMapsJoin
                    .join(TranscriptMaps_.genomeRefSeq);
            predicates.add(critBuilder.equal(transcriptMapsGenomeRefSeqJoin.get(GenomeRefSeq_.verAccession), refSeqAccession));

            Join<TranscriptMaps, Transcript> transcriptMapsTranscriptJoin = transcriptMapsExonsTranscriptMapsJoin
                    .join(TranscriptMaps_.transcript);
            Join<Transcript, TranscriptRefSeqVers> transcriptTranscriptRefseqVersJoin = transcriptMapsTranscriptJoin
                    .join(Transcript_.refseqVersions);
            predicates.add(critBuilder.equal(transcriptTranscriptRefseqVersJoin.get(TranscriptRefSeqVers_.refseqVer), refSeqVersion));

            // crit.select(critBuilder.max(root.get(TranscriptMapsExons_.contigStart)));
            crit.select(critBuilder.min(root.get(TranscriptMapsExons_.contigEnd)));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            // crit.distinct(true);
            TypedQuery<Integer> query = getEntityManager().createQuery(crit);
            ret = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
