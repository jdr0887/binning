package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.annotation.model.AnnotationGene;

@Entity
@Table(schema = "clinbin", name = "incidental_bin_gene")
public class IncidentalBinGene implements Persistable {

    private static final long serialVersionUID = 8992564182838865854L;

    @EmbeddedId
    private IncidentalBinGenePK key;

    @MapsId("incidentalBin")
    @ManyToOne
    @JoinColumn(name = "incidental_bin_id")
    private IncidentalBin incidentalBin;

    @MapsId("gene")
    @ManyToOne
    @JoinColumn(name = "gene_id")
    private AnnotationGene gene;

    @Column(name = "disease", insertable = false, updatable = false)
    private String disease;

    public IncidentalBinGene() {
        super();
    }

    public IncidentalBinGenePK getKey() {
        return key;
    }

    public void setKey(IncidentalBinGenePK key) {
        this.key = key;
    }

    public IncidentalBin getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(IncidentalBin incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    public AnnotationGene getGene() {
        return gene;
    }

    public void setGene(AnnotationGene gene) {
        this.gene = gene;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinGene [key=%s, disease=%s]", key, disease);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((disease == null) ? 0 : disease.hashCode());
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
        IncidentalBinGene other = (IncidentalBinGene) obj;
        if (disease == null) {
            if (other.disease != null)
                return false;
        } else if (!disease.equals(other.disease))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
