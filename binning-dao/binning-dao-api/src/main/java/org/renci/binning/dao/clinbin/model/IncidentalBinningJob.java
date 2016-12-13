package org.renci.binning.dao.clinbin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.Assembly;

@Entity
@Table(schema = "clinbin", name = "incidental_binning_job")
public class IncidentalBinningJob implements Persistable {

    private static final long serialVersionUID = 4262905640217136616L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incidental_binning_job_binning_job_id_seq")
    @SequenceGenerator(schema = "clinbin", name = "incidental_binning_job_binning_job_id_seq", sequenceName = "incidental_binning_job_binning_job_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "binning_job_id")
    private Integer id;

    @Column(name = "incidental_list_version")
    private Integer listVersion;

    @Column(name = "participant", length = 50)
    private String participant;

    @Column(name = "bin_start")
    private Date start;

    @Column(name = "bin_stop")
    private Date stop;

    @Lob
    @Column(name = "gender")
    private String gender;

    @ManyToOne
    @JoinColumn(name = "incidental_bin_id")
    private IncidentalBinX incidentalBinX;

    @ManyToOne
    @JoinColumn(name = "status")
    private IncidentalStatusType status;

    @Column(name = "failure_message", length = 1023)
    private String failureMessage = "";

    @Lob
    @Column(name = "vcf_file")
    private String vcfFile = "";

    @Lob
    @Column(name = "study")
    private String study = "";

    @ManyToOne
    @JoinColumn(name = "asm_id")
    private Assembly assembly;

    public IncidentalBinningJob() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getListVersion() {
        return listVersion;
    }

    public void setListVersion(Integer listVersion) {
        this.listVersion = listVersion;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public IncidentalBinX getIncidentalBinX() {
        return incidentalBinX;
    }

    public void setIncidentalBinX(IncidentalBinX incidentalBinX) {
        this.incidentalBinX = incidentalBinX;
    }

    public IncidentalStatusType getStatus() {
        return status;
    }

    public void setStatus(IncidentalStatusType status) {
        this.status = status;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getVcfFile() {
        return vcfFile;
    }

    public void setVcfFile(String vcfFile) {
        this.vcfFile = vcfFile;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public Assembly getAssembly() {
        return assembly;
    }

    public void setAssembly(Assembly assembly) {
        this.assembly = assembly;
    }

    @Override
    public String toString() {
        return String.format(
                "IncidentalBinningJob [id=%s, listVersion=%s, participant=%s, start=%s, stop=%s, gender=%s, failureMessage=%s, vcfFile=%s, study=%s]",
                id, listVersion, participant, start, stop, gender, failureMessage, vcfFile, study);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((failureMessage == null) ? 0 : failureMessage.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((listVersion == null) ? 0 : listVersion.hashCode());
        result = prime * result + ((participant == null) ? 0 : participant.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + ((stop == null) ? 0 : stop.hashCode());
        result = prime * result + ((study == null) ? 0 : study.hashCode());
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
        IncidentalBinningJob other = (IncidentalBinningJob) obj;
        if (failureMessage == null) {
            if (other.failureMessage != null)
                return false;
        } else if (!failureMessage.equals(other.failureMessage))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (listVersion == null) {
            if (other.listVersion != null)
                return false;
        } else if (!listVersion.equals(other.listVersion))
            return false;
        if (participant == null) {
            if (other.participant != null)
                return false;
        } else if (!participant.equals(other.participant))
            return false;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        if (stop == null) {
            if (other.stop != null)
                return false;
        } else if (!stop.equals(other.stop))
            return false;
        if (study == null) {
            if (other.study != null)
                return false;
        } else if (!study.equals(other.study))
            return false;
        if (vcfFile == null) {
            if (other.vcfFile != null)
                return false;
        } else if (!vcfFile.equals(other.vcfFile))
            return false;
        return true;
    }

}
