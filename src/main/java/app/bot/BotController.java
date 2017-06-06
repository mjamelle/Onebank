package app.bot;

import spark.*;
import java.util.*;
import static app.Application.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BotController {
    private static final Logger logger = LogManager.getLogger();
    
    public static Route serveBotMessage = (Request request, Response response) -> {
        logger.info("/webhook/messages Web request");
        logger.debug("/webhook/messages Web request : " + request.body());
        BotLogic.webHookMessageTrigger(request);
        response.status(200);
        return null;
    };
    public static Route serveBotRooms = (Request request, Response response) -> {
        logger.info("/webhook/rooms Web request");
        logger.debug("/webhook/rooms Web request : " + request.body());
        BotLogic.webHookRoomsTrigger(request);
        response.status(200);
        return null;
    };
    
}
