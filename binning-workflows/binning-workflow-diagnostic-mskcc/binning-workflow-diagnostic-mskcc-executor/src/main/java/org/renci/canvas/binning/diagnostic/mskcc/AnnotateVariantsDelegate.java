package org.renci.canvas.binning.diagnostic.mskcc;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.collections.CollectionUtils;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotateVariantsDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(AnnotateVariantsDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.debug("ENTERING execute(DelegateExecution)");

        Map<String, Object> variables = execution.getVariables();

        BinningDAOBeanService daoBean = null;
        Object o = variables.get("daoBean");
        if (o != null && o instanceof BinningDAOBeanService) {
            daoBean = (BinningDAOBeanService) o;
        }

        DiagnosticBinningJob binningJob = null;
        o = variables.get("job");
        if (o != null && o instanceof DiagnosticBinningJob) {
            binningJob = (DiagnosticBinningJob) o;
        }
        logger.info(binningJob.toString());

        try {
            binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Annotating variants"));
            daoBean.getDiagnosticBinningJobDAO().save(binningJob);

            List<Variants_61_2> variants = Executors.newSingleThreadExecutor().submit(new AnnotateVariantsCallable(daoBean, binningJob))
                    .get();
            if (CollectionUtils.isNotEmpty(variants)) {
                logger.info(String.format("saving %d Variants_61_2 instances", variants.size()));
                for (Variants_61_2 variant : variants) {
                    logger.info(variant.toString());
                    daoBean.getVariants_61_2_DAO().save(variant);
                }
            }

            binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Annotated variants"));
            daoBean.getDiagnosticBinningJobDAO().save(binningJob);

        } catch (Exception e) {
            try {
                binningJob.setStop(new Date());
                binningJob.setFailureMessage(e.getMessage());
                binningJob.setStatus(daoBean.getDiagnosticStatusTypeDAO().findById("Failed"));
                daoBean.getDiagnosticBinningJobDAO().save(binningJob);
            } catch (BinningDAOException e1) {
                e1.printStackTrace();
            }
        }

    }

}
