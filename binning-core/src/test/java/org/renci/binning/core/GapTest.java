package org.renci.binning.core;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.renci.canvas.binning.core.CIGARType;

public class GapTest {

    @Test
    public void test1() {
        // transcr_start | transcr_end | contig_start | contig_end | gap
        // 900 | 4735 | 52840610 | 52836756 | M1549 D2 M252 I1 M805 D18 M1229

        Range<Integer> exonTranscriptRange = Range.between(900, 4735);
        Range<Integer> exonContigRange = Range.between(52836756, 52840610);

        // one based
        Pair<AtomicInteger, AtomicInteger> currentRefReadPair = Pair.of(new AtomicInteger(exonContigRange.getMinimum() - 1),
                new AtomicInteger(exonTranscriptRange.getMinimum() - 1));

        List<Pair<CIGARType, Integer>> blockList = parseGap("M1549 D2 M252 I1 M805 D18 M1229");
        for (Pair<CIGARType, Integer> block : blockList) {
            System.out.println(block.toString());
            for (int i = 0; i < block.getRight(); i++) {
                switch (block.getLeft()) {
                    case MATCH:
                        currentRefReadPair.getLeft().incrementAndGet();
                        currentRefReadPair.getRight().incrementAndGet();
                        break;
                    case INSERT:
                        currentRefReadPair.getRight().incrementAndGet();
                        break;
                    case DELETION:
                        currentRefReadPair.getLeft().incrementAndGet();
                        break;
                }
                System.out
                        .println(String.format("i: %s, ref: %s, read: %s", i, currentRefReadPair.getLeft(), currentRefReadPair.getRight()));
            }
        }

    }

    @Test
    public void test2() {
        // transcr_start | transcr_end | contig_start | contig_end | gap
        // 6155 | 18409 | 40479053 | 40491305 | M6352 I2 M5901

        Long locatedVariantPosition = 40484948L;

        Range<Integer> exonTranscriptRange = Range.between(6155, 18409);
        Range<Integer> exonContigRange = Range.between(40479053, 40491305);

        // one based
        Pair<AtomicInteger, AtomicInteger> currentRefReadPair = Pair.of(new AtomicInteger(exonContigRange.getMinimum() - 1),
                new AtomicInteger(exonTranscriptRange.getMinimum() - 1));

        List<Pair<CIGARType, Integer>> blockList = parseGap("M6352 I2 M5901");
        blockLoop: for (Pair<CIGARType, Integer> block : blockList) {
            System.out.println(block.toString());
            for (int i = 0; i < block.getRight(); i++) {
                switch (block.getLeft()) {
                    case MATCH:
                        currentRefReadPair.getLeft().incrementAndGet();
                        currentRefReadPair.getRight().incrementAndGet();
                        break;
                    case INSERT:
                        currentRefReadPair.getRight().incrementAndGet();
                        break;
                    case DELETION:
                        currentRefReadPair.getLeft().incrementAndGet();
                        break;
                }

                System.out
                        .println(String.format("i: %s, ref: %s, read: %s", i, currentRefReadPair.getLeft(), currentRefReadPair.getRight()));

                if (locatedVariantPosition.intValue() == currentRefReadPair.getLeft().get()) {
                    break blockLoop;
                }
            }

        }

        Integer transcriptPosition = currentRefReadPair.getRight().get();
        System.out.println(String.format("transcriptPosition: %s", transcriptPosition));
        assertTrue(transcriptPosition.intValue() == 12050);

    }

    private List<Pair<CIGARType, Integer>> parseGap(String gap) {
        List<Pair<CIGARType, Integer>> ret = new LinkedList<>();
        String[] gapTokens = gap.split(" ");
        for (String gapToken : gapTokens) {
            try {
                String typeValue = gapToken.substring(0, 1);
                String length = gapToken.substring(1, gapToken.length());
                CIGARType type = Arrays.asList(CIGARType.values()).stream().filter(a -> a.getName().equals(typeValue)).findFirst()
                        .orElse(null);
                ret.add(Pair.of(type, Integer.valueOf(length)));
            } catch (NumberFormatException e) {
                // don't care about NFE
            }
        }
        return ret;
    }

}
