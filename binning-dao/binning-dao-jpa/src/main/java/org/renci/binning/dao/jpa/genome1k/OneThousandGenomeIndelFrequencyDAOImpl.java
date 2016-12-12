package org.renci.binning.dao.jpa.genome1k;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.OneThousandGenomeIndelFrequencyDAO;
import org.renci.binning.dao.genome1k.model.OneThousandGenomeIndelFrequency;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { OneThousandGenomeIndelFrequencyDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class OneThousandGenomeIndelFrequencyDAOImpl extends BaseDAOImpl<OneThousandGenomeIndelFrequency, Long>
        implements OneThousandGenomeIndelFrequencyDAO {

    private static final Logger logger = LoggerFactory.getLogger(OneThousandGenomeIndelFrequencyDAOImpl.class);

    public OneThousandGenomeIndelFrequencyDAOImpl() {
        super();
    }

    @Override
    public Class<OneThousandGenomeIndelFrequency> getPersistentClass() {
        return OneThousandGenomeIndelFrequency.class;
    }

    @Override
    public List<OneThousandGenomeIndelFrequency> findByLocatedVariantIdAndVersion(Long locVarId, Integer version)
            throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, Integer)");
        TypedQuery<OneThousandGenomeIndelFrequency> query = getEntityManager()
                .createNamedQuery("IndelFrequency.findByLocatedVariantIdAndVersion", OneThousandGenomeIndelFrequency.class);
        query.setParameter("LocatedVariantId", locVarId);
        query.setParameter("version", version);
        List<OneThousandGenomeIndelFrequency> ret = query.getResultList();
        return ret;
    }

}
