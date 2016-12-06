package org.renci.binning.dao.dbsnp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SNPGenotypeFrequencyPK implements Serializable {

    private static final long serialVersionUID = -6067169687414525254L;

    @Column(name = "genotype_name")
    private String name;

    @Column(name = "snp_id")
    private Integer snp;

    public SNPGenotypeFrequencyPK() {
        super();
    }

    public SNPGenotypeFrequencyPK(String name, Integer snp) {
        super();
        this.name = name;
        this.snp = snp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSnp() {
        return snp;
    }

    public void setSnp(Integer snp) {
        this.snp = snp;
    }

    @Override
    public String toString() {
        return String.format("SNPGenotypeFrequencyPK [name=%s, snp=%s]", name, snp);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        SNPGenotypeFrequencyPK other = (SNPGenotypeFrequencyPK) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (snp == null) {
            if (other.snp != null)
                return false;
        } else if (!snp.equals(other.snp))
            return false;
        return true;
    }

}
