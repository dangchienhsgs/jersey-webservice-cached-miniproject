package com.adflex.internship.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by dangchienhsgs on 12/08/2015.
 */
public class MongoController {
    public static MongoClient MONGO_CLIENT = new MongoClient(
            MongoConfiguration.MONGO_HOST,
            MongoConfiguration.MONGO_PORT
    );

    public static class AdFlex {
        public static MongoDatabase DATABASE = MONGO_CLIENT.getDatabase("adflex");
        public static MongoCollection<Document> CAMPAIGN_COLLECTION = DATABASE.getCollection("campaign");
    }
}