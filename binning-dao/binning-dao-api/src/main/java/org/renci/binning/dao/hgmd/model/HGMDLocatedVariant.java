package org.renci.binning.dao.hgmd.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "hgmd", name = "hgmd_loc_var")
public class HGMDLocatedVariant implements Persistable {

    private static final long serialVersionUID = -2133260169454893320L;

    @EmbeddedId
    private HGMDLocatedVariantPK key;

    @Lob
    @Column(name = "tag")
    private String tag;

    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    public HGMDLocatedVariant() {
        super();
    }

    public HGMDLocatedVariantPK getKey() {
        return key;
    }

    public void setKey(HGMDLocatedVariantPK key) {
        this.key = key;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    @Override
    public String toString() {
        return String.format("HGMDLocatedVariant [key=%s, tag=%s]", key, tag);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
        HGMDLocatedVariant other = (HGMDLocatedVariant) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        return true;
    }

}
