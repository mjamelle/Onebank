package app.bot;

import spark.*; 
import java.util.*;
import static app.Application.*;
import static app.util.LinkPath.Web.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BotController {
    private static final Logger logger = LogManager.getLogger();
    
    public static Route serveBotMessage = (Request request, Response response) -> {
        logger.info(BOTMESSAGE + "post request");
        logger.debug(BOTMESSAGE + "post request : " + request.body());
        BotLogic.webHookMessageTrigger(request);
        response.status(200);
        return null;
    };
    public static Route serveBotRooms = (Request request, Response response) -> {
        logger.info(BOTROOMS + "post request");
        logger.debug(BOTROOMS + "post request : " + request.body());
        BotLogic.webHookRoomsTrigger(request);
        response.status(200);
        return null;
    };
    public static Route serveBotApiai = (Request request, Response response) -> {
        logger.info(RESTBOTAPIAI + "  post request");
        logger.debug (RESTBOTAPIAI + "  post request" + request.body());
        String translation = BotLogic.webHookAPIAITranslate(request);
        response.status(200);
        response.type("application/json");
        return "{ 'speech': '" + translation + "' }";
    };
}
