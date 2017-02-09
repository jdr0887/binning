package org.renci.binning.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.renci.canvas.dao.var.model.VariantType;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.CommonInfo;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;

public class VCFLoaderTest {

    private static final String SKIP_REF = "skip:ref";

    private static final String MULTIALLELE = "multiallele";

    private static final String SUB = "sub";

    private static final String INS = "ins";

    private static final String DEL = "del";

    private static final String SNP = "snp";

    @Test
    public void displayFilters() throws Exception {

        VCFFileReader vcfFileReader = new VCFFileReader(
                new File("/tmp",
                        "160601_UNC18-D00493_0325_BC8GP3ANXX_TAGCTT_L004.fixed-rg.deduped.realign.fixmate.recal.variant.recalibrated.filtered.vcf"),
                false);
        for (VariantContext vc : vcfFileReader) {
            Set<String> filters = vc.getFilters();

            Set<String> excludeFilters = new HashSet<>();
            // excludeFilters.add("LowQual");

            if (CollectionUtils.isNotEmpty(filters)) {
                if (CollectionUtils.containsAny(filters, excludeFilters)) {
                    continue;
                }
                System.out.println(filters.toString());
            }
        }

    }

    @Test
    public void newVCFReader() throws Exception {

        VCFFileReader vcfFileReader = new VCFFileReader(new File("/tmp", "NCG_00300R09.merged.fb.va.vcf"), false);

        VCFHeader vcfHeader = vcfFileReader.getFileHeader();
        List<String> sampleNames = vcfHeader.getGenotypeSamples();

        try (FileWriter fw = new FileWriter(new File("/tmp", "locatedVariants.new.txt")); BufferedWriter bw = new BufferedWriter(fw)) {

            int numberFiltered = 0;
            for (String sampleName : sampleNames) {

                for (VariantContext vc : vcfFileReader) {

                    Allele refAllele = vc.getReference();

                    if (refAllele.isNoCall()) {
                        continue;
                    }

                    List<Allele> altAlleleList = vc.getAlternateAlleles();

                    String altAllele = StringUtils.join(altAlleleList.toArray());
                    if (!altAllele.matches("[AaCcGgTt,]*")) {
                        numberFiltered++;
                        continue;
                    }

                    CommonInfo commonInfo = vc.getCommonInfo();

                    Double qd = commonInfo.getAttributeAsDouble("QD", 0D);
                    Double readPosRankSum = commonInfo.getAttributeAsDouble("ReadPosRankSum", 0D);
                    Integer hrun = commonInfo.getAttributeAsInt("HRun", 0);
                    Double dels = commonInfo.getAttributeAsDouble("Dels", 0D);
                    Double fs = commonInfo.getAttributeAsDouble("FS", 0D);

                    GenotypesContext genotypesContext = vc.getGenotypes();
                    if (genotypesContext.isEmpty()) {
                        continue;
                    }

                    Integer refDepth = null;
                    Integer altDepth = null;

                    // if (vc.getStart() != 21477867) {
                    // continue;
                    // }

                    for (Genotype genotype : genotypesContext) {

                        if (genotype.isNoCall()) {
                            continue;
                        }

                        String sequence = StringUtils.join(vc.getAlternateAlleles().toArray());
                        LocatedVariant locatedVariant = new LocatedVariant();
                        locatedVariant.setPosition(vc.getStart());
                        locatedVariant.setSeq(sequence);
                        locatedVariant.setRef(vc.getReference().getDisplayString());

                        if (vc.isSNP()) {
                            locatedVariant.setVariantType(new VariantType("snp"));
                            locatedVariant.setEndPosition(vc.getStart() + sequence.length());
                        } else if (vc.isIndel() && vc.isSimpleDeletion()) {
                            locatedVariant.setVariantType(new VariantType("del"));
                            locatedVariant.setEndPosition(vc.getEnd());
                        } else if (vc.isIndel() && vc.isSimpleInsertion()) {
                            locatedVariant.setVariantType(new VariantType("ins"));
                            locatedVariant.setEndPosition(vc.getStart() + sequence.length());
                        } else if (vc.isIndel() && !vc.isSimpleInsertion() && !vc.isSimpleDeletion()) {
                            locatedVariant.setVariantType(new VariantType("del"));
                            locatedVariant.setEndPosition(vc.getEnd());
                        } else {
                            locatedVariant.setVariantType(new VariantType("sub"));
                            locatedVariant.setEndPosition(vc.getStart() + sequence.length());
                        }

                        // bw.write(locatedVariant.toString());
                        bw.write(String.format("%d\t%s\t%s\t%s", vc.getStart(), locatedVariant.getVariantType().getName(),
                                vc.getReference().getDisplayString(), sequence));

                        bw.newLine();
                        bw.flush();

                        int dp = genotype.getDP();
                        if (genotype.getAD() != null) {
                            int[] ad = genotype.getAD();

                            if (ad.length == 2) {
                                refDepth = ad[0];
                                altDepth = ad[1];
                            }

                            // AssemblyLocatedVariantQC alvQC = new AssemblyLocatedVariantQC();
                            // alvQC.setLocatedVariant(locatedVariant);
                            // alvQC.setDepth(dp);
                            // alvQC.setQd(qd);
                            // alvQC.setReadPosRankSum(readPosRankSum);
                            // alvQC.setFracReadsWithDels(dels);
                            // alvQC.setHrun(hrun);
                            // alvQC.setStrandScore(fs);
                            // alvQC.setRefDepth(refDepth);
                            // alvQC.setAltDepth(altDepth);
                            // System.out.println(alvQC.toString());
                        }

                    }

                }

            }
        }
    }

    @Test
    public void oldVCFReader() throws Exception {

        try (FileReader fr = new FileReader(new File("/tmp", "NCG_00300R09.merged.fb.va.vcf"));
                LineNumberReader vcfReader = new LineNumberReader(fr)) {

            Date startTime = new Date();
            int varSetId = 0;
            int[] asmId = null;
            String[] sampleNames = null;
            String line = "";
            while ((line = vcfReader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0 || line.startsWith("#"))
                    continue;
                String[] vcfFields = line.split("\\s");
                String refnocall = vcfFields[3];
                if (!refnocall.matches("[AaCcGgTt]*"))
                    continue;
                String altnocall = vcfFields[4].equals(".") ? "" : vcfFields[4];
                if (!altnocall.matches("[AaCcGgTt,]*")) {
                    continue;
                }
                String filter = vcfFields[6];
                // if no filtervalues specified, try to load the line.

                vcfFields = line.split("\\s");
                String loadedkey = loadDataLine(vcfFields, varSetId, asmId);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String compareType(String a, String b, String samePart) {
        if (a.equals(samePart))
            return INS;
        else if (b.equals(samePart))
            return DEL;
        else if ((a.length() == b.length()) && ((a.length() - samePart.length()) == 1))
            return SNP;
        else
            return SUB;
    }

    private String getPosition(String pos, int index1, String type) {
        int posNum = Integer.parseInt(pos);
        if (type.equals(INS)) {
            posNum = posNum + index1 - 1;
        } else {
            posNum = posNum + index1;
        }
        String newPos = String.valueOf(posNum);
        return newPos;
    }

    private String getSequence(String type, String ref, String alt, String samePart, int index1, int index2) {
        char[] refArray = ref.toCharArray();
        char[] altArray = alt.toCharArray();
        String refSeq = "";
        String altSeq = "";
        String temp = "";

        if (type.equals(SNP)) {
            for (int i = index1; i <= index2; i++) {
                temp = String.valueOf(altArray[i]);
                altSeq = altSeq + temp;
            }
            return altSeq;
        } else if (type.equals(INS)) {
            for (int i = index1; i <= index2; i++) {
                temp = String.valueOf(altArray[i]);
                altSeq = altSeq + temp;
            }
            return altSeq;
        } else if (type.equals(SUB)) {
            int k = ref.length();
            int j = alt.length();
            int index3 = index2 - k + j;
            if (k > j) {
                for (int i = index1; i <= index3; i++) {
                    temp = String.valueOf(altArray[i]);
                    altSeq = altSeq + temp;
                }
                return altSeq;
            } else {
                for (int i = index1; i <= index2; i++) {
                    temp = String.valueOf(altArray[i]);
                    altSeq = altSeq + temp;
                }
                return altSeq;
            }

        } else {
            for (int i = index1; i <= index2; i++) {
                temp = String.valueOf(refArray[i]);
                refSeq = refSeq + temp;
            }
            return refSeq;
        }
    }

    private String getEndPosition(String type, String pos, int index1, int index2, String ref, String alt) {
        int posNum = Integer.parseInt(pos);
        if (type.equals(SNP)) {
            posNum = posNum + index1 + 1;
        } else if (type.equals(INS)) {
            posNum = posNum + index1;
        } else if (type.equals(SUB)) {
            int k = ref.length();
            int j = alt.length();
            if (k > j) {
                posNum = posNum + index2 + 1;
            } else {
                posNum = posNum + k - j + index2 + 1;
            }
        } else {
            posNum = posNum + index2 + 1;
        }
        String newPos = String.valueOf(posNum);
        return newPos;
    }

    private String[] getSampleNames(String[] headers) {
        return Arrays.copyOfRange(headers, 9, headers.length);
    }

    private Map<String, Integer> getIndexMap(String[] a) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int x = 0; x < a.length; x++)
            map.put(a[x], x);
        return map;
    }

    private Map<String, String> infoMap(String[] a) {
        Map<String, String> map = new HashMap<String, String>();
        for (int x = 0; x < a.length; x++) {
            String[] temp = a[x].split("=");
            if (temp.length == 2)
                map.put(temp[0], temp[1]);
        }
        return map;
    }

    public String loadDataLine(String[] vcfFields, int varSetId, int[] asmId) throws CANVASDAOException, BinningException {

        String ref = vcfFields[3];
        // if (!ref.matches("[AaCcGgTt]*"))
        // return;

        String a = vcfFields[4].equals(".") ? "" : vcfFields[4];
        // if (!a.matches("[AaCcGgTt,]*"))
        // return;
        String[] alts = a.split(",");

        Map<String, Integer> fieldIndex = getIndexMap(vcfFields[8].split(":"));
        // System.out.println("*** " + fieldIndex);

        Map<String, String> fieldInfo = infoMap(vcfFields[7].split(";"));
        Float qd = (fieldInfo.get("QD") != null) ? Float.parseFloat(fieldInfo.get("QD")) : null;
        Float read_pos_rank_sum = (fieldInfo.get("ReadPosRankSum") != null) ? Float.parseFloat(fieldInfo.get("ReadPosRankSum")) : null;
        Integer hrun = (fieldInfo.get("HRun") != null) ? Integer.parseInt(fieldInfo.get("HRun")) : null;
        Float frac_reads_with_dels = (fieldInfo.get("Dels") != null) ? Float.parseFloat(fieldInfo.get("Dels")) : null;
        Float strand_score = (fieldInfo.get("FS") != null) ? Float.parseFloat(fieldInfo.get("FS")) : null;

        // the point of this is so that in a multi-sample vcf, we don't have to keep hitting the db
        // to get the loc_var_id.X
        Map<Integer, Integer> altIndex_2_locvarid = new HashMap<Integer, Integer>();
        String loadedtype = SKIP_REF;
        for (int vcfColumn = 9; vcfColumn < vcfFields.length; vcfColumn++) {
            int sampleColumn = vcfColumn - 9;
            String[] sampleField = vcfFields[vcfColumn].split(":");

            Integer gtIndex = fieldIndex.get("GT");
            String[] gt = sampleField[gtIndex].split("[/|]");
            // System.out.println("-->" + Arrays.toString(gt));
            // get homozygous
            Boolean homozygous = Boolean.FALSE;
            if (gt[0].equals(".") || ((gt.length == 2) && (gt[1].equals(".")))) {
                continue;
            }
            if (gt.length == 2 && (gt[0].equals(gt[1]))) {
                homozygous = Boolean.TRUE;
                gt[0] = "0";
            }

            Integer dpIndex = fieldIndex.get("DP");
            Integer depth = (dpIndex != null && !sampleField[dpIndex].equals(".")) ? Integer.parseInt(sampleField[dpIndex]) : null;
            Integer adIndex = fieldIndex.get("AD");
            Integer ref_depth;
            Integer alt_depth;

            Integer gqIndex = fieldIndex.get("GQ");
            float gq = (gqIndex != null && !sampleField[gqIndex].equals(".")) ? Float.parseFloat(sampleField[gqIndex]) : -1;

            // insertAsmLoc(asmId[sampleColumn], refVerAccession, pos, homozygous, gq);

            for (int allele = 0; allele < gt.length; allele++) {
                int gtAllele = Integer.parseInt(gt[allele]);
                if (gtAllele == 0)
                    continue;
                if (adIndex != null) {
                    String[] ad = sampleField[adIndex].split(",");
                    ref_depth = (ad[0] != null && !ad[0].equals(".")) ? Integer.parseInt(ad[0]) : null;
                    alt_depth = (ad[gtAllele] != null && !ad[gtAllele].equals(".")) ? Integer.parseInt(ad[gtAllele]) : null;
                } else {
                    ref_depth = null;
                    alt_depth = null;
                }
                int altIndex = gtAllele - 1;
                String alt = alts[altIndex];

                if (alt.equals(ref))
                    throw new BinningException("ERROR:  ALT value \"" + alt + "\" matches REF in line ");
                if (!ref.equals(alts[altIndex])) {
                    String samePart = "";
                    List<String> list = search(vcfFields[3], alts[altIndex]);
                    for (int h = 0; h < list.size() - 2; h++) {
                        String str = list.get(h);
                        samePart = samePart + str;
                    }
                    String indexS1 = list.get(list.size() - 2);
                    String indexS2 = list.get(list.size() - 1);
                    int index1 = Integer.parseInt(indexS1);
                    int index2 = Integer.parseInt(indexS2);
                    String type = compareType(vcfFields[3], alts[altIndex], samePart);
                    // get startPos
                    String sstartPos = getPosition(vcfFields[1], index1, type);
                    int startPos = Integer.parseInt(sstartPos);
                    // get sequence
                    String sequence = getSequence(type, vcfFields[3], alts[altIndex], samePart, index1, index2);
                    // get end_position
                    String sendPos = getEndPosition(type, vcfFields[1], index1, index2, vcfFields[3], alts[altIndex]);
                    int endPos = Integer.parseInt(sendPos);
                    if (!altIndex_2_locvarid.containsKey(altIndex)) {

                        LocatedVariant example = new LocatedVariant();
                        example.setId(1L);
                        example.setEndPosition(endPos);
                        example.setPosition(startPos);
                        example.setSeq(sequence);
                        example.setRef(ref);
                        example.setVariantType(new VariantType(type));
                        // if (type.equals("sub")) {
                        // System.out.println(type);
                        // System.out.println(example.toString());
                        // }

                        System.out.println(String.format("%d\t%s\t%s\t%s", startPos, type, ref, alt));

                        // List<VariantType> foundTypes = daoMgr.getDAOBean().getVariantTypeDAO().findByName(type);
                        // if (CollectionUtils.isNotEmpty(foundTypes)) {
                        // example.setType(foundTypes.get(0));
                        // }

                        // List<LocatedVariant> foundLocatedVariants =
                        // daoMgr.getDAOBean().getLocatedVariantDAO().findByExample(example);
                        //
                        // if (CollectionUtils.isEmpty(foundLocatedVariants)) {
                        // example.setId(daoMgr.getDAOBean().getLocatedVariantDAO().save(example));
                        // } else {
                        // example = foundLocatedVariants.get(0);
                        // }
                        altIndex_2_locvarid.put(altIndex, example.getId().intValue());
                    }
                    int variant_id = altIndex_2_locvarid.get(altIndex);

                    // Assembly assembly = daoMgr.getDAOBean().getAssemblyDAO().findById(asmId[sampleColumn]);
                    // LocatedVariant locatedVariant =
                    // daoMgr.getDAOBean().getLocatedVariantDAO().findById(Long.valueOf(variant_id));
                    // AssemblyLocatedVariant alv = new AssemblyLocatedVariant();
                    // alv.setAssembly(assembly);
                    // alv.setLocatedVariant(locatedVariant);
                    // alv.setGenotypeQual(Double.valueOf(gq));
                    // alv.setHomozygous(homozygous);
                    // daoMgr.getDAOBean().getAssemblyLocatedVariantDAO().save(alv);

                    if (depth != null && qd != null && read_pos_rank_sum != null && frac_reads_with_dels != null && strand_score != null
                            && hrun != null && ref_depth != null && alt_depth != null) {

                        AssemblyLocatedVariantQC alvQC = new AssemblyLocatedVariantQC();
                        // alvQC.setAssembly(assembly);
                        // alvQC.setLocatedVariant(locatedVariant);

                        alvQC.setDepth(depth);
                        alvQC.setQualityByDepth(qd.doubleValue());
                        alvQC.setReadPosRankSum(read_pos_rank_sum.doubleValue());
                        alvQC.setFracReadsWithDels(frac_reads_with_dels.doubleValue());
                        alvQC.setHomopolymerRun(hrun);
                        alvQC.setStrandScore(strand_score.doubleValue());
                        alvQC.setRefDepth(ref_depth);
                        alvQC.setAltDepth(alt_depth);
                        // System.out.println(alvQC.toString());
                        // daoMgr.getDAOBean().getAssemblyLocatedVariantQCDAO().save(alvQC);

                    }

                    if (loadedtype.equals(SKIP_REF)) {
                        loadedtype = type;
                    } else if (!loadedtype.equals(type)) {
                        loadedtype = MULTIALLELE;
                    }
                }
            }

        }
        return loadedtype;
    }

    private List<String> search(String a, String b) {
        String temp = "";
        int index1 = 0;
        int index2 = 0;
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();

        if (a.length() < b.length()) {
            temp = a;
            a = b;
            b = temp;
        }
        int num = a.length() - b.length();
        for (int n = 1; n <= num; n++) {
            b = b + ".";
        }
        char[] array1 = a.toCharArray();
        char[] array2 = b.toCharArray();

        for (int j = 0; j <= a.length() - 1; j++) {

            if (array1[j] == array2[j]) {
                temp = String.valueOf(array1[j]);
                list1.add(temp);
            } else {
                index1 = j;
                break;
            }
        }
        if (a.length() - num == index1) {
            index2 = a.length() - 1;
        } else {
            for (int i = a.length() - 1; i - num >= index1; i--) {
                if (array1[i] == array2[i - num]) {
                    temp = String.valueOf(array1[i]);
                    list2.add(temp);
                    index2 = i - 1;
                } else {
                    index2 = i;
                    break;
                }
            }
        }
        for (int k = list2.size() - 1; k >= 0; k--) {
            temp = list2.get(k);
            list1.add(temp);
        }
        String S1 = String.valueOf(index1);
        String S2 = String.valueOf(index2);
        list1.add(S1);
        list1.add(S2);
        return list1;
    }

    @Test
    public void scratch() throws Exception {

        List<String> contigList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
                "18", "19", "20", "21", "22", "X", "Y");

        Map<String, Integer> countMap = new HashMap<String, Integer>();
        for (String contig : contigList) {
            countMap.put(contig, 0);
        }
        try (FileReader fr = new FileReader(
                new File("/tmp", "CEU.wgs.consensus.20131118.snps_indels.high_coverage_pcr_free_v2.genotypes.vcf.orig"));
                BufferedReader br = new BufferedReader(fr);
                FileWriter fw = new FileWriter(
                        new File("/tmp", "CEU.wgs.consensus.20131118.snps_indels.high_coverage_pcr_free_v2.genotypes.vcf"));
                BufferedWriter bw = new BufferedWriter(fw)) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.startsWith("#")) {
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                    continue;
                }

                String contig = line.split("\t")[0];
                countMap.put(contig, countMap.get(contig) + 1);

                if (countMap.get(contig) > 10000) {
                    continue;
                }

                bw.write(line);
                bw.newLine();
                bw.flush();

            }

        }

    }

}
