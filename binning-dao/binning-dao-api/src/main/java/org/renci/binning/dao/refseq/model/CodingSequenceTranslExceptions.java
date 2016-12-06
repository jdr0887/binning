package org.renci.binning.dao.refseq.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "refseq", name = "cds_transl_exceptions")
public class CodingSequenceTranslExceptions {

    @EmbeddedId
    private CodingSequenceTranslExceptionsPK key;

    @Column(name = "stop_loc")
    private Integer stopLoc;

    @Column(name = "amino_acid", length = 31)
    private String aminoAcid;

    public CodingSequenceTranslExceptions() {
        super();
    }

    public CodingSequenceTranslExceptionsPK getKey() {
        return key;
    }

    public void setKey(CodingSequenceTranslExceptionsPK key) {
        this.key = key;
    }

    public String getAminoAcid() {
        return aminoAcid;
    }

    public void setAminoAcid(String aminoAcid) {
        this.aminoAcid = aminoAcid;
    }

    public Integer getStopLoc() {
        return stopLoc;
    }

    public void setStopLoc(Integer stopLoc) {
        this.stopLoc = stopLoc;
    }

    @Override
    public String toString() {
        return String.format("CodingSequenceTranslExceptions [key=%s, stopLoc=%s, aminoAcid=%s]", key, stopLoc, aminoAcid);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aminoAcid == null) ? 0 : aminoAcid.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((stopLoc == null) ? 0 : stopLoc.hashCode());
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
        CodingSequenceTranslExceptions other = (CodingSequenceTranslExceptions) obj;
        if (aminoAcid == null) {
            if (other.aminoAcid != null)
                return false;
        } else if (!aminoAcid.equals(other.aminoAcid))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (stopLoc == null) {
            if (other.stopLoc != null)
                return false;
        } else if (!stopLoc.equals(other.stopLoc))
            return false;
        return true;
    }

}
