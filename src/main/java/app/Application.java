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
package app;

import app.admin.AdminController;
import app.bot.BotLogic;
import app.bot.*;
import app.contact.ContactController;
import app.consultant.ConsultantController;
import app.index.IndexController;
import app.login.LoginController;
import app.rest.RestController;
import app.util.CiscoSpark;
import app.util.SystemConfig;
import app.util.Filters;
import app.util.Path;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import static spark.Spark.*;
import static spark.debug.DebugScreen.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Application {
    
    //public static final Logger logger = LogManager.getLogger();
    
    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        
                
        //-------------------setup logging infrastructure-----------------------
        System.setProperty("log4j.configurationFile","config/log4j2.xml");
        Logger logger = LogManager.getLogger();
        //Configurator.setLevel("root.Application", Level.DEBUG);
        
        //----------------get absolute location path on OS----------------------
        File jarPath=new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String locationPath=jarPath.getParentFile().getAbsolutePath();
        logger.info("Location Path : " + locationPath);
        
        
        //----------------General initiations-----------------------------------
        SystemConfig.loadconfig();
        BotLogic.initranslate();
        CiscoSpark.setWebhookMessageLink(SystemConfig.getWebhookMessageLink());
        CiscoSpark.setWebhookRoomsLink(SystemConfig.getWebhookRoomsLink());    
        CiscoSpark.ciscoSparkIni(SystemConfig.getBotAccessToken()); // Spark Object ini and access code from config file    
        logger.info("Spark initialized");
        
        //--------------start Spark web server and set defaults-----------------
        //staticFiles.externalLocation(locationPath + SystemConfig.getStaticWebFileLocation());
        //String layout = locationPath + SystemConfig.LAYOUT;
        staticFileLocation(SystemConfig.getStaticWebFileLocation());
        port(SystemConfig.getServerPort());
        staticFiles.expireTime(60L);
        enableDebugScreen();

        
        
        //-------Set up before-filters (called before each get/post)------------
        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);
        before(Path.Web.SLASH,       Filters.forwardtoIndex);
        
        //-----------------Set up Links and point to the controllers------------
        get(Path.Web.INDEX,          IndexController.serveIndexPage);
        get(Path.Web.CONTACT,        ContactController.serveContactPage);
        get(Path.Web.CONSULTANT,     ConsultantController.serveConsultantPage);
        get(Path.Web.SPARKWIDGET,    ConsultantController.serveSparkWidget);
        post(Path.Web.BOTMESSAGE,    BotController.serveBotMessage);
        post(Path.Web.BOTROOMS,      BotController.serveBotRooms);
        get(Path.Web.REST,           RestController.serveRestAPI);
        get(Path.Web.LOGIN,          LoginController.serveLoginPage);
        post(Path.Web.LOGIN,         LoginController.handleLoginPost);
        post(Path.Web.LOGOUT,        LoginController.handleLogoutPost);
        get(Path.Web.ADMINUSER,      AdminController.serveAdminUserPage);
        get(Path.Web.ADMINREPORT,    AdminController.serveAdminReportPage);
        
        after("*",                   Filters.addGzipHeader);
                 

    }
    
}

