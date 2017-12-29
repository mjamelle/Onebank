package app.consultant;

import app.db.User;
import app.util.*;
import spark.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsultantController {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static Route serveConsultantPage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.CONSULTANT + " get request");
        LOGGER.debug(LinkPath.Web.CONSULTANT + " get request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        model.put("users", User.all());
        return ViewUtil.render(request, model, LinkPath.Template.CONSULTANT);
    };
    
    public static Route serveSparkWidget = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.SPARKWIDGET +request.params(":id") + " get request");
        LOGGER.debug(LinkPath.Web.SPARKWIDGET + request.params(":id") + " get request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        User consultant = User.find(Integer.parseInt(request.params(":id")));
        String dataaccesstoken;
        User user = request.session().attribute("currentUser");
        if (user != null) {
            dataaccesstoken = user.getOauthAccessToken();
        } else {
            dataaccesstoken = SystemConfig.getSparkWidgetAccessToken();
        }
        model.put("consultant", consultant);
        model.put("data-access-token", dataaccesstoken);
        LOGGER.info("serveSparkWidget User  :  " + user);
        
        return ViewUtil.render(request, model, LinkPath.Template.SPARKWIDGET);
    };  
            
    public static Route serveImmoBotPage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.IMMO_BOT + " get request");
        LOGGER.debug(LinkPath.Web.IMMO_BOT + " get request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        String dataaccesstoken;
        User user = request.session().attribute("currentUser");
        if (user != null) {
            dataaccesstoken = user.getOauthAccessToken();
        } else {
            // using mjamlle access token as an interims solution
            dataaccesstoken = "ZGE4MmNlODItNWY0YS00ZDlkLTgyODItNTlmYjRjZDAyMjJlNDRmZTZiY2UtMWJk";
        }
        model.put("data-access-token", dataaccesstoken);
        return ViewUtil.render(request, model, LinkPath.Template.IMMO_BOT);
    };    
}
