package org.renci.binning.dao.clinbin.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "incidental_bin_haplotypex")
public class IncidentalBinHaplotypeX implements Persistable {

    private static final long serialVersionUID = -3352324651632335173L;

    @EmbeddedId
    private IncidentalBinHaplotypeXPK key;

    @MapsId("incidentalBin")
    @ManyToOne
    @JoinColumn(name = "incidental_bin_id")
    private IncidentalBinX incidentalBin;

    @MapsId("haplotype")
    @ManyToOne
    @JoinColumn(name = "haplotype_id")
    private HaplotypeX haplotype;

    @MapsId("phenotype")
    @ManyToOne
    @JoinColumn(name = "phenotype_id")
    private PhenotypeX phenotype;

    @ManyToOne
    @JoinColumn(name = "zygosity_mode")
    private ZygosityModeType zygosityMode;

    public IncidentalBinHaplotypeX() {
        super();
    }

    public IncidentalBinHaplotypeX(IncidentalBinHaplotypeXPK key) {
        super();
        this.key = key;
    }

    public IncidentalBinHaplotypeXPK getKey() {
        return key;
    }

    public void setKey(IncidentalBinHaplotypeXPK key) {
        this.key = key;
    }

    public IncidentalBinX getIncidentalBin() {
        return incidentalBin;
    }

    public void setIncidentalBin(IncidentalBinX incidentalBin) {
        this.incidentalBin = incidentalBin;
    }

    public HaplotypeX getHaplotype() {
        return haplotype;
    }

    public void setHaplotype(HaplotypeX haplotype) {
        this.haplotype = haplotype;
    }

    public PhenotypeX getPhenotype() {
        return phenotype;
    }

    public void setPhenotype(PhenotypeX phenotype) {
        this.phenotype = phenotype;
    }

    public ZygosityModeType getZygosityMode() {
        return zygosityMode;
    }

    public void setZygosityMode(ZygosityModeType zygosityMode) {
        this.zygosityMode = zygosityMode;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinHaplotypeX [key=%s, zygosityMode=%s]", key, zygosityMode);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((zygosityMode == null) ? 0 : zygosityMode.hashCode());
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
        IncidentalBinHaplotypeX other = (IncidentalBinHaplotypeX) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (zygosityMode == null) {
            if (other.zygosityMode != null)
                return false;
        } else if (!zygosityMode.equals(other.zygosityMode))
            return false;
        return true;
    }

}
