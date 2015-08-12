package com.adflex.internship.service;

import com.adflex.internship.dao.MongoController;
import com.adflex.internship.resources.campaign.Campaign;
import com.adflex.internship.result.ResponseController;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;

import javax.print.Doc;
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
    public Response listCampaign() {
        MongoCollection<Document> campaignCollection = MongoController.AdFlex.CAMPAIGN_COLLECTION;
        JSONArray jsonArray = new JSONArray();
        for (Document document: campaignCollection.find()) {
            jsonArray.put(document);
        }

        return Response.status(ResponseController.ResponseCode.OK)
                .entity(jsonArray.toString())
                .build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCampaignById(@QueryParam(Campaign.CAMPAIGN_ID) String id) {
        MongoCollection<Document> campaignCollection = MongoController.AdFlex.CAMPAIGN_COLLECTION;

        JSONArray jsonArray = new JSONArray();
        Block<Document> blockDocument = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                jsonArray.put(document);
            }
        };

        campaignCollection.find(eq(Campaign.CAMPAIGN_ID, id)).forEach(blockDocument);

        return Response.status(ResponseController.ResponseCode.OK)
                .entity(jsonArray.toString() +" | "+id)
                .build();
    }
}