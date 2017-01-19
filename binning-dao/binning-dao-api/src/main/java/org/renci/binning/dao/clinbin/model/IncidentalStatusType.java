package org.renci.binning.dao.clinbin.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.renci.binning.dao.Persistable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncidentalStatusType", propOrder = {})
@XmlRootElement(name = "incidentalStatusType")
@Entity
@Table(schema = "clinbin", name = "incidental_status_type")
@Cacheable
public class IncidentalStatusType implements Persistable {

    private static final long serialVersionUID = 4612957769574414877L;

    @Id
    @Column(name = "status")
    @Lob
    private String name;

    public IncidentalStatusType() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("IncidentalStatusType [name=%s]", name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        IncidentalStatusType other = (IncidentalStatusType) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
