package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class AnalysisClassPK implements Serializable {

    private static final long serialVersionUID = -5781318200771229922L;

    @Column(name = "analysis_class_id")
    private Integer id;

    @Lob
    @Column(name = "selected_class")
    private String selectedClass;

    @Lob
    @Column(name = "select_class_descr")
    private String selectClassDescription;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Lob
    @Column(name = "user_name")
    private String userName;

    @Column(name = "timestamp")
    private Date timestamp;

    @Lob
    @Column(name = "hgnc_gene")
    private String hgncGene;

    @Column(name = "dx_id")
    private Integer dx;

    public AnalysisClassPK() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public String getSelectClassDescription() {
        return selectClassDescription;
    }

    public void setSelectClassDescription(String selectClassDescription) {
        this.selectClassDescription = selectClassDescription;
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getHgncGene() {
        return hgncGene;
    }

    public void setHgncGene(String hgncGene) {
        this.hgncGene = hgncGene;
    }

    public Integer getDx() {
        return dx;
    }

    public void setDx(Integer dx) {
        this.dx = dx;
    }

    @Override
    public String toString() {
        return String.format(
                "AnalysisClassPK [id=%s, selectedClass=%s, selectClassDescription=%s, locatedVariant=%s, userName=%s, timestamp=%s, hgncGene=%s, dx=%s]",
                id, selectedClass, selectClassDescription, locatedVariant, userName, timestamp, hgncGene, dx);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dx == null) ? 0 : dx.hashCode());
        result = prime * result + ((hgncGene == null) ? 0 : hgncGene.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((selectClassDescription == null) ? 0 : selectClassDescription.hashCode());
        result = prime * result + ((selectedClass == null) ? 0 : selectedClass.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
        AnalysisClassPK other = (AnalysisClassPK) obj;
        if (dx == null) {
            if (other.dx != null)
                return false;
        } else if (!dx.equals(other.dx))
            return false;
        if (hgncGene == null) {
            if (other.hgncGene != null)
                return false;
        } else if (!hgncGene.equals(other.hgncGene))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (selectClassDescription == null) {
            if (other.selectClassDescription != null)
                return false;
        } else if (!selectClassDescription.equals(other.selectClassDescription))
            return false;
        if (selectedClass == null) {
            if (other.selectedClass != null)
                return false;
        } else if (!selectedClass.equals(other.selectedClass))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

}
