package org.renci.binning.dao;

import org.renci.binning.dao.annotation.AnnotationGeneDAO;
import org.renci.binning.dao.annotation.AnnotationGeneExternalIdsDAO;
import org.renci.binning.dao.clinbin.BinResultsFinalDiagnosticDAO;
import org.renci.binning.dao.clinbin.BinResultsFinalIncidentalXDAO;
import org.renci.binning.dao.clinbin.BinResultsFinalRiskXDAO;
import org.renci.binning.dao.clinbin.CarrierStatusDAO;
import org.renci.binning.dao.clinbin.DXCoverageDAO;
import org.renci.binning.dao.clinbin.DXDAO;
import org.renci.binning.dao.clinbin.DXExonsDAO;
import org.renci.binning.dao.clinbin.DiagnosticBinningJobDAO;
import org.renci.binning.dao.clinbin.DiagnosticGeneDAO;
import org.renci.binning.dao.clinbin.DiagnosticResultVersionDAO;
import org.renci.binning.dao.clinbin.DiagnosticStatusTypeDAO;
import org.renci.binning.dao.clinbin.DiseaseClassDAO;
import org.renci.binning.dao.clinbin.IncidentalBinGeneXDAO;
import org.renci.binning.dao.clinbin.IncidentalBinGroupVersionXDAO;
import org.renci.binning.dao.clinbin.IncidentalBinHaplotypeXDAO;
import org.renci.binning.dao.clinbin.IncidentalBinXDAO;
import org.renci.binning.dao.clinbin.IncidentalBinningJobDAO;
import org.renci.binning.dao.clinbin.IncidentalResultVersionXDAO;
import org.renci.binning.dao.clinbin.IncidentalStatusTypeDAO;
import org.renci.binning.dao.clinbin.MaxFrequencyDAO;
import org.renci.binning.dao.clinbin.MaxFrequencySourceDAO;
import org.renci.binning.dao.clinbin.MissingHaplotypeDAO;
import org.renci.binning.dao.clinbin.NCGenesFrequenciesDAO;
import org.renci.binning.dao.clinbin.PhenotypeXDAO;
import org.renci.binning.dao.clinbin.ReportDAO;
import org.renci.binning.dao.clinbin.UnimportantExonDAO;
import org.renci.binning.dao.clinvar.AssertionRankingDAO;
import org.renci.binning.dao.clinvar.ReferenceClinicalAssertionsDAO;
import org.renci.binning.dao.dbsnp.SNPAlleleDAO;
import org.renci.binning.dao.dbsnp.SNPAlleleFrequencyDAO;
import org.renci.binning.dao.dbsnp.SNPDAO;
import org.renci.binning.dao.dbsnp.SNPGenotypeFrequencyDAO;
import org.renci.binning.dao.dbsnp.SNPMappingAggDAO;
import org.renci.binning.dao.dbsnp.SNPMappingDAO;
import org.renci.binning.dao.esp.ESPSNPFrequencyPopulationDAO;
import org.renci.binning.dao.exac.MaxVariantFrequencyDAO;
import org.renci.binning.dao.exac.VariantFrequencyDAO;
import org.renci.binning.dao.genome1k.IndelMaxFrequencyDAO;
import org.renci.binning.dao.genome1k.OneThousandGenomeIndelFrequencyDAO;
import org.renci.binning.dao.genome1k.OneThousandGenomeSNPFrequencyPopulationDAO;
import org.renci.binning.dao.genome1k.OneThousandGenomeSNPFrequencySubpopulationDAO;
import org.renci.binning.dao.genome1k.SNPPopulationMaxFrequencyDAO;
import org.renci.binning.dao.hgmd.HGMDLocatedVariantDAO;
import org.renci.binning.dao.hgnc.HGNCGeneDAO;
import org.renci.binning.dao.ref.GenomeRefDAO;
import org.renci.binning.dao.ref.GenomeRefSeqDAO;
import org.renci.binning.dao.ref.GenomeRefSeqLocationDAO;
import org.renci.binning.dao.refseq.FeatureDAO;
import org.renci.binning.dao.refseq.LocationTypeDAO;
import org.renci.binning.dao.refseq.RefSeqCodingSequenceDAO;
import org.renci.binning.dao.refseq.RefSeqGeneDAO;
import org.renci.binning.dao.refseq.RegionGroupDAO;
import org.renci.binning.dao.refseq.RegionGroupRegionDAO;
import org.renci.binning.dao.refseq.TranscriptDAO;
import org.renci.binning.dao.refseq.TranscriptMapsDAO;
import org.renci.binning.dao.refseq.TranscriptMapsExonsDAO;
import org.renci.binning.dao.refseq.VariantEffectDAO;
import org.renci.binning.dao.refseq.Variants_48_2_DAO;
import org.renci.binning.dao.refseq.Variants_61_2_DAO;
import org.renci.binning.dao.refseq.Variants_78_4_DAO;
import org.renci.binning.dao.var.AssemblyDAO;
import org.renci.binning.dao.var.AssemblyLocatedVariantDAO;
import org.renci.binning.dao.var.AssemblyLocatedVariantQCDAO;
import org.renci.binning.dao.var.AssemblyLocationDAO;
import org.renci.binning.dao.var.CanonicalAlleleDAO;
import org.renci.binning.dao.var.LabDAO;
import org.renci.binning.dao.var.LibraryDAO;
import org.renci.binning.dao.var.LocatedVariantDAO;
import org.renci.binning.dao.var.ProjectDAO;
import org.renci.binning.dao.var.SampleDAO;
import org.renci.binning.dao.var.VariantSetDAO;
import org.renci.binning.dao.var.VariantSetLoadDAO;
import org.renci.binning.dao.var.VariantSetLocationDAO;
import org.renci.binning.dao.var.VariantTypeDAO;

public interface BinningDAOBeanService {

    public AnnotationGeneDAO getAnnotationGeneDAO();

    public void setAnnotationGeneDAO(AnnotationGeneDAO annotationGeneDAO);

    public AnnotationGeneExternalIdsDAO getAnnotationGeneExternalIdsDAO();

    public void setAnnotationGeneExternalIdsDAO(AnnotationGeneExternalIdsDAO annotationGeneExternalIdsDAO);

    public BinResultsFinalDiagnosticDAO getBinResultsFinalDiagnosticDAO();

    public void setBinResultsFinalDiagnosticDAO(BinResultsFinalDiagnosticDAO binResultsFinalDiagnosticDAO);

    public BinResultsFinalIncidentalXDAO getBinResultsFinalIncidentalXDAO();

    public void setBinResultsFinalIncidentalXDAO(BinResultsFinalIncidentalXDAO binResultsFinalIncidentalXDAO);

    public BinResultsFinalRiskXDAO getBinResultsFinalRiskXDAO();

    public void setBinResultsFinalRiskXDAO(BinResultsFinalRiskXDAO binResultsFinalRiskXDAO);

    public CarrierStatusDAO getCarrierStatusDAO();

    public void setCarrierStatusDAO(CarrierStatusDAO carrierStatusDAO);

    public DiagnosticBinningJobDAO getDiagnosticBinningJobDAO();

    public void setDiagnosticBinningJobDAO(DiagnosticBinningJobDAO diagnosticBinningJobDAO);

    public DiagnosticGeneDAO getDiagnosticGeneDAO();

    public void setDiagnosticGeneDAO(DiagnosticGeneDAO diagnosticGeneDAO);

    public DiagnosticResultVersionDAO getDiagnosticResultVersionDAO();

    public void setDiagnosticResultVersionDAO(DiagnosticResultVersionDAO diagnosticResultVersionDAO);

    public DiagnosticStatusTypeDAO getDiagnosticStatusTypeDAO();

    public void setDiagnosticStatusTypeDAO(DiagnosticStatusTypeDAO diagnosticStatusTypeDAO);

    public DiseaseClassDAO getDiseaseClassDAO();

    public void setDiseaseClassDAO(DiseaseClassDAO diseaseClassDAO);

    public DXCoverageDAO getDXCoverageDAO();

    public void setDXCoverageDAO(DXCoverageDAO dXCoverageDAO);

    public DXDAO getDXDAO();

    public void setDXDAO(DXDAO dXDAO);

    public DXExonsDAO getDXExonsDAO();

    public void setDXExonsDAO(DXExonsDAO dXExonsDAO);

    public IncidentalBinGeneXDAO getIncidentalBinGeneXDAO();

    public void setIncidentalBinGeneXDAO(IncidentalBinGeneXDAO incidentalBinGeneXDAO);

    public IncidentalBinGroupVersionXDAO getIncidentalBinGroupVersionXDAO();

    public void setIncidentalBinGroupVersionXDAO(IncidentalBinGroupVersionXDAO incidentalBinGroupVersionXDAO);

    public IncidentalBinHaplotypeXDAO getIncidentalBinHaplotypeXDAO();

    public void setIncidentalBinHaplotypeXDAO(IncidentalBinHaplotypeXDAO incidentalBinHaplotypeXDAO);

    public IncidentalBinningJobDAO getIncidentalBinningJobDAO();

    public void setIncidentalBinningJobDAO(IncidentalBinningJobDAO incidentalBinningJobDAO);

    public IncidentalBinXDAO getIncidentalBinXDAO();

    public void setIncidentalBinXDAO(IncidentalBinXDAO incidentalBinXDAO);

    public IncidentalResultVersionXDAO getIncidentalResultVersionXDAO();

    public void setIncidentalResultVersionXDAO(IncidentalResultVersionXDAO incidentalResultVersionXDAO);

    public IncidentalStatusTypeDAO getIncidentalStatusTypeDAO();

    public void setIncidentalStatusTypeDAO(IncidentalStatusTypeDAO incidentalStatusTypeDAO);

    public MaxFrequencyDAO getMaxFrequencyDAO();

    public void setMaxFrequencyDAO(MaxFrequencyDAO maxFrequencyDAO);

    public MaxFrequencySourceDAO getMaxFrequencySourceDAO();

    public void setMaxFrequencySourceDAO(MaxFrequencySourceDAO maxFrequencySourceDAO);

    public MissingHaplotypeDAO getMissingHaplotypeDAO();

    public void setMissingHaplotypeDAO(MissingHaplotypeDAO missingHaplotypeDAO);

    public NCGenesFrequenciesDAO getNCGenesFrequenciesDAO();

    public void setNCGenesFrequenciesDAO(NCGenesFrequenciesDAO nCGenesFrequenciesDAO);

    public PhenotypeXDAO getPhenotypeXDAO();

    public void setPhenotypeXDAO(PhenotypeXDAO phenotypeXDAO);

    public ReportDAO getReportDAO();

    public void setReportDAO(ReportDAO reportDAO);

    public UnimportantExonDAO getUnimportantExonDAO();

    public void setUnimportantExonDAO(UnimportantExonDAO unimportantExonDAO);

    public AssertionRankingDAO getAssertionRankingDAO();

    public void setAssertionRankingDAO(AssertionRankingDAO assertionRankingDAO);

    public ReferenceClinicalAssertionsDAO getReferenceClinicalAssertionsDAO();

    public void setReferenceClinicalAssertionsDAO(ReferenceClinicalAssertionsDAO referenceClinicalAssertionsDAO);

    public SNPAlleleDAO getSNPAlleleDAO();

    public void setSNPAlleleDAO(SNPAlleleDAO sNPAlleleDAO);

    public SNPAlleleFrequencyDAO getSNPAlleleFrequencyDAO();

    public void setSNPAlleleFrequencyDAO(SNPAlleleFrequencyDAO sNPAlleleFrequencyDAO);

    public SNPDAO getSNPDAO();

    public void setSNPDAO(SNPDAO sNPDAO);

    public SNPGenotypeFrequencyDAO getSNPGenotypeFrequencyDAO();

    public void setSNPGenotypeFrequencyDAO(SNPGenotypeFrequencyDAO sNPGenotypeFrequencyDAO);

    public SNPMappingAggDAO getSNPMappingAggDAO();

    public void setSNPMappingAggDAO(SNPMappingAggDAO sNPMappingAggDAO);

    public SNPMappingDAO getSNPMappingDAO();

    public void setSNPMappingDAO(SNPMappingDAO sNPMappingDAO);

    public ESPSNPFrequencyPopulationDAO getESPSNPFrequencyPopulationDAO();

    public void setESPSNPFrequencyPopulationDAO(ESPSNPFrequencyPopulationDAO eSPSNPFrequencyPopulationDAO);

    public MaxVariantFrequencyDAO getMaxVariantFrequencyDAO();

    public void setMaxVariantFrequencyDAO(MaxVariantFrequencyDAO maxVariantFrequencyDAO);

    public VariantFrequencyDAO getVariantFrequencyDAO();

    public void setVariantFrequencyDAO(VariantFrequencyDAO variantFrequencyDAO);

    public IndelMaxFrequencyDAO getIndelMaxFrequencyDAO();

    public void setIndelMaxFrequencyDAO(IndelMaxFrequencyDAO indelMaxFrequencyDAO);

    public OneThousandGenomeIndelFrequencyDAO getOneThousandGenomeIndelFrequencyDAO();

    public void setOneThousandGenomeIndelFrequencyDAO(OneThousandGenomeIndelFrequencyDAO oneThousandGenomeIndelFrequencyDAO);

    public OneThousandGenomeSNPFrequencyPopulationDAO getOneThousandGenomeSNPFrequencyPopulationDAO();

    public void setOneThousandGenomeSNPFrequencyPopulationDAO(
            OneThousandGenomeSNPFrequencyPopulationDAO oneThousandGenomeSNPFrequencyPopulationDAO);

    public OneThousandGenomeSNPFrequencySubpopulationDAO getOneThousandGenomeSNPFrequencySubpopulationDAO();

    void setOneThousandGenomeSNPFrequencySubpopulationDAO(
            OneThousandGenomeSNPFrequencySubpopulationDAO oneThousandGenomeSNPFrequencySubpopulationDAO);

    public SNPPopulationMaxFrequencyDAO getSNPPopulationMaxFrequencyDAO();

    public void setSNPPopulationMaxFrequencyDAO(SNPPopulationMaxFrequencyDAO sNPPopulationMaxFrequencyDAO);

    public HGMDLocatedVariantDAO getHGMDLocatedVariantDAO();

    public void setHGMDLocatedVariantDAO(HGMDLocatedVariantDAO hGMDLocatedVariantDAO);

    public HGNCGeneDAO getHGNCGeneDAO();

    public void setHGNCGeneDAO(HGNCGeneDAO hGNCGeneDAO);

    public GenomeRefDAO getGenomeRefDAO();

    public void setGenomeRefDAO(GenomeRefDAO genomeRefDAO);

    public GenomeRefSeqDAO getGenomeRefSeqDAO();

    public void setGenomeRefSeqDAO(GenomeRefSeqDAO genomeRefSeqDAO);

    public GenomeRefSeqLocationDAO getGenomeRefSeqLocationDAO();

    public void setGenomeRefSeqLocationDAO(GenomeRefSeqLocationDAO genomeRefSeqLocationDAO);

    public FeatureDAO getFeatureDAO();

    public void setFeatureDAO(FeatureDAO featureDAO);

    public LocationTypeDAO getLocationTypeDAO();

    public void setLocationTypeDAO(LocationTypeDAO locationTypeDAO);

    public RefSeqCodingSequenceDAO getRefSeqCodingSequenceDAO();

    public void setRefSeqCodingSequenceDAO(RefSeqCodingSequenceDAO refSeqCodingSequenceDAO);

    public RefSeqGeneDAO getRefSeqGeneDAO();

    public void setRefSeqGeneDAO(RefSeqGeneDAO refSeqGeneDAO);

    public RegionGroupDAO getRegionGroupDAO();

    public void setRegionGroupDAO(RegionGroupDAO regionGroupDAO);

    public RegionGroupRegionDAO getRegionGroupRegionDAO();

    public void setRegionGroupRegionDAO(RegionGroupRegionDAO regionGroupRegionDAO);

    public TranscriptDAO getTranscriptDAO();

    public void setTranscriptDAO(TranscriptDAO transcriptDAO);

    public TranscriptMapsDAO getTranscriptMapsDAO();

    public void setTranscriptMapsDAO(TranscriptMapsDAO transcriptMapsDAO);

    public TranscriptMapsExonsDAO getTranscriptMapsExonsDAO();

    public void setTranscriptMapsExonsDAO(TranscriptMapsExonsDAO transcriptMapsExonsDAO);

    public VariantEffectDAO getVariantEffectDAO();

    public void setVariantEffectDAO(VariantEffectDAO variantEffectDAO);

    public Variants_48_2_DAO getVariants_48_2_DAO();

    public void setVariants_48_2_DAO(Variants_48_2_DAO variants_48_2_DAO);

    public Variants_61_2_DAO getVariants_61_2_DAO();

    public void setVariants_61_2_DAO(Variants_61_2_DAO variants_61_2_DAO);

    public Variants_78_4_DAO getVariants_78_4_DAO();

    public void setVariants_78_4_DAO(Variants_78_4_DAO variants_78_4_DAO);

    public AssemblyDAO getAssemblyDAO();

    public void setAssemblyDAO(AssemblyDAO assemblyDAO);

    public AssemblyLocatedVariantDAO getAssemblyLocatedVariantDAO();

    public void setAssemblyLocatedVariantDAO(AssemblyLocatedVariantDAO assemblyLocatedVariantDAO);

    public AssemblyLocatedVariantQCDAO getAssemblyLocatedVariantQCDAO();

    public void setAssemblyLocatedVariantQCDAO(AssemblyLocatedVariantQCDAO assemblyLocatedVariantQCDAO);

    public AssemblyLocationDAO getAssemblyLocationDAO();

    public void setAssemblyLocationDAO(AssemblyLocationDAO assemblyLocationDAO);

    public CanonicalAlleleDAO getCanonicalAlleleDAO();

    public void setCanonicalAlleleDAO(CanonicalAlleleDAO canonicalAlleleDAO);

    public LabDAO getLabDAO();

    public void setLabDAO(LabDAO labDAO);

    public LibraryDAO getLibraryDAO();

    public void setLibraryDAO(LibraryDAO libraryDAO);

    public LocatedVariantDAO getLocatedVariantDAO();

    public void setLocatedVariantDAO(LocatedVariantDAO locatedVariantDAO);

    public ProjectDAO getProjectDAO();

    public void setProjectDAO(ProjectDAO projectDAO);

    public SampleDAO getSampleDAO();

    public void setSampleDAO(SampleDAO sampleDAO);

    public VariantSetDAO getVariantSetDAO();

    public void setVariantSetDAO(VariantSetDAO variantSetDAO);

    public VariantSetLoadDAO getVariantSetLoadDAO();

    public void setVariantSetLoadDAO(VariantSetLoadDAO variantSetLoadDAO);

    public VariantSetLocationDAO getVariantSetLocationDAO();

    public void setVariantSetLocationDAO(VariantSetLocationDAO variantSetLocationDAO);

    public VariantTypeDAO getVariantTypeDAO();

    public void setVariantTypeDAO(VariantTypeDAO variantTypeDAO);

}