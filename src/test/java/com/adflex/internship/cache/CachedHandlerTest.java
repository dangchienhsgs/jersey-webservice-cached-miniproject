package com.adflex.internship.cache;import com.adflex.internship.dao.MongoController;import com.adflex.internship.resources.CampaignParameter;import com.mongodb.client.MongoCollection;import org.bson.Document;import org.codehaus.jettison.json.JSONException;import org.junit.Assert;import org.junit.Test;import java.util.UUID;/** * Created by dangchienhsgs on 14/08/2015. */public class CachedHandlerTest {    @Test    public void testSingeton() {        CacheHandler cacheHandler1 = CacheHandler.getInstance();        CacheHandler cacheHandler2 = CacheHandler.getInstance();        Assert.assertEquals(cacheHandler1, cacheHandler2);    }    @Test    public void testReload() {        CacheHandler cacheHandler = CacheHandler.getInstance();        cacheHandler.reloadCampaignFromDB();        MongoCollection<Document> campaignCollection = MongoController.AdFlex.CAMPAIGN_COLLECTION;        Assert.assertEquals(campaignCollection.count(), cacheHandler.getListCampaign().length());    }    @Test    public void testInsertCampaign() throws JSONException {        CacheHandler cacheHandler = CacheHandler.getInstance();        String campaignID = UUID.randomUUID().toString();        Document document = new Document().append(CampaignParameter.CAMPAIGN_ID.getValue(), campaignID);        cacheHandler.insertDocument(document);        Assert.assertTrue(cacheHandler.getCampaignById(campaignID).length() == 1);    }    @Test    public void testListCampaign() {        CacheHandler cacheHandler = CacheHandler.getInstance();        System.out.println(cacheHandler.getListCampaign().length());    }    @Test    public void testGetCampaignById() throws JSONException {        CacheHandler cacheHandler = CacheHandler.getInstance();        String campaignID = UUID.randomUUID().toString();        Document document = new Document().append(CampaignParameter.CAMPAIGN_ID.getValue(), campaignID);        cacheHandler.insertDocument(document);        Assert.assertTrue(true);    }}