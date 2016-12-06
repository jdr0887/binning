package org.renci.binning.dao.annotation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@Table(schema = "annot", name = "transcr_map_warnings")
public class AnnotationTranscriptionMapWarnings {

    @Id
    @Column(name = "warning_name")
    private String warningName;

    public AnnotationTranscriptionMapWarnings() {
        super();
    }

    public String getWarningName() {
        return warningName;
    }

    public void setWarningName(String warningName) {
        this.warningName = warningName;
    }

    @Override
    public String toString() {
        return String.format("AnnotationTranscriptionMapWarnings [warningName=%s]", warningName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((warningName == null) ? 0 : warningName.hashCode());
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
        AnnotationTranscriptionMapWarnings other = (AnnotationTranscriptionMapWarnings) obj;
        if (warningName == null) {
            if (other.warningName != null)
                return false;
        } else if (!warningName.equals(other.warningName))
            return false;
        return true;
    }

}
