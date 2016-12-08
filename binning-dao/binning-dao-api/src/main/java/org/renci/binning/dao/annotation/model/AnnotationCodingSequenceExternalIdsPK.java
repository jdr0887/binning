package org.renci.binning.dao.annotation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AnnotationCodingSequenceExternalIdsPK implements Serializable {

    private static final long serialVersionUID = -6426105654816246883L;

    @Column(name = "cds_id")
    private Integer annotationCodingSequence;

    @Column(name = "namespace", length = 31)
    private String namespace;

    @Column(name = "namespace_ver", length = 31)
    private String namespaceVersion;

    @Column(name = "gene_external_id")
    private Integer geneExternalId;

    public AnnotationCodingSequenceExternalIdsPK() {
        super();
    }

    public Integer getAnnotationCodingSequence() {
        return annotationCodingSequence;
    }

    public void setAnnotationCodingSequence(Integer annotationCodingSequence) {
        this.annotationCodingSequence = annotationCodingSequence;
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

    public String getNamespaceVersion() {
        return namespaceVersion;
    }

    public void setNamespaceVersion(String namespaceVersion) {
        this.namespaceVersion = namespaceVersion;
    }

    @Override
    public String toString() {
        return String.format(
                "AnnotationCodingSequenceExternalIdsPK [annotationCodingSequence=%s, namespace=%s, namespaceVersion=%s, geneExternalId=%s]",
                annotationCodingSequence, namespace, namespaceVersion, geneExternalId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((annotationCodingSequence == null) ? 0 : annotationCodingSequence.hashCode());
        result = prime * result + ((geneExternalId == null) ? 0 : geneExternalId.hashCode());
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
        result = prime * result + ((namespaceVersion == null) ? 0 : namespaceVersion.hashCode());
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
        if (annotationCodingSequence == null) {
            if (other.annotationCodingSequence != null)
                return false;
        } else if (!annotationCodingSequence.equals(other.annotationCodingSequence))
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
        if (namespaceVersion == null) {
            if (other.namespaceVersion != null)
                return false;
        } else if (!namespaceVersion.equals(other.namespaceVersion))
            return false;
        return true;
    }

}
