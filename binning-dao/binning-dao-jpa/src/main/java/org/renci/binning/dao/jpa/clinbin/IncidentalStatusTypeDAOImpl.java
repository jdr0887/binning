package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.IncidentalStatusTypeDAO;
import org.renci.binning.dao.clinbin.model.IncidentalStatusType;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { IncidentalStatusTypeDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class IncidentalStatusTypeDAOImpl extends BaseDAOImpl<IncidentalStatusType, String> implements IncidentalStatusTypeDAO {

    private static final Logger logger = LoggerFactory.getLogger(IncidentalStatusTypeDAOImpl.class);

    public IncidentalStatusTypeDAOImpl() {
        super();
    }

    @Override
    public Class<IncidentalStatusType> getPersistentClass() {
        return IncidentalStatusType.class;
    }

    @Override
    public List<IncidentalStatusType> findAll() throws BinningDAOException {
        logger.debug("ENTERING findAll()");
        List<IncidentalStatusType> ret = new ArrayList<>();
        try {
            TypedQuery<IncidentalStatusType> query = getEntityManager().createNamedQuery("clinbin.IncidentalStatusType.findAll",
                    IncidentalStatusType.class);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
