package app.rest;

import app.bot.*;
import spark.*;
import app.util.CiscoSpark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import static app.Application.majabot;

public class RestController {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static Route serveRestAPI = (Request request, Response response) -> {
        LOGGER.info("/rest/config Web request");
        LOGGER.debug("/rest/config Web request : " + request.body());
        response.status(200);
        response.type("application/json");
            
        //load config into JSONObject
        JSONObject obj = new JSONObject();
        obj.put("Botrequest", BotController.getBotrequestcounter());
        obj.put("Rooms", majabot.getRoomamount());
        LOGGER.debug("/rest/config Web response : " + obj.toJSONString());
        return obj.toJSONString();
    };
    
}
