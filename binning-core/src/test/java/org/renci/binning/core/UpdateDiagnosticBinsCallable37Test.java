package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.renci.canvas.binning.core.grch37.BinResultsFinalDiagnosticFactory;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.jpa.CANVASDAOManager;
import org.renci.canvas.dao.refseq.model.Variants_61_2;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateDiagnosticBinsCallable37Test {

    private static final Logger logger = LoggerFactory.getLogger(UpdateDiagnosticBinsCallable37Test.class);

    private static final BinResultsFinalDiagnosticFactory binResultsFinalDiagnosticFactory = BinResultsFinalDiagnosticFactory.getInstance();

    private static final CANVASDAOManager daoMgr = CANVASDAOManager.getInstance();

    public UpdateDiagnosticBinsCallable37Test() {
        super();
    }

    @Test
    public void testKnownPathogenic() throws CANVASDAOException {
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
            results.addAll(binResultsFinalDiagnosticFactory.findHGMDKnownPathogenic(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }

        assertTrue(CollectionUtils.isNotEmpty(results));
        assertTrue(results.size() == 28);
        results.forEach(a -> logger.info(a.toString()));
        logger.info("results.size(): {}", results.size());
    }

    @Test
    public void testKnownPathogenicForGSU_000136() throws CANVASDAOException {
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
            results.addAll(binResultsFinalDiagnosticFactory.findHGMDKnownPathogenic(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
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
    public void testLikelyPathenogenic() throws CANVASDAOException {
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
            results.addAll(binResultsFinalDiagnosticFactory.findHGMDLikelyPathogenic(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        results.forEach(a -> logger.info(a.toString()));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 11);
    }

    @Test
    public void testPossiblyPathenogenic() throws CANVASDAOException {
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
                    binResultsFinalDiagnosticFactory.findHGMDPossiblyPathogenic(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 54);
        results.forEach(a -> logger.info(a.toString()));
    }

    @Test
    public void testUncertainSignificance() throws CANVASDAOException {
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
                    binResultsFinalDiagnosticFactory.findHGMDUncertainSignificance(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 151);
        results.forEach(a -> logger.info(a.toString()));
    }

    @Test
    public void testLikelyBenign() throws CANVASDAOException {
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
            results.addAll(binResultsFinalDiagnosticFactory.findHGMDLikelyBenign(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 1);
        results.forEach(a -> logger.info(a.toString()));
    }

    @Test
    public void testAlmostCertainlyBenign() throws CANVASDAOException {
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
                    binResultsFinalDiagnosticFactory.findHGMDAlmostCertainlyBenign(daoMgr.getDAOBean(), diagnosticBinningJob, variants));
        }
        assertTrue(CollectionUtils.isNotEmpty(results));
        logger.info("results.size(): {}", results.size());
        assertTrue(results.size() == 318);
        results.forEach(a -> logger.info(a.getLocatedVariant().getId().toString()));
    }

}
