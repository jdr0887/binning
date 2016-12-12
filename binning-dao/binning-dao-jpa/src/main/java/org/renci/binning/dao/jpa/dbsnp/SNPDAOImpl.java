package org.renci.binning.dao.jpa.dbsnp;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.dbsnp.SNPDAO;
import org.renci.binning.dao.dbsnp.model.SNP;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { SNPDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
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
