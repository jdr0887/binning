package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DX;

public interface DXDAO extends BaseDAO<DX, Integer> {

    public List<DX> findAll() throws BinningDAOException;

}
