package org.renci.binning.core.diagnostic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.collections.CollectionUtils;
import org.renci.binning.core.BinResultsFinalDiagnosticFactory;
import org.renci.binning.core.BinningException;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.refseq.model.Variants_61_2;
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

            List<Variants_61_2> variants = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(locatedVariantList)) {
                logger.info(String.format("locatedVariantList.size(): %d", locatedVariantList.size()));
                for (LocatedVariant locatedVariant : locatedVariantList) {
                    List<Variants_61_2> foundVariants = daoBean.getVariants_61_2_DAO().findByLocatedVariantId(locatedVariant.getId());
                    if (CollectionUtils.isNotEmpty(foundVariants)) {
                        variants.addAll(foundVariants);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(variants)) {

                logger.info(String.format("variants.size(): %d", variants.size()));

                try {
                    List<BinResultsFinalDiagnostic> hgmdKnownPathogenic = BinResultsFinalDiagnosticFactory.findHGMDKnownPathogenic(daoBean,
                            binningJob, variants);
                    if (CollectionUtils.isNotEmpty(hgmdKnownPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : hgmdKnownPathogenic) {
                            BinResultsFinalDiagnostic foundBinResultsFinalDiagnostic = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findById(binResultsFinalDiagnostic.getKey());
                            if (foundBinResultsFinalDiagnostic == null) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }

                    List<BinResultsFinalDiagnostic> clinvarKnownPathogenic = BinResultsFinalDiagnosticFactory
                            .findClinVarKnownPathogenic(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(clinvarKnownPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : clinvarKnownPathogenic) {
                            BinResultsFinalDiagnostic foundBinResultsFinalDiagnostic = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findById(binResultsFinalDiagnostic.getKey());
                            if (foundBinResultsFinalDiagnostic == null) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    List<BinResultsFinalDiagnostic> hgmdLikelyPathogenic = BinResultsFinalDiagnosticFactory
                            .findHGMDLikelyPathogenic(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(hgmdLikelyPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : hgmdLikelyPathogenic) {
                            logger.info(binResultsFinalDiagnostic.getKey().toString());
                            BinResultsFinalDiagnostic foundBinResultsFinalDiagnostic = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findById(binResultsFinalDiagnostic.getKey());
                            if (foundBinResultsFinalDiagnostic == null) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }

                    List<BinResultsFinalDiagnostic> clinvarLikelyPathogenic = BinResultsFinalDiagnosticFactory
                            .findHGMDLikelyPathogenic(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(clinvarLikelyPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : clinvarLikelyPathogenic) {
                            logger.info(binResultsFinalDiagnostic.getKey().toString());
                            BinResultsFinalDiagnostic foundBinResultsFinalDiagnostic = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findById(binResultsFinalDiagnostic.getKey());
                            if (foundBinResultsFinalDiagnostic == null) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    List<BinResultsFinalDiagnostic> possiblyPathogenic = BinResultsFinalDiagnosticFactory
                            .findHGMDPossiblyPathogenic(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(possiblyPathogenic)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : possiblyPathogenic) {
                            BinResultsFinalDiagnostic foundBinResultsFinalDiagnostic = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findById(binResultsFinalDiagnostic.getKey());
                            if (foundBinResultsFinalDiagnostic == null) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    List<BinResultsFinalDiagnostic> uncertainSignificance = BinResultsFinalDiagnosticFactory
                            .findHGMDUncertainSignificance(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(uncertainSignificance)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : uncertainSignificance) {
                            BinResultsFinalDiagnostic foundBinResultsFinalDiagnostic = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findById(binResultsFinalDiagnostic.getKey());
                            if (foundBinResultsFinalDiagnostic == null) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    List<BinResultsFinalDiagnostic> likelyBenign = BinResultsFinalDiagnosticFactory.findHGMDLikelyBenign(daoBean,
                            binningJob, variants);
                    if (CollectionUtils.isNotEmpty(likelyBenign)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : likelyBenign) {
                            BinResultsFinalDiagnostic foundBinResultsFinalDiagnostic = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findById(binResultsFinalDiagnostic.getKey());
                            if (foundBinResultsFinalDiagnostic == null) {
                                logger.info("saving BinResultsFinalDiagnostic: {}", binResultsFinalDiagnostic.toString());
                                daoBean.getBinResultsFinalDiagnosticDAO().save(binResultsFinalDiagnostic);
                            }
                        }
                    }
                } catch (BinningDAOException e) {
                    logger.error(e.getMessage(), e);
                }

                try {
                    List<BinResultsFinalDiagnostic> almostCertainlyBenign = BinResultsFinalDiagnosticFactory
                            .findHGMDAlmostCertainlyBenign(daoBean, binningJob, variants);
                    if (CollectionUtils.isNotEmpty(almostCertainlyBenign)) {
                        for (BinResultsFinalDiagnostic binResultsFinalDiagnostic : almostCertainlyBenign) {
                            BinResultsFinalDiagnostic foundBinResultsFinalDiagnostic = daoBean.getBinResultsFinalDiagnosticDAO()
                                    .findById(binResultsFinalDiagnostic.getKey());
                            if (foundBinResultsFinalDiagnostic == null) {
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
