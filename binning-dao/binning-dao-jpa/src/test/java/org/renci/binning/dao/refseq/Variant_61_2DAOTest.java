package org.renci.binning.dao.refseq;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.MaxFrequency;
import org.renci.binning.dao.exac.model.MaxVariantFrequency;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.refseq.Variants_61_2_DAO;
import org.renci.binning.dao.refseq.model.VariantEffect;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.renci.binning.dao.refseq.model.Variants_61_2PK;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.VariantType;

public class Variant_61_2DAOTest {

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Test
    public void testFindByAssemblyId() throws BinningDAOException {
        Variants_61_2_DAO variantDAO = daoMgr.getDAOBean().getVariants_61_2_DAO();
        List<Variants_61_2> variants = variantDAO.findByAssemblyId(35579);
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
    }

    @Test
    public void testFindTranscriptDependentCount() throws BinningDAOException {
        Variants_61_2_DAO variantDAO = daoMgr.getDAOBean().getVariants_61_2_DAO();
        Long count = variantDAO.findTranscriptDependentCount(35619);
        assertTrue(count != null);
        System.out.println(count);
    }

    @Test
    public void testFindCodingCount() throws BinningDAOException {
        Variants_61_2_DAO variantDAO = daoMgr.getDAOBean().getVariants_61_2_DAO();
        Long count = variantDAO.findCodingCount(35619);
        assertTrue(count != null);
        System.out.println(count);
    }

    @Test
    public void testFindNonCodingCount() throws BinningDAOException {
        Variants_61_2_DAO variantDAO = daoMgr.getDAOBean().getVariants_61_2_DAO();
        Long count = variantDAO.findNonCodingCount(35619);
        assertTrue(count != null);
        System.out.println(count);
    }

    @Test
    public void testFindByLocatedVariantId() throws BinningDAOException {
        Variants_61_2_DAO variantDAO = daoMgr.getDAOBean().getVariants_61_2_DAO();
        List<Variants_61_2> variants = variantDAO.findByLocatedVariantId(491812292L);
        assertTrue(CollectionUtils.isNotEmpty(variants));
        variants.forEach(a -> {
            System.out.println(a.toString());
            System.out.println(a.getLocatedVariant().toString());
        });
    }

    @Test
    public void testFindByName() throws BinningDAOException {
        Variants_61_2_DAO variantDAO = daoMgr.getDAOBean().getVariants_61_2_DAO();
        List<Variants_61_2> variants = variantDAO.findByGeneName("BRCA1");
        assertTrue(CollectionUtils.isNotEmpty(variants));
        variants.forEach(a -> {
            System.out.println(a.getLocatedVariant().toString());
        });
    }

    @Test
    public void testFindByAssemblyIdAndHGMDVersionAndMaxFrequencyThresholdAndGeneId() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<Variants_61_2> variants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByAssemblyIdAndSampleNameAndHGMDVersionAndMaxFrequencyThresholdAndGeneId(33269, "NCG_01089", 1, 0.1, 21084);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

    @Test
    public void testFindByHGMDVersionAndMaxFrequencyThresholdAndGeneIdAndVariantEffectList() throws BinningDAOException {
        List<String> variantEffectList = Arrays.asList("nonsense", "splice-site", "frameshifting indel", "nonsense indel",
                "boundary-crossing indel", "potential RNA-editing site");
        long startTime = System.currentTimeMillis();
        List<Variants_61_2> variants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                .findByHGMDVersionAndMaxFrequencyThresholdAndGeneIdAndVariantEffectList(2, 0.05, 21084, variantEffectList);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

    @Test
    public void testFindByGeneNameAndMaxAlleleFrequency() throws BinningDAOException {
        Variants_61_2_DAO variantDAO = daoMgr.getDAOBean().getVariants_61_2_DAO();
        List<Variants_61_2> variants = variantDAO.findByGeneNameAndMaxAlleleFrequency("BRCA1", 0.05);
        assertTrue(CollectionUtils.isNotEmpty(variants));
        int count = 0;
        for (Variants_61_2 variant : variants) {
            LocatedVariant locatedVariant = variant.getLocatedVariant();
            List<MaxFrequency> clinbinMaxFrequencies = locatedVariant.getMaxFreqs();
            count += clinbinMaxFrequencies.size();
            List<MaxVariantFrequency> exacMaxFrequencies = locatedVariant.getMaxVariantFrequencies();
            count += exacMaxFrequencies.size();
        }
        System.out.println(count);
    }

    @Test
    public void testFindKnownPathogenic() throws BinningDAOException {
        Variants_61_2_DAO variantDAO = daoMgr.getDAOBean().getVariants_61_2_DAO();
        long startTime = System.currentTimeMillis();
        List<Variants_61_2> variants = variantDAO.findKnownPathenogenic(1, 8, 1);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

    @Test
    public void testFindLikelyPathogenic() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<Variants_61_2> variants = daoMgr.getDAOBean().getVariants_61_2_DAO().findLikelyPathenogenic(1, 29, 1);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

    @Test
    public void testFindPossiblyPathogenic() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<Variants_61_2> variants = daoMgr.getDAOBean().getVariants_61_2_DAO().findPossiblyPathenogenic(1, 6, 1);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

    @Test
    public void testFindUncertainSignificance() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<Variants_61_2> variants = daoMgr.getDAOBean().getVariants_61_2_DAO().findUncertainSignificance(1, 29, 1);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

    @Test
    public void testFindLikelyBenign() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<Variants_61_2> variants = daoMgr.getDAOBean().getVariants_61_2_DAO().findLikelyBenign(1, 6, 1);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

    @Test
    public void testFindAlmostCertainlyBenign() throws BinningDAOException {
        long startTime = System.currentTimeMillis();
        List<Variants_61_2> variants = daoMgr.getDAOBean().getVariants_61_2_DAO().findAlmostCertainlyBenign(1, 6, 1);
        long endTime = System.currentTimeMillis();
        assertTrue(CollectionUtils.isNotEmpty(variants));
        System.out.println(variants.size());
        System.out.printf("duration = %d", ((endTime - startTime) / 1000) / 60);
    }

    @Test
    public void testSave() throws BinningDAOException {

        // GenomeRef genomeRef = new GenomeRef("NCBI", "BUILD.37.1", "37.1",
        // "file:///volume1/annotation/abeast/reference_fasta/BUILD.37.1/BUILD.37.1.fasta",
        // "file:///volume1/annotation/abeast/reference_fasta/BUILD.37.1/BUILD.37.1_extras.fasta");
        // genomeRef.setId(1);
        // daoMgr.getDAOBean().getGenomeRefDAO().save(genomeRef);
        //
        // GenomeRefSeq genomeRefSeq = new GenomeRefSeq("NC_000001.9", "1", "Chromosome");
        // daoMgr.getDAOBean().getGenomeRefSeqDAO().save(genomeRefSeq);
        //
        // LocatedVariant locatedVariant = new LocatedVariant(genomeRef, genomeRefSeq, 154614895, 154614896, vType, "G",
        // "A");
        // locatedVariant.setId(daoMgr.getDAOBean().getLocatedVariantDAO().save(locatedVariant));

        GenomeRef genomeRef = daoMgr.getDAOBean().getGenomeRefDAO().findById(1);
        GenomeRefSeq genomeRefSeq = daoMgr.getDAOBean().getGenomeRefSeqDAO().findById("NC_000001.9");
        VariantType variantType = daoMgr.getDAOBean().getVariantTypeDAO().findByName("snp").get(0);
        LocatedVariant locatedVariant = daoMgr.getDAOBean().getLocatedVariantDAO().findById(2L);
        VariantEffect variantEffect = daoMgr.getDAOBean().getVariantEffectDAO().findByName("missense").get(0);

        Variants_61_2PK key = new Variants_61_2PK(locatedVariant.getId(), "NC_000001.10", 47904478, variantType.getName(), "NM_004474.3",
                "exon", variantEffect.getName(), 1);

        Variants_61_2 variant = new Variants_61_2(key);
        variant.setAminoAcidEnd(224);
        variant.setAminoAcidStart(225);
        variant.setCodingSequencePosition(671);
        variant.setTranscriptPosition(2790);
        variant.setFeatureId(0);
        variant.setFinalAminoAcid("M");
        variant.setFrameshift(Boolean.FALSE);
        // variant.setGene(gene);
        variant.setGenomeRefSeq(genomeRefSeq);
        variant.setHgncGene("FOXD2");
        variant.setInframe(Boolean.FALSE);
        variant.setRefSeqGene("FOXD2");
        variant.setStrand("+");

        daoMgr.getDAOBean().getVariants_61_2_DAO().save(variant);

    }

}
