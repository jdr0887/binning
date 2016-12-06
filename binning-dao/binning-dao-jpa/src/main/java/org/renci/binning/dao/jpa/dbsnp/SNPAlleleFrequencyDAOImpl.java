package org.renci.binning.dao.jpa.dbsnp;

import org.renci.binning.dao.dbsnp.SNPAlleleFrequencyDAO;
import org.renci.binning.dao.dbsnp.model.SNPAlleleFrequency;
import org.renci.binning.dao.dbsnp.model.SNPAlleleFrequencyPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
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
