package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BinResultsFinalRiskXPK implements Serializable {

    private static final long serialVersionUID = -4432334844095779990L;

    @Column(name = "participant", length = 50)
    private String participant;

    @Column(name = "incidental_bin_id")
    private Integer incidentalBin;

    @Column(name = "incidental_result_version")
    private Integer incidentalResultVersion;

    @Column(name = "asm_id")
    private Integer assembly;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Column(name = "haplotype_id")
    private Integer haplotypeId;

    @Column(name = "phenotype_id")
    private Integer phenotype;

    public BinResultsFinalRiskXPK() {
        super();
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

    public Integer getIncidentalResultVersion() {
        return incidentalResultVersion;
    }

    public void setIncidentalResultVersion(Integer incidentalResultVersion) {
        this.incidentalResultVersion = incidentalResultVersion;
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

    public Integer getHaplotypeId() {
        return haplotypeId;
    }

    public void setHaplotypeId(Integer haplotypeId) {
        this.haplotypeId = haplotypeId;
    }

    public Integer getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(Integer phenotype) {
        this.phenotype = phenotype;
    }

    @Override
    public String toString() {
        return String.format(
                "BinResultsFinalRiskXPK [participant=%s, incidentalBin=%s, incidentalResultVersion=%s, assembly=%s, locatedVariant=%s, haplotypeId=%s, phenotype=%s]",
                participant, incidentalBin, incidentalResultVersion, assembly, locatedVariant, haplotypeId, phenotype);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assembly == null) ? 0 : assembly.hashCode());
        result = prime * result + ((haplotypeId == null) ? 0 : haplotypeId.hashCode());
        result = prime * result + ((incidentalBin == null) ? 0 : incidentalBin.hashCode());
        result = prime * result + ((incidentalResultVersion == null) ? 0 : incidentalResultVersion.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((participant == null) ? 0 : participant.hashCode());
        result = prime * result + ((phenotype == null) ? 0 : phenotype.hashCode());
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
        BinResultsFinalRiskXPK other = (BinResultsFinalRiskXPK) obj;
        if (assembly == null) {
            if (other.assembly != null)
                return false;
        } else if (!assembly.equals(other.assembly))
            return false;
        if (haplotypeId == null) {
            if (other.haplotypeId != null)
                return false;
        } else if (!haplotypeId.equals(other.haplotypeId))
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
        if (participant == null) {
            if (other.participant != null)
                return false;
        } else if (!participant.equals(other.participant))
            return false;
        if (phenotype == null) {
            if (other.phenotype != null)
                return false;
        } else if (!phenotype.equals(other.phenotype))
            return false;
        return true;
    }

}
