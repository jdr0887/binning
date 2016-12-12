package org.renci.binning.dao.jpa.var;

import javax.inject.Singleton;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.ProjectDAO;
import org.renci.binning.dao.var.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional
@OsgiServiceProvider(classes = { ProjectDAO.class })
@javax.transaction.Transactional
@Singleton
public class ProjectDAOImpl extends BaseDAOImpl<Project, String> implements ProjectDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDAOImpl.class);

    public ProjectDAOImpl() {
        super();
    }

    @Override
    public Class<Project> getPersistentClass() {
        return Project.class;
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public synchronized String save(Project entity) throws BinningDAOException {
        logger.debug("ENTERING save(Project)");
        if (entity == null) {
            logger.error("entity is null");
            return null;
        }
        if (!getEntityManager().contains(entity) && entity.getName() != null) {
            entity = getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
        return entity.getName();
    }

    @org.springframework.transaction.annotation.Transactional
    @javax.transaction.Transactional
    @Override
    public void delete(Project entity) throws BinningDAOException {
        logger.debug("ENTERING delete(T)");
        Project foundEntity = getEntityManager().find(getPersistentClass(), entity.getName());
        getEntityManager().remove(foundEntity);
    }

}
