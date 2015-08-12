package com.adflex.internship.service;

import com.adflex.internship.resources.campaign.Campaign;
import jdk.nashorn.internal.parser.JSONParser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("campaign")
public class CampaignService {
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response insertCampaign(String sample) throws JSONException{
        Campaign campaign = new Campaign();
        campaign.setAppKey("1333");
        campaign.setBudget("223");

        JSONObject jsonObject = new JSONObject();
        jsonObject.append("as", "as");

        return Response.ok().entity(sample).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listCampaign(String sample) {
        return null;
    }



}