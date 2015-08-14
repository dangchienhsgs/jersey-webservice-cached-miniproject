package com.adflex.internship.service;

import com.adflex.internship.resources.Campaign;
import com.adflex.internship.result.ResponseController;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import org.junit.Assert;

/**
 * Created by dangchienhsgs on 12/08/2015.
 */

public class CampaignServiceTest extends JerseyTest {

    @Override
    protected AppDescriptor configure() {
        // Define resources to test
        return new WebAppDescriptor.Builder("com.adflex.internship.service")
                .build();
    }

    @Test
    public void testInsertCampaign() {
        WebResource webResource = resource();

        BSONObject bsonObject = new BasicBSONObject()
                .append(Campaign.APP_KEY, "appkey")
                .append(Campaign.BUDGET, "Asdsa")
                .append(Campaign.RETENTION_RATE, 13)
                .append(Campaign.TOTAL_INSTALLED, 24)
                .append(Campaign.CAMPAIGN_ID, "12554");


        ClientResponse response = webResource.path("campaign/create")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, bsonObject.toString());

        Assert.assertEquals(response.getEntity(String.class), ResponseController.ResponseMessage.okMessage);
        Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
    }

    @Test
    public void testListCampaign() {
        WebResource webResource = resource();

        ClientResponse response = webResource.path("campaign")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        System.out.println ("List campaign: " + response.getEntity(String.class));
        Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
    }

    @Test
    public void testListCampaignById() {
        WebResource webResource = resource();
        ClientResponse response = webResource.path("campaign/1234")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class);

        System.out.println (response.getEntity(String.class));
        Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
    }

}