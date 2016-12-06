package org.renci.binning.dao.refseq.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "refseq", name = "region_group_regions")
@FetchGroups({ @FetchGroup(name = "includeManyToOnes", attributes = { @FetchAttribute(name = "regionGroup") }) })
public class RegionGroupRegion implements Persistable {

    private static final long serialVersionUID = 7705809636894949101L;

    @EmbeddedId
    private RegionGroupRegionPK key;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "region_group_id")
    private RegionGroup regionGroup;

    public RegionGroupRegion() {
        super();
    }

    public RegionGroupRegionPK getKey() {
        return key;
    }

    public void setKey(RegionGroupRegionPK key) {
        this.key = key;
    }

    public RegionGroup getRegionGroup() {
        return regionGroup;
    }

    public void setRegionGroup(RegionGroup regionGroup) {
        this.regionGroup = regionGroup;
    }

    @Override
    public String toString() {
        return String.format("RegionGroupRegion [key=%s]", key);
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
        RegionGroupRegion other = (RegionGroupRegion) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
