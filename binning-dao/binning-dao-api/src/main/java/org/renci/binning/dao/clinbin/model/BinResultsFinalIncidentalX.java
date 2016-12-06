package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.dbsnp.model.SNPMappingAgg;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.binning.dao.refseq.model.LocationType;
import org.renci.binning.dao.refseq.model.VariantEffect;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "clinbin", name = "bin_results_final_incidentalx")
public class BinResultsFinalIncidentalX implements Persistable {

    private static final long serialVersionUID = 702358141551470361L;

    @EmbeddedId
    private BinResultsFinalIncidentalXPK key;

    @MapsId("incidentalResultVersion")
    @ManyToOne
    @JoinColumn(name = "incidental_result_version")
    private IncidentalResultVersionX incidentalResultVersion;

    @MapsId("incidentalBin")
    @ManyToOne
    @JoinColumn(name = "incidental_bin_id")
    private IncidentalBinX incidentalBin;

    @MapsId("assembly")
    @ManyToOne
    @JoinColumn(name = "asm_id")
    private Assembly assembly;

    @MapsId("LocatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @ManyToOne
    @JoinColumn(name = "zygosity_mode")
    private ZygosityModeType zygosityMode;

    @ManyToOne
    @JoinColumn(name = "phenotype_id")
    private PhenotypeX phenotype;

    @ManyToOne
    @JoinColumn(name = "carrier_status_id")
    private CarrierStatus carrierStatus;

    @Column(name = "chromosome", length = 15)
    private String chromosome;

    @Column(name = "pos")
    private Integer position;

    @Column(name = "type", length = 15)
    private String type;

    @Column(name = "transcr", length = 31)
    private String transcript;

    @Column(name = "refseq_gene")
    private String refseqGene;

    @Column(name = "hgnc_gene")
    private String hgncGene;

    @ManyToOne
    @JoinColumn(name = "loc_type")
    private LocationType locationType;

    @Column(name = "strand", length = 1)
    private String strand;

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

    @ManyToOne
    @JoinColumn(name = "variant_effect")
    private VariantEffect variantEffect;

    @Column(name = "nummaps")
    private Integer nummaps;

    @Column(name = "gene_id")
    private Integer geneId;

    @Column(name = "acc_num", length = 10)
    private String accessionNumber;

    @Lob
    @Column(name = "tag")
    private String tag;

    @Column(name = "max_allele_freq")
    private Double maxAlleleFrequency;

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

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "qd")
    private Double qd;

    @Column(name = "read_pos_rank_sum")
    private Double readPosRankSum;

    @Column(name = "frac_reads_with_dels")
    private Double fracReadsWithDels;

    @Column(name = "hrun")
    private Integer hrun;

    @Column(name = "strand_score")
    private Double strandScore;

    @Column(name = "ref_depth")
    private Integer refDepth;

    @Column(name = "alt_depth")
    private Integer altDepth;

    @Column(name = "homozygous")
    private Boolean homozygous;

    @Column(name = "genotype_qual")
    private Double genotypeQual;

    @Lob
    @Column(name = "rs_id")
    private String rsId;

    @Column(name = "ncg_alt_f")
    private Double NCGenesAlternateFrequency;

    @Column(name = "ncg_hwe_p")
    private Double NCGenesHWEP;

    public BinResultsFinalIncidentalX() {
        super();
    }

    public BinResultsFinalIncidentalX(BinResultsFinalIncidentalXPK key) {
        super();
        this.key = key;
    }

    public BinResultsFinalIncidentalX(BinResultsFinalIncidentalXPK key, Variants_61_2 variant, MaxFrequency maxFrequency,
            HGMDLocatedVariant hgmdLocatedVariant, AssemblyLocatedVariant assemblyLocatedVariant,
            AssemblyLocatedVariantQC assemblyLocatedVariantQC, NCGenesFrequencies ncgenesFrequencies, SNPMappingAgg snpMappingAgg) {
        this(key);

        // variant can't be null
        this.alternateAllele = variant.getAlternateAllele();
        this.aminoAcidEnd = variant.getAminoAcidEnd();
        this.aminoAcidStart = variant.getAminoAcidStart();
        this.codingSequencePosition = variant.getCodingSequencePosition();
        this.finalAminoAcid = variant.getFinalAminoAcid();
        this.frameshift = variant.getFrameshift();
        this.geneId = variant.getGene().getId();
        this.hgncGene = variant.getHgncGene();
        this.hgvsCodingSequence = variant.getHgvsCodingSequence();
        this.hgvsGenomic = variant.getHgvsGenomic();
        this.hgvsProtein = variant.getHgvsProtein();
        this.hgvsTranscript = variant.getHgvsTranscript();
        this.inframe = variant.getInframe();
        this.intronExonDistance = variant.getIntronExonDistance();
        this.locationType = variant.getLocationType();
        this.nummaps = variant.getNumberOfTranscriptMaps();
        this.originalAminoAcid = variant.getOriginalAminoAcid();
        this.strand = variant.getStrand();
        this.position = variant.getKey().getPosition();
        this.referenceAllele = variant.getReferenceAllele();
        this.refseqGene = variant.getRefSeqGene();
        this.transcriptPosition = variant.getTranscriptPosition();
        this.type = variant.getVariantType().getName();
        this.variantEffect = variant.getVariantEffect();

        if (hgmdLocatedVariant != null) {
            this.accessionNumber = hgmdLocatedVariant.getKey().getAccession();
            this.tag = hgmdLocatedVariant.getTag();
        }

        if (assemblyLocatedVariant != null) {
            this.homozygous = assemblyLocatedVariant.getHomozygous();
            this.genotypeQual = assemblyLocatedVariant.getGenotypeQuality();
        }

        if (assemblyLocatedVariantQC != null) {
            this.altDepth = assemblyLocatedVariantQC.getAltDepth();
            this.refDepth = assemblyLocatedVariantQC.getRefDepth();
            this.depth = assemblyLocatedVariantQC.getDepth();
            this.fracReadsWithDels = assemblyLocatedVariantQC.getFracReadsWithDels();
            this.hrun = assemblyLocatedVariantQC.getHomopolymerRun();
            this.qd = assemblyLocatedVariantQC.getQualityByDepth();
            this.strandScore = assemblyLocatedVariantQC.getStrandScore();
            this.readPosRankSum = assemblyLocatedVariantQC.getReadPosRankSum();
        }

        if (ncgenesFrequencies != null) {
            this.NCGenesAlternateFrequency = ncgenesFrequencies.getAltAlleleFrequency() != null ? ncgenesFrequencies.getAltAlleleFrequency()
                    : 0D;
            this.NCGenesHWEP = ncgenesFrequencies.getHweP() != null ? ncgenesFrequencies.getHweP() : 1D;
        }

        if (maxFrequency != null) {
            this.maxAlleleFrequency = maxFrequency.getMaxAlleleFreq();
        }

        if (snpMappingAgg != null) {
            this.rsId = snpMappingAgg.getRsIds();
        }
    }

    public BinResultsFinalIncidentalXPK getKey() {
        return key;
    }

    public void setKey(BinResultsFinalIncidentalXPK key) {
        this.key = key;
    }

    public IncidentalResultVersionX getIncidentalResultVersion() {
        return incidentalResultVersion;
    }

    public void setIncidentalResultVersion(IncidentalResultVersionX incidentalResultVersion) {
        this.incidentalResultVersion = incidentalResultVersion;
    }

    public IncidentalBinX getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(IncidentalBinX incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    public Assembly getAssembly() {
        return assembly;
    }

    public void setAssembly(Assembly assembly) {
        this.assembly = assembly;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public ZygosityModeType getZygosityMode() {
        return zygosityMode;
    }

    public void setZygosityMode(ZygosityModeType zygosityMode) {
        this.zygosityMode = zygosityMode;
    }

    public PhenotypeX getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(PhenotypeX phenotype) {
        this.phenotype = phenotype;
    }

    public CarrierStatus getCarrierStatus() {
        return carrierStatus;
    }

    public void setCarrierStatus(CarrierStatus carrierStatus) {
        this.carrierStatus = carrierStatus;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
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

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
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

    public Integer getIntronExonDistance() {
        return intronExonDistance;
    }

    public void setIntronExonDistance(Integer intronExonDistance) {
        this.intronExonDistance = intronExonDistance;
    }

    public VariantEffect getVariantEffect() {
        return variantEffect;
    }

    public void setVariantEffect(VariantEffect variantEffect) {
        this.variantEffect = variantEffect;
    }

    public Integer getNummaps() {
        return nummaps;
    }

    public void setNummaps(Integer nummaps) {
        this.nummaps = nummaps;
    }

    public Integer getGeneId() {
        return geneId;
    }

    public void setGeneId(Integer geneId) {
        this.geneId = geneId;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getMaxAlleleFrequency() {
        return maxAlleleFrequency;
    }

    public void setMaxAlleleFrequency(Double maxAlleleFrequency) {
        this.maxAlleleFrequency = maxAlleleFrequency;
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

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Double getQd() {
        return qd;
    }

    public void setQd(Double qd) {
        this.qd = qd;
    }

    public Double getReadPosRankSum() {
        return readPosRankSum;
    }

    public void setReadPosRankSum(Double readPosRankSum) {
        this.readPosRankSum = readPosRankSum;
    }

    public Double getFracReadsWithDels() {
        return fracReadsWithDels;
    }

    public void setFracReadsWithDels(Double fracReadsWithDels) {
        this.fracReadsWithDels = fracReadsWithDels;
    }

    public Integer getHrun() {
        return hrun;
    }

    public void setHrun(Integer hrun) {
        this.hrun = hrun;
    }

    public Double getStrandScore() {
        return strandScore;
    }

    public void setStrandScore(Double strandScore) {
        this.strandScore = strandScore;
    }

    public Integer getRefDepth() {
        return refDepth;
    }

    public void setRefDepth(Integer refDepth) {
        this.refDepth = refDepth;
    }

    public Integer getAltDepth() {
        return altDepth;
    }

    public void setAltDepth(Integer altDepth) {
        this.altDepth = altDepth;
    }

    public Boolean getHomozygous() {
        return homozygous;
    }

    public void setHomozygous(Boolean homozygous) {
        this.homozygous = homozygous;
    }

    public Double getGenotypeQual() {
        return genotypeQual;
    }

    public void setGenotypeQual(Double genotypeQual) {
        this.genotypeQual = genotypeQual;
    }

    public String getRsId() {
        return rsId;
    }

    public void setRsId(String rsId) {
        this.rsId = rsId;
    }

    public Double getNCGenesAlternateFrequency() {
        return this.NCGenesAlternateFrequency;
    }

    public void setNCGenesAlternateFrequency(Double nCGenesAlternateFrequency) {
        NCGenesAlternateFrequency = nCGenesAlternateFrequency;
    }

    public Double getNCGenesHWEP() {
        return this.NCGenesHWEP;
    }

    public void setNCGenesHWEP(Double nCGenesHWEP) {
        NCGenesHWEP = nCGenesHWEP;
    }

    @Override
    public String toString() {
        return String.format(
                "BinResultsFinalIncidentalX [key=%s, chromosome=%s, position=%s, type=%s, transcript=%s, refseqGene=%s, hgncGene=%s, strand=%s, transcriptPosition=%s, codingSequencePosition=%s, aminoAcidStart=%s, aminoAcidEnd=%s, originalAminoAcid=%s, finalAminoAcid=%s, frameshift=%s, inframe=%s, intronExonDistance=%s, nummaps=%s, geneId=%s, accessionNumber=%s, tag=%s, maxAlleleFrequency=%s, referenceAllele=%s, alternateAllele=%s, hgvsGenomic=%s, hgvsCodingSequence=%s, hgvsTranscript=%s, hgvsProtein=%s, depth=%s, qd=%s, readPosRankSum=%s, fracReadsWithDels=%s, hrun=%s, strandScore=%s, refDepth=%s, altDepth=%s, homozygous=%s, genotypeQual=%s, rsId=%s, NCGenesAlternateFrequency=%s, NCGenesHWEP=%s]",
                key, chromosome, position, type, transcript, refseqGene, hgncGene, strand, transcriptPosition, codingSequencePosition,
                aminoAcidStart, aminoAcidEnd, originalAminoAcid, finalAminoAcid, frameshift, inframe, intronExonDistance, nummaps, geneId,
                accessionNumber, tag, maxAlleleFrequency, referenceAllele, alternateAllele, hgvsGenomic, hgvsCodingSequence, hgvsTranscript,
                hgvsProtein, depth, qd, readPosRankSum, fracReadsWithDels, hrun, strandScore, refDepth, altDepth, homozygous, genotypeQual,
                rsId, NCGenesAlternateFrequency, NCGenesHWEP);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((NCGenesAlternateFrequency == null) ? 0 : NCGenesAlternateFrequency.hashCode());
        result = prime * result + ((NCGenesHWEP == null) ? 0 : NCGenesHWEP.hashCode());
        result = prime * result + ((accessionNumber == null) ? 0 : accessionNumber.hashCode());
        result = prime * result + ((altDepth == null) ? 0 : altDepth.hashCode());
        result = prime * result + ((alternateAllele == null) ? 0 : alternateAllele.hashCode());
        result = prime * result + ((aminoAcidEnd == null) ? 0 : aminoAcidEnd.hashCode());
        result = prime * result + ((aminoAcidStart == null) ? 0 : aminoAcidStart.hashCode());
        result = prime * result + ((chromosome == null) ? 0 : chromosome.hashCode());
        result = prime * result + ((codingSequencePosition == null) ? 0 : codingSequencePosition.hashCode());
        result = prime * result + ((depth == null) ? 0 : depth.hashCode());
        result = prime * result + ((finalAminoAcid == null) ? 0 : finalAminoAcid.hashCode());
        result = prime * result + ((fracReadsWithDels == null) ? 0 : fracReadsWithDels.hashCode());
        result = prime * result + ((frameshift == null) ? 0 : frameshift.hashCode());
        result = prime * result + ((geneId == null) ? 0 : geneId.hashCode());
        result = prime * result + ((genotypeQual == null) ? 0 : genotypeQual.hashCode());
        result = prime * result + ((hgncGene == null) ? 0 : hgncGene.hashCode());
        result = prime * result + ((hgvsCodingSequence == null) ? 0 : hgvsCodingSequence.hashCode());
        result = prime * result + ((hgvsGenomic == null) ? 0 : hgvsGenomic.hashCode());
        result = prime * result + ((hgvsProtein == null) ? 0 : hgvsProtein.hashCode());
        result = prime * result + ((hgvsTranscript == null) ? 0 : hgvsTranscript.hashCode());
        result = prime * result + ((homozygous == null) ? 0 : homozygous.hashCode());
        result = prime * result + ((hrun == null) ? 0 : hrun.hashCode());
        result = prime * result + ((inframe == null) ? 0 : inframe.hashCode());
        result = prime * result + ((intronExonDistance == null) ? 0 : intronExonDistance.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((maxAlleleFrequency == null) ? 0 : maxAlleleFrequency.hashCode());
        result = prime * result + ((nummaps == null) ? 0 : nummaps.hashCode());
        result = prime * result + ((originalAminoAcid == null) ? 0 : originalAminoAcid.hashCode());
        result = prime * result + ((phenotype == null) ? 0 : phenotype.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((qd == null) ? 0 : qd.hashCode());
        result = prime * result + ((readPosRankSum == null) ? 0 : readPosRankSum.hashCode());
        result = prime * result + ((refDepth == null) ? 0 : refDepth.hashCode());
        result = prime * result + ((referenceAllele == null) ? 0 : referenceAllele.hashCode());
        result = prime * result + ((refseqGene == null) ? 0 : refseqGene.hashCode());
        result = prime * result + ((rsId == null) ? 0 : rsId.hashCode());
        result = prime * result + ((strand == null) ? 0 : strand.hashCode());
        result = prime * result + ((strandScore == null) ? 0 : strandScore.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        result = prime * result + ((transcript == null) ? 0 : transcript.hashCode());
        result = prime * result + ((transcriptPosition == null) ? 0 : transcriptPosition.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((variantEffect == null) ? 0 : variantEffect.hashCode());
        result = prime * result + ((zygosityMode == null) ? 0 : zygosityMode.hashCode());
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
        BinResultsFinalIncidentalX other = (BinResultsFinalIncidentalX) obj;
        if (NCGenesAlternateFrequency == null) {
            if (other.NCGenesAlternateFrequency != null)
                return false;
        } else if (!NCGenesAlternateFrequency.equals(other.NCGenesAlternateFrequency))
            return false;
        if (NCGenesHWEP == null) {
            if (other.NCGenesHWEP != null)
                return false;
        } else if (!NCGenesHWEP.equals(other.NCGenesHWEP))
            return false;
        if (accessionNumber == null) {
            if (other.accessionNumber != null)
                return false;
        } else if (!accessionNumber.equals(other.accessionNumber))
            return false;
        if (altDepth == null) {
            if (other.altDepth != null)
                return false;
        } else if (!altDepth.equals(other.altDepth))
            return false;
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
        if (assembly == null) {
            if (other.assembly != null)
                return false;
        } else if (!assembly.equals(other.assembly))
            return false;
        if (carrierStatus == null) {
            if (other.carrierStatus != null)
                return false;
        } else if (!carrierStatus.equals(other.carrierStatus))
            return false;
        if (chromosome == null) {
            if (other.chromosome != null)
                return false;
        } else if (!chromosome.equals(other.chromosome))
            return false;
        if (codingSequencePosition == null) {
            if (other.codingSequencePosition != null)
                return false;
        } else if (!codingSequencePosition.equals(other.codingSequencePosition))
            return false;
        if (depth == null) {
            if (other.depth != null)
                return false;
        } else if (!depth.equals(other.depth))
            return false;
        if (finalAminoAcid == null) {
            if (other.finalAminoAcid != null)
                return false;
        } else if (!finalAminoAcid.equals(other.finalAminoAcid))
            return false;
        if (fracReadsWithDels == null) {
            if (other.fracReadsWithDels != null)
                return false;
        } else if (!fracReadsWithDels.equals(other.fracReadsWithDels))
            return false;
        if (frameshift == null) {
            if (other.frameshift != null)
                return false;
        } else if (!frameshift.equals(other.frameshift))
            return false;
        if (geneId == null) {
            if (other.geneId != null)
                return false;
        } else if (!geneId.equals(other.geneId))
            return false;
        if (genotypeQual == null) {
            if (other.genotypeQual != null)
                return false;
        } else if (!genotypeQual.equals(other.genotypeQual))
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
        if (homozygous == null) {
            if (other.homozygous != null)
                return false;
        } else if (!homozygous.equals(other.homozygous))
            return false;
        if (hrun == null) {
            if (other.hrun != null)
                return false;
        } else if (!hrun.equals(other.hrun))
            return false;
        if (incidentalBin == null) {
            if (other.incidentalBin != null)
                return false;
        } else if (!incidentalBin.equals(other.incidentalBin))
            return false;
        if (incidentalResultVersion == null) {
            if (other.incidentalResultVersion != null)
                return false;
        } else if (!incidentalResultVersion.equals(other.incidentalResultVersion))
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
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (locationType == null) {
            if (other.locationType != null)
                return false;
        } else if (!locationType.equals(other.locationType))
            return false;
        if (maxAlleleFrequency == null) {
            if (other.maxAlleleFrequency != null)
                return false;
        } else if (!maxAlleleFrequency.equals(other.maxAlleleFrequency))
            return false;
        if (nummaps == null) {
            if (other.nummaps != null)
                return false;
        } else if (!nummaps.equals(other.nummaps))
            return false;
        if (originalAminoAcid == null) {
            if (other.originalAminoAcid != null)
                return false;
        } else if (!originalAminoAcid.equals(other.originalAminoAcid))
            return false;
        if (phenotype == null) {
            if (other.phenotype != null)
                return false;
        } else if (!phenotype.equals(other.phenotype))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (qd == null) {
            if (other.qd != null)
                return false;
        } else if (!qd.equals(other.qd))
            return false;
        if (readPosRankSum == null) {
            if (other.readPosRankSum != null)
                return false;
        } else if (!readPosRankSum.equals(other.readPosRankSum))
            return false;
        if (refDepth == null) {
            if (other.refDepth != null)
                return false;
        } else if (!refDepth.equals(other.refDepth))
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
        if (rsId == null) {
            if (other.rsId != null)
                return false;
        } else if (!rsId.equals(other.rsId))
            return false;
        if (strand == null) {
            if (other.strand != null)
                return false;
        } else if (!strand.equals(other.strand))
            return false;
        if (strandScore == null) {
            if (other.strandScore != null)
                return false;
        } else if (!strandScore.equals(other.strandScore))
            return false;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        if (transcript == null) {
            if (other.transcript != null)
                return false;
        } else if (!transcript.equals(other.transcript))
            return false;
        if (transcriptPosition == null) {
            if (other.transcriptPosition != null)
                return false;
        } else if (!transcriptPosition.equals(other.transcriptPosition))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (variantEffect == null) {
            if (other.variantEffect != null)
                return false;
        } else if (!variantEffect.equals(other.variantEffect))
            return false;
        if (zygosityMode == null) {
            if (other.zygosityMode != null)
                return false;
        } else if (!zygosityMode.equals(other.zygosityMode))
            return false;
        return true;
    }

}
