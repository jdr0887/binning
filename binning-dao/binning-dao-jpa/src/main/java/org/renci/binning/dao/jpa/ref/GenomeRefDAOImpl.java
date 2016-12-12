package org.renci.binning.dao.jpa.ref;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.ref.GenomeRefDAO;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.ref.model.GenomeRefSeq_;
import org.renci.binning.dao.ref.model.GenomeRef_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { GenomeRefDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class GenomeRefDAOImpl extends BaseDAOImpl<GenomeRef, Integer> implements GenomeRefDAO {

    private static final Logger logger = LoggerFactory.getLogger(GenomeRefDAOImpl.class);

    public GenomeRefDAOImpl() {
        super();
    }

    @Override
    public Class<GenomeRef> getPersistentClass() {
        return GenomeRef.class;
    }

    @Override
    public List<GenomeRef> findByName(String name) throws BinningDAOException {
        logger.debug("ENTERING findByName()");
        List<GenomeRef> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<GenomeRef> crit = critBuilder.createQuery(getPersistentClass());
            Root<GenomeRef> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(GenomeRef_.name), name));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<GenomeRef> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<GenomeRef> findByNameAndSource(String name, String source) throws BinningDAOException {
        logger.debug("ENTERING findByNameAndSource(String, String)");
        List<GenomeRef> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<GenomeRef> crit = critBuilder.createQuery(getPersistentClass());
            Root<GenomeRef> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(GenomeRef_.name), name));
            predicates.add(critBuilder.equal(root.get(GenomeRef_.source), source));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<GenomeRef> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<GenomeRef> findAll() throws BinningDAOException {
        logger.debug("ENTERING findAll()");
        List<GenomeRef> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<GenomeRef> crit = critBuilder.createQuery(getPersistentClass());
            crit.distinct(true);
            TypedQuery<GenomeRef> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

    @Override
    public List<GenomeRef> findByGenomeRefSeqVersionAccession(String versionAccession) throws BinningDAOException {
        logger.debug("ENTERING findByGenomeRefSeqVersionAccession(String)");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<GenomeRef> crit = critBuilder.createQuery(getPersistentClass());
        List<Predicate> predicates = new ArrayList<Predicate>();
        Root<GenomeRef> fromGenomeRef = crit.from(getPersistentClass());
        Join<GenomeRef, GenomeRefSeq> genomeRefGenomeRefSeqJoin = fromGenomeRef.join(GenomeRef_.genomeRefSeqs);
        predicates.add(critBuilder.equal(genomeRefGenomeRefSeqJoin.get(GenomeRefSeq_.verAccession), versionAccession));
        crit.where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<GenomeRef> query = getEntityManager().createQuery(crit);
        List<GenomeRef> ret = query.getResultList();
        return ret;
    }

    @Override
    public List<GenomeRef> findBySeqTypeAndContig(String seqType, String contig) throws BinningDAOException {
        logger.debug("ENTERING findBySeqTypeAndContig()");
        TypedQuery<GenomeRef> query = getEntityManager().createNamedQuery("GenomeRef.findBySeqTypeAndContig", GenomeRef.class);
        query.setParameter("seqType", seqType);
        query.setParameter("contig", contig);
        List<GenomeRef> ret = query.getResultList();
        return ret;
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public synchronized Integer save(GenomeRef entity) throws BinningDAOException {
        logger.debug("ENTERING save(GenomeRef)");
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
