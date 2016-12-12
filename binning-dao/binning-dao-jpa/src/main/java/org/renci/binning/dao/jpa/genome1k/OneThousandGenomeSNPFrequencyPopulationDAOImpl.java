package org.renci.binning.dao.jpa.genome1k;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.genome1k.OneThousandGenomeSNPFrequencyPopulationDAO;
import org.renci.binning.dao.genome1k.model.OneThousandGenomeSNPFrequencyPopulation;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { OneThousandGenomeSNPFrequencyPopulationDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class OneThousandGenomeSNPFrequencyPopulationDAOImpl extends BaseDAOImpl<OneThousandGenomeSNPFrequencyPopulation, Long>
        implements OneThousandGenomeSNPFrequencyPopulationDAO {

    private static final Logger logger = LoggerFactory.getLogger(OneThousandGenomeSNPFrequencyPopulationDAOImpl.class);

    public OneThousandGenomeSNPFrequencyPopulationDAOImpl() {
        super();
    }

    @Override
    public Class<OneThousandGenomeSNPFrequencyPopulation> getPersistentClass() {
        return OneThousandGenomeSNPFrequencyPopulation.class;
    }

    @Override
    public List<OneThousandGenomeSNPFrequencyPopulation> findByLocatedVariantIdAndVersion(Long locVarId, Integer version)
            throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, Integer)");
        TypedQuery<OneThousandGenomeSNPFrequencyPopulation> query = getEntityManager().createNamedQuery(
                "genome1k.SNPFrequencyPopulation.findByLocatedVariantIdAndVersion", OneThousandGenomeSNPFrequencyPopulation.class);
        query.setParameter("LocatedVariantId", locVarId);
        query.setParameter("version", version);
        List<OneThousandGenomeSNPFrequencyPopulation> ret = query.getResultList();
        return ret;
    }

}
