package org.renci.binning.dao.ref.model;

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "ref", name = "genome_ref")
@NamedQueries({ @NamedQuery(name = "GenomeRef.findAll", query = "SELECT DISTINCT a FROM GenomeRef a"),
        @NamedQuery(name = "GenomeRef.findByName", query = "SELECT DISTINCT a FROM GenomeRef a where a.name = :name"),
        @NamedQuery(name = "GenomeRef.findByNameAndSource", query = "SELECT DISTINCT a FROM GenomeRef a where a.name = :name and a.source = :source") })
@Cacheable
public class GenomeRef implements Persistable {

    private static final long serialVersionUID = -6241264265732175194L;

    @Id
    @Column(name = "ref_id")
    private Integer id;

    @Column(name = "ref_source")
    private String source;

    @Column(name = "ref_ver")
    private String refVer;

    @Column(name = "ref_shortname", length = 50)
    private String name;

    @Column(name = "basic_fasta_url", length = 1023)
    private String basicFastaURL;

    @Column(name = "extras_fasta_url", length = 1023)
    private String extrasFastaURL;

    @ManyToMany(targetEntity = GenomeRefSeq.class, fetch = FetchType.LAZY)
    @JoinTable(schema = "ref", name = "genome_ref_seqs", joinColumns = @JoinColumn(name = "ref_id"), inverseJoinColumns = @JoinColumn(name = "seq_ver_accession"))
    private Set<GenomeRefSeq> genomeRefSeqs;

    public GenomeRef() {
        super();
    }

    public GenomeRef(String source, String refVer, String name, String basicFastaURL, String extrasFastaURL) {
        super();
        this.source = source;
        this.refVer = refVer;
        this.name = name;
        this.basicFastaURL = basicFastaURL;
        this.extrasFastaURL = extrasFastaURL;
    }

    public Set<GenomeRefSeq> getGenomeRefSeqs() {
        return genomeRefSeqs;
    }

    public void setGenomeRefSeqs(Set<GenomeRefSeq> genomeRefSeqs) {
        this.genomeRefSeqs = genomeRefSeqs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRefVer() {
        return refVer;
    }

    public void setRefVer(String refVer) {
        this.refVer = refVer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasicFastaURL() {
        return basicFastaURL;
    }

    public void setBasicFastaURL(String basicFastaURL) {
        this.basicFastaURL = basicFastaURL;
    }

    public String getExtrasFastaURL() {
        return extrasFastaURL;
    }

    public void setExtrasFastaURL(String extrasFastaURL) {
        this.extrasFastaURL = extrasFastaURL;
    }

    @Override
    public String toString() {
        return String.format("GenomeRef [id=%s, source=%s, refVer=%s, name=%s, basicFastaURL=%s, extrasFastaURL=%s]", id, source, refVer,
                name, basicFastaURL, extrasFastaURL);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((basicFastaURL == null) ? 0 : basicFastaURL.hashCode());
        result = prime * result + ((extrasFastaURL == null) ? 0 : extrasFastaURL.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + ((refVer == null) ? 0 : refVer.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        GenomeRef other = (GenomeRef) obj;
        if (basicFastaURL == null) {
            if (other.basicFastaURL != null)
                return false;
        } else if (!basicFastaURL.equals(other.basicFastaURL))
            return false;
        if (extrasFastaURL == null) {
            if (other.extrasFastaURL != null)
                return false;
        } else if (!extrasFastaURL.equals(other.extrasFastaURL))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        if (refVer == null) {
            if (other.refVer != null)
                return false;
        } else if (!refVer.equals(other.refVer))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
