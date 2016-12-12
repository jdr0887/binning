package org.renci.binning.dao.jpa.refseq;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.LocationTypeDAO;
import org.renci.binning.dao.refseq.model.LocationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { LocationTypeDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class LocationTypeDAOImpl extends BaseDAOImpl<LocationType, String> implements LocationTypeDAO {

    private static final Logger logger = LoggerFactory.getLogger(LocationTypeDAOImpl.class);

    public LocationTypeDAOImpl() {
        super();
    }

    @Override
    public Class<LocationType> getPersistentClass() {
        return LocationType.class;
    }

}
