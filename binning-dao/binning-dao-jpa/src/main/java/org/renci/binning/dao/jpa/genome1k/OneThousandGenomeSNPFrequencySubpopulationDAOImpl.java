package org.renci.binning.dao.jpa.genome1k;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.OneThousandGenomeSNPFrequencySubpopulationDAO;
import org.renci.binning.dao.genome1k.model.OneThousandGenomeSNPFrequencySubpopulation;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { OneThousandGenomeSNPFrequencySubpopulationDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class OneThousandGenomeSNPFrequencySubpopulationDAOImpl extends BaseDAOImpl<OneThousandGenomeSNPFrequencySubpopulation, Long>
        implements OneThousandGenomeSNPFrequencySubpopulationDAO {

    private static final Logger logger = LoggerFactory.getLogger(OneThousandGenomeSNPFrequencySubpopulationDAOImpl.class);

    public OneThousandGenomeSNPFrequencySubpopulationDAOImpl() {
        super();
    }

    @Override
    public Class<OneThousandGenomeSNPFrequencySubpopulation> getPersistentClass() {
        return OneThousandGenomeSNPFrequencySubpopulation.class;
    }

    @Override
    public List<OneThousandGenomeSNPFrequencySubpopulation> findByLocatedVariantIdAndVersion(Long locVarId, Integer version)
            throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, Integer)");
        TypedQuery<OneThousandGenomeSNPFrequencySubpopulation> query = getEntityManager().createNamedQuery(
                "SNPFrequencySubpopulation.findByLocatedVariantIdAndVersion", OneThousandGenomeSNPFrequencySubpopulation.class);
        query.setParameter("LocatedVariantId", locVarId);
        query.setParameter("version", version);
        List<OneThousandGenomeSNPFrequencySubpopulation> ret = query.getResultList();
        return ret;
    }

}
