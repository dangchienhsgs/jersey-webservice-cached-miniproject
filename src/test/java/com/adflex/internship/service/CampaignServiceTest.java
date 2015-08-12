package com.adflex.internship.service;

import com.adflex.internship.resources.campaign.Campaign;
import com.adflex.internship.result.ResponseController;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.json.JSONObject;
import org.junit.Test;
import javax.print.attribute.standard.Media;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

        JSONObject jsonObject = new JSONObject()
                .append(Campaign.APP_KEY, "appkey")
                .append(Campaign.BUDGET, "Asdsa")
                .append(Campaign.RETENTION_RATE, 13)
                .append(Campaign.TOTAL_INSTALLED, 24);

        ClientResponse response = webResource.path("campaign/create")
                .accept(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, jsonObject.toString());

        Assert.assertEquals(response.getEntity(String.class), ResponseController.ResponseMessage.okMessage);
    }
}