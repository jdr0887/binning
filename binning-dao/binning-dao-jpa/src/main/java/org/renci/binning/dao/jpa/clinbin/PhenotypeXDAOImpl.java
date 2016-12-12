package org.renci.binning.dao.jpa.clinbin;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.clinbin.PhenotypeXDAO;
import org.renci.binning.dao.clinbin.model.PhenotypeX;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { PhenotypeXDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class PhenotypeXDAOImpl extends BaseDAOImpl<PhenotypeX, Integer> implements PhenotypeXDAO {

    @Override
    public Class<PhenotypeX> getPersistentClass() {
        return PhenotypeX.class;
    }

}
