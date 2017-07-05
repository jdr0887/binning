package org.renci.canvas.binning.core.diagnostic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.canvas.dao.clinbin.model.MaxFrequency;
import org.renci.canvas.dao.clinbin.model.MaxFrequencyPK;
import org.renci.canvas.dao.clinbin.model.MaxFrequencySource;
import org.renci.canvas.dao.genome1k.model.IndelMaxFrequency;
import org.renci.canvas.dao.genome1k.model.IndelMaxFrequencyPK;
import org.renci.canvas.dao.genome1k.model.SNPPopulationMaxFrequency;
import org.renci.canvas.dao.genome1k.model.SNPPopulationMaxFrequencyPK;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUpdateFrequenciesCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUpdateFrequenciesCallable.class);

    private CANVASDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public AbstractUpdateFrequenciesCallable(CANVASDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public Void call() throws BinningException {
        logger.debug("ENTERING call()");
        try {

            logger.info(binningJob.toString());

            DiagnosticResultVersion diagnosticResultVersion = binningJob.getDiagnosticResultVersion();
            logger.info(diagnosticResultVersion.toString());

            MaxFrequencySource snpMaxFrequencySource = daoBean.getMaxFrequencySourceDAO().findById("snp");
            MaxFrequencySource indelMaxFrequencySource = daoBean.getMaxFrequencySourceDAO().findById("indel");
            MaxFrequencySource noneMaxFrequencySource = daoBean.getMaxFrequencySourceDAO().findById("none");

            List<LocatedVariant> locatedVariantList = daoBean.getLocatedVariantDAO().findByAssemblyId(binningJob.getAssembly().getId());

            Set<MaxFrequency> results = new HashSet<>();

            if (CollectionUtils.isNotEmpty(locatedVariantList)) {
                logger.info(String.format("locatedVariantList.size(): %d", locatedVariantList.size()));

                ExecutorService es = Executors.newFixedThreadPool(4);

                for (LocatedVariant locatedVariant : locatedVariantList) {
                    logger.info(locatedVariant.toString());

                    es.submit(() -> {

                        try {
                            SNPPopulationMaxFrequency snpPopulationMaxFrequency = daoBean.getSNPPopulationMaxFrequencyDAO()
                                    .findById(new SNPPopulationMaxFrequencyPK(locatedVariant.getId(),
                                            diagnosticResultVersion.getGen1000SnpVersion()));

                            if (snpPopulationMaxFrequency != null) {
                                MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(),
                                        diagnosticResultVersion.getGen1000SnpVersion().toString());
                                MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                                if (maxFrequency == null) {
                                    maxFrequency = new MaxFrequency(key, snpMaxFrequencySource);
                                    maxFrequency.setMaxAlleleFreq(snpPopulationMaxFrequency.getMaxAlleleFrequency());
                                    maxFrequency.setLocatedVariant(locatedVariant);
                                    results.add(maxFrequency);
                                }
                                return;
                            }

                            IndelMaxFrequency indelMaxFrequency = daoBean.getIndelMaxFrequencyDAO().findById(
                                    new IndelMaxFrequencyPK(locatedVariant.getId(), diagnosticResultVersion.getGen1000IndelVersion()));

                            if (indelMaxFrequency != null) {
                                MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(),
                                        diagnosticResultVersion.getGen1000IndelVersion().toString());
                                MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                                if (maxFrequency == null) {
                                    maxFrequency = new MaxFrequency(key, indelMaxFrequencySource);
                                    maxFrequency.setMaxAlleleFreq(indelMaxFrequency.getMaxAlleleFrequency());
                                    maxFrequency.setLocatedVariant(locatedVariant);
                                    results.add(maxFrequency);
                                }
                                return;
                            }

                            if (snpPopulationMaxFrequency == null && indelMaxFrequency == null) {
                                MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), "0");
                                MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                                if (maxFrequency != null) {
                                    maxFrequency = new MaxFrequency(key, noneMaxFrequencySource);
                                    maxFrequency.setMaxAlleleFreq(0D);
                                    maxFrequency.setLocatedVariant(locatedVariant);
                                    results.add(maxFrequency);
                                }
                            }

                        } catch (CANVASDAOException e) {
                            logger.error(e.getMessage(), e);
                        }

                    });

                }
                es.shutdown();
                if (!es.awaitTermination(1L, TimeUnit.HOURS)) {
                    es.shutdownNow();
                }

            }

            for (MaxFrequency maxFrequency : results) {
                logger.info(maxFrequency.toString());
                daoBean.getMaxFrequencyDAO().save(maxFrequency);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return null;
    }

    public DiagnosticBinningJob getBinningJob() {
        return binningJob;
    }

    public void setBinningJob(DiagnosticBinningJob binningJob) {
        this.binningJob = binningJob;
    }

    public CANVASDAOBeanService getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(CANVASDAOBeanService daoBean) {
        this.daoBean = daoBean;
    }

}
