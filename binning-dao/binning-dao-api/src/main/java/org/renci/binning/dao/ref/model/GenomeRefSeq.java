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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "ref", name = "genome_ref_seq")
@Cacheable
public class GenomeRefSeq implements Persistable {

    private static final long serialVersionUID = 8237639060154518282L;

    @Id
    @Column(name = "ver_accession")
    private String verAccession;

    @Column(name = "contig")
    private String contig;

    @Column(name = "seq_type")
    private String seqType;

    @Column(name = "descr", length = 1023)
    @Transient
    private String descr;

    @ManyToMany(targetEntity = GenomeRef.class, fetch = FetchType.LAZY)
    @JoinTable(schema = "ref", name = "genome_ref_seqs", joinColumns = @JoinColumn(name = "seq_ver_accession"), inverseJoinColumns = @JoinColumn(name = "ref_id"))
    private Set<GenomeRef> genomeRefs;

    public GenomeRefSeq() {
        super();
    }

    public GenomeRefSeq(String verAccession, String contig, String seqType) {
        super();
        this.verAccession = verAccession;
        this.contig = contig;
        this.seqType = seqType;
    }

    public String getVerAccession() {
        return verAccession;
    }

    public void setVerAccession(String verAccession) {
        this.verAccession = verAccession;
    }

    public String getContig() {
        return contig;
    }

    public void setContig(String contig) {
        this.contig = contig;
    }

    public String getSeqType() {
        return seqType;
    }

    public void setSeqType(String seqType) {
        this.seqType = seqType;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Set<GenomeRef> getGenomeRefs() {
        return genomeRefs;
    }

    public void setGenomeRefs(Set<GenomeRef> genomeRefs) {
        this.genomeRefs = genomeRefs;
    }

    @Override
    public String toString() {
        return String.format("GenomeRefSeq [verAccession=%s, contig=%s, seqType=%s]", verAccession, contig, seqType);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contig == null) ? 0 : contig.hashCode());
        result = prime * result + ((seqType == null) ? 0 : seqType.hashCode());
        result = prime * result + ((verAccession == null) ? 0 : verAccession.hashCode());
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
        GenomeRefSeq other = (GenomeRefSeq) obj;
        if (contig == null) {
            if (other.contig != null)
                return false;
        } else if (!contig.equals(other.contig))
            return false;
        if (seqType == null) {
            if (other.seqType != null)
                return false;
        } else if (!seqType.equals(other.seqType))
            return false;
        if (verAccession == null) {
            if (other.verAccession != null)
                return false;
        } else if (!verAccession.equals(other.verAccession))
            return false;
        return true;
    }

}
