package org.renci.binning.dao.jpa.clinbin;

import org.renci.binning.dao.clinbin.UnimportantExonDAO;
import org.renci.binning.dao.clinbin.model.UnimportantExon;
import org.renci.binning.dao.clinbin.model.UnimportantExonPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class UnimportantExonDAOImpl extends BaseDAOImpl<UnimportantExon, UnimportantExonPK> implements UnimportantExonDAO {

    private static final Logger logger = LoggerFactory.getLogger(UnimportantExonDAOImpl.class);

    public UnimportantExonDAOImpl() {
        super();
    }

    @Override
    public Class<UnimportantExon> getPersistentClass() {
        return UnimportantExon.class;
    }

}
