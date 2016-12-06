package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NCGenesFrequenciesPK implements Serializable {

    private static final long serialVersionUID = 8440261399104853359L;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Column(name = "version", length = 1024)
    private String version;

    public NCGenesFrequenciesPK() {
        super();
    }

    public NCGenesFrequenciesPK(Long locatedVariant, String version) {
        super();
        this.locatedVariant = locatedVariant;
        this.version = version;
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return String.format("NCGenesFrequenciesPK [locatedVariant=%s, version=%s]", locatedVariant, version);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        NCGenesFrequenciesPK other = (NCGenesFrequenciesPK) obj;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
