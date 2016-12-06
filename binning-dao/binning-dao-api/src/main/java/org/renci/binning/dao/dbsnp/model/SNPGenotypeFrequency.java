package org.renci.binning.dao.dbsnp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "dbsnp", name = "snp_genotype_freq")
public class SNPGenotypeFrequency implements Persistable {

    private static final long serialVersionUID = -293137422370473918L;

    @EmbeddedId
    private SNPGenotypeFrequencyPK key;

    @MapsId("snp")
    @ManyToOne
    @JoinColumn(name = "snp_id")
    private SNP snp;

    @Column(name = "individual_count")
    private Integer individualCount;

    @Column(name = "genotype_freq")
    private Double genotypeFrequency;

    public SNPGenotypeFrequency() {
        super();
    }

    public SNPGenotypeFrequency(SNPGenotypeFrequencyPK key) {
        super();
        this.key = key;
    }

    public SNPGenotypeFrequencyPK getKey() {
        return key;
    }

    public void setKey(SNPGenotypeFrequencyPK key) {
        this.key = key;
    }

    public SNP getSnp() {
        return snp;
    }

    public void setSnp(SNP snp) {
        this.snp = snp;
    }

    public Integer getIndividualCount() {
        return individualCount;
    }

    public void setIndividualCount(Integer individualCount) {
        this.individualCount = individualCount;
    }

    public Double getGenotypeFrequency() {
        return genotypeFrequency;
    }

    public void setGenotypeFrequency(Double genotypeFrequency) {
        this.genotypeFrequency = genotypeFrequency;
    }

    @Override
    public String toString() {
        return String.format("SNPGenotypeFrequency [key=%s, individualCount=%s, genotypeFrequency=%s]", key, individualCount,
                genotypeFrequency);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genotypeFrequency == null) ? 0 : genotypeFrequency.hashCode());
        result = prime * result + ((individualCount == null) ? 0 : individualCount.hashCode());
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
        SNPGenotypeFrequency other = (SNPGenotypeFrequency) obj;
        if (genotypeFrequency == null) {
            if (other.genotypeFrequency != null)
                return false;
        } else if (!genotypeFrequency.equals(other.genotypeFrequency))
            return false;
        if (individualCount == null) {
            if (other.individualCount != null)
                return false;
        } else if (!individualCount.equals(other.individualCount))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
