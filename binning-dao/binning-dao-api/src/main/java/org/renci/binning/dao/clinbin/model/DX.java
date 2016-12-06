package org.renci.binning.dao.clinbin.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "dx", uniqueConstraints = { @UniqueConstraint(columnNames = { "dx_name" }) })
@NamedQueries({ @NamedQuery(name = "DX.findAll", query = "SELECT a FROM DX a order by a.name") })
@Cacheable
public class DX implements Persistable {

    private static final long serialVersionUID = 7208210752522886524L;

    @Id
    @Column(name = "dx_id")
    private Integer id;

    @Column(name = "dx_name", length = 1024)
    private String name;

    public DX() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("DX [id=%s, name=%s]", id, name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        DX other = (DX) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
