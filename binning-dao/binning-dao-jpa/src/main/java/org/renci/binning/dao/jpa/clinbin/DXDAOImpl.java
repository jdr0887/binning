package org.renci.binning.dao.jpa.clinbin;

import java.util.List;

import javax.persistence.TypedQuery;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DXDAO;
import org.renci.binning.dao.clinbin.model.DX;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class DXDAOImpl extends BaseDAOImpl<DX, Long> implements DXDAO {

    private static final Logger logger = LoggerFactory.getLogger(DXDAOImpl.class);

    public DXDAOImpl() {
        super();
    }

    @Override
    public Class<DX> getPersistentClass() {
        return DX.class;
    }

    @Override
    public List<DX> findAll() throws BinningDAOException {
        logger.debug("ENTERING findAll()");
        TypedQuery<DX> query = getEntityManager().createNamedQuery("DX.findAll", DX.class);
        List<DX> ret = query.getResultList();
        return ret;
    }

}
