package com.adflex.internship.service;

import com.adflex.internship.cache.CacheHandler;
import com.adflex.internship.result.ResponseController;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Date;

@Path("campaign")
public class CampaignService {
    private static CacheHandler cacheHandler;
    private static Date lastDateChanges = new Date();

    public CampaignService() {
        cacheHandler = CacheHandler.getInstance();
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
        cacheControl.setMaxAge(2);

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
        cacheControl.setMaxAge(10);

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