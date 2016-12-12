package org.renci.binning.dao.clinbin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.model.IncidentalBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class IncidentalBinningJobTest {

    @Test
    public void testFindProcessing() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        IncidentalBinningJobDAO dao = daoMgr.getDAOBean().getIncidentalBinningJobDAO();
        List<IncidentalBinningJob> results = dao.findAvailableJobs();
        System.out.println(results.size());
    }

    @Test
    public void testFindCompleted() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        IncidentalBinningJobDAO dao = daoMgr.getDAOBean().getIncidentalBinningJobDAO();
        List<IncidentalBinningJob> results = dao.findCompletedJobsByStudy("NCGENES Study");
        try (FileWriter fw = new FileWriter(new File("/tmp", "incidentalBinningJobs.sql")); BufferedWriter bw = new BufferedWriter(fw)) {
            for (IncidentalBinningJob job : results) {
                if (StringUtils.isNotEmpty(job.getVcfFile())) {

                    // bw.write(String.format("%s,%s,%s", job.getListVersion(), job.getIncidentalBinX().getId(),
                    // job.getVcfFile().replace("/proj/renci/sequence_analysis/ncgenes/", "")
                    // .replace(".fixed-rg.deduped.realign.fixmate.recal.variant.recalibrated.filtered.vcf", "")
                    // .replace("/", ",")));

                    Integer listVersion = job.getListVersion();
                    Integer incidentalId = job.getIncidentalBinX().getId();
                    String participantId = job.getParticipant();

                    String flowcellBarcodeLane = job.getVcfFile().replace(participantId, "")
                            .replace("/proj/renci/sequence_analysis/ncgenes/", "")
                            .replace(".fixed-rg.deduped.realign.fixmate.recal.variant.recalibrated.filtered.vcf", "").replace("/", "");

                    String flowcell = flowcellBarcodeLane.substring(0, flowcellBarcodeLane.length() - 12);

                    String barcode = flowcellBarcodeLane.replace(flowcell, "");
                    barcode = barcode.substring(0, barcode.length() - 5).replace("_", "");

                    String lane = flowcellBarcodeLane.replace(flowcell, "").replace(barcode, "").replace("_", "").replace("L", "")
                            .replace("0", "");

                    bw.write(String.format(
                            "select a.id from mapseq.sample a left join mapseq.named_entity b on a.id = b.id left join mapseq.flowcell c on c.id = a.flowcell_fid left join mapseq.named_entity d on d.id = c.id where b.name like '%s%%' and a.barcode = '%s' and a.lane_index = %s and d.name = '%s';",
                            participantId, barcode, lane, flowcell));

                    // bw.write(String.format("%s,%s,%s,%s", listVersion, dxId, participantId, flowcell));
                    // bw.write(String.format("ncgenes-incidental-variantcalling:register-to-irods --incidental %d
                    // --version %d --sampleId",
                    // incidentalId, listVersion));

                    bw.newLine();
                    bw.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
