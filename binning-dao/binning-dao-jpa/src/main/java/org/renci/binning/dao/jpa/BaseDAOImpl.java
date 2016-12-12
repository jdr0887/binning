package org.renci.binning.dao.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.Persistable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.springframework.transaction.annotation.Transactional
@javax.transaction.Transactional
public abstract class BaseDAOImpl<T extends Persistable, ID extends Serializable> implements BaseDAO<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(BaseDAOImpl.class);

    @PersistenceContext(name = "binning", unitName = "binning")
    private EntityManager entityManager;

    public BaseDAOImpl() {
        super();
    }

    public abstract Class<T> getPersistentClass();

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
    @Override
    public T findById(ID id) throws BinningDAOException {
        logger.debug("ENTERING findById(T)");
        T ret = entityManager.find(getPersistentClass(), id);
        return ret;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
