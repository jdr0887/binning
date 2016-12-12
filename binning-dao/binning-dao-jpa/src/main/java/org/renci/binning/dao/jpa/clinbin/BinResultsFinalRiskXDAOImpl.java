package org.renci.binning.dao.jpa.clinbin;

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
import org.renci.binning.dao.clinbin.BinResultsFinalRiskXDAO;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskX;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskXPK;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskXPK_;
import org.renci.binning.dao.clinbin.model.BinResultsFinalRiskX_;
import org.renci.binning.dao.clinbin.model.IncidentalBinX;
import org.renci.binning.dao.clinbin.model.IncidentalBinX_;
import org.renci.binning.dao.clinbin.model.IncidentalResultVersionX;
import org.renci.binning.dao.clinbin.model.IncidentalResultVersionX_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { BinResultsFinalRiskXDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class BinResultsFinalRiskXDAOImpl extends BaseDAOImpl<BinResultsFinalRiskX, Long> implements BinResultsFinalRiskXDAO {

    private static final Logger logger = LoggerFactory.getLogger(BinResultsFinalRiskXDAOImpl.class);

    public BinResultsFinalRiskXDAOImpl() {
        super();
    }

    @Override
    public Class<BinResultsFinalRiskX> getPersistentClass() {
        return BinResultsFinalRiskX.class;
    }

    @Override
    public List<BinResultsFinalRiskX> findByParticipantAndIndicentalBinIdAndResultVersion(String participant, Integer incidentalBinId,
            Integer resultVersion) throws BinningDAOException {
        logger.debug("ENTERING findByDXIdAndParticipantAndListVersion(Long, String, Integer)");

        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<BinResultsFinalRiskX> crit = critBuilder.createQuery(getPersistentClass());
        Root<BinResultsFinalRiskX> root = crit.from(getPersistentClass());

        List<Predicate> predicates = new ArrayList<Predicate>();

        Join<BinResultsFinalRiskX, BinResultsFinalRiskXPK> binResultsFinalRiskXBinResultsFinalRiskXPKJoin = root
                .join(BinResultsFinalRiskX_.key);
        predicates.add(
                critBuilder.equal(binResultsFinalRiskXBinResultsFinalRiskXPKJoin.get(BinResultsFinalRiskXPK_.participant), participant));

        Join<BinResultsFinalRiskX, IncidentalBinX> binResultsFinalRiskXIncidentalBinXJoin = root.join(BinResultsFinalRiskX_.incidentalBin);
        predicates.add(critBuilder.equal(binResultsFinalRiskXIncidentalBinXJoin.get(IncidentalBinX_.id), incidentalBinId));

        Join<BinResultsFinalRiskX, IncidentalResultVersionX> binResultsFinalRiskXIncidentalResultVersionXJoin = root
                .join(BinResultsFinalRiskX_.incidentalResultVersion);
        predicates.add(critBuilder.equal(
                binResultsFinalRiskXIncidentalResultVersionXJoin.get(IncidentalResultVersionX_.binningResultVersion), resultVersion));

        crit.distinct(true);
        crit.where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<BinResultsFinalRiskX> query = getEntityManager().createQuery(crit);
        List<BinResultsFinalRiskX> ret = query.getResultList();
        return ret;
    }
}
