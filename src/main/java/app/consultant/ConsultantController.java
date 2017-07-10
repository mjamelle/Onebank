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
        LOGGER.info(Path.Web.CONSULTANT + " get request");
        LOGGER.debug(Path.Web.CONSULTANT + " get request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        model.put("users", User.all());
        return ViewUtil.render(request, model, Path.Template.CONSULTANT);
    };
    
    public static Route serveSparkWidget = (Request request, Response response) -> {
        LOGGER.info(Path.Web.SPARKWIDGET +request.params(":id")+" get request");
        LOGGER.debug(Path.Web.SPARKWIDGET + request.params(":id")+" get request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        User consultant = User.find(Integer.parseInt(request.params(":id")));
        model.put("consultant", consultant);
        model.put("data-access-token", SystemConfig.getSparkWidgetAccessToken());
        return ViewUtil.render(request, model, Path.Template.SPARKWIDGET);
    };
    
}
