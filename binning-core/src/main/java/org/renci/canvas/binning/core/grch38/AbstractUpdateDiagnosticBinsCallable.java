package org.renci.canvas.binning.core.grch38;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.renci.canvas.dao.CANVASDAOBeanService;
import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.DiagnosticGene;
import org.renci.canvas.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.canvas.dao.clinbin.model.DiseaseClass;
import org.renci.canvas.dao.clinbin.model.MaxFrequency;
import org.renci.canvas.dao.clinbin.model.NCGenesFrequencies;
import org.renci.canvas.dao.clinbin.model.NCGenesFrequenciesPK;
import org.renci.canvas.dao.clinbin.model.UnimportantExon;
import org.renci.canvas.dao.clinbin.model.UnimportantExonPK;
import org.renci.canvas.dao.clinvar.model.ReferenceClinicalAssertion;
import org.renci.canvas.dao.clinvar.model.SubmissionClinicalAssertion;
import org.renci.canvas.dao.dbsnp.model.SNPMappingAgg;
import org.renci.canvas.dao.dbsnp.model.SNPMappingAggPK;
import org.renci.canvas.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariant;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantPK;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQC;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQCPK;
import org.renci.canvas.dao.var.model.LocatedVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUpdateDiagnosticBinsCallable implements Callable<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUpdateDiagnosticBinsCallable.class);

    private final List<Integer> knownPathogenicClinVarAssertionRankings = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16);

    private final List<String> clinVarAssertionStatusExcludes = Arrays.asList("no assertion criteria provided", "no assertion provided",
            "not classified by submitter");

    private final List<String> likelyTruncatingVariantEffects = Arrays.asList("nonsense", "splice-site", "boundary-crossing indel",
            "stoploss", "nonsense indel", "frameshifting indel");

    private final List<String> misspellingVariantEffects = Arrays.asList("missense", "non-frameshifting indel");

    private final List<String> uncertainSignificanceAllowableLocationTypes = Arrays.asList("UTR-5", "UTR-3", "UTR", "exon",
            "intron/exon boundary");

    private final List<String> uncertainSignificanceAllowableVariantEffects = Arrays.asList("synonymous", "synonymous indel", "intron",
            "splice-site-UTR-3", "splice-site-UTR-5", "splice-site-UTR", "potential RNA-editing site", "noncoding boundary-crossing indel");

    private List<DiseaseClass> allDiseaseClasses;

    private Integer maxNCGenesFrequenciesVersion;

    protected CANVASDAOBeanService daoBean;

    protected DiagnosticBinningJob diagnosticBinningJob;

    public AbstractUpdateDiagnosticBinsCallable(CANVASDAOBeanService daoBean, DiagnosticBinningJob diagnosticBinningJob) {
        super();
        this.daoBean = daoBean;
        this.diagnosticBinningJob = diagnosticBinningJob;
        try {
            this.allDiseaseClasses = daoBean.getDiseaseClassDAO().findAll();
            this.maxNCGenesFrequenciesVersion = daoBean.getNCGenesFrequenciesDAO().findMaxVersion();
        } catch (CANVASDAOException e) {
            e.printStackTrace();
        }
    }

    private BinResultsFinalDiagnostic makeProvisionalBinResults(Variants_80_4 variant, LocatedVariant locatedVariant37,
            MaxFrequency maxFrequency, DiagnosticGene diagnosticGene) throws CANVASDAOException {
        SNPMappingAgg snpMappingAgg = daoBean.getSNPMappingAggDAO().findById(new SNPMappingAggPK(locatedVariant37.getId()));

        NCGenesFrequencies ncgenesFrequencies = daoBean.getNCGenesFrequenciesDAO()
                .findById(new NCGenesFrequenciesPK(locatedVariant37.getId(), maxNCGenesFrequenciesVersion.toString()));

        AssemblyLocatedVariant assemblyLocatedVariant = daoBean.getAssemblyLocatedVariantDAO()
                .findById(new AssemblyLocatedVariantPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        AssemblyLocatedVariantQC assemblyLocatedVariantQC = daoBean.getAssemblyLocatedVariantQCDAO()
                .findById(new AssemblyLocatedVariantQCPK(diagnosticBinningJob.getAssembly().getId(), variant.getLocatedVariant().getId()));

        UnimportantExon unimportantExon = daoBean.getUnimportantExonDAO()
                .findById(new UnimportantExonPK(variant.getId().getTranscript(), variant.getNonCanonicalExon()));

        return BinResultsFinalDiagnosticFactory.createBinResultsFinalDiagnostic(diagnosticBinningJob, variant, diagnosticGene, maxFrequency,
                snpMappingAgg, ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

    }

    private final DiseaseClass getDiseaseClass(int code) {
        return allDiseaseClasses.stream().filter(a -> a.getId().equals(code)).findAny().orElse(null);
    }

    private DiseaseClass calculateProvisionalClass(Variants_80_4 variant, MaxFrequency maxFrequency) throws CANVASDAOException {

        if (maxFrequency.getMaxAlleleFreq() < 0.01) {
            // class B
            if (likelyTruncatingVariantEffects.contains(variant.getVariantEffect().getId())) {
                return getDiseaseClass(2);
            }
            // class C
            if (misspellingVariantEffects.contains(variant.getVariantEffect().getId())) {
                return getDiseaseClass(3);
            }
            if (uncertainSignificanceAllowableVariantEffects.contains(variant.getVariantEffect().getId())
                    || uncertainSignificanceAllowableLocationTypes.contains(variant.getLocationType().getId())) {
                return getDiseaseClass(4);
            }
        } else if (maxFrequency.getMaxAlleleFreq() < 0.05) {
            if (uncertainSignificanceAllowableLocationTypes.contains(variant.getLocationType().getId())) {
                return getDiseaseClass(4);
            } else {
                return getDiseaseClass(5);
            }
        } else if (maxFrequency.getMaxAlleleFreq() < 0.1) {
            if (uncertainSignificanceAllowableLocationTypes.contains(variant.getLocationType().getId())) {
                return getDiseaseClass(5);
            }
        }

        return getDiseaseClass(6);
    }

    public BinResultsFinalDiagnostic binVariantClinVar(Variants_80_4 variant, LocatedVariant locatedVariant37, MaxFrequency maxFrequency,
            DiagnosticGene diagnosticGene) throws CANVASDAOException {

        DiseaseClass variantClass = calculateProvisionalClass(variant, maxFrequency);

        DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();

        ReferenceClinicalAssertion rca = null;

        List<ReferenceClinicalAssertion> foundReferenceClinicalAssersions = daoBean.getReferenceClinicalAssertionDAO()
                .findByLocatedVariantIdAndVersionAndAssertionStatusExclusionList(locatedVariant37.getId(),
                        diagnosticResultVersion.getClinvarVersion().getId(), clinVarAssertionStatusExcludes);

        if (CollectionUtils.isNotEmpty(foundReferenceClinicalAssersions)) {

            boolean containsValidAssertionRanking = foundReferenceClinicalAssersions.stream()
                    .anyMatch((s) -> knownPathogenicClinVarAssertionRankings.contains(s.getAssertion().getRank()));

            if (containsValidAssertionRanking) {
                foundReferenceClinicalAssersions.sort((a, b) -> a.getAssertion().getRank().compareTo(b.getAssertion().getRank()));
                rca = foundReferenceClinicalAssersions.get(0);
                logger.debug(rca.toString());
                if (StringUtils.isEmpty(rca.getExplanation()) || StringUtils.containsIgnoreCase(rca.getExplanation(), "pathogenic")) {
                    if (CollectionUtils.isNotEmpty(rca.getSubmissionClinicalAssertions())) {

                        Optional<SubmissionClinicalAssertion> optionalSubmissionClinicalAssertion = rca.getSubmissionClinicalAssertions()
                                .stream().filter(a -> StringUtils.containsIgnoreCase(a.getAssertion(), "pathogenic")).findAny();
                        if (optionalSubmissionClinicalAssertion.isPresent()) {
                            optionalSubmissionClinicalAssertion = rca.getSubmissionClinicalAssertions().stream()
                                    .filter(a -> StringUtils.containsIgnoreCase(a.getAssertion(), "pathogenic")
                                            && StringUtils.containsIgnoreCase(a.getReviewStatus(), "no assertion"))
                                    .findAny();
                            if (optionalSubmissionClinicalAssertion.isPresent()) {
                                // We found a match!
                                variantClass = getDiseaseClass(1);
                            }
                        }
                    }
                }
            }
        }

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = makeProvisionalBinResults(variant, locatedVariant37, maxFrequency,
                diagnosticGene);

        binResultsFinalDiagnostic.setClinvarDiseaseClass(variantClass);

        if (rca != null) {
            binResultsFinalDiagnostic.setClinvarAccession(rca.getAccession());
            binResultsFinalDiagnostic.setClinvarAssertion(rca.getAssertion());
        }

        return binResultsFinalDiagnostic;

    }

    public BinResultsFinalDiagnostic binVariantHGMD(Variants_80_4 variant, LocatedVariant locatedVariant37, MaxFrequency maxFrequency,
            DiagnosticGene diagnosticGene) throws CANVASDAOException {
        DiseaseClass variantClass = calculateProvisionalClass(variant, maxFrequency);

        HGMDLocatedVariant hgmdLocatedVariant = null;
        List<HGMDLocatedVariant> hgmdLocatedVariantList = daoBean.getHGMDLocatedVariantDAO()
                .findByLocatedVariantId(locatedVariant37.getId());

        if (CollectionUtils.isNotEmpty(hgmdLocatedVariantList)) {

            Optional<HGMDLocatedVariant> optionalHGMDLocatedVariant = hgmdLocatedVariantList.stream()
                    .filter((s) -> s.getId().getVersion().equals(2) && s.getTag().equals("DM")).findAny();

            if (optionalHGMDLocatedVariant.isPresent()) {
                // found a match!
                hgmdLocatedVariant = optionalHGMDLocatedVariant.get();
                variantClass = getDiseaseClass(1);
            }

        }

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = makeProvisionalBinResults(variant, locatedVariant37, maxFrequency,
                diagnosticGene);

        binResultsFinalDiagnostic.setHgmdDiseaseClass(variantClass);

        if (hgmdLocatedVariant != null) {
            binResultsFinalDiagnostic.setHgmdAccessionNumber(hgmdLocatedVariant.getId().getAccession());
            binResultsFinalDiagnostic.setHgmdTag(hgmdLocatedVariant.getTag());
        }

        return binResultsFinalDiagnostic;

    }

}
