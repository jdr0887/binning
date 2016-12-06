package org.renci.binning.diagnostic.gs.executor;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.renci.binning.BinningException;
import org.renci.binning.IRODSUtils;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.diagnostic.AbstractLoadVCFCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import htsjdk.samtools.liftover.LiftOver;
import htsjdk.samtools.util.Interval;

public class LoadVCFCallable extends AbstractLoadVCFCallable {

    private static final Logger logger = LoggerFactory.getLogger(LoadVCFCallable.class);

    private String binningDirectory;

    public LoadVCFCallable(BinningDAOBeanService daoBean, DiagnosticBinningJob binningJob, String binningDirectory) {
        super(daoBean, binningJob);
        this.binningDirectory = binningDirectory;
    }

    @Override
    public String getLabName() {
        return "GS";
    }

    @Override
    public String getLibraryName() {
        return "FreeBayes";
    }

    @Override
    public String getStudyName() {
        return "GS";
    }

    @Override
    public Set<String> getExcludesFilter() {
        logger.debug("ENTERING getExcludesFilter()");
        Set<String> excludesFilter = new HashSet<>();
        excludesFilter.add("LowQual");
        return excludesFilter;
    }

    @Override
    public LocatedVariant liftOver(LocatedVariant locatedVariant) throws BinningDAOException {
        logger.debug("ENTERING liftOver(LocatedVariant)");
        File chainFile = new File(binningDirectory, "hg19ToHg38.over.chain.gz");
        GenomeRef build38GenomeRef = getDaoBean().getGenomeRefDAO().findById(4);
        LiftOver liftOver = new LiftOver(chainFile);
        Interval interval = new Interval(String.format("chr", locatedVariant.getGenomeRefSeq().getContig()), locatedVariant.getPosition(),
                locatedVariant.getEndPosition());
        Interval loInterval = liftOver.liftOver(interval);
        LocatedVariant ret = new LocatedVariant(build38GenomeRef, locatedVariant.getGenomeRefSeq(), loInterval.getStart(),
                loInterval.getEnd(), locatedVariant.getVariantType(), locatedVariant.getRef(), locatedVariant.getSeq());
        return ret;
    }

    @Override
    public File getVCF(String participant) throws BinningException {
        logger.debug("ENTERING getVCF(String)");
        Map<String, String> avuMap = new HashMap<String, String>();
        avuMap.put("MaPSeqStudyName", "GS");
        avuMap.put("MaPSeqWorkflowName", "GSVariantCalling");
        avuMap.put("MaPSeqJobName", "GATKVariantAnnotator");
        avuMap.put("MaPSeqMimeType", "TEXT_VCF");
        String irodsFile = IRODSUtils.findFile(participant, avuMap);
        String participantDir = String.format("%s/annotation/GS/%s", binningDirectory, participant);
        File vcfFile = IRODSUtils.getFile(irodsFile, participantDir);
        return vcfFile;
    }

    public static void main(String[] args) {
        try {
            BinningDAOManager daoMgr = BinningDAOManager.getInstance();
            DiagnosticBinningJob binningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4218);
            LoadVCFCallable callable = new LoadVCFCallable(daoMgr.getDAOBean(), binningJob, "/tmp");
            callable.call();
        } catch (BinningDAOException | BinningException e) {
            e.printStackTrace();
        }
    }

}
