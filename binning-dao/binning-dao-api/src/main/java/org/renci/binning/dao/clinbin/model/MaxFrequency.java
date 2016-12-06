package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "clinbin", name = "max_freq")
@FetchGroups({ @FetchGroup(name = "includeManyToOnes", attributes = { @FetchAttribute(name = "locatedVariant"),
        @FetchAttribute(name = "source") }) })
public class MaxFrequency implements Persistable {

    private static final long serialVersionUID = -2401541418491242656L;

    @EmbeddedId
    private MaxFrequencyPK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @Column(name = "max_allele_freq")
    private Double maxAlleleFreq;

    @ManyToOne
    @JoinColumn(name = "source")
    private MaxFrequencySource source;

    public MaxFrequency() {
        super();
    }

    public MaxFrequency(MaxFrequencyPK key, MaxFrequencySource source) {
        super();
        this.key = key;
        this.source = source;
    }

    public MaxFrequencyPK getKey() {
        return key;
    }

    public void setKey(MaxFrequencyPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public Double getMaxAlleleFreq() {
        return maxAlleleFreq;
    }

    public void setMaxAlleleFreq(Double maxAlleleFreq) {
        this.maxAlleleFreq = maxAlleleFreq;
    }

    public MaxFrequencySource getSource() {
        return source;
    }

    public void setSource(MaxFrequencySource source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return String.format("MaxFrequency [key=%s, maxAlleleFreq=%s, source=%s]", key, maxAlleleFreq, source);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((maxAlleleFreq == null) ? 0 : maxAlleleFreq.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        MaxFrequency other = (MaxFrequency) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (maxAlleleFreq == null) {
            if (other.maxAlleleFreq != null)
                return false;
        } else if (!maxAlleleFreq.equals(other.maxAlleleFreq))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }

}
