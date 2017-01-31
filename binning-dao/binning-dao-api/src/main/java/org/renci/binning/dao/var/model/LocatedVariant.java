package org.renci.binning.dao.var.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.Range;
import org.apache.openjpa.persistence.FetchAttribute;
import org.apache.openjpa.persistence.FetchGroup;
import org.apache.openjpa.persistence.FetchGroups;
import org.renci.binning.dao.Persistable;
import org.renci.binning.dao.clinbin.model.MaxFrequency;
import org.renci.binning.dao.exac.model.MaxVariantFrequency;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.binning.dao.ref.model.GenomeRef;
import org.renci.binning.dao.ref.model.GenomeRefSeq;
import org.renci.binning.dao.refseq.model.Variants_61_2;

@Entity
@Table(schema = "var", name = "loc_var", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "loc_var_id", "ref_id", "ref_ver_accession", "pos", "type", "seq", "end_pos" }) })
@FetchGroups({
        @FetchGroup(name = "includeManyToOnes", attributes = { @FetchAttribute(name = "genomeRef"), @FetchAttribute(name = "genomeRefSeq"),
                @FetchAttribute(name = "variantType") }),
        @FetchGroup(name = "includeVariants_61_2", fetchGroups = { "includeManyToOnes" }, attributes = {
                @FetchAttribute(name = "variants_61_2") }) })
public class LocatedVariant implements Persistable {

    private static final long serialVersionUID = 3259272023352164114L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loc_var_loc_var_id_seq")
    @SequenceGenerator(schema = "var", name = "loc_var_loc_var_id_seq", sequenceName = "loc_var_loc_var_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "loc_var_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ref_id")
    private GenomeRef genomeRef;

    @ManyToOne
    @JoinColumn(name = "ref_ver_accession")
    private GenomeRefSeq genomeRefSeq;

    @Column(name = "pos")
    private Integer position;

    @Column(name = "end_pos")
    private Integer endPosition;

    @ManyToOne
    @JoinColumn(name = "type")
    private VariantType variantType;

    @Lob
    @Column(name = "ref")
    private String ref;

    @Column(name = "seq", length = 65535)
    private String seq;

    @OneToMany(mappedBy = "locatedVariant", fetch = FetchType.LAZY)
    private List<AssemblyLocatedVariant> assemblyLocatedVariants;

    @OneToMany(mappedBy = "locatedVariant", fetch = FetchType.LAZY)
    private List<AssemblyLocatedVariantQC> assemblyLocatedVariantQCs;

    @OneToMany(mappedBy = "locatedVariant", fetch = FetchType.LAZY)
    private List<MaxVariantFrequency> maxVariantFrequencies;

    @OneToMany(mappedBy = "locatedVariant", fetch = FetchType.LAZY)
    private List<MaxFrequency> maxFreqs;

    @OneToMany(mappedBy = "locatedVariant", fetch = FetchType.LAZY)
    private List<Variants_61_2> variants_61_2;

    @OneToMany(mappedBy = "locatedVariant", fetch = FetchType.LAZY)
    private List<HGMDLocatedVariant> hgmdLocatedVariants;

    @ManyToMany(mappedBy = "locatedVariants")
    private Set<CanonicalAllele> canonicalAlleles;

    public LocatedVariant() {
        super();
    }

    public LocatedVariant(GenomeRef genomeRef, GenomeRefSeq genomeRefSeq) {
        super();
        this.genomeRef = genomeRef;
        this.genomeRefSeq = genomeRefSeq;
    }

    public LocatedVariant(GenomeRef genomeRef, GenomeRefSeq genomeRefSeq, Integer position, Integer endPosition, VariantType variantType,
            String ref, String seq) {
        super();
        this.genomeRef = genomeRef;
        this.genomeRefSeq = genomeRefSeq;
        this.position = position;
        this.ref = ref;
        this.endPosition = endPosition;
        this.variantType = variantType;
        this.seq = seq;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenomeRef getGenomeRef() {
        return genomeRef;
    }

    public void setGenomeRef(GenomeRef genomeRef) {
        this.genomeRef = genomeRef;
    }

    public GenomeRefSeq getGenomeRefSeq() {
        return genomeRefSeq;
    }

    public void setGenomeRefSeq(GenomeRefSeq genomeRefSeq) {
        this.genomeRefSeq = genomeRefSeq;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Integer getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }

    public VariantType getVariantType() {
        return variantType;
    }

    public void setVariantType(VariantType variantType) {
        this.variantType = variantType;
    }

    public List<AssemblyLocatedVariantQC> getAssemblyLocatedVariantQCs() {
        return assemblyLocatedVariantQCs;
    }

    public void setAssemblyLocatedVariantQCs(List<AssemblyLocatedVariantQC> assemblyLocatedVariantQCs) {
        this.assemblyLocatedVariantQCs = assemblyLocatedVariantQCs;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public List<MaxVariantFrequency> getMaxVariantFrequencies() {
        return maxVariantFrequencies;
    }

    public void setMaxVariantFrequencies(List<MaxVariantFrequency> maxVariantFrequencies) {
        this.maxVariantFrequencies = maxVariantFrequencies;
    }

    public List<MaxFrequency> getMaxFreqs() {
        return maxFreqs;
    }

    public void setMaxFreqs(List<MaxFrequency> maxFreqs) {
        this.maxFreqs = maxFreqs;
    }

    public List<Variants_61_2> getVariants_61_2() {
        return variants_61_2;
    }

    public void setVariants_61_2(List<Variants_61_2> variants_61_2) {
        this.variants_61_2 = variants_61_2;
    }

    public List<AssemblyLocatedVariant> getAssemblyLocatedVariants() {
        return assemblyLocatedVariants;
    }

    public void setAssemblyLocatedVariants(List<AssemblyLocatedVariant> assemblyLocatedVariants) {
        this.assemblyLocatedVariants = assemblyLocatedVariants;
    }

    public List<HGMDLocatedVariant> getHgmdLocatedVariants() {
        return hgmdLocatedVariants;
    }

    public void setHgmdLocatedVariants(List<HGMDLocatedVariant> hgmdLocatedVariants) {
        this.hgmdLocatedVariants = hgmdLocatedVariants;
    }

    public Set<CanonicalAllele> getCanonicalAlleles() {
        return canonicalAlleles;
    }

    public void setCanonicalAlleles(Set<CanonicalAllele> canonicalAlleles) {
        this.canonicalAlleles = canonicalAlleles;
    }

    @Override
    public String toString() {
        return String.format("LocatedVariant [id=%s, position=%s, ref=%s, endPosition=%s, seq=%s, variantType=%s]", id, position, ref,
                endPosition, seq, variantType != null ? variantType.toString() : "");
    }

    public Range<Integer> toRange() {
        return Range.between(this.position, this.endPosition);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endPosition == null) ? 0 : endPosition.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((ref == null) ? 0 : ref.hashCode());
        result = prime * result + ((seq == null) ? 0 : seq.hashCode());
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
        LocatedVariant other = (LocatedVariant) obj;
        if (endPosition == null) {
            if (other.endPosition != null)
                return false;
        } else if (!endPosition.equals(other.endPosition))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (ref == null) {
            if (other.ref != null)
                return false;
        } else if (!ref.equals(other.ref))
            return false;
        if (seq == null) {
            if (other.seq != null)
                return false;
        } else if (!seq.equals(other.seq))
            return false;
        return true;
    }

}
