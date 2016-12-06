package org.renci.binning.dao.refseq.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "refseq", name = "transcr")
@FetchGroups({ @FetchGroup(name = "includeTranscriptRefSeqVers", attributes = { @FetchAttribute(name = "refseqVersions") }),
        @FetchGroup(name = "includeAll", attributes = { @FetchAttribute(name = "refseqVersions"), @FetchAttribute(name = "transcriptMaps"),
                @FetchAttribute(name = "regionGroups") }) })
public class Transcript implements Persistable {

    private static final long serialVersionUID = -2441504727987883964L;

    @Id
    @Column(name = "ver_id", length = 31)
    protected String versionId;

    @Column(name = "accession", length = 31)
    protected String accession;

    @Column(name = "seq", length = 524287)
    protected String seq;

    @OneToMany(mappedBy = "transcript", fetch = FetchType.LAZY)
    protected Set<TranscriptRefSeqVers> refseqVersions;

    @OneToMany(mappedBy = "transcript", fetch = FetchType.LAZY)
    protected Set<TranscriptMaps> transcriptMaps;

    @OneToMany(mappedBy = "transcript", fetch = FetchType.LAZY)
    protected Set<RegionGroup> regionGroups;

    public Transcript() {
        super();
        this.transcriptMaps = new HashSet<TranscriptMaps>();
        this.refseqVersions = new HashSet<TranscriptRefSeqVers>();
        this.regionGroups = new HashSet<RegionGroup>();
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public Set<TranscriptRefSeqVers> getRefseqVersions() {
        return refseqVersions;
    }

    public void setRefseqVersions(Set<TranscriptRefSeqVers> refseqVersions) {
        this.refseqVersions = refseqVersions;
    }

    public Set<TranscriptMaps> getTranscriptMaps() {
        return transcriptMaps;
    }

    public void setTranscriptMaps(Set<TranscriptMaps> transcriptMaps) {
        this.transcriptMaps = transcriptMaps;
    }

    public Set<RegionGroup> getRegionGroups() {
        return regionGroups;
    }

    public void setRegionGroups(Set<RegionGroup> regionGroups) {
        this.regionGroups = regionGroups;
    }

    @Override
    public String toString() {
        return String.format("Transcript [versionId=%s, accession=%s]", versionId, accession);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accession == null) ? 0 : accession.hashCode());
        result = prime * result + ((versionId == null) ? 0 : versionId.hashCode());
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
        Transcript other = (Transcript) obj;
        if (accession == null) {
            if (other.accession != null)
                return false;
        } else if (!accession.equals(other.accession))
            return false;
        if (versionId == null) {
            if (other.versionId != null)
                return false;
        } else if (!versionId.equals(other.versionId))
            return false;
        return true;
    }

}
