package org.renci.binning.dao.clinbin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "binning_job")
public class BinningJob implements Persistable {

    private static final long serialVersionUID = 6699867736074991901L;

    @Id
    @Column(name = "binning_job_id")
    private Integer binningJobId;

    @Column(name = "incidental_list_version")
    private Integer incidentalListVersion;

    @Column(name = "diagnostic_list_version")
    private Integer diagnosticListVersion;

    @Column(name = "participant", length = 50)
    private String participant;

    @Column(name = "bin_start")
    private Date binStart;

    @Column(name = "bin_stop")
    private Date binStop;

    @Column(name = "select_bin_1")
    private Boolean selectBin1;

    @Column(name = "select_bin_2a")
    private Boolean selectBin2a;

    @Column(name = "select_bin_2b")
    private Boolean selectBin2b;

    @Column(name = "select_bin_2c")
    private Boolean selectBin2c;

    @ManyToOne
    @JoinColumn(name = "dx_id")
    private DX dx;

    @ManyToOne
    @JoinColumn(name = "status")
    private ClinBinStatusType status;

    @Column(name = "failure_message", length = 1023)
    private String failureMessage;

    @Column(name = "select_carrier")
    private Boolean selectCarrier;

    @Lob
    @Column(name = "vcf_file")
    private String vcfFile;

    public BinningJob() {
        super();
    }

    public Integer getBinningJobId() {
        return binningJobId;
    }

    public void setBinningJobId(Integer binningJobId) {
        this.binningJobId = binningJobId;
    }

    public Integer getIncidentalListVersion() {
        return incidentalListVersion;
    }

    public void setIncidentalListVersion(Integer incidentalListVersion) {
        this.incidentalListVersion = incidentalListVersion;
    }

    public Integer getDiagnosticListVersion() {
        return diagnosticListVersion;
    }

    public void setDiagnosticListVersion(Integer diagnosticListVersion) {
        this.diagnosticListVersion = diagnosticListVersion;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Date getBinStart() {
        return binStart;
    }

    public void setBinStart(Date binStart) {
        this.binStart = binStart;
    }

    public Date getBinStop() {
        return binStop;
    }

    public void setBinStop(Date binStop) {
        this.binStop = binStop;
    }

    public Boolean getSelectBin1() {
        return selectBin1;
    }

    public void setSelectBin1(Boolean selectBin1) {
        this.selectBin1 = selectBin1;
    }

    public Boolean getSelectBin2a() {
        return selectBin2a;
    }

    public void setSelectBin2a(Boolean selectBin2a) {
        this.selectBin2a = selectBin2a;
    }

    public Boolean getSelectBin2b() {
        return selectBin2b;
    }

    public void setSelectBin2b(Boolean selectBin2b) {
        this.selectBin2b = selectBin2b;
    }

    public Boolean getSelectBin2c() {
        return selectBin2c;
    }

    public void setSelectBin2c(Boolean selectBin2c) {
        this.selectBin2c = selectBin2c;
    }

    public DX getDx() {
        return dx;
    }

    public void setDx(DX dx) {
        this.dx = dx;
    }

    public ClinBinStatusType getStatus() {
        return status;
    }

    public void setStatus(ClinBinStatusType status) {
        this.status = status;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public Boolean getSelectCarrier() {
        return selectCarrier;
    }

    public void setSelectCarrier(Boolean selectCarrier) {
        this.selectCarrier = selectCarrier;
    }

    public String getVcfFile() {
        return vcfFile;
    }

    public void setVcfFile(String vcfFile) {
        this.vcfFile = vcfFile;
    }

    @Override
    public String toString() {
        return String.format(
                "BinningJob [binningJobId=%s, incidentalListVersion=%s, diagnosticListVersion=%s, participant=%s, binStart=%s, binStop=%s, selectBin1=%s, selectBin2a=%s, selectBin2b=%s, selectBin2c=%s, failureMessage=%s, selectCarrier=%s, vcfFile=%s]",
                binningJobId, incidentalListVersion, diagnosticListVersion, participant, binStart, binStop, selectBin1, selectBin2a,
                selectBin2b, selectBin2c, failureMessage, selectCarrier, vcfFile);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((binStart == null) ? 0 : binStart.hashCode());
        result = prime * result + ((binStop == null) ? 0 : binStop.hashCode());
        result = prime * result + ((binningJobId == null) ? 0 : binningJobId.hashCode());
        result = prime * result + ((diagnosticListVersion == null) ? 0 : diagnosticListVersion.hashCode());
        result = prime * result + ((failureMessage == null) ? 0 : failureMessage.hashCode());
        result = prime * result + ((incidentalListVersion == null) ? 0 : incidentalListVersion.hashCode());
        result = prime * result + ((participant == null) ? 0 : participant.hashCode());
        result = prime * result + ((selectBin1 == null) ? 0 : selectBin1.hashCode());
        result = prime * result + ((selectBin2a == null) ? 0 : selectBin2a.hashCode());
        result = prime * result + ((selectBin2b == null) ? 0 : selectBin2b.hashCode());
        result = prime * result + ((selectBin2c == null) ? 0 : selectBin2c.hashCode());
        result = prime * result + ((selectCarrier == null) ? 0 : selectCarrier.hashCode());
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
        BinningJob other = (BinningJob) obj;
        if (binStart == null) {
            if (other.binStart != null)
                return false;
        } else if (!binStart.equals(other.binStart))
            return false;
        if (binStop == null) {
            if (other.binStop != null)
                return false;
        } else if (!binStop.equals(other.binStop))
            return false;
        if (binningJobId == null) {
            if (other.binningJobId != null)
                return false;
        } else if (!binningJobId.equals(other.binningJobId))
            return false;
        if (diagnosticListVersion == null) {
            if (other.diagnosticListVersion != null)
                return false;
        } else if (!diagnosticListVersion.equals(other.diagnosticListVersion))
            return false;
        if (failureMessage == null) {
            if (other.failureMessage != null)
                return false;
        } else if (!failureMessage.equals(other.failureMessage))
            return false;
        if (incidentalListVersion == null) {
            if (other.incidentalListVersion != null)
                return false;
        } else if (!incidentalListVersion.equals(other.incidentalListVersion))
            return false;
        if (participant == null) {
            if (other.participant != null)
                return false;
        } else if (!participant.equals(other.participant))
            return false;
        if (selectBin1 == null) {
            if (other.selectBin1 != null)
                return false;
        } else if (!selectBin1.equals(other.selectBin1))
            return false;
        if (selectBin2a == null) {
            if (other.selectBin2a != null)
                return false;
        } else if (!selectBin2a.equals(other.selectBin2a))
            return false;
        if (selectBin2b == null) {
            if (other.selectBin2b != null)
                return false;
        } else if (!selectBin2b.equals(other.selectBin2b))
            return false;
        if (selectBin2c == null) {
            if (other.selectBin2c != null)
                return false;
        } else if (!selectBin2c.equals(other.selectBin2c))
            return false;
        if (selectCarrier == null) {
            if (other.selectCarrier != null)
                return false;
        } else if (!selectCarrier.equals(other.selectCarrier))
            return false;
        if (vcfFile == null) {
            if (other.vcfFile != null)
                return false;
        } else if (!vcfFile.equals(other.vcfFile))
            return false;
        return true;
    }

}
