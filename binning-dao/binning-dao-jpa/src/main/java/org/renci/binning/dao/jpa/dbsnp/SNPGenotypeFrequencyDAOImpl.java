package org.renci.binning.dao.jpa.dbsnp;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.dbsnp.SNPGenotypeFrequencyDAO;
import org.renci.binning.dao.dbsnp.model.SNPGenotypeFrequency;
import org.renci.binning.dao.dbsnp.model.SNPGenotypeFrequencyPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { SNPGenotypeFrequencyDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class SNPGenotypeFrequencyDAOImpl extends BaseDAOImpl<SNPGenotypeFrequency, SNPGenotypeFrequencyPK>
        implements SNPGenotypeFrequencyDAO {

    private static final Logger logger = LoggerFactory.getLogger(SNPGenotypeFrequencyDAOImpl.class);

    public SNPGenotypeFrequencyDAOImpl() {
        super();
    }

    @Override
    public Class<SNPGenotypeFrequency> getPersistentClass() {
        return SNPGenotypeFrequency.class;
    }

}
