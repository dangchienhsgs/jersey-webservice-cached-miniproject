package com.adflex.internship.service;

import com.adflex.internship.cached.CacheHandler;
import com.adflex.internship.resources.Campaign;
import com.adflex.internship.result.ResponseController;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("campaign")
public class CampaignService {
    private CacheHandler cacheHandler;

    public CampaignService() {
        this.cacheHandler = CacheHandler.getInstance();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response insertCampaign(String jsonString) throws JSONException {
        Document document = Document.parse(jsonString);
        cacheHandler.insertDocument(document);
        return Response.status(ResponseController.ResponseCode.OK)
                .entity(ResponseController.ResponseMessage.okMessage)
                .build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCampaign() {
        JSONArray jsonArray = cacheHandler.getListCampaign();
        return Response.status(ResponseController.ResponseCode.OK)
                .entity(jsonArray.toString())
                .build();
    }

    @GET
    @Path("{" + Campaign.CAMPAIGN_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCampaignById(@PathParam(Campaign.CAMPAIGN_ID) String id) {
        JSONArray jsonArray = cacheHandler.getCampaignById(id);

        return Response.status(ResponseController.ResponseCode.OK)
                .entity(jsonArray.toString() + " | " + id)
                .build();
    }
}