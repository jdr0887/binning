package org.renci.binning.dao.exac.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "exac", name = "variant_freq")
@NamedQueries({
        @NamedQuery(name = "exac.VariantFrequency.findByLocatedVariantIdAndVersion", query = "FROM VariantFrequency a join a.LocatedVariant b where b.id = :LocatedVariantId and a.key.version = :version order by a.alternateAlleleFrequency desc") })
public class VariantFrequency implements Persistable {

    private static final long serialVersionUID = 4359650786462818369L;

    @EmbeddedId
    private VariantFrequencyPK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @Column(name = "alt_allele_freq")
    private Double alternateAlleleFrequency;

    @Column(name = "alt_allele_count", insertable = false, updatable = false)
    private Integer alternateAlleleCount;

    @Column(name = "alt_allele_count")
    private Integer totalAlleleCount;

    public VariantFrequency() {
        super();
    }

    public VariantFrequencyPK getKey() {
        return key;
    }

    public void setKey(VariantFrequencyPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public Double getAlternateAlleleFrequency() {
        return alternateAlleleFrequency;
    }

    public void setAlternateAlleleFrequency(Double alternateAlleleFrequency) {
        this.alternateAlleleFrequency = alternateAlleleFrequency;
    }

    public Integer getAlternateAlleleCount() {
        return alternateAlleleCount;
    }

    public void setAlternateAlleleCount(Integer alternateAlleleCount) {
        this.alternateAlleleCount = alternateAlleleCount;
    }

    public Integer getTotalAlleleCount() {
        return totalAlleleCount;
    }

    public void setTotalAlleleCount(Integer totalAlleleCount) {
        this.totalAlleleCount = totalAlleleCount;
    }

    @Override
    public String toString() {
        return String.format(
                "VariantFrequency [locatedVariant=%s, alternateAlleleFrequency=%s, alternateAlleleCount=%s, totalAlleleCount=%s]",
                locatedVariant, alternateAlleleFrequency, alternateAlleleCount, totalAlleleCount);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alternateAlleleCount == null) ? 0 : alternateAlleleCount.hashCode());
        result = prime * result + ((alternateAlleleFrequency == null) ? 0 : alternateAlleleFrequency.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((totalAlleleCount == null) ? 0 : totalAlleleCount.hashCode());
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
        VariantFrequency other = (VariantFrequency) obj;
        if (alternateAlleleCount == null) {
            if (other.alternateAlleleCount != null)
                return false;
        } else if (!alternateAlleleCount.equals(other.alternateAlleleCount))
            return false;
        if (alternateAlleleFrequency == null) {
            if (other.alternateAlleleFrequency != null)
                return false;
        } else if (!alternateAlleleFrequency.equals(other.alternateAlleleFrequency))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (totalAlleleCount == null) {
            if (other.totalAlleleCount != null)
                return false;
        } else if (!totalAlleleCount.equals(other.totalAlleleCount))
            return false;
        return true;
    }

}
