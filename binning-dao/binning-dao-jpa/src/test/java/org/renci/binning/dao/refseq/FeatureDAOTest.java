package org.renci.binning.dao.refseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.renci.binning.dao.BinningDAOException;
import org.renci.binning.dao.jpa.BinningDAOManager;
import org.renci.binning.dao.refseq.model.Feature;
import org.renci.binning.dao.refseq.model.RegionGroupRegion;

public class FeatureDAOTest {

    @Test
    public void testFindByRefSeqVersionAndTranscriptId() throws BinningDAOException {
        BinningDAOManager daoMgr = BinningDAOManager.getInstance();
        List<Feature> featureList = daoMgr.getDAOBean().getFeatureDAO().findByRefSeqVersionAndTranscriptId("61", "NM_000179.2");

        Map<String, List<Feature>> featureMap = new HashMap<String, List<Feature>>();

        for (Feature feature : featureList) {
            List<RegionGroupRegion> rgrList = daoMgr.getDAOBean().getRegionGroupRegionDAO()
                    .findByRegionGroupId(feature.getRegionGroup().getRegionGroupId());
            feature.getRegionGroup().getTranscript();

            if (!featureMap.containsKey(feature.getRegionGroup().getTranscript().getVersionId())) {
                featureMap.put(feature.getRegionGroup().getTranscript().getVersionId(), new ArrayList<Feature>());
            }
            featureMap.get(feature.getRegionGroup().getTranscript().getVersionId()).add(feature);
        }

        // List<Feature> toRemove = new ArrayList<>();
        // for (String key : featureMap.keySet()) {
        // if (featureMap.get(key).size() > 1) {
        // featureMap.get(key).sort((a, b) -> a.getRegionGroup().getTranscript().compareTo());
        // }
        //
        // if (CollectionUtils.isNotEmpty(rgrList)) {
        // for (RegionGroupRegion rgr : rgrList) {
        // Range<Integer> rgrRange = Range.between(rgr.getKey().getRegionStart(), rgr.getKey().getRegionEnd());
        // if (rgrRange.contains(transcriptPosition)) {
        // return f.getId();
        // }
        // }
        //
        // }
        //
        // }

        // Map<String, List<TranscriptMaps>> transcriptMap = new HashMap<String, List<TranscriptMaps>>();
        // for (TranscriptMaps tMap : transcriptMapsList) {
        // if (!transcriptMap.containsKey(tMap.getTranscript().getVersionId())) {
        // transcriptMap.put(tMap.getTranscript().getVersionId(), new ArrayList<TranscriptMaps>());
        // }
        // transcriptMap.get(tMap.getTranscript().getVersionId()).add(tMap);
        // }
        // TranscriptMaps remove = null;
        // for (String key : transcriptMap.keySet()) {
        // if (transcriptMap.get(key).size() > 1) {
        // transcriptMap.get(key).sort((a, b) -> a.getMapCount().compareTo(b.getMapCount()));
        // remove = transcriptMap.get(key).get(0);
        // }
        // }
        // transcriptMapsList.remove(remove);

    }
}
