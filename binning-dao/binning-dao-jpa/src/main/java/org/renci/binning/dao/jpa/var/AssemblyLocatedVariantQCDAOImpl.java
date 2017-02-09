package org.renci.binning.dao.jpa.var;

import javax.inject.Singleton;
import javax.persistence.Query;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.AssemblyLocatedVariantQCDAO;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQCPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional
@OsgiServiceProvider(classes = { AssemblyLocatedVariantQCDAO.class })
@javax.transaction.Transactional
@Singleton
public class AssemblyLocatedVariantQCDAOImpl extends BaseDAOImpl<AssemblyLocatedVariantQC, AssemblyLocatedVariantQCPK>
        implements AssemblyLocatedVariantQCDAO {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyLocatedVariantQCDAOImpl.class);

    public AssemblyLocatedVariantQCDAOImpl() {
        super();
    }

    @Override
    public Class<AssemblyLocatedVariantQC> getPersistentClass() {
        return AssemblyLocatedVariantQC.class;
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public synchronized AssemblyLocatedVariantQCPK save(AssemblyLocatedVariantQC entity) throws BinningDAOException {
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

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public void deleteByAssemblyId(Integer assemblyId) throws BinningDAOException {
        Query qDelete = getEntityManager()
                .createQuery("delete from " + getPersistentClass().getSimpleName() + " a where a.assembly.id = :assemblyId");
        qDelete.setParameter("assemblyId", assemblyId);
        qDelete.executeUpdate();
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public void delete(AssemblyLocatedVariantQC entity) throws BinningDAOException {
        logger.debug("ENTERING delete(AssemblyLocatedVariantQC)");
        AssemblyLocatedVariantQC foundEntity = getEntityManager().find(getPersistentClass(), entity.getKey());
        getEntityManager().remove(foundEntity);
    }

}