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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lombok.*;


import java.io.FileInputStream;
import java.io.FileOutputStream;        
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.logging.log4j.Level;


public class SystemConfig {
    
    private static final Logger LOGGER = LogManager.getLogger();
        private static String serverPort = "4567";    
@Getter @Setter    private static String mConfigFile = "config/config.properties";
@Getter @Setter    private static String ServerURL = "www.example.com";
@Getter @Setter    private static String botAccessToken = "Insert Access Token";
@Getter @Setter    private static String sparkWidgetAccessToken = "Insert Access Token";
@Getter @Setter    private static String webhookMessageroute = "webhook/messages";
@Getter @Setter    private static String webhookRoomsroute = "webhook/rooms";
@Getter @Setter    private static String staticWebFileLocation = "/web";  
@Getter @Setter    private static String botUserName = "maja@sparkbot.io";
@Getter @Setter    private static String postgresUser = "postgres";
@Getter @Setter    private static String postgresPassword = "postgrespassword";
    

    public static int getServerPort() {
        int value = Integer.parseInt(SystemConfig.serverPort);
        return value;
    }

    public static void setServerPort(Integer serverPort) {
        SystemConfig.serverPort = serverPort.toString();
    }
   
    public static String getWebhookMessageLink() {
        return "http://" + ServerURL + ":" + serverPort + "/" + webhookMessageroute;
    }

    public static String getWebhookRoomsLink() {
        return "http://" + ServerURL + ":" + serverPort + "/" + webhookRoomsroute;
    }    
 
    public static void loadconfig ()  {
        
    Properties prop = new Properties();
	InputStream input = null;

	try {

		input = new FileInputStream(mConfigFile);

		// load a properties file
		prop.load(input);

		// get the property values and copy to class
                SystemConfig.ServerURL = prop.getProperty("ServerURL");
                SystemConfig.botAccessToken = prop.getProperty("BotAccessToken");
                SystemConfig.sparkWidgetAccessToken = prop.getProperty("SparkWidgetAccessToken");
                SystemConfig.serverPort = prop.getProperty("ServerPort");
                SystemConfig.staticWebFileLocation = prop.getProperty("StaticWebFileLocation");
                SystemConfig.webhookMessageroute = prop.getProperty("WebhookMessageroute");
                SystemConfig.webhookRoomsroute = prop.getProperty("WebhookRoomsroute");
                SystemConfig.botUserName = prop.getProperty("BotUserName");
                SystemConfig.postgresUser = prop.getProperty("PostgresUser");
                SystemConfig.postgresPassword = prop.getProperty("PostgresPassword");
                
        
                LOGGER.info("loadconfig successful");
                LOGGER.debug("Loadconfig ServerURL : " + SystemConfig.ServerURL);
                LOGGER.debug("Loadconfig BotAccessToken : " + SystemConfig.botAccessToken);
                LOGGER.debug("Loadconfig sparkWidgetAccessToken : " + SystemConfig.sparkWidgetAccessToken);
                LOGGER.debug("Loadconfig serverPort : " + SystemConfig.serverPort);
                LOGGER.debug("Loadconfig staticWebFileLocation : " + SystemConfig.staticWebFileLocation);
                LOGGER.debug("Loadconfig webhookMessageroute : " + SystemConfig.webhookMessageroute);
                LOGGER.debug("Loadconfig webhookRoomsroute : " + SystemConfig.webhookRoomsroute);
                LOGGER.debug("Loadconfig botUserName : " + SystemConfig.botUserName);
                LOGGER.debug("Loadconfig postgresUser : " + SystemConfig.postgresUser);
                LOGGER.debug("Loadconfig postgresPassword : " + SystemConfig.postgresPassword);
                
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				LOGGER.log(Level.ERROR,e);
			}
		}
	}
    } 

   
    public static void writeconfig () { 
    
        Properties prop = new Properties();
	OutputStream output = null;

	try {

		output = new FileOutputStream(mConfigFile);
                

		// set the properties value
                prop.setProperty("ServerURL", SystemConfig.ServerURL);
                prop.setProperty("BotAccessToken", SystemConfig.botAccessToken);
                prop.setProperty("SparkWidgetAccessToken", SystemConfig.sparkWidgetAccessToken);
                prop.setProperty("BotUserName", SystemConfig.botUserName);
                prop.setProperty("ServerPort", SystemConfig.serverPort);
                prop.setProperty("StaticWebFileLocation", SystemConfig.staticWebFileLocation);
		prop.setProperty("WebhookMessageroute", SystemConfig.webhookMessageroute);
                prop.setProperty("WebhookRoomsroute", SystemConfig.webhookRoomsroute);
                prop.setProperty("PostgresUser", SystemConfig.postgresUser);
                prop.setProperty("PostgresPassword", SystemConfig.postgresPassword);

                
                
		// save properties to project config folder
		prop.store(output, null);
                
                LOGGER.info("Config file written");
                LOGGER.debug("Config file written : " + output);
                

	} catch (IOException io) {
		io.printStackTrace();
	} finally {
		if (output != null) {
			try {
                            output.close();
			} catch (IOException e) {
                            LOGGER.log(Level.ERROR,"could'nt write config file ",e);
			}
		}

	}
    }
}
