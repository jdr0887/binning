package org.renci.binning.dao.refseq.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "refseq", name = "cds_ec_nums")
public class CodingSequenceEcNums {

    @Column(name = "refseq_cds_id", nullable = false)
    private RefSeqCodingSequence cds;

    @Id
    @Column(name = "ec_num", length = 31)
    private String ecNum;

    public CodingSequenceEcNums() {
        super();
    }

}
