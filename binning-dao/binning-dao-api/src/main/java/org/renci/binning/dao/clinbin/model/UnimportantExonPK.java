package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class UnimportantExonPK implements Serializable {

    private static final long serialVersionUID = -7711785993510035926L;

    @Lob
    @Column(name = "transcr")
    private String transcript;

    @Column(name = "noncan_exon")
    private Integer nonCanonicalExon;

    public UnimportantExonPK() {
        super();
    }

    public UnimportantExonPK(String transcript, Integer nonCanonicalExon) {
        super();
        this.transcript = transcript;
        this.nonCanonicalExon = nonCanonicalExon;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public Integer getNonCanonicalExon() {
        return nonCanonicalExon;
    }

    public void setNonCanonicalExon(Integer nonCanonicalExon) {
        this.nonCanonicalExon = nonCanonicalExon;
    }

    @Override
    public String toString() {
        return String.format("UnimportantExonPK [transcript=%s, nonCanonicalExon=%s]", transcript, nonCanonicalExon);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nonCanonicalExon == null) ? 0 : nonCanonicalExon.hashCode());
        result = prime * result + ((transcript == null) ? 0 : transcript.hashCode());
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
        UnimportantExonPK other = (UnimportantExonPK) obj;
        if (nonCanonicalExon == null) {
            if (other.nonCanonicalExon != null)
                return false;
        } else if (!nonCanonicalExon.equals(other.nonCanonicalExon))
            return false;
        if (transcript == null) {
            if (other.transcript != null)
                return false;
        } else if (!transcript.equals(other.transcript))
            return false;
        return true;
    }

}
