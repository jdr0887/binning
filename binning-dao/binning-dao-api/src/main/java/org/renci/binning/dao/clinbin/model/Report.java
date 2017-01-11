package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "report")
public class Report implements Persistable {

    private static final long serialVersionUID = 1704688615170608496L;

    @EmbeddedId
    private ReportPK key;

    @Column(name = "total_variants")
    private Integer totalVariants;

    @Column(name = "n_var_loc_coding")
    private Integer numberOfCodingLocatedVariants;

    @Column(name = "n_var_loc_noncoding")
    private Integer numberOfNonCodingLocatedVariants;

    @Column(name = "n_var_loc_transcr_dep")
    private Integer numberOfTransriptDepLocatedVariants;

    @Column(name = "n_var_type_sub")
    private Integer numberOfSubstitutionTypes;

    @Column(name = "n_var_type_indel")
    private Integer numberOfIndelTypes;

    @Column(name = "n_var_eff_intergenic")
    private Integer numberOfIntergenicVariantEffects;

    @Column(name = "n_var_eff_intronic")
    private Integer numberOfIntronicVariantEffects;

    @Column(name = "n_var_eff_untrans")
    private Integer numberOfUntranslatedVariantEffects;

    @Column(name = "n_var_eff_synon")
    private Integer numberOfSynonymousVariantEffects;

    @Column(name = "n_var_eff_miss")
    private Integer numberOfMissenseVariantEffects;

    @Column(name = "n_var_eff_nonshiftindel")
    private Integer numberOfNonShiftIndelVariantEffects;

    @Column(name = "n_var_eff_shiftindel")
    private Integer numberOfShiftIndelVariantEffects;

    @Column(name = "n_var_eff_nonsense")
    private Integer numberOfNonsenseVariantEffects;

    @Column(name = "n_var_eff_stoploss")
    private Integer numberOfStoplossVariantEffects;

    @Column(name = "n_var_eff_splice")
    private Integer numberOfSpliceVariantEffects;

    @Column(name = "n_var_eff_other")
    private Integer numberOfOtherVariantEffects;

    @Column(name = "n_analyzed_var")
    private Integer numberOfAnalyzedVariants;

    @Column(name = "n_analyzed_hgmd_class_1")
    private Integer numberOfHGMDKnownPathenogenic;

    @Column(name = "n_analyzed_hgmd_class_2")
    private Integer numberOfHGMDLikelyPathenogenic;

    @Column(name = "n_analyzed_hgmd_class_3")
    private Integer numberOfHGMDPossiblyPathenogenic;

    @Column(name = "n_analyzed_hgmd_class_4")
    private Integer numberOfHGMDVariantsOfUncertainSignificance;

    @Column(name = "n_analyzed_hgmd_class_5")
    private Integer numberOfHGMDLikelyBenign;

    @Column(name = "n_analyzed_hgmd_class_6")
    private Integer numberOfHGMDAlmostCertainlyBenign;

    @Column(name = "n_analyzed_clinvar_class_1")
    private Integer numberOfClinVarKnownPathenogenic;

    @Column(name = "n_analyzed_clinvar_class_2")
    private Integer numberOfClinVarLikelyPathenogenic;

    @Column(name = "n_analyzed_clinvar_class_3")
    private Integer numberOfClinVarPossiblyPathenogenic;

    @Column(name = "n_analyzed_clinvar_class_4")
    private Integer numberOfClinVarVariantsOfUncertainSignificance;

    @Column(name = "n_analyzed_clinvar_class_5")
    private Integer numberOfClinVarLikelyBenign;

    @Column(name = "n_analyzed_clinvar_class_6")
    private Integer numberOfClinVarAlmostCertainlyBenign;

    public Report() {
        super();
    }

    public Report(ReportPK key) {
        super();
        this.key = key;
    }

    public ReportPK getKey() {
        return key;
    }

    public void setKey(ReportPK key) {
        this.key = key;
    }

    public Integer getTotalVariants() {
        return totalVariants;
    }

    public void setTotalVariants(Integer totalVariants) {
        this.totalVariants = totalVariants;
    }

    public Integer getNumberOfCodingLocatedVariants() {
        return numberOfCodingLocatedVariants;
    }

    public void setNumberOfCodingLocatedVariants(Integer numberOfCodingLocatedVariants) {
        this.numberOfCodingLocatedVariants = numberOfCodingLocatedVariants;
    }

    public Integer getNumberOfNonCodingLocatedVariants() {
        return numberOfNonCodingLocatedVariants;
    }

    public void setNumberOfNonCodingLocatedVariants(Integer numberOfNonCodingLocatedVariants) {
        this.numberOfNonCodingLocatedVariants = numberOfNonCodingLocatedVariants;
    }

    public Integer getNumberOfTransriptDepLocatedVariants() {
        return numberOfTransriptDepLocatedVariants;
    }

    public void setNumberOfTransriptDepLocatedVariants(Integer numberOfTransriptDepLocatedVariants) {
        this.numberOfTransriptDepLocatedVariants = numberOfTransriptDepLocatedVariants;
    }

    public Integer getNumberOfSubstitutionTypes() {
        return numberOfSubstitutionTypes;
    }

    public void setNumberOfSubstitutionTypes(Integer numberOfSubstitutionTypes) {
        this.numberOfSubstitutionTypes = numberOfSubstitutionTypes;
    }

    public Integer getNumberOfIndelTypes() {
        return numberOfIndelTypes;
    }

    public void setNumberOfIndelTypes(Integer numberOfIndelTypes) {
        this.numberOfIndelTypes = numberOfIndelTypes;
    }

    public Integer getNumberOfIntergenicVariantEffects() {
        return numberOfIntergenicVariantEffects;
    }

    public void setNumberOfIntergenicVariantEffects(Integer numberOfIntergenicVariantEffects) {
        this.numberOfIntergenicVariantEffects = numberOfIntergenicVariantEffects;
    }

    public Integer getNumberOfIntronicVariantEffects() {
        return numberOfIntronicVariantEffects;
    }

    public void setNumberOfIntronicVariantEffects(Integer numberOfIntronicVariantEffects) {
        this.numberOfIntronicVariantEffects = numberOfIntronicVariantEffects;
    }

    public Integer getNumberOfUntranslatedVariantEffects() {
        return numberOfUntranslatedVariantEffects;
    }

    public void setNumberOfUntranslatedVariantEffects(Integer numberOfUntranslatedVariantEffects) {
        this.numberOfUntranslatedVariantEffects = numberOfUntranslatedVariantEffects;
    }

    public Integer getNumberOfSynonymousVariantEffects() {
        return numberOfSynonymousVariantEffects;
    }

    public void setNumberOfSynonymousVariantEffects(Integer numberOfSynonymousVariantEffects) {
        this.numberOfSynonymousVariantEffects = numberOfSynonymousVariantEffects;
    }

    public Integer getNumberOfMissenseVariantEffects() {
        return numberOfMissenseVariantEffects;
    }

    public void setNumberOfMissenseVariantEffects(Integer numberOfMissenseVariantEffects) {
        this.numberOfMissenseVariantEffects = numberOfMissenseVariantEffects;
    }

    public Integer getNumberOfNonShiftIndelVariantEffects() {
        return numberOfNonShiftIndelVariantEffects;
    }

    public void setNumberOfNonShiftIndelVariantEffects(Integer numberOfNonShiftIndelVariantEffects) {
        this.numberOfNonShiftIndelVariantEffects = numberOfNonShiftIndelVariantEffects;
    }

    public Integer getNumberOfShiftIndelVariantEffects() {
        return numberOfShiftIndelVariantEffects;
    }

    public void setNumberOfShiftIndelVariantEffects(Integer numberOfShiftIndelVariantEffects) {
        this.numberOfShiftIndelVariantEffects = numberOfShiftIndelVariantEffects;
    }

    public Integer getNumberOfNonsenseVariantEffects() {
        return numberOfNonsenseVariantEffects;
    }

    public void setNumberOfNonsenseVariantEffects(Integer numberOfNonsenseVariantEffects) {
        this.numberOfNonsenseVariantEffects = numberOfNonsenseVariantEffects;
    }

    public Integer getNumberOfStoplossVariantEffects() {
        return numberOfStoplossVariantEffects;
    }

    public void setNumberOfStoplossVariantEffects(Integer numberOfStoplossVariantEffects) {
        this.numberOfStoplossVariantEffects = numberOfStoplossVariantEffects;
    }

    public Integer getNumberOfSpliceVariantEffects() {
        return numberOfSpliceVariantEffects;
    }

    public void setNumberOfSpliceVariantEffects(Integer numberOfSpliceVariantEffects) {
        this.numberOfSpliceVariantEffects = numberOfSpliceVariantEffects;
    }

    public Integer getNumberOfOtherVariantEffects() {
        return numberOfOtherVariantEffects;
    }

    public void setNumberOfOtherVariantEffects(Integer numberOfOtherVariantEffects) {
        this.numberOfOtherVariantEffects = numberOfOtherVariantEffects;
    }

    public Integer getNumberOfAnalyzedVariants() {
        return numberOfAnalyzedVariants;
    }

    public void setNumberOfAnalyzedVariants(Integer numberOfAnalyzedVariants) {
        this.numberOfAnalyzedVariants = numberOfAnalyzedVariants;
    }

    public Integer getNumberOfHGMDKnownPathenogenic() {
        return numberOfHGMDKnownPathenogenic;
    }

    public void setNumberOfHGMDKnownPathenogenic(Integer numberOfHGMDKnownPathenogenic) {
        this.numberOfHGMDKnownPathenogenic = numberOfHGMDKnownPathenogenic;
    }

    public Integer getNumberOfHGMDLikelyPathenogenic() {
        return numberOfHGMDLikelyPathenogenic;
    }

    public void setNumberOfHGMDLikelyPathenogenic(Integer numberOfHGMDLikelyPathenogenic) {
        this.numberOfHGMDLikelyPathenogenic = numberOfHGMDLikelyPathenogenic;
    }

    public Integer getNumberOfHGMDPossiblyPathenogenic() {
        return numberOfHGMDPossiblyPathenogenic;
    }

    public void setNumberOfHGMDPossiblyPathenogenic(Integer numberOfHGMDPossiblyPathenogenic) {
        this.numberOfHGMDPossiblyPathenogenic = numberOfHGMDPossiblyPathenogenic;
    }

    public Integer getNumberOfHGMDVariantsOfUncertainSignificance() {
        return numberOfHGMDVariantsOfUncertainSignificance;
    }

    public void setNumberOfHGMDVariantsOfUncertainSignificance(Integer numberOfHGMDVariantsOfUncertainSignificance) {
        this.numberOfHGMDVariantsOfUncertainSignificance = numberOfHGMDVariantsOfUncertainSignificance;
    }

    public Integer getNumberOfHGMDLikelyBenign() {
        return numberOfHGMDLikelyBenign;
    }

    public void setNumberOfHGMDLikelyBenign(Integer numberOfHGMDLikelyBenign) {
        this.numberOfHGMDLikelyBenign = numberOfHGMDLikelyBenign;
    }

    public Integer getNumberOfHGMDAlmostCertainlyBenign() {
        return numberOfHGMDAlmostCertainlyBenign;
    }

    public void setNumberOfHGMDAlmostCertainlyBenign(Integer numberOfHGMDAlmostCertainlyBenign) {
        this.numberOfHGMDAlmostCertainlyBenign = numberOfHGMDAlmostCertainlyBenign;
    }

    public Integer getNumberOfClinVarKnownPathenogenic() {
        return numberOfClinVarKnownPathenogenic;
    }

    public void setNumberOfClinVarKnownPathenogenic(Integer numberOfClinVarKnownPathenogenic) {
        this.numberOfClinVarKnownPathenogenic = numberOfClinVarKnownPathenogenic;
    }

    public Integer getNumberOfClinVarLikelyPathenogenic() {
        return numberOfClinVarLikelyPathenogenic;
    }

    public void setNumberOfClinVarLikelyPathenogenic(Integer numberOfClinVarLikelyPathenogenic) {
        this.numberOfClinVarLikelyPathenogenic = numberOfClinVarLikelyPathenogenic;
    }

    public Integer getNumberOfClinVarPossiblyPathenogenic() {
        return numberOfClinVarPossiblyPathenogenic;
    }

    public void setNumberOfClinVarPossiblyPathenogenic(Integer numberOfClinVarPossiblyPathenogenic) {
        this.numberOfClinVarPossiblyPathenogenic = numberOfClinVarPossiblyPathenogenic;
    }

    public Integer getNumberOfClinVarVariantsOfUncertainSignificance() {
        return numberOfClinVarVariantsOfUncertainSignificance;
    }

    public void setNumberOfClinVarVariantsOfUncertainSignificance(Integer numberOfClinVarVariantsOfUncertainSignificance) {
        this.numberOfClinVarVariantsOfUncertainSignificance = numberOfClinVarVariantsOfUncertainSignificance;
    }

    public Integer getNumberOfClinVarLikelyBenign() {
        return numberOfClinVarLikelyBenign;
    }

    public void setNumberOfClinVarLikelyBenign(Integer numberOfClinVarLikelyBenign) {
        this.numberOfClinVarLikelyBenign = numberOfClinVarLikelyBenign;
    }

    public Integer getNumberOfClinVarAlmostCertainlyBenign() {
        return numberOfClinVarAlmostCertainlyBenign;
    }

    public void setNumberOfClinVarAlmostCertainlyBenign(Integer numberOfClinVarAlmostCertainlyBenign) {
        this.numberOfClinVarAlmostCertainlyBenign = numberOfClinVarAlmostCertainlyBenign;
    }

    @Override
    public String toString() {
        return String.format(
                "Report [key=%s, totalVariants=%s, numberOfCodingLocatedVariants=%s, numberOfNonCodingLocatedVariants=%s, numberOfTransriptDepLocatedVariants=%s, numberOfSubstitutionTypes=%s, numberOfIndelTypes=%s, numberOfIntergenicVariantEffects=%s, numberOfIntronicVariantEffects=%s, numberOfUntranslatedVariantEffects=%s, numberOfSynonymousVariantEffects=%s, numberOfMissenseVariantEffects=%s, numberOfNonShiftIndelVariantEffects=%s, numberOfShiftIndelVariantEffects=%s, numberOfNonsenseVariantEffects=%s, numberOfStoplossVariantEffects=%s, numberOfSpliceVariantEffects=%s, numberOfOtherVariantEffects=%s, numberOfAnalyzedVariants=%s, numberOfHGMDKnownPathenogenic=%s, numberOfHGMDLikelyPathenogenic=%s, numberOfHGMDPossiblyPathenogenic=%s, numberOfHGMDVariantsOfUncertainSignificance=%s, numberOfHGMDLikelyBenign=%s, numberOfHGMDAlmostCertainlyBenign=%s, numberOfClinVarKnownPathenogenic=%s, numberOfClinVarLikelyPathenogenic=%s, numberOfClinVarPossiblyPathenogenic=%s, numberOfClinVarVariantsOfUncertainSignificance=%s, numberOfClinVarLikelyBenign=%s, numberOfClinVarAlmostCertainlyBenign=%s]",
                key, totalVariants, numberOfCodingLocatedVariants, numberOfNonCodingLocatedVariants, numberOfTransriptDepLocatedVariants,
                numberOfSubstitutionTypes, numberOfIndelTypes, numberOfIntergenicVariantEffects, numberOfIntronicVariantEffects,
                numberOfUntranslatedVariantEffects, numberOfSynonymousVariantEffects, numberOfMissenseVariantEffects,
                numberOfNonShiftIndelVariantEffects, numberOfShiftIndelVariantEffects, numberOfNonsenseVariantEffects,
                numberOfStoplossVariantEffects, numberOfSpliceVariantEffects, numberOfOtherVariantEffects, numberOfAnalyzedVariants,
                numberOfHGMDKnownPathenogenic, numberOfHGMDLikelyPathenogenic, numberOfHGMDPossiblyPathenogenic,
                numberOfHGMDVariantsOfUncertainSignificance, numberOfHGMDLikelyBenign, numberOfHGMDAlmostCertainlyBenign,
                numberOfClinVarKnownPathenogenic, numberOfClinVarLikelyPathenogenic, numberOfClinVarPossiblyPathenogenic,
                numberOfClinVarVariantsOfUncertainSignificance, numberOfClinVarLikelyBenign, numberOfClinVarAlmostCertainlyBenign);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((numberOfAnalyzedVariants == null) ? 0 : numberOfAnalyzedVariants.hashCode());
        result = prime * result + ((numberOfClinVarAlmostCertainlyBenign == null) ? 0 : numberOfClinVarAlmostCertainlyBenign.hashCode());
        result = prime * result + ((numberOfClinVarKnownPathenogenic == null) ? 0 : numberOfClinVarKnownPathenogenic.hashCode());
        result = prime * result + ((numberOfClinVarLikelyBenign == null) ? 0 : numberOfClinVarLikelyBenign.hashCode());
        result = prime * result + ((numberOfClinVarLikelyPathenogenic == null) ? 0 : numberOfClinVarLikelyPathenogenic.hashCode());
        result = prime * result + ((numberOfClinVarPossiblyPathenogenic == null) ? 0 : numberOfClinVarPossiblyPathenogenic.hashCode());
        result = prime * result + ((numberOfClinVarVariantsOfUncertainSignificance == null) ? 0
                : numberOfClinVarVariantsOfUncertainSignificance.hashCode());
        result = prime * result + ((numberOfCodingLocatedVariants == null) ? 0 : numberOfCodingLocatedVariants.hashCode());
        result = prime * result + ((numberOfHGMDAlmostCertainlyBenign == null) ? 0 : numberOfHGMDAlmostCertainlyBenign.hashCode());
        result = prime * result + ((numberOfHGMDKnownPathenogenic == null) ? 0 : numberOfHGMDKnownPathenogenic.hashCode());
        result = prime * result + ((numberOfHGMDLikelyBenign == null) ? 0 : numberOfHGMDLikelyBenign.hashCode());
        result = prime * result + ((numberOfHGMDLikelyPathenogenic == null) ? 0 : numberOfHGMDLikelyPathenogenic.hashCode());
        result = prime * result + ((numberOfHGMDPossiblyPathenogenic == null) ? 0 : numberOfHGMDPossiblyPathenogenic.hashCode());
        result = prime * result
                + ((numberOfHGMDVariantsOfUncertainSignificance == null) ? 0 : numberOfHGMDVariantsOfUncertainSignificance.hashCode());
        result = prime * result + ((numberOfIndelTypes == null) ? 0 : numberOfIndelTypes.hashCode());
        result = prime * result + ((numberOfIntergenicVariantEffects == null) ? 0 : numberOfIntergenicVariantEffects.hashCode());
        result = prime * result + ((numberOfIntronicVariantEffects == null) ? 0 : numberOfIntronicVariantEffects.hashCode());
        result = prime * result + ((numberOfMissenseVariantEffects == null) ? 0 : numberOfMissenseVariantEffects.hashCode());
        result = prime * result + ((numberOfNonCodingLocatedVariants == null) ? 0 : numberOfNonCodingLocatedVariants.hashCode());
        result = prime * result + ((numberOfNonShiftIndelVariantEffects == null) ? 0 : numberOfNonShiftIndelVariantEffects.hashCode());
        result = prime * result + ((numberOfNonsenseVariantEffects == null) ? 0 : numberOfNonsenseVariantEffects.hashCode());
        result = prime * result + ((numberOfOtherVariantEffects == null) ? 0 : numberOfOtherVariantEffects.hashCode());
        result = prime * result + ((numberOfShiftIndelVariantEffects == null) ? 0 : numberOfShiftIndelVariantEffects.hashCode());
        result = prime * result + ((numberOfSpliceVariantEffects == null) ? 0 : numberOfSpliceVariantEffects.hashCode());
        result = prime * result + ((numberOfStoplossVariantEffects == null) ? 0 : numberOfStoplossVariantEffects.hashCode());
        result = prime * result + ((numberOfSubstitutionTypes == null) ? 0 : numberOfSubstitutionTypes.hashCode());
        result = prime * result + ((numberOfSynonymousVariantEffects == null) ? 0 : numberOfSynonymousVariantEffects.hashCode());
        result = prime * result + ((numberOfTransriptDepLocatedVariants == null) ? 0 : numberOfTransriptDepLocatedVariants.hashCode());
        result = prime * result + ((numberOfUntranslatedVariantEffects == null) ? 0 : numberOfUntranslatedVariantEffects.hashCode());
        result = prime * result + ((totalVariants == null) ? 0 : totalVariants.hashCode());
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
        Report other = (Report) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (numberOfAnalyzedVariants == null) {
            if (other.numberOfAnalyzedVariants != null)
                return false;
        } else if (!numberOfAnalyzedVariants.equals(other.numberOfAnalyzedVariants))
            return false;
        if (numberOfClinVarAlmostCertainlyBenign == null) {
            if (other.numberOfClinVarAlmostCertainlyBenign != null)
                return false;
        } else if (!numberOfClinVarAlmostCertainlyBenign.equals(other.numberOfClinVarAlmostCertainlyBenign))
            return false;
        if (numberOfClinVarKnownPathenogenic == null) {
            if (other.numberOfClinVarKnownPathenogenic != null)
                return false;
        } else if (!numberOfClinVarKnownPathenogenic.equals(other.numberOfClinVarKnownPathenogenic))
            return false;
        if (numberOfClinVarLikelyBenign == null) {
            if (other.numberOfClinVarLikelyBenign != null)
                return false;
        } else if (!numberOfClinVarLikelyBenign.equals(other.numberOfClinVarLikelyBenign))
            return false;
        if (numberOfClinVarLikelyPathenogenic == null) {
            if (other.numberOfClinVarLikelyPathenogenic != null)
                return false;
        } else if (!numberOfClinVarLikelyPathenogenic.equals(other.numberOfClinVarLikelyPathenogenic))
            return false;
        if (numberOfClinVarPossiblyPathenogenic == null) {
            if (other.numberOfClinVarPossiblyPathenogenic != null)
                return false;
        } else if (!numberOfClinVarPossiblyPathenogenic.equals(other.numberOfClinVarPossiblyPathenogenic))
            return false;
        if (numberOfClinVarVariantsOfUncertainSignificance == null) {
            if (other.numberOfClinVarVariantsOfUncertainSignificance != null)
                return false;
        } else if (!numberOfClinVarVariantsOfUncertainSignificance.equals(other.numberOfClinVarVariantsOfUncertainSignificance))
            return false;
        if (numberOfCodingLocatedVariants == null) {
            if (other.numberOfCodingLocatedVariants != null)
                return false;
        } else if (!numberOfCodingLocatedVariants.equals(other.numberOfCodingLocatedVariants))
            return false;
        if (numberOfHGMDAlmostCertainlyBenign == null) {
            if (other.numberOfHGMDAlmostCertainlyBenign != null)
                return false;
        } else if (!numberOfHGMDAlmostCertainlyBenign.equals(other.numberOfHGMDAlmostCertainlyBenign))
            return false;
        if (numberOfHGMDKnownPathenogenic == null) {
            if (other.numberOfHGMDKnownPathenogenic != null)
                return false;
        } else if (!numberOfHGMDKnownPathenogenic.equals(other.numberOfHGMDKnownPathenogenic))
            return false;
        if (numberOfHGMDLikelyBenign == null) {
            if (other.numberOfHGMDLikelyBenign != null)
                return false;
        } else if (!numberOfHGMDLikelyBenign.equals(other.numberOfHGMDLikelyBenign))
            return false;
        if (numberOfHGMDLikelyPathenogenic == null) {
            if (other.numberOfHGMDLikelyPathenogenic != null)
                return false;
        } else if (!numberOfHGMDLikelyPathenogenic.equals(other.numberOfHGMDLikelyPathenogenic))
            return false;
        if (numberOfHGMDPossiblyPathenogenic == null) {
            if (other.numberOfHGMDPossiblyPathenogenic != null)
                return false;
        } else if (!numberOfHGMDPossiblyPathenogenic.equals(other.numberOfHGMDPossiblyPathenogenic))
            return false;
        if (numberOfHGMDVariantsOfUncertainSignificance == null) {
            if (other.numberOfHGMDVariantsOfUncertainSignificance != null)
                return false;
        } else if (!numberOfHGMDVariantsOfUncertainSignificance.equals(other.numberOfHGMDVariantsOfUncertainSignificance))
            return false;
        if (numberOfIndelTypes == null) {
            if (other.numberOfIndelTypes != null)
                return false;
        } else if (!numberOfIndelTypes.equals(other.numberOfIndelTypes))
            return false;
        if (numberOfIntergenicVariantEffects == null) {
            if (other.numberOfIntergenicVariantEffects != null)
                return false;
        } else if (!numberOfIntergenicVariantEffects.equals(other.numberOfIntergenicVariantEffects))
            return false;
        if (numberOfIntronicVariantEffects == null) {
            if (other.numberOfIntronicVariantEffects != null)
                return false;
        } else if (!numberOfIntronicVariantEffects.equals(other.numberOfIntronicVariantEffects))
            return false;
        if (numberOfMissenseVariantEffects == null) {
            if (other.numberOfMissenseVariantEffects != null)
                return false;
        } else if (!numberOfMissenseVariantEffects.equals(other.numberOfMissenseVariantEffects))
            return false;
        if (numberOfNonCodingLocatedVariants == null) {
            if (other.numberOfNonCodingLocatedVariants != null)
                return false;
        } else if (!numberOfNonCodingLocatedVariants.equals(other.numberOfNonCodingLocatedVariants))
            return false;
        if (numberOfNonShiftIndelVariantEffects == null) {
            if (other.numberOfNonShiftIndelVariantEffects != null)
                return false;
        } else if (!numberOfNonShiftIndelVariantEffects.equals(other.numberOfNonShiftIndelVariantEffects))
            return false;
        if (numberOfNonsenseVariantEffects == null) {
            if (other.numberOfNonsenseVariantEffects != null)
                return false;
        } else if (!numberOfNonsenseVariantEffects.equals(other.numberOfNonsenseVariantEffects))
            return false;
        if (numberOfOtherVariantEffects == null) {
            if (other.numberOfOtherVariantEffects != null)
                return false;
        } else if (!numberOfOtherVariantEffects.equals(other.numberOfOtherVariantEffects))
            return false;
        if (numberOfShiftIndelVariantEffects == null) {
            if (other.numberOfShiftIndelVariantEffects != null)
                return false;
        } else if (!numberOfShiftIndelVariantEffects.equals(other.numberOfShiftIndelVariantEffects))
            return false;
        if (numberOfSpliceVariantEffects == null) {
            if (other.numberOfSpliceVariantEffects != null)
                return false;
        } else if (!numberOfSpliceVariantEffects.equals(other.numberOfSpliceVariantEffects))
            return false;
        if (numberOfStoplossVariantEffects == null) {
            if (other.numberOfStoplossVariantEffects != null)
                return false;
        } else if (!numberOfStoplossVariantEffects.equals(other.numberOfStoplossVariantEffects))
            return false;
        if (numberOfSubstitutionTypes == null) {
            if (other.numberOfSubstitutionTypes != null)
                return false;
        } else if (!numberOfSubstitutionTypes.equals(other.numberOfSubstitutionTypes))
            return false;
        if (numberOfSynonymousVariantEffects == null) {
            if (other.numberOfSynonymousVariantEffects != null)
                return false;
        } else if (!numberOfSynonymousVariantEffects.equals(other.numberOfSynonymousVariantEffects))
            return false;
        if (numberOfTransriptDepLocatedVariants == null) {
            if (other.numberOfTransriptDepLocatedVariants != null)
                return false;
        } else if (!numberOfTransriptDepLocatedVariants.equals(other.numberOfTransriptDepLocatedVariants))
            return false;
        if (numberOfUntranslatedVariantEffects == null) {
            if (other.numberOfUntranslatedVariantEffects != null)
                return false;
        } else if (!numberOfUntranslatedVariantEffects.equals(other.numberOfUntranslatedVariantEffects))
            return false;
        if (totalVariants == null) {
            if (other.totalVariants != null)
                return false;
        } else if (!totalVariants.equals(other.totalVariants))
            return false;
        return true;
    }

}
