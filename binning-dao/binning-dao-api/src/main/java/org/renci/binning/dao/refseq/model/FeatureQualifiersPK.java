package org.renci.binning.dao.refseq.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FeatureQualifiersPK implements Serializable {

    private static final long serialVersionUID = 572839748626093266L;

    @Column(name = "refseq_feature_id")
    private Integer refseqFeatureId;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    public FeatureQualifiersPK() {
        super();
    }

    public Integer getRefseqFeatureId() {
        return refseqFeatureId;
    }

    public void setRefseqFeatureId(Integer refseqFeatureId) {
        this.refseqFeatureId = refseqFeatureId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return String.format("FeatureQualifiersPK [refseqFeatureId=%s, key=%s, value=%s]", refseqFeatureId, key, value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((refseqFeatureId == null) ? 0 : refseqFeatureId.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        FeatureQualifiersPK other = (FeatureQualifiersPK) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (refseqFeatureId == null) {
            if (other.refseqFeatureId != null)
                return false;
        } else if (!refseqFeatureId.equals(other.refseqFeatureId))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
