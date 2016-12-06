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
import javax.persistence.UniqueConstraint;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "var", name = "sample", uniqueConstraints = { @UniqueConstraint(columnNames = { "project_name", "sample_name" }) })
public class Sample implements Persistable {

    private static final long serialVersionUID = 1409603720862058803L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sample_sample_id_seq")
    @SequenceGenerator(schema = "var", name = "sample_sample_id_seq", sequenceName = "sample_sample_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "sample_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "project_name")
    private Project project;

    @Column(name = "sample_name")
    private String name;

    public Sample() {
        super();
    }

    public Sample(String name) {
        super();
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Sample [id=%s, name=%s]", id, name);
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
        Sample other = (Sample) obj;
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
