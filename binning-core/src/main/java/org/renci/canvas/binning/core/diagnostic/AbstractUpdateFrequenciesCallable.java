package org.renci.canvas.binning.core.diagnostic;

import java.util.ArrayList;
import java.util.List;
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
import org.renci.canvas.dao.genome1k.model.SNPPopulationMaxFrequency;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUpdateFrequenciesCallable implements Callable<List<MaxFrequency>> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUpdateFrequenciesCallable.class);

    private CANVASDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public AbstractUpdateFrequenciesCallable(CANVASDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public List<MaxFrequency> call() throws BinningException {
        logger.debug("ENTERING call()");
        List<MaxFrequency> results = new ArrayList<>();

        try {

            DiagnosticResultVersion diagnosticResultVersion = daoBean.getDiagnosticResultVersionDAO().findById(binningJob.getListVersion());
            logger.info(diagnosticResultVersion.toString());

            MaxFrequencySource snpMaxFrequencySource = daoBean.getMaxFrequencySourceDAO().findById("snp");
            MaxFrequencySource indelMaxFrequencySource = daoBean.getMaxFrequencySourceDAO().findById("indel");
            MaxFrequencySource noneMaxFrequencySource = daoBean.getMaxFrequencySourceDAO().findById("none");

            List<LocatedVariant> locatedVariantList = daoBean.getLocatedVariantDAO().findByAssemblyId(binningJob.getAssembly().getId());

            if (CollectionUtils.isNotEmpty(locatedVariantList)) {
                logger.info(String.format("locatedVariantList.size(): %d", locatedVariantList.size()));

                ExecutorService es = Executors.newFixedThreadPool(2);

                for (LocatedVariant locatedVariant : locatedVariantList) {
                    logger.info(locatedVariant.toString());

                    es.submit(() -> {

                        try {
                            List<SNPPopulationMaxFrequency> snpPopulationMaxFrequencyList = daoBean.getSNPPopulationMaxFrequencyDAO()
                                    .findByLocatedVariantIdAndVersion(locatedVariant.getId(),
                                            diagnosticResultVersion.getGen1000SnpVersion());

                            if (CollectionUtils.isNotEmpty(snpPopulationMaxFrequencyList)) {

                                MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(),
                                        diagnosticResultVersion.getGen1000SnpVersion());
                                MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                                if (maxFrequency != null) {
                                    // has already been created
                                    return;
                                }
                                maxFrequency = new MaxFrequency(key, snpMaxFrequencySource);
                                maxFrequency.setMaxAlleleFreq(snpPopulationMaxFrequencyList.get(0).getMaxAlleleFrequency());
                                maxFrequency.setLocatedVariant(locatedVariant);
                                results.add(maxFrequency);
                                return;
                            }

                            List<IndelMaxFrequency> indelMaxFrequencyList = daoBean.getIndelMaxFrequencyDAO()
                                    .findByLocatedVariantIdAndVersion(locatedVariant.getId(),
                                            diagnosticResultVersion.getGen1000IndelVersion());

                            if (CollectionUtils.isNotEmpty(indelMaxFrequencyList)) {
                                MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(),
                                        diagnosticResultVersion.getGen1000IndelVersion());
                                MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                                if (maxFrequency != null) {
                                    // has already been created
                                    return;
                                }
                                maxFrequency = new MaxFrequency(key, indelMaxFrequencySource);
                                maxFrequency.setMaxAlleleFreq(indelMaxFrequencyList.get(0).getMaxAlleleFrequency());
                                maxFrequency.setLocatedVariant(locatedVariant);
                                results.add(maxFrequency);
                                return;
                            }

                            if (CollectionUtils.isEmpty(snpPopulationMaxFrequencyList) && CollectionUtils.isEmpty(indelMaxFrequencyList)) {
                                MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 0);
                                MaxFrequency maxFrequency = daoBean.getMaxFrequencyDAO().findById(key);
                                if (maxFrequency != null) {
                                    // has already been created
                                    return;
                                }
                                maxFrequency = new MaxFrequency(key, noneMaxFrequencySource);
                                maxFrequency.setMaxAlleleFreq(0D);
                                maxFrequency.setLocatedVariant(locatedVariant);
                                results.add(maxFrequency);
                            }
                        } catch (CANVASDAOException e) {
                            e.printStackTrace();
                        }

                    });

                    es.shutdown();
                    es.awaitTermination(1L, TimeUnit.HOURS);
                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return results;
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
