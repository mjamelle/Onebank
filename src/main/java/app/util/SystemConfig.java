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


import java.io.FileInputStream;
import java.io.FileOutputStream;        
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.logging.log4j.Level;


public class SystemConfig {
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static String mConfigFile = "config/config.properties";
    private static String ServerURL = "www.example.com";
    private static String botAccessToken = "Insert Access Token";
    private static String sparkWidgetAccessToken = "Insert Access Token";
    private static String serverPort = "4567";
    private static String webhookMessageroute = "webhook/messages";
    private static String webhookRoomsroute = "webhook/rooms";
    private static String staticWebFileLocation = "/web";  
    private static String botUserName = "maja@sparkbot.io";
    private static String postgresUser = "postgres";
    private static String postgresPassword = "postgrespassword";
    


    
    
    public SystemConfig() {
       loadconfig();
    }
    
    public SystemConfig(String mFilename) {
       SystemConfig.mConfigFile = mFilename; 
       loadconfig();
    }

    public static String getmConfigFile() {
        return mConfigFile;
    }

    public static void setmConfigFile(String mConfigFile) {
        SystemConfig.mConfigFile = mConfigFile;
    }

    public static String getWebhookMessageroute() {
        return webhookMessageroute;
    }

    public static void setWebhookMessageroute(String webhookMessageroute) {
        SystemConfig.webhookMessageroute = webhookMessageroute;
    }

    public static String getWebhookRoomsroute() {
        return webhookRoomsroute;
    }

    public static void setWebhookRoomsroute(String webhookRoomsroute) {
        SystemConfig.webhookRoomsroute = webhookRoomsroute;
    }

    public static String getStaticWebFileLocation() {
        return staticWebFileLocation;
    }

    public static void setStaticWebFileLocation(String staticWebFileLocation) {
        SystemConfig.staticWebFileLocation = staticWebFileLocation;
    }

    public static int getServerPort() {
        int value = Integer.parseInt(SystemConfig.serverPort);
        return value;
    }

    public static void setServerPort(Integer serverPort) {
        SystemConfig.serverPort = serverPort.toString();
    }

    public static String getBotAccessToken() {
        return botAccessToken;
    }

    public static void setBotAccessToken(String botAccessToken) {
        SystemConfig.botAccessToken = botAccessToken;
    }

    public static String getServerURL() {
        return ServerURL;
    }

    public static void setServerURL(String ServerURL) {
        SystemConfig.ServerURL = ServerURL;
    }

    public static String getBotUserName() {
        return botUserName;
    }

    public static void setBotUserName(String botUserName) {
        SystemConfig.botUserName = botUserName;
    }

    public static String getSparkWidgetAccessToken() {
        return sparkWidgetAccessToken;
    }

    public static void setSparkWidgetAccessToken(String sparkWidgetAccessToken) {
        SystemConfig.sparkWidgetAccessToken = sparkWidgetAccessToken;
    }

    public static String getPostgresUser() {
        return postgresUser;
    }

    public static void setPostgresUser(String postgresUser) {
        SystemConfig.postgresUser = postgresUser;
    }

    public static String getPostgresPassword() {
        return postgresPassword;
    }

    public static void setPostgresPassword(String postgresPassword) {
        SystemConfig.postgresPassword = postgresPassword;
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
