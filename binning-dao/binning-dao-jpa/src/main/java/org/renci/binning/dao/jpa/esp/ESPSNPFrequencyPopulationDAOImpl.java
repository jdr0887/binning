package org.renci.binning.dao.jpa.esp;

import java.util.List;

import javax.persistence.TypedQuery;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.esp.ESPSNPFrequencyPopulationDAO;
import org.renci.binning.dao.esp.model.ESPSNPFrequencyPopulation;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class ESPSNPFrequencyPopulationDAOImpl extends BaseDAOImpl<ESPSNPFrequencyPopulation, Long> implements ESPSNPFrequencyPopulationDAO {

    private static final Logger logger = LoggerFactory.getLogger(ESPSNPFrequencyPopulationDAOImpl.class);

    public ESPSNPFrequencyPopulationDAOImpl() {
        super();
    }

    @Override
    public Class<ESPSNPFrequencyPopulation> getPersistentClass() {
        return ESPSNPFrequencyPopulation.class;
    }

    @Override
    public List<ESPSNPFrequencyPopulation> findByLocatedVariantIdAndVersion(Long locVarId, Integer version) throws BinningDAOException {
        logger.debug("ENTERING findByLocatedVariantIdAndVersion(Long, Integer)");
        TypedQuery<ESPSNPFrequencyPopulation> query = getEntityManager()
                .createNamedQuery("esp.SNPFrequencyPopulation.findByLocatedVariantIdAndVersion", ESPSNPFrequencyPopulation.class);
        query.setParameter("LocatedVariantId", locVarId);
        query.setParameter("version", version);
        List<ESPSNPFrequencyPopulation> ret = query.getResultList();
        return ret;
    }

}
