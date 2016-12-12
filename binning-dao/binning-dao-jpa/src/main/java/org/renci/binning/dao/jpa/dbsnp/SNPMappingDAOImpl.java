package org.renci.binning.dao.jpa.dbsnp;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.dbsnp.SNPMappingDAO;
import org.renci.binning.dao.dbsnp.model.SNPMapping;
import org.renci.binning.dao.dbsnp.model.SNPMappingPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { SNPMappingDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class SNPMappingDAOImpl extends BaseDAOImpl<SNPMapping, SNPMappingPK> implements SNPMappingDAO {

    private static final Logger logger = LoggerFactory.getLogger(SNPMappingDAOImpl.class);

    public SNPMappingDAOImpl() {
        super();
    }

    @Override
    public Class<SNPMapping> getPersistentClass() {
        return SNPMapping.class;
    }

}
