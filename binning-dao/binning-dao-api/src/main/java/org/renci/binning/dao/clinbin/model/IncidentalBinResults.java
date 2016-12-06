package org.renci.binning.dao.clinbin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.annotation.model.AnnotationGene;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "clinbin", name = "incidental_bin_results")
public class IncidentalBinResults implements Persistable {

    private static final long serialVersionUID = -5498414203626044918L;

    @EmbeddedId
    private IncidentalBinResultsPK key;

    @MapsId("LocatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    @MapsId("zygosityMode")
    @ManyToOne
    @JoinColumn(name = "zygosity_mode")
    private ZygosityModeType zygosityMode;

    @Column(name = "bin_timestamp")
    private Date binTimestamp;

    @Column(name = "bin_name", length = 1024)
    private String binName;

    @ManyToOne
    @JoinColumn(name = "gene_id")
    private AnnotationGene gene;

    @Column(name = "disease")
    private String disease;

    @Column(name = "incidental_bin", length = 15)
    private String incidentalBin;

    public IncidentalBinResults() {
        super();
    }

    public IncidentalBinResultsPK getKey() {
        return key;
    }

    public void setKey(IncidentalBinResultsPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public Date getBinTimestamp() {
        return binTimestamp;
    }

    public void setBinTimestamp(Date binTimestamp) {
        this.binTimestamp = binTimestamp;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public AnnotationGene getGene() {
        return gene;
    }

    public void setGene(AnnotationGene gene) {
        this.gene = gene;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public ZygosityModeType getZygosityMode() {
        return zygosityMode;
    }

    public void setZygosityMode(ZygosityModeType zygosityMode) {
        this.zygosityMode = zygosityMode;
    }

    public String getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(String incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinResults [binTimestamp=%s, binName=%s, disease=%s, incidentalBin=%s]", binTimestamp, binName,
                disease, incidentalBin);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((binName == null) ? 0 : binName.hashCode());
        result = prime * result + ((binTimestamp == null) ? 0 : binTimestamp.hashCode());
        result = prime * result + ((disease == null) ? 0 : disease.hashCode());
        result = prime * result + ((incidentalBin == null) ? 0 : incidentalBin.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
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
        IncidentalBinResults other = (IncidentalBinResults) obj;
        if (binName == null) {
            if (other.binName != null)
                return false;
        } else if (!binName.equals(other.binName))
            return false;
        if (binTimestamp == null) {
            if (other.binTimestamp != null)
                return false;
        } else if (!binTimestamp.equals(other.binTimestamp))
            return false;
        if (disease == null) {
            if (other.disease != null)
                return false;
        } else if (!disease.equals(other.disease))
            return false;
        if (incidentalBin == null) {
            if (other.incidentalBin != null)
                return false;
        } else if (!incidentalBin.equals(other.incidentalBin))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
