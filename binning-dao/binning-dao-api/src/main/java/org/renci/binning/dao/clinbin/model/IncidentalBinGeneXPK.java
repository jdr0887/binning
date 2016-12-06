package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncidentalBinGeneXPK implements Serializable {

    private static final long serialVersionUID = 5774709424660804712L;

    @Column(name = "incidental_bin_id")
    private Integer incidentalBin;

    @Column(name = "gene_id")
    private Integer gene;

    @Column(name = "incidental_bin_version")
    private Integer version;

    public IncidentalBinGeneXPK() {
        super();
    }

    public IncidentalBinGeneXPK(Integer incidentalBin, Integer gene, Integer version) {
        super();
        this.incidentalBin = incidentalBin;
        this.gene = gene;
        this.version = version;
    }

    public Integer getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(Integer incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    public Integer getGene() {
        return gene;
    }

    public void setGene(Integer gene) {
        this.gene = gene;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinGeneXPK [incidentalBin=%s, gene=%s, version=%s]", incidentalBin, gene, version);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gene == null) ? 0 : gene.hashCode());
        result = prime * result + ((incidentalBin == null) ? 0 : incidentalBin.hashCode());
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
        IncidentalBinGeneXPK other = (IncidentalBinGeneXPK) obj;
        if (gene == null) {
            if (other.gene != null)
                return false;
        } else if (!gene.equals(other.gene))
            return false;
        if (incidentalBin == null) {
            if (other.incidentalBin != null)
                return false;
        } else if (!incidentalBin.equals(other.incidentalBin))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
