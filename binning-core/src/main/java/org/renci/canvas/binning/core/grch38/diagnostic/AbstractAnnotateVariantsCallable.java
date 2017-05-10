package org.renci.canvas.binning.core.grch38.diagnostic;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.binning.core.grch38.VariantsFactory;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.canvas.dao.ref.model.GenomeRef;
import org.renci.canvas.dao.refseq.model.LocationType;
import org.renci.canvas.dao.refseq.model.TranscriptMaps;
import org.renci.canvas.dao.refseq.model.TranscriptMapsExons;
import org.renci.canvas.dao.refseq.model.VariantEffect;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAnnotateVariantsCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAnnotateVariantsCallable.class);

    private static final VariantsFactory variantsFactory = VariantsFactory.getInstance();

    private CANVASDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public AbstractAnnotateVariantsCallable(CANVASDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public Void call() throws BinningException {
        logger.debug("ENTERING run()");

        try {

            DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO().findById(binningJob.getListVersion());
            logger.info(diagnosticResultVersion.toString());

            String refseqVersion = diagnosticResultVersion.getRefseqVersion().toString();

            GenomeRef genomeRef = diagnosticResultVersion.getGenomeRef();
            logger.info(genomeRef.toString());

            List<LocationType> allLocationTypes = daoBean.getLocationTypeDAO().findAll();
            List<VariantEffect> allVariantEffects = daoBean.getVariantEffectDAO().findAll();

            List<LocatedVariant> locatedVariantList = daoBean.getLocatedVariantDAO().findByAssemblyId(binningJob.getAssembly().getId());

            if (CollectionUtils.isNotEmpty(locatedVariantList)) {
                logger.info(String.format("locatedVariantList.size(): %d", locatedVariantList.size()));

                locatedVariantList.sort((a, b) -> {
                    int ret = a.getGenomeRefSeq().getId().compareTo(b.getGenomeRefSeq().getId());
                    if (ret == 0) {
                        ret = a.getPosition().compareTo(b.getPosition());
                    }
                    return ret;
                });

                for (LocatedVariant locatedVariant : locatedVariantList) {
                    daoBean.getVariants_80_4_DAO().deleteByLocatedVariantId(locatedVariant.getId());
                }
                
                ExecutorService es = Executors.newFixedThreadPool(6);

                for (LocatedVariant locatedVariant : locatedVariantList) {

                    es.submit(() -> {

                        logger.info(locatedVariant.toString());

                        try {
                            final List<TranscriptMaps> transcriptMapsList = daoBean.getTranscriptMapsDAO()
                                    .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRef.getId(),
                                            refseqVersion, locatedVariant.getGenomeRefSeq().getId(), locatedVariant.getPosition());

                            if (CollectionUtils.isNotEmpty(transcriptMapsList)) {

                                List<TranscriptMaps> distinctTranscriptMapsList = transcriptMapsList.stream()
                                        .map(a -> a.getTranscript().getId())
                                        .distinct().map(a -> transcriptMapsList.parallelStream()
                                                .filter(b -> b.getTranscript().getId().equals(a)).findAny().get())
                                        .collect(Collectors.toList());

                                distinctTranscriptMapsList.sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                                // handling non boundary crossing variants (intron/exon/utr*)
                                logger.info("distinctTranscriptMapsList.size(): {}", distinctTranscriptMapsList.size());

                                for (TranscriptMaps tMap : distinctTranscriptMapsList) {

                                    logger.info(tMap.toString());

                                    List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                            .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRef.getId(), refseqVersion,
                                                    tMap.getTranscript().getId());

                                    List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                            .findByTranscriptMapsId(tMap.getId());

                                    Variants_80_4 variant = null;

                                    Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.parallelStream()
                                            .filter(a -> a.getContigRange().contains(locatedVariant.getPosition())).findAny();
                                    if (optionalTranscriptMapsExons.isPresent()) {
                                        TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                                        logger.debug(transcriptMapsExons.toString());

                                        if ((transcriptMapsExons.getContigEnd().equals(locatedVariant.getPosition())
                                                && "-".equals(tMap.getStrand()))
                                                || (transcriptMapsExons.getContigStart().equals(locatedVariant.getPosition())
                                                        && "+".equals(tMap.getStrand()))) {
                                            variant = variantsFactory.createBorderCrossingVariant(daoBean, locatedVariant, tMap, mapsList,
                                                    transcriptMapsExonsList, transcriptMapsExons, allLocationTypes, allVariantEffects);
                                        } else {
                                            variant = variantsFactory.createExonicVariant(daoBean, locatedVariant, mapsList,
                                                    transcriptMapsExonsList, transcriptMapsExons, allLocationTypes, allVariantEffects);
                                        }

                                    } else {
                                        variant = variantsFactory.createIntronicVariant(daoBean, locatedVariant, mapsList, tMap,
                                                transcriptMapsExonsList, allLocationTypes, allVariantEffects);
                                    }
                                    Variants_80_4 foundVariant = daoBean.getVariants_80_4_DAO().findById(variant.getId());
                                    if (foundVariant == null) {
                                        daoBean.getVariants_80_4_DAO().save(variant);
                                    }

                                }

                            } else {

                                // try searching by adjusting for length of locatedVariant.getSeq()...could be intron/exon
                                // boundary crossing

                                final List<TranscriptMaps> boundaryCrossingRightTranscriptMapsList = daoBean.getTranscriptMapsDAO()
                                        .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRef.getId(),
                                                refseqVersion, locatedVariant.getGenomeRefSeq().getId(),
                                                locatedVariant.getPosition() + locatedVariant.getRef().length() - 1);

                                if (CollectionUtils.isNotEmpty(boundaryCrossingRightTranscriptMapsList)) {

                                    List<TranscriptMaps> distinctBoundaryCrossingTranscriptMapsList = boundaryCrossingRightTranscriptMapsList
                                            .stream().map(a -> a.getTranscript().getId()).distinct()
                                            .map(a -> boundaryCrossingRightTranscriptMapsList.parallelStream()
                                                    .filter(b -> b.getTranscript().getId().equals(a)).findAny().get())
                                            .collect(Collectors.toList());

                                    distinctBoundaryCrossingTranscriptMapsList
                                            .sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                                    for (TranscriptMaps tMap : distinctBoundaryCrossingTranscriptMapsList) {
                                        logger.info(tMap.toString());

                                        List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                                .findByTranscriptMapsId(tMap.getId());

                                        List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                                .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRef.getId(), refseqVersion,
                                                        tMap.getTranscript().getId());

                                        Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList.parallelStream()
                                                .filter(a -> a.getContigRange()
                                                        .contains(locatedVariant.getPosition() + locatedVariant.getRef().length() - 1))
                                                .findAny();

                                        Variants_80_4 variant = null;

                                        if (optionalTranscriptMapsExons.isPresent()) {
                                            // we have a border crossing variant starting in an exon
                                            TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                                            logger.debug(transcriptMapsExons.toString());
                                            variant = variantsFactory.createBorderCrossingVariant(daoBean, locatedVariant, tMap, mapsList,
                                                    transcriptMapsExonsList, transcriptMapsExons, allLocationTypes, allVariantEffects);
                                        } else {
                                            // we have a border crossing variant starting in an intron
                                            variant = variantsFactory.createBorderCrossingVariant(daoBean, locatedVariant, tMap, mapsList,
                                                    transcriptMapsExonsList, null, allLocationTypes, allVariantEffects);
                                        }
                                        Variants_80_4 foundVariant = daoBean.getVariants_80_4_DAO().findById(variant.getId());
                                        if (foundVariant == null) {
                                            daoBean.getVariants_80_4_DAO().save(variant);
                                        }

                                    }

                                }

                                if (CollectionUtils.isEmpty(boundaryCrossingRightTranscriptMapsList)) {

                                    final List<TranscriptMaps> boundaryCrossingLeftTranscriptMapsList = daoBean.getTranscriptMapsDAO()
                                            .findByGenomeRefIdAndRefSeqVersionAndGenomeRefSeqAccessionAndInExonRange(genomeRef.getId(),
                                                    refseqVersion, locatedVariant.getGenomeRefSeq().getId(),
                                                    locatedVariant.getPosition() - locatedVariant.getRef().length());

                                    if (CollectionUtils.isNotEmpty(boundaryCrossingLeftTranscriptMapsList)) {

                                        List<TranscriptMaps> distinctBoundaryCrossingTranscriptMapsList = boundaryCrossingLeftTranscriptMapsList
                                                .stream().map(a -> a.getTranscript().getId()).distinct()
                                                .map(a -> boundaryCrossingLeftTranscriptMapsList.parallelStream()
                                                        .filter(b -> b.getTranscript().getId().equals(a)).findAny().get())
                                                .collect(Collectors.toList());

                                        distinctBoundaryCrossingTranscriptMapsList
                                                .sort((a, b) -> b.getTranscript().getId().compareTo(a.getTranscript().getId()));

                                        for (TranscriptMaps tMap : distinctBoundaryCrossingTranscriptMapsList) {

                                            List<TranscriptMapsExons> transcriptMapsExonsList = daoBean.getTranscriptMapsExonsDAO()
                                                    .findByTranscriptMapsId(tMap.getId());

                                            List<TranscriptMaps> mapsList = daoBean.getTranscriptMapsDAO()
                                                    .findByGenomeRefIdAndRefSeqVersionAndTranscriptId(genomeRef.getId(), refseqVersion,
                                                            tMap.getTranscript().getId());

                                            Optional<TranscriptMapsExons> optionalTranscriptMapsExons = transcriptMapsExonsList
                                                    .parallelStream()
                                                    .filter(a -> a.getContigRange()
                                                            .contains(locatedVariant.getPosition() - locatedVariant.getRef().length()))
                                                    .findAny();
                                            Variants_80_4 variant = null;
                                            if (optionalTranscriptMapsExons.isPresent()) {
                                                TranscriptMapsExons transcriptMapsExons = optionalTranscriptMapsExons.get();
                                                logger.debug(transcriptMapsExons.toString());
                                                variant = variantsFactory.createBorderCrossingVariant(daoBean, locatedVariant, tMap,
                                                        mapsList, transcriptMapsExonsList, transcriptMapsExons, allLocationTypes,
                                                        allVariantEffects);
                                            } else {
                                                // we have a border crossing variant starting in an intron
                                                variant = variantsFactory.createBorderCrossingVariant(daoBean, locatedVariant, tMap,
                                                        mapsList, transcriptMapsExonsList, null, allLocationTypes, allVariantEffects);
                                            }
                                            Variants_80_4 foundVariant = daoBean.getVariants_80_4_DAO().findById(variant.getId());
                                            if (foundVariant == null) {
                                                daoBean.getVariants_80_4_DAO().save(variant);
                                            }
                                        }
                                    }

                                }

                            }
                        } catch (CANVASDAOException | BinningException e) {
                            logger.error(e.getMessage(), e);
                        }

                    });

                }

                es.shutdown();
                if (!es.awaitTermination(1L, TimeUnit.DAYS)) {
                    es.shutdownNow();
                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return null;
    }

    public CANVASDAOBeanService getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(CANVASDAOBeanService daoBean) {
        this.daoBean = daoBean;
    }

    public DiagnosticBinningJob getBinningJob() {
        return binningJob;
    }

    public void setBinningJob(DiagnosticBinningJob binningJob) {
        this.binningJob = binningJob;
    }

}
