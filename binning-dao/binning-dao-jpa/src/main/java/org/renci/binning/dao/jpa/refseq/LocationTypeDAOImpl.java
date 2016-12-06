package org.renci.binning.dao.jpa.refseq;

import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.LocationTypeDAO;
import org.renci.binning.dao.refseq.model.LocationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
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
