/*
 * Copyright (C) 2017 mjamelle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
Markos Test with Git Branching 
*/
package app.bot;

import app.util.CiscoSpark;
import app.util.SystemConfig;
import com.ciscospark.Message;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import spark.Request;

// Imports the Google Cloud client library
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import javax.json.JsonArray;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 *
 * @author mjamelle
 */
public class BotLogic {
    
    public enum Language { German, English}
    private static final Logger logger = LogManager.getLogger();
    
   // static boolean isEnglish;  
    
    private static int botrequestcounter = 0;
    private static int apiairequestcounter = 0;
    private static Translate translate; //needed for Google translation 
    
    public static void webHookMessageTrigger (Request request) {
        
        ++botrequestcounter;  //counts the Bot requests
        
        /*
        
        Message receive  = new Message(), response = new Message();
        
        //read Json structure into message objects          
        JsonReader jsonReader = Json.createReader(new StringReader(request.body()));
        JsonObject messageBody = jsonReader.readObject();
        JsonObject messageData = messageBody.getJsonObject("data");
        jsonReader.close();
        
        String messageID = messageData.getString("id");
        String messageRoomType = messageData.getString("roomType");
        
        receive = CiscoSpark.getMessage(messageID);
        response.setRoomId(receive.getRoomId());
        
        if (!receive.getPersonEmail().equals(SystemConfig.getBotUserName())) {
            
            if (receive.getText().toUpperCase().contains("TRANSLATE")) {mytranslate(receive,response,messageRoomType, Language.English);}
            else if ((receive.getText().toUpperCase().contains("ÜBERSETZE")) 
                || (receive.getText().toUpperCase().contains("UEBERSETZE"))) {mytranslate(receive,response,messageRoomType, Language.German);}
            else if (receive.getText().toUpperCase().contains("HELP")) {sendhelp(response, Language.English);}
            else if (receive.getText().toUpperCase().contains("HILFE")) {sendhelp(response, Language.German);}
            else {  response.setMarkdown("Sorry - don't get it - Tschuldige, ich verstehe nicht\n" +
                    "Type/Schreibe **help**  **Hilfe**");
                    CiscoSpark.sendMessage(response);}
                 
        }
        */

    }
    
    public static String webHookAPIAITranslate (Request request) {
        
        String text="",langto="",langfrom ="";       
        ++apiairequestcounter;  //counter incremental for Report page
        
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
        
        logger.info("Infos  : " + text + "  " + langfrom + "   " +  langto);
        } catch (Exception e) {
            logger.log(Level.INFO,e);   
        };
        
        
        //Translate text
        Translation translation;
        translation = translate.translate(
                text,
                TranslateOption.sourceLanguage(langfrom),
                TranslateOption.targetLanguage(langto));
        
        logger.info("Translated Text : " + translation.getTranslatedText()); 

        return translation.getTranslatedText();
    } 
    
    
    public static void webHookRoomsTrigger (Request request) {
        
        //read Json structure into message objects          
        JsonReader jsonReader = Json.createReader(new StringReader(request.body()));
        JsonObject messageBody = jsonReader.readObject();
        JsonObject messageData = messageBody.getJsonObject("data");
        jsonReader.close();
        
        CiscoSpark.addRoom(messageData.getString("id"));
    }
    
    private static void sendhelp(Message response, Language language) {

        if (language == Language.English) {
        response.setMarkdown("I'm always happy to help you\n" + 
                "- Type in **Translate** to translate your message text to German\n" +
                "- Type in **Uebersetze** to translate to English");
        CiscoSpark.sendMessage(response);
        } else if (language == Language.German) {
        response.setMarkdown("Ich helfe dir gerne\n" + 
                "- Schreibe **Translate** um Text ins Deutsche zu übersetzen\n" +
                "- Schreibe **Übersetze** um Text ins Englische zu übersetzen");
        CiscoSpark.sendMessage(response);
        }
    }
    
    public static void initranslate () {
        translate = TranslateOptions.getDefaultInstance().getService();
    }
    
    
     private static void mytranslate(Message receive, Message response,String messageRoomType, Language language) {

        // The text to translate
        String text = receive.getText();
        logger.info("Translate Text : " + text);


        Translation translation;
        if (language == Language.English) {
        // Translates text to German
        if (messageRoomType.equals ("direct")) text = text.substring(10);
            else text = text.substring(15);
        
        translation =
            translate.translate(
                text,
                TranslateOption.sourceLanguage("en"),
                TranslateOption.targetLanguage("de"));
        } else {
        // Translates text to English
        if (messageRoomType.equals ("direct")) text = text.substring(10);
            else text = text.substring(15);
        
        translation =
            translate.translate(
                text,
                TranslateOption.sourceLanguage("de"),
                TranslateOption.targetLanguage("en"));            
        }
 
        logger.info("Translated Text : " + translation.getTranslatedText());
        
        response.setText(translation.getTranslatedText());
        CiscoSpark.sendMessage(response);
    }   
   
    public static int getBotrequestcounter ()  {  
        return botrequestcounter;
    }
    public static int getApiairequestcounter ()  {  
        return apiairequestcounter;
    }
    
    
}
