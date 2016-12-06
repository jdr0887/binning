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

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@Table(schema = "omim", name = "gene")
public class OMIMGene {

    @Id
    @Column(name = "omim_gene_id")
    private Integer omimGeneId;

    @Column(name = "version")
    private Date version;

    @Column(name = "mixed")
    private Boolean mixed;

    @Column(name = "otype")
    private Integer otype;

    @Lob
    @Column(name = "name")
    private String name;

    public OMIMGene() {
        super();
    }

}
