package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "max_freq_source")
public class MaxFrequencySource implements Persistable {

    private static final long serialVersionUID = 3941360567984045245L;

    @Id
    @Lob
    @Column(name = "source")
    private String source;

    public MaxFrequencySource() {
        super();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return String.format("MaxFreqSource [source=%s]", source);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        MaxFrequencySource other = (MaxFrequencySource) obj;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }

}
