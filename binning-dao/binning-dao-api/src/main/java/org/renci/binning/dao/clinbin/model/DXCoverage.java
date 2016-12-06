package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "dx_coverage")
public class DXCoverage implements Persistable {

    private static final long serialVersionUID = -2985428056984351080L;

    @EmbeddedId
    private DXCoveragePK key;

    @ManyToOne
    @MapsId("exonId")
    @JoinColumn(name = "dx_exon_id")
    private DXExons exon;

    @Column(name = "frac_gt_1")
    private Double fractionGreaterThan1;

    @Column(name = "frac_gt_2")
    private Double fractionGreaterThan2;

    @Column(name = "frac_gt_5")
    private Double fractionGreaterThan5;

    @Column(name = "frac_gt_8")
    private Double fractionGreaterThan8;

    @Column(name = "frac_gt_10")
    private Double fractionGreaterThan10;

    @Column(name = "frac_gt_15")
    private Double fractionGreaterThan15;

    @Column(name = "frac_gt_20")
    private Double fractionGreaterThan20;

    @Column(name = "frac_gt_30")
    private Double fractionGreaterThan30;

    @Column(name = "frac_gt_50")
    private Double fractionGreaterThan50;

    public DXCoverage() {
        super();
    }

    public DXCoverage(DXCoveragePK key) {
        super();
        this.key = key;
    }

    public DXCoveragePK getKey() {
        return key;
    }

    public void setKey(DXCoveragePK key) {
        this.key = key;
    }

    public DXExons getExon() {
        return exon;
    }

    public void setExon(DXExons exon) {
        this.exon = exon;
    }

    public Double getFractionGreaterThan1() {
        return fractionGreaterThan1;
    }

    public void setFractionGreaterThan1(Double fractionGreaterThan1) {
        this.fractionGreaterThan1 = fractionGreaterThan1;
    }

    public Double getFractionGreaterThan2() {
        return fractionGreaterThan2;
    }

    public void setFractionGreaterThan2(Double fractionGreaterThan2) {
        this.fractionGreaterThan2 = fractionGreaterThan2;
    }

    public Double getFractionGreaterThan5() {
        return fractionGreaterThan5;
    }

    public void setFractionGreaterThan5(Double fractionGreaterThan5) {
        this.fractionGreaterThan5 = fractionGreaterThan5;
    }

    public Double getFractionGreaterThan8() {
        return fractionGreaterThan8;
    }

    public void setFractionGreaterThan8(Double fractionGreaterThan8) {
        this.fractionGreaterThan8 = fractionGreaterThan8;
    }

    public Double getFractionGreaterThan10() {
        return fractionGreaterThan10;
    }

    public void setFractionGreaterThan10(Double fractionGreaterThan10) {
        this.fractionGreaterThan10 = fractionGreaterThan10;
    }

    public Double getFractionGreaterThan15() {
        return fractionGreaterThan15;
    }

    public void setFractionGreaterThan15(Double fractionGreaterThan15) {
        this.fractionGreaterThan15 = fractionGreaterThan15;
    }

    public Double getFractionGreaterThan20() {
        return fractionGreaterThan20;
    }

    public void setFractionGreaterThan20(Double fractionGreaterThan20) {
        this.fractionGreaterThan20 = fractionGreaterThan20;
    }

    public Double getFractionGreaterThan30() {
        return fractionGreaterThan30;
    }

    public void setFractionGreaterThan30(Double fractionGreaterThan30) {
        this.fractionGreaterThan30 = fractionGreaterThan30;
    }

    public Double getFractionGreaterThan50() {
        return fractionGreaterThan50;
    }

    public void setFractionGreaterThan50(Double fractionGreaterThan50) {
        this.fractionGreaterThan50 = fractionGreaterThan50;
    }

    @Override
    public String toString() {
        return String.format(
                "DXCoverage [key=%s, fractionGreaterThan1=%s, fractionGreaterThan2=%s, fractionGreaterThan5=%s, fractionGreaterThan8=%s, fractionGreaterThan10=%s, fractionGreaterThan15=%s, fractionGreaterThan20=%s, fractionGreaterThan30=%s, fractionGreaterThan50=%s]",
                key, fractionGreaterThan1, fractionGreaterThan2, fractionGreaterThan5, fractionGreaterThan8, fractionGreaterThan10,
                fractionGreaterThan15, fractionGreaterThan20, fractionGreaterThan30, fractionGreaterThan50);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fractionGreaterThan1 == null) ? 0 : fractionGreaterThan1.hashCode());
        result = prime * result + ((fractionGreaterThan10 == null) ? 0 : fractionGreaterThan10.hashCode());
        result = prime * result + ((fractionGreaterThan15 == null) ? 0 : fractionGreaterThan15.hashCode());
        result = prime * result + ((fractionGreaterThan2 == null) ? 0 : fractionGreaterThan2.hashCode());
        result = prime * result + ((fractionGreaterThan20 == null) ? 0 : fractionGreaterThan20.hashCode());
        result = prime * result + ((fractionGreaterThan30 == null) ? 0 : fractionGreaterThan30.hashCode());
        result = prime * result + ((fractionGreaterThan5 == null) ? 0 : fractionGreaterThan5.hashCode());
        result = prime * result + ((fractionGreaterThan50 == null) ? 0 : fractionGreaterThan50.hashCode());
        result = prime * result + ((fractionGreaterThan8 == null) ? 0 : fractionGreaterThan8.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
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
        DXCoverage other = (DXCoverage) obj;
        if (fractionGreaterThan1 == null) {
            if (other.fractionGreaterThan1 != null)
                return false;
        } else if (!fractionGreaterThan1.equals(other.fractionGreaterThan1))
            return false;
        if (fractionGreaterThan10 == null) {
            if (other.fractionGreaterThan10 != null)
                return false;
        } else if (!fractionGreaterThan10.equals(other.fractionGreaterThan10))
            return false;
        if (fractionGreaterThan15 == null) {
            if (other.fractionGreaterThan15 != null)
                return false;
        } else if (!fractionGreaterThan15.equals(other.fractionGreaterThan15))
            return false;
        if (fractionGreaterThan2 == null) {
            if (other.fractionGreaterThan2 != null)
                return false;
        } else if (!fractionGreaterThan2.equals(other.fractionGreaterThan2))
            return false;
        if (fractionGreaterThan20 == null) {
            if (other.fractionGreaterThan20 != null)
                return false;
        } else if (!fractionGreaterThan20.equals(other.fractionGreaterThan20))
            return false;
        if (fractionGreaterThan30 == null) {
            if (other.fractionGreaterThan30 != null)
                return false;
        } else if (!fractionGreaterThan30.equals(other.fractionGreaterThan30))
            return false;
        if (fractionGreaterThan5 == null) {
            if (other.fractionGreaterThan5 != null)
                return false;
        } else if (!fractionGreaterThan5.equals(other.fractionGreaterThan5))
            return false;
        if (fractionGreaterThan50 == null) {
            if (other.fractionGreaterThan50 != null)
                return false;
        } else if (!fractionGreaterThan50.equals(other.fractionGreaterThan50))
            return false;
        if (fractionGreaterThan8 == null) {
            if (other.fractionGreaterThan8 != null)
                return false;
        } else if (!fractionGreaterThan8.equals(other.fractionGreaterThan8))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
