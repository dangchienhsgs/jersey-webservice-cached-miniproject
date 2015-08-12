package com.adflex.internship.service;

import com.adflex.internship.resources.campaign.Campaign;
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

public class SimpleTest extends JerseyTest {

    @Path("hello")
    public static class HelloResource {
        @GET
        public String getHello() {
            return "Hello World!";
        }
    }

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("com.adflex.internship.service")
                .build();
    }

    @Test
    public void test() {
        WebResource webResource = resource();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Campaign.APP_KEY, "appkey");
        jsonObject.put(Campaign.BUDGET, "Asdsa");
        jsonObject.put(Campaign.RETENTION_RATE, 13);
        jsonObject.put(Campaign.TOTAL_INSTALLED, 24);

        System.out.println (jsonObject.toString());

        String response = webResource.path("campaign/create")
                .accept(MediaType.APPLICATION_JSON)
                .post(String.class, jsonObject.toString());

        Assert.assertEquals(response, "Hello");
    }
}