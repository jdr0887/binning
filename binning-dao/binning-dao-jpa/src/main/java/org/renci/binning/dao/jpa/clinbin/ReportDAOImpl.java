package org.renci.binning.dao.jpa.clinbin;

import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.ReportDAO;
import org.renci.binning.dao.clinbin.model.Report;
import org.renci.binning.dao.clinbin.model.ReportPK;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class ReportDAOImpl extends BaseDAOImpl<Report, ReportPK> implements ReportDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReportDAOImpl.class);

    public ReportDAOImpl() {
        super();
    }

    @Override
    public Class<Report> getPersistentClass() {
        return Report.class;
    }

    @Override
    @Transactional
    public ReportPK save(Report entity) throws BinningDAOException {
        logger.debug("ENTERING save(Report)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getKey() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getKey();
    }

}
