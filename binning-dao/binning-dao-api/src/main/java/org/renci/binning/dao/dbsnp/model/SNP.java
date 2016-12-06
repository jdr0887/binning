package org.renci.binning.dao.dbsnp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "dbsnp", name = "snp", uniqueConstraints = { @UniqueConstraint(columnNames = { "rs_id", "dbsnp_ver" }) })
public class SNP implements Persistable {

    private static final long serialVersionUID = 2773808300759948395L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "snp_snp_id_seq")
    @SequenceGenerator(name = "snp_snp_id_seq", schema = "dbsnp", sequenceName = "snp_snp_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "snp_id")
    private Integer id;

    @Column(name = "rs_id")
    private Integer rsId;

    @Column(name = "dbsnp_ver", length = 15)
    private String version;

    @Column(name = "avg_heterozygosity")
    private Double averageHeterozygosity;

    @Column(name = "het_se")
    private Double hetSE;

    @Column(name = "last_updated_time")
    private Date lastModified;

    @Column(name = "ancestral_allele_name", length = 15)
    private String ancestralAlleleName;

    public SNP() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRsId() {
        return rsId;
    }

    public void setRsId(Integer rsId) {
        this.rsId = rsId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Double getAverageHeterozygosity() {
        return averageHeterozygosity;
    }

    public void setAverageHeterozygosity(Double averageHeterozygosity) {
        this.averageHeterozygosity = averageHeterozygosity;
    }

    public Double getHetSE() {
        return hetSE;
    }

    public void setHetSE(Double hetSE) {
        this.hetSE = hetSE;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getAncestralAlleleName() {
        return ancestralAlleleName;
    }

    public void setAncestralAlleleName(String ancestralAlleleName) {
        this.ancestralAlleleName = ancestralAlleleName;
    }

    @Override
    public String toString() {
        return String.format(
                "SNP [id=%s, rsId=%s, version=%s, averageHeterozygosity=%s, hetSE=%s, lastModified=%s, ancestralAlleleName=%s]", id, rsId,
                version, averageHeterozygosity, hetSE, lastModified, ancestralAlleleName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ancestralAlleleName == null) ? 0 : ancestralAlleleName.hashCode());
        result = prime * result + ((averageHeterozygosity == null) ? 0 : averageHeterozygosity.hashCode());
        result = prime * result + ((hetSE == null) ? 0 : hetSE.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
        result = prime * result + ((rsId == null) ? 0 : rsId.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        SNP other = (SNP) obj;
        if (ancestralAlleleName == null) {
            if (other.ancestralAlleleName != null)
                return false;
        } else if (!ancestralAlleleName.equals(other.ancestralAlleleName))
            return false;
        if (averageHeterozygosity == null) {
            if (other.averageHeterozygosity != null)
                return false;
        } else if (!averageHeterozygosity.equals(other.averageHeterozygosity))
            return false;
        if (hetSE == null) {
            if (other.hetSE != null)
                return false;
        } else if (!hetSE.equals(other.hetSE))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lastModified == null) {
            if (other.lastModified != null)
                return false;
        } else if (!lastModified.equals(other.lastModified))
            return false;
        if (rsId == null) {
            if (other.rsId != null)
                return false;
        } else if (!rsId.equals(other.rsId))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
