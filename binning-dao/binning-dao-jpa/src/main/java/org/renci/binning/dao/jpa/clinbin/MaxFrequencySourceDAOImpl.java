package org.renci.binning.dao.jpa.clinbin;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.clinbin.MaxFrequencySourceDAO;
import org.renci.binning.dao.clinbin.model.MaxFrequencySource;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { MaxFrequencySourceDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class MaxFrequencySourceDAOImpl extends BaseDAOImpl<MaxFrequencySource, String> implements MaxFrequencySourceDAO {

    private static final Logger logger = LoggerFactory.getLogger(MaxFrequencySourceDAOImpl.class);

    public MaxFrequencySourceDAOImpl() {
        super();
    }

    @Override
    public Class<MaxFrequencySource> getPersistentClass() {
        return MaxFrequencySource.class;
    }

}
