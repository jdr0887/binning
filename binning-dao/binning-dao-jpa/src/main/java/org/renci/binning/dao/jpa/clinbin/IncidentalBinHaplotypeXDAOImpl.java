package org.renci.binning.dao.jpa.clinbin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.clinbin.IncidentalBinHaplotypeXDAO;
import org.renci.binning.dao.clinbin.model.HaplotypeX;
import org.renci.binning.dao.clinbin.model.HaplotypeX_;
import org.renci.binning.dao.clinbin.model.IncidentalBinHaplotypeX;
import org.renci.binning.dao.clinbin.model.IncidentalBinHaplotypeXPK;
import org.renci.binning.dao.clinbin.model.IncidentalBinHaplotypeXPK_;
import org.renci.binning.dao.clinbin.model.IncidentalBinHaplotypeX_;
import org.renci.binning.dao.clinbin.model.ZygosityModeType;
import org.renci.binning.dao.clinbin.model.ZygosityModeType_;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariantPK_;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.var.model.Assembly;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant;
import org.renci.binning.dao.var.model.AssemblyLocatedVariant_;
import org.renci.binning.dao.var.model.Assembly_;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { IncidentalBinHaplotypeXDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class IncidentalBinHaplotypeXDAOImpl extends BaseDAOImpl<IncidentalBinHaplotypeX, IncidentalBinHaplotypeXPK>
        implements IncidentalBinHaplotypeXDAO {

    private static final Logger logger = LoggerFactory.getLogger(IncidentalBinHaplotypeXDAOImpl.class);

    public IncidentalBinHaplotypeXDAOImpl() {
        super();
    }

    @Override
    public Class<IncidentalBinHaplotypeX> getPersistentClass() {
        return IncidentalBinHaplotypeX.class;
    }

    @Override
    public List<IncidentalBinHaplotypeX> findByIncidentalBinIdAndVersion(Integer id, Integer version) throws BinningDAOException {
        logger.debug("ENTERING findByIncidentalBinIdAndVersion(Integer, Integer)");
        List<IncidentalBinHaplotypeX> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IncidentalBinHaplotypeX> crit = critBuilder.createQuery(getPersistentClass());
            Root<IncidentalBinHaplotypeX> root = crit.from(getPersistentClass());

            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(critBuilder.equal(root.get(IncidentalBinHaplotypeX_.key).get(IncidentalBinHaplotypeXPK_.incidentalBin), id));
            predicates.add(critBuilder.equal(root.get(IncidentalBinHaplotypeX_.key).get(IncidentalBinHaplotypeXPK_.incidentalBinVersion),
                    version));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<IncidentalBinHaplotypeX> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<IncidentalBinHaplotypeX> findAll() throws BinningDAOException {
        logger.debug("ENTERING findAll()");
        List<IncidentalBinHaplotypeX> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IncidentalBinHaplotypeX> crit = critBuilder.createQuery(getPersistentClass());
            crit.from(getPersistentClass());
            TypedQuery<IncidentalBinHaplotypeX> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public List<IncidentalBinHaplotypeX> findByIncidentalBinIdAndVersionAndAssemblyIdAndHGMDVersionAndZygosityMode(Integer id,
            Integer version, Integer assemblyId, Integer hgmdVersion, List<String> zygosityModes) throws BinningDAOException {
        logger.debug("ENTERING findByIncidentalBinIdAndVersionAndAssemblyIdAndZygosityMode(Integer, Integer, Integer, List<String>)");
        List<IncidentalBinHaplotypeX> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IncidentalBinHaplotypeX> crit = critBuilder.createQuery(getPersistentClass());
            Root<IncidentalBinHaplotypeX> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            predicates.add(critBuilder.equal(root.get(IncidentalBinHaplotypeX_.key).get(IncidentalBinHaplotypeXPK_.incidentalBin), id));
            predicates.add(critBuilder.equal(root.get(IncidentalBinHaplotypeX_.key).get(IncidentalBinHaplotypeXPK_.incidentalBinVersion),
                    version));

            Join<IncidentalBinHaplotypeX, ZygosityModeType> incidentalBinHaplotypeXZygosityModeTypeJoin = root
                    .join(IncidentalBinHaplotypeX_.zygosityMode, JoinType.LEFT);
            predicates.add(incidentalBinHaplotypeXZygosityModeTypeJoin.get(ZygosityModeType_.name).in(zygosityModes));

            Join<HaplotypeX, LocatedVariant> haplotypeXLocatedVariantJoin = root.join(IncidentalBinHaplotypeX_.haplotype)
                    .join(HaplotypeX_.locatedVariant, JoinType.LEFT);

            Join<AssemblyLocatedVariant, Assembly> assemblyLocatedVariantAssemblyJoin = haplotypeXLocatedVariantJoin
                    .join(LocatedVariant_.assemblyLocatedVariants, JoinType.LEFT).join(AssemblyLocatedVariant_.assembly, JoinType.LEFT);
            predicates.add(critBuilder.equal(assemblyLocatedVariantAssemblyJoin.get(Assembly_.id), assemblyId));

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = haplotypeXLocatedVariantJoin
                    .join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<IncidentalBinHaplotypeX> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (

        Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
