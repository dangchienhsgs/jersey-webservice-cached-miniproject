package com.adflex.internship.service;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
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
        String response = webResource.path("/campaign/create")
                .post(String.class);

        System.out.println (response);
        Assert.assertEquals(response, "Hello");
    }
}