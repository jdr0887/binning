package org.renci.binning.core.incidental;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonInclude(Include.NON_EMPTY)
@JsonRootName(value = "incidentalBinningJobInfo")
@XmlRootElement
public class IncidentalBinningJobInfo {

    private Integer id;

    private String participant;

    private String gender;

    private Integer incidentalBinId;

    private Integer listVersion;

    public IncidentalBinningJobInfo() {
        super();
    }

    public IncidentalBinningJobInfo(String participant, String gender, Integer incidentalBinId, Integer listVersion) {
        super();
        this.participant = participant;
        this.gender = gender;
        this.incidentalBinId = incidentalBinId;
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

    public Integer getIncidentalBinId() {
        return incidentalBinId;
    }

    public void setIncidentalBinId(Integer incidentalBinId) {
        this.incidentalBinId = incidentalBinId;
    }

    public Integer getListVersion() {
        return listVersion;
    }

    public void setListVersion(Integer listVersion) {
        this.listVersion = listVersion;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinningJobInfo [id=%s, participant=%s, gender=%s, incidentalBinId=%s, listVersion=%s]", id,
                participant, gender, incidentalBinId, listVersion);
    }

}
