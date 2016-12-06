package org.renci.binning.dao.annotation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AnnotationCodingSequenceExternalIdsPK implements Serializable {

    private static final long serialVersionUID = -6426105654816246883L;

    @Column(name = "cds_id")
    private Integer cdsId;

    @Column(name = "namespace", length = 31)
    private String namespace;

    @Column(name = "namespace_ver", length = 31)
    private String namespaceVer;

    @Column(name = "gene_external_id")
    private Integer geneExternalId;

    public AnnotationCodingSequenceExternalIdsPK() {
        super();
    }

    public Integer getCdsId() {
        return cdsId;
    }

    public void setCdsId(Integer cdsId) {
        this.cdsId = cdsId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Integer getGeneExternalId() {
        return geneExternalId;
    }

    public void setGeneExternalId(Integer geneExternalId) {
        this.geneExternalId = geneExternalId;
    }

    @Override
    public String toString() {
        return String.format("AnnotationCodingSequenceExternalIdsPK [cdsId=%s, namespace=%s, namespaceVer=%s, geneExternalId=%s]", cdsId,
                namespace, namespaceVer, geneExternalId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cdsId == null) ? 0 : cdsId.hashCode());
        result = prime * result + ((geneExternalId == null) ? 0 : geneExternalId.hashCode());
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
        result = prime * result + ((namespaceVer == null) ? 0 : namespaceVer.hashCode());
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
        AnnotationCodingSequenceExternalIdsPK other = (AnnotationCodingSequenceExternalIdsPK) obj;
        if (cdsId == null) {
            if (other.cdsId != null)
                return false;
        } else if (!cdsId.equals(other.cdsId))
            return false;
        if (geneExternalId == null) {
            if (other.geneExternalId != null)
                return false;
        } else if (!geneExternalId.equals(other.geneExternalId))
            return false;
        if (namespace == null) {
            if (other.namespace != null)
                return false;
        } else if (!namespace.equals(other.namespace))
            return false;
        if (namespaceVer == null) {
            if (other.namespaceVer != null)
                return false;
        } else if (!namespaceVer.equals(other.namespaceVer))
            return false;
        return true;
    }

}
