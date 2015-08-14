package com.adflex.internship.resources;


import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dangchienhsgs on 14/08/2015.
 */
public class CampaignUtils {
    public static JSONObject validation(JSONObject document) {
        for (CampaignParameter campaignParameter : CampaignParameter.values()) {
            if (!document.has(campaignParameter.getValue())) {
                document.append(campaignParameter.getValue(), "");
            }
        }

        return document;
    }

    public static Document validation(Document document) {
        for (CampaignParameter campaignParameter : CampaignParameter.values()) {
            if (!document.containsKey(campaignParameter.getValue())) {
                document.append(campaignParameter.getValue(), "");
            }
        }

        return document;
    }
}

