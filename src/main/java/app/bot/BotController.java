package app.bot;

import static app.Application.bankdemobot;
import static app.Application.majabot;
import spark.*; 
import lombok.*;
import static app.util.LinkPath.Web.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import java.io.StringReader;
import static java.net.HttpURLConnection.*;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BotController {
    private static final Logger LOGGER = LogManager.getLogger();
    @Getter private static int botrequestcounter = 0;
    @Getter private static int translatencounter = 0;
    private static Translate translate; //needed for Google translation 
    
    public static void initranslate () {
        translate = TranslateOptions.getDefaultInstance().getService();
    }
     
    public static Route serveBotMessage = (Request request, Response response) -> {
        LOGGER.info(BOTMESSAGE + "  post request");
        LOGGER.debug(BOTMESSAGE + "  post request : " + request.body());
        ++botrequestcounter;
        response.status(HTTP_OK);
        return "";
    };
    public static Route serveBotRooms = (Request request, Response response) -> {
        LOGGER.info(BOTROOMS + "  post request");
        LOGGER.debug(BOTROOMS + "  post request : " + request.body());
        try {
           ObjectMapper mapper = new ObjectMapper();
           WebhookEvent webhook = mapper.readValue(request.body(), WebhookEvent.class);
           //switch to the right Bot and add the room based on the ID
           if (webhook.getName().equals(majabot.getWebhookName())) majabot.addRoom(webhook.getData().getId());
           if (webhook.getName().equals(bankdemobot.getWebhookName())) bankdemobot.addRoom(webhook.getData().getId());
           
        } catch (Exception e) {
           LOGGER.error(e);
           response.status(HTTP_BAD_REQUEST); // Hey, you did not send a valid request!
           return "";            
        }
        response.status(HTTP_OK);
        response.type("application/json; charset=utf-8");
        return "";
    };
    public static Route serveTranslate = (Request request, Response response) -> {
        LOGGER.info(RESTTRANSLATE + "  post request");
        LOGGER.debug (RESTTRANSLATE + "  post request" + request.body());
        String translation = Translate(request);
        response.status(HTTP_OK);
        response.type("application/json; charset=utf-8");
        return "{ \"speech\": \"" + translation + "\", \"data\" : [{ \"translated-text\" : \"" + translation + "\"}]}";
    };
    
    public static String Translate (Request request) {
        ++translatencounter;      
        
        String text="",langto="",langfrom ="";       
       
        try {
        // read the API.AI Json format and assign to the objects    
        JsonReader jsonReader = Json.createReader(new StringReader(request.body()));
        JsonObject messageBody = jsonReader.readObject();
        JsonObject result = messageBody.getJsonObject("result");
        JsonArray contexts = result.getJsonArray("contexts");
        jsonReader.close();
        
        text = result.getJsonObject("parameters").getString("text");
        langto = result.getJsonObject("parameters").getString("lang-to");
        langfrom = result.getJsonObject("parameters").getString("lang-from");
        
        LOGGER.info("Translate Parameters  : " + text + "  " + langfrom + "   " +  langto);
        } catch (Exception e) {
            LOGGER.log(Level.INFO,e);   
        }
        
        
        //Translate text
        Translation translation;
        translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(langfrom),
                Translate.TranslateOption.targetLanguage(langto));
        
        LOGGER.info("Translated Text : " + translation.getTranslatedText()); 

        return translation.getTranslatedText();
    } 
}