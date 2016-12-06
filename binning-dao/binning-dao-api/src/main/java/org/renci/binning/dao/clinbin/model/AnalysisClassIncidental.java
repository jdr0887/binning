package org.renci.binning.dao.clinbin.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "clinbin", name = "analysis_class_incidental")
public class AnalysisClassIncidental implements Persistable {

    private static final long serialVersionUID = -4523788379740204587L;

    @EmbeddedId
    private AnalysisClassIncidentalPK key;

    @MapsId("LocatedVariant")
    @ManyToOne
    @JoinColumn(name = "loc_var_id")
    private LocatedVariant locatedVariant;

    public AnalysisClassIncidental() {
        super();
    }

    public AnalysisClassIncidentalPK getKey() {
        return key;
    }

    public void setKey(AnalysisClassIncidentalPK key) {
        this.key = key;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    @Override
    public String toString() {
        return String.format("AnalysisClassIncidental [key=%s, locatedVariant=%s]", key, locatedVariant);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((locatedVariant == null) ? 0 : locatedVariant.hashCode());
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
        AnalysisClassIncidental other = (AnalysisClassIncidental) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (locatedVariant == null) {
            if (other.locatedVariant != null)
                return false;
        } else if (!locatedVariant.equals(other.locatedVariant))
            return false;
        return true;
    }

}
