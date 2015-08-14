package com.adflex.internship.cached;

import com.adflex.internship.dao.MongoConfiguration;
import com.adflex.internship.dao.MongoController;
import com.adflex.internship.resources.Campaign;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by dangchienhsgs on 13/08/2015.
 */
public class CacheHandler {
    private static Map<String, Document> campaignList;   // id, document
    private static Map<String, Pair<Document, Integer>> changesList;   // id, pair <document, status>

    private static CacheHandler cacheHandler = null;

    private CacheHandler() {
        // do nothing
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
            campaignList.put(document.getString(Campaign.CAMPAIGN_ID), document);
        }
    }

    public void insertDocument(Document document) {
        String id = document.getString(Campaign.CAMPAIGN_ID);

        // add in cache
        changesList.put(id, Pair.of(document, CacheConfiguration.CacheStatus.WAITING));
        campaignList.put(id, document);
        changesList.put(id, Pair.of(document, CacheConfiguration.CacheStatus.RELOAD));

        // add in database
        MongoController.AdFlex.CAMPAIGN_COLLECTION.insertOne(document);

        // remove in changes cache
        changesList.remove(id);
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
