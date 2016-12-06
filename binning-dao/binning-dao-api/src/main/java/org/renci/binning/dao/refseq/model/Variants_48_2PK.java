package org.renci.binning.dao.refseq.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Variants_48_2PK implements Serializable {

    private static final long serialVersionUID = 2179690321834683705L;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Column(name = "chromosome")
    private String chromosome;

    @Column(name = "pos")
    private Integer pos;

    @Lob
    @Column(name = "type")
    private String type;

    @Column(name = "transcr", length = 31)
    private String transcr;

    @Lob
    @Column(name = "loc_type")
    private String locationType;

    @Lob
    @Column(name = "variant_effect")
    private String variantEffect;

    @Column(name = "mapnum")
    private Integer mapnum;

    public Variants_48_2PK() {
        super();
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTranscr() {
        return transcr;
    }

    public void setTranscr(String transcr) {
        this.transcr = transcr;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getVariantEffect() {
        return variantEffect;
    }

    public void setVariantEffect(String variantEffect) {
        this.variantEffect = variantEffect;
    }

    public Integer getMapnum() {
        return mapnum;
    }

    public void setMapnum(Integer mapnum) {
        this.mapnum = mapnum;
    }

    @Override
    public String toString() {
        return String.format(
                "Variants_61_2PK [locatedVariant=%s, chromosome=%s, pos=%s, type=%s, transcr=%s, locationType=%s, variantEffect=%s, mapnum=%s]",
                locatedVariant, chromosome, pos, type, transcr, locationType, variantEffect, mapnum);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chromosome == null) ? 0 : chromosome.hashCode());
        result = prime * result + ((locationType == null) ? 0 : locationType.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((mapnum == null) ? 0 : mapnum.hashCode());
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
        result = prime * result + ((transcr == null) ? 0 : transcr.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((variantEffect == null) ? 0 : variantEffect.hashCode());
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
        Variants_48_2PK other = (Variants_48_2PK) obj;
        if (chromosome == null) {
            if (other.chromosome != null)
                return false;
        } else if (!chromosome.equals(other.chromosome))
            return false;
        if (locationType == null) {
            if (other.locationType != null)
                return false;
        } else if (!locationType.equals(other.locationType))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (mapnum == null) {
            if (other.mapnum != null)
                return false;
        } else if (!mapnum.equals(other.mapnum))
            return false;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        if (transcr == null) {
            if (other.transcr != null)
                return false;
        } else if (!transcr.equals(other.transcr))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (variantEffect == null) {
            if (other.variantEffect != null)
                return false;
        } else if (!variantEffect.equals(other.variantEffect))
            return false;
        return true;
    }

}
