package com.adflex.internship.resources;


import org.bson.Document;
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

    public static boolean compare(Document document1, Document document2) {
        for (CampaignParameter campaignParameter : CampaignParameter.values()) {
            if (!String.valueOf(document1.get(campaignParameter))
                    .equals(String.valueOf(document2.get(campaignParameter)))) {
                return false;
            }
        }
        return true;
    }
}

