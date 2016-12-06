package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.ref.model.GenomeRef;

@Entity
@Table(schema = "clinbin", name = "incidental_result_version", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ref_id", "refseq_version", "hgmd_version", "gen1000_snp_version", "gen1000_indel_version",
                "ibin_group_version", "binning_algorithm_version" }) })
public class IncidentalResultVersion implements Persistable {

    private static final long serialVersionUID = 8796340369982243885L;

    @Id
    @Column(name = "binning_result_version")
    private Integer binningResultVersion;

    @ManyToOne
    @JoinColumn(name = "ref_id")
    private GenomeRef genomeRef;

    @Column(name = "refseq_version")
    private Integer refseqVersion;

    @Column(name = "hgmd_version")
    private Integer hgmdVersion;

    @Column(name = "gen1000_snp_version")
    private Integer gen1000SnpVersion;

    @Column(name = "gen1000_indel_version")
    private Integer gen1000IndelVersion;

    @Column(name = "ibin_group_version")
    private Integer ibinGroupVersion;

    @Column(name = "binning_algorithm_version")
    private Integer binningAlgorithmVersion;

    public IncidentalResultVersion() {
        super();
    }

    public Integer getBinningResultVersion() {
        return binningResultVersion;
    }

    public void setBinningResultVersion(Integer binningResultVersion) {
        this.binningResultVersion = binningResultVersion;
    }

    public GenomeRef getGenomeRef() {
        return genomeRef;
    }

    public void setGenomeRef(GenomeRef genomeRef) {
        this.genomeRef = genomeRef;
    }

    public Integer getRefseqVersion() {
        return refseqVersion;
    }

    public void setRefseqVersion(Integer refseqVersion) {
        this.refseqVersion = refseqVersion;
    }

    public Integer getHgmdVersion() {
        return hgmdVersion;
    }

    public void setHgmdVersion(Integer hgmdVersion) {
        this.hgmdVersion = hgmdVersion;
    }

    public Integer getGen1000SnpVersion() {
        return gen1000SnpVersion;
    }

    public void setGen1000SnpVersion(Integer gen1000SnpVersion) {
        this.gen1000SnpVersion = gen1000SnpVersion;
    }

    public Integer getGen1000IndelVersion() {
        return gen1000IndelVersion;
    }

    public void setGen1000IndelVersion(Integer gen1000IndelVersion) {
        this.gen1000IndelVersion = gen1000IndelVersion;
    }

    public Integer getIbinGroupVersion() {
        return ibinGroupVersion;
    }

    public void setIbinGroupVersion(Integer ibinGroupVersion) {
        this.ibinGroupVersion = ibinGroupVersion;
    }

    public Integer getBinningAlgorithmVersion() {
        return binningAlgorithmVersion;
    }

    public void setBinningAlgorithmVersion(Integer binningAlgorithmVersion) {
        this.binningAlgorithmVersion = binningAlgorithmVersion;
    }

    @Override
    public String toString() {
        return String.format(
                "IncidentalResultVersion [binningResultVersion=%s, refseqVersion=%s, hgmdVersion=%s, gen1000SnpVersion=%s, gen1000IndelVersion=%s, ibinGroupVersion=%s, binningAlgorithmVersion=%s]",
                binningResultVersion, refseqVersion, hgmdVersion, gen1000SnpVersion, gen1000IndelVersion, ibinGroupVersion,
                binningAlgorithmVersion);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((binningAlgorithmVersion == null) ? 0 : binningAlgorithmVersion.hashCode());
        result = prime * result + ((binningResultVersion == null) ? 0 : binningResultVersion.hashCode());
        result = prime * result + ((gen1000IndelVersion == null) ? 0 : gen1000IndelVersion.hashCode());
        result = prime * result + ((gen1000SnpVersion == null) ? 0 : gen1000SnpVersion.hashCode());
        result = prime * result + ((hgmdVersion == null) ? 0 : hgmdVersion.hashCode());
        result = prime * result + ((ibinGroupVersion == null) ? 0 : ibinGroupVersion.hashCode());
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
        IncidentalResultVersion other = (IncidentalResultVersion) obj;
        if (binningAlgorithmVersion == null) {
            if (other.binningAlgorithmVersion != null)
                return false;
        } else if (!binningAlgorithmVersion.equals(other.binningAlgorithmVersion))
            return false;
        if (binningResultVersion == null) {
            if (other.binningResultVersion != null)
                return false;
        } else if (!binningResultVersion.equals(other.binningResultVersion))
            return false;
        if (gen1000IndelVersion == null) {
            if (other.gen1000IndelVersion != null)
                return false;
        } else if (!gen1000IndelVersion.equals(other.gen1000IndelVersion))
            return false;
        if (gen1000SnpVersion == null) {
            if (other.gen1000SnpVersion != null)
                return false;
        } else if (!gen1000SnpVersion.equals(other.gen1000SnpVersion))
            return false;
        if (hgmdVersion == null) {
            if (other.hgmdVersion != null)
                return false;
        } else if (!hgmdVersion.equals(other.hgmdVersion))
            return false;
        if (ibinGroupVersion == null) {
            if (other.ibinGroupVersion != null)
                return false;
        } else if (!ibinGroupVersion.equals(other.ibinGroupVersion))
            return false;
        if (refseqVersion == null) {
            if (other.refseqVersion != null)
                return false;
        } else if (!refseqVersion.equals(other.refseqVersion))
            return false;
        return true;
    }

}
