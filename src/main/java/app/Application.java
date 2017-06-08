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

import app.bot.BotLogic;
import app.bot.*;
import app.contact.ContactController;
import app.consultant.ConsultantController;
import app.index.IndexController;
import app.rest.RestController;
import app.util.CiscoSpark;
import app.util.Config;
import app.util.Filters;
import app.util.Path;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import static spark.Spark.*;
import static spark.debug.DebugScreen.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;


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
        
        //--------------start Spark web server and set defaults-----------------
        //staticFiles.externalLocation(locationPath + Config.WEBFILELOCATION);
        //String layout = locationPath + Config.LAYOUT;
        staticFileLocation(Config.WEBFILELOCATION);
        port(Config.PORT);
        enableDebugScreen();
        
        //----------------General Initiations-----------------------------------
        BotLogic.initranslate();
        Config config = new Config ();  //config file ini
        CiscoSpark.setWebhookMessageLink(config.getWebhookMessageLink());
        CiscoSpark.setWebhookRoomsLink(config.getWebhookRoomsLink());       
        CiscoSpark.ciscoSparkIni(config.getAccessToken()); // Spark Object ini and access code from config file    
        logger.info("Spark initilized");
        
        
        //-------Set up before-filters (called before each get/post)------------
        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);
        
        //-----------------Setup redirect link to index page--------------------
        get("/", (request, response) -> {
           response.redirect("/index"); 
           return null;
        });
        
        //-----------------Set up Links and point to the controllers------------
        get(Path.Web.INDEX,          IndexController.serveIndexPage);
        get(Path.Web.CONTACT,        ContactController.serveContactPage);
        get(Path.Web.CONSULTANT,     ConsultantController.serveConsultantPage);
        post(Path.Web.BOTMESSAGE,    BotController.serveBotMessage);
        post(Path.Web.BOTROOMS,      BotController.serveBotRooms);
        get(Path.Web.REST,           RestController.serveRestAPI);
        
        after("*",                   Filters.addGzipHeader);
        

        
        // New code for Onebank
        
        /*
           post("/tasks", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Category category = Category.find(Integer.parseInt(request.queryParams("categoryId")));
            String description = request.queryParams("description");
            Consultant newTask = new Consultant(description, category.getId());
            newTask.save();
            model.put("category", category);
            model.put("template", "templates/category-task-success.vtl");
            return new ModelAndView(model, layout);
          }, new VelocityTemplateEngine());

          get("/tasks/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Consultant task = Consultant.find(Integer.parseInt(request.params(":id")));
            model.put("task", task);
            model.put("template", "templates/task.vtl");
            return new ModelAndView(model, layout);
          }, new VelocityTemplateEngine());

          post("/categories", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String name = request.queryParams("name");
            Category newCategory = new Category(name);
            newCategory.save();
            model.put("template", "templates/category-success.vtl");
            return new ModelAndView(model, layout);
          }, new VelocityTemplateEngine());

          get("/categories", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("categories", Category.all());
            model.put("template", "templates/categories.vtl");
            return new ModelAndView(model, layout);
          }, new VelocityTemplateEngine());
          
          post("/yourUploadPath", (request, response) -> {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                // Use the input stream to create a file
            }
            return "File uploaded";
          });
         
        
*/
        
          

    }
    
}

