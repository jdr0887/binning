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
import org.apache.commons.io.output.ByteArrayOutputStream;
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
        File chainFile = new File("/home/jdr0887/Downloads", "hg19ToHg38.over.chain.gz");
        LiftOver liftOver = new LiftOver(chainFile);

        Interval ret = liftOver.liftOver(new Interval("chr1", 16776377, 16776452));
        System.out.println(ret.toString());

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
