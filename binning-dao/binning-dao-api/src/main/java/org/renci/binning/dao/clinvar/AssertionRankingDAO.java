package org.renci.binning.dao.clinvar;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinvar.model.AssertionRanking;
import org.renci.binning.dao.clinvar.model.AssertionRankingPK;

public interface AssertionRankingDAO extends BaseDAO<AssertionRanking, AssertionRankingPK> {

    public List<AssertionRanking> findByAssertion(String assertion) throws BinningDAOException;

}
