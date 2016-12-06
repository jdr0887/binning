package org.renci.binning.dao.var.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "var", name = "library")
public class Library implements Persistable {

    private static final long serialVersionUID = 3324902011981084756L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "library_library_id_seq")
    @SequenceGenerator(name = "library_library_id_seq", schema = "var", sequenceName = "library_library_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "library_id")
    private Integer id;

    @Column(name = "htsf_library_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "sample_id")
    private Sample sample;

    public Library(String name) {
        super();
        this.name = name;
    }

    public Library() {
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

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    @Override
    public String toString() {
        return String.format("Library [id=%s, name=%s]", id, name);
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
        Library other = (Library) obj;
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
