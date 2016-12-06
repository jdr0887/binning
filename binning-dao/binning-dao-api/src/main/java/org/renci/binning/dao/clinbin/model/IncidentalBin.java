package org.renci.binning.dao.clinbin.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "incidental_bin", uniqueConstraints = { @UniqueConstraint(columnNames = { "bin_version", "bin_name" }) })
public class IncidentalBin implements Persistable {

    private static final long serialVersionUID = 8180106062083474626L;

    @Id
    @Column(name = "incidental_bin_id")
    private Integer id;

    @Column(name = "bin_version")
    private Integer version;

    @Column(name = "bin_name", length = 1024)
    private String name;

    @ManyToOne
    @JoinColumn(name = "zygosity_mode")
    private ZygosityModeType zygosityMode;

    @ManyToOne
    @JoinColumn(name = "incidental_bin")
    private IncidentalBinType type;

    @OneToMany(mappedBy = "incidentalBin")
    private Set<IncidentalBinGroupVersionX> incidentalBinGroupVersions;

    public IncidentalBin() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<IncidentalBinGroupVersionX> getIncidentalBinGroupVersions() {
        return incidentalBinGroupVersions;
    }

    public void setIncidentalBinGroupVersions(Set<IncidentalBinGroupVersionX> incidentalBinGroupVersions) {
        this.incidentalBinGroupVersions = incidentalBinGroupVersions;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZygosityModeType getZygosityMode() {
        return zygosityMode;
    }

    public void setZygosityMode(ZygosityModeType zygosityMode) {
        this.zygosityMode = zygosityMode;
    }

    public IncidentalBinType getType() {
        return type;
    }

    public void setType(IncidentalBinType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("IncidentalBin [id=%s, version=%s, name=%s]", id, version, name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        IncidentalBin other = (IncidentalBin) obj;
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
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
