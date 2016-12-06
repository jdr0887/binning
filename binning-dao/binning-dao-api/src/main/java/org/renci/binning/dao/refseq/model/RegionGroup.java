package org.renci.binning.dao.refseq.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "refseq", name = "region_group")
@FetchGroups({
        @FetchGroup(name = "includeManyToOnes", attributes = { @FetchAttribute(name = "transcript"),
                @FetchAttribute(name = "groupingType") }),
        @FetchGroup(name = "includeRegionGroupRegions", fetchGroups = { "includeManyToOnes" }, attributes = {
                @FetchAttribute(name = "regionGroupRegions") }) })
public class RegionGroup implements Persistable {

    private static final long serialVersionUID = -6293737799108423842L;

    @Id
    @Column(name = "region_group_id")
    private Integer regionGroupId;

    @ManyToOne
    @JoinColumn(name = "transcr_ver_id")
    private Transcript transcript;

    @ManyToOne
    @JoinColumn(name = "grouping_type")
    private GroupingType groupingType;

    @OneToMany(mappedBy = "regionGroup", fetch = FetchType.LAZY)
    protected Set<RegionGroupRegion> regionGroupRegions;

    @OneToMany(mappedBy = "regionGroup", fetch = FetchType.LAZY)
    protected Set<Feature> features;

    @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
    private Set<RefSeqGene> refSeqGenes;

    @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
    private Set<RefSeqCodingSequence> refSeqCodingSequence;

    public RegionGroup() {
        super();
    }

    public Set<RefSeqCodingSequence> getRefSeqCodingSequence() {
        return refSeqCodingSequence;
    }

    public void setRefSeqCodingSequence(Set<RefSeqCodingSequence> refSeqCodingSequence) {
        this.refSeqCodingSequence = refSeqCodingSequence;
    }

    public Set<RefSeqGene> getRefSeqGenes() {
        return refSeqGenes;
    }

    public void setRefSeqGenes(Set<RefSeqGene> refSeqGenes) {
        this.refSeqGenes = refSeqGenes;
    }

    public Integer getRegionGroupId() {
        return regionGroupId;
    }

    public void setRegionGroupId(Integer regionGroupId) {
        this.regionGroupId = regionGroupId;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public GroupingType getGroupingType() {
        return groupingType;
    }

    public void setGroupingType(GroupingType groupingType) {
        this.groupingType = groupingType;
    }

    public Set<RegionGroupRegion> getRegionGroupRegions() {
        return regionGroupRegions;
    }

    public void setRegionGroupRegions(Set<RegionGroupRegion> regionGroupRegions) {
        this.regionGroupRegions = regionGroupRegions;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return String.format("RegionGroup [regionGroupId=%s]", regionGroupId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((features == null) ? 0 : features.hashCode());
        result = prime * result + ((groupingType == null) ? 0 : groupingType.hashCode());
        result = prime * result + ((regionGroupId == null) ? 0 : regionGroupId.hashCode());
        result = prime * result + ((transcript == null) ? 0 : transcript.hashCode());
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
        RegionGroup other = (RegionGroup) obj;
        if (features == null) {
            if (other.features != null)
                return false;
        } else if (!features.equals(other.features))
            return false;
        if (groupingType == null) {
            if (other.groupingType != null)
                return false;
        } else if (!groupingType.equals(other.groupingType))
            return false;
        if (regionGroupId == null) {
            if (other.regionGroupId != null)
                return false;
        } else if (!regionGroupId.equals(other.regionGroupId))
            return false;
        if (transcript == null) {
            if (other.transcript != null)
                return false;
        } else if (!transcript.equals(other.transcript))
            return false;
        return true;
    }

}
