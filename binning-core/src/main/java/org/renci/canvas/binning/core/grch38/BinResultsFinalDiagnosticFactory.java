package org.renci.canvas.binning.core.grch38;

import org.renci.canvas.dao.CANVASDAOException;
import org.renci.canvas.dao.clinbin.model.BinResultsFinalDiagnostic;
import org.renci.canvas.dao.clinbin.model.BinResultsFinalDiagnosticPK;
import org.renci.canvas.dao.clinbin.model.DX;
import org.renci.canvas.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.canvas.dao.clinbin.model.DiagnosticGene;
import org.renci.canvas.dao.clinbin.model.DiagnosticResultVersion;
import org.renci.canvas.dao.clinbin.model.DiseaseClass;
import org.renci.canvas.dao.clinbin.model.MaxFrequency;
import org.renci.canvas.dao.clinbin.model.NCGenesFrequencies;
import org.renci.canvas.dao.clinbin.model.UnimportantExon;
import org.renci.canvas.dao.clinvar.model.ReferenceClinicalAssertion;
import org.renci.canvas.dao.dbsnp.model.SNPMappingAgg;
import org.renci.canvas.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.canvas.dao.refseq.model.Variants_80_4;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariant;
import org.renci.canvas.dao.var.model.AssemblyLocatedVariantQC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinResultsFinalDiagnosticFactory {

    private static final Logger logger = LoggerFactory.getLogger(BinResultsFinalDiagnosticFactory.class);

    public static BinResultsFinalDiagnostic createBinResultsFinalDiagnostic(DiagnosticBinningJob diagnosticBinningJob,
                                                                            Variants_80_4 variant, DiagnosticGene diagnosticGene,
                                                                            MaxFrequency maxFrequency, SNPMappingAgg snpMappingAgg,
                                                                            NCGenesFrequencies ncgenesFrequencies, AssemblyLocatedVariant assemblyLocatedVariant,
                                                                            AssemblyLocatedVariantQC assemblyLocatedVariantQC, UnimportantExon unimportantExon) {
        BinResultsFinalDiagnostic binResultsFinalDiagnostic = new BinResultsFinalDiagnostic();
        try {

            DiagnosticResultVersion diagnosticResultVersion = diagnosticBinningJob.getDiagnosticResultVersion();
            DX dx = diagnosticBinningJob.getDx();

            BinResultsFinalDiagnosticPK binResultsFinalDiagnosticPK = new BinResultsFinalDiagnosticPK(diagnosticBinningJob.getParticipant(),
                    diagnosticResultVersion.getId(), dx.getId(), diagnosticBinningJob.getAssembly().getId(),
                    variant.getLocatedVariant().getId(), variant.getId().getMapNumber(), variant.getId().getTranscript());

            binResultsFinalDiagnostic = new BinResultsFinalDiagnostic(binResultsFinalDiagnosticPK);
            binResultsFinalDiagnostic.setLocatedVariant(variant.getLocatedVariant());
            binResultsFinalDiagnostic.setAssembly(diagnosticBinningJob.getAssembly());
            binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
            binResultsFinalDiagnostic.setDiagnosticResultVersion(diagnosticResultVersion);
            binResultsFinalDiagnostic.setDx(dx);
            binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());

            // variant stuff
            binResultsFinalDiagnostic.setChromosome(variant.getId().getGenomeRefSeq());
            binResultsFinalDiagnostic.setAlternateAllele(variant.getAlternateAllele());
            binResultsFinalDiagnostic.setAminoAcidStart(variant.getAminoAcidStart());
            binResultsFinalDiagnostic.setAminoAcidEnd(variant.getAminoAcidEnd());
            binResultsFinalDiagnostic.setCodingSequencePosition(variant.getCodingSequencePosition());
            binResultsFinalDiagnostic.setFinalAminoAcid(variant.getFinalAminoAcid());
            binResultsFinalDiagnostic.setFrameshift(variant.getFrameshift());
            binResultsFinalDiagnostic.setGeneId(variant.getGene().getId());
            binResultsFinalDiagnostic.setHgncGene(variant.getHgncGene());
            binResultsFinalDiagnostic.setHgvsCodingSequence(variant.getHgvsCodingSequence());
            binResultsFinalDiagnostic.setHgvsGenomic(variant.getHgvsGenomic());
            binResultsFinalDiagnostic.setHgvsProtein(variant.getHgvsProtein());
            binResultsFinalDiagnostic.setHgvsTranscript(variant.getHgvsTranscript());
            binResultsFinalDiagnostic.setInframe(variant.getInframe());
            binResultsFinalDiagnostic.setIntronExonDistance(variant.getIntronExonDistance());
            binResultsFinalDiagnostic.setLocationType(variant.getLocationType());
            binResultsFinalDiagnostic.setNummaps(variant.getNumberOfTranscriptMaps());
            binResultsFinalDiagnostic.setOriginalAminoAcid(variant.getOriginalAminoAcid());
            binResultsFinalDiagnostic.setStrand(variant.getStrand());
            binResultsFinalDiagnostic.setPosition(variant.getId().getPosition());
            binResultsFinalDiagnostic.setReferenceAllele(variant.getReferenceAllele());
            binResultsFinalDiagnostic.setRefseqGene(variant.getRefSeqGene());
            binResultsFinalDiagnostic.setTranscriptPosition(variant.getTranscriptPosition());
            binResultsFinalDiagnostic.setType(variant.getVariantType().getId());
            binResultsFinalDiagnostic.setVariantEffect(variant.getVariantEffect());

            if (assemblyLocatedVariant != null) {
                binResultsFinalDiagnostic.setHomozygous(assemblyLocatedVariant.getHomozygous());
                binResultsFinalDiagnostic.setGenotypeQual(assemblyLocatedVariant.getGenotypeQuality());
            }

            if (assemblyLocatedVariantQC != null) {
                binResultsFinalDiagnostic.setAltDepth(assemblyLocatedVariantQC.getAltDepth());
                binResultsFinalDiagnostic.setRefDepth(assemblyLocatedVariantQC.getRefDepth());
                binResultsFinalDiagnostic.setDepth(assemblyLocatedVariantQC.getDepth());
                binResultsFinalDiagnostic.setFracReadsWithDels(assemblyLocatedVariantQC.getFracReadsWithDels());
                binResultsFinalDiagnostic.setHrun(assemblyLocatedVariantQC.getHomopolymerRun());
                binResultsFinalDiagnostic.setGenotypeQual(assemblyLocatedVariantQC.getQualityByDepth());
                binResultsFinalDiagnostic.setStrandScore(assemblyLocatedVariantQC.getStrandScore());
                binResultsFinalDiagnostic.setReadPosRankSum(assemblyLocatedVariantQC.getReadPosRankSum());
                binResultsFinalDiagnostic.setQd(assemblyLocatedVariantQC.getQualityByDepth());
            }

            if (maxFrequency != null) {
                binResultsFinalDiagnostic.setMaxAlleleFrequency(maxFrequency.getMaxAlleleFreq());
            }

            if (diagnosticGene != null) {
                binResultsFinalDiagnostic.setTier(diagnosticGene.getTier());
                binResultsFinalDiagnostic.setInheritance(diagnosticGene.getInheritance());
            }

            if (snpMappingAgg != null) {
                binResultsFinalDiagnostic.setRsId(snpMappingAgg.getRsIds());
            }

            binResultsFinalDiagnostic
                    .setNCGenesAlternateFrequency((ncgenesFrequencies != null && ncgenesFrequencies.getAltAlleleFrequency() != null)
                            ? ncgenesFrequencies.getAltAlleleFrequency() : 0D);

            binResultsFinalDiagnostic.setNCGenesHWEP(
                    (ncgenesFrequencies != null && ncgenesFrequencies.getHweP() != null) ? ncgenesFrequencies.getHweP() : 1D);

            binResultsFinalDiagnostic.setExonTruncationCount(
                    (unimportantExon != null && unimportantExon.getCount() != null) ? unimportantExon.getCount() : 0);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return binResultsFinalDiagnostic;
    }

    // ClinVar
    public static BinResultsFinalDiagnostic createBinResultsFinalDiagnostic(DiagnosticBinningJob diagnosticBinningJob,
                                                                            Variants_80_4 variant, DiseaseClass clinvarDiseaseClass, DiagnosticGene diagnosticGene, MaxFrequency maxFrequency,
                                                                            ReferenceClinicalAssertion rca, SNPMappingAgg snpMappingAgg,
                                                                            NCGenesFrequencies ncgenesFrequencies, AssemblyLocatedVariant assemblyLocatedVariant,
                                                                            AssemblyLocatedVariantQC assemblyLocatedVariantQC, UnimportantExon unimportantExon) throws CANVASDAOException {
        logger.debug(
                "ENTERING createBinResultsFinalDiagnostic(DiagnosticBinningJob, Variants_80_4, DiseaseClass, DiagnosticGene, MaxFrequency, ReferenceClinicalAssertion, Integer, SNPMappingAgg)");

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant, diagnosticGene, maxFrequency, snpMappingAgg, ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

        if (rca != null) {
            binResultsFinalDiagnostic.setClinvarAccession(rca.getAccession());
            binResultsFinalDiagnostic.setClinvarAssertion(rca.getAssertion());
        }

        binResultsFinalDiagnostic.setClinvarDiseaseClass(clinvarDiseaseClass);

        return binResultsFinalDiagnostic;
    }


    // hgmd
    public static BinResultsFinalDiagnostic createBinResultsFinalDiagnostic(DiagnosticBinningJob diagnosticBinningJob,
                                                                            Variants_80_4 variant, DiseaseClass hgmdDiseaseClass, DiagnosticGene diagnosticGene, MaxFrequency maxFrequency,
                                                                            HGMDLocatedVariant hgmdLocatedVariant, SNPMappingAgg snpMappingAgg,
                                                                            NCGenesFrequencies ncgenesFrequencies, AssemblyLocatedVariant assemblyLocatedVariant,
                                                                            AssemblyLocatedVariantQC assemblyLocatedVariantQC, UnimportantExon unimportantExon) throws CANVASDAOException {
        logger.debug(
                "ENTERING createBinResultsFinalDiagnostic(DiagnosticBinningJob, Variants_80_4, DiseaseClass, DiagnosticGene, MaxFrequency, HGMDLocatedVariant, Integer, SNPMappingAgg)");

        BinResultsFinalDiagnostic binResultsFinalDiagnostic = createBinResultsFinalDiagnostic(diagnosticBinningJob, variant, diagnosticGene, maxFrequency, snpMappingAgg, ncgenesFrequencies, assemblyLocatedVariant, assemblyLocatedVariantQC, unimportantExon);

        if (hgmdLocatedVariant != null) {
            binResultsFinalDiagnostic.setHgmdAccessionNumber(hgmdLocatedVariant.getId().getAccession());
            binResultsFinalDiagnostic.setHgmdTag(hgmdLocatedVariant.getTag());
        }

        binResultsFinalDiagnostic.setHgmdDiseaseClass(hgmdDiseaseClass);

        return binResultsFinalDiagnostic;
    }

}
