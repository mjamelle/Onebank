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
        ++botrequestcounter;
    }
    
    public static String webHookAPIAITranslate (Request request) {
        ++apiairequestcounter;      
        
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
    
    public static void initranslate () {
        translate = TranslateOptions.getDefaultInstance().getService();
    }
     
    public static int getBotrequestcounter ()  {  
        return botrequestcounter;
    }
    public static int getApiairequestcounter ()  {  
        return apiairequestcounter;
    }
    
    
}
