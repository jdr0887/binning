package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "phenotypex", uniqueConstraints = { @UniqueConstraint(columnNames = { "phenotype_name" }) })
public class PhenotypeX implements Persistable {

    private static final long serialVersionUID = 2392647483088782553L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phenotypex_phenotype_id_seq")
    @SequenceGenerator(schema = "clinbin", name = "phenotypex_phenotype_id_seq", sequenceName = "phenotypex_phenotype_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "phenotype_id")
    private Integer id;

    @Column(name = "phenotype_name", length = 1024)
    private String name;

    public PhenotypeX() {
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
        return String.format("PhenotypeX [id=%s, name=%s]", id, name);
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
        PhenotypeX other = (PhenotypeX) obj;
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
