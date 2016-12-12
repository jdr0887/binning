package org.renci.binning.dao.jpa.clinbin;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.NCGenesFrequenciesDAO;
import org.renci.binning.dao.clinbin.model.NCGenesFrequencies;
import org.renci.binning.dao.clinbin.model.NCGenesFrequenciesPK;
import org.renci.binning.dao.clinbin.model.NCGenesFrequenciesPK_;
import org.renci.binning.dao.clinbin.model.NCGenesFrequencies_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { NCGenesFrequenciesDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class NCGenesFrequenciesDAOImpl extends BaseDAOImpl<NCGenesFrequencies, NCGenesFrequenciesPK> implements NCGenesFrequenciesDAO {

    private static final Logger logger = LoggerFactory.getLogger(NCGenesFrequenciesDAOImpl.class);

    public NCGenesFrequenciesDAOImpl() {
        super();
    }

    @Override
    public Class<NCGenesFrequencies> getPersistentClass() {
        return NCGenesFrequencies.class;
    }

    @Override
    public Integer findMaxVersion() throws BinningDAOException {
        logger.debug("ENTERING findMaxVersion()");
        Integer ret = null;
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Integer> crit = critBuilder.createQuery(Integer.class);
            Root<NCGenesFrequencies> root = crit.from(getPersistentClass());
            crit.select(critBuilder.max(critBuilder.function("varchar_to_int", Integer.class,
                    root.get(NCGenesFrequencies_.key).get(NCGenesFrequenciesPK_.version))));
            TypedQuery<Integer> query = getEntityManager().createQuery(crit);
            ret = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
