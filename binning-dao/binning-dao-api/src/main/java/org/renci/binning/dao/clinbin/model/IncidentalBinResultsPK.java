package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class IncidentalBinResultsPK implements Serializable {

    private static final long serialVersionUID = -4224198274029225689L;

    @Column(name = "loc_var_id")
    private Integer locatedVariant;

    @Column(name = "bin_version")
    private Integer binVersion;

    @Lob
    @Column(name = "zygosity_mode")
    private String zygosityMode;

    @Column(name = "transcr", length = 31)
    private String transcr;

    public IncidentalBinResultsPK() {
        super();
    }

    public Integer getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Integer locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public Integer getBinVersion() {
        return binVersion;
    }

    public void setBinVersion(Integer binVersion) {
        this.binVersion = binVersion;
    }

    public String getZygosityMode() {
        return zygosityMode;
    }

    public void setZygosityMode(String zygosityMode) {
        this.zygosityMode = zygosityMode;
    }

    public String getTranscr() {
        return transcr;
    }

    public void setTranscr(String transcr) {
        this.transcr = transcr;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinResultsPK [locatedVariant=%s, binVersion=%s, zygosityMode=%s, transcr=%s]", locatedVariant,
                binVersion, zygosityMode, transcr);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((binVersion == null) ? 0 : binVersion.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((transcr == null) ? 0 : transcr.hashCode());
        result = prime * result + ((zygosityMode == null) ? 0 : zygosityMode.hashCode());
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
        IncidentalBinResultsPK other = (IncidentalBinResultsPK) obj;
        if (binVersion == null) {
            if (other.binVersion != null)
                return false;
        } else if (!binVersion.equals(other.binVersion))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (transcr == null) {
            if (other.transcr != null)
                return false;
        } else if (!transcr.equals(other.transcr))
            return false;
        if (zygosityMode == null) {
            if (other.zygosityMode != null)
                return false;
        } else if (!zygosityMode.equals(other.zygosityMode))
            return false;
        return true;
    }

}
