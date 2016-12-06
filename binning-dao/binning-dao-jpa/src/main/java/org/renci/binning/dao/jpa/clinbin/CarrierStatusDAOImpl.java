package org.renci.binning.dao.jpa.clinbin;

import org.renci.binning.dao.clinbin.CarrierStatusDAO;
import org.renci.binning.dao.clinbin.model.CarrierStatus;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
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
