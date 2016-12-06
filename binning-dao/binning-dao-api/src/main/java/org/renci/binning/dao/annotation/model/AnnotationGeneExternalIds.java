package org.renci.binning.dao.annotation.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "annot", name = "gene_external_ids")
public class AnnotationGeneExternalIds implements Persistable {

    private static final long serialVersionUID = 5179600096320755261L;

    @EmbeddedId
    private AnnotationGeneExternalIdsPK key;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "gene_id")
    private AnnotationGene gene;

    public AnnotationGeneExternalIds() {
        super();
    }

    public AnnotationGeneExternalIdsPK getKey() {
        return key;
    }

    public void setKey(AnnotationGeneExternalIdsPK key) {
        this.key = key;
    }

    public AnnotationGene getGene() {
        return gene;
    }

    public void setGene(AnnotationGene gene) {
        this.gene = gene;
    }

    @Override
    public String toString() {
        return String.format("AnnotationGeneExternalIds [key=%s]", key);
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
        AnnotationGeneExternalIds other = (AnnotationGeneExternalIds) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
