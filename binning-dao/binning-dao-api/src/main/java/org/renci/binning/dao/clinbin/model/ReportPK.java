package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReportPK implements Serializable {

    private static final long serialVersionUID = 2519843495617416372L;

    @Column(name = "participant")
    private String participant;

    @Column(name = "num_dx")
    private Integer numDX;

    public ReportPK() {
        super();
    }

    public ReportPK(String participant, Integer numDX) {
        super();
        this.participant = participant;
        this.numDX = numDX;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Integer getNumDX() {
        return numDX;
    }

    public void setNumDX(Integer numDX) {
        this.numDX = numDX;
    }

    @Override
    public String toString() {
        return String.format("ReportPK [participant=%s, numDX=%s]", participant, numDX);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((numDX == null) ? 0 : numDX.hashCode());
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
        ReportPK other = (ReportPK) obj;
        if (numDX == null) {
            if (other.numDX != null)
                return false;
        } else if (!numDX.equals(other.numDX))
            return false;
        if (participant == null) {
            if (other.participant != null)
                return false;
        } else if (!participant.equals(other.participant))
            return false;
        return true;
    }

}
