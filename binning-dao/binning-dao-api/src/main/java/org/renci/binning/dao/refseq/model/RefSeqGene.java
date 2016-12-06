package org.renci.binning.dao.refseq.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "refseq", name = "gene")
public class RefSeqGene implements Persistable {

    private static final long serialVersionUID = 5463934316096229177L;

    @Id
    @Column(name = "refseq_gene_id")
    private Integer id;

    @Column(name = "refseq_ver")
    private String refseqVersion;

    @Column(name = "short_name")
    private String name;

    @Column(name = "descr", length = 4095)
    private String description;

    @ManyToMany(targetEntity = RegionGroup.class, fetch = FetchType.LAZY)
    @JoinTable(schema = "refseq", name = "gene_locs", joinColumns = {
            @JoinColumn(name = "refseq_gene_id", referencedColumnName = "refseq_gene_id") }, inverseJoinColumns = {
                    @JoinColumn(name = "loc_region_group_id", referencedColumnName = "region_group_id") })
    private Set<RegionGroup> locations;

    public RefSeqGene() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefseqVersion() {
        return refseqVersion;
    }

    public void setRefseqVersion(String refseqVersion) {
        this.refseqVersion = refseqVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RegionGroup> getLocations() {
        return locations;
    }

    public void setLocations(Set<RegionGroup> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return String.format("RefSeqGene [id=%s, refseqVersion=%s, name=%s, description=%s]", id, refseqVersion, name, description);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((refseqVersion == null) ? 0 : refseqVersion.hashCode());
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
        RefSeqGene other = (RefSeqGene) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (refseqVersion == null) {
            if (other.refseqVersion != null)
                return false;
        } else if (!refseqVersion.equals(other.refseqVersion))
            return false;
        return true;
    }

}
