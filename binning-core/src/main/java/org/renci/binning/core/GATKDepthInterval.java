package org.renci.binning.core;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

public class GATKDepthInterval implements Serializable, Comparable<GATKDepthInterval> {

    private static final long serialVersionUID = 7645760450620588143L;

    private static final Pattern targetPattern = Pattern.compile("(?<contig>.+):(?<start>\\d+)-?(?<end>\\d+)?");

    private String contig;

    private Integer startPosition;

    private Integer endPosition;

    private Integer totalCoverage;

    private Double averageCoverage;

    private Integer sampleTotalCoverage;

    private Double sampleMeanCoverage;

    private String sampleGranularQ1;

    private String sampleGranularMedian;

    private String sampleGranularQ3;

    private Double samplePercentAbove1;

    private Double samplePercentAbove2;

    private Double samplePercentAbove5;

    private Double samplePercentAbove8;

    private Double samplePercentAbove10;

    private Double samplePercentAbove15;

    private Double samplePercentAbove20;

    private Double samplePercentAbove30;

    private Double samplePercentAbove50;

    public GATKDepthInterval() {
        super();
    }

    public GATKDepthInterval(String line) {
        super();
        String[] split = line.split("\t");

        if (split.length == 4) {
            this.contig = split[0];
            this.startPosition = Integer.valueOf(split[1]);
            this.endPosition = Integer.valueOf(split[2]);
        }

        if (split.length == 1) {
            String target = split[0];
            Matcher m = targetPattern.matcher(target);
            if (m.matches()) {
                this.contig = m.group("contig");
                this.startPosition = Integer.valueOf(m.group("start"));
                this.endPosition = Integer.valueOf(m.group("end"));
            }
        }
        
        if (split.length == 17) {
            String target = split[0];
            Matcher m = targetPattern.matcher(target);
            if (m.matches()) {
                this.contig = m.group("contig");
                this.startPosition = Integer.valueOf(m.group("start"));
                this.endPosition = Integer.valueOf(m.group("end"));
            }
            this.totalCoverage = Integer.valueOf(split[1]);
            this.averageCoverage = Double.valueOf(split[2]);
            this.sampleTotalCoverage = Integer.valueOf(split[3]);
            this.sampleMeanCoverage = Double.valueOf(split[4]);
            this.sampleGranularQ1 = split[5];
            this.sampleGranularMedian = split[6];
            this.sampleGranularQ3 = split[7];
            this.samplePercentAbove1 = Double.valueOf(split[8]);
            this.samplePercentAbove2 = Double.valueOf(split[9]);
            this.samplePercentAbove5 = Double.valueOf(split[10]);
            this.samplePercentAbove8 = Double.valueOf(split[11]);
            this.samplePercentAbove10 = Double.valueOf(split[12]);
            this.samplePercentAbove15 = Double.valueOf(split[13]);
            this.samplePercentAbove20 = Double.valueOf(split[14]);
            this.samplePercentAbove30 = Double.valueOf(split[15]);
            this.samplePercentAbove50 = Double.valueOf(split[16]);
        }
    }

    public String getContig() {
        return contig;
    }

    public void setContig(String contig) {
        this.contig = contig;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }

    public Integer getTotalCoverage() {
        return totalCoverage;
    }

    public void setTotalCoverage(Integer totalCoverage) {
        this.totalCoverage = totalCoverage;
    }

    public Double getAverageCoverage() {
        return averageCoverage;
    }

    public void setAverageCoverage(Double averageCoverage) {
        this.averageCoverage = averageCoverage;
    }

    public Integer getSampleTotalCoverage() {
        return sampleTotalCoverage;
    }

    public void setSampleTotalCoverage(Integer sampleTotalCoverage) {
        this.sampleTotalCoverage = sampleTotalCoverage;
    }

    public Double getSampleMeanCoverage() {
        return sampleMeanCoverage;
    }

    public void setSampleMeanCoverage(Double sampleMeanCoverage) {
        this.sampleMeanCoverage = sampleMeanCoverage;
    }

    public String getSampleGranularQ1() {
        return sampleGranularQ1;
    }

    public void setSampleGranularQ1(String sampleGranularQ1) {
        this.sampleGranularQ1 = sampleGranularQ1;
    }

    public String getSampleGranularMedian() {
        return sampleGranularMedian;
    }

    public void setSampleGranularMedian(String sampleGranularMedian) {
        this.sampleGranularMedian = sampleGranularMedian;
    }

    public String getSampleGranularQ3() {
        return sampleGranularQ3;
    }

    public void setSampleGranularQ3(String sampleGranularQ3) {
        this.sampleGranularQ3 = sampleGranularQ3;
    }

    public Double getSamplePercentAbove1() {
        return samplePercentAbove1;
    }

    public void setSamplePercentAbove1(Double samplePercentAbove1) {
        this.samplePercentAbove1 = samplePercentAbove1;
    }

    public Double getSamplePercentAbove2() {
        return samplePercentAbove2;
    }

    public void setSamplePercentAbove2(Double samplePercentAbove2) {
        this.samplePercentAbove2 = samplePercentAbove2;
    }

    public Double getSamplePercentAbove5() {
        return samplePercentAbove5;
    }

    public void setSamplePercentAbove5(Double samplePercentAbove5) {
        this.samplePercentAbove5 = samplePercentAbove5;
    }

    public Double getSamplePercentAbove8() {
        return samplePercentAbove8;
    }

    public void setSamplePercentAbove8(Double samplePercentAbove8) {
        this.samplePercentAbove8 = samplePercentAbove8;
    }

    public Double getSamplePercentAbove10() {
        return samplePercentAbove10;
    }

    public void setSamplePercentAbove10(Double samplePercentAbove10) {
        this.samplePercentAbove10 = samplePercentAbove10;
    }

    public Double getSamplePercentAbove15() {
        return samplePercentAbove15;
    }

    public void setSamplePercentAbove15(Double samplePercentAbove15) {
        this.samplePercentAbove15 = samplePercentAbove15;
    }

    public Double getSamplePercentAbove20() {
        return samplePercentAbove20;
    }

    public void setSamplePercentAbove20(Double samplePercentAbove20) {
        this.samplePercentAbove20 = samplePercentAbove20;
    }

    public Double getSamplePercentAbove30() {
        return samplePercentAbove30;
    }

    public void setSamplePercentAbove30(Double samplePercentAbove30) {
        this.samplePercentAbove30 = samplePercentAbove30;
    }

    public Double getSamplePercentAbove50() {
        return samplePercentAbove50;
    }

    public void setSamplePercentAbove50(Double samplePercentAbove50) {
        this.samplePercentAbove50 = samplePercentAbove50;
    }

    public Range<Integer> getPositionRange() {
        return Range.between(this.startPosition, this.endPosition);
    }

    public Integer getLength() {
        return this.endPosition - this.startPosition + 1;
    }

    @Override
    public int compareTo(GATKDepthInterval o) {
        int ret = 0;
        if (StringUtils.isNotEmpty(this.contig)) {
            ret = this.contig.compareTo(o.getContig());
            if (ret == 0) {
                ret = this.startPosition.compareTo(o.getStartPosition());
            }
        }
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((averageCoverage == null) ? 0 : averageCoverage.hashCode());
        result = prime * result + ((contig == null) ? 0 : contig.hashCode());
        result = prime * result + ((endPosition == null) ? 0 : endPosition.hashCode());
        result = prime * result + ((sampleGranularMedian == null) ? 0 : sampleGranularMedian.hashCode());
        result = prime * result + ((sampleGranularQ1 == null) ? 0 : sampleGranularQ1.hashCode());
        result = prime * result + ((sampleGranularQ3 == null) ? 0 : sampleGranularQ3.hashCode());
        result = prime * result + ((sampleMeanCoverage == null) ? 0 : sampleMeanCoverage.hashCode());
        result = prime * result + ((samplePercentAbove1 == null) ? 0 : samplePercentAbove1.hashCode());
        result = prime * result + ((samplePercentAbove10 == null) ? 0 : samplePercentAbove10.hashCode());
        result = prime * result + ((samplePercentAbove15 == null) ? 0 : samplePercentAbove15.hashCode());
        result = prime * result + ((samplePercentAbove2 == null) ? 0 : samplePercentAbove2.hashCode());
        result = prime * result + ((samplePercentAbove20 == null) ? 0 : samplePercentAbove20.hashCode());
        result = prime * result + ((samplePercentAbove30 == null) ? 0 : samplePercentAbove30.hashCode());
        result = prime * result + ((samplePercentAbove5 == null) ? 0 : samplePercentAbove5.hashCode());
        result = prime * result + ((samplePercentAbove50 == null) ? 0 : samplePercentAbove50.hashCode());
        result = prime * result + ((samplePercentAbove8 == null) ? 0 : samplePercentAbove8.hashCode());
        result = prime * result + ((sampleTotalCoverage == null) ? 0 : sampleTotalCoverage.hashCode());
        result = prime * result + ((startPosition == null) ? 0 : startPosition.hashCode());
        result = prime * result + ((totalCoverage == null) ? 0 : totalCoverage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GATKDepthInterval other = (GATKDepthInterval) obj;
        if (averageCoverage == null) {
            if (other.averageCoverage != null)
                return false;
        } else if (!averageCoverage.equals(other.averageCoverage))
            return false;
        if (contig == null) {
            if (other.contig != null)
                return false;
        } else if (!contig.equals(other.contig))
            return false;
        if (endPosition == null) {
            if (other.endPosition != null)
                return false;
        } else if (!endPosition.equals(other.endPosition))
            return false;
        if (sampleGranularMedian == null) {
            if (other.sampleGranularMedian != null)
                return false;
        } else if (!sampleGranularMedian.equals(other.sampleGranularMedian))
            return false;
        if (sampleGranularQ1 == null) {
            if (other.sampleGranularQ1 != null)
                return false;
        } else if (!sampleGranularQ1.equals(other.sampleGranularQ1))
            return false;
        if (sampleGranularQ3 == null) {
            if (other.sampleGranularQ3 != null)
                return false;
        } else if (!sampleGranularQ3.equals(other.sampleGranularQ3))
            return false;
        if (sampleMeanCoverage == null) {
            if (other.sampleMeanCoverage != null)
                return false;
        } else if (!sampleMeanCoverage.equals(other.sampleMeanCoverage))
            return false;
        if (samplePercentAbove1 == null) {
            if (other.samplePercentAbove1 != null)
                return false;
        } else if (!samplePercentAbove1.equals(other.samplePercentAbove1))
            return false;
        if (samplePercentAbove10 == null) {
            if (other.samplePercentAbove10 != null)
                return false;
        } else if (!samplePercentAbove10.equals(other.samplePercentAbove10))
            return false;
        if (samplePercentAbove15 == null) {
            if (other.samplePercentAbove15 != null)
                return false;
        } else if (!samplePercentAbove15.equals(other.samplePercentAbove15))
            return false;
        if (samplePercentAbove2 == null) {
            if (other.samplePercentAbove2 != null)
                return false;
        } else if (!samplePercentAbove2.equals(other.samplePercentAbove2))
            return false;
        if (samplePercentAbove20 == null) {
            if (other.samplePercentAbove20 != null)
                return false;
        } else if (!samplePercentAbove20.equals(other.samplePercentAbove20))
            return false;
        if (samplePercentAbove30 == null) {
            if (other.samplePercentAbove30 != null)
                return false;
        } else if (!samplePercentAbove30.equals(other.samplePercentAbove30))
            return false;
        if (samplePercentAbove5 == null) {
            if (other.samplePercentAbove5 != null)
                return false;
        } else if (!samplePercentAbove5.equals(other.samplePercentAbove5))
            return false;
        if (samplePercentAbove50 == null) {
            if (other.samplePercentAbove50 != null)
                return false;
        } else if (!samplePercentAbove50.equals(other.samplePercentAbove50))
            return false;
        if (samplePercentAbove8 == null) {
            if (other.samplePercentAbove8 != null)
                return false;
        } else if (!samplePercentAbove8.equals(other.samplePercentAbove8))
            return false;
        if (sampleTotalCoverage == null) {
            if (other.sampleTotalCoverage != null)
                return false;
        } else if (!sampleTotalCoverage.equals(other.sampleTotalCoverage))
            return false;
        if (startPosition == null) {
            if (other.startPosition != null)
                return false;
        } else if (!startPosition.equals(other.startPosition))
            return false;
        if (totalCoverage == null) {
            if (other.totalCoverage != null)
                return false;
        } else if (!totalCoverage.equals(other.totalCoverage))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%.2f\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n",
                String.format("%s:%d-%d", contig, startPosition, endPosition), totalCoverage, averageCoverage, sampleTotalCoverage,
                sampleMeanCoverage, sampleGranularQ1, sampleGranularMedian, sampleGranularQ3, samplePercentAbove1, samplePercentAbove2,
                samplePercentAbove5, samplePercentAbove8, samplePercentAbove10, samplePercentAbove15, samplePercentAbove20,
                samplePercentAbove30, samplePercentAbove50);
    }

    public String toStringTrimmed() {
        return String.format("%s\t%d\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f%n",
                String.format("%s:%d-%d", contig, startPosition, endPosition), totalCoverage, averageCoverage, samplePercentAbove1,
                samplePercentAbove2, samplePercentAbove5, samplePercentAbove8, samplePercentAbove10, samplePercentAbove15,
                samplePercentAbove20, samplePercentAbove30, samplePercentAbove50);
    }

}
