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
@Table(schema = "var", name = "asm")
public class Assembly implements Persistable {

    private static final long serialVersionUID = 6349711189938113203L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asm_asm_id_seq")
    @SequenceGenerator(name = "asm_asm_id_seq", schema = "var", sequenceName = "asm_asm_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "asm_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    @ManyToOne
    @JoinColumn(name = "var_set_id")
    private VariantSet variantSet;

    public Assembly() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public VariantSet getVariantSet() {
        return variantSet;
    }

    public void setVariantSet(VariantSet variantSet) {
        this.variantSet = variantSet;
    }

    @Override
    public String toString() {
        return String.format("Assembly [id=%s]", id);
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
        Assembly other = (Assembly) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
