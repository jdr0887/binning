package org.renci.binning.dao.clinbin.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.renci.binning.dao.Persistable;

@Entity
@Table(schema = "clinbin", name = "dx", uniqueConstraints = { @UniqueConstraint(columnNames = { "dx_name" }) })
@Cacheable
public class DX implements Persistable {

    private static final long serialVersionUID = 7208210752522886524L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dx_dx_id_seq")
    @SequenceGenerator(schema = "clinbin", name = "dx_dx_id_seq", sequenceName = "dx_dx_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "dx_id")
    private Integer id;

    @Column(name = "dx_name", length = 1024)
    private String name;

    @OneToMany(mappedBy = "dx", fetch = FetchType.LAZY)
    private List<DiagnosticBinningJob> diagnosticBinningJobSet;

    public DX() {
        super();
    }

    public List<DiagnosticBinningJob> getDiagnosticBinningJobSet() {
        return diagnosticBinningJobSet;
    }

    public void setDiagnosticBinningJobSet(List<DiagnosticBinningJob> diagnosticBinningJobSet) {
        this.diagnosticBinningJobSet = diagnosticBinningJobSet;
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

    @Override
    public String toString() {
        return String.format("DX [id=%s, name=%s]", id, name);
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
        DX other = (DX) obj;
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
