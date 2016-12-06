package org.renci.binning.dao.refseq.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "refseq", name = "feature_qualifiers")
public class FeatureQualifiers {

    @EmbeddedId
    private FeatureQualifiersPK key;

    public FeatureQualifiers() {
        super();
    }

    public FeatureQualifiersPK getKey() {
        return key;
    }

    public void setKey(FeatureQualifiersPK key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return String.format("FeatureQualifiers [key=%s]", key);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        FeatureQualifiers other = (FeatureQualifiers) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
