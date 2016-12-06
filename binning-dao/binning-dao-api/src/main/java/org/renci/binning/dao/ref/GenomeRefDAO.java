package org.renci.binning.dao.ref;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.ref.model.GenomeRef;

public interface GenomeRefDAO extends BaseDAO<GenomeRef, Integer> {

    public List<GenomeRef> findAll() throws BinningDAOException;

    public List<GenomeRef> findByName(String name) throws BinningDAOException;

    public List<GenomeRef> findByNameAndSource(String name, String source) throws BinningDAOException;

    public List<GenomeRef> findBySeqTypeAndContig(String seqType, String contig) throws BinningDAOException;

    public List<GenomeRef> findByGenomeRefSeqVersionAccession(String versionAccession) throws BinningDAOException;

    public Integer save(GenomeRef genomeRef) throws BinningDAOException;

}
