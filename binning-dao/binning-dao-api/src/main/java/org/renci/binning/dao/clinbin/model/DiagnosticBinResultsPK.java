package org.renci.binning.dao.clinbin.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DiagnosticBinResultsPK implements Serializable {

    private static final long serialVersionUID = -4066677132203326435L;

    @Column(name = "loc_var_id")
    private Long locatedVariant;

    @Column(name = "bin_timestamp")
    private Date binTimestamp;

    @Column(name = "bin_version")
    private Integer binVersion;

    @Column(name = "dx_id")
    private Integer dx;

    @Column(name = "gene_id")
    private Integer geneId;

    @Column(name = "class_id")
    private Integer diseaseClass;

    @Column(name = "transcr", length = 31)
    private String transcr;

    @Column(name = "mapnum")
    private Integer mapnum;

    public DiagnosticBinResultsPK() {
        super();
    }

    public Long getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(Long locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public Date getBinTimestamp() {
        return binTimestamp;
    }

    public void setBinTimestamp(Date binTimestamp) {
        this.binTimestamp = binTimestamp;
    }

    public Integer getBinVersion() {
        return binVersion;
    }

    public void setBinVersion(Integer binVersion) {
        this.binVersion = binVersion;
    }

    public Integer getDx() {
        return dx;
    }

    public void setDx(Integer dx) {
        this.dx = dx;
    }

    public Integer getGeneId() {
        return geneId;
    }

    public void setGeneId(Integer geneId) {
        this.geneId = geneId;
    }

    public Integer getDiseaseClass() {
        return diseaseClass;
    }

    public void setDiseaseClass(Integer diseaseClass) {
        this.diseaseClass = diseaseClass;
    }

    public String getTranscr() {
        return transcr;
    }

    public void setTranscr(String transcr) {
        this.transcr = transcr;
    }

    public Integer getMapnum() {
        return mapnum;
    }

    public void setMapnum(Integer mapnum) {
        this.mapnum = mapnum;
    }

    @Override
    public String toString() {
        return String.format(
                "DiagnosticBinResultsPK [locatedVariant=%s, binTimestamp=%s, binVersion=%s, dx=%s, geneId=%s, diseaseClass=%s, transcr=%s, mapnum=%s]",
                locatedVariant, binTimestamp, binVersion, dx, geneId, diseaseClass, transcr, mapnum);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((binTimestamp == null) ? 0 : binTimestamp.hashCode());
        result = prime * result + ((binVersion == null) ? 0 : binVersion.hashCode());
        result = prime * result + ((diseaseClass == null) ? 0 : diseaseClass.hashCode());
        result = prime * result + ((dx == null) ? 0 : dx.hashCode());
        result = prime * result + ((geneId == null) ? 0 : geneId.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
        result = prime * result + ((mapnum == null) ? 0 : mapnum.hashCode());
        result = prime * result + ((transcr == null) ? 0 : transcr.hashCode());
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
        DiagnosticBinResultsPK other = (DiagnosticBinResultsPK) obj;
        if (binTimestamp == null) {
            if (other.binTimestamp != null)
                return false;
        } else if (!binTimestamp.equals(other.binTimestamp))
            return false;
        if (binVersion == null) {
            if (other.binVersion != null)
                return false;
        } else if (!binVersion.equals(other.binVersion))
            return false;
        if (diseaseClass == null) {
            if (other.diseaseClass != null)
                return false;
        } else if (!diseaseClass.equals(other.diseaseClass))
            return false;
        if (dx == null) {
            if (other.dx != null)
                return false;
        } else if (!dx.equals(other.dx))
            return false;
        if (geneId == null) {
            if (other.geneId != null)
                return false;
        } else if (!geneId.equals(other.geneId))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        if (mapnum == null) {
            if (other.mapnum != null)
                return false;
        } else if (!mapnum.equals(other.mapnum))
            return false;
        if (transcr == null) {
            if (other.transcr != null)
                return false;
        } else if (!transcr.equals(other.transcr))
            return false;
        return true;
    }

}
