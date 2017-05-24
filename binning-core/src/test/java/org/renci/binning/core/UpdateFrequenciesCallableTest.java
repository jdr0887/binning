package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.collections.CollectionUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.MaxFrequency;
import org.renci.canvas.dao.clinbin.model.MaxFrequencyPK;
import org.renci.canvas.dao.genome1k.model.IndelMaxFrequency;
import org.renci.canvas.dao.genome1k.model.IndelMaxFrequencyPK;
import org.renci.canvas.dao.genome1k.model.SNPPopulationMaxFrequency;
import org.renci.canvas.dao.genome1k.model.SNPPopulationMaxFrequencyPK;
import org.renci.canvas.dao.jpa.CANVASDAOBeanServiceImpl;
import org.renci.canvas.dao.jpa.annotation.AnnotationGeneExternalIdDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.DiagnosticBinningJobDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.DiagnosticGeneDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.DiagnosticResultVersionDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.DiseaseClassDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.MaxFrequencyDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.MaxFrequencySourceDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.NCGenesFrequenciesDAOImpl;
import org.renci.canvas.dao.jpa.clinbin.UnimportantExonDAOImpl;
import org.renci.canvas.dao.jpa.clinvar.ReferenceClinicalAssertionDAOImpl;
import org.renci.canvas.dao.jpa.dbsnp.SNPMappingAggDAOImpl;
import org.renci.canvas.dao.jpa.genome1k.IndelMaxFrequencyDAOImpl;
import org.renci.canvas.dao.jpa.genome1k.SNPPopulationMaxFrequencyDAOImpl;
import org.renci.canvas.dao.jpa.hgnc.HGNCGeneDAOImpl;
import org.renci.canvas.dao.jpa.refseq.FeatureDAOImpl;
import org.renci.canvas.dao.jpa.refseq.LocationTypeDAOImpl;
import org.renci.canvas.dao.jpa.refseq.RefSeqCodingSequenceDAOImpl;
import org.renci.canvas.dao.jpa.refseq.RefSeqGeneDAOImpl;
import org.renci.canvas.dao.jpa.refseq.RegionGroupRegionDAOImpl;
import org.renci.canvas.dao.jpa.refseq.TranscriptMapsDAOImpl;
import org.renci.canvas.dao.jpa.refseq.TranscriptMapsExonsDAOImpl;
import org.renci.canvas.dao.jpa.refseq.VariantEffectDAOImpl;
import org.renci.canvas.dao.jpa.refseq.Variants_80_4_DAOImpl;
import org.renci.canvas.dao.jpa.var.AssemblyLocatedVariantDAOImpl;
import org.renci.canvas.dao.jpa.var.AssemblyLocatedVariantQCDAOImpl;
import org.renci.canvas.dao.jpa.var.CanonicalAlleleDAOImpl;
import org.renci.canvas.dao.jpa.var.LocatedVariantDAOImpl;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateFrequenciesCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(UpdateFrequenciesCallableTest.class);

    private static EntityManagerFactory emf;

    private static EntityManager em;

    private static CANVASDAOBeanServiceImpl daoBean = new CANVASDAOBeanServiceImpl();

    public UpdateFrequenciesCallableTest() {
        super();
    }

    @BeforeClass
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("canvas_test", null);
        em = emf.createEntityManager();

        TranscriptMapsDAOImpl transcriptMapsDAO = new TranscriptMapsDAOImpl();
        transcriptMapsDAO.setEntityManager(em);
        daoBean.setTranscriptMapsDAO(transcriptMapsDAO);

        TranscriptMapsExonsDAOImpl transcriptMapsExonsDAO = new TranscriptMapsExonsDAOImpl();
        transcriptMapsExonsDAO.setEntityManager(em);
        daoBean.setTranscriptMapsExonsDAO(transcriptMapsExonsDAO);

        LocationTypeDAOImpl locationTypeDAO = new LocationTypeDAOImpl();
        locationTypeDAO.setEntityManager(em);
        daoBean.setLocationTypeDAO(locationTypeDAO);

        VariantEffectDAOImpl variantEffectDAO = new VariantEffectDAOImpl();
        variantEffectDAO.setEntityManager(em);
        daoBean.setVariantEffectDAO(variantEffectDAO);

        RefSeqGeneDAOImpl refSeqGeneDAO = new RefSeqGeneDAOImpl();
        refSeqGeneDAO.setEntityManager(em);
        daoBean.setRefSeqGeneDAO(refSeqGeneDAO);

        HGNCGeneDAOImpl hgncGeneDAO = new HGNCGeneDAOImpl();
        hgncGeneDAO.setEntityManager(em);
        daoBean.setHGNCGeneDAO(hgncGeneDAO);

        AnnotationGeneExternalIdDAOImpl annotationGeneExternalIdDAO = new AnnotationGeneExternalIdDAOImpl();
        annotationGeneExternalIdDAO.setEntityManager(em);
        daoBean.setAnnotationGeneExternalIdDAO(annotationGeneExternalIdDAO);

        RegionGroupRegionDAOImpl regionGroupRegionDAO = new RegionGroupRegionDAOImpl();
        regionGroupRegionDAO.setEntityManager(em);
        daoBean.setRegionGroupRegionDAO(regionGroupRegionDAO);

        RefSeqCodingSequenceDAOImpl refSeqCodingSequenceDAO = new RefSeqCodingSequenceDAOImpl();
        refSeqCodingSequenceDAO.setEntityManager(em);
        daoBean.setRefSeqCodingSequenceDAO(refSeqCodingSequenceDAO);

        FeatureDAOImpl featureDAO = new FeatureDAOImpl();
        featureDAO.setEntityManager(em);
        daoBean.setFeatureDAO(featureDAO);

        LocatedVariantDAOImpl locatedVariantDAO = new LocatedVariantDAOImpl();
        locatedVariantDAO.setEntityManager(em);
        daoBean.setLocatedVariantDAO(locatedVariantDAO);

        DiagnosticBinningJobDAOImpl diagnosticBinningJobDAO = new DiagnosticBinningJobDAOImpl();
        diagnosticBinningJobDAO.setEntityManager(em);
        daoBean.setDiagnosticBinningJobDAO(diagnosticBinningJobDAO);

        Variants_80_4_DAOImpl variants_80_4_DAO = new Variants_80_4_DAOImpl();
        variants_80_4_DAO.setEntityManager(em);
        daoBean.setVariants_80_4_DAO(variants_80_4_DAO);

        DiseaseClassDAOImpl diseaseClassDAO = new DiseaseClassDAOImpl();
        diseaseClassDAO.setEntityManager(em);
        daoBean.setDiseaseClassDAO(diseaseClassDAO);

        NCGenesFrequenciesDAOImpl ncgenesFrequenciesDAO = new NCGenesFrequenciesDAOImpl();
        ncgenesFrequenciesDAO.setEntityManager(em);
        daoBean.setNCGenesFrequenciesDAO(ncgenesFrequenciesDAO);

        DiagnosticResultVersionDAOImpl diagnosticResultVersionDAO = new DiagnosticResultVersionDAOImpl();
        diagnosticResultVersionDAO.setEntityManager(em);
        daoBean.setDiagnosticResultVersionDAO(diagnosticResultVersionDAO);

        ReferenceClinicalAssertionDAOImpl referenceClinicalAssertionDAO = new ReferenceClinicalAssertionDAOImpl();
        referenceClinicalAssertionDAO.setEntityManager(em);
        daoBean.setReferenceClinicalAssertionDAO(referenceClinicalAssertionDAO);

        CanonicalAlleleDAOImpl canonicalAlleleDAO = new CanonicalAlleleDAOImpl();
        canonicalAlleleDAO.setEntityManager(em);
        daoBean.setCanonicalAlleleDAO(canonicalAlleleDAO);

        MaxFrequencyDAOImpl maxFrequencyDAO = new MaxFrequencyDAOImpl();
        maxFrequencyDAO.setEntityManager(em);
        daoBean.setMaxFrequencyDAO(maxFrequencyDAO);

        DiagnosticGeneDAOImpl diagnosticGeneDAO = new DiagnosticGeneDAOImpl();
        diagnosticGeneDAO.setEntityManager(em);
        daoBean.setDiagnosticGeneDAO(diagnosticGeneDAO);

        SNPMappingAggDAOImpl snpMappingAggDAO = new SNPMappingAggDAOImpl();
        snpMappingAggDAO.setEntityManager(em);
        daoBean.setSNPMappingAggDAO(snpMappingAggDAO);

        SNPPopulationMaxFrequencyDAOImpl snpPopulationMaxFrequencyDAO = new SNPPopulationMaxFrequencyDAOImpl();
        snpPopulationMaxFrequencyDAO.setEntityManager(em);
        daoBean.setSNPPopulationMaxFrequencyDAO(snpPopulationMaxFrequencyDAO);

        IndelMaxFrequencyDAOImpl indelMaxFrequencyDAO = new IndelMaxFrequencyDAOImpl();
        indelMaxFrequencyDAO.setEntityManager(em);
        daoBean.setIndelMaxFrequencyDAO(indelMaxFrequencyDAO);

        MaxFrequencySourceDAOImpl maxFrequencySourceDAO = new MaxFrequencySourceDAOImpl();
        maxFrequencySourceDAO.setEntityManager(em);
        daoBean.setMaxFrequencySourceDAO(maxFrequencySourceDAO);

        AssemblyLocatedVariantDAOImpl assemblyLocatedVariantDAO = new AssemblyLocatedVariantDAOImpl();
        assemblyLocatedVariantDAO.setEntityManager(em);
        daoBean.setAssemblyLocatedVariantDAO(assemblyLocatedVariantDAO);

        AssemblyLocatedVariantQCDAOImpl assemblyLocatedVariantQCDAO = new AssemblyLocatedVariantQCDAOImpl();
        assemblyLocatedVariantQCDAO.setEntityManager(em);
        daoBean.setAssemblyLocatedVariantQCDAO(assemblyLocatedVariantQCDAO);

        UnimportantExonDAOImpl unimportantExonDAO = new UnimportantExonDAOImpl();
        unimportantExonDAO.setEntityManager(em);
        daoBean.setUnimportantExonDAO(unimportantExonDAO);

    }

    @AfterClass
    public static void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void test() throws CANVASDAOException, BinningException, IOException {

        List<LocatedVariant> locatedVariantList = daoBean.getLocatedVariantDAO().findByAssemblyId(36354);

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {

            List<MaxFrequency> results = new ArrayList<>();

            for (LocatedVariant locatedVariant : locatedVariantList) {
                logger.debug(locatedVariant.toString());

                SNPPopulationMaxFrequency snpPopulationMaxFrequency = daoBean.getSNPPopulationMaxFrequencyDAO()
                        .findById(new SNPPopulationMaxFrequencyPK(locatedVariant.getId(), 2));

                if (snpPopulationMaxFrequency != null) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 2);
                    MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                    if (maxFrequency == null) {
                        maxFrequency = new MaxFrequency(key, daoBean.getMaxFrequencySourceDAO().findById("snp"));
                        maxFrequency.setMaxAlleleFreq(snpPopulationMaxFrequency.getMaxAlleleFrequency());
                        results.add(maxFrequency);
                    }
                    continue;
                }

                IndelMaxFrequency indelMaxFrequency = daoBean.getIndelMaxFrequencyDAO()
                        .findById(new IndelMaxFrequencyPK(locatedVariant.getId(), 1));

                if (indelMaxFrequency != null) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 1);
                    MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                    if (maxFrequency == null) {
                        maxFrequency = new MaxFrequency(key, daoBean.getMaxFrequencySourceDAO().findById("indel"));
                        maxFrequency.setMaxAlleleFreq(indelMaxFrequency.getMaxAlleleFrequency());
                        results.add(maxFrequency);
                    }
                    continue;
                }

                if (snpPopulationMaxFrequency == null && indelMaxFrequency == null) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 0);
                    MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                    if (maxFrequency == null) {
                        maxFrequency = new MaxFrequency(key, daoBean.getMaxFrequencySourceDAO().findById("none"));
                        maxFrequency.setMaxAlleleFreq(0D);
                        results.add(maxFrequency);
                    }
                }

            }

            assertTrue(CollectionUtils.isEmpty(results));

            // if (CollectionUtils.isEmpty(results)) {
            // logger.info(String.format("attempting to save %d MaxFrequency instances", results.size()));
            // for (MaxFrequency maxFrequency : results) {
            // logger.info(maxFrequency.toString());
            // daoBean.getMaxFrequencyDAO().save(maxFrequency);
            // }
            // }

        }

    }

}
