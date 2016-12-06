package org.renci.binning.dao.ref.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "ref", name = "genome_ref_seq_loc")
public class GenomeRefSeqLocation implements Persistable {

    private static final long serialVersionUID = 8237639060154518282L;

    @EmbeddedId
    private GenomeRefSeqLocationPK key;

    @Column(name = "ref_pos")
    private Integer position;

    @Column(name = "ref_base", length = 1)
    private String base;

    public GenomeRefSeqLocation() {
        super();
    }

    public GenomeRefSeqLocationPK getKey() {
        return key;
    }

    public void setKey(GenomeRefSeqLocationPK key) {
        this.key = key;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    @Override
    public String toString() {
        return String.format("GenomeRefSeqLocation [key=%s, position=%s, base=%s]", key, position, base);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
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
        GenomeRefSeqLocation other = (GenomeRefSeqLocation) obj;
        if (base == null) {
            if (other.base != null)
                return false;
        } else if (!base.equals(other.base))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }

}
