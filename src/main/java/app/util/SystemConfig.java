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
import app.Application;


import java.io.FileInputStream;
import java.io.FileOutputStream;        
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import org.apache.logging.log4j.Level;


public class SystemConfig {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String mConfigFile = "config/config.properties";  
                           private static String webUserSessiontimeout = "600";   
        @Getter @Setter    private static URL    ServerURL;
        @Getter @Setter    private static String majaBotAccessToken;
        @Getter @Setter    private static String bankBotAccessToken;
        @Getter @Setter    private static String sparkWidgetAccessToken;
        @Getter @Setter    private static String webhookMessageroute = "/webhook/messages"; 
        @Getter @Setter    private static String webhookRoomsroute = "/webhook/rooms";
        @Getter @Setter    private static String staticWebFileLocation = "/web";
        @Getter @Setter    private static String staticFilesExternalLocation = "web";
        @Getter @Setter    private static String postgresUser = "postgres";
        @Getter @Setter    private static String postgresPassword = "postgrespassword"; 
        @Getter @Setter    private static String postgresDBLink = "jdbc:postgresql://localhost:5432/onebank";
        @Getter @Setter    private static URL    SparkApiUrl;        
        @Getter @Setter    private static URL    oauthAuthorizationLocation;
        @Getter @Setter    private static URL    oauTokenLocation;
        @Getter @Setter    private static String oauthClientId;
        @Getter @Setter    private static String oauthClientSecret;
        @Getter @Setter    private static URL    oauthRedirectURI;
        @Getter @Setter    private static String guestIssuerID;
        @Getter @Setter    private static String guestIssuerSharedSecret;

    
    public static int getwebUserSessiontimeout() {
        int value = Integer.parseInt(SystemConfig.webUserSessiontimeout);
        return value;
    }

    public static void setwebUserSessiontimeout(Integer sessionTimeout) {
        SystemConfig.webUserSessiontimeout = sessionTimeout.toString();
    }
   
    public static String getWebhookMessageLink() {
        return ServerURL + webhookMessageroute;
    }

    public static String getWebhookRoomsLink() {
        return ServerURL + webhookRoomsroute;
    }    
 
    public static String getSystemUpTime()  {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        long seconds = mxBean.getUptime() / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String time = days + "Days : " + hours % 24 + "Hours : " + minutes % 60 + "Minutes";  
    return time;
    }
    
    public static String getSystemUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        long memory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        memory = memory/1024/1024;
        String showusedMemory = Long.toString(memory);
    return showusedMemory;
    }
    
    public static String getSystemName() {
        String jarName = new java.io.File (Application.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                .getName(); 
        return jarName;
    }
    
    public static void loadconfig ()  {
        
        Properties prop = new Properties();
	InputStream input = null;

	try {
		input = new FileInputStream(mConfigFile);

		// load a properties file
		prop.load(input);

		// get the property values and copy to class
                ServerURL = new URL(prop.getProperty("ServerURL"));
                majaBotAccessToken = prop.getProperty("MajaBotAccessToken");
                bankBotAccessToken = prop.getProperty("BankBotAccessToken");
                sparkWidgetAccessToken = prop.getProperty("SparkWidgetAccessToken");
                staticWebFileLocation = prop.getProperty("StaticWebFileLocation");
                webhookMessageroute = prop.getProperty("WebhookMessageroute");
                webhookRoomsroute = prop.getProperty("WebhookRoomsroute");
                postgresUser = prop.getProperty("PostgresUser");
                postgresPassword = prop.getProperty("PostgresPassword");
                postgresDBLink = prop.getProperty("PostgresDBLink");
                webUserSessiontimeout = prop.getProperty("WebUserSessiontimeout");
                SparkApiUrl = new URL(prop.getProperty("SparkApiUrl"));        
                oauthAuthorizationLocation = new URL(prop.getProperty("OauthAuthorizationLocation"));
                oauTokenLocation = new URL(prop.getProperty("OauthTokenLocation"));
                oauthClientId = prop.getProperty("OauthClientId");
                oauthClientSecret = prop.getProperty("OauthClientSecret");
                oauthRedirectURI = new URL(prop.getProperty("OauthRedirectURI"));
                guestIssuerID = prop.getProperty("GuestIssuerID");
                guestIssuerSharedSecret = prop.getProperty("GuestIssuerSharedSecret");
                
	} catch (IOException ex) {
            LOGGER.error(ex);                
	} catch (Exception e) {
            LOGGER.error(e);
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
            prop.setProperty("ServerURL", ServerURL.toString());
            prop.setProperty("MajaBotAccessToken", majaBotAccessToken);
            prop.setProperty("BankBotAccessToken", bankBotAccessToken);
            prop.setProperty("SparkWidgetAccessToken", sparkWidgetAccessToken);
            prop.setProperty("StaticWebFileLocation", staticWebFileLocation);
            prop.setProperty("WebhookMessageroute", webhookMessageroute);
            prop.setProperty("WebhookRoomsroute", webhookRoomsroute);
            prop.setProperty("PostgresUser", postgresUser);
            prop.setProperty("PostgresPassword", postgresPassword);
            prop.setProperty("WebUserSessiontimeout", webUserSessiontimeout);

            // save properties to project config folder
            prop.store(output, null);

            LOGGER.info("Config file written");
            LOGGER.debug("Config file written : " + output);              
	} catch (IOException io) {
            LOGGER.error(io);
	} catch (Exception e) {
            LOGGER.error(e);
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
