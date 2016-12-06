package org.renci.binning.dao.refseq.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.annotation.model.AnnotationGene;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.VariantType;

@Entity
@Table(schema = "refseq", name = "variants_61_2")
@FetchGroups({ @FetchGroup(name = "includeManyToOnes", attributes = { @FetchAttribute(name = "locatedVariant"),
        @FetchAttribute(name = "genomeRefSeq"), @FetchAttribute(name = "variantType"), @FetchAttribute(name = "locationType"),
        @FetchAttribute(name = "variantEffect"), @FetchAttribute(name = "gene") }) })
public class Variants_61_2 implements Persistable {

    private static final long serialVersionUID = 7532101830529403701L;

    @EmbeddedId
    private Variants_61_2PK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @MapsId("genomeRefSeq")
    @ManyToOne
    @JoinColumn(name = "chromosome")
    private GenomeRefSeq genomeRefSeq;

    @MapsId("variantType")
    @ManyToOne
    @JoinColumn(name = "type")
    private VariantType variantType;

    @MapsId("locationType")
    @ManyToOne
    @JoinColumn(name = "loc_type")
    private LocationType locationType;

    @MapsId("variantEffect")
    @ManyToOne
    @JoinColumn(name = "variant_effect")
    private VariantEffect variantEffect;

    @ManyToOne
    @JoinColumn(name = "gene_id")
    private AnnotationGene gene;

    @Column(name = "refseq_gene")
    private String refSeqGene;

    @Column(name = "hgnc_gene")
    private String hgncGene;

    @Column(name = "transcr_pos")
    private Integer transcriptPosition;

    @Column(name = "cds_pos")
    private Integer codingSequencePosition;

    @Column(name = "aa_start")
    private Integer aminoAcidStart;

    @Column(name = "aa_end")
    private Integer aminoAcidEnd;

    @Column(name = "original_aa")
    private String originalAminoAcid;

    @Column(name = "final_aa")
    private String finalAminoAcid;

    @Column(name = "frameshift")
    private Boolean frameshift;

    @Column(name = "inframe")
    private Boolean inframe;

    @Column(name = "intron_exon_dist")
    private Integer intronExonDistance;

    @Column(name = "strand", length = 1)
    private String strand;

    @Column(name = "nummaps")
    private Integer numberOfTranscriptMaps;

    @Lob
    @Column(name = "refallele")
    private String referenceAllele;

    @Column(name = "altallele", length = 65535)
    private String alternateAllele;

    @Column(name = "hgvsgenomic", length = 131090)
    private String hgvsGenomic;

    @Column(name = "hgvscds", length = 131090)
    private String hgvsCodingSequence;

    @Column(name = "hgvstranscript", length = 131090)
    private String hgvsTranscript;

    @Column(name = "hgvsprotein", length = 65555)
    private String hgvsProtein;

    @Column(name = "noncan_exon")
    private Integer nonCanonicalExon;

    @Column(name = "feature_id")
    private Integer featureId;

    public Variants_61_2() {
        super();
    }

    public Variants_61_2(Variants_61_2PK key) {
        super();
        this.key = key;
    }

    public Variants_61_2PK getKey() {
        return key;
    }

    public void setKey(Variants_61_2PK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public GenomeRefSeq getGenomeRefSeq() {
        return genomeRefSeq;
    }

    public void setGenomeRefSeq(GenomeRefSeq genomeRefSeq) {
        this.genomeRefSeq = genomeRefSeq;
    }

    public VariantType getVariantType() {
        return variantType;
    }

    public void setVariantType(VariantType variantType) {
        this.variantType = variantType;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public VariantEffect getVariantEffect() {
        return variantEffect;
    }

    public void setVariantEffect(VariantEffect variantEffect) {
        this.variantEffect = variantEffect;
    }

    public String getRefSeqGene() {
        return refSeqGene;
    }

    public void setRefSeqGene(String refSeqGene) {
        this.refSeqGene = refSeqGene;
    }

    public String getHgncGene() {
        return hgncGene;
    }

    public void setHgncGene(String hgncGene) {
        this.hgncGene = hgncGene;
    }

    public Integer getTranscriptPosition() {
        return transcriptPosition;
    }

    public void setTranscriptPosition(Integer transcriptPosition) {
        this.transcriptPosition = transcriptPosition;
    }

    public Integer getCodingSequencePosition() {
        return codingSequencePosition;
    }

    public void setCodingSequencePosition(Integer codingSequencePosition) {
        this.codingSequencePosition = codingSequencePosition;
    }

    public Integer getAminoAcidStart() {
        return aminoAcidStart;
    }

    public void setAminoAcidStart(Integer aminoAcidStart) {
        this.aminoAcidStart = aminoAcidStart;
    }

    public Integer getAminoAcidEnd() {
        return aminoAcidEnd;
    }

    public void setAminoAcidEnd(Integer aminoAcidEnd) {
        this.aminoAcidEnd = aminoAcidEnd;
    }

    public String getOriginalAminoAcid() {
        return originalAminoAcid;
    }

    public void setOriginalAminoAcid(String originalAminoAcid) {
        this.originalAminoAcid = originalAminoAcid;
    }

    public String getFinalAminoAcid() {
        return finalAminoAcid;
    }

    public void setFinalAminoAcid(String finalAminoAcid) {
        this.finalAminoAcid = finalAminoAcid;
    }

    public Boolean getFrameshift() {
        return frameshift;
    }

    public void setFrameshift(Boolean frameshift) {
        this.frameshift = frameshift;
    }

    public Boolean getInframe() {
        return inframe;
    }

    public void setInframe(Boolean inframe) {
        this.inframe = inframe;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public Integer getIntronExonDistance() {
        return intronExonDistance;
    }

    public void setIntronExonDistance(Integer intronExonDistance) {
        this.intronExonDistance = intronExonDistance;
    }

    public Integer getNumberOfTranscriptMaps() {
        return numberOfTranscriptMaps;
    }

    public void setNumberOfTranscriptMaps(Integer numberOfTranscriptMaps) {
        this.numberOfTranscriptMaps = numberOfTranscriptMaps;
    }

    public AnnotationGene getGene() {
        return gene;
    }

    public void setGene(AnnotationGene gene) {
        this.gene = gene;
    }

    public String getReferenceAllele() {
        return referenceAllele;
    }

    public void setReferenceAllele(String referenceAllele) {
        this.referenceAllele = referenceAllele;
    }

    public String getAlternateAllele() {
        return alternateAllele;
    }

    public void setAlternateAllele(String alternateAllele) {
        this.alternateAllele = alternateAllele;
    }

    public String getHgvsGenomic() {
        return hgvsGenomic;
    }

    public void setHgvsGenomic(String hgvsGenomic) {
        this.hgvsGenomic = hgvsGenomic;
    }

    public String getHgvsCodingSequence() {
        return hgvsCodingSequence;
    }

    public void setHgvsCodingSequence(String hgvsCodingSequence) {
        this.hgvsCodingSequence = hgvsCodingSequence;
    }

    public String getHgvsTranscript() {
        return hgvsTranscript;
    }

    public void setHgvsTranscript(String hgvsTranscript) {
        this.hgvsTranscript = hgvsTranscript;
    }

    public String getHgvsProtein() {
        return hgvsProtein;
    }

    public void setHgvsProtein(String hgvsProtein) {
        this.hgvsProtein = hgvsProtein;
    }

    public Integer getNonCanonicalExon() {
        return nonCanonicalExon;
    }

    public void setNonCanonicalExon(Integer nonCanonicalExon) {
        this.nonCanonicalExon = nonCanonicalExon;
    }

    public Integer getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Integer featureId) {
        this.featureId = featureId;
    }

    @Override
    public String toString() {
        return String.format(
                "Variants_61_2 [key=%s, refSeqGene=%s, hgncGene=%s, transcriptPosition=%s, codingSequencePosition=%s, aminoAcidStart=%s, aminoAcidEnd=%s, originalAminoAcid=%s, finalAminoAcid=%s, frameshift=%s, inframe=%s, intronExonDistance=%s, strand=%s, numberOfTranscriptMaps=%s, referenceAllele=%s, alternateAllele=%s, hgvsGenomic=%s, hgvsCodingSequence=%s, hgvsTranscript=%s, hgvsProtein=%s, nonCanonicalExon=%s, featureId=%s]",
                key, refSeqGene, hgncGene, transcriptPosition, codingSequencePosition, aminoAcidStart, aminoAcidEnd, originalAminoAcid,
                finalAminoAcid, frameshift, inframe, intronExonDistance, strand, numberOfTranscriptMaps, referenceAllele, alternateAllele,
                hgvsGenomic, hgvsCodingSequence, hgvsTranscript, hgvsProtein, nonCanonicalExon, featureId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alternateAllele == null) ? 0 : alternateAllele.hashCode());
        result = prime * result + ((aminoAcidEnd == null) ? 0 : aminoAcidEnd.hashCode());
        result = prime * result + ((aminoAcidStart == null) ? 0 : aminoAcidStart.hashCode());
        result = prime * result + ((codingSequencePosition == null) ? 0 : codingSequencePosition.hashCode());
        result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
        result = prime * result + ((finalAminoAcid == null) ? 0 : finalAminoAcid.hashCode());
        result = prime * result + ((frameshift == null) ? 0 : frameshift.hashCode());
        result = prime * result + ((hgncGene == null) ? 0 : hgncGene.hashCode());
        result = prime * result + ((hgvsCodingSequence == null) ? 0 : hgvsCodingSequence.hashCode());
        result = prime * result + ((hgvsGenomic == null) ? 0 : hgvsGenomic.hashCode());
        result = prime * result + ((hgvsProtein == null) ? 0 : hgvsProtein.hashCode());
        result = prime * result + ((hgvsTranscript == null) ? 0 : hgvsTranscript.hashCode());
        result = prime * result + ((inframe == null) ? 0 : inframe.hashCode());
        result = prime * result + ((intronExonDistance == null) ? 0 : intronExonDistance.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((nonCanonicalExon == null) ? 0 : nonCanonicalExon.hashCode());
        result = prime * result + ((numberOfTranscriptMaps == null) ? 0 : numberOfTranscriptMaps.hashCode());
        result = prime * result + ((originalAminoAcid == null) ? 0 : originalAminoAcid.hashCode());
        result = prime * result + ((refSeqGene == null) ? 0 : refSeqGene.hashCode());
        result = prime * result + ((referenceAllele == null) ? 0 : referenceAllele.hashCode());
        result = prime * result + ((strand == null) ? 0 : strand.hashCode());
        result = prime * result + ((transcriptPosition == null) ? 0 : transcriptPosition.hashCode());
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
        Variants_61_2 other = (Variants_61_2) obj;
        if (alternateAllele == null) {
            if (other.alternateAllele != null)
                return false;
        } else if (!alternateAllele.equals(other.alternateAllele))
            return false;
        if (aminoAcidEnd == null) {
            if (other.aminoAcidEnd != null)
                return false;
        } else if (!aminoAcidEnd.equals(other.aminoAcidEnd))
            return false;
        if (aminoAcidStart == null) {
            if (other.aminoAcidStart != null)
                return false;
        } else if (!aminoAcidStart.equals(other.aminoAcidStart))
            return false;
        if (codingSequencePosition == null) {
            if (other.codingSequencePosition != null)
                return false;
        } else if (!codingSequencePosition.equals(other.codingSequencePosition))
            return false;
        if (featureId == null) {
            if (other.featureId != null)
                return false;
        } else if (!featureId.equals(other.featureId))
            return false;
        if (finalAminoAcid == null) {
            if (other.finalAminoAcid != null)
                return false;
        } else if (!finalAminoAcid.equals(other.finalAminoAcid))
            return false;
        if (frameshift == null) {
            if (other.frameshift != null)
                return false;
        } else if (!frameshift.equals(other.frameshift))
            return false;
        if (hgncGene == null) {
            if (other.hgncGene != null)
                return false;
        } else if (!hgncGene.equals(other.hgncGene))
            return false;
        if (hgvsCodingSequence == null) {
            if (other.hgvsCodingSequence != null)
                return false;
        } else if (!hgvsCodingSequence.equals(other.hgvsCodingSequence))
            return false;
        if (hgvsGenomic == null) {
            if (other.hgvsGenomic != null)
                return false;
        } else if (!hgvsGenomic.equals(other.hgvsGenomic))
            return false;
        if (hgvsProtein == null) {
            if (other.hgvsProtein != null)
                return false;
        } else if (!hgvsProtein.equals(other.hgvsProtein))
            return false;
        if (hgvsTranscript == null) {
            if (other.hgvsTranscript != null)
                return false;
        } else if (!hgvsTranscript.equals(other.hgvsTranscript))
            return false;
        if (inframe == null) {
            if (other.inframe != null)
                return false;
        } else if (!inframe.equals(other.inframe))
            return false;
        if (intronExonDistance == null) {
            if (other.intronExonDistance != null)
                return false;
        } else if (!intronExonDistance.equals(other.intronExonDistance))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (nonCanonicalExon == null) {
            if (other.nonCanonicalExon != null)
                return false;
        } else if (!nonCanonicalExon.equals(other.nonCanonicalExon))
            return false;
        if (numberOfTranscriptMaps == null) {
            if (other.numberOfTranscriptMaps != null)
                return false;
        } else if (!numberOfTranscriptMaps.equals(other.numberOfTranscriptMaps))
            return false;
        if (originalAminoAcid == null) {
            if (other.originalAminoAcid != null)
                return false;
        } else if (!originalAminoAcid.equals(other.originalAminoAcid))
            return false;
        if (refSeqGene == null) {
            if (other.refSeqGene != null)
                return false;
        } else if (!refSeqGene.equals(other.refSeqGene))
            return false;
        if (referenceAllele == null) {
            if (other.referenceAllele != null)
                return false;
        } else if (!referenceAllele.equals(other.referenceAllele))
            return false;
        if (strand == null) {
            if (other.strand != null)
                return false;
        } else if (!strand.equals(other.strand))
            return false;
        if (transcriptPosition == null) {
            if (other.transcriptPosition != null)
                return false;
        } else if (!transcriptPosition.equals(other.transcriptPosition))
            return false;
        return true;
    }

}
