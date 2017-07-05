package org.renci.binning.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.renci.canvas.binning.core.BinningException;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.commons.LocatedVariantFactory;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.renci.canvas.dao.var.model.VariantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import htsjdk.variant.variantcontext.Allele;
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

        List<Character> referenceChars = ref.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        List<Character> alternateChars = alt.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

        StringBuilder frontChars2Remove = new StringBuilder();
        StringBuilder backChars2Remove = new StringBuilder();

        List<Character> charsToRemoveFromRef = new ArrayList<>();

        for (int i = 0; i < referenceChars.size(); ++i) {
            if (referenceChars.get(i) != alternateChars.get(i)) {
                break;
            }
            charsToRemoveFromRef.add(referenceChars.get(i));
            frontChars2Remove.append(referenceChars.get(i));
        }

        for (int i = 0; i < charsToRemoveFromRef.size(); ++i) {
            referenceChars.remove(charsToRemoveFromRef.get(i));
        }

        if (CollectionUtils.isNotEmpty(referenceChars)) {
            Collections.reverse(referenceChars);
            Collections.reverse(alternateChars);
            for (int i = 0; i < referenceChars.size(); ++i) {
                if (referenceChars.get(i) != alternateChars.get(i)) {
                    break;
                }
                backChars2Remove.append(referenceChars.get(i));
            }
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

        LocatedVariant locatedVariant = new LocatedVariant();

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
        File vcfFile = new File("/tmp/NCX_00004", "NCX_00004.merged.rg.deduped.filtered.srd.ps.va.38.vcf");
        // File vcfFile = new File("/home/jdr0887/Downloads", "gnomad.exomes.r2.0.1.sites.vcf");

        int refSkippedCount = 0;
        int errorCount = 0;
        int filteredCount = 0;
        int noCallCount = 0;

        Map<String, List<VariantContext>> variantContext2SampleNameMap = new HashMap<String, List<VariantContext>>();

        try (VCFFileReader vcfFileReader = new VCFFileReader(vcfFile, false)) {

            VCFHeader vcfHeader = vcfFileReader.getFileHeader();
            // List<String> sampleNames = vcfHeader.getGenotypeSamples();
            //
            // for (String sampleName : sampleNames) {
            //
            // if (!variantContext2SampleNameMap.containsKey(sampleName)) {
            // variantContext2SampleNameMap.put(sampleName, new ArrayList<VariantContext>());
            // }
            //
            // variantContextLoop: for (VariantContext variantContext : vcfFileReader) {
            //
            // Allele refAllele = variantContext.getReference();
            //
            // if (refAllele.isNoCall()) {
            // noCallCount++;
            // continue;
            // }
            //
            // String altAlleles = StringUtils.join(variantContext.getAlternateAlleles().toArray());
            // if (!altAlleles.matches("[AaCcGgTt,]*")) {
            // errorCount++;
            // continue;
            // }
            //
            // GenotypesContext genotypesContext = variantContext.getGenotypes();
            //
            // for (Genotype genotype : genotypesContext) {
            //
            // if (genotype.isNoCall()) {
            // noCallCount++;
            // continue variantContextLoop;
            // }
            //
            // if (genotype.isHomRef()) {
            // refSkippedCount++;
            // continue variantContextLoop;
            // }
            // }
            //
            // variantContext2SampleNameMap.get(sampleName).add(variantContext);
            //
            // }
            //
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // }

            List<LocatedVariant> locatedVariantList = new ArrayList<>();

            // for (String sampleName : variantContext2SampleNameMap.keySet()) {
            // List<VariantContext> variantContextList = variantContext2SampleNameMap.get(sampleName);
            // if (CollectionUtils.isNotEmpty(variantContextList)) {
            // for (VariantContext variantContext : variantContextList) {

            List<VariantType> allVariantTypes = Arrays.asList(new VariantType("sub"), new VariantType("snp"), new VariantType("ins"),
                    new VariantType("del"));

            for (VariantContext variantContext : vcfFileReader) {
                for (Allele altAllele : variantContext.getAlternateAlleles()) {

                    if (variantContext.getStart() == 100155162) {
                        System.out.println("");
                    }

                    if (variantContext.getStart() == 13417) {
                        System.out.println("");
                    }

                    if (variantContext.getStart() == 1758687) {
                        System.out.println("");
                    }

                    LocatedVariant locatedVariant = LocatedVariantFactory.create(null, null, variantContext, altAllele, allVariantTypes);

                    locatedVariantList.add(locatedVariant);

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
            // snps.forEach(a -> logger.info(a.toString()));

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
