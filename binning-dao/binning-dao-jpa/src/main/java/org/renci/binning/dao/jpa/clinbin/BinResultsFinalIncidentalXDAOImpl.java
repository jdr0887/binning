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
import org.renci.binning.dao.clinbin.BinResultsFinalIncidentalXDAO;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalX;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalXPK;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalXPK_;
import org.renci.binning.dao.clinbin.model.BinResultsFinalIncidentalX_;
import org.renci.binning.dao.clinbin.model.CarrierStatus;
import org.renci.binning.dao.clinbin.model.CarrierStatus_;
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
@OsgiServiceProvider(classes = { BinResultsFinalIncidentalXDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class BinResultsFinalIncidentalXDAOImpl extends BaseDAOImpl<BinResultsFinalIncidentalX, Long>
        implements BinResultsFinalIncidentalXDAO {

    private static final Logger logger = LoggerFactory.getLogger(BinResultsFinalIncidentalXDAOImpl.class);

    public BinResultsFinalIncidentalXDAOImpl() {
        super();
    }

    @Override
    public Class<BinResultsFinalIncidentalX> getPersistentClass() {
        return BinResultsFinalIncidentalX.class;
    }

    @Override
    public List<BinResultsFinalIncidentalX> findByParticipantAndIncidentalBinIdAndResultVersion(String participant, Integer incidentalBinId,
            Integer resultVersion) throws BinningDAOException {
        logger.debug("ENTERING findByParticipantAndIncidentalBinIdAndResultVersion(String, Integer, Integer)");
        List<BinResultsFinalIncidentalX> ret = new ArrayList<>();

        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<BinResultsFinalIncidentalX> crit = critBuilder.createQuery(getPersistentClass());
            Root<BinResultsFinalIncidentalX> root = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<BinResultsFinalIncidentalX, BinResultsFinalIncidentalXPK> binResultsFinalIncidentalXBinResultsFinalIncidentalXPKJoin = root
                    .join(BinResultsFinalIncidentalX_.key);
            predicates.add(critBuilder.equal(
                    binResultsFinalIncidentalXBinResultsFinalIncidentalXPKJoin.get(BinResultsFinalIncidentalXPK_.participant),
                    participant));

            Join<BinResultsFinalIncidentalX, IncidentalBinX> binResultsFinalIncidentalXIncidentalBinXJoin = root
                    .join(BinResultsFinalIncidentalX_.incidentalBin);
            predicates.add(critBuilder.equal(binResultsFinalIncidentalXIncidentalBinXJoin.get(IncidentalBinX_.id), incidentalBinId));

            Join<BinResultsFinalIncidentalX, IncidentalResultVersionX> binResultsFinalIncidentalXIncidentalResultVersionXJoin = root
                    .join(BinResultsFinalIncidentalX_.incidentalResultVersion);
            predicates.add(critBuilder.equal(
                    binResultsFinalIncidentalXIncidentalResultVersionXJoin.get(IncidentalResultVersionX_.binningResultVersion),
                    resultVersion));

            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<BinResultsFinalIncidentalX> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<BinResultsFinalIncidentalX> findByParticipantAndIncidentalBinIdAndResultVersionAndCarrierStatusId(String participant,
            Integer incidentalBinId, Integer version, Integer carrierStatusId) throws BinningDAOException {
        logger.debug("ENTERING findByParticipantAndIncidentalBinIdAndResultVersionAndCarrierStatusId(String, Integer, Integer, Integer)");
        List<BinResultsFinalIncidentalX> ret = new ArrayList<>();

        try {

            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<BinResultsFinalIncidentalX> crit = critBuilder.createQuery(getPersistentClass());
            Root<BinResultsFinalIncidentalX> root = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<BinResultsFinalIncidentalX, BinResultsFinalIncidentalXPK> binResultsFinalIncidentalXBinResultsFinalIncidentalXPKJoin = root
                    .join(BinResultsFinalIncidentalX_.key);
            predicates.add(critBuilder.equal(
                    binResultsFinalIncidentalXBinResultsFinalIncidentalXPKJoin.get(BinResultsFinalIncidentalXPK_.participant),
                    participant));

            Join<BinResultsFinalIncidentalX, IncidentalBinX> binResultsFinalIncidentalXIncidentalBinXJoin = root
                    .join(BinResultsFinalIncidentalX_.incidentalBin);
            predicates.add(critBuilder.equal(binResultsFinalIncidentalXIncidentalBinXJoin.get(IncidentalBinX_.id), incidentalBinId));

            Join<BinResultsFinalIncidentalX, IncidentalResultVersionX> binResultsFinalIncidentalXIncidentalResultVersionXJoin = root
                    .join(BinResultsFinalIncidentalX_.incidentalResultVersion);
            predicates.add(critBuilder.equal(
                    binResultsFinalIncidentalXIncidentalResultVersionXJoin.get(IncidentalResultVersionX_.binningResultVersion), version));

            Join<BinResultsFinalIncidentalX, CarrierStatus> binResultsFinalIncidentalXCarrierStatusJoin = root
                    .join(BinResultsFinalIncidentalX_.carrierStatus);
            predicates.add(critBuilder.equal(binResultsFinalIncidentalXCarrierStatusJoin.get(CarrierStatus_.id), carrierStatusId));

            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));

            TypedQuery<BinResultsFinalIncidentalX> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
