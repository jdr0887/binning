package org.renci.binning.dao.jpa.var;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.VariantTypeDAO;
import org.renci.binning.dao.var.model.VariantType;
import org.renci.binning.dao.var.model.VariantType_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class VariantTypeDAOImpl extends BaseDAOImpl<VariantType, String> implements VariantTypeDAO {

    private static final Logger logger = LoggerFactory.getLogger(VariantTypeDAOImpl.class);

    public VariantTypeDAOImpl() {
        super();
    }

    @Override
    public Class<VariantType> getPersistentClass() {
        return VariantType.class;
    }

    @Override
    public synchronized String save(VariantType entity) throws BinningDAOException {
        logger.debug("ENTERING save(VariantType)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getName() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getName();
    }

    @Override
    public List<VariantType> findByName(String name) throws BinningDAOException {
        logger.debug("ENTERING findByName(String)");
        List<VariantType> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<VariantType> crit = critBuilder.createQuery(getPersistentClass());
            Root<VariantType> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (!name.endsWith("%")) {
                name += "%";
            }
            predicates.add(critBuilder.like(root.get(VariantType_.name), name));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<VariantType> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
