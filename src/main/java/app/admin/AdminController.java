package app.admin;

import app.bot.BotLogic;
import app.db.User;
import app.login.LoginController;
import app.util.SystemConfig;
import app.util.Filters;
import app.util.JsonUtil;
import app.util.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import spark.*;
import java.util.*;
import javax.servlet.MultipartConfigElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

public class AdminController {
    private static final Logger LOGGER = LogManager.getLogger();

    public static Route serveAdminUserPage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.ADMINUSER + " get request");
        LOGGER.debug(LinkPath.Web.ADMINUSER + " get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, LinkPath.Template.ADMINUSER);
    };
    
        public static Route serveAdminReportPage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.ADMINREPORT + " get request");
        LOGGER.debug(LinkPath.Web.ADMINREPORT + " get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
            model.put("Botrequestcounter", BotLogic.getBotrequestcounter());
            model.put("Roomamount", CiscoSpark.getRoomamount());
            model.put("SystemUpTime", SystemConfig.getSystemUpTime());
            model.put("UsedMemory", SystemConfig.getSystemUsedMemory());
            model.put("Webrequests", Filters.getWebrequests());
        return ViewUtil.render(request, model, LinkPath.Template.ADMINREPORT);
    };
    
    public static Route serveAdminDesignPage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.ADMINDESIGN + " get request");
        LOGGER.debug(LinkPath.Web.ADMINDESIGN + " get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, LinkPath.Template.ADMINDESIGN);
    };
    
    public static Route serveRestListUsers = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTLISTUSERS +" post request");
        LOGGER.debug(LinkPath.Web.RESTLISTUSERS + " post request : " + request.body());
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
        LOGGER.info(LinkPath.Web.RESTCREATEUSER +" post request");
        LOGGER.debug(LinkPath.Web.RESTCREATEUSER + " post request : " + request.body());
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
        LOGGER.info(LinkPath.Web.RESTUPDATEUSER +" post request");
        LOGGER.debug(LinkPath.Web.RESTUPDATEUSER + " post request : " + request.body());
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
        LOGGER.info(LinkPath.Web.RESTDELETEUSER +" post request");
        LOGGER.debug(LinkPath.Web.RESTDELETEUSER + " post request : " + request.body());
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
        
     public static Route serveRestUploadUserImage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTUPLOADUSERIMAGE +" post request");
        LOGGER.debug(LinkPath.Web.RESTUPLOADUSERIMAGE + " post request : " + request.body());
        File uploadDir = new File("upload");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist
        Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
        
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
        try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) {
            LOGGER.info("Test von Marko");
            // Use the input stream to create a file
            Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            
        } catch (Exception e) {
            LOGGER.error("could'nt write user image file ",e);
        }
        LOGGER.info("Uploaded file '" + getFileName(request.raw().getPart("uploaded_file")) + "' saved as '" + tempFile.toAbsolutePath() + "'");
        return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName() + "'>";
    };   

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    
}