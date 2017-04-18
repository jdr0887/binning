package org.renci.binning.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.renci.canvas.binning.core.GATKDepthInterval;
import org.renci.canvas.binning.core.SAMToolsDepthInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadCoverageGSCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(LoadCoverageGSCallableTest.class);

    @Test
    public void test() throws IOException {

        File allIntervalsFile = new File("/tmp/Intervals", String.format("allintervals.v%d.txt", 43));
        List<String> allIntervals = FileUtils.readLines(allIntervalsFile, "UTF-8");
        if (allIntervals.contains("Targets")) {
            allIntervals.remove("Targets");
        }
        SortedSet<GATKDepthInterval> allIntervalSet = new TreeSet<GATKDepthInterval>();
        allIntervals.forEach(a -> allIntervalSet.add(new GATKDepthInterval(a)));

        logger.info("done parsing allIntervals");

        File depthFile = new File("/tmp", "NCX_00101.merged.rg.deduped.depth.txt");
        // File depthFile = new File("/tmp", "GSU_000384.merged.depth.txt");

        ExecutorService es = Executors.newFixedThreadPool(8);

        try (FileReader fr = new FileReader(depthFile);
                BufferedReader br = new BufferedReader(fr, Double.valueOf(Math.pow(2, 14)).intValue())) {
            String line;
            while ((line = br.readLine()) != null) {

                SAMToolsDepthInterval samtoolsDepthInterval = new SAMToolsDepthInterval(line);

                es.submit(() -> {

                    Optional<GATKDepthInterval> optionalGATKDepthInterval = allIntervalSet.stream()
                            .filter(a -> a.getContig().equals(samtoolsDepthInterval.getContig())
                                    && a.getPositionRange().contains(samtoolsDepthInterval.getPosition()))
                            .findFirst();
                    if (optionalGATKDepthInterval.isPresent()) {
                        GATKDepthInterval gatkDepthInterval = optionalGATKDepthInterval.get();

                        gatkDepthInterval.getTotalCoverage().addAndGet(samtoolsDepthInterval.getCoverage());

                        if (samtoolsDepthInterval.getCoverage() >= 1) {
                            gatkDepthInterval.getSampleCountAbove1().incrementAndGet();
                        }

                        if (samtoolsDepthInterval.getCoverage() >= 2) {
                            gatkDepthInterval.getSampleCountAbove2().incrementAndGet();
                        }

                        if (samtoolsDepthInterval.getCoverage() >= 5) {
                            gatkDepthInterval.getSampleCountAbove5().incrementAndGet();
                        }

                        if (samtoolsDepthInterval.getCoverage() >= 8) {
                            gatkDepthInterval.getSampleCountAbove8().incrementAndGet();
                        }

                        if (samtoolsDepthInterval.getCoverage() >= 10) {
                            gatkDepthInterval.getSampleCountAbove10().incrementAndGet();
                        }

                        if (samtoolsDepthInterval.getCoverage() >= 15) {
                            gatkDepthInterval.getSampleCountAbove15().incrementAndGet();
                        }

                        if (samtoolsDepthInterval.getCoverage() >= 20) {
                            gatkDepthInterval.getSampleCountAbove20().incrementAndGet();
                        }

                        if (samtoolsDepthInterval.getCoverage() >= 30) {
                            gatkDepthInterval.getSampleCountAbove30().incrementAndGet();
                        }

                        if (samtoolsDepthInterval.getCoverage() >= 50) {
                            gatkDepthInterval.getSampleCountAbove50().incrementAndGet();
                        }

                    }

                });

            }
            es.shutdown();
            es.awaitTermination(1L, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("writing output");
        File convertedDepthFile = new File(depthFile.getParentFile(), depthFile.getName().replace(".txt", ".txt.converted"));
        try (FileWriter fw = new FileWriter(convertedDepthFile); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("Target\ttotal_coverage\taverage_coverage");

            Arrays.asList(1, 2, 5, 8, 10, 15, 20, 30, 50).forEach(a -> {
                try {
                    bw.write(String.format("\tSample_%%_above_%d", a));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bw.newLine();
            bw.flush();

            for (GATKDepthInterval gatkDepthInterval : allIntervalSet) {
                fw.write(gatkDepthInterval.toStringTrimmed());
                bw.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info(convertedDepthFile.getAbsolutePath());
    }

    @Test
    public void parseAlreadyFormatConvertedDepthFile() {
        SortedSet<GATKDepthInterval> allIntervalSet = new TreeSet<GATKDepthInterval>();
        File depthFile = new File("/tmp", "GSU_000384.merged.depth.converted.txt");
        try (Stream<String> stream = Files.lines(depthFile.toPath())) {
            stream.forEach(a -> {
                if (!a.startsWith("Target")) {
                    allIntervalSet.add(new GATKDepthInterval(a));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("done");
    }

}
