package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DiagnosticStatusTypeDAO;
import org.renci.binning.dao.clinbin.model.DiagnosticStatusType;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { DiagnosticStatusTypeDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class DiagnosticStatusTypeDAOImpl extends BaseDAOImpl<DiagnosticStatusType, String> implements DiagnosticStatusTypeDAO {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticStatusTypeDAOImpl.class);

    public DiagnosticStatusTypeDAOImpl() {
        super();
    }

    @Override
    public Class<DiagnosticStatusType> getPersistentClass() {
        return DiagnosticStatusType.class;
    }

    @Override
    public List<DiagnosticStatusType> findAll() throws BinningDAOException {
        logger.debug("ENTERING findAll()");
        List<DiagnosticStatusType> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<DiagnosticStatusType> crit = critBuilder.createQuery(getPersistentClass());
            TypedQuery<DiagnosticStatusType> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
