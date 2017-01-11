package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateHGMDDiagnosticBinsCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(UpdateHGMDDiagnosticBinsCallableTest.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    public UpdateHGMDDiagnosticBinsCallableTest() {
        super();
    }

    @Test
    public void testKnownPathogenic() throws BinningDAOException {
        DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4207);
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO()
                .findByAssemblyId(diagnosticBinningJob.getAssembly().getId());

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {
            List<Variants_61_2> variants = new ArrayList<>();
            for (LocatedVariant locatedVariant : locatedVariantList) {
                List<Variants_61_2> foundVariants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                        .findByLocatedVariantId(locatedVariant.getId());
                assertTrue(CollectionUtils.isNotEmpty(foundVariants));
                variants.addAll(foundVariants);
            }
            results.addAll(BinResultsFinalDiagnosticFactory.findHGMDKnownPathogenic(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }

        assertTrue(CollectionUtils.isNotEmpty(results));
        assertTrue(results.size() == 28);
        results.forEach(a -> logger.info(a.toString()));
        logger.info("results.size(): {}", results.size());
    }

    @Test
    public void testKnownPathogenicForGSU_000136() throws BinningDAOException {
        // DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4218);
        DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4272);
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();

        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO()
                .findByAssemblyId(diagnosticBinningJob.getAssembly().getId());

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {
            List<Variants_61_2> variants = new ArrayList<>();
            for (LocatedVariant locatedVariant : locatedVariantList) {
                List<Variants_61_2> foundVariants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                        .findByLocatedVariantId(locatedVariant.getId());
                assertTrue(CollectionUtils.isNotEmpty(foundVariants));
                variants.addAll(foundVariants);
            }
            results.addAll(BinResultsFinalDiagnosticFactory.findHGMDKnownPathogenic(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }

        assertTrue(CollectionUtils.isNotEmpty(results));

        results.forEach(a -> {
            if (a.getLocatedVariant().getId() == 404841675) {
                logger.info(a.toString());
            }
        });
        assertTrue(results.size() == 14);
        logger.info("results.size(): {}", results.size());
    }

    @Test
    public void testLikelyPathenogenic() throws BinningDAOException {
        DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4218);
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO()
                .findByAssemblyId(diagnosticBinningJob.getAssembly().getId());

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {
            List<Variants_61_2> variants = new ArrayList<>();
            for (LocatedVariant locatedVariant : locatedVariantList) {
                List<Variants_61_2> foundVariants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                        .findByLocatedVariantId(locatedVariant.getId());
                assertTrue(CollectionUtils.isNotEmpty(foundVariants));
                variants.addAll(foundVariants);
            }
            results.addAll(BinResultsFinalDiagnosticFactory.findHGMDLikelyPathogenic(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        results.forEach(a -> logger.info(a.toString()));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 11);
    }

    @Test
    public void testPossiblyPathenogenic() throws BinningDAOException {
        DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4207);
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO()
                .findByAssemblyId(diagnosticBinningJob.getAssembly().getId());

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {
            List<Variants_61_2> variants = new ArrayList<>();
            for (LocatedVariant locatedVariant : locatedVariantList) {
                List<Variants_61_2> foundVariants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                        .findByLocatedVariantId(locatedVariant.getId());
                assertTrue(CollectionUtils.isNotEmpty(foundVariants));
                variants.addAll(foundVariants);
            }
            results.addAll(
                    BinResultsFinalDiagnosticFactory.findHGMDPossiblyPathogenic(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 54);
        results.forEach(a -> logger.info(a.toString()));
    }

    @Test
    public void testUncertainSignificance() throws BinningDAOException {
        DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4207);
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO()
                .findByAssemblyId(diagnosticBinningJob.getAssembly().getId());

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {
            List<Variants_61_2> variants = new ArrayList<>();
            for (LocatedVariant locatedVariant : locatedVariantList) {
                List<Variants_61_2> foundVariants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                        .findByLocatedVariantId(locatedVariant.getId());
                assertTrue(CollectionUtils.isNotEmpty(foundVariants));
                variants.addAll(foundVariants);
            }
            results.addAll(
                    BinResultsFinalDiagnosticFactory.findHGMDUncertainSignificance(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 151);
        results.forEach(a -> logger.info(a.toString()));
    }

    @Test
    public void testLikelyBenign() throws BinningDAOException {
        DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4207);
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO()
                .findByAssemblyId(diagnosticBinningJob.getAssembly().getId());

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {
            List<Variants_61_2> variants = new ArrayList<>();
            for (LocatedVariant locatedVariant : locatedVariantList) {
                List<Variants_61_2> foundVariants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                        .findByLocatedVariantId(locatedVariant.getId());
                assertTrue(CollectionUtils.isNotEmpty(foundVariants));
                variants.addAll(foundVariants);
            }
            results.addAll(BinResultsFinalDiagnosticFactory.findHGMDLikelyBenign(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 1);
        results.forEach(a -> logger.info(a.toString()));
    }

    @Test
    public void testAlmostCertainlyBenign() throws BinningDAOException {
        DiagnosticBinningJob diagnosticBinningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4207);
        List<BinResultsFinalDiagnostic> results = new ArrayList<>();
        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO()
                .findByAssemblyId(diagnosticBinningJob.getAssembly().getId());

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {
            List<Variants_61_2> variants = new ArrayList<>();
            for (LocatedVariant locatedVariant : locatedVariantList) {
                List<Variants_61_2> foundVariants = daoMgr.getDAOBean().getVariants_61_2_DAO()
                        .findByLocatedVariantId(locatedVariant.getId());
                assertTrue(CollectionUtils.isNotEmpty(foundVariants));
                variants.addAll(foundVariants);
            }
            results.addAll(
                    BinResultsFinalDiagnosticFactory.findHGMDAlmostCertainlyBenign(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 318);
        results.forEach(a -> logger.info(a.getLocatedVariant().getId().toString()));
    }

}
