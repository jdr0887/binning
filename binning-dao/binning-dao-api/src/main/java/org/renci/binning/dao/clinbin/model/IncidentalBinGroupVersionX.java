package org.renci.binning.dao.clinbin.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "incidental_bin_group_versionx")
public class IncidentalBinGroupVersionX implements Persistable {

    private static final long serialVersionUID = 2212074020390950607L;

    @EmbeddedId
    private IncidentalBinGroupVersionXPK key;

    @MapsId("incidentalBin")
    @ManyToOne
    @JoinColumn(name = "incidental_bin_id")
    private IncidentalBin incidentalBin;

    public IncidentalBinGroupVersionX() {
        super();
    }

    public IncidentalBinGroupVersionX(IncidentalBinGroupVersionXPK key) {
        super();
        this.key = key;
    }

    public IncidentalBinGroupVersionXPK getKey() {
        return key;
    }

    public void setKey(IncidentalBinGroupVersionXPK key) {
        this.key = key;
    }

    public IncidentalBin getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(IncidentalBin incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinGroupVersionX [key=%s]", key);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        IncidentalBinGroupVersionX other = (IncidentalBinGroupVersionX) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
