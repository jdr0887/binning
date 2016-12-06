package org.renci.binning.dao.jpa.var;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.VariantSetLoadDAO;
import org.renci.binning.dao.var.model.VariantSet;
import org.renci.binning.dao.var.model.VariantSetLoad;
import org.renci.binning.dao.var.model.VariantSetLoad_;
import org.renci.binning.dao.var.model.VariantSet_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class VariantSetLoadDAOImpl extends BaseDAOImpl<VariantSetLoad, Integer> implements VariantSetLoadDAO {

    private static final Logger logger = LoggerFactory.getLogger(VariantSetLoadDAOImpl.class);

    public VariantSetLoadDAOImpl() {
        super();
    }

    @Override
    public Class<VariantSetLoad> getPersistentClass() {
        return VariantSetLoad.class;
    }

    @Override
    public Integer save(VariantSetLoad entity) {
        logger.debug("ENTERING save(VariantSetLoad)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getVariantSet().getId() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getVariantSet().getId();
    }

    @Override
    public List<VariantSetLoad> findByExample(VariantSetLoad variantSetLoad) throws BinningDAOException {
        logger.debug("ENTERING findByExample(VariantSetLoad)");
        List<VariantSetLoad> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<VariantSetLoad> crit = critBuilder.createQuery(getPersistentClass());
            Root<VariantSetLoad> root = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();

            if (StringUtils.isNotEmpty(variantSetLoad.getLoadFilename())) {
                predicates.add(critBuilder.equal(root.get(VariantSetLoad_.loadFilename), variantSetLoad.getLoadFilename()));
            }
            if (StringUtils.isNotEmpty(variantSetLoad.getLoadProgramName())) {
                predicates.add(critBuilder.equal(root.get(VariantSetLoad_.loadProgramName), variantSetLoad.getLoadProgramName()));
            }
            if (StringUtils.isNotEmpty(variantSetLoad.getLoadProgramVersion())) {
                predicates.add(critBuilder.equal(root.get(VariantSetLoad_.loadProgramVersion), variantSetLoad.getLoadProgramVersion()));
            }

            if (variantSetLoad.getVariantSet() != null) {
                Join<VariantSetLoad, VariantSet> variantSetLoadVariantSetJoin = root.join(VariantSetLoad_.variantSet);
                predicates.add(critBuilder.equal(variantSetLoadVariantSetJoin.get(VariantSet_.id), variantSetLoad.getVariantSet().getId()));
            }

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<VariantSetLoad> query = getEntityManager().createQuery(crit);
            OpenJPAQuery<VariantSetLoad> openjpaQuery = OpenJPAPersistence.cast(query);
            openjpaQuery.getFetchPlan().addFetchGroup("includeManyToOnes");
            ret.addAll(openjpaQuery.getResultList());
        } catch (Exception e) {
            throw new BinningDAOException(e);
        }
        return ret;
    }

}
