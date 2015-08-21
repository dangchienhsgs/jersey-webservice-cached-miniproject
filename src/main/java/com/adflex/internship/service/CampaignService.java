package com.adflex.internship.service;

import com.adflex.internship.cache.CacheHandler;
import com.adflex.internship.result.ResponseController;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.ResourceConfig;
import org.bson.Document;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.json.JSONArray;
import org.json.JSONException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Key;
import java.util.Date;

@Path("campaign")
public class CampaignService {
    private static CacheHandler cacheHandler;
    private static Date lastDateChanges = new Date();

    public CampaignService() {
        cacheHandler = CacheHandler.getInstance();
    }

    @GET
    @Path("/testdb")
    public Response testAuthenByDatabase(@Context SecurityContext securityContext) {
        return Response.ok("User is allowed").build();
    }

    @GET
    @Path("/test")
    public Response testAuthentication(@Context HttpHeaders headers) {
        String bearer = headers.getRequestHeader("Authorization").get(0);
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setKey(new AesKey("chien1994@112233".getBytes()));

        try{
            jwe.setCompactSerialization(bearer.split(" ")[1]);
            System.out.println (jwe.getPayload());
            return Response.ok(jwe.getPayload()).build();
        } catch (JoseException e) {
            return Response.status(302)
                    .entity("Token fail")
                    .build();
        }
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response insertCampaign(String jsonString) throws JSONException {
        Document document = Document.parse(jsonString);
        Boolean result = cacheHandler.insertDocument(document);

        if (result) {
            lastDateChanges = new Date();
            return Response.status(ResponseController.ResponseCode.OK)
                    .entity(ResponseController.ResponseMessage.okMessage)
                    .build();
        } else {
            return Response.status(ResponseController.ResponseCode.BAD_REQUEST)
                    .entity(ResponseController.ResponseMessage.badRequestMessage)
                    .build();
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCampaign(@Context Request request) {
        // Using ETAG to reduce bandwidth
        // Instead of hashing result, we hash the lastModified date
        // Reduce computation and bandwidth
        EntityTag etag = new EntityTag(lastDateChanges.hashCode() + "");
        Response.ResponseBuilder rb = request.evaluatePreconditions(etag);

        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(86400);

        if (rb != null) {
            // the last modified is the same as result
            // tell browser use it cache
            return rb.cacheControl(cacheControl).tag(etag).build();

        } else {
            // create new result
            JSONArray jsonArray = cacheHandler.getListCampaign();
            return Response.status(ResponseController.ResponseCode.OK)
                    .entity(jsonArray.toString())
                    .cacheControl(cacheControl)
                    .tag(etag)
                    .build();
        }
    }

    @GET
    @Path("{campaignid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCampaignById(@PathParam("campaignid") String id, @Context Request request) {
        String result = cacheHandler.getCampaignById(id).toString();

        // Using ETAG to reduce bandwidth
        // if the result is same as cached result (hashcode is the same)
        // tell browser to use the cached result instead of sending it
        EntityTag etag = new EntityTag(result.hashCode() + "");
        Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(86400);

        if (rb != null) {
            // the result is the same
            // send the notification that use its cache
            return rb.tag(etag).build();
        }

        return Response.status(ResponseController.ResponseCode.OK)
                .entity(result)
                .cacheControl(cacheControl)
                .tag(etag)
                .build();
    }
}