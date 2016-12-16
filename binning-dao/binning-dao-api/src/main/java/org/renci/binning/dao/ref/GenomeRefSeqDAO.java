package org.renci.binning.dao.ref;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.ref.model.GenomeRefSeq;

public interface GenomeRefSeqDAO extends BaseDAO<GenomeRefSeq, String> {

    public List<GenomeRefSeq> findAll() throws BinningDAOException;

    public List<GenomeRefSeq> findBySeqType(String seqType) throws BinningDAOException;

    public List<GenomeRefSeq> findByNameAndSourceAndContig(String name, String source, String contig) throws BinningDAOException;

    public List<GenomeRefSeq> findByVersionedAccession(String refVerAccession) throws BinningDAOException;

    public List<GenomeRefSeq> findByRefIdAndContigAndSeqType(Integer refId, String contig, String seqType) throws BinningDAOException;

    public List<GenomeRefSeq> findByRefIdAndContigAndSeqTypeAndAccessionPrefix(Integer refId, String contig, String seqType, String prefix)
            throws BinningDAOException;

    public String save(GenomeRefSeq genomeRef) throws BinningDAOException;

}
