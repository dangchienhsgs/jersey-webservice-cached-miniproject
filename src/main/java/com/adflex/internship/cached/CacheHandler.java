package com.adflex.internship.cached;

import com.adflex.internship.dao.MongoController;
import com.adflex.internship.resources.CampaignParameter;
import com.adflex.internship.resources.CampaignUtils;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dangchienhsgs on 13/08/2015.
 * In this design, each campaign has unique id
 */
public class CacheHandler {
    private static Map<String, Document> campaignList;   // id, document
    private static Map<String, Pair<Document, Integer>> changesList;   // id, pair <document, status>

    private static CacheHandler cacheHandler = null;

    private CacheHandler() {
        campaignList = new HashMap<>();
        changesList = new HashMap<>();
    }

    public static CacheHandler getInstance() {
        if (cacheHandler == null) {
            cacheHandler = new CacheHandler();
            cacheHandler.reloadCampaignFromDB();
        }

        return cacheHandler;
    }

    public void reloadCampaignFromDB() {
        MongoCollection<Document> campaignCollection = MongoController.AdFlex.CAMPAIGN_COLLECTION;

        for (Document document : campaignCollection.find()) {
            campaignList.put(document.getString(CampaignParameter.CAMPAIGN_ID.getValue()), document);
        }
    }

    public boolean insertDocument(Document document) {
        if (document.containsKey(CampaignParameter.CAMPAIGN_ID.getValue())) {
            document = CampaignUtils.validation(document);
            String id = document.getString(CampaignParameter.CAMPAIGN_ID.getValue());

            changesList.put(id, Pair.of(document, CacheConfiguration.CacheStatus.WAITING));
            campaignList.put(id, document);
            changesList.put(id, Pair.of(document, CacheConfiguration.CacheStatus.RELOAD));

            MongoController.AdFlex.CAMPAIGN_COLLECTION.insertOne(document);

            changesList.remove(id);

            return true;
        } else {
            return false;
        }
    }

    public JSONArray getListCampaign() {
        JSONArray jsonArray = new JSONArray();
        for (Document document : campaignList.values()) {
            jsonArray.put(document);
        }

        changesList.forEach((id, pair) -> {
            if (pair.getValue() == CacheConfiguration.CacheStatus.WAITING) {
                jsonArray.put(pair.getKey());
            }
        });

        return jsonArray;
    }

    public JSONArray getCampaignById(String id) {
        JSONArray jsonArray = new JSONArray();

        if (changesList.containsKey(id)) {
            jsonArray.put(changesList.get(id).getKey());
            return jsonArray;
        }

        if (campaignList.containsKey(id)) {
            jsonArray.put(campaignList.get(id));
        }

        return jsonArray;
    }
}
