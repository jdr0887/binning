package org.renci.binning.dao.clinbin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DiagnosticBinningJobDAO;
import org.renci.binning.dao.clinbin.model.DiagnosticBinningJob;
import org.renci.binning.dao.jpa.BinningDAOManager;

public class DiagnosticBinningJobDAOTest {

    @Test
    public void testSave() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        final DiagnosticBinningJobDAO diagnosticBinningJobDAO = daoMgr.getDAOBean().getDiagnosticBinningJobDAO();

        Arrays.asList("NA12878", "NA12891", "NA12892").forEach(a -> {
            try {
                DiagnosticBinningJob job = new DiagnosticBinningJob();
                job.setDx(daoMgr.getDAOBean().getDXDAO().findById(9));
                job.setStatus(daoMgr.getDAOBean().getDiagnosticStatusTypeDAO().findById("Requested"));
                job.setGender("M");
                job.setListVersion(40);
                job.setStudy("NCGENES Study");
                job.setParticipant(a);
                job.setVcfFile(String.format("/tmp/%s-exons.vcf", a));
                job.setFailureMessage("");
                job.setId(diagnosticBinningJobDAO.save(job));
                System.out.println(job.toString());
            } catch (BinningDAOException e) {
                e.printStackTrace();
            }
        });

        // DiagnosticBinningJob job = new DiagnosticBinningJob();
        // job.setDx(daoMgr.getDAOBean().getDXDAO().findById(46L));
        // job.setStatus(daoMgr.getDAOBean().getDiagnosticStatusTypeDAO().findById("Requested"));
        // job.setGender("F");
        // job.setListVersion(40);
        // job.setStudy("GS");
        // job.setParticipant("GSU_000136");
        // job.setVcfFile("/tmp/GSU_000136.merged.fb.va.vcf");
        // job.setFailureMessage("");
        // job.setId(diagnosticBinningJobDAO.save(job));

    }

    @Test
    public void testFindProcessing() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        DiagnosticBinningJobDAO binResultsFinalDiagnosticDAO = daoMgr.getDAOBean().getDiagnosticBinningJobDAO();
        List<DiagnosticBinningJob> results = binResultsFinalDiagnosticDAO.findAvailableJobs();
        System.out.println(results.size());
    }

    @Test
    public void testFindCompleted() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        DiagnosticBinningJobDAO binResultsFinalDiagnosticDAO = daoMgr.getDAOBean().getDiagnosticBinningJobDAO();
        List<DiagnosticBinningJob> results = binResultsFinalDiagnosticDAO.findCompletedJobsByStudy("NCGENES Study");
        // for (DiagnosticBinningJob job : results) {
        // System.out.println(job.toString());
        // }
        // results.forEach(a -> System.out.println(String.format("%s,%s,%s", a.getListVersion(), a.getDx().getId(),
        // a.getParticipant())));

        try (FileWriter fw = new FileWriter(new File("/tmp", "diagnosticBinningJobs.txt")); BufferedWriter bw = new BufferedWriter(fw)) {
            for (DiagnosticBinningJob job : results) {

                Integer listVersion = job.getListVersion();
                Integer dxId = job.getDx().getId();
                String participantId = job.getParticipant();

                String flowcellBarcodeLane = job.getVcfFile().replace(participantId, "")
                        .replace("/proj/renci/sequence_analysis/ncgenes/", "")
                        .replace(".fixed-rg.deduped.realign.fixmate.recal.variant.recalibrated.filtered.vcf", "").replace("/", "");

                String flowcell = flowcellBarcodeLane.substring(0, flowcellBarcodeLane.length() - 12);

                String barcode = flowcellBarcodeLane.replace(flowcell, "");
                barcode = barcode.substring(0, barcode.length() - 5).replace("_", "");

                String lane = flowcellBarcodeLane.replace(flowcell, "").replace(barcode, "").replace("_", "").replace("L", "").replace("0",
                        "");

                // bw.write(String.format("%s,%s,%s,%s", listVersion, dxId, participantId, flowcell));
                bw.write(String.format("ncgenes-dx:register-to-irods --dx %d --version %d --sampleId", dxId, listVersion));

                // bw.write(String.format(
                // "select a.id from mapseq.sample a left join mapseq.named_entity b on a.id = b.id left join
                // mapseq.flowcell c on c.id = a.flowcell_fid left join mapseq.named_entity d on d.id = c.id where
                // b.name like '%s%%' and a.barcode = '%s' and a.lane_index = %s and d.name = '%s';",
                // participantId, barcode, lane, flowcell));

                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set<String> participants = new HashSet<String>();
        // results.forEach(a -> participants.add(a.getParticipant()));
        // Collections.synchronizedSet(participants);
        // try (FileWriter fw = new FileWriter(new File("/tmp", "participants.txt")); BufferedWriter bw = new
        // BufferedWriter(fw)) {
        // for (DiagnosticBinningJob job : results) {
        // bw.write(String.format("%s", job.getParticipant()));
        // bw.newLine();
        // bw.flush();
        // }
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

    }

}
