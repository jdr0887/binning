package org.renci.binning.dao.jpa.annotation;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.annotation.AnnotationGeneDAO;
import org.renci.binning.dao.annotation.model.AnnotationGene;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { AnnotationGeneDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class AnnotationGeneDAOImpl extends BaseDAOImpl<AnnotationGene, Long> implements AnnotationGeneDAO {

    public AnnotationGeneDAOImpl() {
        super();
    }

    @Override
    public Class<AnnotationGene> getPersistentClass() {
        return AnnotationGene.class;
    }

}
