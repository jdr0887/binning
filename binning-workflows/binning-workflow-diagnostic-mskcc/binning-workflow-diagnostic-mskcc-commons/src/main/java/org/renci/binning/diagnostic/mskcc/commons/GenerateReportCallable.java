package org.renci.binning.diagnostic.mskcc.commons;

import java.util.concurrent.Executors;

import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.diagnostic.AbstractGenerateReportCallable;

public class GenerateReportCallable extends AbstractGenerateReportCallable {

    public GenerateReportCallable(BinningDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super(daoBean, binningJob);
    }

    public static void main(String[] args) {
        try {
            BinningDAOManager daoMgr = BinningDAOManager.getInstance();
            DiagnosticBinningJob binningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4218);
            GenerateReportCallable runnable = new GenerateReportCallable(daoMgr.getDAOBean(), binningJob);
            Executors.newSingleThreadExecutor().submit(runnable);
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
    }
}
