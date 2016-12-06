package org.renci.binning.dao.var.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "var", name = "asm_loc_var")
public class AssemblyLocatedVariant implements Persistable {

    private static final long serialVersionUID = -5771832456633119719L;

    @EmbeddedId
    private AssemblyLocatedVariantPK key;

    @MapsId("assembly")
    @ManyToOne
    @JoinColumn(name = "asm_id", nullable = false)
    private Assembly assembly;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @Column(name = "homozygous")
    private Boolean homozygous;

    @Column(name = "genotype_qual")
    private Double genotypeQuality;

    public AssemblyLocatedVariant() {
        super();
    }

    public AssemblyLocatedVariant(AssemblyLocatedVariantPK key) {
        super();
        this.key = key;
    }

    public AssemblyLocatedVariant(AssemblyLocatedVariantPK key, Boolean homozygous, Double genotypeQuality) {
        super();
        this.key = key;
        this.homozygous = homozygous;
        this.genotypeQuality = genotypeQuality;
    }

    public Assembly getAssembly() {
        return assembly;
    }

    public void setAssembly(Assembly assembly) {
        this.assembly = assembly;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public AssemblyLocatedVariantPK getKey() {
        return key;
    }

    public void setKey(AssemblyLocatedVariantPK key) {
        this.key = key;
    }

    public Boolean getHomozygous() {
        return homozygous;
    }

    public void setHomozygous(Boolean homozygous) {
        this.homozygous = homozygous;
    }

    public Double getGenotypeQuality() {
        return genotypeQuality;
    }

    public void setGenotypeQuality(Double genotypeQuality) {
        this.genotypeQuality = genotypeQuality;
    }

    @Override
    public String toString() {
        return String.format("AssemblyLocatedVariant [key=%s, homozygous=%s, genotypeQuality=%s]", key, homozygous, genotypeQuality);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genotypeQuality == null) ? 0 : genotypeQuality.hashCode());
        result = prime * result + ((homozygous == null) ? 0 : homozygous.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
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
        AssemblyLocatedVariant other = (AssemblyLocatedVariant) obj;
        if (genotypeQuality == null) {
            if (other.genotypeQuality != null)
                return false;
        } else if (!genotypeQuality.equals(other.genotypeQuality))
            return false;
        if (homozygous == null) {
            if (other.homozygous != null)
                return false;
        } else if (!homozygous.equals(other.homozygous))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
