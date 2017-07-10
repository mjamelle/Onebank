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

public class AdminController {
    private static final Logger LOGGER = LogManager.getLogger();

    public static Route serveAdminUserPage = (Request request, Response response) -> {
        LOGGER.info("/admin/ get request");
        LOGGER.debug("/admin/ get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.ADMINUSER);
    };
    
        public static Route serveAdminReportPage = (Request request, Response response) -> {
        LOGGER.info("/report/ get request");
        LOGGER.debug("/report/ get request : " + request.body());
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
        LOGGER.info("/design/ get request");
        LOGGER.debug("/design/ get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.ADMINDESIGN);
    };
    
    public static Route serveAdminListUsers = (Request request, Response response) -> {
        LOGGER.info("/rest/listuser/ get request");
        LOGGER.debug("/rest/listuser/ get request : " + request.body());
        response.type("application/json");
        RestUserallResponse result = new RestUserallResponse();
        try { //TBD throw exception in case of issues and setResult to ERROR
            result.setRecords(User.all());
            result.setResult("OK");
        } catch (Exception ex) {
            result.setResult("ERROR");
        }      
        return JsonUtil.dataToJson(result);
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