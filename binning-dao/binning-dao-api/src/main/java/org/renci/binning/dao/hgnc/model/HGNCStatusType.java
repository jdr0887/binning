package org.renci.binning.dao.hgnc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "hgnc", name = "status_type")
public class HGNCStatusType implements Persistable {

    private static final long serialVersionUID = 1069297528359738574L;

    @Id
    @Column(name = "status_type_name")
    private String name;

    public HGNCStatusType() {
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
        return String.format("StatusType [name=%s]", name);
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
        HGNCStatusType other = (HGNCStatusType) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
