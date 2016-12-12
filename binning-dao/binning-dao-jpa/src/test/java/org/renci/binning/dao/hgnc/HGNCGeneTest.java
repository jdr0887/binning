package org.renci.binning.dao.hgnc;

import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.hgnc.model.HGNCGene;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class HGNCGeneTest {

    @Test
    public void testFindByGeneIdAndNamespace() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        HGNCGeneDAO hgncGeneDAO = daoMgr.getDAOBean().getHGNCGeneDAO();
        List<HGNCGene> foundHGNCGenes = hgncGeneDAO.findByAnnotationGeneExternalIdsGeneIdsAndNamespace(20051, "HGNC");
        foundHGNCGenes.forEach(a -> System.out.println(a.toString()));
    }

}
