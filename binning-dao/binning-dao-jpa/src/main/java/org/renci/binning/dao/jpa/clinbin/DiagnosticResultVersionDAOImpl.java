package org.renci.binning.dao.jpa.clinbin;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DiagnosticResultVersionDAO;
import org.renci.binning.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.clinbin.model.DiagnosticResultVersion_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class DiagnosticResultVersionDAOImpl extends BaseDAOImpl<DiagnosticResultVersion, Integer> implements DiagnosticResultVersionDAO {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticResultVersionDAOImpl.class);

    public DiagnosticResultVersionDAOImpl() {
        super();
    }

    @Override
    public Class<DiagnosticResultVersion> getPersistentClass() {
        return DiagnosticResultVersion.class;
    }

    @Override
    public Integer findMaxResultVersion() throws BinningDAOException {
        logger.debug("ENTERING findMaxResultVersion()");
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Integer> crit = critBuilder.createQuery(Integer.class);
        Root<DiagnosticResultVersion> root = crit.from(getPersistentClass());
        crit.select(critBuilder.max(root.get(DiagnosticResultVersion_.diagnosticResultVersion)));
        TypedQuery<Integer> query = getEntityManager().createQuery(crit);
        Integer ret = query.getSingleResult();
        return ret;
    }

}
