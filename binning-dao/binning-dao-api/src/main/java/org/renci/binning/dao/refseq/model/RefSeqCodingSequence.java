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
import javax.persistence.Transient;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "refseq", name = "cds")
@FetchGroups({ @FetchGroup(name = "includeAll", attributes = { @FetchAttribute(name = "locations") }) })
public class RefSeqCodingSequence implements Persistable {

    private static final long serialVersionUID = -3518848421271412683L;

    @Id
    @Column(name = "refseq_cds_id")
    private Integer id;

    @Column(name = "refseq_ver")
    private String version;

    @Column(name = "protein_id", length = 31)
    private String proteinId;

    @Column(name = "product")
    private String product;

    @Column(name = "descr", length = 65535)
    @Transient
    private String descr;

    @Column(name = "transl", length = 65535)
    @Transient
    private String transl;

    @Column(name = "note", length = 1023)
    private String note;

    @ManyToMany(targetEntity = RegionGroup.class, fetch = FetchType.LAZY)
    @JoinTable(schema = "refseq", name = "cds_locs", joinColumns = @JoinColumn(name = "refseq_cds_id"), inverseJoinColumns = @JoinColumn(name = "loc_region_group_id"))
    protected Set<RegionGroup> locations;

    public RefSeqCodingSequence() {
        super();
    }

    public Set<RegionGroup> getLocations() {
        return locations;
    }

    public void setLocations(Set<RegionGroup> locations) {
        this.locations = locations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProteinId() {
        return proteinId;
    }

    public void setProteinId(String proteinId) {
        this.proteinId = proteinId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getTransl() {
        return transl;
    }

    public void setTransl(String transl) {
        this.transl = transl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        // return String.format("CDS [id=%s, version=%s, proteinId=%s, product=%s, descr=%s, transl=%s, note=%s]", id,
        // version, proteinId, product, descr, transl, note);
        return String.format("CDS [id=%s, version=%s, proteinId=%s, product=%s, note=%s]", id, version, proteinId, product, note);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + ((descr == null) ? 0 : descr.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((note == null) ? 0 : note.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((proteinId == null) ? 0 : proteinId.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        // result = prime * result + ((transl == null) ? 0 : transl.hashCode());
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
        RefSeqCodingSequence other = (RefSeqCodingSequence) obj;
        // if (descr == null) {
        // if (other.descr != null)
        // return false;
        // } else if (!descr.equals(other.descr))
        // return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (note == null) {
            if (other.note != null)
                return false;
        } else if (!note.equals(other.note))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (proteinId == null) {
            if (other.proteinId != null)
                return false;
        } else if (!proteinId.equals(other.proteinId))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        // if (transl == null) {
        // if (other.transl != null)
        // return false;
        // } else if (!transl.equals(other.transl))
        // return false;
        return true;
    }

}
