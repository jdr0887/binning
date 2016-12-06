package org.renci.binning.dao.refseq.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Variants_78_4PK implements Serializable {

    private static final long serialVersionUID = 5655108968471172030L;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Column(name = "chromosome")
    private String genomeRefSeq;

    @Column(name = "pos")
    private Integer position;

    @Column(name = "type")
    private String variantType;

    @Column(name = "transcr", length = 31)
    private String transcript;

    @Column(name = "loc_type")
    private String locationType;

    @Column(name = "variant_effect")
    private String variantEffect;

    @Column(name = "mapnum")
    private Integer mapNumber;

    public Variants_78_4PK() {
        super();
    }

    public Variants_78_4PK(Long locatedVariant, String genomeRefSeq, Integer position, String variantType, String transcript,
            String locationType, String variantEffect, Integer mapNumber) {
        super();
        this.locatedVariant = locatedVariant;
        this.genomeRefSeq = genomeRefSeq;
        this.position = position;
        this.variantType = variantType;
        this.transcript = transcript;
        this.locationType = locationType;
        this.variantEffect = variantEffect;
        this.mapNumber = mapNumber;
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public String getGenomeRefSeq() {
        return genomeRefSeq;
    }

    public void setGenomeRefSeq(String genomeRefSeq) {
        this.genomeRefSeq = genomeRefSeq;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
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

    public Integer getMapNumber() {
        return mapNumber;
    }

    public void setMapNumber(Integer mapNumber) {
        this.mapNumber = mapNumber;
    }

    @Override
    public String toString() {
        return String.format(
                "Variants_61_2PK [locatedVariant=%s, genomeRefSeq=%s, position=%s, variantType=%s, transcript=%s, locationType=%s, variantEffect=%s, mapNumber=%s]",
                locatedVariant, genomeRefSeq, position, variantType, transcript, locationType, variantEffect, mapNumber);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genomeRefSeq == null) ? 0 : genomeRefSeq.hashCode());
        result = prime * result + ((locationType == null) ? 0 : locationType.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((mapNumber == null) ? 0 : mapNumber.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((transcript == null) ? 0 : transcript.hashCode());
        result = prime * result + ((variantType == null) ? 0 : variantType.hashCode());
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
        Variants_78_4PK other = (Variants_78_4PK) obj;
        if (genomeRefSeq == null) {
            if (other.genomeRefSeq != null)
                return false;
        } else if (!genomeRefSeq.equals(other.genomeRefSeq))
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
        if (mapNumber == null) {
            if (other.mapNumber != null)
                return false;
        } else if (!mapNumber.equals(other.mapNumber))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (transcript == null) {
            if (other.transcript != null)
                return false;
        } else if (!transcript.equals(other.transcript))
            return false;
        if (variantType == null) {
            if (other.variantType != null)
                return false;
        } else if (!variantType.equals(other.variantType))
            return false;
        if (variantEffect == null) {
            if (other.variantEffect != null)
                return false;
        } else if (!variantEffect.equals(other.variantEffect))
            return false;
        return true;
    }

}
