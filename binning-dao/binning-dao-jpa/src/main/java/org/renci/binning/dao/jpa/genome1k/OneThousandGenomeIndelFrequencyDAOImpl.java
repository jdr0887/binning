package org.renci.binning.dao.jpa.genome1k;

import java.util.List;

import javax.persistence.TypedQuery;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.OneThousandGenomeIndelFrequencyDAO;
import org.renci.binning.dao.genome1k.model.OneThousandGenomeIndelFrequency;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
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
