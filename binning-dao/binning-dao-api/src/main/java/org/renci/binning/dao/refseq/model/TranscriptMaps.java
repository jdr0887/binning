package org.renci.binning.dao.refseq.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.ref.model.GenomeRefSeq;

@Entity
@Table(schema = "refseq", name = "transcr_maps")
@FetchGroups({
        @FetchGroup(name = "includeManyToOnes", attributes = { @FetchAttribute(name = "transcript"),
                @FetchAttribute(name = "genomeRefSeq") }),
        @FetchGroup(name = "includeAll", fetchGroups = { "includeManyToOnes" }, attributes = { @FetchAttribute(name = "exons") }) })
public class TranscriptMaps implements Persistable {

    private static final long serialVersionUID = 8175717803443861686L;

    @Id
    @Column(name = "refseq_transcr_maps_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "refseq_transcr_ver_id")
    private Transcript transcript;

    @Column(name = "genome_ref_id")
    private Integer genomeRefId;

    @Column(name = "map_count")
    private Integer mapCount;

    @Column(name = "strand", length = 1)
    private String strand;

    @Column(name = "score")
    private Double score;

    @Column(name = "ident")
    private Double ident;

    @ManyToOne
    @JoinColumn(name = "seq_ver_accession")
    private GenomeRefSeq genomeRefSeq;

    @Column(name = "exon_count")
    private Integer exonCount;

    @OneToMany(mappedBy = "transcriptMaps", fetch = FetchType.LAZY)
    private List<TranscriptMapsExons> exons;

    public TranscriptMaps() {
        super();
        this.exons = new ArrayList<TranscriptMapsExons>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public Integer getGenomeRefId() {
        return genomeRefId;
    }

    public void setGenomeRefId(Integer genomeRefId) {
        this.genomeRefId = genomeRefId;
    }

    public Integer getMapCount() {
        return mapCount;
    }

    public void setMapCount(Integer mapCount) {
        this.mapCount = mapCount;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getIdent() {
        return ident;
    }

    public void setIdent(Double ident) {
        this.ident = ident;
    }

    public GenomeRefSeq getGenomeRefSeq() {
        return genomeRefSeq;
    }

    public void setGenomeRefSeq(GenomeRefSeq genomeRefSeq) {
        this.genomeRefSeq = genomeRefSeq;
    }

    public Integer getExonCount() {
        return exonCount;
    }

    public void setExonCount(Integer exonCount) {
        this.exonCount = exonCount;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public List<TranscriptMapsExons> getExons() {
        return exons;
    }

    public void setTranscriptMapsExons(List<TranscriptMapsExons> exons) {
        this.exons = exons;
    }

    @Override
    public String toString() {
        return String.format("TranscriptMaps [id=%s, genomeRefId=%s, mapCount=%s, strand=%s, score=%s, ident=%s, exonCount=%s]", id,
                genomeRefId, mapCount, strand, score, ident, exonCount);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((exonCount == null) ? 0 : exonCount.hashCode());
        result = prime * result + ((genomeRefId == null) ? 0 : genomeRefId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((ident == null) ? 0 : ident.hashCode());
        result = prime * result + ((mapCount == null) ? 0 : mapCount.hashCode());
        result = prime * result + ((score == null) ? 0 : score.hashCode());
        result = prime * result + ((strand == null) ? 0 : strand.hashCode());
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
        TranscriptMaps other = (TranscriptMaps) obj;
        if (exonCount == null) {
            if (other.exonCount != null)
                return false;
        } else if (!exonCount.equals(other.exonCount))
            return false;
        if (genomeRefId == null) {
            if (other.genomeRefId != null)
                return false;
        } else if (!genomeRefId.equals(other.genomeRefId))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (ident == null) {
            if (other.ident != null)
                return false;
        } else if (!ident.equals(other.ident))
            return false;
        if (mapCount == null) {
            if (other.mapCount != null)
                return false;
        } else if (!mapCount.equals(other.mapCount))
            return false;
        if (score == null) {
            if (other.score != null)
                return false;
        } else if (!score.equals(other.score))
            return false;
        if (strand == null) {
            if (other.strand != null)
                return false;
        } else if (!strand.equals(other.strand))
            return false;
        return true;
    }

}
