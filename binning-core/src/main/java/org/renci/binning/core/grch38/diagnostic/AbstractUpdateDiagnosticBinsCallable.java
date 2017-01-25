package org.renci.binning.core.grch38.diagnostic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.collections.CollectionUtils;
import org.renci.binning.core.BinningException;
import org.renci.binning.core.grch38.BinResultsFinalDiagnosticFactory;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.refseq.model.Variants_78_4;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUpdateDiagnosticBinsCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUpdateDiagnosticBinsCallable.class);

    private BinningDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public AbstractUpdateDiagnosticBinsCallable(BinningDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public Void call() throws BinningException {
        logger.debug("ENTERING call()");

        try {

            // maybe delete by dxId as well as assemblyId?
            daoBean.getBinResultsFinalDiagnosticDAO().deleteByAssemblyId(binningJob.getAssembly().getId());

            List<LocatedVariant> locatedVariantList = daoBean.getLocatedVariantDAO().findByAssemblyId(binningJob.getAssembly().getId());

            List<Variants_78_4> variants = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(locatedVariantList)) {
                logger.info(String.format("locatedVariantList.size(): %d", locatedVariantList.size()));
                for (LocatedVariant locatedVariant : locatedVariantList) {
                    List<Variants_78_4> foundVariants = daoBean.getVariants_78_4_DAO().findByLocatedVariantId(locatedVariant.getId());
                    if (CollectionUtils.isNotEmpty(foundVariants)) {
                        variants.addAll(foundVariants);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(variants)) {

                logger.info(String.format("variants.size(): %d", variants.size()));

                try {
                    // hgmd known pathogenic...disease class 1
                    List<BinResultsFinalDiagnostic> knownPathogenic = BinResultsFinalDiagnosticFactory.findHGMDKnownPathogenic(daoBean,
                            binningJob, variants);
                    if (CollectionUtils.isNotEmpty(knownPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : knownPathogenic) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getKey(), 1);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }

                    // clinvar known pathogenic...disease class 1
                    knownPathogenic = BinResultsFinalDiagnosticFactory.findClinVarKnownPathogenic(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(knownPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : knownPathogenic) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndClinVarDiseaseClassId(binResultsFinalDiagnostic.getKey(), 1);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    // hgmd likely pathogenic...disease class 2
                    List<BinResultsFinalDiagnostic> likelyPathogenic = BinResultsFinalDiagnosticFactory.findHGMDLikelyPathogenic(daoBean,
                            binningJob, variants);
                    if (CollectionUtils.isNotEmpty(likelyPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : likelyPathogenic) {
                            logger.info(binResultsFinalDiagnostic.getKey().toString());
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getKey(), 2);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }

                    // clinvar likely pathogenic...disease class 2
                    likelyPathogenic = BinResultsFinalDiagnosticFactory.findHGMDLikelyPathogenic(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(likelyPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : likelyPathogenic) {
                            logger.info(binResultsFinalDiagnostic.getKey().toString());
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndClinVarDiseaseClassId(binResultsFinalDiagnostic.getKey(), 2);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    // hgmd possibly pathogenic...disease class 3
                    List<BinResultsFinalDiagnostic> possiblyPathogenic = BinResultsFinalDiagnosticFactory
                            .findHGMDPossiblyPathogenic(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(possiblyPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : possiblyPathogenic) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getKey(), 3);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }

                    // clinvar possibly pathogenic...disease class 3
                    possiblyPathogenic = BinResultsFinalDiagnosticFactory.findClinVarPossiblyPathogenic(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(possiblyPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : possiblyPathogenic) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndClinVarDiseaseClassId(binResultsFinalDiagnostic.getKey(), 3);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    // hgmd uncertain significance...disease class 4
                    List<BinResultsFinalDiagnostic> uncertainSignificance = BinResultsFinalDiagnosticFactory
                            .findHGMDUncertainSignificance(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(uncertainSignificance)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : uncertainSignificance) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getKey(), 4);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }

                    // clinvar uncertain significance...disease class 4
                    uncertainSignificance = BinResultsFinalDiagnosticFactory.findClinVarUncertainSignificance(daoBean, binningJob,
                            variants);
                    if (CollectionUtils.isNotEmpty(uncertainSignificance)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : uncertainSignificance) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndClinVarDiseaseClassId(binResultsFinalDiagnostic.getKey(), 4);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    // hgmd likely benign...disease class 5
                    List<BinResultsFinalDiagnostic> likelyBenign = BinResultsFinalDiagnosticFactory.findHGMDLikelyBenign(daoBean,
                            binningJob, variants);
                    if (CollectionUtils.isNotEmpty(likelyBenign)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : likelyBenign) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getKey(), 5);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }

                    // clinvar likely benign...disease class 5
                    likelyBenign = BinResultsFinalDiagnosticFactory.findHGMDLikelyBenign(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(likelyBenign)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : likelyBenign) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndClinVarDiseaseClassId(binResultsFinalDiagnostic.getKey(), 5);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    // hgmd almost certainly benign...disease class 6
                    List<BinResultsFinalDiagnostic> almostCertainlyBenign = BinResultsFinalDiagnosticFactory
                            .findHGMDAlmostCertainlyBenign(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(almostCertainlyBenign)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : almostCertainlyBenign) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndHGMDDiseaseClassId(binResultsFinalDiagnostic.getKey(), 6);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }

                    // clinvar almost certainly benign...disease class 6
                    almostCertainlyBenign = BinResultsFinalDiagnosticFactory.findClinVarAlmostCertainlyBenign(daoBean, binningJob,
                            variants);
                    if (CollectionUtils.isNotEmpty(almostCertainlyBenign)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : almostCertainlyBenign) {
                            List<BinResultsFinalDiagnostic> foundBinResultsFinalDiagnostics = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findByKeyAndClinVarDiseaseClassId(binResultsFinalDiagnostic.getKey(), 6);
                            if (CollectionUtils.isEmpty(foundBinResultsFinalDiagnostics)) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }
                // });
                //
                // es.shutdown();
                // es.awaitTermination(2L, TimeUnit.HOURS);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return null;
    }

}
