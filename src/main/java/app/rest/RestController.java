package app.rest;

import app.bot.*;
import spark.*;
import app.util.CiscoSpark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

public class RestController {
    private static final Logger logger = LogManager.getLogger();
    
    public static Route serveRestAPI = (Request request, Response response) -> {
        logger.info("/rest/config Web request");
        logger.debug("/rest/config Web request : " + request.body());
        response.status(200);
        response.type("application/json");
            
        //load config into JSONObject
        JSONObject obj = new JSONObject();
        obj.put("Botrequest", BotLogic.getBotrequestcounter());
        obj.put("Rooms", CiscoSpark.getRoomamount());
        logger.debug("/rest/config Web response : " + obj.toJSONString());
        return obj.toJSONString();
    };
    
}
