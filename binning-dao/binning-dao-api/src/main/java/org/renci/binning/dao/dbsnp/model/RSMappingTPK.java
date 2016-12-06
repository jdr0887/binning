package org.renci.binning.dao.dbsnp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RSMappingTPK implements Serializable {

    private static final long serialVersionUID = 3664587193735716405L;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    public RSMappingTPK() {
        super();
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    @Override
    public String toString() {
        return String.format("SNPMappingAggPK [locatedVariant=%s]", locatedVariant);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
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
        RSMappingTPK other = (RSMappingTPK) obj;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        return true;
    }

}
