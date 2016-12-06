package org.renci.binning.dao.refseq.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.annotation.model.AnnotationGene;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.VariantType;

@Entity
@Table(schema = "refseq", name = "variants_48_2")
@NamedQueries({
        @NamedQuery(name = "Variants_48_2.findByLocatedVariantId", query = "SELECT a FROM Variants_48_2 a join a.LocatedVariant b where b.id = :LocatedVariantId") })
public class Variants_48_2 implements Persistable {

    private static final long serialVersionUID = -3196283582319841870L;

    @EmbeddedId
    private Variants_48_2PK key;

    @MapsId("locatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @MapsId("genomeRefSeq")
    @ManyToOne
    @JoinColumn(name = "chromosome")
    private GenomeRefSeq genomeRefSeq;

    @MapsId("type")
    @ManyToOne
    @JoinColumn(name = "type")
    private VariantType type;

    @MapsId("locationType")
    @ManyToOne
    @JoinColumn(name = "loc_type")
    private LocationType locationType;

    @MapsId("variantEffect")
    @ManyToOne
    @JoinColumn(name = "variant_effect")
    private VariantEffect variantEffect;

    @Column(name = "refseq_gene")
    private String refseqGene;

    @Column(name = "hgnc_gene")
    private String hgncGene;

    @Column(name = "transcr_pos")
    private Integer transcrPos;

    @Column(name = "cds_pos")
    private Integer cdsPos;

    @Column(name = "aa_start")
    private Integer aaStart;

    @Column(name = "aa_end")
    private Integer aaEnd;

    @Column(name = "original_aa")
    private String originalAa;

    @Column(name = "final_aa")
    private String finalAa;

    @Column(name = "frameshift")
    private Boolean frameshift;

    @Column(name = "inframe")
    private Boolean inframe;

    @Column(name = "intron_exon_dist")
    private Integer intronExonDist;

    @Column(name = "strand", length = 1)
    private String strand;

    @Column(name = "nummaps")
    private Integer nummaps;

    @ManyToOne
    @JoinColumn(name = "gene_id")
    private AnnotationGene gene;

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
    private Integer noncanExon;

    @Column(name = "feature_id")
    private Integer featureId;

    public Variants_48_2() {
        super();
    }

    public Variants_48_2PK getKey() {
        return key;
    }

    public void setKey(Variants_48_2PK key) {
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

    public VariantType getType() {
        return type;
    }

    public void setType(VariantType type) {
        this.type = type;
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

    public String getRefseqGene() {
        return refseqGene;
    }

    public void setRefseqGene(String refseqGene) {
        this.refseqGene = refseqGene;
    }

    public String getHgncGene() {
        return hgncGene;
    }

    public void setHgncGene(String hgncGene) {
        this.hgncGene = hgncGene;
    }

    public Integer getTranscrPos() {
        return transcrPos;
    }

    public void setTranscrPos(Integer transcrPos) {
        this.transcrPos = transcrPos;
    }

    public Integer getCdsPos() {
        return cdsPos;
    }

    public void setCdsPos(Integer cdsPos) {
        this.cdsPos = cdsPos;
    }

    public Integer getAaStart() {
        return aaStart;
    }

    public void setAaStart(Integer aaStart) {
        this.aaStart = aaStart;
    }

    public Integer getAaEnd() {
        return aaEnd;
    }

    public void setAaEnd(Integer aaEnd) {
        this.aaEnd = aaEnd;
    }

    public String getOriginalAa() {
        return originalAa;
    }

    public void setOriginalAa(String originalAa) {
        this.originalAa = originalAa;
    }

    public String getFinalAa() {
        return finalAa;
    }

    public void setFinalAa(String finalAa) {
        this.finalAa = finalAa;
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

    public Integer getIntronExonDist() {
        return intronExonDist;
    }

    public void setIntronExonDist(Integer intronExonDist) {
        this.intronExonDist = intronExonDist;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public Integer getNummaps() {
        return nummaps;
    }

    public void setNummaps(Integer nummaps) {
        this.nummaps = nummaps;
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

    public Integer getNoncanExon() {
        return noncanExon;
    }

    public void setNoncanExon(Integer noncanExon) {
        this.noncanExon = noncanExon;
    }

    public Integer getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Integer featureId) {
        this.featureId = featureId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aaEnd == null) ? 0 : aaEnd.hashCode());
        result = prime * result + ((aaStart == null) ? 0 : aaStart.hashCode());
        result = prime * result + ((alternateAllele == null) ? 0 : alternateAllele.hashCode());
        result = prime * result + ((cdsPos == null) ? 0 : cdsPos.hashCode());
        result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
        result = prime * result + ((finalAa == null) ? 0 : finalAa.hashCode());
        result = prime * result + ((frameshift == null) ? 0 : frameshift.hashCode());
        result = prime * result + ((hgncGene == null) ? 0 : hgncGene.hashCode());
        result = prime * result + ((hgvsCodingSequence == null) ? 0 : hgvsCodingSequence.hashCode());
        result = prime * result + ((hgvsGenomic == null) ? 0 : hgvsGenomic.hashCode());
        result = prime * result + ((hgvsProtein == null) ? 0 : hgvsProtein.hashCode());
        result = prime * result + ((hgvsTranscript == null) ? 0 : hgvsTranscript.hashCode());
        result = prime * result + ((inframe == null) ? 0 : inframe.hashCode());
        result = prime * result + ((intronExonDist == null) ? 0 : intronExonDist.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((noncanExon == null) ? 0 : noncanExon.hashCode());
        result = prime * result + ((nummaps == null) ? 0 : nummaps.hashCode());
        result = prime * result + ((originalAa == null) ? 0 : originalAa.hashCode());
        result = prime * result + ((referenceAllele == null) ? 0 : referenceAllele.hashCode());
        result = prime * result + ((refseqGene == null) ? 0 : refseqGene.hashCode());
        result = prime * result + ((strand == null) ? 0 : strand.hashCode());
        result = prime * result + ((transcrPos == null) ? 0 : transcrPos.hashCode());
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
        Variants_48_2 other = (Variants_48_2) obj;
        if (aaEnd == null) {
            if (other.aaEnd != null)
                return false;
        } else if (!aaEnd.equals(other.aaEnd))
            return false;
        if (aaStart == null) {
            if (other.aaStart != null)
                return false;
        } else if (!aaStart.equals(other.aaStart))
            return false;
        if (alternateAllele == null) {
            if (other.alternateAllele != null)
                return false;
        } else if (!alternateAllele.equals(other.alternateAllele))
            return false;
        if (cdsPos == null) {
            if (other.cdsPos != null)
                return false;
        } else if (!cdsPos.equals(other.cdsPos))
            return false;
        if (featureId == null) {
            if (other.featureId != null)
                return false;
        } else if (!featureId.equals(other.featureId))
            return false;
        if (finalAa == null) {
            if (other.finalAa != null)
                return false;
        } else if (!finalAa.equals(other.finalAa))
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
        if (intronExonDist == null) {
            if (other.intronExonDist != null)
                return false;
        } else if (!intronExonDist.equals(other.intronExonDist))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (noncanExon == null) {
            if (other.noncanExon != null)
                return false;
        } else if (!noncanExon.equals(other.noncanExon))
            return false;
        if (nummaps == null) {
            if (other.nummaps != null)
                return false;
        } else if (!nummaps.equals(other.nummaps))
            return false;
        if (originalAa == null) {
            if (other.originalAa != null)
                return false;
        } else if (!originalAa.equals(other.originalAa))
            return false;
        if (referenceAllele == null) {
            if (other.referenceAllele != null)
                return false;
        } else if (!referenceAllele.equals(other.referenceAllele))
            return false;
        if (refseqGene == null) {
            if (other.refseqGene != null)
                return false;
        } else if (!refseqGene.equals(other.refseqGene))
            return false;
        if (strand == null) {
            if (other.strand != null)
                return false;
        } else if (!strand.equals(other.strand))
            return false;
        if (transcrPos == null) {
            if (other.transcrPos != null)
                return false;
        } else if (!transcrPos.equals(other.transcrPos))
            return false;
        return true;
    }

}
