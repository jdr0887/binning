package org.renci.binning.dao.jpa.exac;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.exac.VariantFrequencyDAO;
import org.renci.binning.dao.exac.model.VariantFrequency;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { VariantFrequencyDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class VariantFrequencyDAOImpl extends BaseDAOImpl<VariantFrequency, Long> implements VariantFrequencyDAO {

    private static final Logger logger = LoggerFactory.getLogger(VariantFrequencyDAOImpl.class);

    public VariantFrequencyDAOImpl() {
        super();
    }

    @Override
    public Class<VariantFrequency> getPersistentClass() {
        return VariantFrequency.class;
    }

    @Override
    public List<VariantFrequency> findByLocatedVariantIdAndVersion(Long locVarId, String version) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, String)");
        TypedQuery<VariantFrequency> query = getEntityManager().createNamedQuery("exac.VariantFrequency.findByLocatedVariantIdAndVersion",
                VariantFrequency.class);
        query.setParameter("LocatedVariantId", locVarId);
        query.setParameter("version", version);
        List<VariantFrequency> ret = query.getResultList();
        return ret;
    }

}
