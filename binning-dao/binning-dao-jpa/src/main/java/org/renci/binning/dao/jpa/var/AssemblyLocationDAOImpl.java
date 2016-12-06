package org.renci.binning.dao.jpa.var;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.AssemblyLocationDAO;
import org.renci.binning.dao.var.model.AssemblyLocation;
import org.renci.binning.dao.var.model.AssemblyLocationPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AssemblyLocationDAOImpl extends BaseDAOImpl<AssemblyLocation, AssemblyLocationPK> implements AssemblyLocationDAO {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyLocationDAOImpl.class);

    public AssemblyLocationDAOImpl() {
        super();
    }

    @Override
    public Class<AssemblyLocation> getPersistentClass() {
        return AssemblyLocation.class;
    }

    @Override
    public synchronized AssemblyLocationPK save(AssemblyLocation entity) throws BinningDAOException {
        logger.debug("ENTERING save(AssemblyLocation)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getKey() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getKey();
    }

    @Override
    public void delete(AssemblyLocation entity) throws BinningDAOException {
        logger.debug("ENTERING delete(AssemblyLocation)");
        AssemblyLocation foundEntity = getEntityManager().find(getPersistentClass(), entity.getKey());
        getEntityManager().remove(foundEntity);
    }

}
