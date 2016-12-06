package org.renci.binning.dao.refseq.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "refseq", name = "transcr_refseq_vers")
public class TranscriptRefSeqVers implements Persistable {

    private static final long serialVersionUID = 7323721337345220492L;

    @ManyToOne
    @JoinColumn(name = "ver_id")
    private Transcript transcript;

    @Id
    @Column(name = "refseq_ver")
    private String refseqVer;

    public TranscriptRefSeqVers() {
        super();
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public String getRefseqVer() {
        return refseqVer;
    }

    public void setRefseqVer(String refseqVer) {
        this.refseqVer = refseqVer;
    }

    @Override
    public String toString() {
        return String.format("TranscriptRefSeqVers [refseqVer=%s]", refseqVer);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((refseqVer == null) ? 0 : refseqVer.hashCode());
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
        TranscriptRefSeqVers other = (TranscriptRefSeqVers) obj;
        if (refseqVer == null) {
            if (other.refseqVer != null)
                return false;
        } else if (!refseqVer.equals(other.refseqVer))
            return false;
        if (transcript == null) {
            if (other.transcript != null)
                return false;
        } else if (!transcript.equals(other.transcript))
            return false;
        return true;
    }

}
