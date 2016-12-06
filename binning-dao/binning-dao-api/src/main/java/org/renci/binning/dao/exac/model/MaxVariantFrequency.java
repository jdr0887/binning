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

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "exac", name = "max_variant_freq")
@NamedQueries({
        @NamedQuery(name = "exac.MaxVariantFrequency.findByLocatedVariantIdAndVersion", query = "SELECT a FROM MaxVariantFrequency a join a.locatedVariant b where b.id = :locatedVariantId and a.key.version = :version order by a.maxAlleleFrequency desc") })
@FetchGroups({ @FetchGroup(name = "includeManyToOnes", attributes = { @FetchAttribute(name = "locatedVariant") }) })
public class MaxVariantFrequency implements Persistable {

    private static final long serialVersionUID = -1388708510623130329L;

    @EmbeddedId
    private MaxVariantFrequencyPK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @Column(name = "max_allele_freq")
    private Double maxAlleleFrequency;

    public MaxVariantFrequency() {
        super();
    }

    public MaxVariantFrequencyPK getKey() {
        return key;
    }

    public void setKey(MaxVariantFrequencyPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public Double getMaxAlleleFrequency() {
        return maxAlleleFrequency;
    }

    public void setMaxAlleleFrequency(Double maxAlleleFrequency) {
        this.maxAlleleFrequency = maxAlleleFrequency;
    }

    @Override
    public String toString() {
        return String.format("MaxVariantFrequency [key=%s, maxAlleleFrequency=%s]", key, maxAlleleFrequency);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((maxAlleleFrequency == null) ? 0 : maxAlleleFrequency.hashCode());
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
        MaxVariantFrequency other = (MaxVariantFrequency) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (maxAlleleFrequency == null) {
            if (other.maxAlleleFrequency != null)
                return false;
        } else if (!maxAlleleFrequency.equals(other.maxAlleleFrequency))
            return false;
        return true;
    }

}
