package org.renci.binning.dao.jpa.clinbin;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.IncidentalResultVersionXDAO;
import org.renci.binning.dao.clinbin.model.IncidentalResultVersionX;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.clinbin.model.IncidentalResultVersionX_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class IncidentalResultVersionXDAOImpl extends BaseDAOImpl<IncidentalResultVersionX, Integer> implements IncidentalResultVersionXDAO {

    private static final Logger logger = LoggerFactory.getLogger(IncidentalResultVersionXDAOImpl.class);

    public IncidentalResultVersionXDAOImpl() {
        super();
    }

    @Override
    public Class<IncidentalResultVersionX> getPersistentClass() {
        return IncidentalResultVersionX.class;
    }

    @Override
    public Integer findMaxResultVersion() throws BinningDAOException {
        logger.debug("ENTERING findMaxResultVersion()");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> crit = critBuilder.createQuery(Integer.class);
        Root<IncidentalResultVersionX> root = crit.from(getPersistentClass());
        crit.select(critBuilder.max(root.get(IncidentalResultVersionX_.binningResultVersion)));
        TypedQuery<Integer> query = getEntityManager().createQuery(crit);
        Integer ret = query.getSingleResult();
        return ret;
    }

}
