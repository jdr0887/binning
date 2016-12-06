package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DiagnosticStatusTypeDAO;
import org.renci.binning.dao.clinbin.model.DiagnosticStatusType;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
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
            TypedQuery<DiagnosticStatusType> query = getEntityManager().createNamedQuery("clinbin.DiagnosticStatusType.findAll",
                    DiagnosticStatusType.class);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
