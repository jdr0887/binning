package org.renci.binning.dao.dbsnp.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "dbsnp", name = "snp_hwprob")
public class SNPHWProb implements Persistable {

    private static final long serialVersionUID = 1983500438611675436L;

    @EmbeddedId
    private SNPHWProbPK key;

    @MapsId("snp")
    @ManyToOne
    @JoinColumn(name = "snp_id")
    private SNP snp;

    public SNPHWProb() {
        super();
    }

    public SNPHWProb(SNPHWProbPK key) {
        super();
        this.key = key;
    }

    public SNPHWProbPK getKey() {
        return key;
    }

    public void setKey(SNPHWProbPK key) {
        this.key = key;
    }

    public SNP getSnp() {
        return snp;
    }

    public void setSnp(SNP snp) {
        this.snp = snp;
    }

    @Override
    public String toString() {
        return String.format("SNPHWProb [key=%s, snp=%s]", key, snp);
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
        SNPHWProb other = (SNPHWProb) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
