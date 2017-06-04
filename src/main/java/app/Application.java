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


import app.book.BookDao;
import app.bot.BotLogic;
import app.user.UserDao;
import app.util.CiscoSpark;
import app.util.VelocityTemplateEngine;
import app.util.Config;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import static spark.Spark.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import org.apache.logging.log4j.Level;
import spark.ModelAndView;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.json.simple.JSONObject;


        

/**
 *
 * @author mjamelle
 */
public class Application {
    
    //public static final Logger logger = LogManager.getLogger();
    public static BookDao bookDao;  //Test for webtemplate migration
    public static UserDao userDao;  //Test for webtemplate migration
    
    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        
                
        //setup logging infrastructure
        System.setProperty("log4j.configurationFile","config/log4j2.xml");
        Logger logger = LogManager.getLogger();
        //Configurator.setLevel("root.Application", Level.DEBUG);
        
        //absolute location path on OS
        File jarPath=new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String locationPath=jarPath.getParentFile().getAbsolutePath();
        logger.info("Location Path : " + locationPath);
        
        //start Spark web server and set defaults
        //staticFiles.externalLocation(locationPath + Config.WEBFILELOCATION);
        //String layout = locationPath + Config.LAYOUT;
        staticFileLocation(Config.WEBFILELOCATION);
        String layout = Config.LAYOUT;
        port(Config.PORT);
        
        //general inits
        BotLogic.initranslate();
        Config config = new Config ();  //config file ini
        CiscoSpark.setWebhookMessageLink(config.getWebhookMessageLink());
        CiscoSpark.setWebhookRoomsLink(config.getWebhookRoomsLink());       
        CiscoSpark.ciscoSparkIni(config.getAccessToken()); // Spark Object ini and access code from config file    
        logger.info("Spark initilized");
        
        
        //web routes responses---------------------------------------------------
        get("/", (request, response) -> {
            logger.info("/ Web request");
            logger.debug("/ Web request : " + request.body());
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("template", "/web/Welcome.html" );
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());
        
        get("/setup", (request, response) -> {
            logger.info("/setup Web request");
            logger.debug("/setup Web request : " + request.body());
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("template", "/web/Setup_form.html");
            model.put("AccessToken", config.getAccessToken());
            model.put("ServerURL", config.getServerURL());
            model.put("ServerPort", config.getServerPort());
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());
 
        get("/setup_submit", (request, response) -> {
            logger.info("/setup_submit Web request");
            logger.debug("/setup_submit Web request : " + request.body());
            config.setAccessToken(request.queryParams("AccessToken"));
            config.setServerURL(request.queryParams("ServerURL"));
            config.setServerPort(request.queryParams("ServerPort")); 
            config.writeconfig();
            response.redirect("/");
            return "ok";
        });

        get("/info", (request, response) -> {
            logger.info("/info Web request");
            logger.debug("/info Web request : " + request.body());
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("template", "/web/Info_form.html");
            model.put("Botrequestcounter", BotLogic.getBotrequestcounter());
            model.put("Roomamount", CiscoSpark.getRoomamount());
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());
              
        post("webhook/messages", (request, response) -> {
            logger.info("/webhook/messages Web request");
            logger.debug("/webhook/messages Web request : " + request.body());
            BotLogic.webHookMessageTrigger(request);
            response.status(200);
            return "ok";  
        });
               
        post("/webhook/rooms", (request, response) -> {
            logger.info("/webhook/rooms Web request");
            logger.debug("/webhook/rooms Web request : " + request.body());
            BotLogic.webHookRoomsTrigger(request);
            response.status(200);
            return "ok";  
        });
        
        // REST Implementation
        get("/rest/config", (request, response) -> {
            logger.info("/rest/config Web request");
            logger.debug("/rest/config Web request : " + request.body());
            response.status(200);
            response.type("application/json");
            
            //load config into JSONObject
            JSONObject obj = new JSONObject();
            obj.put("Botrequest", BotLogic.getBotrequestcounter());
            obj.put("Rooms", CiscoSpark.getRoomamount());
            logger.debug("/rest/config Web response : " + obj.toJSONString());
            return obj.toJSONString();  
        });
        
        
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
          get("/hello/:name", (request, response) -> {
            return "Hello: " + request.params(":name");
          });
    }
    
}

