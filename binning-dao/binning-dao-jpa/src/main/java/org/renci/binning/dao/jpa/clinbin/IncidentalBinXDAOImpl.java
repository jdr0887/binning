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
import org.renci.binning.dao.annotation.model.AnnotationGene;
import org.renci.binning.dao.annotation.model.AnnotationGene_;
import org.renci.binning.dao.clinbin.IncidentalBinXDAO;
import org.renci.binning.dao.clinbin.model.IncidentalBinGeneX_;
import org.renci.binning.dao.clinbin.model.IncidentalBinX;
import org.renci.binning.dao.clinbin.model.IncidentalBinX_;
import org.renci.binning.dao.clinbin.model.MaxFrequency;
import org.renci.binning.dao.clinbin.model.MaxFrequency_;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariantPK_;
import org.renci.binning.dao.hgmd.model.HGMDLocatedVariant_;
import org.renci.binning.dao.jpa.BaseDAOImpl;
import org.renci.binning.dao.refseq.model.Variants_61_2;
import org.renci.binning.dao.refseq.model.Variants_61_2_;
import org.renci.binning.dao.var.model.LocatedVariant;
import org.renci.binning.dao.var.model.LocatedVariant_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@org.springframework.transaction.annotation.Transactional(readOnly = true)
@OsgiServiceProvider(classes = { IncidentalBinXDAO.class })
@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
@Singleton
public class IncidentalBinXDAOImpl extends BaseDAOImpl<IncidentalBinX, Integer> implements IncidentalBinXDAO {

    private static final Logger logger = LoggerFactory.getLogger(IncidentalBinXDAOImpl.class);

    public IncidentalBinXDAOImpl() {
        super();
    }

    @Override
    public Class<IncidentalBinX> getPersistentClass() {
        return IncidentalBinX.class;
    }

    @Override
    public List<IncidentalBinX> findByHGMDVersion(Integer hgmdVersion) throws BinningDAOException {
        logger.debug("ENTERING findByHGMDVersion(Integer)");
        List<IncidentalBinX> ret = new ArrayList<>();
        try {
            CriteriaBuilder critBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<IncidentalBinX> crit = critBuilder.createQuery(getPersistentClass());
            Root<IncidentalBinX> root = crit.from(getPersistentClass());
            List<Predicate> predicates = new ArrayList<Predicate>();

            Join<AnnotationGene, Variants_61_2> annotationGeneVariantsJoin = root.join(IncidentalBinX_.incidentalBinGenes, JoinType.LEFT)
                    .join(IncidentalBinGeneX_.gene, JoinType.LEFT).join(AnnotationGene_.variants, JoinType.LEFT);

            Join<LocatedVariant, HGMDLocatedVariant> variantsHGMDLocatedVariantJoin = annotationGeneVariantsJoin
                    .join(Variants_61_2_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.hgmdLocatedVariants, JoinType.LEFT);
            predicates.add(critBuilder.equal(
                    critBuilder.function("hgmd_enum_to_varchar", String.class, variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.tag)),
                    "DM"));
            predicates.add(critBuilder.equal(variantsHGMDLocatedVariantJoin.get(HGMDLocatedVariant_.key).get(HGMDLocatedVariantPK_.version),
                    hgmdVersion));

            Join<LocatedVariant, MaxFrequency> locatedVariantMaxFrequencyJoin = annotationGeneVariantsJoin
                    .join(Variants_61_2_.locatedVariant, JoinType.LEFT).join(LocatedVariant_.maxFreqs, JoinType.LEFT);
            predicates.add(critBuilder.lt(locatedVariantMaxFrequencyJoin.get(MaxFrequency_.maxAlleleFreq), 0.05));

            crit.where(predicates.toArray(new Predicate[predicates.size()]));
            crit.distinct(true);

            TypedQuery<IncidentalBinX> query = getEntityManager().createQuery(crit);
            ret.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
