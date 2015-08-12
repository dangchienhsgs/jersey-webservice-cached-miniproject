package com.adflex.internship.service;

import com.adflex.internship.dao.MongoController;
import com.adflex.internship.result.ResponseController;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.codehaus.jettison.json.JSONException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("campaign")
public class CampaignService {
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response insertCampaign(String jsonString) throws JSONException{
        Document document = Document.parse(jsonString);

        MongoCollection campaignCollection = MongoController.AdFlex.CAMPAIGN_COLLECTION;
        campaignCollection.insertOne(document);

        return Response.status(ResponseController.ResponseCode.OK)
                .entity(ResponseController.ResponseMessage.okMessage)
                .build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCampaign(String sample) {
        return null;
    }

    @GET
    @Path("/")
    public Response getCampaignById() {
        return null;
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCampaign() {
        return null;
    }
}