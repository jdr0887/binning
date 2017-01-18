package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import htsjdk.samtools.liftover.LiftOver;
import htsjdk.samtools.util.Interval;

public class Scratch {

    private static final Pattern targetPattern = Pattern.compile("(?<chromosome>.+):(?<start>\\d+)-?(?<end>\\d+)?");

    @Test
    public void testIntervalDisjunction() throws IOException {

        File allIntervalsFile = new File(String.format("%s/allintervals.v%d.txt", "/tmp", 39));
        List<String> allIntervals = FileUtils.readLines(allIntervalsFile);
        allIntervals.remove("Targets");

        List<String> foundIntervals = new ArrayList<>();
        try (Reader in = new FileReader(new File("/tmp",
                "160601_UNC18-D00493_0325_BC8GP3ANXX_GGCTAC_L004.fixed-rg.deduped.realign.fixmate.recal.coverage.v39.gene.sample_interval_summary.original"))) {
            Iterable<CSVRecord> records = CSVFormat.TDF.parse(in);
            for (CSVRecord record : records) {
                String target = record.get(0);
                if (target.contains("-") || record.getRecordNumber() == 1) {
                    foundIntervals.add(target);
                    continue;
                }
                foundIntervals.add(String.format("%s-%s", target, target.split(":")[1]));
            }
        }
        foundIntervals.remove("Target");

        Collection<String> missingIntervals = CollectionUtils.disjunction(allIntervals, foundIntervals);
        System.out.println(missingIntervals.size());
    }

    @Test
    public void testIntervalReintegration() throws IOException, BinningException {

        File allIntervalsFile = new File("/tmp", String.format("allintervals.v%d.txt", 39));
        List<String> allIntervals = FileUtils.readLines(allIntervalsFile);
        allIntervals.remove("Targets");
        File bamFile = new File("/tmp", "140912_UNC17-D00216_0247_BC4G46ANXX_ACAGTG_L004.fixed-rg.deduped.realign.fixmate.recal.bam");

        File sampleIntervalSummaryFile = new File("/tmp",
                "140912_UNC17-D00216_0247_BC4G46ANXX_ACAGTG_L004.fixed-rg.deduped.realign.fixmate.recal.coverage.v39.gene.sample_interval_summary");
        List<String> foundIntervals = new ArrayList<>();
        try (Reader in = new FileReader(sampleIntervalSummaryFile)) {
            Iterable<CSVRecord> records = CSVFormat.TDF.parse(in);
            for (CSVRecord record : records) {
                String target = record.get(0);
                if (target.contains("-") || record.getRecordNumber() == 1) {
                    foundIntervals.add(target);
                    continue;
                }
                foundIntervals.add(String.format("%s-%s", target, target.split(":")[1]));
            }
        }
        foundIntervals.remove("Target");

        Collection<String> missingIntervals = CollectionUtils.disjunction(allIntervals, foundIntervals);
        System.out.println(missingIntervals.size());

        if (CollectionUtils.isNotEmpty(missingIntervals)) {
            File allMissingIntervalsFile = new File("/tmp", String.format("allm.v%d.interval_list", 39));

            for (String missingInterval : missingIntervals) {
                // FileUtils.write(allMissingIntervalsFile, String.format("%s%n", missingInterval), true);
                // File missingSampleIntervalSummaryFile = runGATKDepthOfCoverageJob(bamFile, 39);
                File missingSampleIntervalSummaryFile = new File("/tmp", "missing.v39.sample_interval_summary");

                // reintegrate output
                List<String> missingSampleIntervalSummaryFileLines = FileUtils.readLines(missingSampleIntervalSummaryFile);

                if (missingSampleIntervalSummaryFileLines.size() == 1) {
                    // only contains header...missing results?
                    throw new BinningException("Missing coverage data");
                }

                if (missingSampleIntervalSummaryFileLines.size() == 2) {
                    // remove header
                    String lineToInject = missingSampleIntervalSummaryFileLines.get(1);

                    SortedSet<GATKDepthInterval> intervalSet = new TreeSet<GATKDepthInterval>();
                    intervalSet.add(new GATKDepthInterval(lineToInject));

                    File destFile = new File(sampleIntervalSummaryFile.getParentFile(),
                            sampleIntervalSummaryFile.getName().replace(".sample_interval_summary", ".sample_interval_summary.orig"));
                    FileUtils.moveFile(sampleIntervalSummaryFile, destFile);

                    try (FileReader fr = new FileReader(destFile); BufferedReader br = new BufferedReader(fr)) {
                        // skip header
                        br.readLine();
                        String line;
                        while ((line = br.readLine()) != null) {
                            intervalSet.add(new GATKDepthInterval(line));
                        }

                    }

                    try (FileWriter fw = new FileWriter(sampleIntervalSummaryFile); BufferedWriter bw = new BufferedWriter(fw)) {
                        bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", "Target",
                                "total_coverage", "average_coverage", "NCG_00579_total_cvg", "NCG_00579_mean_cvg", "NCG_00579_granular_Q1",
                                "NCG_00579_granular_median", "NCG_00579_granular_Q3", "NCG_00579_%_above_1", "NCG_00579_%_above_2",
                                "NCG_00579_%_above_5", "NCG_00579_%_above_8", "NCG_00579_%_above_10", "NCG_00579_%_above_15",
                                "NCG_00579_%_above_20", "NCG_00579_%_above_30", "NCG_00579_%_above_50"));
                        bw.flush();

                        for (GATKDepthInterval interval : intervalSet) {
                            bw.write(interval.toString());
                            bw.flush();
                        }
                    }

                }

            }
        }

    }

    @Test
    public void scratch() {
        // Double d = 0.01 * Double.valueOf("69.6");
        // System.out.println(d.toString());

        String bamFile = "/storage/binning/annotation/ncgenes/NCG_00579/140912_UNC17-D00216_0247_BC4G46ANXX_ACAGTG_L004.fixed-rg.deduped.realign.fixmate.recal.bam";
        String referenceSequence = "/projects/mapseq/data/references/BUILD.37.1/bwa061sam0118/BUILD.37.1.sorted.shortid.fa";
        String command = String.format(
                "$JAVA7_HOME/bin/java -Xmx4g -jar $GATK_HOME/GenomeAnalysisTK.jar -T DepthOfCoverage -I %1$s -L m.v%2$s.interval_list -o missing.v%2$s -R %3$s -im OVERLAPPING_ONLY -omitLocusTable -omitBaseOutput -omitSampleSummary -ct 1 -ct 2 -ct 5 -ct 8 -ct 10 -ct 15 -ct 20 -ct 30 -ct 50",
                bamFile, 39, referenceSequence);
        System.out.println(command);
    }

    @Test
    public void testLiftOver() {
        File chainFile = new File("/home/jdr0887/workspace/renci/canvas/binning/binning-server/src/main/resources/data/liftOver",
                "hg19ToHg38.over.chain.gz");
        LiftOver liftOver = new LiftOver(chainFile);

        Interval ret = liftOver.liftOver(new Interval("chr1", 16776377, 16776452));
        System.out.println(ret.toString());

    }

    @Test
    public void convert37BedTo38Bed() {
        File chainFile = new File("/home/jdr0887/workspace/renci/canvas/binning/binning-server/src/main/resources/data/liftOver",
                "hg19ToHg38.over.chain.gz");
        LiftOver liftOver = new LiftOver(chainFile);

        List<File> inputs = Arrays.asList(new File("/tmp", "agilent_v6_capture_region_pm_100.shortid.bed"),
                new File("/tmp", "agilent_v5_capture_region_pm_100.shortid.bed"),
                new File("/tmp", "agilent_v5_egl_capture_region_pm_100.shortid.bed"));

        for (File input : inputs) {

            File output = new File(input.getParentFile(), input.getName().replace(".bed", ".38.bed"));

            try (FileReader fr = new FileReader(input);
                    BufferedReader br = new BufferedReader(fr);
                    FileWriter fw = new FileWriter(output);
                    BufferedWriter bw = new BufferedWriter(fw)) {

                String line;
                while ((line = br.readLine()) != null) {
                    List<String> row = Arrays.asList(line.split("\t"));

                    String contig = row.get(0).replace("NC_", "");
                    contig = contig.substring(0, contig.indexOf("."));

                    String chromosome = contig;
                    if ("000023".equals(chromosome)) {
                        chromosome = "X";
                    } else if ("000024".equals(chromosome)) {
                        chromosome = "Y";
                    } else {
                        chromosome = Integer.valueOf(contig).toString();
                    }

                    String version = row.get(0);
                    version = version.substring(version.indexOf(".") + 1, version.length());
                    Integer ver = Integer.valueOf(version);
                    ver++;

                    Interval convertedRow = liftOver.liftOver(
                            new Interval(String.format("chr%s", chromosome), Integer.valueOf(row.get(1)), Integer.valueOf(row.get(2))));

                    if (convertedRow != null) {
                        bw.write(String.format("NC_%06d.%2$d\t%3$d\t%4$d", Integer.valueOf(contig), ver, convertedRow.getStart(),
                                convertedRow.getEnd()));
                        bw.newLine();
                        bw.flush();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Test
    public void convertAnnotation37BedTo38Bed() {
        File chainFile = new File("/home/jdr0887/workspace/renci/canvas/binning/binning-server/src/main/resources/data/liftOver",
                "hg19ToHg38.over.chain.gz");
        LiftOver liftOver = new LiftOver(chainFile);

        Collection<File> files = FileUtils.listFiles(new File("/tmp/NCNEXUS38"), FileFilterUtils.suffixFileFilter("bed"),
                TrueFileFilter.INSTANCE);
        // files.forEach(a -> System.out.println(a.getAbsolutePath()));
        System.out.println(files.size());
        for (File input : files) {

            File output = new File(input.getParentFile(), input.getName().replace(".bed", ".38.bed"));

            try (FileReader fr = new FileReader(input);
                    BufferedReader br = new BufferedReader(fr);
                    FileWriter fw = new FileWriter(output);
                    BufferedWriter bw = new BufferedWriter(fw)) {

                String line;
                while ((line = br.readLine()) != null) {
                    List<String> row = Arrays.asList(line.split("\t"));

                    String contig = row.get(0).replace("NC_", "");
                    contig = contig.substring(0, contig.indexOf("."));
                    String chromosome = contig;
                    if ("000023".equals(chromosome)) {
                        chromosome = "X";
                    } else if ("000024".equals(chromosome)) {
                        chromosome = "Y";
                    } else {
                        chromosome = Integer.valueOf(contig).toString();
                    }

                    String version = row.get(0);
                    version = version.substring(version.indexOf(".") + 1, version.length());
                    Integer ver = Integer.valueOf(version);
                    ver++;

                    Interval convertedRow = liftOver.liftOver(
                            new Interval(String.format("chr%s", chromosome), Integer.valueOf(row.get(1)), Integer.valueOf(row.get(2))));

                    if (convertedRow != null) {
                        bw.write(String.format("NC_%06d.%2$d\t%3$d\t%4$d", Integer.valueOf(contig), ver, convertedRow.getStart(),
                                convertedRow.getEnd()));
                        bw.newLine();
                        bw.flush();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            input.delete();
            output.renameTo(input);

        }

    }

    @Test
    public void testCLI() {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            String line = String.format("/bin/echo %s", "monkeyfart");
            DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(baos);
            pumpStreamHandler.setStopTimeout(5L);
            executor.setStreamHandler(pumpStreamHandler);
            int exitValue = executor.execute(CommandLine.parse(line));
            System.out.println(baos.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testTargetPattern() {
        Pattern pattern = Pattern.compile("(?<name>.+):(?<start>\\d+)-?(?<end>\\d+)?");
        Matcher matcher = pattern.matcher("NC_000001.10:955551-955755");
        assertTrue(matcher.matches());
        assertTrue(matcher.group("name").equals("NC_000001.10"));
        assertTrue(matcher.group("start").equals("955551"));
        assertTrue(matcher.group("end").equals("955755"));
    }

    @Test
    public void testParseGeneIntervalSummary() {

        try {
            File geneSampleIntervalSummaryFile = new File("/tmp",
                    "160105_UNC16-SN851_0640_AHGKYJBCXX_AAAGCA_L002.fixed-rg.deduped.realign.fixmate.recal.coverage.v39.gene.sample_interval_summary");
            Reader in = new FileReader(geneSampleIntervalSummaryFile);
            Iterable<CSVRecord> records = CSVFormat.TDF.parse(in);
            for (CSVRecord record : records) {
                String target = record.get(0);
                System.out.println(target);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
