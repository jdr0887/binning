package org.renci.binning.dao.clinbin.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "bin_gene_symbol")
public class BinGeneSymbol implements Persistable {

    private static final long serialVersionUID = -2225220140087770254L;

    @EmbeddedId
    private BinGeneSymbolPK key;

    public BinGeneSymbol() {
        super();
    }

    public BinGeneSymbolPK getKey() {
        return key;
    }

    public void setKey(BinGeneSymbolPK key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return String.format("BinGeneSymbol [key=%s]", key);
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
        BinGeneSymbol other = (BinGeneSymbol) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

}
