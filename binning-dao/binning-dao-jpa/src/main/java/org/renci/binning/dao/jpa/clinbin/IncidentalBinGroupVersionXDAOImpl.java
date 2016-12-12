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
import org.renci.binning.dao.clinbin.IncidentalBinGroupVersionXDAO;
import org.renci.binning.dao.clinbin.model.IncidentalBinGroupVersionX;
import org.renci.binning.dao.clinbin.model.IncidentalBinGroupVersionXPK;
import org.renci.binning.dao.clinbin.model.IncidentalBinGroupVersionXPK_;
import org.renci.binning.dao.clinbin.model.IncidentalBinGroupVersionX_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { IncidentalBinGroupVersionXDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class IncidentalBinGroupVersionXDAOImpl extends BaseDAOImpl<IncidentalBinGroupVersionX, IncidentalBinGroupVersionXPK>
        implements IncidentalBinGroupVersionXDAO {

    private static final Logger logger = LoggerFactory.getLogger(IncidentalBinGroupVersionXDAOImpl.class);

    public IncidentalBinGroupVersionXDAOImpl() {
        super();
    }

    @Override
    public Class<IncidentalBinGroupVersionX> getPersistentClass() {
        return IncidentalBinGroupVersionX.class;
    }

    @Override
    public List<IncidentalBinGroupVersionX> findByIncidentalBinIdAndGroupVersion(Integer id, Integer groupVersion)
            throws BinningDAOException {
        logger.debug("ENTERING findByIncidentalBinIdAndGroupVersion(Integer, Integer)");
        List<IncidentalBinGroupVersionX> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IncidentalBinGroupVersionX> crit = critBuilder.createQuery(IncidentalBinGroupVersionX.class);
            Root<IncidentalBinGroupVersionX> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(
                    root.get(IncidentalBinGroupVersionX_.key).get(IncidentalBinGroupVersionXPK_.incidentalBinGroupVersion), groupVersion));
            predicates
                    .add(critBuilder.equal(root.get(IncidentalBinGroupVersionX_.key).get(IncidentalBinGroupVersionXPK_.incidentalBin), id));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<IncidentalBinGroupVersionX> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
