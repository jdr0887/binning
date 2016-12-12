package org.renci.binning.dao.jpa.dbsnp;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.dbsnp.SNPAlleleFrequencyDAO;
import org.renci.binning.dao.dbsnp.model.SNPAlleleFrequency;
import org.renci.binning.dao.dbsnp.model.SNPAlleleFrequencyPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { SNPAlleleFrequencyDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class SNPAlleleFrequencyDAOImpl extends BaseDAOImpl<SNPAlleleFrequency, SNPAlleleFrequencyPK> implements SNPAlleleFrequencyDAO {

    private static final Logger logger = LoggerFactory.getLogger(SNPAlleleFrequencyDAOImpl.class);

    public SNPAlleleFrequencyDAOImpl() {
        super();
    }

    @Override
    public Class<SNPAlleleFrequency> getPersistentClass() {
        return SNPAlleleFrequency.class;
    }

}
