package com.adflex.internship.resources;

import com.adflex.internship.model.Model;
import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by dangchienhsgs on 11/08/2015.
 */

@XmlRootElement
public class Campaign {
    public static final String CAMPAIGN_ID ="campaign_id";
    public static final String APP_KEY = "app_key";
    public static final String BUDGET = "budget";
    public static final String TOTAL_INSTALLED = "total_installed";
    public static final String RETENTION_RATE = "retention_rate";

    public Campaign() {

    }

    //@XmlElement(name="campaign_id")
    public String campaignId;

    //@XmlElement(name="app_key")
    public String appKey;

    //@XmlElement(name="budget")
    public String budget;

    //@XmlElement(name="total_installed")
    public int totalInstalled;

    //@XmlElement(name="retention_rate")
    public float retention_rate;

    public String getCampaignId() {
        return campaignId;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getBudget() {
        return budget;
    }

    public int getTotalInstalled() {
        return totalInstalled;
    }

    public float getRetention_rate() {
        return retention_rate;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setTotalInstalled(int totalInstalled) {
        this.totalInstalled = totalInstalled;
    }

    public void setRetention_rate(float retention_rate) {
        this.retention_rate = retention_rate;
    }
}
