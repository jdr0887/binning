package org.renci.binning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.renci.binning.GATKDepthInterval;
import org.renci.binning.SAMToolsDepthInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadCoverageGSCallableTest {

    private static final Logger logger = LoggerFactory.getLogger(LoadCoverageGSCallableTest.class);

    @Test
    public void test() throws IOException {

        File allIntervalsFile = new File("/tmp/Intervals", String.format("allintervals.v%d.txt", 40));
        List<String> allIntervals = FileUtils.readLines(allIntervalsFile);
        if (allIntervals.contains("Targets")) {
            allIntervals.remove("Targets");
        }
        SortedSet<GATKDepthInterval> allIntervalSet = new TreeSet<GATKDepthInterval>();
        allIntervals.forEach(a -> allIntervalSet.add(new GATKDepthInterval(a)));

        File depthFile = new File("/tmp", "GSU_000136.merged.depth.txt");

        File convertedDepthFile = convertDepthFile(allIntervalSet, depthFile);
        logger.info(convertedDepthFile.getAbsolutePath());
    }

    private File convertDepthFile(SortedSet<GATKDepthInterval> allIntervalSet, File depthFile) {

        List<SAMToolsDepthInterval> samtoolsDepthIntervals = new ArrayList<>();

        try (FileReader fr = new FileReader(depthFile); BufferedReader br = new BufferedReader(fr)) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                samtoolsDepthIntervals.add(new SAMToolsDepthInterval(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ExecutorService es = Executors.newFixedThreadPool(4);
            for (GATKDepthInterval gatkDepthInterval : allIntervalSet) {

                es.submit(() -> {
                    Map<Integer, Integer> percentageMap = new HashMap<Integer, Integer>();

                    percentageMap.put(1, 0);
                    percentageMap.put(2, 0);
                    percentageMap.put(5, 0);
                    percentageMap.put(8, 0);
                    percentageMap.put(10, 0);
                    percentageMap.put(15, 0);
                    percentageMap.put(20, 0);
                    percentageMap.put(30, 0);
                    percentageMap.put(50, 0);

                    int total = 0;
                    for (SAMToolsDepthInterval samtoolsDepthInterval : samtoolsDepthIntervals) {
                        if (!gatkDepthInterval.getContig().equals(samtoolsDepthInterval.getContig())) {
                            continue;
                        }
                        if (!gatkDepthInterval.getPositionRange().contains(samtoolsDepthInterval.getPosition())) {
                            continue;
                        }

                        total += samtoolsDepthInterval.getCoverage();

                        for (Integer key : percentageMap.keySet()) {
                            if (samtoolsDepthInterval.getCoverage() >= key) {
                                percentageMap.put(key, percentageMap.get(key) + 1);
                            }
                        }

                    }
                    gatkDepthInterval.setTotalCoverage(total);
                    gatkDepthInterval
                            .setAverageCoverage(Double.valueOf(1D * gatkDepthInterval.getTotalCoverage() / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove1(Double.valueOf(100D * percentageMap.get(1) / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove2(Double.valueOf(100D * percentageMap.get(2) / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove5(Double.valueOf(100D * percentageMap.get(5) / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove8(Double.valueOf(100D * percentageMap.get(8) / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove10(Double.valueOf(100D * percentageMap.get(10) / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove15(Double.valueOf(100D * percentageMap.get(15) / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove20(Double.valueOf(100D * percentageMap.get(20) / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove30(Double.valueOf(100D * percentageMap.get(30) / gatkDepthInterval.getLength()));
                    gatkDepthInterval.setSamplePercentAbove50(Double.valueOf(100D * percentageMap.get(50) / gatkDepthInterval.getLength()));

                });
            }
            es.shutdown();
            es.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        File ret = new File(depthFile.getParentFile(), depthFile.getName().replace(".txt", ".txt.converted"));
        try (FileWriter fw = new FileWriter(ret); BufferedWriter bw = new BufferedWriter(fw)) {
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

        return ret;
    }

}
