package org.renci.binning.dao.annotation.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@Table(schema = "annot", name = "cds_external_ids")
public class AnnotationCodingSequenceExternalIds {

    @EmbeddedId
    private AnnotationCodingSequenceExternalIdsPK key;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "cds_id")
    private AnnotationCodingSequence annotationCodingSequence;

    public AnnotationCodingSequenceExternalIds() {
        super();
    }

    public AnnotationCodingSequenceExternalIdsPK getKey() {
        return key;
    }

    public void setKey(AnnotationCodingSequenceExternalIdsPK key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return String.format("AnnotationCodingSequenceExternalIds [key=%s]", key);
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
        AnnotationCodingSequenceExternalIds other = (AnnotationCodingSequenceExternalIds) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
