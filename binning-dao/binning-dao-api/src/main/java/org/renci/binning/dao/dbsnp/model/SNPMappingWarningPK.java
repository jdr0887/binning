package org.renci.binning.dao.dbsnp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SNPMappingWarningPK implements Serializable {

    private static final long serialVersionUID = 4545180729831869759L;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Column(name = "src")
    private String source;

    @Column(name = "snp_id")
    private Integer snp;

    @Column(name = "warning_name")
    private String mappingWarning;

    public SNPMappingWarningPK() {
        super();
    }

    public SNPMappingWarningPK(Long locatedVariant, String source, Integer snp, String mappingWarning) {
        super();
        this.locatedVariant = locatedVariant;
        this.source = source;
        this.snp = snp;
        this.mappingWarning = mappingWarning;
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

    public Integer getSnp() {
        return snp;
    }

    public void setSnp(Integer snp) {
        this.snp = snp;
    }

    public String getMappingWarning() {
        return mappingWarning;
    }

    public void setMappingWarning(String mappingWarning) {
        this.mappingWarning = mappingWarning;
    }

    @Override
    public String toString() {
        return String.format("SNPMappingWarningPK [locatedVariant=%s, source=%s, snp=%s, mappingWarning=%s]", locatedVariant, source, snp,
                mappingWarning);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((mappingWarning == null) ? 0 : mappingWarning.hashCode());
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
        SNPMappingWarningPK other = (SNPMappingWarningPK) obj;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (mappingWarning == null) {
            if (other.mappingWarning != null)
                return false;
        } else if (!mappingWarning.equals(other.mappingWarning))
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
