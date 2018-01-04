package app.admin;

import static app.Application.bankdemobot;
import app.bot.BotController;
import app.db.User;
import app.login.LoginController;
import app.util.SystemConfig;
import app.util.Filters;
import app.util.JsonUtil;
import app.util.*;
import javax.servlet.*;
import java.io.*;
import java.nio.file.*;
import spark.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import static app.Application.majabot;

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
            model.put("Botrequestcounter", BotController.getBotrequestcounter());
            model.put("Translatencounter", BotController.getTranslatencounter());
            model.put("Majarooms", majabot.getRoomamount());
            model.put("Bankdemorooms", bankdemobot.getRoomamount());
            model.put("SystemUpTime", SystemConfig.getSystemUpTime());
            model.put("UsedMemory", SystemConfig.getSystemUsedMemory());
            model.put("SystemName", SystemConfig.getSystemName());
            model.put("Webrequests", Filters.getWebSessions());
        return ViewUtil.render(request, model, LinkPath.Template.ADMINREPORT);
    };
    
    public static Route serveAdminDesignPage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.ADMINDESIGN + " get request");
        LOGGER.debug(LinkPath.Web.ADMINDESIGN + " get request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, LinkPath.Template.ADMINDESIGN);
    };
    
    
    public static Route serveRestDesignCustom = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTDESIGNCUSTOM + " post request");
        LOGGER.debug(LinkPath.Web.RESTDESIGNCUSTOM + " post request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        WebCustomize.setCompanyname(request.queryParams("companyname"));
        WebCustomize.setBackgroundcustomized(request.queryParams("backgroundcustomized"));
        LOGGER.info("Companybackground set to  :  " + request.queryParams("backgroundcustomized"));
        return "successful";
    };    
    
    public static Route serveRestListUsers = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTLISTUSERS +" post request");
        LOGGER.debug(LinkPath.Web.RESTLISTUSERS + " post request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        String jtStartIndex = request.queryParams("jtStartIndex");  //Index start for sorted table
        String jtPageSize = request.queryParams("jtPageSize");  //page size of jtable
        String jtSorting = request.queryParams("jtSorting");  //page sorting of jtable
        response.type("application/json");
        RestUserallResponse result = new RestUserallResponse();
        try {
            result.setRecords(User.all(jtStartIndex, jtPageSize, jtSorting));
            result.setTotalRecordCount(User.getUserCount());
            result.setResult("OK");
        } catch (Exception ex) {
            result.setResult("ERROR");
            LOGGER.error(ex);
        }      
        return JsonUtil.dataToJson(result);
    };  
    
    public static Route serveRestCreateUsers = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTCREATEUSER +" post request");
        LOGGER.debug(LinkPath.Web.RESTCREATEUSER + " post request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
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
            LOGGER.error(ex);
        }      
        return JsonUtil.dataToJson(result);
    }; 
        
    public static Route serveRestUpdateUsers = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTUPDATEUSER +" post request");
        LOGGER.debug(LinkPath.Web.RESTUPDATEUSER + " post request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
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
            LOGGER.error(ex);
        }      
        return obj.toJSONString();
    }; 
            
    public static Route serveRestDeleteUsers = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTDELETEUSER +" post request");
        LOGGER.debug(LinkPath.Web.RESTDELETEUSER + " post request : " + request.body());
        LoginController.ensureAdminIsLoggedIn(request, response);
        JSONObject obj = new JSONObject();
        response.type("application/json");
        User user = new  User();
        try {
            user.setId(Integer.parseInt(request.queryParams("id")));
            user.delete();
            obj.put("Result", "OK");
        } catch (Exception ex) {
            obj.put("Result", "ERROR");
            LOGGER.error(ex);
        }      
        return obj.toJSONString();
    }; 
        
     public static Route serveRestUploadUserImage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTUPLOADUSERIMAGE +" post request");
        //LOGGER.debug(LinkPath.Web.RESTUPLOADUSERIMAGE + " post request : " + request.body()); // issue when enableing debug!!
        LoginController.ensureAdminIsLoggedIn(request, response);
        File uploadDir1 = new File("web");
        File uploadDir2 = new File("web/userimages");
        uploadDir1.mkdir(); // create the upload directory if it doesn't exist
        uploadDir2.mkdir(); // create the upload directory if it doesn't exist
        Path userImagePath = Paths.get("web/userimages/" + request.queryParams("filename"));
        File userImageFile = new File("web/userimages/" + request.queryParams("filename"));
        userImageFile.createNewFile();
        
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));      
        try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) {

            // Use the input stream to create a file
            Files.copy(input, userImagePath, StandardCopyOption.REPLACE_EXISTING);

   
        } catch (Exception e) {
            LOGGER.error("Can't write user image file  : ",e);
            return "upload failed";
        }

        //LOGGER.info("Uploaded file '" + getFileName(request.raw().getPart("uploaded_file")) + "' saved as '" + tempFile.toAbsolutePath() + "'");
        return "upload successful";
    };   
    
    public static Route serveRestUploadBackgroundImage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTUPLOADBACKGROUNDIMAGE +" post request");
        //LOGGER.debug(LinkPath.Web.RESTUPLOADUSERIMAGE + " post request : " + request.body()); // issue when enableing debug!!
        LoginController.ensureAdminIsLoggedIn(request, response);
        File uploadDir1 = new File("web");
        File uploadDir2 = new File("web/backgroundimages");
        uploadDir1.mkdir(); // create the upload directory if it doesn't exist
        uploadDir2.mkdir(); // create the upload directory if it doesn't exist
        Path imagePath = Paths.get("web/backgroundimages/alternative_image.jpg");
        File imageFile = new File("web/backgroundimages/alternative_image.jpg");
        imageFile.createNewFile();
        
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));      
        try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) {

            // Use the input stream to create a file
            Files.copy(input, imagePath, StandardCopyOption.REPLACE_EXISTING);

   
        } catch (Exception e) {
            LOGGER.error("Can't write background image file  : ",e);
            return "upload failed";
        }
        return "upload successful";
    }; 
}