package org.renci.binning.dao.dbsnp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "dbsnp", name = "snp_mapping_agg")
public class SNPMappingAgg implements Persistable {

    private static final long serialVersionUID = -4600354212595708650L;

    @EmbeddedId
    private SNPMappingAggPK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @Lob
    @Column(name = "rs_ids")
    private String rsIds;

    public SNPMappingAgg() {
        super();
    }

    public SNPMappingAgg(SNPMappingAggPK key) {
        super();
        this.key = key;
    }

    public SNPMappingAggPK getKey() {
        return key;
    }

    public void setKey(SNPMappingAggPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public String getRsIds() {
        return rsIds;
    }

    public void setRsIds(String rsIds) {
        this.rsIds = rsIds;
    }

    @Override
    public String toString() {
        return String.format("SNPMappingAgg [key=%s, rsIds=%s]", key, rsIds);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((rsIds == null) ? 0 : rsIds.hashCode());
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
        SNPMappingAgg other = (SNPMappingAgg) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (rsIds == null) {
            if (other.rsIds != null)
                return false;
        } else if (!rsIds.equals(other.rsIds))
            return false;
        return true;
    }

}
