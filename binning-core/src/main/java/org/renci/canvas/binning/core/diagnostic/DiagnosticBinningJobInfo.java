package org.renci.canvas.binning.core.diagnostic;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonInclude(Include.NON_EMPTY)
@JsonRootName(value = "diagnosticBinningJobInfo")
@XmlRootElement
public class DiagnosticBinningJobInfo {

    private Integer id;

    private String participant;

    private String gender;

    private Integer dxId;

    private Integer listVersion;

    public DiagnosticBinningJobInfo() {
        super();
    }

    public DiagnosticBinningJobInfo(String participant, String gender, Integer dxId, Integer listVersion) {
        super();
        this.participant = participant;
        this.gender = gender;
        this.dxId = dxId;
        this.listVersion = listVersion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getDxId() {
        return dxId;
    }

    public void setDxId(Integer dxId) {
        this.dxId = dxId;
    }

    public Integer getListVersion() {
        return listVersion;
    }

    public void setListVersion(Integer listVersion) {
        this.listVersion = listVersion;
    }

    @Override
    public String toString() {
        return String.format("DiagnosticBinningJobInfo [participant=%s, gender=%s, dxId=%s, listVersion=%s]", participant, gender, dxId,
                listVersion);
    }

}
