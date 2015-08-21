package com.adflex.internship.service;

import com.adflex.internship.resources.CampaignParameter;
import com.adflex.internship.result.ResponseController;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.security.Key;
import java.util.UUID;

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

        String id = UUID.randomUUID().toString();
        BSONObject bsonObject = new BasicBSONObject()
                .append(CampaignParameter.APP_KEY.getValue(), "appkey")
                .append(CampaignParameter.BUDGET.getValue(), "Asdsa")
                .append(CampaignParameter.RETENTION_RATE.getValue(), 13)
                .append(CampaignParameter.TOTAL_INSTALLED.getValue(), 24)
                .append(CampaignParameter.CAMPAIGN_ID.getValue(), id);


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

        String list1 = response.getEntity(String.class);

        response = webResource.path("campaign")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        String list2 = response.getEntity(String.class);
        Assert.assertEquals(list1, list2);
        Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
    }

    @Test
    public void testListCampaignById() {
        WebResource webResource = resource();
        ClientResponse response = webResource.path("campaign/1234")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        System.out.println(response.getEntity(String.class));
        Assert.assertEquals(response.getStatus(), ResponseController.ResponseCode.OK);
    }

    @Test
    public void testAuthen() {
        WebResource webResource = resource();

        Key key = new AesKey("chien1994@112233".getBytes());
        
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setPlaintext(new JSONObject().put("scope", "email,username").toString());
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(key);

        String serializedJwe;
        try {
            serializedJwe = jwe.getCompactSerialization();
        } catch (Exception e) {
            serializedJwe = "{\"scope\": \"error\"}";
        }

        ClientResponse response = webResource.path("campaign/test")
                .header("Authorization", "Bearer " + serializedJwe)
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        System.out.println(response.getEntity(String.class));
    }

    @Test
    public void testAuthenDB() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("root", "chien1994"));
        String result = client.resource("http://localhost:8080/campaign/testdb")
                .get(String.class);
        System.out.println(result);
    }
}