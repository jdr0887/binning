package org.renci.binning.dao.refseq.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TranscriptMapsWarningsPK implements Serializable {

    private static final long serialVersionUID = -3283891847947358775L;

    @Column(name = "refseq_transcr_maps_id")
    private Integer refseqTranscrMapsId;

    @Column(name = "warning_warning_name")
    private String warningWarningName;

    public TranscriptMapsWarningsPK() {
        super();
    }

    public Integer getRefseqTranscrMapsId() {
        return refseqTranscrMapsId;
    }

    public void setRefseqTranscrMapsId(Integer refseqTranscrMapsId) {
        this.refseqTranscrMapsId = refseqTranscrMapsId;
    }

    public String getWarningWarningName() {
        return warningWarningName;
    }

    public void setWarningWarningName(String warningWarningName) {
        this.warningWarningName = warningWarningName;
    }

    @Override
    public String toString() {
        return String.format("TranscriptMapsWarningsPK [refseqTranscrMapsId=%s, warningWarningName=%s]", refseqTranscrMapsId,
                warningWarningName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((refseqTranscrMapsId == null) ? 0 : refseqTranscrMapsId.hashCode());
        result = prime * result + ((warningWarningName == null) ? 0 : warningWarningName.hashCode());
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
        TranscriptMapsWarningsPK other = (TranscriptMapsWarningsPK) obj;
        if (refseqTranscrMapsId == null) {
            if (other.refseqTranscrMapsId != null)
                return false;
        } else if (!refseqTranscrMapsId.equals(other.refseqTranscrMapsId))
            return false;
        if (warningWarningName == null) {
            if (other.warningWarningName != null)
                return false;
        } else if (!warningWarningName.equals(other.warningWarningName))
            return false;
        return true;
    }

}
