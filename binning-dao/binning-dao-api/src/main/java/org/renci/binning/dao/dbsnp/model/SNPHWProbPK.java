package org.renci.binning.dao.dbsnp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SNPHWProbPK implements Serializable {

    private static final long serialVersionUID = -5941798422418282237L;

    @Column(name = "snp_id")
    private Integer snp;

    @Column(name = "degrees_of_freedom")
    private Integer degreesOfFreedom;

    @Column(name = "chisq")
    private Double chisq;

    @Column(name = "hwp")
    private Double hwp;

    public SNPHWProbPK() {
        super();
    }

    public SNPHWProbPK(Integer snp, Integer degreesOfFreedom, Double chisq, Double hwp) {
        super();
        this.snp = snp;
        this.degreesOfFreedom = degreesOfFreedom;
        this.chisq = chisq;
        this.hwp = hwp;
    }

    public Integer getSnp() {
        return snp;
    }

    public void setSnp(Integer snp) {
        this.snp = snp;
    }

    public Integer getDegreesOfFreedom() {
        return degreesOfFreedom;
    }

    public void setDegreesOfFreedom(Integer degreesOfFreedom) {
        this.degreesOfFreedom = degreesOfFreedom;
    }

    public Double getChisq() {
        return chisq;
    }

    public void setChisq(Double chisq) {
        this.chisq = chisq;
    }

    public Double getHwp() {
        return hwp;
    }

    public void setHwp(Double hwp) {
        this.hwp = hwp;
    }

    @Override
    public String toString() {
        return String.format("SNPHWProb [snp=%s, degreesOfFreedom=%s, chisq=%s, hwp=%s]", snp, degreesOfFreedom, chisq, hwp);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chisq == null) ? 0 : chisq.hashCode());
        result = prime * result + ((degreesOfFreedom == null) ? 0 : degreesOfFreedom.hashCode());
        result = prime * result + ((hwp == null) ? 0 : hwp.hashCode());
        result = prime * result + ((snp == null) ? 0 : snp.hashCode());
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
        SNPHWProbPK other = (SNPHWProbPK) obj;
        if (chisq == null) {
            if (other.chisq != null)
                return false;
        } else if (!chisq.equals(other.chisq))
            return false;
        if (degreesOfFreedom == null) {
            if (other.degreesOfFreedom != null)
                return false;
        } else if (!degreesOfFreedom.equals(other.degreesOfFreedom))
            return false;
        if (hwp == null) {
            if (other.hwp != null)
                return false;
        } else if (!hwp.equals(other.hwp))
            return false;
        if (snp == null) {
            if (other.snp != null)
                return false;
        } else if (!snp.equals(other.snp))
            return false;
        return true;
    }

}
