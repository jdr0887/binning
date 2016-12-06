package org.renci.binning.dao.ref.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "ref", name = "genome_ref_seq_alt_ids")
public class GenomeRefSeqAltIds implements Persistable {

    private static final long serialVersionUID = -2418046550597045333L;

    @ManyToOne
    @JoinColumn(name = "ver_accession", nullable = false)
    private GenomeRefSeq genomeRefSeq;

    @Id
    @Column(name = "id_type")
    private String idType;

    @Column(name = "id")
    private String id;

    public GenomeRefSeqAltIds() {
        super();
    }

    public GenomeRefSeq getGenomeRefSeq() {
        return genomeRefSeq;
    }

    public void setGenomeRefSeq(GenomeRefSeq genomeRefSeq) {
        this.genomeRefSeq = genomeRefSeq;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GenomeRefSeqAltIds [genomeRefSeq=" + genomeRefSeq + ", idType=" + idType + ", id=" + id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genomeRefSeq == null) ? 0 : genomeRefSeq.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((idType == null) ? 0 : idType.hashCode());
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
        GenomeRefSeqAltIds other = (GenomeRefSeqAltIds) obj;
        if (genomeRefSeq == null) {
            if (other.genomeRefSeq != null)
                return false;
        } else if (!genomeRefSeq.equals(other.genomeRefSeq))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (idType == null) {
            if (other.idType != null)
                return false;
        } else if (!idType.equals(other.idType))
            return false;
        return true;
    }

}
