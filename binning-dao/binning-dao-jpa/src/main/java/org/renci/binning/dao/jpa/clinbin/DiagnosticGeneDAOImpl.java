package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DiagnosticGeneDAO;
import org.renci.binning.dao.clinbin.model.DiagnosticGene;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.annotation.model.AnnotationGene_;
import org.renci.binning.dao.clinbin.model.DX_;
import org.renci.binning.dao.clinbin.model.DiagnosticGene_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class DiagnosticGeneDAOImpl extends BaseDAOImpl<DiagnosticGene, Integer> implements DiagnosticGeneDAO {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticGeneDAOImpl.class);

    public DiagnosticGeneDAOImpl() {
        super();
    }

    @Override
    public Class<DiagnosticGene> getPersistentClass() {
        return DiagnosticGene.class;
    }

    @Override
    public List<DiagnosticGene> findByGeneIdAndDXId(Integer geneId, Integer dxId) throws BinningDAOException {
        logger.debug("ENTERING findByGeneIdAndListVersionAndDXId(Integer, Integer)");
        List<DiagnosticGene> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DiagnosticGene> crit = critBuilder.createQuery(getPersistentClass());
            Root<DiagnosticGene> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.join(DiagnosticGene_.gene).get(AnnotationGene_.id), geneId));
            predicates.add(critBuilder.equal(root.join(DiagnosticGene_.dx).get(DX_.id), dxId));
            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);
            TypedQuery<DiagnosticGene> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
