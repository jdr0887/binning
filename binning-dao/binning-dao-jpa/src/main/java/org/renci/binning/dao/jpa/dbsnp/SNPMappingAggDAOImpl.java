package org.renci.binning.dao.jpa.dbsnp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.dbsnp.SNPMappingAggDAO;
import org.renci.binning.dao.dbsnp.model.SNPMappingAgg;
import org.renci.binning.dao.dbsnp.model.SNPMappingAggPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.dbsnp.model.SNPMappingAgg_;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class SNPMappingAggDAOImpl extends BaseDAOImpl<SNPMappingAgg, SNPMappingAggPK> implements SNPMappingAggDAO {

    private static final Logger logger = LoggerFactory.getLogger(SNPMappingAggDAOImpl.class);

    public SNPMappingAggDAOImpl() {
        super();
    }

    @Override
    public Class<SNPMappingAgg> getPersistentClass() {
        return SNPMappingAgg.class;
    }

    @Override
    public List<SNPMappingAgg> findByLocatedVariantId(Long locatedVariantId) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantId(Long)");
        List<SNPMappingAgg> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<SNPMappingAgg> crit = critBuilder.createQuery(getPersistentClass());
            Root<SNPMappingAgg> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<SNPMappingAgg, LocatedVariant> snpMappingAggLocatedVariantJoin = root.join(SNPMappingAgg_.locatedVariant);
            predicates.add(critBuilder.equal(snpMappingAggLocatedVariantJoin.get(LocatedVariant_.id), locatedVariantId));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<SNPMappingAgg> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
