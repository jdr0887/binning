package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncidentalBinHaplotypeXPK implements Serializable {

    private static final long serialVersionUID = 2946128706055452546L;

    @Column(name = "incidental_bin_id")
    private Integer incidentalBin;

    @Column(name = "incidental_bin_version")
    private Integer incidentalBinVersion;

    @Column(name = "haplotype_id")
    private Integer haplotype;

    @Column(name = "phenotype_id")
    private Integer phenotype;

    public IncidentalBinHaplotypeXPK() {
        super();
    }

    public IncidentalBinHaplotypeXPK(Integer incidentalBin, Integer incidentalBinVersion, Integer haplotype, Integer phenotype) {
        super();
        this.incidentalBin = incidentalBin;
        this.incidentalBinVersion = incidentalBinVersion;
        this.haplotype = haplotype;
        this.phenotype = phenotype;
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

    public Integer getHaplotype() {
        return haplotype;
    }

    public void setHaplotype(Integer haplotype) {
        this.haplotype = haplotype;
    }

    public Integer getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(Integer phenotype) {
        this.phenotype = phenotype;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinHaplotypeXPK [incidentalBin=%s, incidentalBinVersion=%s, haplotype=%s, phenotype=%s]",
                incidentalBin, incidentalBinVersion, haplotype, phenotype);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((haplotype == null) ? 0 : haplotype.hashCode());
        result = prime * result + ((incidentalBin == null) ? 0 : incidentalBin.hashCode());
        result = prime * result + ((incidentalBinVersion == null) ? 0 : incidentalBinVersion.hashCode());
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
        IncidentalBinHaplotypeXPK other = (IncidentalBinHaplotypeXPK) obj;
        if (haplotype == null) {
            if (other.haplotype != null)
                return false;
        } else if (!haplotype.equals(other.haplotype))
            return false;
        if (incidentalBin == null) {
            if (other.incidentalBin != null)
                return false;
        } else if (!incidentalBin.equals(other.incidentalBin))
            return false;
        if (incidentalBinVersion == null) {
            if (other.incidentalBinVersion != null)
                return false;
        } else if (!incidentalBinVersion.equals(other.incidentalBinVersion))
            return false;
        if (phenotype == null) {
            if (other.phenotype != null)
                return false;
        } else if (!phenotype.equals(other.phenotype))
            return false;
        return true;
    }

}
