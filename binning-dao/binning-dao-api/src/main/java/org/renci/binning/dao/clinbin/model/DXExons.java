package org.renci.binning.dao.clinbin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.Range;
import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "dx_exons", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "list_version", "transcr", "exon", "mapnum" }) })
public class DXExons implements Persistable {

    private static final long serialVersionUID = -1900172062196628883L;

    @Id
    @Column(name = "dx_exon_id")
    private Integer id;

    @Column(name = "list_version")
    private Integer listVersion;

    @ManyToOne
    @JoinColumn(name = "gene_id")
    private DiagnosticGene gene;

    @Column(name = "transcr", length = 100)
    private String transcr;

    @Column(name = "exon")
    private Integer exon;

    @Column(name = "chromosome", length = 100)
    private String chromosome;

    @Column(name = "interval_start")
    private Integer intervalStart;

    @Column(name = "interval_end")
    private Integer intervalEnd;

    @Column(name = "mapnum")
    private Integer mapNum;

    public DXExons() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getListVersion() {
        return listVersion;
    }

    public void setListVersion(Integer listVersion) {
        this.listVersion = listVersion;
    }

    public DiagnosticGene getGene() {
        return gene;
    }

    public void setGene(DiagnosticGene gene) {
        this.gene = gene;
    }

    public String getTranscr() {
        return transcr;
    }

    public void setTranscr(String transcr) {
        this.transcr = transcr;
    }

    public Integer getExon() {
        return exon;
    }

    public void setExon(Integer exon) {
        this.exon = exon;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Integer getIntervalStart() {
        return intervalStart;
    }

    public void setIntervalStart(Integer intervalStart) {
        this.intervalStart = intervalStart;
    }

    public Integer getIntervalEnd() {
        return intervalEnd;
    }

    public void setIntervalEnd(Integer intervalEnd) {
        this.intervalEnd = intervalEnd;
    }

    public Integer getMapNum() {
        return mapNum;
    }

    public void setMapNum(Integer mapNum) {
        this.mapNum = mapNum;
    }

    public Range<Integer> getIntervalRange() {
        return Range.between(this.intervalStart, this.intervalEnd);
    }

    @Override
    public String toString() {
        return String.format(
                "DXExons [id=%s, listVersion=%s, transcr=%s, exon=%s, chromosome=%s, intervalStart=%s, intervalEnd=%s, mapNum=%s]", id,
                listVersion, transcr, exon, chromosome, intervalStart, intervalEnd, mapNum);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chromosome == null) ? 0 : chromosome.hashCode());
        result = prime * result + ((exon == null) ? 0 : exon.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((intervalEnd == null) ? 0 : intervalEnd.hashCode());
        result = prime * result + ((intervalStart == null) ? 0 : intervalStart.hashCode());
        result = prime * result + ((listVersion == null) ? 0 : listVersion.hashCode());
        result = prime * result + ((mapNum == null) ? 0 : mapNum.hashCode());
        result = prime * result + ((transcr == null) ? 0 : transcr.hashCode());
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
        DXExons other = (DXExons) obj;
        if (chromosome == null) {
            if (other.chromosome != null)
                return false;
        } else if (!chromosome.equals(other.chromosome))
            return false;
        if (exon == null) {
            if (other.exon != null)
                return false;
        } else if (!exon.equals(other.exon))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (intervalEnd == null) {
            if (other.intervalEnd != null)
                return false;
        } else if (!intervalEnd.equals(other.intervalEnd))
            return false;
        if (intervalStart == null) {
            if (other.intervalStart != null)
                return false;
        } else if (!intervalStart.equals(other.intervalStart))
            return false;
        if (listVersion == null) {
            if (other.listVersion != null)
                return false;
        } else if (!listVersion.equals(other.listVersion))
            return false;
        if (mapNum == null) {
            if (other.mapNum != null)
                return false;
        } else if (!mapNum.equals(other.mapNum))
            return false;
        if (transcr == null) {
            if (other.transcr != null)
                return false;
        } else if (!transcr.equals(other.transcr))
            return false;
        return true;
    }

}
