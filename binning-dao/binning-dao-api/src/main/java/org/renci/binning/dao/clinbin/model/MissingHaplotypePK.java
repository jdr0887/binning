package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MissingHaplotypePK implements Serializable {

    private static final long serialVersionUID = 6191445038140835400L;

    @Column(name = "participant", length = 50)
    private String participant;

    @Column(name = "incidental_bin_id")
    private Integer incidentalBin;

    @Column(name = "incidental_list_version")
    private Integer listVersion;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    public MissingHaplotypePK() {
        super();
    }

    public MissingHaplotypePK(String participant, Integer incidentalBin, Integer listVersion, Long locatedVariant) {
        super();
        this.participant = participant;
        this.incidentalBin = incidentalBin;
        this.listVersion = listVersion;
        this.locatedVariant = locatedVariant;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Integer getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(Integer incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    public Integer getListVersion() {
        return listVersion;
    }

    public void setListVersion(Integer listVersion) {
        this.listVersion = listVersion;
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    @Override
    public String toString() {
        return String.format("MissingHaplotypesPK [participant=%s, incidentalBin=%s, listVersion=%s, locatedVariant=%s]", participant,
                incidentalBin, listVersion, locatedVariant);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((incidentalBin == null) ? 0 : incidentalBin.hashCode());
        result = prime * result + ((listVersion == null) ? 0 : listVersion.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((participant == null) ? 0 : participant.hashCode());
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
        MissingHaplotypePK other = (MissingHaplotypePK) obj;
        if (incidentalBin == null) {
            if (other.incidentalBin != null)
                return false;
        } else if (!incidentalBin.equals(other.incidentalBin))
            return false;
        if (listVersion == null) {
            if (other.listVersion != null)
                return false;
        } else if (!listVersion.equals(other.listVersion))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (participant == null) {
            if (other.participant != null)
                return false;
        } else if (!participant.equals(other.participant))
            return false;
        return true;
    }

}
