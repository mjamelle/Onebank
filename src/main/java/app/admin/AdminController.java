package app.admin;

import app.bot.BotLogic;
import app.db.User;
import app.login.LoginController;
import app.user.UserController;
import app.util.SystemConfig;
import app.util.Filters;
import app.util.JsonUtil;
import app.util.*;
import spark.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

public class AdminController {
    private static final Logger LOGGER = LogManager.getLogger();

    public static Route serveAdminUserPage = (Request request, Response response) -> {
        LOGGER.info(Path.Web.ADMINUSER + " get request");
        LOGGER.debug(Path.Web.ADMINUSER + " get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.ADMINUSER);
    };
    
        public static Route serveAdminReportPage = (Request request, Response response) -> {
        LOGGER.info(Path.Web.ADMINREPORT + " get request");
        LOGGER.debug(Path.Web.ADMINREPORT + " get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
            model.put("Botrequestcounter", BotLogic.getBotrequestcounter());
            model.put("Roomamount", CiscoSpark.getRoomamount());
            model.put("SystemUpTime", SystemConfig.getSystemUpTime());
            model.put("UsedMemory", SystemConfig.getSystemUsedMemory());
            model.put("Webrequests", Filters.getWebrequests());
        return ViewUtil.render(request, model, Path.Template.ADMINREPORT);
    };
    
    public static Route serveAdminDesignPage = (Request request, Response response) -> {
        LOGGER.info(Path.Web.ADMINDESIGN + " get request");
        LOGGER.debug(Path.Web.ADMINDESIGN + " get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.ADMINDESIGN);
    };
    
    public static Route serveRestListUsers = (Request request, Response response) -> {
        LOGGER.info(Path.Web.RESTLISTUSERS +" post request");
        LOGGER.debug(Path.Web.RESTLISTUSERS + " post request : " + request.body());
        response.type("application/json");
        RestUserallResponse result = new RestUserallResponse();
        try {
            result.setRecords(User.all());
            result.setResult("OK");
        } catch (Exception ex) {
            result.setResult("ERROR");
        }      
        return JsonUtil.dataToJson(result);
    };  
    
        public static Route serveRestCreateUsers = (Request request, Response response) -> {
        LOGGER.info(Path.Web.RESTCREATEUSER +" post request");
        LOGGER.debug(Path.Web.RESTCREATEUSER + " post request : " + request.body());
        response.type("application/json");
        RestcreateUserResponse result = new RestcreateUserResponse();
        User user = new  User();
        try {
            user.setGivenName(request.queryParams("givenName"));
            user.setSurName(request.queryParams("surName"));
            user.setUsername(request.queryParams("username"));
            user.setPassword(request.queryParams("password"));
            user.setPhotolink(request.queryParams("photolink"));
            user.setEmail(request.queryParams("email"));
            user.setFunction(request.queryParams("function"));
            user.setJabber_use(Boolean.parseBoolean(request.queryParams("jabber_use")));
            user.setSpark_use(Boolean.parseBoolean(request.queryParams("spark_use")));
            user.setAdminprivilege(Boolean.parseBoolean(request.queryParams("adminprivilege")));
            user.save();
            result.setRecord(User.find(user.getId()));
            result.setResult("OK");
        } catch (Exception ex) {
            result.setResult("ERROR");
        }      
        return JsonUtil.dataToJson(result);
    }; 
        
        public static Route serveRestUpdateUsers = (Request request, Response response) -> {
        LOGGER.info(Path.Web.RESTUPDATEUSER +" post request");
        LOGGER.debug(Path.Web.RESTUPDATEUSER + " post request : " + request.body());
        JSONObject obj = new JSONObject();
        response.type("application/json");
        User user = new  User();
        try {
            user.setId(Integer.parseInt(request.queryParams("id")));
            user.setGivenName(request.queryParams("givenName"));
            user.setSurName(request.queryParams("surName"));
            user.setUsername(request.queryParams("username"));
            user.setPassword(request.queryParams("password"));
            user.setPhotolink(request.queryParams("photolink"));
            user.setEmail(request.queryParams("email"));
            user.setFunction(request.queryParams("function"));
            user.setJabber_use(Boolean.parseBoolean(request.queryParams("jabber_use")));
            user.setSpark_use(Boolean.parseBoolean(request.queryParams("spark_use")));
            user.setAdminprivilege(Boolean.parseBoolean(request.queryParams("adminprivilege")));
            user.update();
            obj.put("Result", "OK");
        } catch (Exception ex) {
            obj.put("Result", "ERROR");
        }      
        return obj.toJSONString();
    }; 
            
        public static Route serveRestDeleteUsers = (Request request, Response response) -> {
        LOGGER.info(Path.Web.RESTDELETEUSER +" post request");
        LOGGER.debug(Path.Web.RESTDELETEUSER + " post request : " + request.body());
        JSONObject obj = new JSONObject();
        response.type("application/json");
        User user = new  User();
        try {
            user.setId(Integer.parseInt(request.queryParams("id")));
            user.delete();
            obj.put("Result", "OK");
        } catch (Exception ex) {
            obj.put("Result", "ERROR");
        }      
        return obj.toJSONString();
    }; 
        
        
    /*
        New code for Onebank
          
          post("/yourUploadPath", (request, response) -> {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try (InputStream is = request.raw().getPart("uploaded_file").getInputStream()) {
                // Use the input stream to create a file
            }
            return "File uploaded";
          });
         
        
*/
    
}