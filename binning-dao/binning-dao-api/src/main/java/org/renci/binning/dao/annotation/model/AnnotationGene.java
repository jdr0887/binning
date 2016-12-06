package org.renci.binning.dao.annotation.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.clinbin.model.DiagnosticGene;
import org.renci.binning.dao.clinbin.model.IncidentalBinGeneX;
import org.renci.binning.dao.refseq.model.Variants_61_2;

@Entity
@Table(schema = "annot", name = "gene")
public class AnnotationGene implements Persistable {

    private static final long serialVersionUID = 3422741731748311729L;

    @Id
    @Column(name = "gene_id")
    private Integer id;

    @Column(name = "preferred_name")
    private String preferredName;

    @Column(name = "preferred_descr", length = 4095)
    private String preferredDescription;

    @OneToMany(mappedBy = "gene", fetch = FetchType.LAZY)
    private Set<AnnotationGeneExternalIds> externals;

    @OneToMany(mappedBy = "gene", fetch = FetchType.LAZY)
    private Set<AnnotationGeneSynonyms> synonyms;

    @OneToMany(mappedBy = "gene", fetch = FetchType.LAZY)
    private Set<DiagnosticGene> diagnosticGenes;

    @OneToMany(mappedBy = "gene", fetch = FetchType.LAZY)
    private Set<IncidentalBinGeneX> incidentalGenes;

    @OneToMany(mappedBy = "gene", fetch = FetchType.LAZY)
    private Set<Variants_61_2> variants;

    public AnnotationGene() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Variants_61_2> getVariants() {
        return variants;
    }

    public void setVariants(Set<Variants_61_2> variants) {
        this.variants = variants;
    }

    public Set<AnnotationGeneSynonyms> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<AnnotationGeneSynonyms> synonyms) {
        this.synonyms = synonyms;
    }

    public Set<DiagnosticGene> getDiagnosticGenes() {
        return diagnosticGenes;
    }

    public void setDiagnosticGenes(Set<DiagnosticGene> diagnosticGenes) {
        this.diagnosticGenes = diagnosticGenes;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getPreferredDescription() {
        return preferredDescription;
    }

    public void setPreferredDescription(String preferredDescription) {
        this.preferredDescription = preferredDescription;
    }

    public Set<AnnotationGeneExternalIds> getExternals() {
        return externals;
    }

    public void setExternals(Set<AnnotationGeneExternalIds> externals) {
        this.externals = externals;
    }

    public Set<IncidentalBinGeneX> getIncidentalGenes() {
        return incidentalGenes;
    }

    public void setIncidentalGenes(Set<IncidentalBinGeneX> incidentalGenes) {
        this.incidentalGenes = incidentalGenes;
    }

    @Override
    public String toString() {
        return String.format("AnnotationGene [id=%s, preferredName=%s, preferredDescription=%s]", id, preferredName, preferredDescription);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((preferredDescription == null) ? 0 : preferredDescription.hashCode());
        result = prime * result + ((preferredName == null) ? 0 : preferredName.hashCode());
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
        AnnotationGene other = (AnnotationGene) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (preferredDescription == null) {
            if (other.preferredDescription != null)
                return false;
        } else if (!preferredDescription.equals(other.preferredDescription))
            return false;
        if (preferredName == null) {
            if (other.preferredName != null)
                return false;
        } else if (!preferredName.equals(other.preferredName))
            return false;
        return true;
    }

}
