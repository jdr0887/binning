package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HaplotypeXPK implements Serializable {

    private static final long serialVersionUID = -294078799819106503L;

    @Column(name = "haplotype_id")
    private Integer id;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Column(name = "allele", length = 1024)
    private String allele;

    public HaplotypeXPK() {
        super();
    }

    public HaplotypeXPK(Integer id, Long locatedVariant, String allele) {
        super();
        this.id = id;
        this.locatedVariant = locatedVariant;
        this.allele = allele;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public String getAllele() {
        return allele;
    }

    public void setAllele(String allele) {
        this.allele = allele;
    }

    @Override
    public String toString() {
        return String.format("HaplotypeXPK [id=%s, locatedVariant=%s, allele=%s]", id, locatedVariant, allele);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((allele == null) ? 0 : allele.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        HaplotypeXPK other = (HaplotypeXPK) obj;
        if (allele == null) {
            if (other.allele != null)
                return false;
        } else if (!allele.equals(other.allele))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        return true;
    }

}
