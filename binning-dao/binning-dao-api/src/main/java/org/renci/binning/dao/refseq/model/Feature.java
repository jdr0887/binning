package org.renci.binning.dao.refseq.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "refseq", name = "feature")
public class Feature implements Persistable {

    private static final long serialVersionUID = -3021365878092927482L;

    @Id
    @Column(name = "refseq_feature_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "feature_type_type_name")
    private FeatureTypes featureTypes;

    @Column(name = "refseq_ver")
    private String refseqVer;

    @Column(name = "note", length = 1023)
    private String note;

    @ManyToOne
    @JoinColumn(name = "loc_region_group_id")
    private RegionGroup regionGroup;

    public Feature() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FeatureTypes getFeatureTypes() {
        return featureTypes;
    }

    public void setFeatureTypes(FeatureTypes featureTypes) {
        this.featureTypes = featureTypes;
    }

    public String getRefseqVer() {
        return refseqVer;
    }

    public void setRefseqVer(String refseqVer) {
        this.refseqVer = refseqVer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RegionGroup getRegionGroup() {
        return regionGroup;
    }

    public void setRegionGroup(RegionGroup regionGroup) {
        this.regionGroup = regionGroup;
    }

    @Override
    public String toString() {
        return String.format("Feature [id=%s, refseqVer=%s, note=%s]", id, refseqVer, note);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((refseqVer == null) ? 0 : refseqVer.hashCode());
        result = prime * result + ((note == null) ? 0 : note.hashCode());
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
        Feature other = (Feature) obj;
        if (note == null) {
            if (other.note != null)
                return false;
        } else if (!note.equals(other.note))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (refseqVer == null) {
            if (other.refseqVer != null)
                return false;
        } else if (!refseqVer.equals(other.refseqVer))
            return false;
        return true;
    }

}
