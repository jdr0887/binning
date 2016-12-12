package org.renci.binning.dao.jpa.clinbin;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.clinbin.CarrierStatusDAO;
import org.renci.binning.dao.clinbin.model.CarrierStatus;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { CarrierStatusDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class CarrierStatusDAOImpl extends BaseDAOImpl<CarrierStatus, Integer> implements CarrierStatusDAO {

    private static final Logger logger = LoggerFactory.getLogger(CarrierStatusDAOImpl.class);

    public CarrierStatusDAOImpl() {
        super();
    }

    @Override
    public Class<CarrierStatus> getPersistentClass() {
        return CarrierStatus.class;
    }

}
