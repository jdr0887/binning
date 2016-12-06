package org.renci.binning.dao;

import java.io.Serializable;

public interface BaseDAO<T extends Persistable, ID extends Serializable> {

    public abstract T findById(ID id) throws BinningDAOException;

}
