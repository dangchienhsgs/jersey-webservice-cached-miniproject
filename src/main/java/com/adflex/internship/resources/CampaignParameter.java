package com.adflex.internship.resources;


/**
 * Created by dangchienhsgs on 11/08/2015.
 */

public enum CampaignParameter {
    CAMPAIGN_ID("campaign_id"),
    APP_KEY("app_key"),
    BUDGET("budget"),
    TOTAL_INSTALLED("total_installed"),
    RETENTION_RATE("retention_rate");

    private String value;

    CampaignParameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

