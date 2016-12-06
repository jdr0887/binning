package org.renci.binning.dao.var.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AssemblyLocatedVariantPK implements Serializable {

    private static final long serialVersionUID = 745714824781965526L;

    @Column(name = "asm_id", nullable = false)
    private Integer assembly;

    @Column(name = "loc_var_id", nullable = false)
    private Long locatedVariant;

    public AssemblyLocatedVariantPK() {
        super();
    }

    public AssemblyLocatedVariantPK(Integer assembly, Long locatedVariant) {
        super();
        this.assembly = assembly;
        this.locatedVariant = locatedVariant;
    }

    public Integer getAssembly() {
        return assembly;
    }

    public void setAssembly(Integer assembly) {
        this.assembly = assembly;
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    @Override
    public String toString() {
        return String.format("AssemblyLocatedVariantPK [assembly=%s, LocatedVariant=%s]", assembly, locatedVariant);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assembly == null) ? 0 : assembly.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
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
        AssemblyLocatedVariantPK other = (AssemblyLocatedVariantPK) obj;
        if (assembly == null) {
            if (other.assembly != null)
                return false;
        } else if (!assembly.equals(other.assembly))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        return true;
    }

}
