package org.renci.binning.dao.jpa.ref;

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
import org.renci.binning.dao.ref.GenomeRefSeqDAO;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.ref.model.GenomeRefSeq_;
import org.renci.binning.dao.ref.model.GenomeRef_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class GenomeRefSeqDAOImpl extends BaseDAOImpl<GenomeRefSeq, String> implements GenomeRefSeqDAO {

    private static final Logger logger = LoggerFactory.getLogger(GenomeRefSeqDAOImpl.class);

    public GenomeRefSeqDAOImpl() {
        super();
    }

    @Override
    public Class<GenomeRefSeq> getPersistentClass() {
        return GenomeRefSeq.class;
    }

    @Override
    public List<GenomeRefSeq> findAll() throws BinningDAOException {
        logger.debug("ENTERING findAll()");
        TypedQuery<GenomeRefSeq> query = getEntityManager().createNamedQuery("GenomeRefSeq.findAll", GenomeRefSeq.class);
        List<GenomeRefSeq> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<GenomeRefSeq> findBySeqType(String seqType) throws BinningDAOException {
        logger.debug("ENTERING findBySeqType(String)");
        List<GenomeRefSeq> ret = new ArrayList<GenomeRefSeq>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<GenomeRefSeq> crit = critBuilder.createQuery(getPersistentClass());
            Root<GenomeRefSeq> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(GenomeRefSeq_.seqType), seqType));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<GenomeRefSeq> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<GenomeRefSeq> findByNameAndSourceAndContig(String name, String source, String contig) throws BinningDAOException {
        List<GenomeRefSeq> ret = new ArrayList<GenomeRefSeq>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<GenomeRefSeq> crit = critBuilder.createQuery(getPersistentClass());
            Root<GenomeRefSeq> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(GenomeRefSeq_.contig), contig));
            predicates.add(critBuilder.equal(root.get(GenomeRefSeq_.seqType), "Chromosome"));
            Join<GenomeRefSeq, GenomeRef> genomeRefGenomeRefSeqJoin = root.join(GenomeRefSeq_.genomeRefs);
            predicates.add(critBuilder.equal(genomeRefGenomeRefSeqJoin.get(GenomeRef_.source), source));
            predicates.add(critBuilder.equal(genomeRefGenomeRefSeqJoin.get(GenomeRef_.name), name));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<GenomeRefSeq> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<GenomeRefSeq> findByRefIdAndContigAndSeqType(Integer refId, String contig, String seqType) throws BinningDAOException {
        List<GenomeRefSeq> ret = new ArrayList<GenomeRefSeq>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<GenomeRefSeq> crit = critBuilder.createQuery(getPersistentClass());
            Root<GenomeRefSeq> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(GenomeRefSeq_.contig), contig));
            predicates.add(critBuilder.equal(root.get(GenomeRefSeq_.seqType), seqType));
            predicates.add(critBuilder.equal(root.join(GenomeRefSeq_.genomeRefs).get(GenomeRef_.id), refId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<GenomeRefSeq> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<GenomeRefSeq> findByVersionedAccession(String refVerAccession) throws BinningDAOException {
        List<GenomeRefSeq> ret = new ArrayList<GenomeRefSeq>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<GenomeRefSeq> crit = critBuilder.createQuery(getPersistentClass());
            Root<GenomeRefSeq> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(GenomeRefSeq_.verAccession), refVerAccession));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<GenomeRefSeq> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public synchronized String save(GenomeRefSeq entity) throws BinningDAOException {
        logger.debug("ENTERING save(GenomeRefSeq)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getVerAccession() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getVerAccession();
    }

}
