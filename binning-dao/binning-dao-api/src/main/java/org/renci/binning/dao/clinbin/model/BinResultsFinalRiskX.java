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
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "clinbin", name = "bin_results_final_riskx")
public class BinResultsFinalRiskX implements Persistable {

    private static final long serialVersionUID = -4432334844095779990L;

    @EmbeddedId
    private BinResultsFinalRiskXPK key;

    @MapsId("incidentalBin")
    @ManyToOne
    @JoinColumn(name = "incidental_bin_id")
    private IncidentalBinX incidentalBin;

    @MapsId("incidentalResultVersion")
    @ManyToOne
    @JoinColumn(name = "incidental_result_version")
    private IncidentalResultVersionX incidentalResultVersion;

    @MapsId("assembly")
    @ManyToOne
    @JoinColumn(name = "asm_id")
    private Assembly assembly;

    @MapsId("LocatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @MapsId("phenotype")
    @ManyToOne
    @JoinColumn(name = "phenotype_id")
    private PhenotypeX phenotype;

    @Column(name = "chromosome", length = 15)
    private String chromosome;

    @Column(name = "pos")
    private Integer pos;

    @Column(name = "type", length = 15)
    private String type;

    @Lob
    @Column(name = "ref_allele")
    private String refAllele;

    @Lob
    @Column(name = "alt_allele")
    private String alternateAllele;

    @Lob
    @Column(name = "risk_allele")
    private String riskAllele;

    @Column(name = "num_risk_alleles")
    private Integer numRiskAlleles;

    @Column(name = "num_alleles")
    private Integer numAlleles;

    @Column(name = "rs_id")
    private Integer rsId;

    @Lob
    @Column(name = "vcf_file")
    private String vcfFile;

    @Column(name = "gq")
    private Double gq;

    @Column(name = "dp")
    private Double dp;

    @Column(name = "refdepth")
    private Integer refDepth;

    @Column(name = "altdepth")
    private Integer altDepth;

    public BinResultsFinalRiskX() {
        super();
    }

    public BinResultsFinalRiskXPK getKey() {
        return key;
    }

    public void setKey(BinResultsFinalRiskXPK key) {
        this.key = key;
    }

    public IncidentalBinX getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(IncidentalBinX incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    public IncidentalResultVersionX getIncidentalResultVersion() {
        return incidentalResultVersion;
    }

    public void setIncidentalResultVersion(IncidentalResultVersionX incidentalResultVersion) {
        this.incidentalResultVersion = incidentalResultVersion;
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

    public PhenotypeX getPhenotype() {
        return phenotype;
    }

    public void setPhenotypeX(PhenotypeX phenotype) {
        this.phenotype = phenotype;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefAllele() {
        return refAllele;
    }

    public void setRefAllele(String refAllele) {
        this.refAllele = refAllele;
    }

    public String getAlternateAllele() {
        return alternateAllele;
    }

    public void setAlternateAllele(String alternateAllele) {
        this.alternateAllele = alternateAllele;
    }

    public String getRiskAllele() {
        return riskAllele;
    }

    public void setRiskAllele(String riskAllele) {
        this.riskAllele = riskAllele;
    }

    public Integer getNumRiskAlleles() {
        return numRiskAlleles;
    }

    public void setNumRiskAlleles(Integer numRiskAlleles) {
        this.numRiskAlleles = numRiskAlleles;
    }

    public Integer getNumAlleles() {
        return numAlleles;
    }

    public void setNumAlleles(Integer numAlleles) {
        this.numAlleles = numAlleles;
    }

    public Integer getRsId() {
        return rsId;
    }

    public void setRsId(Integer rsId) {
        this.rsId = rsId;
    }

    public String getVcfFile() {
        return vcfFile;
    }

    public void setVcfFile(String vcfFile) {
        this.vcfFile = vcfFile;
    }

    public Double getGq() {
        return gq;
    }

    public void setGq(Double gq) {
        this.gq = gq;
    }

    public Double getDp() {
        return dp;
    }

    public void setDp(Double dp) {
        this.dp = dp;
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

    @Override
    public String toString() {
        return String.format(
                "BinResultsFinalRiskX [chromosome=%s, pos=%s, type=%s, refAllele=%s, alternateAllele=%s, riskAllele=%s, numRiskAlleles=%s, numAlleles=%s, rsId=%s, vcfFile=%s, gq=%s, dp=%s, refDepth=%s, altDepth=%s]",
                chromosome, pos, type, refAllele, alternateAllele, riskAllele, numRiskAlleles, numAlleles, rsId, vcfFile, gq, dp, refDepth,
                altDepth);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((altDepth == null) ? 0 : altDepth.hashCode());
        result = prime * result + ((alternateAllele == null) ? 0 : alternateAllele.hashCode());
        result = prime * result + ((chromosome == null) ? 0 : chromosome.hashCode());
        result = prime * result + ((dp == null) ? 0 : dp.hashCode());
        result = prime * result + ((gq == null) ? 0 : gq.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((numAlleles == null) ? 0 : numAlleles.hashCode());
        result = prime * result + ((numRiskAlleles == null) ? 0 : numRiskAlleles.hashCode());
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
        result = prime * result + ((refAllele == null) ? 0 : refAllele.hashCode());
        result = prime * result + ((refDepth == null) ? 0 : refDepth.hashCode());
        result = prime * result + ((riskAllele == null) ? 0 : riskAllele.hashCode());
        result = prime * result + ((rsId == null) ? 0 : rsId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((vcfFile == null) ? 0 : vcfFile.hashCode());
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
        BinResultsFinalRiskX other = (BinResultsFinalRiskX) obj;
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
        if (chromosome == null) {
            if (other.chromosome != null)
                return false;
        } else if (!chromosome.equals(other.chromosome))
            return false;
        if (dp == null) {
            if (other.dp != null)
                return false;
        } else if (!dp.equals(other.dp))
            return false;
        if (gq == null) {
            if (other.gq != null)
                return false;
        } else if (!gq.equals(other.gq))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (numAlleles == null) {
            if (other.numAlleles != null)
                return false;
        } else if (!numAlleles.equals(other.numAlleles))
            return false;
        if (numRiskAlleles == null) {
            if (other.numRiskAlleles != null)
                return false;
        } else if (!numRiskAlleles.equals(other.numRiskAlleles))
            return false;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        if (refAllele == null) {
            if (other.refAllele != null)
                return false;
        } else if (!refAllele.equals(other.refAllele))
            return false;
        if (refDepth == null) {
            if (other.refDepth != null)
                return false;
        } else if (!refDepth.equals(other.refDepth))
            return false;
        if (riskAllele == null) {
            if (other.riskAllele != null)
                return false;
        } else if (!riskAllele.equals(other.riskAllele))
            return false;
        if (rsId == null) {
            if (other.rsId != null)
                return false;
        } else if (!rsId.equals(other.rsId))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (vcfFile == null) {
            if (other.vcfFile != null)
                return false;
        } else if (!vcfFile.equals(other.vcfFile))
            return false;
        return true;
    }

}
