package org.renci.binning.dao.genome1k.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "gen1000", name = "indel_freq")
@NamedQueries({
        @NamedQuery(name = "OneThousandGenomeIndelFrequency.findByLocatedVariantIdAndVersion", query = "FROM OneThousandGenomeIndelFrequency a join a.LocatedVariant b where b.id = :LocatedVariantId and a.key.version = :version") })
public class OneThousandGenomeIndelFrequency implements Persistable {

    private static final long serialVersionUID = 5872322341385565072L;

    @EmbeddedId
    private OneThousandGenomeIndelFrequencyPK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @Column(name = "allele_freq")
    private Double alleleFreq;

    @Column(name = "num_samples")
    private Integer numSamples;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "homopolymer_count")
    private Integer homopolymerCount;

    @Column(name = "forward_var_count")
    private Integer forwardVarCount;

    @Column(name = "reverse_var_count")
    private Integer reverseVarCount;

    public OneThousandGenomeIndelFrequency() {
        super();
    }

    public OneThousandGenomeIndelFrequencyPK getKey() {
        return key;
    }

    public void setKey(OneThousandGenomeIndelFrequencyPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public Double getAlleleFreq() {
        return alleleFreq;
    }

    public void setAlleleFreq(Double alleleFreq) {
        this.alleleFreq = alleleFreq;
    }

    public Integer getNumSamples() {
        return numSamples;
    }

    public void setNumSamples(Integer numSamples) {
        this.numSamples = numSamples;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getHomopolymerCount() {
        return homopolymerCount;
    }

    public void setHomopolymerCount(Integer homopolymerCount) {
        this.homopolymerCount = homopolymerCount;
    }

    public Integer getForwardVarCount() {
        return forwardVarCount;
    }

    public void setForwardVarCount(Integer forwardVarCount) {
        this.forwardVarCount = forwardVarCount;
    }

    public Integer getReverseVarCount() {
        return reverseVarCount;
    }

    public void setReverseVarCount(Integer reverseVarCount) {
        this.reverseVarCount = reverseVarCount;
    }

    @Override
    public String toString() {
        return String.format(
                "IndelFrequency [alleleFreq=%s, numSamples=%s, depth=%s, homopolymerCount=%s, forwardVarCount=%s, reverseVarCount=%s]",
                alleleFreq, numSamples, depth, homopolymerCount, forwardVarCount, reverseVarCount);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alleleFreq == null) ? 0 : alleleFreq.hashCode());
        result = prime * result + ((depth == null) ? 0 : depth.hashCode());
        result = prime * result + ((forwardVarCount == null) ? 0 : forwardVarCount.hashCode());
        result = prime * result + ((homopolymerCount == null) ? 0 : homopolymerCount.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((numSamples == null) ? 0 : numSamples.hashCode());
        result = prime * result + ((reverseVarCount == null) ? 0 : reverseVarCount.hashCode());
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
        OneThousandGenomeIndelFrequency other = (OneThousandGenomeIndelFrequency) obj;
        if (alleleFreq == null) {
            if (other.alleleFreq != null)
                return false;
        } else if (!alleleFreq.equals(other.alleleFreq))
            return false;
        if (depth == null) {
            if (other.depth != null)
                return false;
        } else if (!depth.equals(other.depth))
            return false;
        if (forwardVarCount == null) {
            if (other.forwardVarCount != null)
                return false;
        } else if (!forwardVarCount.equals(other.forwardVarCount))
            return false;
        if (homopolymerCount == null) {
            if (other.homopolymerCount != null)
                return false;
        } else if (!homopolymerCount.equals(other.homopolymerCount))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (numSamples == null) {
            if (other.numSamples != null)
                return false;
        } else if (!numSamples.equals(other.numSamples))
            return false;
        if (reverseVarCount == null) {
            if (other.reverseVarCount != null)
                return false;
        } else if (!reverseVarCount.equals(other.reverseVarCount))
            return false;
        return true;
    }

}
