package org.renci.binning.dao.clinbin;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticGene;

public interface DiagnosticGeneDAO extends BaseDAO<DiagnosticGene, Integer> {

    public List<DiagnosticGene> findByGeneIdAndDXId(Integer geneId, Integer dxId) throws BinningDAOException;

}
