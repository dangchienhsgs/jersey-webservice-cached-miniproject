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
                .append(CampaignParameter.APP_KEY.getValue(), "appkey")
                .append(CampaignParameter.BUDGET.getValue(), "Asdsa")
                .append(CampaignParameter.RETENTION_RATE.getValue(), 13)
                .append(CampaignParameter.TOTAL_INSTALLED.getValue(), 24)
                .append(CampaignParameter.CAMPAIGN_ID.getValue(), "12554");


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
                .get(ClientResponse.class);

        System.out.println (response.getEntity(String.class));
        Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
    }

}