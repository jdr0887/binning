package org.renci.binning.dao.dbsnp.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "dbsnp", name = "snp_mapping_warning")
public class SNPMappingWarning implements Persistable {

    private static final long serialVersionUID = 4545180729831869759L;

    @EmbeddedId
    private SNPMappingWarningPK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @MapsId("snp")
    @ManyToOne
    @JoinColumn(name = "snp_id")
    private SNP snp;

    @MapsId("mappingWarning")
    @ManyToOne
    @JoinColumn(name = "warning_name")
    private MappingWarning mappingWarning;

    public SNPMappingWarning() {
        super();
    }

    public SNPMappingWarning(SNPMappingWarningPK key) {
        super();
        this.key = key;
    }

    public SNPMappingWarningPK getKey() {
        return key;
    }

    public void setKey(SNPMappingWarningPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public SNP getSnp() {
        return snp;
    }

    public void setSnp(SNP snp) {
        this.snp = snp;
    }

    public MappingWarning getMappingWarning() {
        return mappingWarning;
    }

    public void setMappingWarning(MappingWarning mappingWarning) {
        this.mappingWarning = mappingWarning;
    }

    @Override
    public String toString() {
        return String.format("SNPMappingWarning [key=%s]", key);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        SNPMappingWarning other = (SNPMappingWarning) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
