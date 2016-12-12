package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DXExonsDAO;
import org.renci.binning.dao.clinbin.model.DXExons;
import org.renci.binning.dao.clinbin.model.DXExons_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { DXExonsDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class DXExonsDAOImpl extends BaseDAOImpl<DXExons, Integer> implements DXExonsDAO {

    private static final Logger logger = LoggerFactory.getLogger(DXExonsDAOImpl.class);

    public DXExonsDAOImpl() {
        super();
    }

    @Override
    public Class<DXExons> getPersistentClass() {
        return DXExons.class;
    }

    @Override
    public Integer findMaxListVersion() throws BinningDAOException {
        logger.debug("ENTERING findMaxListVersion()");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> crit = critBuilder.createQuery(Integer.class);
        Root<DXExons> root = crit.from(getPersistentClass());
        crit.select(critBuilder.max(root.get(DXExons_.listVersion)));
        TypedQuery<Integer> query = getEntityManager().createQuery(crit);
        Integer ret = query.getSingleResult();
        return ret;
    }

    @Override
    public List<DXExons> findByListVersion(Integer listVersion) throws BinningDAOException {
        logger.debug("ENTERING findByListVersion(Integer)");
        List<DXExons> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DXExons> crit = critBuilder.createQuery(getPersistentClass());
            Root<DXExons> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(DXExons_.listVersion), listVersion));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<DXExons> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<DXExons> findByListVersionAndChromosome(Integer listVersion, String chromosome) throws BinningDAOException {
        logger.debug("ENTERING findByListVersionAndChromosome(Integer, String)");
        List<DXExons> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DXExons> crit = critBuilder.createQuery(getPersistentClass());
            Root<DXExons> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(DXExons_.listVersion), listVersion));
            predicates.add(critBuilder.equal(root.get(DXExons_.chromosome), chromosome));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<DXExons> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<DXExons> findByListVersionAndChromosomeAndRange(Integer listVersion, String chromosome, Integer start, Integer end)
            throws BinningDAOException {
        logger.debug("ENTERING findByListVersionAndChromosomeAndRange(Integer, String, Integer, Integer)");
        List<DXExons> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DXExons> crit = critBuilder.createQuery(getPersistentClass());
            Root<DXExons> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(DXExons_.listVersion), listVersion));
            predicates.add(critBuilder.equal(root.get(DXExons_.chromosome), chromosome));
            predicates.add(critBuilder.greaterThanOrEqualTo(root.get(DXExons_.intervalStart), start));
            predicates.add(critBuilder.lessThanOrEqualTo(root.get(DXExons_.intervalEnd), end));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<DXExons> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
