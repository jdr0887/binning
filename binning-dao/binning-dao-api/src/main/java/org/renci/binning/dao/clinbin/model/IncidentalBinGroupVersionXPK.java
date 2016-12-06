package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncidentalBinGroupVersionXPK implements Serializable {

    private static final long serialVersionUID = -2967879242600491211L;

    @Column(name = "incidental_bin_group_version")
    private Integer incidentalBinGroupVersion;

    @Column(name = "incidental_bin_id")
    private Integer incidentalBin;

    @Column(name = "incidental_bin_version")
    private Integer incidentalBinVersion;

    public IncidentalBinGroupVersionXPK() {
        super();
    }

    public IncidentalBinGroupVersionXPK(Integer incidentalBinGroupVersion, Integer incidentalBin, Integer incidentalBinVersion) {
        super();
        this.incidentalBinGroupVersion = incidentalBinGroupVersion;
        this.incidentalBin = incidentalBin;
        this.incidentalBinVersion = incidentalBinVersion;
    }

    public Integer getIncidentalBinGroupVersion() {
        return incidentalBinGroupVersion;
    }

    public void setIncidentalBinGroupVersion(Integer incidentalBinGroupVersion) {
        this.incidentalBinGroupVersion = incidentalBinGroupVersion;
    }

    public Integer getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(Integer incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    public Integer getIncidentalBinVersion() {
        return incidentalBinVersion;
    }

    public void setIncidentalBinVersion(Integer incidentalBinVersion) {
        this.incidentalBinVersion = incidentalBinVersion;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinGroupVersionXPK [incidentalBinGroupVersion=%s, incidentalBin=%s, incidentalBinVersion=%s]",
                incidentalBinGroupVersion, incidentalBin, incidentalBinVersion);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((incidentalBin == null) ? 0 : incidentalBin.hashCode());
        result = prime * result + ((incidentalBinGroupVersion == null) ? 0 : incidentalBinGroupVersion.hashCode());
        result = prime * result + ((incidentalBinVersion == null) ? 0 : incidentalBinVersion.hashCode());
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
        IncidentalBinGroupVersionXPK other = (IncidentalBinGroupVersionXPK) obj;
        if (incidentalBin == null) {
            if (other.incidentalBin != null)
                return false;
        } else if (!incidentalBin.equals(other.incidentalBin))
            return false;
        if (incidentalBinGroupVersion == null) {
            if (other.incidentalBinGroupVersion != null)
                return false;
        } else if (!incidentalBinGroupVersion.equals(other.incidentalBinGroupVersion))
            return false;
        if (incidentalBinVersion == null) {
            if (other.incidentalBinVersion != null)
                return false;
        } else if (!incidentalBinVersion.equals(other.incidentalBinVersion))
            return false;
        return true;
    }

}
