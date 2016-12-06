package org.renci.binning.dao.var.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VariantSetLocationPK implements Serializable {

    private static final long serialVersionUID = -2850126342576220251L;

    @Column(name = "var_set_id")
    private Integer varSet;

    @Column(name = "ref_ver_accession")
    private String genomeRefSeq;

    @Column(name = "pos")
    private Integer pos;

    public VariantSetLocationPK() {
        super();
    }

    public Integer getVarSet() {
        return varSet;
    }

    public void setVarSet(Integer varSet) {
        this.varSet = varSet;
    }

    public String getGenomeRefSeq() {
        return genomeRefSeq;
    }

    public void setGenomeRefSeq(String genomeRefSeq) {
        this.genomeRefSeq = genomeRefSeq;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return String.format("VariantSetLocationPK [varSet=%s, genomeRefSeq=%s, pos=%s]", varSet, genomeRefSeq, pos);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genomeRefSeq == null) ? 0 : genomeRefSeq.hashCode());
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
        result = prime * result + ((varSet == null) ? 0 : varSet.hashCode());
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
        VariantSetLocationPK other = (VariantSetLocationPK) obj;
        if (genomeRefSeq == null) {
            if (other.genomeRefSeq != null)
                return false;
        } else if (!genomeRefSeq.equals(other.genomeRefSeq))
            return false;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        if (varSet == null) {
            if (other.varSet != null)
                return false;
        } else if (!varSet.equals(other.varSet))
            return false;
        return true;
    }

}
