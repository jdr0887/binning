package org.renci.binning.dao.ref.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GenomeRefSeqLocationPK implements Serializable {

    private static final long serialVersionUID = 4103404522615560260L;

    @Column(name = "ver_accession")
    private String verAccession;

    @Column(name = "ref_id")
    private Integer refId;

    public GenomeRefSeqLocationPK() {
        super();
    }

    public String getVerAccession() {
        return verAccession;
    }

    public void setVerAccession(String verAccession) {
        this.verAccession = verAccession;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    @Override
    public String toString() {
        return String.format("GenomeRefSeqLocationPK [verAccession=%s, refId=%s]", verAccession, refId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((refId == null) ? 0 : refId.hashCode());
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
        GenomeRefSeqLocationPK other = (GenomeRefSeqLocationPK) obj;
        if (refId == null) {
            if (other.refId != null)
                return false;
        } else if (!refId.equals(other.refId))
            return false;
        if (verAccession == null) {
            if (other.verAccession != null)
                return false;
        } else if (!verAccession.equals(other.verAccession))
            return false;
        return true;
    }

}
