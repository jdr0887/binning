package org.renci.binning.dao.var.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.ref.model.GenomeRef;

@Entity
@Table(schema = "var", name = "var_set")
public class VariantSet implements Persistable {

    private static final long serialVersionUID = 6821062280221341220L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "var_set_var_set_id_seq")
    @SequenceGenerator(name = "var_set_var_set_id_seq", schema = "var", sequenceName = "var_set_var_set_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "var_set_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ref_id")
    private GenomeRef genomeRef;

    @OneToMany(mappedBy = "variantSet", fetch = FetchType.LAZY)
    private Set<Assembly> assemblies;

    public VariantSet() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GenomeRef getGenomeRef() {
        return genomeRef;
    }

    public void setGenomeRef(GenomeRef genomeRef) {
        this.genomeRef = genomeRef;
    }

    public Set<Assembly> getAssemblies() {
        return assemblies;
    }

    public void setAssemblies(Set<Assembly> assemblies) {
        this.assemblies = assemblies;
    }

    @Override
    public String toString() {
        return String.format("VariantSet [id=%s]", id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        VariantSet other = (VariantSet) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
