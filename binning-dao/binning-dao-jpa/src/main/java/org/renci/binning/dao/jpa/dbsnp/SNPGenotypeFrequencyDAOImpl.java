package org.renci.binning.dao.jpa.dbsnp;

import org.renci.binning.dao.dbsnp.SNPGenotypeFrequencyDAO;
import org.renci.binning.dao.dbsnp.model.SNPGenotypeFrequency;
import org.renci.binning.dao.dbsnp.model.SNPGenotypeFrequencyPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
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
