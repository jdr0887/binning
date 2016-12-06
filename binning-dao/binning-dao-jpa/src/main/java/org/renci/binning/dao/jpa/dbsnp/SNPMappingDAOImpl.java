package org.renci.binning.dao.jpa.dbsnp;

import org.renci.binning.dao.dbsnp.SNPMappingDAO;
import org.renci.binning.dao.dbsnp.model.SNPMapping;
import org.renci.binning.dao.dbsnp.model.SNPMappingPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
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
