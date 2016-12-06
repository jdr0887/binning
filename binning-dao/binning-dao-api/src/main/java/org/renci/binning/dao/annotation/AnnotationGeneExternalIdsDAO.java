package org.renci.binning.dao.annotation;

import java.util.List;

import org.renci.binning.dao.BaseDAO;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIds;
import org.renci.binning.dao.annotation.model.AnnotationGeneExternalIdsPK;

public interface AnnotationGeneExternalIdsDAO extends BaseDAO<AnnotationGeneExternalIds, AnnotationGeneExternalIdsPK> {

    public List<AnnotationGeneExternalIds> findByNamespace(String namespace) throws BinningDAOException;

    public List<AnnotationGeneExternalIds> findByNamespaceAndNamespaceVersion(String namespace, String version) throws BinningDAOException;

    public List<AnnotationGeneExternalIds> findByExternalId(Integer externalId) throws BinningDAOException;

}
