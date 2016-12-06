package org.renci.binning.dao.clinbin.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "diagnostic_gene_group_version")
public class DiagnosticGeneGroupVersion implements Persistable {

    private static final long serialVersionUID = -7848676259877544883L;

    @EmbeddedId
    private DiagnosticGeneGroupVersionPK key;

    @MapsId("dx")
    @ManyToOne
    @JoinColumn(name = "dx_id")
    private DX dx;

    public DiagnosticGeneGroupVersion() {
        super();
    }

    public DiagnosticGeneGroupVersionPK getKey() {
        return key;
    }

    public void setKey(DiagnosticGeneGroupVersionPK key) {
        this.key = key;
    }

    public DX getDx() {
        return dx;
    }

    public void setDx(DX dx) {
        this.dx = dx;
    }

    @Override
    public String toString() {
        return String.format("DiagnosticGeneGroupVersion [key=%s]", key);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dx == null) ? 0 : dx.hashCode());
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
        DiagnosticGeneGroupVersion other = (DiagnosticGeneGroupVersion) obj;
        if (dx == null) {
            if (other.dx != null)
                return false;
        } else if (!dx.equals(other.dx))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
