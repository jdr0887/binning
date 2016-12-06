package org.renci.binning.dao.jpa.clinvar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinvar.AssertionRankingDAO;
import org.renci.binning.dao.clinvar.model.AssertionRanking;
import org.renci.binning.dao.clinvar.model.AssertionRankingPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.clinvar.model.AssertionRankingPK_;
import org.renci.binning.dao.clinvar.model.AssertionRanking_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class AssertionRankingDAOImpl extends BaseDAOImpl<AssertionRanking, AssertionRankingPK> implements AssertionRankingDAO {

    private static final Logger logger = LoggerFactory.getLogger(AssertionRankingDAOImpl.class);

    public AssertionRankingDAOImpl() {
        super();
    }

    @Override
    public Class<AssertionRanking> getPersistentClass() {
        return AssertionRanking.class;
    }

    @Override
    public List<AssertionRanking> findByAssertion(String assertion) throws BinningDAOException {
        logger.debug("ENTERING findByAssertion(String)");
        List<AssertionRanking> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<AssertionRanking> crit = critBuilder.createQuery(getPersistentClass());
            Root<AssertionRanking> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(AssertionRanking_.key).get(AssertionRankingPK_.assertion), assertion));
            crit.distinct(true);
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<AssertionRanking> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
