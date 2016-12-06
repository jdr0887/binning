package org.renci.binning.dao.jpa.dbsnp;

import org.renci.binning.dao.dbsnp.SNPAlleleDAO;
import org.renci.binning.dao.dbsnp.model.SNPAllele;
import org.renci.binning.dao.dbsnp.model.SNPAllelePK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class SNPAlleleDAOImpl extends BaseDAOImpl<SNPAllele, SNPAllelePK> implements SNPAlleleDAO {

    private static final Logger logger = LoggerFactory.getLogger(SNPAlleleDAOImpl.class);

    public SNPAlleleDAOImpl() {
        super();
    }

    @Override
    public Class<SNPAllele> getPersistentClass() {
        return SNPAllele.class;
    }

}
