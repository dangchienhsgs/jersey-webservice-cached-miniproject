package com.adflex.internship.service;

import com.adflex.internship.cached.CacheHandler;
import com.adflex.internship.result.ResponseController;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("campaign")
public class CampaignService {
    private CacheHandler cacheHandler;
    private CacheControl cacheControl;

    public CampaignService() {
        this.cacheHandler = CacheHandler.getInstance();
        this.cacheControl = new CacheControl();
        cacheControl.setMaxAge(86400);
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response insertCampaign(String jsonString) throws JSONException {
        Document document = Document.parse(jsonString);
        Boolean result = cacheHandler.insertDocument(document);

        if (result) {
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
        JSONArray jsonArray = cacheHandler.getListCampaign();

        return Response.status(ResponseController.ResponseCode.OK)
                .entity(jsonArray.toString())
                .build();
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

        return Response.ok(result)
                .cacheControl(cacheControl)
                .tag(etag)
                .build();
    }
}