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
@Table(schema = "dbsnp", name = "snp_allele_freq")
public class SNPAlleleFrequency implements Persistable {

    private static final long serialVersionUID = 3304971565225295358L;

    @EmbeddedId
    private SNPAlleleFrequencyPK key;

    @MapsId("snp")
    @ManyToOne
    @JoinColumn(name = "snp_id")
    private SNP snp;

    @Column(name = "chr_count")
    private Integer chromosomeCount;

    @Column(name = "chr_count")
    private Double alleleFrequency;

    public SNPAlleleFrequency() {
        super();
    }

    public SNPAlleleFrequency(SNPAlleleFrequencyPK key) {
        super();
        this.key = key;
    }

    public SNPAlleleFrequencyPK getKey() {
        return key;
    }

    public void setKey(SNPAlleleFrequencyPK key) {
        this.key = key;
    }

    public SNP getSnp() {
        return snp;
    }

    public void setSnp(SNP snp) {
        this.snp = snp;
    }

    public Integer getChromosomeCount() {
        return chromosomeCount;
    }

    public void setChromosomeCount(Integer chromosomeCount) {
        this.chromosomeCount = chromosomeCount;
    }

    public Double getAlleleFrequency() {
        return alleleFrequency;
    }

    public void setAlleleFrequency(Double alleleFrequency) {
        this.alleleFrequency = alleleFrequency;
    }

    @Override
    public String toString() {
        return String.format("SNPAlleleFrequency [key=%s, chromosomeCount=%s, alleleFrequency=%s]", key, chromosomeCount, alleleFrequency);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alleleFrequency == null) ? 0 : alleleFrequency.hashCode());
        result = prime * result + ((chromosomeCount == null) ? 0 : chromosomeCount.hashCode());
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
        SNPAlleleFrequency other = (SNPAlleleFrequency) obj;
        if (alleleFrequency == null) {
            if (other.alleleFrequency != null)
                return false;
        } else if (!alleleFrequency.equals(other.alleleFrequency))
            return false;
        if (chromosomeCount == null) {
            if (other.chromosomeCount != null)
                return false;
        } else if (!chromosomeCount.equals(other.chromosomeCount))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
