package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DXCoveragePK implements Serializable {

    private static final long serialVersionUID = 745714824781965526L;

    @Column(name = "dx_exon_id")
    private Integer exonId;

    @Column(name = "participant", length = 50)
    private String participant;

    public DXCoveragePK() {
        super();
    }

    public DXCoveragePK(Integer exonId, String participant) {
        super();
        this.exonId = exonId;
        this.participant = participant;
    }

    public Integer getExonId() {
        return exonId;
    }

    public void setExonId(Integer exonId) {
        this.exonId = exonId;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    @Override
    public String toString() {
        return String.format("DXCoveragePK [exonId=%s, participant=%s]", exonId, participant);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((exonId == null) ? 0 : exonId.hashCode());
        result = prime * result + ((participant == null) ? 0 : participant.hashCode());
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
        DXCoveragePK other = (DXCoveragePK) obj;
        if (exonId == null) {
            if (other.exonId != null)
                return false;
        } else if (!exonId.equals(other.exonId))
            return false;
        if (participant == null) {
            if (other.participant != null)
                return false;
        } else if (!participant.equals(other.participant))
            return false;
        return true;
    }

}
