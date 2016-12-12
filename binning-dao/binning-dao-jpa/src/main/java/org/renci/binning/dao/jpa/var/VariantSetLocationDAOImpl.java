package org.renci.binning.dao.jpa.var;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.VariantSetLocationDAO;
import org.renci.binning.dao.var.model.VariantSetLocation;
import org.renci.binning.dao.var.model.VariantSetLocationPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional
@OsgiServiceProvider(classes = { VariantSetLocationDAO.class })
@javax.transaction.Transactional
@Singleton
public class VariantSetLocationDAOImpl extends BaseDAOImpl<VariantSetLocation, VariantSetLocationPK> implements VariantSetLocationDAO {

    private static final Logger logger = LoggerFactory.getLogger(VariantSetLocationDAOImpl.class);

    public VariantSetLocationDAOImpl() {
        super();
    }

    @Override
    public Class<VariantSetLocation> getPersistentClass() {
        return VariantSetLocation.class;
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public VariantSetLocationPK save(VariantSetLocation entity) throws BinningDAOException {
        logger.debug("ENTERING save(Assembly)");
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

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public void delete(VariantSetLocation entity) throws BinningDAOException {
        logger.debug("ENTERING delete(VariantSetLocation)");
        VariantSetLocation foundEntity = getEntityManager().find(getPersistentClass(), entity.getKey());
        getEntityManager().remove(foundEntity);
    }

}
