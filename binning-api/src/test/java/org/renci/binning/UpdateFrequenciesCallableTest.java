package org.renci.binning;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.renci.binning.BinningException;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.MaxFrequency;
import org.renci.binning.dao.clinbin.model.MaxFrequencyPK;
import org.renci.binning.dao.genome1k.model.IndelMaxFrequency;
import org.renci.binning.dao.genome1k.model.SNPPopulationMaxFrequency;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateFrequenciesCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(UpdateFrequenciesCallableTest.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Test
    public void test() throws BinningDAOException, BinningException, IOException {

        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO().findByAssemblyId(35619);

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {

            List<MaxFrequency> results = new ArrayList<>();

            for (LocatedVariant locatedVariant : locatedVariantList) {
                logger.debug(locatedVariant.toString());

                List<SNPPopulationMaxFrequency> snpPopulationMaxFrequencyList = daoMgr.getDAOBean().getSNPPopulationMaxFrequencyDAO()
                        .findByLocatedVariantIdAndVersion(locatedVariant.getId(), 2);

                if (CollectionUtils.isNotEmpty(snpPopulationMaxFrequencyList)) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 2);
                    MaxFrequency maxFrequency = daoMgr.getDAOBean().getMaxFrequencyDAO().findById(key);
                    if (maxFrequency != null) {
                        // has already been created
                        continue;
                    }
                    maxFrequency = new MaxFrequency(key, daoMgr.getDAOBean().getMaxFrequencySourceDAO().findById("snp"));
                    maxFrequency.setMaxAlleleFreq(snpPopulationMaxFrequencyList.get(0).getMaxAlleleFrequency());
                    results.add(maxFrequency);
                    continue;
                }

                List<IndelMaxFrequency> indelMaxFrequencyList = daoMgr.getDAOBean().getIndelMaxFrequencyDAO()
                        .findByLocatedVariantIdAndVersion(locatedVariant.getId(), 1);

                if (CollectionUtils.isNotEmpty(indelMaxFrequencyList)) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 1);
                    MaxFrequency maxFrequency = daoMgr.getDAOBean().getMaxFrequencyDAO().findById(key);
                    if (maxFrequency != null) {
                        // has already been created
                        continue;
                    }
                    maxFrequency = new MaxFrequency(key, daoMgr.getDAOBean().getMaxFrequencySourceDAO().findById("indel"));
                    maxFrequency.setMaxAlleleFreq(indelMaxFrequencyList.get(0).getMaxAlleleFrequency());
                    results.add(maxFrequency);
                    continue;
                }

                if (CollectionUtils.isEmpty(snpPopulationMaxFrequencyList) && CollectionUtils.isEmpty(indelMaxFrequencyList)) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 0);
                    MaxFrequency maxFrequency = daoMgr.getDAOBean().getMaxFrequencyDAO().findById(key);
                    if (maxFrequency != null) {
                        // has already been created
                        continue;
                    }
                    maxFrequency = new MaxFrequency(key, daoMgr.getDAOBean().getMaxFrequencySourceDAO().findById("none"));
                    maxFrequency.setMaxAlleleFreq(0D);
                    results.add(maxFrequency);
                }

            }

            assertTrue(CollectionUtils.isEmpty(results));

            // if (CollectionUtils.isEmpty(results)) {
            // logger.info(String.format("attempting to save %d MaxFrequency instances", results.size()));
            // for (MaxFrequency maxFrequency : results) {
            // logger.info(maxFrequency.toString());
            // daoMgr.getDAOBean().getMaxFrequencyDAO().save(maxFrequency);
            // }
            // }

        }

    }

}
