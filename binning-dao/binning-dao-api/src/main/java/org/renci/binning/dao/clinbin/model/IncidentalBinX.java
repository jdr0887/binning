package org.renci.binning.dao.clinbin.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "incidental_binx", uniqueConstraints = { @UniqueConstraint(columnNames = { "bin_name" }) })
@FetchGroups({ @FetchGroup(name = "includeAll", attributes = { @FetchAttribute(name = "incidentalBinHaplotypes") }) })
public class IncidentalBinX implements Persistable {

    private static final long serialVersionUID = -3352324651632335173L;

    @Id
    @Column(name = "incidental_bin_id")
    private Integer id;

    @Column(name = "bin_name", length = 1024)
    private String name;

    @OneToMany(mappedBy = "incidentalBin", fetch = FetchType.LAZY)
    private List<IncidentalBinHaplotypeX> incidentalBinHaplotypes;

    @OneToMany(mappedBy = "incidentalBin", fetch = FetchType.LAZY)
    private List<IncidentalBinGeneX> incidentalBinGenes;

    public IncidentalBinX() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IncidentalBinGeneX> getIncidentalBinGenes() {
        return incidentalBinGenes;
    }

    public void setIncidentalBinGenes(List<IncidentalBinGeneX> incidentalBinGenes) {
        this.incidentalBinGenes = incidentalBinGenes;
    }

    public List<IncidentalBinHaplotypeX> getIncidentalBinHaplotypes() {
        return incidentalBinHaplotypes;
    }

    public void setIncidentalBinHaplotypes(List<IncidentalBinHaplotypeX> incidentalBinHaplotypes) {
        this.incidentalBinHaplotypes = incidentalBinHaplotypes;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBinX [id=%s, name=%s]", id, name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        IncidentalBinX other = (IncidentalBinX) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
