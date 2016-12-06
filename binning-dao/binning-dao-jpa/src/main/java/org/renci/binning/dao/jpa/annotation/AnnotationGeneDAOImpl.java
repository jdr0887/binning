package org.renci.binning.dao.jpa.annotation;

import org.renci.binning.dao.annotation.AnnotationGeneDAO;
import org.renci.binning.dao.annotation.model.AnnotationGene;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class AnnotationGeneDAOImpl extends BaseDAOImpl<AnnotationGene, Long> implements AnnotationGeneDAO {

    public AnnotationGeneDAOImpl() {
        super();
    }

    @Override
    public Class<AnnotationGene> getPersistentClass() {
        return AnnotationGene.class;
    }

}
