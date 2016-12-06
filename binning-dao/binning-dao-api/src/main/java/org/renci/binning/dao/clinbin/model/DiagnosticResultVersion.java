package org.renci.binning.dao.clinbin.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.clinvar.model.Versions;
import org.renci.binning.dao.ref.model.GenomeRef;

@Entity
@Table(schema = "clinbin", name = "diagnostic_result_version", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ref_id", "refseq_version", "hgmd_version", "gen1000_snp_version", "gen1000_indel_version",
                "dbin_group_version", "algorithm_version", "dbsnp_version", "vcf_loader_name", "vcf_loader_version" }) })
public class DiagnosticResultVersion implements Persistable {

    private static final long serialVersionUID = 8183127521986516469L;

    @Id
    @Column(name = "diagnostic_result_version")
    private Integer diagnosticResultVersion;

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

    @Column(name = "dbin_group_version")
    private Integer dbinGroupVersion;

    @Column(name = "algorithm_version")
    private Integer algorithmVersion;

    @Column(name = "dbsnp_version")
    private Integer dbsnpVersion;

    @Lob
    @Column(name = "note")
    private String note;

    @Column(name = "vcf_loader_name", length = 1023)
    private String vcfLoaderName;

    @Column(name = "vcf_loader_version")
    private String vcfLoaderVersion;

    @ManyToOne
    @JoinColumn(name = "clinvar_version", columnDefinition = "int4")
    private Versions clinvarVersion;

    @OneToMany(mappedBy = "diagnosticResultVersion", fetch = FetchType.LAZY)
    private Set<BinResultsFinalDiagnostic> binResultsFinalDiagnostics;

    public DiagnosticResultVersion() {
        super();
    }

    public Set<BinResultsFinalDiagnostic> getBinResultsFinalDiagnostics() {
        return binResultsFinalDiagnostics;
    }

    public void setBinResultsFinalDiagnostics(Set<BinResultsFinalDiagnostic> binResultsFinalDiagnostics) {
        this.binResultsFinalDiagnostics = binResultsFinalDiagnostics;
    }

    public Integer getDiagnosticResultVersion() {
        return diagnosticResultVersion;
    }

    public void setDiagnosticResultVersion(Integer diagnosticResultVersion) {
        this.diagnosticResultVersion = diagnosticResultVersion;
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

    public Integer getDbinGroupVersion() {
        return dbinGroupVersion;
    }

    public void setDbinGroupVersion(Integer dbinGroupVersion) {
        this.dbinGroupVersion = dbinGroupVersion;
    }

    public Integer getAlgorithmVersion() {
        return algorithmVersion;
    }

    public void setAlgorithmVersion(Integer algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }

    public Integer getDbsnpVersion() {
        return dbsnpVersion;
    }

    public void setDbsnpVersion(Integer dbsnpVersion) {
        this.dbsnpVersion = dbsnpVersion;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVcfLoaderName() {
        return vcfLoaderName;
    }

    public void setVcfLoaderName(String vcfLoaderName) {
        this.vcfLoaderName = vcfLoaderName;
    }

    public String getVcfLoaderVersion() {
        return vcfLoaderVersion;
    }

    public void setVcfLoaderVersion(String vcfLoaderVersion) {
        this.vcfLoaderVersion = vcfLoaderVersion;
    }

    public Versions getClinvarVersion() {
        return clinvarVersion;
    }

    public void setClinvarVersion(Versions clinvarVersion) {
        this.clinvarVersion = clinvarVersion;
    }

    @Override
    public String toString() {
        return String.format(
                "DiagnosticResultVersion [diagnosticResultVersion=%s, refseqVersion=%s, hgmdVersion=%s, gen1000SnpVersion=%s, gen1000IndelVersion=%s, dbinGroupVersion=%s, algorithmVersion=%s, dbsnpVersion=%s, note=%s, vcfLoaderName=%s, vcfLoaderVersion=%s]",
                diagnosticResultVersion, refseqVersion, hgmdVersion, gen1000SnpVersion, gen1000IndelVersion, dbinGroupVersion,
                algorithmVersion, dbsnpVersion, note, vcfLoaderName, vcfLoaderVersion);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((algorithmVersion == null) ? 0 : algorithmVersion.hashCode());
        result = prime * result + ((dbinGroupVersion == null) ? 0 : dbinGroupVersion.hashCode());
        result = prime * result + ((dbsnpVersion == null) ? 0 : dbsnpVersion.hashCode());
        result = prime * result + ((diagnosticResultVersion == null) ? 0 : diagnosticResultVersion.hashCode());
        result = prime * result + ((gen1000IndelVersion == null) ? 0 : gen1000IndelVersion.hashCode());
        result = prime * result + ((gen1000SnpVersion == null) ? 0 : gen1000SnpVersion.hashCode());
        result = prime * result + ((hgmdVersion == null) ? 0 : hgmdVersion.hashCode());
        result = prime * result + ((note == null) ? 0 : note.hashCode());
        result = prime * result + ((refseqVersion == null) ? 0 : refseqVersion.hashCode());
        result = prime * result + ((vcfLoaderName == null) ? 0 : vcfLoaderName.hashCode());
        result = prime * result + ((vcfLoaderVersion == null) ? 0 : vcfLoaderVersion.hashCode());
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
        DiagnosticResultVersion other = (DiagnosticResultVersion) obj;
        if (algorithmVersion == null) {
            if (other.algorithmVersion != null)
                return false;
        } else if (!algorithmVersion.equals(other.algorithmVersion))
            return false;
        if (dbinGroupVersion == null) {
            if (other.dbinGroupVersion != null)
                return false;
        } else if (!dbinGroupVersion.equals(other.dbinGroupVersion))
            return false;
        if (dbsnpVersion == null) {
            if (other.dbsnpVersion != null)
                return false;
        } else if (!dbsnpVersion.equals(other.dbsnpVersion))
            return false;
        if (diagnosticResultVersion == null) {
            if (other.diagnosticResultVersion != null)
                return false;
        } else if (!diagnosticResultVersion.equals(other.diagnosticResultVersion))
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
        if (note == null) {
            if (other.note != null)
                return false;
        } else if (!note.equals(other.note))
            return false;
        if (refseqVersion == null) {
            if (other.refseqVersion != null)
                return false;
        } else if (!refseqVersion.equals(other.refseqVersion))
            return false;
        if (vcfLoaderName == null) {
            if (other.vcfLoaderName != null)
                return false;
        } else if (!vcfLoaderName.equals(other.vcfLoaderName))
            return false;
        if (vcfLoaderVersion == null) {
            if (other.vcfLoaderVersion != null)
                return false;
        } else if (!vcfLoaderVersion.equals(other.vcfLoaderVersion))
            return false;
        return true;
    }

}
