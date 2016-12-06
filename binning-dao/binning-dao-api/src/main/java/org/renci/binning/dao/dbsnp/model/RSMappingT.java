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
@Table(schema = "dbsnp", name = "rs_mapping_t")
public class RSMappingT implements Persistable {

    private static final long serialVersionUID = 5916515490663909853L;

    @EmbeddedId
    private RSMappingTPK key;

    @Lob
    @Column(name = "rs_ids")
    private String rsIds;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    public RSMappingT() {
        super();
    }

    public RSMappingT(RSMappingTPK key) {
        super();
        this.key = key;
    }

    public RSMappingTPK getKey() {
        return key;
    }

    public void setKey(RSMappingTPK key) {
        this.key = key;
    }

    public String getRsIds() {
        return rsIds;
    }

    public void setRsIds(String rsIds) {
        this.rsIds = rsIds;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    @Override
    public String toString() {
        return String.format("RSMappingT [key=%s, rsIds=%s]", key, rsIds);
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
        RSMappingT other = (RSMappingT) obj;
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
