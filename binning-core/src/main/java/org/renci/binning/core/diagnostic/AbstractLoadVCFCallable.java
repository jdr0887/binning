package org.renci.binning.core.diagnostic;

import java.io.File;
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
import org.renci.binning.dao.BinningDAOException;
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

        int refSkippedCount = 0;
        int errorCount = 0;
        int filteredCount = 0;
        int noCallCount = 0;

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
                if (!vcfFile.exists()) {
                    vcfFile = getVCF(binningJob.getParticipant());
                    binningJob.setVcfFile(vcfFile.getAbsolutePath());
                    daoBean.getDiagnosticBinningJobDAO().save(binningJob);
                }
            }
            logger.info(binningJob.toString());

            final GenomeRef genomeRef = getGenomeRef();
            logger.info(genomeRef.toString());

            Map<String, List<VariantContext>> variantContext2SampleNameMap = new HashMap<String, List<VariantContext>>();

            Set<String> excludesFilter = getExcludesFilter();

            try (VCFFileReader vcfFileReader = new VCFFileReader(vcfFile, false)) {

                VCFHeader vcfHeader = vcfFileReader.getFileHeader();
                List<String> sampleNames = vcfHeader.getGenotypeSamples();

                for (String sampleName : sampleNames) {

                    if (!variantContext2SampleNameMap.containsKey(sampleName)) {
                        variantContext2SampleNameMap.put(sampleName, new ArrayList<VariantContext>());
                    }

                    variantContextLoop: for (VariantContext variantContext : vcfFileReader) {

                        Allele refAllele = variantContext.getReference();

                        if (refAllele.isNoCall()) {
                            noCallCount++;
                            continue;
                        }

                        String altAlleles = StringUtils.join(variantContext.getAlternateAlleles().toArray());
                        if (!altAlleles.matches("[AaCcGgTt,]*")) {
                            errorCount++;
                            continue;
                        }

                        if (CollectionUtils.containsAny(variantContext.getFilters(), excludesFilter)) {
                            filteredCount++;
                            continue;
                        }

                        GenotypesContext genotypesContext = variantContext.getGenotypes();

                        for (Genotype genotype : genotypesContext) {

                            if (genotype.isNoCall()) {
                                noCallCount++;
                                continue variantContextLoop;
                            }

                            if (genotype.isHomRef()) {
                                refSkippedCount++;
                                continue variantContextLoop;
                            }
                        }

                        variantContext2SampleNameMap.get(sampleName).add(variantContext);

                    }

                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new BinningException(e);
            }

            for (String sampleName : variantContext2SampleNameMap.keySet()) {

                if (!sampleName.equals(binningJob.getParticipant())) {
                    throw new BinningException("sampleName does match participant");
                }
                try {
                    Assembly assembly = buildAssembly(sampleName);
                    logger.info(assembly.toString());

                    List<LocatedVariant> locatedVariantList = new ArrayList<>();

                    List<VariantContext> variantContextList = variantContext2SampleNameMap.get(sampleName);

                    if (CollectionUtils.isNotEmpty(variantContextList)) {
                        logger.info("variantContextList.size(): {}", variantContextList.size());
                        for (VariantContext variantContext : variantContextList) {

                            List<GenomeRefSeq> foundGenomeRefSeqs = null;
                            if (variantContext.getContig().length() < 3 && !variantContext.getContig().startsWith("NC_")) {
                                foundGenomeRefSeqs = daoBean.getGenomeRefSeqDAO().findByRefIdAndContigAndSeqType(genomeRef.getId(),
                                        variantContext.getContig(), "Chromosome");
                            } else {
                                foundGenomeRefSeqs = daoBean.getGenomeRefSeqDAO().findByVersionedAccession(variantContext.getContig());
                            }

                            if (CollectionUtils.isEmpty(foundGenomeRefSeqs)) {
                                logger.warn("Could not find GenomeRefSeq by contig: {}", variantContext.getContig());
                                errorCount++;
                                continue;
                            }

                            GenomeRefSeq genomeRefSeq = foundGenomeRefSeqs.get(0);
                            logger.info(genomeRefSeq.toString());
                            List<String> types = variantContext.getAttributeAsStringList("TYPE", "");

                            for (Allele altAllele : variantContext.getAlternateAlleles()) {
                                if (variantContext.isSNP()) {
                                    LocatedVariant locatedVariant = new LocatedVariant(genomeRef, genomeRefSeq, variantContext.getStart(),
                                            variantContext.getStart() + variantContext.getReference().getDisplayString().length(),
                                            snpVariantType, variantContext.getReference().getDisplayString(), altAllele.getDisplayString());
                                    locatedVariantList.add(createLocatedVariant(locatedVariant));
                                    createAssmeblyLocatedVariantQC(sampleName, variantContext, locatedVariant, assembly);
                                }
                            }

                            for (Allele altAllele : variantContext.getAlternateAlleles()) {
                                if (variantContext.isIndel() && variantContext.isSimpleInsertion()) {
                                    String ref = variantContext.getReference().getDisplayString();
                                    LocatedVariant locatedVariant = new LocatedVariant(genomeRef, genomeRefSeq, variantContext.getStart(),
                                            variantContext.getStart() + ref.length(), insVariantType, "",
                                            altAllele.getDisplayString().replaceFirst(ref, ""));
                                    locatedVariantList.add(createLocatedVariant(locatedVariant));
                                    createAssmeblyLocatedVariantQC(sampleName, variantContext, locatedVariant, assembly);
                                }
                            }

                            for (Allele altAllele : variantContext.getAlternateAlleles()) {
                                if (variantContext.isIndel() && variantContext.isSimpleDeletion()) {
                                    String ref = variantContext.getReference().getDisplayString().replaceFirst(altAllele.getDisplayString(),
                                            "");
                                    LocatedVariant locatedVariant = new LocatedVariant(genomeRef, genomeRefSeq,
                                            variantContext.getStart() + 1, variantContext.getStart() + 1 + ref.length(), delVariantType,
                                            ref, ref);
                                    locatedVariantList.add(createLocatedVariant(locatedVariant));
                                    createAssmeblyLocatedVariantQC(sampleName, variantContext, locatedVariant, assembly);
                                }
                            }

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

                                    // cant trust htsjdk to parse properly...switch on freebayes type (if available)
                                    if (CollectionUtils.isNotEmpty(types)) {
                                        LocatedVariant locatedVariant = new LocatedVariant(genomeRef, genomeRefSeq);
                                        String type = types.get(variantContext.getAlleleIndex(altAllele) - 1);
                                        switch (type) {
                                            case "del":
                                                locatedVariant.setVariantType(delVariantType);
                                                String ref = variantContext.getReference().getDisplayString()
                                                        .replaceFirst(altAllele.getDisplayString(), "");
                                                locatedVariant.setPosition(variantContext.getStart() + 1);
                                                locatedVariant.setEndPosition(variantContext.getStart() + 1 + ref.length());
                                                locatedVariant.setRef(ref);
                                                locatedVariant.setSeq(ref);
                                                break;
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
                                        locatedVariantList.add(createLocatedVariant(locatedVariant));
                                        createAssmeblyLocatedVariantQC(sampleName, variantContext, locatedVariant, assembly);
                                    }
                                }
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
                    variantSetLoad.setNumberOfInsRows(locatedVariantList.stream().filter(a -> a.getVariantType().getName().equals("ins"))
                            .collect(Collectors.toList()).size());
                    variantSetLoad.setNumberOfSNPRows(locatedVariantList.stream().filter(a -> a.getVariantType().getName().equals("snp"))
                            .collect(Collectors.toList()).size());
                    variantSetLoad.setNumberOfSubRows(locatedVariantList.stream().filter(a -> a.getVariantType().getName().equals("sub"))
                            .collect(Collectors.toList()).size());
                    variantSetLoad.setNumberOfErrorRows(errorCount);
                    variantSetLoad.setNumberOfFilteredRows(filteredCount);
                    variantSetLoad.setNumberOfMultiRows(0);
                    variantSetLoad.setNumberOfSkippedRefRows(refSkippedCount);
                    daoBean.getVariantSetLoadDAO().save(variantSetLoad);
                    logger.info(variantSetLoad.toString());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new BinningException(e);
                }

            }

        } catch (

        Exception e) {
            logger.error(e.getMessage(), e);
            throw new BinningException(e);
        }

        return null;
    }

    private LocatedVariant createLocatedVariant(LocatedVariant locatedVariant) throws BinningException {
        logger.debug("ENTERING createLocatedVariant(LocatedVariant)");
        logger.info(locatedVariant.toString());
        try {
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
            List<CanonicalAllele> foundCanonicalAlleles = daoBean.getCanonicalAlleleDAO().findByLocatedVariantId(locatedVariant.getId());
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
        } catch (BinningDAOException e) {
            throw new BinningException(e);
        }

        return locatedVariant;
    }

    private void createAssmeblyLocatedVariantQC(String sampleName, VariantContext variantContext, LocatedVariant locatedVariant,
            Assembly assembly) throws BinningException {

        try {

            CommonInfo commonInfo = variantContext.getCommonInfo();

            Double qualityByDepth = commonInfo.hasAttribute("QD") ? Double.valueOf(commonInfo.getAttribute("QD").toString()) : null;

            Double readPosRankSum = commonInfo.hasAttribute("ReadPosRankSum")
                    ? Double.valueOf(commonInfo.getAttribute("ReadPosRankSum").toString()) : null;

            Integer homopolymerRun = commonInfo.hasAttribute("HRun") ? Integer.valueOf(commonInfo.getAttribute("HRun").toString()) : null;

            Double dels = commonInfo.hasAttribute("Dels") ? Double.valueOf(commonInfo.getAttribute("Dels").toString()) : null;

            Double fs = commonInfo.hasAttribute("FS") ? Double.valueOf(commonInfo.getAttribute("FS").toString()) : null;

            Genotype genotype = variantContext.getGenotype(sampleName);
            Integer dp = genotype.getDP();
            int[] ad = genotype.getAD();

            Integer refDepth = null;
            Integer altDepth = null;

            if (ad != null && ad.length == 2) {
                refDepth = ad[0];
                altDepth = ad[1];
            }

            AssemblyLocatedVariantPK alKey = new AssemblyLocatedVariantPK(assembly.getId(), locatedVariant.getId());
            AssemblyLocatedVariant alv = new AssemblyLocatedVariant(alKey);
            if (genotype.hasGQ()) {
                alv.setGenotypeQuality(Double.valueOf(genotype.getGQ() >= 0 ? genotype.getGQ() : -1));
            }
            alv.setHomozygous(genotype.isHom());
            daoBean.getAssemblyLocatedVariantDAO().save(alv);
            logger.info(alv.toString());

            // this is retarded...following database constraints
            if (dp != null || qualityByDepth != null || readPosRankSum != null || dels != null || homopolymerRun != null || fs != null
                    || refDepth != null || altDepth != null) {

                AssemblyLocatedVariantQCPK alvKey = new AssemblyLocatedVariantQCPK(assembly.getId(), locatedVariant.getId());
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
        } catch (NumberFormatException | BinningDAOException e) {
            throw new BinningException(e);
        }

    }

    private Assembly buildAssembly(String sampleName) throws BinningException {
        logger.debug("ENTERING buildAssembly(String)");
        Assembly assembly = null;

        try {
            Lab lab = null;
            List<Lab> foundLabs = daoBean.getLabDAO().findByName(getLabName());
            if (CollectionUtils.isEmpty(foundLabs)) {
                lab = new Lab(getLabName());
                daoBean.getLabDAO().save(lab);
            } else {
                lab = foundLabs.get(0);
            }
            logger.info(lab.toString());

            Project project = daoBean.getProjectDAO().findById(getStudyName());
            if (project == null) {
                project = new Project(getStudyName());
                project.setLab(lab);
                daoBean.getProjectDAO().save(project);
                project = daoBean.getProjectDAO().findById(getStudyName());
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
            List<Library> foundLibraries = daoBean.getLibraryDAO().findByNameAndSampleId(getLibraryName(), sample.getId());
            if (CollectionUtils.isEmpty(foundLibraries)) {
                library = new Library(getLibraryName());
                library.setSample(sample);
                library.setId(daoBean.getLibraryDAO().save(library));
            } else {
                library = foundLibraries.get(0);
            }
            logger.info(library.toString());

            List<Assembly> foundAssemblies = daoBean.getAssemblyDAO().findByLibraryId(library.getId());
            if (CollectionUtils.isEmpty(foundAssemblies)) {

                VariantSet variantSet = new VariantSet();
                variantSet.setGenomeRef(getGenomeRef());
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
        } catch (BinningDAOException e) {
            e.printStackTrace();
        }
        return assembly;
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
