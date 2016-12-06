package org.renci.binning.dao.dbsnp.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.renci.binning.dao.Persistable;

@Embeddable
public class SNPMappingPK implements Persistable {

    private static final long serialVersionUID = -7892149256878922346L;

    @Column(name = "snp_id")
    private Integer snp;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Column(name = "src")
    private String source;

    public SNPMappingPK() {
        super();
    }

    public SNPMappingPK(Integer snp, Long locatedVariant, String source) {
        super();
        this.snp = snp;
        this.locatedVariant = locatedVariant;
        this.source = source;
    }

    public Integer getSnp() {
        return snp;
    }

    public void setSnp(Integer snp) {
        this.snp = snp;
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return String.format("SNPMappingPK [snp=%s, locatedVariant=%s, source=%s]", snp, locatedVariant, source);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((snp == null) ? 0 : snp.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        SNPMappingPK other = (SNPMappingPK) obj;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (snp == null) {
            if (other.snp != null)
                return false;
        } else if (!snp.equals(other.snp))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }

}
