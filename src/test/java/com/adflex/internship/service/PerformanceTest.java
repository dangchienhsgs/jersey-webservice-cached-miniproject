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
    public void insertTest() {
        WebResource webResource = resource();
        for (int i = 0; i < 1000; i++) {
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
}
