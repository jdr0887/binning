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

    @Column(name = "n_analyzed_class_1")
    private Integer numberOfKnownPathenogenic;

    @Column(name = "n_analyzed_class_2")
    private Integer numberOfLikelyPathenogenic;

    @Column(name = "n_analyzed_class_3")
    private Integer numberOfPossiblyPathenogenic;

    @Column(name = "n_analyzed_class_4")
    private Integer numberOfVariantsOfUncertainSignificance;

    @Column(name = "n_analyzed_class_5")
    private Integer numberOfLikelyBenign;

    @Column(name = "n_analyzed_class_6")
    private Integer numberOfAlmostCertainlyBenign;

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

    public Integer getNumberOfKnownPathenogenic() {
        return numberOfKnownPathenogenic;
    }

    public void setNumberOfKnownPathenogenic(Integer numberOfKnownPathenogenic) {
        this.numberOfKnownPathenogenic = numberOfKnownPathenogenic;
    }

    public Integer getNumberOfLikelyPathenogenic() {
        return numberOfLikelyPathenogenic;
    }

    public void setNumberOfLikelyPathenogenic(Integer numberOfLikelyPathenogenic) {
        this.numberOfLikelyPathenogenic = numberOfLikelyPathenogenic;
    }

    public Integer getNumberOfPossiblyPathenogenic() {
        return numberOfPossiblyPathenogenic;
    }

    public void setNumberOfPossiblyPathenogenic(Integer numberOfPossiblyPathenogenic) {
        this.numberOfPossiblyPathenogenic = numberOfPossiblyPathenogenic;
    }

    public Integer getNumberOfVariantsOfUncertainSignificance() {
        return numberOfVariantsOfUncertainSignificance;
    }

    public void setNumberOfVariantsOfUncertainSignificance(Integer numberOfVariantsOfUncertainSignificance) {
        this.numberOfVariantsOfUncertainSignificance = numberOfVariantsOfUncertainSignificance;
    }

    public Integer getNumberOfLikelyBenign() {
        return numberOfLikelyBenign;
    }

    public void setNumberOfLikelyBenign(Integer numberOfLikelyBenign) {
        this.numberOfLikelyBenign = numberOfLikelyBenign;
    }

    public Integer getNumberOfAlmostCertainlyBenign() {
        return numberOfAlmostCertainlyBenign;
    }

    public void setNumberOfAlmostCertainlyBenign(Integer numberOfAlmostCertainlyBenign) {
        this.numberOfAlmostCertainlyBenign = numberOfAlmostCertainlyBenign;
    }

    @Override
    public String toString() {
        return String.format(
                "Report [key=%s, totalVariants=%s, numberOfCodingLocatedVariants=%s, numberOfNonCodingLocatedVariants=%s, numberOfTransriptDepLocatedVariants=%s, numberOfSubstitutionTypes=%s, numberOfIndelTypes=%s, numberOfIntergenicVariantEffects=%s, numberOfIntronicVariantEffects=%s, numberOfUntranslatedVariantEffects=%s, numberOfSynonymousVariantEffects=%s, numberOfMissenseVariantEffects=%s, numberOfNonShiftIndelVariantEffects=%s, numberOfShiftIndelVariantEffects=%s, numberOfNonsenseVariantEffects=%s, numberOfStoplossVariantEffects=%s, numberOfSpliceVariantEffects=%s, numberOfOtherVariantEffects=%s, numberOfAnalyzedVariants=%s, numberOfKnownPathenogenic=%s, numberOfLikelyPathenogenic=%s, numberOfPossiblyPathenogenic=%s, numberOfVariantsOfUncertainSignificance=%s, numberOfLikelyBenign=%s, numberOfAlmostCertainlyBenign=%s]",
                key, totalVariants, numberOfCodingLocatedVariants, numberOfNonCodingLocatedVariants, numberOfTransriptDepLocatedVariants,
                numberOfSubstitutionTypes, numberOfIndelTypes, numberOfIntergenicVariantEffects, numberOfIntronicVariantEffects,
                numberOfUntranslatedVariantEffects, numberOfSynonymousVariantEffects, numberOfMissenseVariantEffects,
                numberOfNonShiftIndelVariantEffects, numberOfShiftIndelVariantEffects, numberOfNonsenseVariantEffects,
                numberOfStoplossVariantEffects, numberOfSpliceVariantEffects, numberOfOtherVariantEffects, numberOfAnalyzedVariants,
                numberOfKnownPathenogenic, numberOfLikelyPathenogenic, numberOfPossiblyPathenogenic,
                numberOfVariantsOfUncertainSignificance, numberOfLikelyBenign, numberOfAlmostCertainlyBenign);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((numberOfAlmostCertainlyBenign == null) ? 0 : numberOfAlmostCertainlyBenign.hashCode());
        result = prime * result + ((numberOfAnalyzedVariants == null) ? 0 : numberOfAnalyzedVariants.hashCode());
        result = prime * result + ((numberOfCodingLocatedVariants == null) ? 0 : numberOfCodingLocatedVariants.hashCode());
        result = prime * result + ((numberOfIndelTypes == null) ? 0 : numberOfIndelTypes.hashCode());
        result = prime * result + ((numberOfIntergenicVariantEffects == null) ? 0 : numberOfIntergenicVariantEffects.hashCode());
        result = prime * result + ((numberOfIntronicVariantEffects == null) ? 0 : numberOfIntronicVariantEffects.hashCode());
        result = prime * result + ((numberOfKnownPathenogenic == null) ? 0 : numberOfKnownPathenogenic.hashCode());
        result = prime * result + ((numberOfLikelyBenign == null) ? 0 : numberOfLikelyBenign.hashCode());
        result = prime * result + ((numberOfLikelyPathenogenic == null) ? 0 : numberOfLikelyPathenogenic.hashCode());
        result = prime * result + ((numberOfMissenseVariantEffects == null) ? 0 : numberOfMissenseVariantEffects.hashCode());
        result = prime * result + ((numberOfNonCodingLocatedVariants == null) ? 0 : numberOfNonCodingLocatedVariants.hashCode());
        result = prime * result + ((numberOfNonShiftIndelVariantEffects == null) ? 0 : numberOfNonShiftIndelVariantEffects.hashCode());
        result = prime * result + ((numberOfNonsenseVariantEffects == null) ? 0 : numberOfNonsenseVariantEffects.hashCode());
        result = prime * result + ((numberOfOtherVariantEffects == null) ? 0 : numberOfOtherVariantEffects.hashCode());
        result = prime * result + ((numberOfPossiblyPathenogenic == null) ? 0 : numberOfPossiblyPathenogenic.hashCode());
        result = prime * result + ((numberOfShiftIndelVariantEffects == null) ? 0 : numberOfShiftIndelVariantEffects.hashCode());
        result = prime * result + ((numberOfSpliceVariantEffects == null) ? 0 : numberOfSpliceVariantEffects.hashCode());
        result = prime * result + ((numberOfStoplossVariantEffects == null) ? 0 : numberOfStoplossVariantEffects.hashCode());
        result = prime * result + ((numberOfSubstitutionTypes == null) ? 0 : numberOfSubstitutionTypes.hashCode());
        result = prime * result + ((numberOfSynonymousVariantEffects == null) ? 0 : numberOfSynonymousVariantEffects.hashCode());
        result = prime * result + ((numberOfTransriptDepLocatedVariants == null) ? 0 : numberOfTransriptDepLocatedVariants.hashCode());
        result = prime * result + ((numberOfUntranslatedVariantEffects == null) ? 0 : numberOfUntranslatedVariantEffects.hashCode());
        result = prime * result
                + ((numberOfVariantsOfUncertainSignificance == null) ? 0 : numberOfVariantsOfUncertainSignificance.hashCode());
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
        if (numberOfAlmostCertainlyBenign == null) {
            if (other.numberOfAlmostCertainlyBenign != null)
                return false;
        } else if (!numberOfAlmostCertainlyBenign.equals(other.numberOfAlmostCertainlyBenign))
            return false;
        if (numberOfAnalyzedVariants == null) {
            if (other.numberOfAnalyzedVariants != null)
                return false;
        } else if (!numberOfAnalyzedVariants.equals(other.numberOfAnalyzedVariants))
            return false;
        if (numberOfCodingLocatedVariants == null) {
            if (other.numberOfCodingLocatedVariants != null)
                return false;
        } else if (!numberOfCodingLocatedVariants.equals(other.numberOfCodingLocatedVariants))
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
        if (numberOfKnownPathenogenic == null) {
            if (other.numberOfKnownPathenogenic != null)
                return false;
        } else if (!numberOfKnownPathenogenic.equals(other.numberOfKnownPathenogenic))
            return false;
        if (numberOfLikelyBenign == null) {
            if (other.numberOfLikelyBenign != null)
                return false;
        } else if (!numberOfLikelyBenign.equals(other.numberOfLikelyBenign))
            return false;
        if (numberOfLikelyPathenogenic == null) {
            if (other.numberOfLikelyPathenogenic != null)
                return false;
        } else if (!numberOfLikelyPathenogenic.equals(other.numberOfLikelyPathenogenic))
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
        if (numberOfPossiblyPathenogenic == null) {
            if (other.numberOfPossiblyPathenogenic != null)
                return false;
        } else if (!numberOfPossiblyPathenogenic.equals(other.numberOfPossiblyPathenogenic))
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
        if (numberOfVariantsOfUncertainSignificance == null) {
            if (other.numberOfVariantsOfUncertainSignificance != null)
                return false;
        } else if (!numberOfVariantsOfUncertainSignificance.equals(other.numberOfVariantsOfUncertainSignificance))
            return false;
        if (totalVariants == null) {
            if (other.totalVariants != null)
                return false;
        } else if (!totalVariants.equals(other.totalVariants))
            return false;
        return true;
    }

}
