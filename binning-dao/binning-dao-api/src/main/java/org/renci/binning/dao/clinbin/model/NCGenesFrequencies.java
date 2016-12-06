package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "clinbin", name = "ncgenes_frequencies")
public class NCGenesFrequencies implements Persistable {

    private static final long serialVersionUID = -3413773809175450239L;

    @EmbeddedId
    private NCGenesFrequenciesPK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @Column(name = "num_hom_ref")
    private Integer numberHomRef;

    @Column(name = "num_het")
    private Integer numHet;

    @Column(name = "num_hom_alt")
    private Integer numHomAlt;

    @Column(name = "alt_allele_freq")
    private Double altAlleleFrequency;

    @Column(name = "hwe_p")
    private Double hweP;

    public NCGenesFrequencies() {
        super();
    }

    public NCGenesFrequencies(NCGenesFrequenciesPK key) {
        super();
        this.key = key;
    }

    public NCGenesFrequenciesPK getKey() {
        return key;
    }

    public void setKey(NCGenesFrequenciesPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public Integer getNumberHomRef() {
        return numberHomRef;
    }

    public void setNumberHomRef(Integer numberHomRef) {
        this.numberHomRef = numberHomRef;
    }

    public Integer getNumHet() {
        return numHet;
    }

    public void setNumHet(Integer numHet) {
        this.numHet = numHet;
    }

    public Integer getNumHomAlt() {
        return numHomAlt;
    }

    public void setNumHomAlt(Integer numHomAlt) {
        this.numHomAlt = numHomAlt;
    }

    public Double getAltAlleleFrequency() {
        return altAlleleFrequency;
    }

    public void setAltAlleleFrequency(Double altAlleleFrequency) {
        this.altAlleleFrequency = altAlleleFrequency;
    }

    public Double getHweP() {
        return hweP;
    }

    public void setHweP(Double hweP) {
        this.hweP = hweP;
    }

    @Override
    public String toString() {
        return String.format("NCGenesFrequencies [key=%s, numberHomRef=%s, numHet=%s, numHomAlt=%s, altAlleleFrequency=%s, hweP=%s]", key,
                numberHomRef, numHet, numHomAlt, altAlleleFrequency, hweP);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((altAlleleFrequency == null) ? 0 : altAlleleFrequency.hashCode());
        result = prime * result + ((hweP == null) ? 0 : hweP.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((numHet == null) ? 0 : numHet.hashCode());
        result = prime * result + ((numHomAlt == null) ? 0 : numHomAlt.hashCode());
        result = prime * result + ((numberHomRef == null) ? 0 : numberHomRef.hashCode());
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
        NCGenesFrequencies other = (NCGenesFrequencies) obj;
        if (altAlleleFrequency == null) {
            if (other.altAlleleFrequency != null)
                return false;
        } else if (!altAlleleFrequency.equals(other.altAlleleFrequency))
            return false;
        if (hweP == null) {
            if (other.hweP != null)
                return false;
        } else if (!hweP.equals(other.hweP))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (numHet == null) {
            if (other.numHet != null)
                return false;
        } else if (!numHet.equals(other.numHet))
            return false;
        if (numHomAlt == null) {
            if (other.numHomAlt != null)
                return false;
        } else if (!numHomAlt.equals(other.numHomAlt))
            return false;
        if (numberHomRef == null) {
            if (other.numberHomRef != null)
                return false;
        } else if (!numberHomRef.equals(other.numberHomRef))
            return false;
        return true;
    }

}
