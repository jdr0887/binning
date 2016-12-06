package org.renci.binning.dao.hgnc;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.hgnc.model.HGNCGene;

public interface HGNCGeneDAO extends BaseDAO<HGNCGene, Integer> {

    public List<HGNCGene> findByAnnotationGeneExternalIdsNamespace(String namespace) throws BinningDAOException;

    public List<HGNCGene> findByAnnotationGeneExternalIdsGeneIdsAndNamespace(Integer geneId, String namespace) throws BinningDAOException;

    public List<HGNCGene> findByName(String name) throws BinningDAOException;

    public List<HGNCGene> findBySymbol(String symbol) throws BinningDAOException;

}
