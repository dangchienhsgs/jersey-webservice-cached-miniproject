package com.adflex.internship.service;

import com.adflex.internship.resources.campaign.Campaign;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("campaign")
public class CampaignService {
//    @GET
//    @Path("{i}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getCampaignById(@PathParam("{i}") int i){
//
//    }
//
    @Produces(MediaType.TEXT_PLAIN)
    @POST
    @Path("/create")
    public Response insertCampaign() {
        return Response.ok().entity("Hello").build();
    }
}