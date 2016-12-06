package org.renci.binning.dao.var;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.var.AssemblyDAO;
import org.renci.binning.dao.var.VariantSetLoadDAO;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.VariantSet;
import org.renci.binning.dao.var.model.VariantSetLoad;

public class VariantSetLoadTest {

    @Test
    public void testFindByExample() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        VariantSetLoadDAO variantSetLoadDAO = daoMgr.getDAOBean().getVariantSetLoadDAO();
        AssemblyDAO assemblyDAO = daoMgr.getDAOBean().getAssemblyDAO();

        VariantSetLoad example = new VariantSetLoad();
        example.setLoadFilename("/proj/renci/sequence_analysis/ncgenes/UNCseq0004/gatk.SNVcalls.vcf");
        example.setLoadProgramName("org.renci.sequencing.vcf.VcfLoader");
        example.setLoadProgramVersion("2.0");
        List<VariantSetLoad> results = variantSetLoadDAO.findByExample(example);
        if (CollectionUtils.isNotEmpty(results)) {
            VariantSetLoad vsl = results.get(0);
            VariantSet vs = vsl.getVariantSet();
            List<Assembly> foundAssemblies = assemblyDAO.findByVariantSetId(vs.getId());
            assertTrue(CollectionUtils.isNotEmpty(foundAssemblies));
        }

    }

}
