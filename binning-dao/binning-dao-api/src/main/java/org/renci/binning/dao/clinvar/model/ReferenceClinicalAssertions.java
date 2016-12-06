package org.renci.binning.dao.clinvar.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.var.model.LocatedVariant;

@Entity
@Table(schema = "clinvar", name = "reference_clinical_assertions")
public class ReferenceClinicalAssertions implements Persistable {

    private static final long serialVersionUID = 1918641526123264979L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reference_clinical_assertions_assertion_id_seq")
    @SequenceGenerator(schema = "clinvar", name = "reference_clinical_assertions_assertion_id_seq", sequenceName = "reference_clinical_assertions_assertion_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "assertion_id")
    private Long id;

    @Column(name = "accession", length = 20)
    private String accession;

    @Column(name = "version")
    private Integer version;

    @Column(name = "date_created")
    private Date created;

    @Column(name = "date_updated")
    private Date updated;

    @Column(name = "record_status", length = 20)
    private String recordStatus;

    @Column(name = "assertion_status", length = 100)
    private String assertionStatus;

    @Column(name = "assertion", length = 100)
    private String assertion;

    @Column(name = "assertion_type", length = 100)
    private String assertionType;

    @ManyToOne
    @JoinColumn(name = "loc_var_id", columnDefinition = "int4")
    private LocatedVariant locatedVariant;

    @ManyToOne
    @JoinColumn(name = "trait_set_id")
    private TraitSets traitSet;

    @ManyToMany(targetEntity = Versions.class, fetch = FetchType.LAZY)
    @JoinTable(schema = "clinvar", name = "version_accession_map", joinColumns = @JoinColumn(name = "clinvar_ref_assertion_id", columnDefinition = "int4"), inverseJoinColumns = @JoinColumn(name = "clinvar_version_id", columnDefinition = "int4"))
    private Set<Versions> versions;

    public ReferenceClinicalAssertions() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getAssertionStatus() {
        return assertionStatus;
    }

    public void setAssertionStatus(String assertionStatus) {
        this.assertionStatus = assertionStatus;
    }

    public String getAssertion() {
        return assertion;
    }

    public void setAssertion(String assertion) {
        this.assertion = assertion;
    }

    public String getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(String assertionType) {
        this.assertionType = assertionType;
    }

    public LocatedVariant getLocatedVariant() {
        return locatedVariant;
    }

    public void setLocatedVariant(LocatedVariant locatedVariant) {
        this.locatedVariant = locatedVariant;
    }

    public TraitSets getTraitSet() {
        return traitSet;
    }

    public void setTraitSet(TraitSets traitSet) {
        this.traitSet = traitSet;
    }

    public Set<Versions> getVersions() {
        return versions;
    }

    public void setVersions(Set<Versions> versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return String.format(
                "ReferenceClinicalAssertions [id=%s, accession=%s, version=%s, created=%s, updated=%s, recordStatus=%s, assertionStatus=%s, assertion=%s, assertionType=%s]",
                id, accession, version, created, updated, recordStatus, assertionStatus, assertion, assertionType);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accession == null) ? 0 : accession.hashCode());
        result = prime * result + ((assertion == null) ? 0 : assertion.hashCode());
        result = prime * result + ((assertionStatus == null) ? 0 : assertionStatus.hashCode());
        result = prime * result + ((assertionType == null) ? 0 : assertionType.hashCode());
        result = prime * result + ((created == null) ? 0 : created.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
        result = prime * result + ((updated == null) ? 0 : updated.hashCode());
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
        ReferenceClinicalAssertions other = (ReferenceClinicalAssertions) obj;
        if (accession == null) {
            if (other.accession != null)
                return false;
        } else if (!accession.equals(other.accession))
            return false;
        if (assertion == null) {
            if (other.assertion != null)
                return false;
        } else if (!assertion.equals(other.assertion))
            return false;
        if (assertionStatus == null) {
            if (other.assertionStatus != null)
                return false;
        } else if (!assertionStatus.equals(other.assertionStatus))
            return false;
        if (assertionType == null) {
            if (other.assertionType != null)
                return false;
        } else if (!assertionType.equals(other.assertionType))
            return false;
        if (created == null) {
            if (other.created != null)
                return false;
        } else if (!created.equals(other.created))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (recordStatus == null) {
            if (other.recordStatus != null)
                return false;
        } else if (!recordStatus.equals(other.recordStatus))
            return false;
        if (updated == null) {
            if (other.updated != null)
                return false;
        } else if (!updated.equals(other.updated))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
