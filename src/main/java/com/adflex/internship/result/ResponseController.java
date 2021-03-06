package com.adflex.internship.result;

import javax.ws.rs.core.Response;

/**
 * Created by dangchienhsgs on 12/08/2015.
 */
public class ResponseController {
    public static class ResponseCode {
        public static int OK = 200;
        public static int BAD_REQUEST = 400;
    }

    public static class ResponseMessage {
        public static String okMessage = "{\"result\": \"OK\"}";
        public static String badRequestMessage = "{\"result\": \"Validation failed\"}";
    }
}
