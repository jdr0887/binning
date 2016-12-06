package org.renci.binning.dao.jpa.dbsnp;

import org.renci.binning.dao.dbsnp.SNPDAO;
import org.renci.binning.dao.dbsnp.model.SNP;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class SNPDAOImpl extends BaseDAOImpl<SNP, Integer> implements SNPDAO {

    private static final Logger logger = LoggerFactory.getLogger(SNPDAOImpl.class);

    public SNPDAOImpl() {
        super();
    }

    @Override
    public Class<SNP> getPersistentClass() {
        return SNP.class;
    }

}
