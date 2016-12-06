package org.renci.binning.dao.clinvar.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinvar", name = "assertion_ranking")
public class AssertionRanking implements Persistable {

    private static final long serialVersionUID = 7855405654995199169L;

    @EmbeddedId
    private AssertionRankingPK key;

    public AssertionRanking() {
        super();
    }

    public AssertionRankingPK getKey() {
        return key;
    }

    public void setKey(AssertionRankingPK key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return String.format("AssertionRanking [key=%s]", key);
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
        AssertionRanking other = (AssertionRanking) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
