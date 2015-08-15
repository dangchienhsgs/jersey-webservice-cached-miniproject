package com.adflex.internship.service;

import com.adflex.internship.resources.CampaignParameter;
import com.adflex.internship.result.ResponseController;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * Created by dangchienhsgs on 14/08/2015.
 */
public class PerformanceTest extends JerseyTest {
    @Override
    protected AppDescriptor configure() {
        // Define resources to test
        return new WebAppDescriptor.Builder("com.adflex.internship.service")
                .build();
    }

    @Test
    public void testInsertPerformance() {
        WebResource webResource = resource();
        for (int i = 0; i < 10; i++) {
            System.out.println(i);

            BSONObject bsonObject = new BasicBSONObject()
                    .append(CampaignParameter.APP_KEY.getValue(), "appkey")
                    .append(CampaignParameter.BUDGET.getValue(), "asdsa")
                    .append(CampaignParameter.RETENTION_RATE.getValue(), 13)
                    .append(CampaignParameter.TOTAL_INSTALLED.getValue(), 24)
                    .append(CampaignParameter.CAMPAIGN_ID.getValue(), UUID.randomUUID().toString());


            ClientResponse response = webResource.path("campaign/create")
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, bsonObject.toString());

            Assert.assertEquals(response.getEntity(String.class), ResponseController.ResponseMessage.okMessage);
            Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
        }
    }

    @Test
    public void testAllCampaign() {
        WebResource webResource = resource();
        for (int i = 0; i < 100; i++) {
            ClientResponse response = webResource.path("campaign/")
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);

            Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
        }
    }


    /***
     * Test the caching by etag for getCampaignById
     */
    @Test
    public void testGetCampaignByIdPerformance() {
        WebResource webResource = resource();

        // create an campaign with specific id
        String campaignId = UUID.randomUUID().toString();
        BSONObject bsonObject = new BasicBSONObject()
                .append(CampaignParameter.APP_KEY.getValue(), "appkey")
                .append(CampaignParameter.BUDGET.getValue(), "asdsa")
                .append(CampaignParameter.RETENTION_RATE.getValue(), 13)
                .append(CampaignParameter.TOTAL_INSTALLED.getValue(), 24)
                .append(CampaignParameter.CAMPAIGN_ID.getValue(), campaignId);


        webResource.path("campaign/create")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, bsonObject.toString());

        // query that campaign for many times, etag value must be remained
        String entityTag = null;
        String responseValue = null;
        for (int i = 0; i < 100; i++) {
            ClientResponse response = webResource.path("campaign/" + campaignId)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);

            if (entityTag == null) {
                entityTag = response.getEntityTag().getValue();
            } else {
                Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
            }

            if (responseValue == null) {
                responseValue = response.getEntity(String.class);
            } else {
                Assert.assertEquals(response.getEntity(String.class), responseValue);
            }
        }
    }

    /***
     * Test the caching by etag for getCampaignById
     */
    @Test
    public void testGetCampaignListPerformance() {
        WebResource webResource = resource();

        // query that campaign for many times, etag value must be remained
        String entityTag = null;
        String responseValue = null;
        for (int i = 0; i < 10; i++) {
            ClientResponse response = webResource.path("campaign")
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);

            if (entityTag == null) {
                entityTag = response.getEntityTag().getValue();
            } else {
                Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
            }

            if (responseValue == null) {
                responseValue = response.getEntity(String.class);
            } else {
                Assert.assertEquals(response.getEntity(String.class), responseValue);
            }
        }

        // create a campaign with specific id
        // after insert, the entity tag should be changed
        String campaignId = UUID.randomUUID().toString();
        BSONObject bsonObject = new BasicBSONObject()
                .append(CampaignParameter.APP_KEY.getValue(), "appkey")
                .append(CampaignParameter.BUDGET.getValue(), "asdsa")
                .append(CampaignParameter.RETENTION_RATE.getValue(), 13)
                .append(CampaignParameter.TOTAL_INSTALLED.getValue(), 24)
                .append(CampaignParameter.CAMPAIGN_ID.getValue(), campaignId);


        webResource.path("campaign/create")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, bsonObject.toString());


        ClientResponse response = webResource.path("campaign")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        // compare old result and new result, old etag and new etag
        Assert.assertNotEquals(response.getEntity(String.class), responseValue);
        Assert.assertNotEquals(response.getEntityTag().toString(), entityTag.toString());
    }
}
