package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncidentalBinGroupVersionPK implements Serializable {

    private static final long serialVersionUID = -2967879242600491211L;

    @Column(name = "ibin_group_version")
    private Integer ibinGroupVersion;

    @Column(name = "incidental_bin_id")
    private Integer incidentalBin;

    public IncidentalBinGroupVersionPK() {
        super();
    }

    public Integer getIbinGroupVersion() {
        return ibinGroupVersion;
    }

    public void setIbinGroupVersion(Integer ibinGroupVersion) {
        this.ibinGroupVersion = ibinGroupVersion;
    }

    public Integer getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(Integer incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinGroupVersionPK [ibinGroupVersion=%s, incidentalBin=%s]", ibinGroupVersion, incidentalBin);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ibinGroupVersion == null) ? 0 : ibinGroupVersion.hashCode());
        result = prime * result + ((incidentalBin == null) ? 0 : incidentalBin.hashCode());
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
        IncidentalBinGroupVersionPK other = (IncidentalBinGroupVersionPK) obj;
        if (ibinGroupVersion == null) {
            if (other.ibinGroupVersion != null)
                return false;
        } else if (!ibinGroupVersion.equals(other.ibinGroupVersion))
            return false;
        if (incidentalBin == null) {
            if (other.incidentalBin != null)
                return false;
        } else if (!incidentalBin.equals(other.incidentalBin))
            return false;
        return true;
    }

}
