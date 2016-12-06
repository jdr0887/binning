package org.renci.binning.dao.omim.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Phenotype")
@XmlRootElement(name = "phenotype")
@Entity
@Table(schema = "omim", name = "phenotype")
public class Phenotype {

    @Column(name = "version")
    private Date version;

    @Id
    @Column(name = "omim_phenotype_id")
    private Integer omimPhenotypeId;

    @Column(name = "mixed")
    private Boolean mixed;

    @Lob
    @Column(name = "title")
    private String title;

    @Column(name = "otype")
    private Integer otype;

    public Phenotype() {
        super();
    }

}
