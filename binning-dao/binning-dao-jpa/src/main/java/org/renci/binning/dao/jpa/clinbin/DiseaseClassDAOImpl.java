package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.DiseaseClassDAO;
import org.renci.binning.dao.clinbin.model.DiseaseClass;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class DiseaseClassDAOImpl extends BaseDAOImpl<DiseaseClass, Integer> implements DiseaseClassDAO {

    private static final Logger logger = LoggerFactory.getLogger(DiseaseClassDAOImpl.class);

    public DiseaseClassDAOImpl() {
        super();
    }

    @Override
    public Class<DiseaseClass> getPersistentClass() {
        return DiseaseClass.class;
    }

    @Override
    public List<DiseaseClass> findAll() throws BinningDAOException {
        logger.debug("ENTERING findAll()");
        List<DiseaseClass> ret = new ArrayList<>();
        CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DiseaseClass> crit = critBuilder.createQuery(getPersistentClass());
        crit.distinct(true);
        TypedQuery<DiseaseClass> query = getEntityManager().createQuery(crit);
        ret.addAll(query.getResultList());
        return ret;
    }

}
