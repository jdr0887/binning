package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DiagnosticGeneGroupVersionPK implements Serializable {

    private static final long serialVersionUID = -8269663894007518236L;

    @Column(name = "dbin_group_version")
    private Integer dbinGroupVersion;

    @Column(name = "dx_id")
    private Integer dx;

    @Column(name = "diagnostic_list_version")
    private Integer diagnosticListVersion;

    public DiagnosticGeneGroupVersionPK() {
        super();
    }

    public Integer getDbinGroupVersion() {
        return dbinGroupVersion;
    }

    public void setDbinGroupVersion(Integer dbinGroupVersion) {
        this.dbinGroupVersion = dbinGroupVersion;
    }

    public Integer getDx() {
        return dx;
    }

    public void setDx(Integer dx) {
        this.dx = dx;
    }

    public Integer getDiagnosticListVersion() {
        return diagnosticListVersion;
    }

    public void setDiagnosticListVersion(Integer diagnosticListVersion) {
        this.diagnosticListVersion = diagnosticListVersion;
    }

    @Override
    public String toString() {
        return String.format("DiagnosticGeneGroupVersionPK [dbinGroupVersion=%s, dx=%s, diagnosticListVersion=%s]", dbinGroupVersion, dx,
                diagnosticListVersion);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dbinGroupVersion == null) ? 0 : dbinGroupVersion.hashCode());
        result = prime * result + ((diagnosticListVersion == null) ? 0 : diagnosticListVersion.hashCode());
        result = prime * result + ((dx == null) ? 0 : dx.hashCode());
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
        DiagnosticGeneGroupVersionPK other = (DiagnosticGeneGroupVersionPK) obj;
        if (dbinGroupVersion == null) {
            if (other.dbinGroupVersion != null)
                return false;
        } else if (!dbinGroupVersion.equals(other.dbinGroupVersion))
            return false;
        if (diagnosticListVersion == null) {
            if (other.diagnosticListVersion != null)
                return false;
        } else if (!diagnosticListVersion.equals(other.diagnosticListVersion))
            return false;
        if (dx == null) {
            if (other.dx != null)
                return false;
        } else if (!dx.equals(other.dx))
            return false;
        return true;
    }

}
