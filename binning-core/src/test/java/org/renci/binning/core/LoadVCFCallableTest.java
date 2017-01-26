package org.renci.binning.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.CommonInfo;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;

public class LoadVCFCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(LoadVCFCallableTest.class);

    private static final BinningDAOManager daoMgr = BinningDAOManager.getInstance();

    @Test
    public void test() throws BinningDAOException, BinningException, IOException {

        DiagnosticBinningJob binningJob = daoMgr.getDAOBean().getDiagnosticBinningJobDAO().findById(4093);
        binningJob.setVcfFile(String.format("/tmp/%s",
                binningJob.getVcfFile().substring(binningJob.getVcfFile().lastIndexOf("/"), binningJob.getVcfFile().length())));
        Map<String, Object> variables = new HashMap<>();
        variables.put("job", binningJob);
        // variables.put(INTERVALS_DIR, "/storage/binning/annotation/intervals");

        String participant = binningJob.getParticipant();

        // String binningIntervalsDir = variables.get(INTERVALS_DIR).toString();
        //
        // String participantDir = String.format("%s/%s/%s", variables.get(ANNOTATION_DIR).toString(), "ncgenes",
        // binningJob.getParticipant());

        Map<String, String> avuMap = new HashMap<String, String>();
        avuMap.put("MaPSeqStudyName", "NC_GENES");
        avuMap.put("MaPSeqWorkflowName", "NCGenesBaseline");

        File vcfFile = null;
        // if (StringUtils.isEmpty(binningJob.getVcfFile())) {
        //
        // avuMap.put("MaPSeqJobName", "GATKApplyRecalibration");
        // avuMap.put("MaPSeqMimeType", "TEXT_VCF");
        // String irodsFile = IRODSUtils.findFile(participant, avuMap);
        // vcfFile = IRODSUtils.getFile(irodsFile, participantDir);
        // binningJob.setVcfFile(vcfFile.getAbsolutePath());
        // // daoMgr.getDAOBean().getDiagnosticBinningJobDAO().save(binningJob);
        // } else {
        // vcfFile = new File(binningJob.getVcfFile());
        // }

        GenomeRef build37GenomeRef = daoMgr.getDAOBean().getGenomeRefDAO().findById(2);

        try (VCFFileReader vcfFileReader = new VCFFileReader(vcfFile, false)) {

            VCFHeader vcfHeader = vcfFileReader.getFileHeader();
            List<String> sampleNames = vcfHeader.getGenotypeSamples();

            Set<String> excludesFilter = new HashSet<>();

            String labName = null;
            String libraryName = "";
            String studyName = null;
            String sampleNameOverride = null;

            switch (binningJob.getStudy()) {
                case "NCGENES Study":
                    excludesFilter.add("LowQual");

                    avuMap.put("ParticipantId", participant);
                    avuMap.put("MaPSeqSystem", "prod");
                    avuMap.put("MaPSeqJobName", "WriteVCFHeader");
                    avuMap.put("MaPSeqMimeType", "TEXT_PLAIN");
                    String possibleHeaderFile = IRODSUtils.findFile(avuMap);

                    if (StringUtils.isNotEmpty(possibleHeaderFile)) {
                        // List<String> headerFileLines = FileUtils.readLines(IRODSUtils.getFile(possibleHeaderFile,
                        // participantDir));
                        // for (String line : headerFileLines) {
                        // if (line.contains("LabName")) {
                        // labName = line.split(" = ")[1];
                        // }
                        // if (line.contains("StudyName")) {
                        // studyName = line.split(" = ")[1];
                        // }
                        // }
                    }
                    break;
                case "UNCSeq Cancer Study":
                    labName = "LCCC";
                    libraryName = "unknown";
                    studyName = "UNCSeq";
                    sampleNames.clear();
                    sampleNames.add(sampleNameOverride);
                    break;
                case "MSKCC":
                    labName = "MSKCC";
                    libraryName = "unknown";
                    studyName = "MSKCC";
                    sampleNames.clear();
                    sampleNames.add(sampleNameOverride);
                    break;
                case "PipelineTest":
                    labName = "PipelineTest";
                    libraryName = "unknown";
                    studyName = "PipelineTest";
                    sampleNames.clear();
                    sampleNames.add(sampleNameOverride);
                    break;
            }

            int snpCount = 0;
            int delCount = 0;
            int insCount = 0;
            int subCount = 0;
            int refSkippedCount = 0;
            int filterSkippedCount = 0;
            int noCallCount = 0;

            for (String sampleName : sampleNames) {

                Assembly assembly = null;
                if (binningJob.getAssembly() == null) {

                    // Lab lab = null;
                    // List<Lab> foundLabs = daoMgr.getDAOBean().getLabDAO().findByName(labName);
                    // if (CollectionUtils.isEmpty(foundLabs)) {
                    // lab = new Lab(labName);
                    // // daoMgr.getDAOBean().getLabDAO().save(lab);
                    // } else {
                    // lab = foundLabs.get(0);
                    // }
                    // logger.info(lab.toString());
                    //
                    // Project project = null;
                    // List<Project> foundProjects = daoMgr.getDAOBean().getProjectDAO().findByName(studyName);
                    // if (CollectionUtils.isEmpty(foundProjects)) {
                    // project = new Project(studyName);
                    // project.setLab(lab);
                    // // daoMgr.getDAOBean().getProjectDAO().save(project);
                    // } else {
                    // project = foundProjects.get(0);
                    // }
                    // logger.info(project.toString());
                    //
                    // Sample sample = null;
                    // List<Sample> foundSamples =
                    // daoMgr.getDAOBean().getSampleDAO().findByNameAndProjectName(sampleName, project.getName());
                    // if (CollectionUtils.isEmpty(foundSamples)) {
                    // sample = new Sample(sampleName);
                    // sample.setProject(project);
                    // // sample.setId(daoMgr.getDAOBean().getSampleDAO().save(sample));
                    // } else {
                    // sample = foundSamples.get(0);
                    // }
                    // logger.info(sample.toString());
                    //
                    // // library
                    // Library library = null;
                    // List<Library> foundLibraries =
                    // daoMgr.getDAOBean().getLibraryDAO().findByNameAndSampleId(libraryName, sample.getId());
                    // if (CollectionUtils.isEmpty(foundLibraries)) {
                    // library = new Library(libraryName);
                    // library.setSample(sample);
                    // // library.setId(daoMgr.getDAOBean().getLibraryDAO().save(library));
                    // } else {
                    // library = foundLibraries.get(0);
                    // }
                    // logger.info(library.toString());
                    //
                    // VariantSet variantSet = new VariantSet();
                    // variantSet.setGenomeRef(genomeRef);
                    // variantSet.setId(daoMgr.getDAOBean().getVariantSetDAO().save(variantSet));
                    //
                    // assembly = new Assembly();
                    // assembly.setLibrary(library);
                    // assembly.setVariantSet(variantSet);
                    // assembly.setId(daoMgr.getDAOBean().getAssemblyDAO().save(assembly));
                } else {
                    assembly = binningJob.getAssembly();
                }
                logger.info(assembly.toString());

                List<LocatedVariant> locatedVariantList = new ArrayList<>();
                for (VariantContext variantContext : vcfFileReader) {

                    Allele refAllele = variantContext.getReference();

                    if (refAllele.isNoCall()) {
                        noCallCount++;
                        continue;
                    }

                    String altAlleles = StringUtils.join(variantContext.getAlternateAlleles().toArray());
                    if (!altAlleles.matches("[AaCcGgTt,]*")) {
                        refSkippedCount++;
                        continue;
                    }

                    if (CollectionUtils.containsAny(variantContext.getFilters(), excludesFilter)) {
                        filterSkippedCount++;
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

                    GenotypesContext genotypesContext = variantContext.getGenotypes();

                    Integer refDepth = null;
                    Integer altDepth = null;

                    for (Genotype genotype : genotypesContext) {

                        if (genotype.isNoCall()) {
                            continue;
                        }

                        GenomeRefSeq genomeRefSeq = null;
                        List<GenomeRefSeq> foundGenomeRefSeqs = daoMgr.getDAOBean().getGenomeRefSeqDAO()
                                .findByVersionedAccession(variantContext.getContig());
                        if (CollectionUtils.isNotEmpty(foundGenomeRefSeqs)) {
                            genomeRefSeq = foundGenomeRefSeqs.get(0);
                        }

                        if (genomeRefSeq == null) {
                            throw new BinningDAOException(String.format("Unable to find GenomeRefSeq: %s ", variantContext.getContig()));
                        }

                        LocatedVariant locatedVariant = new LocatedVariant();
                        locatedVariant.setGenomeRef(build37GenomeRef);
                        locatedVariant.setGenomeRefSeq(genomeRefSeq);
                        locatedVariant.setPosition(variantContext.getStart());
                        locatedVariant.setSeq(StringUtils.join(variantContext.getAlternateAlleles().toArray()));

                        // List<GenomeRefSeqLocation> genomeRefSeqLocationList =
                        // daoMgr.getDAOBean().getGenomeRefSeqLocationDAO()
                        // .findByRefIdAndVersionedAccesionAndPosition(genomeRef.getId(),
                        // genomeRefSeq.getVerAccession(),
                        // variantContext.getStart());
                        // if (CollectionUtils.isNotEmpty(genomeRefSeqLocationList)) {
                        // locatedVariant.setRef(genomeRefSeqLocationList.get(0).getBase());
                        // } else {
                        locatedVariant.setRef(variantContext.getReference().getDisplayString());
                        // }

                        if (variantContext.isSNP()) {
                            snpCount++;
                            locatedVariant.setVariantType(daoMgr.getDAOBean().getVariantTypeDAO().findById("snp"));
                            locatedVariant.setEndPosition(locatedVariant.getPosition() + locatedVariant.getSeq().length());
                        } else if (variantContext.isIndel()) {
                            if (variantContext.isSimpleDeletion()) {
                                delCount++;
                                locatedVariant.setVariantType(daoMgr.getDAOBean().getVariantTypeDAO().findById("del"));
                            }
                            if (variantContext.isSimpleInsertion()) {
                                insCount++;
                                locatedVariant.setVariantType(daoMgr.getDAOBean().getVariantTypeDAO().findById("ins"));
                                String reference = variantContext.getReference().getBaseString();
                                locatedVariant.setSeq(locatedVariant.getSeq().replaceFirst(reference, ""));
                            }
                        } else {
                            subCount++;
                            locatedVariant.setVariantType(daoMgr.getDAOBean().getVariantTypeDAO().findById("sub"));
                        }

                        // List<LocatedVariant> foundLocatedVariants = daoMgr.getDAOBean().getLocatedVariantDAO()
                        // .findByExample(locatedVariant);
                        //
                        // if (CollectionUtils.isNotEmpty(foundLocatedVariants)) {
                        // locatedVariant = foundLocatedVariants.get(0);
                        // } else {
                        // locatedVariant.setId(daoMgr.getDAOBean().getLocatedVariantDAO().save(locatedVariant));
                        // }
                        // logger.info(locatedVariant.toString());
                        locatedVariantList.add(locatedVariant);

                    }

                }

                locatedVariantList.forEach(a -> logger.info(a.toString()));
                logger.info("locatedVariantList.size() = {}", locatedVariantList.size());

            }

            System.out.println("DONE");

        }

    }

}
