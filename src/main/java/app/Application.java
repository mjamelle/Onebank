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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package app;

import app.admin.AdminController;
import app.bot.*;
import app.contact.ContactController;
import app.consultant.ConsultantController;
import app.index.IndexController;
import app.login.LoginController;
import app.rest.RestController;
import app.util.*;
import java.io.File;
import static spark.Spark.*;
import static spark.debug.DebugScreen.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Application {
    static public CiscoSpark majabot;
    static public CiscoSpark bankdemobot;
    
    //public static final Logger logger = LogManager.getLogger();
    
    public static void main(String[] args) {
        
                
        //-------------------setup logging infrastructure-----------------------
        System.setProperty("log4j.configurationFile","config/log4j2.xml");
        Logger logger = LogManager.getLogger();
        logger.info("-----------------------------APPLICATION  START-------------------------------");
        
        //----------------get absolute location path on OS----------------------
        File jarPath = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String locationPath=jarPath.getParentFile().getAbsolutePath();
        logger.info("Location Path : " + locationPath);
        
        
        //----------------General initiations-----------------------------------
        SystemConfig.loadconfig();
        
        try {
        BotController.initranslate(); 
        //CiscoSpark.ciscoSparkIni(SystemConfig.getMajaBotAccessToken()); // Spark Object ini and access code from config file    
        majabot = new CiscoSpark(SystemConfig.getMajaBotAccessToken(), "Maja Webhook");
        logger.info("CiscoSpark Bot Maja initialized");
        bankdemobot = new CiscoSpark(SystemConfig.getBankBotAccessToken(), "BankBotWebhook");
        logger.info("CiscoSpark BankBot initialized");
        } catch (Exception e) {
            logger.info("CiscoSpark Bot initialization failed :  " + e);
        }
        
        //--------------start Spark web server and set defaults-----------------
        //staticFiles.externalLocation(locationPath + SystemConfig.getStaticWebFileLocation());
        //String layout = locationPath + SystemConfig.LAYOUT;
        secure("config/keystore.jks", "supermarko", null, null);
        staticFileLocation(SystemConfig.getStaticWebFileLocation());
        staticFiles.externalLocation(SystemConfig.getStaticFilesExternalLocation());  //upload folder for user images
        port(SystemConfig.getServerURL().getPort());
        staticFiles.expireTime(60);
        enableDebugScreen();
        
        
        //-------Set up before-filters (called before each get/post)------------
        //before("*",                  Filters.addTrailingSlashes);
        before("*",                             Filters.handleLocaleChange);
        before(LinkPath.Web.INDEX,              Filters.newSessions);
        before(LinkPath.Web.CONTACT,            Filters.newSessions);
        before(LinkPath.Web.CONSULTANT,         Filters.newSessions);
        before(LinkPath.Web.IMMO_BOT,           Filters.newSessions);
        before(LinkPath.Web.INDEX,              LoginController.checkNewUser);
        before(LinkPath.Web.CONTACT,            LoginController.checkNewUser);
        before(LinkPath.Web.CONSULTANT,         LoginController.checkNewUser);
        before(LinkPath.Web.IMMO_BOT,           LoginController.checkNewUser);
        before(LinkPath.Web.SLASH,              Filters.forwardtoIndex);
        
        //-----------------Set up Links and point to the controllers------------
        get(LinkPath.Web.INDEX,                 IndexController.serveIndexPage);
        get(LinkPath.Web.CONTACT,               ContactController.serveContactPage);
        get(LinkPath.Web.CONSULTANT,            ConsultantController.serveConsultantPage);
        get(LinkPath.Web.IMMO_BOT,              ConsultantController.serveImmoBotPage);
        get(LinkPath.Web.SPARKWIDGET,           ConsultantController.serveSparkWidget);
        post(LinkPath.Web.BOTMESSAGE,           BotController.serveBotMessage);
        post(LinkPath.Web.BOTROOMS,             BotController.serveBotRooms);
        get(LinkPath.Web.REST,                  RestController.serveRestAPI);
        get(LinkPath.Web.LOGIN,                 LoginController.serveLoginPage);
        post(LinkPath.Web.LOGIN,                LoginController.handleLoginPost);
        post(LinkPath.Web.LOGOUT,               LoginController.handleLogoutPost);
        get(LinkPath.Web.RESTSPARKOAUTH,        LoginController.handleOAuthresponse);
        get(LinkPath.Web.ADMINDESIGN,           AdminController.serveAdminDesignPage);
        get(LinkPath.Web.ADMINUSER,             AdminController.serveAdminUserPage);
        get(LinkPath.Web.ADMINREPORT,           AdminController.serveAdminReportPage);
        
        
        //--------------------RESTFUL API-----------------------------------------
        post(LinkPath.Web.RESTLISTUSERS,                AdminController.serveRestListUsers);
        post(LinkPath.Web.RESTCREATEUSER,               AdminController.serveRestCreateUsers);
        post(LinkPath.Web.RESTUPDATEUSER,               AdminController.serveRestUpdateUsers);
        post(LinkPath.Web.RESTDELETEUSER,               AdminController.serveRestDeleteUsers);
        post(LinkPath.Web.RESTUPLOADUSERIMAGE,          AdminController.serveRestUploadUserImage);
        post(LinkPath.Web.RESTDESIGNCUSTOM,             AdminController.serveRestDesignCustom);
        post(LinkPath.Web.RESTUPLOADBACKGROUNDIMAGE,    AdminController.serveRestUploadBackgroundImage);
        post(LinkPath.Web.RESTTRANSLATE,                BotController.serveTranslate);

        get("*",                     ViewUtil.notFound);
        
        after("*",                   Filters.addGzipHeader);      
    };   
}

