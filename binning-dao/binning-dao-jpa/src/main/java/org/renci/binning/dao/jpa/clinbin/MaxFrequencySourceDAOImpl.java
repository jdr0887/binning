package org.renci.binning.dao.jpa.clinbin;

import org.renci.binning.dao.clinbin.MaxFrequencySourceDAO;
import org.renci.binning.dao.clinbin.model.MaxFrequencySource;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
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
