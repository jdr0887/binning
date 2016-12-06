package org.renci.binning.dao.refseq.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TranscriptMapsExonsPK implements Serializable {

    private static final long serialVersionUID = 3101914375360177904L;

    @Column(name = "refseq_transcr_maps_id")
    private Integer transcriptMapsId;

    @Column(name = "exon_num")
    private Integer exonNum;

    public TranscriptMapsExonsPK() {
        super();
    }

    public Integer getTranscriptMapsId() {
        return transcriptMapsId;
    }

    public void setTranscriptMapsId(Integer transcriptMapsId) {
        this.transcriptMapsId = transcriptMapsId;
    }

    public Integer getExonNum() {
        return exonNum;
    }

    public void setExonNum(Integer exonNum) {
        this.exonNum = exonNum;
    }

    @Override
    public String toString() {
        return String.format("TranscriptMapsExonsPK [transcriptMapsId=%s, exonNum=%s]", transcriptMapsId, exonNum);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((exonNum == null) ? 0 : exonNum.hashCode());
        result = prime * result + ((transcriptMapsId == null) ? 0 : transcriptMapsId.hashCode());
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
        TranscriptMapsExonsPK other = (TranscriptMapsExonsPK) obj;
        if (exonNum == null) {
            if (other.exonNum != null)
                return false;
        } else if (!exonNum.equals(other.exonNum))
            return false;
        if (transcriptMapsId == null) {
            if (other.transcriptMapsId != null)
                return false;
        } else if (!transcriptMapsId.equals(other.transcriptMapsId))
            return false;
        return true;
    }

}
