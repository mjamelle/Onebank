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
package app.util;

import com.ciscospark.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class CiscoSpark {
    
    public static final Logger LOGGER = LogManager.getLogger();
    
    final private URL SPARK_API_URL = SystemConfig.getSparkApiUrl();
    //final private String MAJA_MESSAGE_WEBHOOK = "Maja Webhook";
    final static String WEBHOOKMESSAGELINK = SystemConfig.getWebhookMessageLink();
    final static String WEBHOOKROOMSLINK = SystemConfig.getWebhookRoomsLink();
    final private Spark ciscospark;
    private String webhookName;
    private List<Room> mySparkrooms = new ArrayList();
    private List<Webhook> myWebhooks = new ArrayList();    

    public CiscoSpark(String accessToken, String webhookName) throws MalformedURLException, URISyntaxException {
        this.webhookName = webhookName;
         // Initialize the Spark client
        ciscospark = Spark.builder()
        .baseUrl(SPARK_API_URL.toURI())
        .accessToken(accessToken)
        .build();
        
        //load rooms into array
        iniRooms ();
        iniWebhooks();

        LOGGER.info("iniSpark successful AT : " + accessToken);
    }    
/*
    public void ciscoSparkIni (String accessToken) throws MalformedURLException, URISyntaxException {
        
         // Initialize the Spark client
        ciscospark = Spark.builder()
        .baseUrl(SPARK_API_URL.toURI())
        .accessToken(accessToken)
        .build();
        
        //load rooms into array
        iniRooms ();
        iniWebhooks();

        LOGGER.info("iniSpark successful AT : " + accessToken);
    }
    */
    
    private void iniRooms ()  {
        
        // List the rooms that I'm in
        ciscospark.rooms()
            .iterate()
            .forEachRemaining(room -> {
                mySparkrooms.add(room);
                LOGGER.info("iniRoom : "+room.getTitle());
            });        
        
    };
    
    public void addRoom (String id)  { 
        // add room
        Room receive = new Room();
        receive = ciscospark.rooms().path("/"+id, Room.class).get();
        mySparkrooms.add(receive);
        LOGGER.info("addRoom : " + receive.getTitle());    
    };
    
    public int getRoomamount ()  {  
        return mySparkrooms.size();
    };   
        
    private void iniWebhooks () throws MalformedURLException, URISyntaxException {
        
        // List and initialize the Webhooks  Bot is in
        ciscospark.webhooks()
            .iterate()
            .forEachRemaining(webhook -> {
                myWebhooks.add(webhook);
                LOGGER.info("iniWebhook : " + webhook.getTargetUrl().toString());
            });  
    
        //check if  Default Message Webhook exist 
        boolean webhookexist = false;
        for(Webhook intern : myWebhooks)  {
            if (WEBHOOKMESSAGELINK.equals(intern.getTargetUrl().toString())) webhookexist = true;
        }
        //if not then add Default Message Webhook   
            if (!webhookexist) {
                Webhook webhook = new Webhook();
                webhook.setName(webhookName);

                URL url = new URL(WEBHOOKMESSAGELINK);
                URI uri = url.toURI();

                webhook.setTargetUrl(uri);
                webhook.setResource("messages");
                webhook.setEvent("created");
                myWebhooks.add(webhook);
                ciscospark.webhooks().post(webhook);    
            }
        //check if  Default Rooms Webhook exist 
        webhookexist = false;
        for(Webhook intern : myWebhooks)  {
            if (WEBHOOKROOMSLINK.equals(intern.getTargetUrl().toString())) webhookexist = true;
        }
        //if not then add Default Rooms Webhook   
            if (!webhookexist) {
                Webhook webhook = new Webhook();
                webhook.setName(webhookName);

                URL url = new URL(WEBHOOKROOMSLINK);
                URI uri = url.toURI();

                webhook.setTargetUrl(uri);
                webhook.setResource("rooms");
                webhook.setEvent("created");
                myWebhooks.add(webhook);
                ciscospark.webhooks().post(webhook);    
            }
    }; 
    
    public int getWebhookscounter ()  {  
        return myWebhooks.size();
    };  
    
       
    public Message getMessage(String id) {
        Message receive = new Message();
        receive = ciscospark.messages().path("/"+ id, Message.class).get();
    return (receive);
    }
    
    public void sendMessage (Message message) {
        LOGGER.info("sendMessage : " + message.getText());
        ciscospark.messages().post(message);
    }
    
}