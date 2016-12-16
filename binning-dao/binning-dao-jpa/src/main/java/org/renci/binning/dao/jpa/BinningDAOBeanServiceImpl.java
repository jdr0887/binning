package org.renci.binning.dao.jpa;

import org.renci.binning.dao.BinningDAOBeanService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BinningDAOBeanServiceImpl implements BinningDAOBeanService {

    @Autowired
    private AnnotationGeneDAO annotationGeneDAO;

    @Autowired
    private AnnotationGeneExternalIdsDAO annotationGeneExternalIdsDAO;

    @Autowired
    private BinResultsFinalDiagnosticDAO binResultsFinalDiagnosticDAO;

    @Autowired
    private BinResultsFinalIncidentalXDAO binResultsFinalIncidentalXDAO;

    @Autowired
    private BinResultsFinalRiskXDAO binResultsFinalRiskXDAO;

    @Autowired
    private CarrierStatusDAO carrierStatusDAO;

    @Autowired
    private DiagnosticBinningJobDAO diagnosticBinningJobDAO;

    @Autowired
    private DiagnosticStatusTypeDAO diagnosticStatusTypeDAO;

    @Autowired
    private DiagnosticGeneDAO diagnosticGeneDAO;

    @Autowired
    private DiagnosticResultVersionDAO diagnosticResultVersionDAO;

    @Autowired
    private DiseaseClassDAO diseaseClassDAO;

    @Autowired
    private IncidentalResultVersionXDAO incidentalResultVersionXDAO;

    @Autowired
    private IncidentalStatusTypeDAO incidentalStatusTypeDAO;

    @Autowired
    private IncidentalBinHaplotypeXDAO incidentalBinHaplotypeXDAO;

    @Autowired
    private IncidentalBinGeneXDAO incidentalBinGeneXDAO;

    @Autowired
    private IncidentalBinGroupVersionXDAO incidentalBinGroupVersionXDAO;

    @Autowired
    private IncidentalBinXDAO incidentalBinXDAO;

    @Autowired
    private DXDAO DXDAO;

    @Autowired
    private DXExonsDAO DXExonsDAO;

    @Autowired
    private DXCoverageDAO DXCoverageDAO;

    @Autowired
    private FeatureDAO featureDAO;

    @Autowired
    private GenomeRefDAO genomeRefDAO;

    @Autowired
    private GenomeRefSeqDAO genomeRefSeqDAO;

    @Autowired
    private GenomeRefSeqLocationDAO genomeRefSeqLocationDAO;

    @Autowired
    private HGNCGeneDAO HGNCGeneDAO;

    @Autowired
    private HGMDLocatedVariantDAO HGMDLocatedVariantDAO;

    @Autowired
    private IncidentalBinningJobDAO incidentalBinningJobDAO;

    @Autowired
    private LocationTypeDAO locationTypeDAO;

    @Autowired
    private MaxVariantFrequencyDAO maxVariantFrequencyDAO;

    @Autowired
    private MaxFrequencyDAO maxFrequencyDAO;

    @Autowired
    private MaxFrequencySourceDAO maxFrequencySourceDAO;

    @Autowired
    private MissingHaplotypeDAO missingHaplotypeDAO;

    @Autowired
    private NCGenesFrequenciesDAO NCGenesFrequenciesDAO;

    @Autowired
    private OneThousandGenomeIndelFrequencyDAO oneThousandGenomeIndelFrequencyDAO;

    @Autowired
    private OneThousandGenomeSNPFrequencyPopulationDAO oneThousandGenomeSNPFrequencyPopulationDAO;

    @Autowired
    private OneThousandGenomeSNPFrequencySubpopulationDAO oneThousandGenomeSNPFrequencySubpopulationDAO;

    @Autowired
    private IndelMaxFrequencyDAO indelMaxFrequencyDAO;

    @Autowired
    private ReportDAO reportDAO;

    @Autowired
    private ESPSNPFrequencyPopulationDAO ESPSNPFrequencyPopulationDAO;

    @Autowired
    private SNPPopulationMaxFrequencyDAO SNPPopulationMaxFrequencyDAO;

    @Autowired
    private SNPAlleleDAO SNPAlleleDAO;

    @Autowired
    private SNPAlleleFrequencyDAO SNPAlleleFrequencyDAO;

    @Autowired
    private SNPDAO SNPDAO;

    @Autowired
    private SNPGenotypeFrequencyDAO SNPGenotypeFrequencyDAO;

    @Autowired
    private SNPMappingAggDAO SNPMappingAggDAO;

    @Autowired
    private SNPMappingDAO SNPMappingDAO;

    @Autowired
    private ReferenceClinicalAssertionsDAO referenceClinicalAssertionsDAO;

    @Autowired
    private AssertionRankingDAO assertionRankingDAO;

    @Autowired
    private RefSeqCodingSequenceDAO refSeqCodingSequenceDAO;

    @Autowired
    private RefSeqGeneDAO refSeqGeneDAO;

    @Autowired
    private RegionGroupDAO regionGroupDAO;

    @Autowired
    private RegionGroupRegionDAO regionGroupRegionDAO;

    @Autowired
    private TranscriptDAO transcriptDAO;

    @Autowired
    private TranscriptMapsDAO transcriptMapsDAO;

    @Autowired
    private TranscriptMapsExonsDAO transcriptMapsExonsDAO;

    @Autowired
    private VariantEffectDAO variantEffectDAO;

    @Autowired
    private Variants_61_2_DAO variants_61_2_DAO;

    @Autowired
    private Variants_48_2_DAO variants_48_2_DAO;

    @Autowired
    private VariantFrequencyDAO variantFrequencyDAO;

    @Autowired
    private AssemblyDAO assemblyDAO;

    @Autowired
    private AssemblyLocationDAO assemblyLocationDAO;

    @Autowired
    private AssemblyLocatedVariantDAO assemblyLocatedVariantDAO;

    @Autowired
    private AssemblyLocatedVariantQCDAO assemblyLocatedVariantQCDAO;

    @Autowired
    private CanonicalAlleleDAO canonicalAlleleDAO;

    @Autowired
    private LabDAO labDAO;

    @Autowired
    private LibraryDAO libraryDAO;

    @Autowired
    private LocatedVariantDAO LocatedVariantDAO;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private SampleDAO sampleDAO;

    @Autowired
    private VariantSetDAO variantSetDAO;

    @Autowired
    private VariantSetLoadDAO variantSetLoadDAO;

    @Autowired
    private VariantSetLocationDAO variantSetLocationDAO;

    @Autowired
    private VariantTypeDAO variantTypeDAO;

    @Autowired
    private UnimportantExonDAO unimportantExonDAO;

    @Autowired
    private PhenotypeXDAO phenotypeXDAO;

    public BinningDAOBeanServiceImpl() {
        super();
    }

    public AnnotationGeneDAO getAnnotationGeneDAO() {
        return annotationGeneDAO;
    }

    public void setAnnotationGeneDAO(AnnotationGeneDAO annotationGeneDAO) {
        this.annotationGeneDAO = annotationGeneDAO;
    }

    public AnnotationGeneExternalIdsDAO getAnnotationGeneExternalIdsDAO() {
        return annotationGeneExternalIdsDAO;
    }

    public void setAnnotationGeneExternalIdsDAO(AnnotationGeneExternalIdsDAO annotationGeneExternalIdsDAO) {
        this.annotationGeneExternalIdsDAO = annotationGeneExternalIdsDAO;
    }

    public BinResultsFinalDiagnosticDAO getBinResultsFinalDiagnosticDAO() {
        return binResultsFinalDiagnosticDAO;
    }

    public void setBinResultsFinalDiagnosticDAO(BinResultsFinalDiagnosticDAO binResultsFinalDiagnosticDAO) {
        this.binResultsFinalDiagnosticDAO = binResultsFinalDiagnosticDAO;
    }

    public BinResultsFinalIncidentalXDAO getBinResultsFinalIncidentalXDAO() {
        return binResultsFinalIncidentalXDAO;
    }

    public void setBinResultsFinalIncidentalXDAO(BinResultsFinalIncidentalXDAO binResultsFinalIncidentalXDAO) {
        this.binResultsFinalIncidentalXDAO = binResultsFinalIncidentalXDAO;
    }

    public BinResultsFinalRiskXDAO getBinResultsFinalRiskXDAO() {
        return binResultsFinalRiskXDAO;
    }

    public void setBinResultsFinalRiskXDAO(BinResultsFinalRiskXDAO binResultsFinalRiskXDAO) {
        this.binResultsFinalRiskXDAO = binResultsFinalRiskXDAO;
    }

    public CarrierStatusDAO getCarrierStatusDAO() {
        return carrierStatusDAO;
    }

    public void setCarrierStatusDAO(CarrierStatusDAO carrierStatusDAO) {
        this.carrierStatusDAO = carrierStatusDAO;
    }

    public DiagnosticBinningJobDAO getDiagnosticBinningJobDAO() {
        return diagnosticBinningJobDAO;
    }

    public void setDiagnosticBinningJobDAO(DiagnosticBinningJobDAO diagnosticBinningJobDAO) {
        this.diagnosticBinningJobDAO = diagnosticBinningJobDAO;
    }

    public DiagnosticStatusTypeDAO getDiagnosticStatusTypeDAO() {
        return diagnosticStatusTypeDAO;
    }

    public void setDiagnosticStatusTypeDAO(DiagnosticStatusTypeDAO diagnosticStatusTypeDAO) {
        this.diagnosticStatusTypeDAO = diagnosticStatusTypeDAO;
    }

    public DiagnosticGeneDAO getDiagnosticGeneDAO() {
        return diagnosticGeneDAO;
    }

    public void setDiagnosticGeneDAO(DiagnosticGeneDAO diagnosticGeneDAO) {
        this.diagnosticGeneDAO = diagnosticGeneDAO;
    }

    public DiagnosticResultVersionDAO getDiagnosticResultVersionDAO() {
        return diagnosticResultVersionDAO;
    }

    public void setDiagnosticResultVersionDAO(DiagnosticResultVersionDAO diagnosticResultVersionDAO) {
        this.diagnosticResultVersionDAO = diagnosticResultVersionDAO;
    }

    public DiseaseClassDAO getDiseaseClassDAO() {
        return diseaseClassDAO;
    }

    public void setDiseaseClassDAO(DiseaseClassDAO diseaseClassDAO) {
        this.diseaseClassDAO = diseaseClassDAO;
    }

    public IncidentalResultVersionXDAO getIncidentalResultVersionXDAO() {
        return incidentalResultVersionXDAO;
    }

    public void setIncidentalResultVersionXDAO(IncidentalResultVersionXDAO incidentalResultVersionXDAO) {
        this.incidentalResultVersionXDAO = incidentalResultVersionXDAO;
    }

    public IncidentalStatusTypeDAO getIncidentalStatusTypeDAO() {
        return incidentalStatusTypeDAO;
    }

    public void setIncidentalStatusTypeDAO(IncidentalStatusTypeDAO incidentalStatusTypeDAO) {
        this.incidentalStatusTypeDAO = incidentalStatusTypeDAO;
    }

    public IncidentalBinHaplotypeXDAO getIncidentalBinHaplotypeXDAO() {
        return incidentalBinHaplotypeXDAO;
    }

    public void setIncidentalBinHaplotypeXDAO(IncidentalBinHaplotypeXDAO incidentalBinHaplotypeXDAO) {
        this.incidentalBinHaplotypeXDAO = incidentalBinHaplotypeXDAO;
    }

    public IncidentalBinGeneXDAO getIncidentalBinGeneXDAO() {
        return incidentalBinGeneXDAO;
    }

    public void setIncidentalBinGeneXDAO(IncidentalBinGeneXDAO incidentalBinGeneXDAO) {
        this.incidentalBinGeneXDAO = incidentalBinGeneXDAO;
    }

    public IncidentalBinGroupVersionXDAO getIncidentalBinGroupVersionXDAO() {
        return incidentalBinGroupVersionXDAO;
    }

    public void setIncidentalBinGroupVersionXDAO(IncidentalBinGroupVersionXDAO incidentalBinGroupVersionXDAO) {
        this.incidentalBinGroupVersionXDAO = incidentalBinGroupVersionXDAO;
    }

    public IncidentalBinXDAO getIncidentalBinXDAO() {
        return incidentalBinXDAO;
    }

    public void setIncidentalBinXDAO(IncidentalBinXDAO incidentalBinXDAO) {
        this.incidentalBinXDAO = incidentalBinXDAO;
    }

    public DXDAO getDXDAO() {
        return DXDAO;
    }

    public void setDXDAO(DXDAO dXDAO) {
        DXDAO = dXDAO;
    }

    public DXExonsDAO getDXExonsDAO() {
        return DXExonsDAO;
    }

    public void setDXExonsDAO(DXExonsDAO dXExonsDAO) {
        DXExonsDAO = dXExonsDAO;
    }

    public DXCoverageDAO getDXCoverageDAO() {
        return DXCoverageDAO;
    }

    public void setDXCoverageDAO(DXCoverageDAO dXCoverageDAO) {
        DXCoverageDAO = dXCoverageDAO;
    }

    public FeatureDAO getFeatureDAO() {
        return featureDAO;
    }

    public void setFeatureDAO(FeatureDAO featureDAO) {
        this.featureDAO = featureDAO;
    }

    public GenomeRefDAO getGenomeRefDAO() {
        return genomeRefDAO;
    }

    public void setGenomeRefDAO(GenomeRefDAO genomeRefDAO) {
        this.genomeRefDAO = genomeRefDAO;
    }

    public GenomeRefSeqDAO getGenomeRefSeqDAO() {
        return genomeRefSeqDAO;
    }

    public void setGenomeRefSeqDAO(GenomeRefSeqDAO genomeRefSeqDAO) {
        this.genomeRefSeqDAO = genomeRefSeqDAO;
    }

    public GenomeRefSeqLocationDAO getGenomeRefSeqLocationDAO() {
        return genomeRefSeqLocationDAO;
    }

    public void setGenomeRefSeqLocationDAO(GenomeRefSeqLocationDAO genomeRefSeqLocationDAO) {
        this.genomeRefSeqLocationDAO = genomeRefSeqLocationDAO;
    }

    public HGNCGeneDAO getHGNCGeneDAO() {
        return HGNCGeneDAO;
    }

    public void setHGNCGeneDAO(HGNCGeneDAO hGNCGeneDAO) {
        HGNCGeneDAO = hGNCGeneDAO;
    }

    public HGMDLocatedVariantDAO getHGMDLocatedVariantDAO() {
        return HGMDLocatedVariantDAO;
    }

    public void setHGMDLocatedVariantDAO(HGMDLocatedVariantDAO hGMDLocatedVariantDAO) {
        HGMDLocatedVariantDAO = hGMDLocatedVariantDAO;
    }

    public IncidentalBinningJobDAO getIncidentalBinningJobDAO() {
        return incidentalBinningJobDAO;
    }

    public void setIncidentalBinningJobDAO(IncidentalBinningJobDAO incidentalBinningJobDAO) {
        this.incidentalBinningJobDAO = incidentalBinningJobDAO;
    }

    public LocationTypeDAO getLocationTypeDAO() {
        return locationTypeDAO;
    }

    public void setLocationTypeDAO(LocationTypeDAO locationTypeDAO) {
        this.locationTypeDAO = locationTypeDAO;
    }

    public MaxVariantFrequencyDAO getMaxVariantFrequencyDAO() {
        return maxVariantFrequencyDAO;
    }

    public void setMaxVariantFrequencyDAO(MaxVariantFrequencyDAO maxVariantFrequencyDAO) {
        this.maxVariantFrequencyDAO = maxVariantFrequencyDAO;
    }

    public MaxFrequencyDAO getMaxFrequencyDAO() {
        return maxFrequencyDAO;
    }

    public void setMaxFrequencyDAO(MaxFrequencyDAO maxFrequencyDAO) {
        this.maxFrequencyDAO = maxFrequencyDAO;
    }

    public MaxFrequencySourceDAO getMaxFrequencySourceDAO() {
        return maxFrequencySourceDAO;
    }

    public void setMaxFrequencySourceDAO(MaxFrequencySourceDAO maxFrequencySourceDAO) {
        this.maxFrequencySourceDAO = maxFrequencySourceDAO;
    }

    public MissingHaplotypeDAO getMissingHaplotypeDAO() {
        return missingHaplotypeDAO;
    }

    public void setMissingHaplotypeDAO(MissingHaplotypeDAO missingHaplotypeDAO) {
        this.missingHaplotypeDAO = missingHaplotypeDAO;
    }

    public NCGenesFrequenciesDAO getNCGenesFrequenciesDAO() {
        return NCGenesFrequenciesDAO;
    }

    public void setNCGenesFrequenciesDAO(NCGenesFrequenciesDAO nCGenesFrequenciesDAO) {
        NCGenesFrequenciesDAO = nCGenesFrequenciesDAO;
    }

    public OneThousandGenomeIndelFrequencyDAO getOneThousandGenomeIndelFrequencyDAO() {
        return oneThousandGenomeIndelFrequencyDAO;
    }

    public void setOneThousandGenomeIndelFrequencyDAO(OneThousandGenomeIndelFrequencyDAO oneThousandGenomeIndelFrequencyDAO) {
        this.oneThousandGenomeIndelFrequencyDAO = oneThousandGenomeIndelFrequencyDAO;
    }

    public OneThousandGenomeSNPFrequencyPopulationDAO getOneThousandGenomeSNPFrequencyPopulationDAO() {
        return oneThousandGenomeSNPFrequencyPopulationDAO;
    }

    public void setOneThousandGenomeSNPFrequencyPopulationDAO(
            OneThousandGenomeSNPFrequencyPopulationDAO oneThousandGenomeSNPFrequencyPopulationDAO) {
        this.oneThousandGenomeSNPFrequencyPopulationDAO = oneThousandGenomeSNPFrequencyPopulationDAO;
    }

    public OneThousandGenomeSNPFrequencySubpopulationDAO getOneThousandGenomeSNPFrequencySubpopulationDAO() {
        return oneThousandGenomeSNPFrequencySubpopulationDAO;
    }

    public void setOneThousandGenomeSNPFrequencySubpopulationDAO(
            OneThousandGenomeSNPFrequencySubpopulationDAO oneThousandGenomeSNPFrequencySubpopulationDAO) {
        this.oneThousandGenomeSNPFrequencySubpopulationDAO = oneThousandGenomeSNPFrequencySubpopulationDAO;
    }

    public IndelMaxFrequencyDAO getIndelMaxFrequencyDAO() {
        return indelMaxFrequencyDAO;
    }

    public void setIndelMaxFrequencyDAO(IndelMaxFrequencyDAO indelMaxFrequencyDAO) {
        this.indelMaxFrequencyDAO = indelMaxFrequencyDAO;
    }

    public ReportDAO getReportDAO() {
        return reportDAO;
    }

    public void setReportDAO(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public ESPSNPFrequencyPopulationDAO getESPSNPFrequencyPopulationDAO() {
        return ESPSNPFrequencyPopulationDAO;
    }

    public void setESPSNPFrequencyPopulationDAO(ESPSNPFrequencyPopulationDAO eSPSNPFrequencyPopulationDAO) {
        ESPSNPFrequencyPopulationDAO = eSPSNPFrequencyPopulationDAO;
    }

    public SNPPopulationMaxFrequencyDAO getSNPPopulationMaxFrequencyDAO() {
        return SNPPopulationMaxFrequencyDAO;
    }

    public void setSNPPopulationMaxFrequencyDAO(SNPPopulationMaxFrequencyDAO sNPPopulationMaxFrequencyDAO) {
        SNPPopulationMaxFrequencyDAO = sNPPopulationMaxFrequencyDAO;
    }

    public SNPAlleleDAO getSNPAlleleDAO() {
        return SNPAlleleDAO;
    }

    public void setSNPAlleleDAO(SNPAlleleDAO sNPAlleleDAO) {
        SNPAlleleDAO = sNPAlleleDAO;
    }

    public SNPAlleleFrequencyDAO getSNPAlleleFrequencyDAO() {
        return SNPAlleleFrequencyDAO;
    }

    public void setSNPAlleleFrequencyDAO(SNPAlleleFrequencyDAO sNPAlleleFrequencyDAO) {
        SNPAlleleFrequencyDAO = sNPAlleleFrequencyDAO;
    }

    public SNPDAO getSNPDAO() {
        return SNPDAO;
    }

    public void setSNPDAO(SNPDAO sNPDAO) {
        SNPDAO = sNPDAO;
    }

    public SNPGenotypeFrequencyDAO getSNPGenotypeFrequencyDAO() {
        return SNPGenotypeFrequencyDAO;
    }

    public void setSNPGenotypeFrequencyDAO(SNPGenotypeFrequencyDAO sNPGenotypeFrequencyDAO) {
        SNPGenotypeFrequencyDAO = sNPGenotypeFrequencyDAO;
    }

    public SNPMappingAggDAO getSNPMappingAggDAO() {
        return SNPMappingAggDAO;
    }

    public void setSNPMappingAggDAO(SNPMappingAggDAO sNPMappingAggDAO) {
        SNPMappingAggDAO = sNPMappingAggDAO;
    }

    public SNPMappingDAO getSNPMappingDAO() {
        return SNPMappingDAO;
    }

    public void setSNPMappingDAO(SNPMappingDAO sNPMappingDAO) {
        SNPMappingDAO = sNPMappingDAO;
    }

    public ReferenceClinicalAssertionsDAO getReferenceClinicalAssertionsDAO() {
        return referenceClinicalAssertionsDAO;
    }

    public void setReferenceClinicalAssertionsDAO(ReferenceClinicalAssertionsDAO referenceClinicalAssertionsDAO) {
        this.referenceClinicalAssertionsDAO = referenceClinicalAssertionsDAO;
    }

    public AssertionRankingDAO getAssertionRankingDAO() {
        return assertionRankingDAO;
    }

    public void setAssertionRankingDAO(AssertionRankingDAO assertionRankingDAO) {
        this.assertionRankingDAO = assertionRankingDAO;
    }

    public RefSeqCodingSequenceDAO getRefSeqCodingSequenceDAO() {
        return refSeqCodingSequenceDAO;
    }

    public void setRefSeqCodingSequenceDAO(RefSeqCodingSequenceDAO refSeqCodingSequenceDAO) {
        this.refSeqCodingSequenceDAO = refSeqCodingSequenceDAO;
    }

    public RefSeqGeneDAO getRefSeqGeneDAO() {
        return refSeqGeneDAO;
    }

    public void setRefSeqGeneDAO(RefSeqGeneDAO refSeqGeneDAO) {
        this.refSeqGeneDAO = refSeqGeneDAO;
    }

    public RegionGroupDAO getRegionGroupDAO() {
        return regionGroupDAO;
    }

    public void setRegionGroupDAO(RegionGroupDAO regionGroupDAO) {
        this.regionGroupDAO = regionGroupDAO;
    }

    public RegionGroupRegionDAO getRegionGroupRegionDAO() {
        return regionGroupRegionDAO;
    }

    public void setRegionGroupRegionDAO(RegionGroupRegionDAO regionGroupRegionDAO) {
        this.regionGroupRegionDAO = regionGroupRegionDAO;
    }

    public TranscriptDAO getTranscriptDAO() {
        return transcriptDAO;
    }

    public void setTranscriptDAO(TranscriptDAO transcriptDAO) {
        this.transcriptDAO = transcriptDAO;
    }

    public TranscriptMapsDAO getTranscriptMapsDAO() {
        return transcriptMapsDAO;
    }

    public void setTranscriptMapsDAO(TranscriptMapsDAO transcriptMapsDAO) {
        this.transcriptMapsDAO = transcriptMapsDAO;
    }

    public TranscriptMapsExonsDAO getTranscriptMapsExonsDAO() {
        return transcriptMapsExonsDAO;
    }

    public void setTranscriptMapsExonsDAO(TranscriptMapsExonsDAO transcriptMapsExonsDAO) {
        this.transcriptMapsExonsDAO = transcriptMapsExonsDAO;
    }

    public VariantEffectDAO getVariantEffectDAO() {
        return variantEffectDAO;
    }

    public void setVariantEffectDAO(VariantEffectDAO variantEffectDAO) {
        this.variantEffectDAO = variantEffectDAO;
    }

    public Variants_61_2_DAO getVariants_61_2_DAO() {
        return variants_61_2_DAO;
    }

    public void setVariants_61_2_DAO(Variants_61_2_DAO variants_61_2_DAO) {
        this.variants_61_2_DAO = variants_61_2_DAO;
    }

    public Variants_48_2_DAO getVariants_48_2_DAO() {
        return variants_48_2_DAO;
    }

    public void setVariants_48_2_DAO(Variants_48_2_DAO variants_48_2_DAO) {
        this.variants_48_2_DAO = variants_48_2_DAO;
    }

    public VariantFrequencyDAO getVariantFrequencyDAO() {
        return variantFrequencyDAO;
    }

    public void setVariantFrequencyDAO(VariantFrequencyDAO variantFrequencyDAO) {
        this.variantFrequencyDAO = variantFrequencyDAO;
    }

    public AssemblyDAO getAssemblyDAO() {
        return assemblyDAO;
    }

    public void setAssemblyDAO(AssemblyDAO assemblyDAO) {
        this.assemblyDAO = assemblyDAO;
    }

    public AssemblyLocationDAO getAssemblyLocationDAO() {
        return assemblyLocationDAO;
    }

    public void setAssemblyLocationDAO(AssemblyLocationDAO assemblyLocationDAO) {
        this.assemblyLocationDAO = assemblyLocationDAO;
    }

    public AssemblyLocatedVariantDAO getAssemblyLocatedVariantDAO() {
        return assemblyLocatedVariantDAO;
    }

    public void setAssemblyLocatedVariantDAO(AssemblyLocatedVariantDAO assemblyLocatedVariantDAO) {
        this.assemblyLocatedVariantDAO = assemblyLocatedVariantDAO;
    }

    public AssemblyLocatedVariantQCDAO getAssemblyLocatedVariantQCDAO() {
        return assemblyLocatedVariantQCDAO;
    }

    public void setAssemblyLocatedVariantQCDAO(AssemblyLocatedVariantQCDAO assemblyLocatedVariantQCDAO) {
        this.assemblyLocatedVariantQCDAO = assemblyLocatedVariantQCDAO;
    }

    public CanonicalAlleleDAO getCanonicalAlleleDAO() {
        return canonicalAlleleDAO;
    }

    public void setCanonicalAlleleDAO(CanonicalAlleleDAO canonicalAlleleDAO) {
        this.canonicalAlleleDAO = canonicalAlleleDAO;
    }

    public LabDAO getLabDAO() {
        return labDAO;
    }

    public void setLabDAO(LabDAO labDAO) {
        this.labDAO = labDAO;
    }

    public LibraryDAO getLibraryDAO() {
        return libraryDAO;
    }

    public void setLibraryDAO(LibraryDAO libraryDAO) {
        this.libraryDAO = libraryDAO;
    }

    public LocatedVariantDAO getLocatedVariantDAO() {
        return LocatedVariantDAO;
    }

    public void setLocatedVariantDAO(LocatedVariantDAO locatedVariantDAO) {
        LocatedVariantDAO = locatedVariantDAO;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public SampleDAO getSampleDAO() {
        return sampleDAO;
    }

    public void setSampleDAO(SampleDAO sampleDAO) {
        this.sampleDAO = sampleDAO;
    }

    public VariantSetDAO getVariantSetDAO() {
        return variantSetDAO;
    }

    public void setVariantSetDAO(VariantSetDAO variantSetDAO) {
        this.variantSetDAO = variantSetDAO;
    }

    public VariantSetLoadDAO getVariantSetLoadDAO() {
        return variantSetLoadDAO;
    }

    public void setVariantSetLoadDAO(VariantSetLoadDAO variantSetLoadDAO) {
        this.variantSetLoadDAO = variantSetLoadDAO;
    }

    public VariantSetLocationDAO getVariantSetLocationDAO() {
        return variantSetLocationDAO;
    }

    public void setVariantSetLocationDAO(VariantSetLocationDAO variantSetLocationDAO) {
        this.variantSetLocationDAO = variantSetLocationDAO;
    }

    public VariantTypeDAO getVariantTypeDAO() {
        return variantTypeDAO;
    }

    public void setVariantTypeDAO(VariantTypeDAO variantTypeDAO) {
        this.variantTypeDAO = variantTypeDAO;
    }

    public UnimportantExonDAO getUnimportantExonDAO() {
        return unimportantExonDAO;
    }

    public void setUnimportantExonDAO(UnimportantExonDAO unimportantExonDAO) {
        this.unimportantExonDAO = unimportantExonDAO;
    }

    public PhenotypeXDAO getPhenotypeXDAO() {
        return phenotypeXDAO;
    }

    public void setPhenotypeXDAO(PhenotypeXDAO phenotypeXDAO) {
        this.phenotypeXDAO = phenotypeXDAO;
    }

}
