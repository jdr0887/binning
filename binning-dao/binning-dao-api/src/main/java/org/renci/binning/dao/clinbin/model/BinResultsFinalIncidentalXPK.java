package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BinResultsFinalIncidentalXPK implements Serializable {

    private static final long serialVersionUID = 745714824781965526L;

    @Column(name = "participant", length = 50)
    private String participant;

    @Column(name = "mapnum")
    private Integer mapnum;

    @Column(name = "incidental_result_version")
    private Integer incidentalResultVersion;

    @Column(name = "incidental_bin_id")
    private Integer incidentalBin;

    @Column(name = "asm_id")
    private Integer assembly;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    public BinResultsFinalIncidentalXPK() {
        super();
    }

    public BinResultsFinalIncidentalXPK(String participant, Integer mapnum, Integer incidentalResultVersion, Integer incidentalBin,
            Integer assembly, Long locatedVariant) {
        super();
        this.participant = participant;
        this.mapnum = mapnum;
        this.incidentalResultVersion = incidentalResultVersion;
        this.incidentalBin = incidentalBin;
        this.assembly = assembly;
        this.locatedVariant = locatedVariant;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Integer getMapnum() {
        return mapnum;
    }

    public void setMapnum(Integer mapnum) {
        this.mapnum = mapnum;
    }

    public Integer getIncidentalResultVersion() {
        return incidentalResultVersion;
    }

    public void setIncidentalResultVersion(Integer incidentalResultVersion) {
        this.incidentalResultVersion = incidentalResultVersion;
    }

    public Integer getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(Integer incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    public Integer getAssembly() {
        return assembly;
    }

    public void setAssembly(Integer assembly) {
        this.assembly = assembly;
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    @Override
    public String toString() {
        return String.format(
                "BinResultsFinalIncidentalXPK [participant=%s, mapnum=%s, incidentalResultVersion=%s, incidentalBin=%s, assembly=%s, locatedVariant=%s]",
                participant, mapnum, incidentalResultVersion, incidentalBin, assembly, locatedVariant);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assembly == null) ? 0 : assembly.hashCode());
        result = prime * result + ((incidentalBin == null) ? 0 : incidentalBin.hashCode());
        result = prime * result + ((incidentalResultVersion == null) ? 0 : incidentalResultVersion.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((mapnum == null) ? 0 : mapnum.hashCode());
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
        BinResultsFinalIncidentalXPK other = (BinResultsFinalIncidentalXPK) obj;
        if (assembly == null) {
            if (other.assembly != null)
                return false;
        } else if (!assembly.equals(other.assembly))
            return false;
        if (incidentalBin == null) {
            if (other.incidentalBin != null)
                return false;
        } else if (!incidentalBin.equals(other.incidentalBin))
            return false;
        if (incidentalResultVersion == null) {
            if (other.incidentalResultVersion != null)
                return false;
        } else if (!incidentalResultVersion.equals(other.incidentalResultVersion))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (mapnum == null) {
            if (other.mapnum != null)
                return false;
        } else if (!mapnum.equals(other.mapnum))
            return false;
        if (participant == null) {
            if (other.participant != null)
                return false;
        } else if (!participant.equals(other.participant))
            return false;
        return true;
    }

}
