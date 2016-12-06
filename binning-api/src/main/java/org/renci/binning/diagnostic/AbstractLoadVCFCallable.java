package org.renci.binning.diagnostic;

import java.io.File;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.renci.binning.BinningException;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.ref.model.GenomeRefSeqLocation;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantPK;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQCPK;
import org.renci.binning.dao.var.model.Lab;
import org.renci.binning.dao.var.model.Library;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.Project;
import org.renci.binning.dao.var.model.Sample;
import org.renci.binning.dao.var.model.VariantSet;
import org.renci.binning.dao.var.model.VariantSetLoad;
import org.renci.binning.dao.var.model.VariantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.CommonInfo;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;

public abstract class AbstractLoadVCFCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLoadVCFCallable.class);

    private BinningDAOBeanService daoBean;

    private DiagnosticBinningJob binningJob;

    public abstract File getVCF(String participant) throws BinningException;

    public abstract Set<String> getExcludesFilter();

    public abstract String getLabName();

    public abstract String getLibraryName();

    public abstract String getStudyName();

    public abstract LocatedVariant liftOver(LocatedVariant locatedVariant) throws BinningDAOException;

    public AbstractLoadVCFCallable(BinningDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public Void call() throws BinningException {
        logger.debug("ENTERING run()");

        final Map<String, BigInteger> countMap = new HashMap<String, BigInteger>();
        countMap.put("subCount", BigInteger.valueOf(0));
        countMap.put("snpCount", BigInteger.valueOf(0));
        countMap.put("delCount", BigInteger.valueOf(0));
        countMap.put("insCount", BigInteger.valueOf(0));
        countMap.put("multiCount", BigInteger.valueOf(0));
        countMap.put("refSkippedCount", BigInteger.valueOf(0));
        countMap.put("filterSkippedCount", BigInteger.valueOf(0));
        countMap.put("errorCount", BigInteger.valueOf(0));
        countMap.put("noCallCount", BigInteger.valueOf(0));

        try {

            Date startDate = new Date();

            VariantType snpVariantType = daoBean.getVariantTypeDAO().findById("snp");
            VariantType delVariantType = daoBean.getVariantTypeDAO().findById("del");
            VariantType insVariantType = daoBean.getVariantTypeDAO().findById("ins");
            VariantType subVariantType = daoBean.getVariantTypeDAO().findById("sub");

            File vcfFile = null;
            if (StringUtils.isEmpty(binningJob.getVcfFile())) {
                vcfFile = getVCF(binningJob.getParticipant());
                binningJob.setVcfFile(vcfFile.getAbsolutePath());
                daoBean.getDiagnosticBinningJobDAO().save(binningJob);
            } else {
                vcfFile = new File(binningJob.getVcfFile());
            }

            GenomeRef build37GenomeRef = daoBean.getGenomeRefDAO().findById(2);

            try (final VCFFileReader vcfFileReader = new VCFFileReader(vcfFile, false)) {

                VCFHeader vcfHeader = vcfFileReader.getFileHeader();
                List<String> sampleNames = vcfHeader.getGenotypeSamples();

                Set<String> excludesFilter = getExcludesFilter();

                String labName = getLabName();
                String libraryName = getLibraryName();
                String studyName = getStudyName();

                if (!sampleNames.contains(binningJob.getParticipant())) {
                    throw new BinningException("sampleNames does not contain participant");
                }

                Lab lab = null;
                List<Lab> foundLabs = daoBean.getLabDAO().findByName(labName);
                if (CollectionUtils.isEmpty(foundLabs)) {
                    lab = new Lab(labName);
                    daoBean.getLabDAO().save(lab);
                } else {
                    lab = foundLabs.get(0);
                }
                logger.info(lab.toString());

                Project project = daoBean.getProjectDAO().findById(studyName);
                if (project == null) {
                    project = new Project(studyName);
                    project.setLab(lab);
                    daoBean.getProjectDAO().save(project);
                }
                logger.info(project.toString());

                Sample sample = null;
                List<Sample> foundSamples = daoBean.getSampleDAO().findByNameAndProjectName(binningJob.getParticipant(), project.getName());
                if (CollectionUtils.isEmpty(foundSamples)) {
                    sample = new Sample(binningJob.getParticipant());
                    sample.setProject(project);
                    sample.setId(daoBean.getSampleDAO().save(sample));
                } else {
                    sample = foundSamples.get(0);
                }
                logger.info(sample.toString());

                // library
                Library library = null;
                List<Library> foundLibraries = daoBean.getLibraryDAO().findByNameAndSampleId(libraryName, sample.getId());
                if (CollectionUtils.isEmpty(foundLibraries)) {
                    library = new Library(libraryName);
                    library.setSample(sample);
                    library.setId(daoBean.getLibraryDAO().save(library));
                } else {
                    library = foundLibraries.get(0);
                }
                logger.info(library.toString());

                Assembly assembly = null;

                List<Assembly> foundAssemblies = daoBean.getAssemblyDAO().findByLibraryId(library.getId());
                if (CollectionUtils.isEmpty(foundAssemblies)) {

                    VariantSet variantSet = new VariantSet();
                    variantSet.setGenomeRef(build37GenomeRef);
                    variantSet.setId(daoBean.getVariantSetDAO().save(variantSet));

                    assembly = new Assembly();
                    assembly.setLibrary(library);
                    assembly.setVariantSet(variantSet);
                    assembly.setId(daoBean.getAssemblyDAO().save(assembly));

                    binningJob.setAssembly(assembly);
                    daoBean.getDiagnosticBinningJobDAO().save(binningJob);

                } else {
                    assembly = binningJob.getAssembly();
                    // delete asm loc var & qc instances
                    logger.info("deleting AssemblyLocatedVariant instances");
                    daoBean.getAssemblyLocatedVariantDAO().deleteByAssemblyId(binningJob.getAssembly().getId());
                    logger.info("deleting AssemblyLocatedVariantQC instances");
                    daoBean.getAssemblyLocatedVariantQCDAO().deleteByAssemblyId(binningJob.getAssembly().getId());
                }
                logger.info(assembly.toString());

                final Integer assemblyId = assembly.getId();

                ExecutorService es = Executors.newFixedThreadPool(4);

                for (VariantContext variantContext : vcfFileReader) {

                    Allele refAllele = variantContext.getReference();

                    if (refAllele.isNoCall()) {
                        synchronized (countMap) {
                            countMap.put("noCallCount", countMap.get("noCallCount").add(BigInteger.ONE));
                        }
                        continue;
                    }

                    String altAlleles = StringUtils.join(variantContext.getAlternateAlleles().toArray());
                    if (!altAlleles.matches("[AaCcGgTt,]*")) {
                        synchronized (countMap) {
                            countMap.put("refSkippedCount", countMap.get("refSkippedCount").add(BigInteger.ONE));
                        }
                        continue;
                    }

                    if (CollectionUtils.containsAny(variantContext.getFilters(), excludesFilter)) {
                        synchronized (countMap) {
                            countMap.put("filterSkippedCount", countMap.get("filterSkippedCount").add(BigInteger.ONE));
                        }
                        continue;
                    }
                    CommonInfo commonInfo = variantContext.getCommonInfo();

                    Double qualityByDepth = commonInfo.hasAttribute("QD") ? Double.valueOf(commonInfo.getAttribute("QD").toString()) : null;

                    Double readPosRankSum = commonInfo.hasAttribute("ReadPosRankSum")
                            ? Double.valueOf(commonInfo.getAttribute("ReadPosRankSum").toString()) : null;

                    Integer homopolymerRun = commonInfo.hasAttribute("HRun") ? Integer.valueOf(commonInfo.getAttribute("HRun").toString())
                            : null;

                    Double dels = commonInfo.hasAttribute("Dels") ? Double.valueOf(commonInfo.getAttribute("Dels").toString()) : null;

                    Double fs = commonInfo.hasAttribute("FS") ? Double.valueOf(commonInfo.getAttribute("FS").toString()) : null;

                    GenotypesContext genotypesContext = variantContext.getGenotypes(binningJob.getParticipant());

                    es.submit(() -> {

                        for (Genotype genotype : genotypesContext) {

                            if (genotype.isNoCall()) {
                                synchronized (countMap) {
                                    countMap.put("noCallCount", countMap.get("noCallCount").add(BigInteger.ONE));
                                }
                                continue;
                            }

                            if (genotype.isHomRef()) {
                                synchronized (countMap) {
                                    countMap.put("refSkippedCount", countMap.get("refSkippedCount").add(BigInteger.ONE));
                                }
                                continue;
                            }

                            try {
                                List<GenomeRefSeq> foundGenomeRefSeqs = null;
                                if (variantContext.getContig().length() < 3 && !variantContext.getContig().startsWith("NC_")) {
                                    foundGenomeRefSeqs = daoBean.getGenomeRefSeqDAO().findByRefIdAndContigAndSeqType(
                                            build37GenomeRef.getId(), variantContext.getContig(), "Chromosome");
                                } else {
                                    foundGenomeRefSeqs = daoBean.getGenomeRefSeqDAO().findByVersionedAccession(variantContext.getContig());
                                }

                                if (CollectionUtils.isEmpty(foundGenomeRefSeqs)) {
                                    logger.warn("Could not find GenomeRefSeq by contig: {}", variantContext.getContig());
                                    synchronized (countMap) {
                                        countMap.put("errorCount", countMap.get("errorCount").add(BigInteger.ONE));
                                    }
                                    continue;
                                }

                                GenomeRefSeq genomeRefSeq = foundGenomeRefSeqs.get(0);

                                for (Allele altAllele : variantContext.getAlternateAlleles()) {

                                    LocatedVariant locatedVariant = new LocatedVariant();
                                    locatedVariant.setGenomeRef(build37GenomeRef);
                                    locatedVariant.setGenomeRefSeq(genomeRefSeq);
                                    locatedVariant.setSeq(altAllele.getDisplayString());

                                    if (variantContext.isSNP()) {
                                        synchronized (countMap) {
                                            countMap.put("snpCount", countMap.get("snpCount").add(BigInteger.ONE));
                                        }

                                        List<GenomeRefSeqLocation> genomeRefSeqLocationList = daoBean.getGenomeRefSeqLocationDAO()
                                                .findByRefIdAndVersionedAccesionAndPosition(build37GenomeRef.getId(),
                                                        genomeRefSeq.getVerAccession(), variantContext.getStart());
                                        if (CollectionUtils.isNotEmpty(genomeRefSeqLocationList)) {
                                            locatedVariant.setRef(genomeRefSeqLocationList.get(0).getBase());
                                        } else {
                                            locatedVariant.setRef(variantContext.getReference().getDisplayString());
                                        }
                                        locatedVariant.setPosition(variantContext.getStart());
                                        locatedVariant.setVariantType(snpVariantType);
                                        locatedVariant.setEndPosition(variantContext.getStart() + locatedVariant.getRef().length());
                                    } else if (variantContext.isIndel() && variantContext.isSimpleDeletion()) {
                                        synchronized (countMap) {
                                            countMap.put("delCount", countMap.get("delCount").add(BigInteger.ONE));
                                        }

                                        locatedVariant.setPosition(variantContext.getStart() + 1);
                                        locatedVariant.setRef(
                                                variantContext.getReference().getDisplayString().replaceFirst(locatedVariant.getSeq(), ""));
                                        locatedVariant.setSeq(locatedVariant.getRef());
                                        locatedVariant.setVariantType(delVariantType);
                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + locatedVariant.getRef().length());
                                    } else if (variantContext.isIndel() && variantContext.isSimpleInsertion()) {
                                        synchronized (countMap) {
                                            countMap.put("insCount", countMap.get("insCount").add(BigInteger.ONE));
                                        }
                                        String ref = "";
                                        locatedVariant.setPosition(variantContext.getStart());
                                        List<GenomeRefSeqLocation> genomeRefSeqLocationList = daoBean.getGenomeRefSeqLocationDAO()
                                                .findByRefIdAndVersionedAccesionAndPosition(build37GenomeRef.getId(),
                                                        genomeRefSeq.getVerAccession(), locatedVariant.getPosition());
                                        if (CollectionUtils.isNotEmpty(genomeRefSeqLocationList)) {
                                            ref = genomeRefSeqLocationList.get(0).getBase();
                                        }

                                        locatedVariant.setVariantType(insVariantType);
                                        locatedVariant.setSeq(locatedVariant.getSeq().replaceFirst(ref, ""));
                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + ref.length());
                                        locatedVariant.setRef("");
                                    } else if (variantContext.isIndel() && !variantContext.isSimpleInsertion()
                                            && !variantContext.isSimpleDeletion()) {
                                        synchronized (countMap) {
                                            countMap.put("delCount", countMap.get("delCount").add(BigInteger.ONE));
                                        }
                                        locatedVariant.setPosition(variantContext.getStart() + 1);
                                        List<GenomeRefSeqLocation> genomeRefSeqLocationList = daoBean.getGenomeRefSeqLocationDAO()
                                                .findByRefIdAndVersionedAccesionAndPosition(build37GenomeRef.getId(),
                                                        genomeRefSeq.getVerAccession(), locatedVariant.getPosition());
                                        if (CollectionUtils.isNotEmpty(genomeRefSeqLocationList)) {
                                            locatedVariant.setRef(genomeRefSeqLocationList.get(0).getBase());
                                        }
                                        locatedVariant.setVariantType(delVariantType);
                                        locatedVariant.setSeq(
                                                variantContext.getReference().getDisplayString().replaceFirst(locatedVariant.getSeq(), ""));
                                        locatedVariant.setRef(locatedVariant.getSeq());
                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + locatedVariant.getRef().length());
                                    } else if (variantContext.isMNP()) {
                                        synchronized (countMap) {
                                            countMap.put("multiCount", countMap.get("multiCount").add(BigInteger.ONE));
                                        }

                                        char[] referenceChars = variantContext.getReference().getDisplayString().toCharArray();
                                        char[] alternateChars = locatedVariant.getSeq().toCharArray();

                                        StringBuilder charsToRemove = new StringBuilder();
                                        for (int i = 0; i < referenceChars.length; ++i) {
                                            if (referenceChars[i] != alternateChars[i]) {
                                                break;
                                            }
                                            charsToRemove.append(referenceChars[i]);
                                        }

                                        if (charsToRemove.length() > 0) {
                                            locatedVariant.setPosition(variantContext.getStart() + charsToRemove.length());
                                            locatedVariant.setRef(variantContext.getReference().getDisplayString()
                                                    .replaceFirst(charsToRemove.toString(), ""));
                                            locatedVariant.setSeq(locatedVariant.getSeq().replaceFirst(charsToRemove.toString(), ""));
                                        } else {
                                            locatedVariant.setPosition(variantContext.getStart());
                                            locatedVariant.setRef(variantContext.getReference().getDisplayString());
                                        }

                                        locatedVariant.setVariantType(subVariantType);
                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + locatedVariant.getRef().length());
                                    } else {
                                        synchronized (countMap) {
                                            countMap.put("subCount", countMap.get("subCount").add(BigInteger.ONE));
                                        }
                                        locatedVariant.setPosition(variantContext.getStart() + 1);
                                        List<GenomeRefSeqLocation> genomeRefSeqLocationList = daoBean.getGenomeRefSeqLocationDAO()
                                                .findByRefIdAndVersionedAccesionAndPosition(build37GenomeRef.getId(),
                                                        genomeRefSeq.getVerAccession(), locatedVariant.getPosition());
                                        if (CollectionUtils.isNotEmpty(genomeRefSeqLocationList)) {
                                            String ref = genomeRefSeqLocationList.get(0).getBase();
                                            locatedVariant.setRef(variantContext.getReference().getDisplayString().replaceFirst(ref, ""));
                                            locatedVariant.setSeq(locatedVariant.getSeq().replaceFirst(ref, ""));
                                        }
                                        locatedVariant.setVariantType(subVariantType);
                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + locatedVariant.getRef().length());
                                    }

                                    List<LocatedVariant> foundLocatedVariants = daoBean.getLocatedVariantDAO()
                                            .findByExample(locatedVariant);
                                    if (CollectionUtils.isNotEmpty(foundLocatedVariants)) {
                                        locatedVariant = foundLocatedVariants.get(0);
                                    } else {
                                        locatedVariant.setId(daoBean.getLocatedVariantDAO().save(locatedVariant));
                                    }
                                    logger.info(locatedVariant.toString());

                                    LocatedVariant liftOverLocatedVariant = liftOver(locatedVariant);
                                    foundLocatedVariants = daoBean.getLocatedVariantDAO().findByExample(liftOverLocatedVariant);
                                    if (CollectionUtils.isNotEmpty(foundLocatedVariants)) {
                                        locatedVariant = foundLocatedVariants.get(0);
                                    } else {
                                        locatedVariant.setId(daoBean.getLocatedVariantDAO().save(locatedVariant));
                                    }
                                    logger.info(locatedVariant.toString());

                                    Integer dp = genotype.getDP();
                                    int[] ad = genotype.getAD();

                                    Integer refDepth = null;
                                    Integer altDepth = null;

                                    if (ad != null && ad.length == 2) {
                                        refDepth = ad[0];
                                        altDepth = ad[1];
                                    }

                                    AssemblyLocatedVariantPK alKey = new AssemblyLocatedVariantPK(assemblyId, locatedVariant.getId());
                                    AssemblyLocatedVariant alv = new AssemblyLocatedVariant(alKey);
                                    if (genotype.hasGQ()) {
                                        alv.setGenotypeQuality(Double.valueOf(genotype.getGQ() >= 0 ? genotype.getGQ() : -1));
                                    }
                                    alv.setHomozygous(genotype.isHom());
                                    daoBean.getAssemblyLocatedVariantDAO().save(alv);
                                    logger.info(alv.toString());

                                    // this is retarded...following database constraints
                                    if (dp != null || qualityByDepth != null || readPosRankSum != null || dels != null
                                            || homopolymerRun != null || fs != null || refDepth != null || altDepth != null) {

                                        AssemblyLocatedVariantQCPK alvKey = new AssemblyLocatedVariantQCPK(assemblyId,
                                                locatedVariant.getId());
                                        AssemblyLocatedVariantQC alvQC = new AssemblyLocatedVariantQC(alvKey);
                                        if (dp == null || (dp != null && dp >= 0)) {
                                            alvQC.setDepth(dp);
                                        }
                                        if (qualityByDepth == null || (qualityByDepth != null && qualityByDepth >= 0)) {
                                            alvQC.setQualityByDepth(qualityByDepth);
                                        }
                                        alvQC.setReadPosRankSum(readPosRankSum);
                                        if (dels == null || (dels != null && dels >= 0 && dels <= 1))
                                            alvQC.setFracReadsWithDels(dels);
                                        if (homopolymerRun == null || (homopolymerRun != null && homopolymerRun >= 0)) {
                                            alvQC.setHomopolymerRun(homopolymerRun);
                                        }
                                        if (fs == null || (fs != null && fs >= 0)) {
                                            alvQC.setStrandScore(fs);
                                        }
                                        if (refDepth == null || (refDepth != null && refDepth >= 0)) {
                                            alvQC.setRefDepth(refDepth);
                                        }
                                        if (altDepth == null || (altDepth != null && altDepth >= 0)) {
                                            alvQC.setAltDepth(altDepth);
                                        }
                                        daoBean.getAssemblyLocatedVariantQCDAO().save(alvQC);
                                        logger.info(alvQC.toString());

                                    }

                                }

                            } catch (BinningDAOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

                es.shutdown();
                es.awaitTermination(1L, TimeUnit.DAYS);

                VariantSetLoad variantSetLoad = new VariantSetLoad();
                variantSetLoad.setLoadFilename(vcfFile.getAbsolutePath());
                variantSetLoad.setLoadProgramName(getClass().getName());
                variantSetLoad.setLoadProgramVersion(ResourceBundle.getBundle("org/renci/canvas/binning/canvas").getString("version"));
                variantSetLoad.setVariantSet(assembly.getVariantSet());

                List<VariantSetLoad> foundVariantSetLoads = daoBean.getVariantSetLoadDAO().findByExample(variantSetLoad);
                if (CollectionUtils.isNotEmpty(foundVariantSetLoads)) {
                    variantSetLoad = foundVariantSetLoads.get(0);
                }

                variantSetLoad.setLoadUser(System.getProperty("user.name"));
                variantSetLoad.setLoadTimeStart(startDate);
                variantSetLoad.setLoadTimeStop(new Date());
                variantSetLoad.setNotes("");
                variantSetLoad.setNumberOfDelRows(countMap.get("delCount").intValue());
                variantSetLoad.setNumberOfErrorRows(countMap.get("errorCount").intValue());
                variantSetLoad.setNumberOfFilteredRows(countMap.get("filterSkippedCount").intValue());
                variantSetLoad.setNumberOfInsRows(countMap.get("insCount").intValue());
                variantSetLoad.setNumberOfMultiRows(countMap.get("multiCount").intValue());
                variantSetLoad.setNumberOfSkippedRefRows(countMap.get("refSkippedCount").intValue());
                variantSetLoad.setNumberOfSNPRows(countMap.get("snpCount").intValue());
                variantSetLoad.setNumberOfSubRows(countMap.get("subCount").intValue());
                daoBean.getVariantSetLoadDAO().save(variantSetLoad);
                logger.info(variantSetLoad.toString());

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return null;
    }

    public BinningDAOBeanService getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(BinningDAOBeanService daoBean) {
        this.daoBean = daoBean;
    }

    public DiagnosticBinningJob getBinningJob() {
        return binningJob;
    }

    public void setBinningJob(DiagnosticBinningJob binningJob) {
        this.binningJob = binningJob;
    }

}
