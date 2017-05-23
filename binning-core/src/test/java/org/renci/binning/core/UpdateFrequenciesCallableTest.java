package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.MaxFrequency;
import org.renci.canvas.dao.clinbin.model.MaxFrequencyPK;
import org.renci.canvas.dao.genome1k.model.IndelMaxFrequency;
import org.renci.canvas.dao.genome1k.model.IndelMaxFrequencyPK;
import org.renci.canvas.dao.genome1k.model.SNPPopulationMaxFrequency;
import org.renci.canvas.dao.genome1k.model.SNPPopulationMaxFrequencyPK;
import org.renci.canvas.dao.jpa.CANVASDAOManager;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateFrequenciesCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(UpdateFrequenciesCallableTest.class);

    private static final CANVASDAOManager daoMgr = CANVASDAOManager.getInstance();

    @Test
    public void test() throws CANVASDAOException, BinningException, IOException {

        List<LocatedVariant> locatedVariantList = daoMgr.getDAOBean().getLocatedVariantDAO().findByAssemblyId(35619);

        if (CollectionUtils.isNotEmpty(locatedVariantList)) {

            List<MaxFrequency> results = new ArrayList<>();

            for (LocatedVariant locatedVariant : locatedVariantList) {
                logger.debug(locatedVariant.toString());

                SNPPopulationMaxFrequency snpPopulationMaxFrequency = daoMgr.getDAOBean().getSNPPopulationMaxFrequencyDAO()
                        .findById(new SNPPopulationMaxFrequencyPK(locatedVariant.getId(), 2));

                if (snpPopulationMaxFrequency != null) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 2);
                    MaxFrequency maxFrequency = daoMgr.getDAOBean().getMaxFrequencyDAO().findById(key);
                    if (maxFrequency == null) {
                        maxFrequency = new MaxFrequency(key, daoMgr.getDAOBean().getMaxFrequencySourceDAO().findById("snp"));
                        maxFrequency.setMaxAlleleFreq(snpPopulationMaxFrequency.getMaxAlleleFrequency());
                        results.add(maxFrequency);
                    }
                    continue;
                }

                IndelMaxFrequency indelMaxFrequency = daoMgr.getDAOBean().getIndelMaxFrequencyDAO()
                        .findById(new IndelMaxFrequencyPK(locatedVariant.getId(), 1));

                if (indelMaxFrequency != null) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 1);
                    MaxFrequency maxFrequency = daoMgr.getDAOBean().getMaxFrequencyDAO().findById(key);
                    if (maxFrequency == null) {
                        maxFrequency = new MaxFrequency(key, daoMgr.getDAOBean().getMaxFrequencySourceDAO().findById("indel"));
                        maxFrequency.setMaxAlleleFreq(indelMaxFrequency.getMaxAlleleFrequency());
                        results.add(maxFrequency);
                    }
                    continue;
                }

                if (snpPopulationMaxFrequency == null && indelMaxFrequency == null) {
                    MaxFrequencyPK key = new MaxFrequencyPK(locatedVariant.getId(), 0);
                    MaxFrequency maxFrequency = daoMgr.getDAOBean().getMaxFrequencyDAO().findById(key);
                    if (maxFrequency == null) {
                        maxFrequency = new MaxFrequency(key, daoMgr.getDAOBean().getMaxFrequencySourceDAO().findById("none"));
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
            // daoMgr.getDAOBean().getMaxFrequencyDAO().save(maxFrequency);
            // }
            // }

        }

    }

}
