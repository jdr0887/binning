package org.renci.binning.dao.jpa.hgmd;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.hgmd.HGMDLocatedVariantDAO;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariantPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant_;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class HGMDLocatedVariantDAOImpl extends BaseDAOImpl<HGMDLocatedVariant, HGMDLocatedVariantPK> implements HGMDLocatedVariantDAO {

    private static final Logger logger = LoggerFactory.getLogger(HGMDLocatedVariantDAOImpl.class);

    public HGMDLocatedVariantDAOImpl() {
        super();
    }

    @Override
    public Class<HGMDLocatedVariant> getPersistentClass() {
        return HGMDLocatedVariant.class;
    }

    @Override
    public List<HGMDLocatedVariant> findByLocatedVariantId(Long locatedVariantId) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantId(Long)");
        List<HGMDLocatedVariant> ret = new ArrayList<HGMDLocatedVariant>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<HGMDLocatedVariant> crit = critBuilder.createQuery(getPersistentClass());
            Root<HGMDLocatedVariant> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            Join<HGMDLocatedVariant, LocatedVariant> hgmdLocatedVariantLocatedVariantJoin = root.join(HGMDLocatedVariant_.locatedVariant);
            predicates.add(critBuilder.equal(hgmdLocatedVariantLocatedVariantJoin.get(LocatedVariant_.id), locatedVariantId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<HGMDLocatedVariant> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
