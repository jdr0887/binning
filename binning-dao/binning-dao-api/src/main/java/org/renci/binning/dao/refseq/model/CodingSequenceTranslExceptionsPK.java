package org.renci.binning.dao.refseq.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CodingSequenceTranslExceptionsPK implements Serializable {

    private static final long serialVersionUID = 1188343289598927266L;

    @Column(name = "refseq_cds_id")
    private Integer refseqCdsId;

    @Column(name = "start_loc")
    private Integer startLoc;

    public CodingSequenceTranslExceptionsPK() {
        super();
    }

    public Integer getRefseqCdsId() {
        return refseqCdsId;
    }

    public void setRefseqCdsId(Integer refseqCdsId) {
        this.refseqCdsId = refseqCdsId;
    }

    public Integer getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(Integer startLoc) {
        this.startLoc = startLoc;
    }

    @Override
    public String toString() {
        return String.format("CodingSequenceTranslExceptionsPK [refseqCdsId=%s, startLoc=%s]", refseqCdsId, startLoc);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((refseqCdsId == null) ? 0 : refseqCdsId.hashCode());
        result = prime * result + ((startLoc == null) ? 0 : startLoc.hashCode());
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
        CodingSequenceTranslExceptionsPK other = (CodingSequenceTranslExceptionsPK) obj;
        if (refseqCdsId == null) {
            if (other.refseqCdsId != null)
                return false;
        } else if (!refseqCdsId.equals(other.refseqCdsId))
            return false;
        if (startLoc == null) {
            if (other.startLoc != null)
                return false;
        } else if (!startLoc.equals(other.startLoc))
            return false;
        return true;
    }

}
