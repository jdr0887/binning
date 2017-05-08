package org.renci.binning.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.renci.canvas.dao.var.model.VariantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;

public class LoadVCFCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(LoadVCFCallableTest.class);

    @Test
    public void scratch() {

        Integer position = 104951923;
        String ref = "TGACG";
        String alt = "TGTCG";

        char[] referenceChars = ref.toCharArray();
        char[] alternateChars = alt.toCharArray();

        StringBuilder frontChars2Remove = new StringBuilder();
        StringBuilder backChars2Remove = new StringBuilder();

        for (int i = 0; i < referenceChars.length; ++i) {
            if (referenceChars[i] != alternateChars[i]) {
                break;
            }
            frontChars2Remove.append(referenceChars[i]);
        }

        for (int i = referenceChars.length - 1; i > 0; --i) {
            if (referenceChars[i] != alternateChars[i]) {
                break;
            }
            backChars2Remove.append(referenceChars[i]);
        }

        LocatedVariant locatedVariant = new LocatedVariant();

        if (frontChars2Remove.length() > 0) {
            ref = ref.replaceFirst(frontChars2Remove.toString(), "");
            alt = alt.replaceFirst(frontChars2Remove.toString(), "");
        }

        if (backChars2Remove.length() > 0) {
            backChars2Remove.reverse();
            ref = StringUtils.removeEnd(ref, backChars2Remove.toString());
            alt = StringUtils.removeEnd(alt, backChars2Remove.toString());
        }

        locatedVariant.setPosition(position + frontChars2Remove.length() > 0 ? frontChars2Remove.length() : 0);
        locatedVariant.setEndPosition(locatedVariant.getPosition() + 1);
        locatedVariant.setRef(ref);
        locatedVariant.setSeq(alt);
        System.out.println(locatedVariant.toString());
    }

    @Test
    public void test() throws CANVASDAOException, BinningException, IOException {

        // File vcfFile = new File("/tmp", "GSK_007006.merged.fb.sorted.va.vcf");
        // File vcfFile = new File("/tmp", "GSK_007217.merged.fb.sorted.va.vcf");
        // File vcfFile = new File("/tmp", "NCG_00020.merged.rg.deduped.filtered_by_dxid_7_v22.vcf");
        File vcfFile = new File("/tmp/NCX_00004", "NCX_00004.merged.rg.deduped.filtered.srd.ps.va.vcf");

        int refSkippedCount = 0;
        int errorCount = 0;
        int filteredCount = 0;
        int noCallCount = 0;

        Map<String, List<VariantContext>> variantContext2SampleNameMap = new HashMap<String, List<VariantContext>>();

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
            e.printStackTrace();
        }

        List<LocatedVariant> locatedVariantList = new ArrayList<>();

        for (String sampleName : variantContext2SampleNameMap.keySet()) {
            List<VariantContext> variantContextList = variantContext2SampleNameMap.get(sampleName);

            if (CollectionUtils.isNotEmpty(variantContextList)) {
                for (VariantContext variantContext : variantContextList) {

                    for (Allele altAllele : variantContext.getAlternateAlleles()) {
                        LocatedVariant locatedVariant = new LocatedVariant();
                        if (variantContext.isSNP()) {
                            locatedVariant.setSeq(altAllele.getDisplayString());
                            locatedVariant.setRef(variantContext.getReference().getDisplayString());
                            locatedVariant.setPosition(variantContext.getStart());
                            locatedVariant.setVariantType(new VariantType("snp"));
                            locatedVariant.setEndPosition(variantContext.getStart() + locatedVariant.getRef().length());
                            locatedVariantList.add(locatedVariant);
                        }
                    }

                }

                for (VariantContext variantContext : variantContextList) {

                    for (Allele altAllele : variantContext.getAlternateAlleles()) {
                        LocatedVariant locatedVariant = new LocatedVariant();
                        if (variantContext.isSimpleInsertion()) {
                            locatedVariant.setPosition(variantContext.getStart());
                            locatedVariant.setVariantType(new VariantType("ins"));
                            String ref = variantContext.getReference().getDisplayString();
                            locatedVariant.setSeq(altAllele.getDisplayString().replaceFirst(ref, ""));
                            locatedVariant.setEndPosition(locatedVariant.getPosition() + ref.length());
                            locatedVariant.setRef("");
                            locatedVariantList.add(locatedVariant);
                        }
                    }

                }

                for (VariantContext variantContext : variantContextList) {

                    for (Allele altAllele : variantContext.getAlternateAlleles()) {
                        LocatedVariant locatedVariant = new LocatedVariant();
                        if (variantContext.isSimpleDeletion()) {
                            locatedVariant.setPosition(variantContext.getStart() + 1);
                            locatedVariant.setRef(
                                    variantContext.getReference().getDisplayString().replaceFirst(altAllele.getDisplayString(), ""));
                            locatedVariant.setSeq(locatedVariant.getRef());
                            locatedVariant.setVariantType(new VariantType("del"));
                            locatedVariant.setEndPosition(locatedVariant.getPosition() + locatedVariant.getRef().length());
                            locatedVariantList.add(locatedVariant);
                        }
                    }

                }

                for (VariantContext variantContext : variantContextList) {

                    // cant trust htsjdk to parse properly...switch on freebayes type (if available)
                    List<String> types = variantContext.getAttributeAsStringList("TYPE", null);
                    for (Allele altAllele : variantContext.getAlternateAlleles()) {
                        if ((variantContext.isIndel() && variantContext.isComplexIndel()) || variantContext.isMNP()) {

                            String ref = variantContext.getReference().getDisplayString();
                            String alt = altAllele.getDisplayString();

                            char[] referenceChars = ref.toCharArray();
                            char[] alternateChars = alt.toCharArray();

                            if (CollectionUtils.isNotEmpty(types)) {
                                LocatedVariant locatedVariant = new LocatedVariant();
                                String type = types.get(variantContext.getAlleleIndex(altAllele) - 1);
                                switch (type) {
                                    case "del":
                                        locatedVariant.setPosition(variantContext.getStart() + 1);
                                        locatedVariant.setVariantType(new VariantType("del"));
                                        locatedVariant.setRef(variantContext.getReference().getDisplayString()
                                                .replaceFirst(altAllele.getDisplayString(), ""));
                                        locatedVariant.setSeq(locatedVariant.getRef());
                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + locatedVariant.getRef().length());
                                        break;
                                    case "ins":
                                        locatedVariant.setVariantType(new VariantType("ins"));
                                        locatedVariant.setPosition(variantContext.getStart());

                                        if (referenceChars.length > 1 && alternateChars.length > 1) {

                                            StringBuilder frontChars2Remove = new StringBuilder();
                                            StringBuilder backChars2Remove = new StringBuilder();

                                            for (int i = 0; i < referenceChars.length; ++i) {
                                                if (referenceChars[i] != alternateChars[i]) {
                                                    break;
                                                }
                                                frontChars2Remove.append(referenceChars[i]);
                                            }

                                            for (int i = referenceChars.length - 1; i > 0; --i) {
                                                if (referenceChars[i] != alternateChars[i]) {
                                                    break;
                                                }
                                                backChars2Remove.append(referenceChars[i]);
                                            }

                                            if (frontChars2Remove.length() > 0) {
                                                ref = ref.replaceFirst(frontChars2Remove.toString(), "");
                                                alt = alt.replaceFirst(frontChars2Remove.toString(), "");
                                            }

                                            if (backChars2Remove.length() > 0) {
                                                backChars2Remove.reverse();
                                                ref = StringUtils.removeEnd(ref, backChars2Remove.toString());
                                                alt = StringUtils.removeEnd(alt, backChars2Remove.toString());
                                            }

                                            locatedVariant.setPosition(variantContext.getStart()
                                                    + (frontChars2Remove.length() > 0 ? frontChars2Remove.length() : 0));
                                        }

                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + 1);
                                        locatedVariant.setRef("");
                                        locatedVariant.setSeq(alt);

                                        break;
                                    case "snp":
                                        locatedVariant.setVariantType(new VariantType("snp"));
                                        locatedVariant.setPosition(variantContext.getStart());

                                        if (referenceChars.length > 1 && alternateChars.length > 1) {

                                            StringBuilder frontChars2Remove = new StringBuilder();
                                            StringBuilder backChars2Remove = new StringBuilder();

                                            for (int i = 0; i < referenceChars.length; ++i) {
                                                if (referenceChars[i] != alternateChars[i]) {
                                                    break;
                                                }
                                                frontChars2Remove.append(referenceChars[i]);
                                            }

                                            for (int i = referenceChars.length - 1; i > 0; --i) {
                                                if (referenceChars[i] != alternateChars[i]) {
                                                    break;
                                                }
                                                backChars2Remove.append(referenceChars[i]);
                                            }

                                            if (frontChars2Remove.length() > 0) {
                                                ref = ref.replaceFirst(frontChars2Remove.toString(), "");
                                                alt = alt.replaceFirst(frontChars2Remove.toString(), "");
                                            }

                                            if (backChars2Remove.length() > 0) {
                                                backChars2Remove.reverse();
                                                ref = StringUtils.removeEnd(ref, backChars2Remove.toString());
                                                alt = StringUtils.removeEnd(alt, backChars2Remove.toString());
                                            }

                                            locatedVariant.setPosition(variantContext.getStart()
                                                    + (frontChars2Remove.length() > 0 ? frontChars2Remove.length() : 0));
                                        }
                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + 1);
                                        locatedVariant.setRef(ref);
                                        locatedVariant.setSeq(alt);

                                        break;
                                    default:
                                        locatedVariant.setVariantType(new VariantType("sub"));
                                        locatedVariant.setPosition(variantContext.getStart());
                                        locatedVariant.setRef(ref);
                                        locatedVariant.setSeq(alt);
                                        locatedVariant.setEndPosition(locatedVariant.getPosition() + ref.length());
                                        break;
                                }
                                locatedVariantList.add(locatedVariant);
                            }
                        }
                    }

                }
            }

            // List<LocatedVariant> snps = locatedVariantList.stream()
            // .filter(a -> a.getVariantType().getName().equals("snp") && a.getPosition().equals(11221454))
            // .collect(Collectors.toList());
            // snps.forEach(a -> logger.info(a.toString()));

            List<LocatedVariant> subs = locatedVariantList.stream().filter(a -> a.getVariantType().getId().equals("sub"))
                    .collect(Collectors.toList());
            // subs.forEach(a -> logger.info(a.toString()));

            List<LocatedVariant> ins = locatedVariantList.stream().filter(a -> a.getVariantType().getId().equals("ins"))
                    .collect(Collectors.toList());
            // ins.forEach(a -> logger.info(a.toString()));

            List<LocatedVariant> snps = locatedVariantList.stream().filter(a -> a.getVariantType().getId().equals("snp"))
                    .collect(Collectors.toList());
            snps.forEach(a -> logger.info(a.toString()));

            List<LocatedVariant> dels = locatedVariantList.stream().filter(a -> a.getVariantType().getId().equals("del"))
                    .collect(Collectors.toList());
            // dels.forEach(a -> logger.info(a.toString()));

            logger.info("locatedVariantList.size() = {}", locatedVariantList.size());

            System.out.println("num_snp_rows | num_del_rows | num_sub_rows | num_ins_rows | num_skipped_ref_rows");
            // System.out.println("118 | 10 | 3 | 4 | 160918");
            // System.out.println("124 | 11 | 1 | 6 | 160429");

            System.out.println(String.format("%d | %d | %d | %d | %d", snps.size(), dels.size(), subs.size(), ins.size(), refSkippedCount));

        }

    }

}
