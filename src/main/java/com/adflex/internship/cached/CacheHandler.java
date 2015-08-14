package com.adflex.internship.cached;

import com.adflex.internship.dao.MongoConfiguration;
import com.adflex.internship.dao.MongoController;
import com.adflex.internship.resources.Campaign;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
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
    private static Map<Document, Integer> changesList;   // id, pair <document, status>

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
        changesList.put(document, CacheConfiguration.CacheStatus.WAITING);

        campaignList.put(document.getString(Campaign.CAMPAIGN_ID), document);
        changesList.put(document, CacheConfiguration.CacheStatus.RELOAD);

        MongoController.AdFlex.CAMPAIGN_COLLECTION.insertOne(document);
        changesList.remove(document);
    }

    public JSONArray getListCampaign() {
        JSONArray jsonArray = new JSONArray();
        for (Document document: campaignList.values()) {
            jsonArray.put(document);
        }

        if (changesList.size() > 0) {
            for (Map.Entry<Document, Integer> entry: changesList.entrySet()) {
                if (entry.getValue() == CacheConfiguration.CacheStatus.WAITING) {
                    jsonArray.put(entry.getKey());
                }
            }
        }

        return jsonArray;
    }

    public JSONArray getCampaignById(String id) {
        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<Document, Integer> entry: changesList.entrySet()) {
            if (entry.getKey().get(Campaign.CAMPAIGN_ID).equals(id)) {
                jsonArray.put(entry.getKey());
            }

            return jsonArray;
        }

        if (campaignList.containsKey(id)) {
            jsonArray.put(campaignList.get(id));
        }

        return jsonArray;
    }
}
