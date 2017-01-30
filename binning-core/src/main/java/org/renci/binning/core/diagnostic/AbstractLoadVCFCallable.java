package org.renci.binning.core.diagnostic;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.renci.binning.core.BinningException;
import org.renci.binning.dao.BinningDAOBeanService;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantPK;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQCPK;
import org.renci.binning.dao.var.model.CanonicalAllele;
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

    public abstract GenomeRef getGenomeRef();

    public abstract LocatedVariant liftOver(LocatedVariant locatedVariant) throws BinningException;

    public AbstractLoadVCFCallable(BinningDAOBeanService daoBean, DiagnosticBinningJob binningJob) {
        super();
        this.daoBean = daoBean;
        this.binningJob = binningJob;
    }

    @Override
    public Void call() throws BinningException {
        logger.debug("ENTERING run()");

        final Map<String, BigInteger> countMap = new HashMap<String, BigInteger>();
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

            GenomeRef genomeRef = getGenomeRef();

            final List<LocatedVariant> locatedVariantList = new ArrayList<>();
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
                    project = daoBean.getProjectDAO().findById(studyName);
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
                    variantSet.setGenomeRef(genomeRef);
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
                            countMap.put("errorCount", countMap.get("errorCount").add(BigInteger.ONE));
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
                                foundGenomeRefSeqs = daoBean.getGenomeRefSeqDAO().findByRefIdAndContigAndSeqType(genomeRef.getId(),
                                        variantContext.getContig(), "Chromosome");
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
                                locatedVariant.setGenomeRefSeq(genomeRefSeq);
                                locatedVariant.setGenomeRef(getGenomeRef());
                                if (variantContext.isSNP()) {
                                    locatedVariant.setSeq(altAllele.getDisplayString());
                                    locatedVariant.setRef(variantContext.getReference().getDisplayString());
                                    locatedVariant.setPosition(variantContext.getStart());
                                    locatedVariant.setVariantType(snpVariantType);
                                    locatedVariant.setEndPosition(variantContext.getStart() + locatedVariant.getRef().length());
                                    locatedVariantList.add(locatedVariant);
                                }
                            }

                            for (Allele altAllele : variantContext.getAlternateAlleles()) {
                                LocatedVariant locatedVariant = new LocatedVariant();
                                locatedVariant.setGenomeRefSeq(genomeRefSeq);
                                locatedVariant.setGenomeRef(getGenomeRef());
                                if (variantContext.isIndel() && variantContext.isSimpleInsertion()) {
                                    locatedVariant.setPosition(variantContext.getStart());
                                    locatedVariant.setVariantType(insVariantType);
                                    String ref = variantContext.getReference().getDisplayString();
                                    locatedVariant.setSeq(altAllele.getDisplayString().replaceFirst(ref, ""));
                                    locatedVariant.setEndPosition(locatedVariant.getPosition() + ref.length());
                                    locatedVariant.setRef("");
                                    locatedVariantList.add(locatedVariant);
                                }
                            }

                            for (Allele altAllele : variantContext.getAlternateAlleles()) {
                                LocatedVariant locatedVariant = new LocatedVariant();
                                locatedVariant.setGenomeRefSeq(genomeRefSeq);
                                locatedVariant.setGenomeRef(getGenomeRef());
                                if (variantContext.isIndel() && variantContext.isSimpleDeletion()) {
                                    locatedVariant.setPosition(variantContext.getStart() + 1);
                                    locatedVariant.setRef(variantContext.getReference().getDisplayString()
                                            .replaceFirst(altAllele.getDisplayString(), ""));
                                    locatedVariant.setSeq(locatedVariant.getRef());
                                    locatedVariant.setVariantType(delVariantType);
                                    locatedVariant.setEndPosition(locatedVariant.getPosition() + locatedVariant.getRef().length());
                                    locatedVariantList.add(locatedVariant);
                                }
                            }

                            // cant trust htsjdk to parse properly...switch on freebayes type (if available)
                            List<String> types = variantContext.getAttributeAsStringList("TYPE", null);
                            for (Allele altAllele : variantContext.getAlternateAlleles()) {
                                if ((variantContext.isIndel() && variantContext.isComplexIndel()) || variantContext.isMNP()) {

                                    String forwardRef = variantContext.getReference().getDisplayString();
                                    String forwardAlt = altAllele.getDisplayString();
                                    String forwardDiff = StringUtils.difference(forwardAlt, forwardRef);
                                    Integer forwardDiffIdx = StringUtils.indexOfDifference(forwardAlt, forwardRef);

                                    String reverseRef = StringUtils.reverse(variantContext.getReference().getDisplayString());
                                    String reverseAlt = StringUtils.reverse(altAllele.getDisplayString());
                                    String reverseDiff = StringUtils.difference(reverseAlt, reverseRef);
                                    Integer reverseDiffIdx = StringUtils.indexOfDifference(reverseAlt, reverseRef);

                                    if (CollectionUtils.isNotEmpty(types)) {
                                        LocatedVariant locatedVariant = new LocatedVariant();
                                        locatedVariant.setGenomeRefSeq(genomeRefSeq);
                                        locatedVariant.setGenomeRef(getGenomeRef());
                                        String type = types.get(variantContext.getAlleleIndex(altAllele) - 1);
                                        switch (type) {
                                            case "ins":
                                                locatedVariant.setVariantType(insVariantType);
                                                locatedVariant.setRef("");
                                                if (forwardDiffIdx > 0 && reverseDiffIdx == 0) {
                                                    locatedVariant.setPosition(variantContext.getStart());
                                                    locatedVariant.setSeq(StringUtils.difference(forwardRef, forwardAlt));
                                                    locatedVariant.setEndPosition(locatedVariant.getPosition() + forwardDiffIdx);
                                                } else if (forwardDiffIdx == 0 && reverseDiffIdx > 0) {
                                                    locatedVariant.setPosition(variantContext.getStart());
                                                    locatedVariant.setSeq(StringUtils.difference(reverseRef, reverseAlt));
                                                    locatedVariant.setEndPosition(locatedVariant.getPosition() + reverseDiffIdx);
                                                }

                                                break;
                                            case "snp":
                                                if (forwardDiffIdx > 0 && reverseDiffIdx == 0) {
                                                    locatedVariant.setPosition(variantContext.getStart());
                                                    locatedVariant.setRef(forwardRef);
                                                    locatedVariant.setSeq(StringUtils.difference(forwardRef, forwardAlt));
                                                    locatedVariant.setEndPosition(locatedVariant.getPosition() + forwardDiffIdx);
                                                } else if (forwardDiffIdx == 0 && reverseDiffIdx > 0) {
                                                    locatedVariant.setPosition(variantContext.getStart());
                                                    locatedVariant.setRef(reverseRef);
                                                    locatedVariant.setSeq(StringUtils.difference(reverseRef, reverseAlt));
                                                    locatedVariant.setEndPosition(locatedVariant.getPosition() + reverseDiffIdx);
                                                }
                                                locatedVariant.setVariantType(snpVariantType);
                                                break;
                                            default:
                                                locatedVariant.setVariantType(subVariantType);
                                                locatedVariant.setPosition(variantContext.getStart());
                                                locatedVariant.setRef(forwardRef);
                                                locatedVariant.setSeq(forwardAlt);
                                                locatedVariant.setEndPosition(locatedVariant.getPosition() + forwardRef.length());
                                                break;
                                        }
                                        locatedVariantList.add(locatedVariant);
                                    }
                                }
                            }
                            logger.info("done creating LocatedVariant instances");
                            locatedVariantList.forEach(a -> logger.debug(a.toString()));

                            for (LocatedVariant locatedVariant : locatedVariantList) {

                                List<LocatedVariant> foundLocatedVariants = daoBean.getLocatedVariantDAO().findByExample(locatedVariant);
                                if (CollectionUtils.isNotEmpty(foundLocatedVariants)) {
                                    locatedVariant = foundLocatedVariants.get(0);
                                } else {
                                    locatedVariant.setId(daoBean.getLocatedVariantDAO().save(locatedVariant));
                                }

                                LocatedVariant liftOverLocatedVariant = liftOver(locatedVariant);
                                logger.info(liftOverLocatedVariant.toString());
                                foundLocatedVariants = daoBean.getLocatedVariantDAO().findByExample(liftOverLocatedVariant);
                                if (CollectionUtils.isNotEmpty(foundLocatedVariants)) {
                                    liftOverLocatedVariant = foundLocatedVariants.get(0);
                                } else {
                                    liftOverLocatedVariant.setId(daoBean.getLocatedVariantDAO().save(liftOverLocatedVariant));
                                }

                                CanonicalAllele canonicalAllele = null;
                                // first try to find CanonicalAllele by LocatedVariant
                                List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO()
                                        .findByLocatedVariantId(locatedVariant.getId());
                                if (CollectionUtils.isNotEmpty(foundCanonicalAlleles)) {
                                    canonicalAllele = foundCanonicalAlleles.get(0);
                                }

                                // if not found, try to find CanonicalAllele by liftover LocatedVariant
                                if (canonicalAllele == null) {
                                    List<CanonicalAllele> foundCanonicalAllelesByLiftOverLocatedVariant = daoBean.getCanonicalAlleleDAO()
                                            .findByLocatedVariantId(liftOverLocatedVariant.getId());
                                    if (CollectionUtils.isNotEmpty(foundCanonicalAllelesByLiftOverLocatedVariant)) {
                                        canonicalAllele = foundCanonicalAlleles.get(0);
                                    }
                                }

                                // if still null, it doesn't exist...so create it
                                if (canonicalAllele == null) {
                                    canonicalAllele = new CanonicalAllele();
                                    daoBean.getCanonicalAlleleDAO().save(canonicalAllele);
                                    canonicalAllele.getLocatedVariants().add(locatedVariant);
                                    canonicalAllele.getLocatedVariants().add(liftOverLocatedVariant);
                                    daoBean.getCanonicalAlleleDAO().save(canonicalAllele);
                                } else {

                                    if (!canonicalAllele.getLocatedVariants().contains(locatedVariant)) {
                                        canonicalAllele.getLocatedVariants().add(locatedVariant);
                                    }

                                    if (!canonicalAllele.getLocatedVariants().contains(liftOverLocatedVariant)) {
                                        canonicalAllele.getLocatedVariants().add(liftOverLocatedVariant);
                                    }
                                    daoBean.getCanonicalAlleleDAO().save(canonicalAllele);

                                }

                            }
                            logger.info("done persisting LocatedVariants");

                            
                            Integer dp = genotype.getDP();
                            int[] ad = genotype.getAD();

                            Integer refDepth = null;
                            Integer altDepth = null;

                            if (ad != null && ad.length == 2) {
                                refDepth = ad[0];
                                altDepth = ad[1];
                            }

                            for (LocatedVariant locatedVariant : locatedVariantList) {
                                AssemblyLocatedVariantPK alKey = new AssemblyLocatedVariantPK(assemblyId, locatedVariant.getId());
                                AssemblyLocatedVariant alv = new AssemblyLocatedVariant(alKey);
                                if (genotype.hasGQ()) {
                                    alv.setGenotypeQuality(Double.valueOf(genotype.getGQ() >= 0 ? genotype.getGQ() : -1));
                                }
                                alv.setHomozygous(genotype.isHom());
                                daoBean.getAssemblyLocatedVariantDAO().save(alv);
                                logger.info(alv.toString());

                                // this is retarded...following database constraints
                                if (dp != null || qualityByDepth != null || readPosRankSum != null || dels != null || homopolymerRun != null
                                        || fs != null || refDepth != null || altDepth != null) {

                                    AssemblyLocatedVariantQCPK alvKey = new AssemblyLocatedVariantQCPK(assemblyId, locatedVariant.getId());
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
                            logger.info("done associated LocatedVariants w/ the Assembly");

                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            e.printStackTrace();
                        }

                    }
                }

                VariantSetLoad variantSetLoad = new VariantSetLoad();
                variantSetLoad.setLoadFilename(vcfFile.getAbsolutePath());
                variantSetLoad.setLoadProgramName(getClass().getName());

                // BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
                // Bundle bundle = bundleContext.getBundle();
                // String version = bundle.getVersion().toString();
                String version = ResourceBundle.getBundle("org/renci/binning/binning").getString("version");

                variantSetLoad.setLoadProgramVersion(version);
                variantSetLoad.setVariantSet(assembly.getVariantSet());

                List<VariantSetLoad> foundVariantSetLoads = daoBean.getVariantSetLoadDAO().findByExample(variantSetLoad);
                if (CollectionUtils.isNotEmpty(foundVariantSetLoads)) {
                    variantSetLoad = foundVariantSetLoads.get(0);
                }

                variantSetLoad.setLoadUser(System.getProperty("user.name"));
                variantSetLoad.setLoadTimeStart(startDate);
                variantSetLoad.setLoadTimeStop(new Date());
                variantSetLoad.setNotes("");
                variantSetLoad.setNumberOfDelRows(locatedVariantList.stream().filter(a -> a.getVariantType().getName().equals("del"))
                        .collect(Collectors.toList()).size());
                variantSetLoad.setNumberOfErrorRows(countMap.get("errorCount").intValue());
                variantSetLoad.setNumberOfFilteredRows(countMap.get("filterSkippedCount").intValue());
                variantSetLoad.setNumberOfInsRows(locatedVariantList.stream().filter(a -> a.getVariantType().getName().equals("ins"))
                        .collect(Collectors.toList()).size());
                variantSetLoad.setNumberOfMultiRows(countMap.get("multiCount").intValue());
                variantSetLoad.setNumberOfSkippedRefRows(countMap.get("refSkippedCount").intValue());
                variantSetLoad.setNumberOfSNPRows(locatedVariantList.stream().filter(a -> a.getVariantType().getName().equals("snp"))
                        .collect(Collectors.toList()).size());
                variantSetLoad.setNumberOfSubRows(locatedVariantList.stream().filter(a -> a.getVariantType().getName().equals("sub"))
                        .collect(Collectors.toList()).size());
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
