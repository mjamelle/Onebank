package app.index;

import app.util.*;
import spark.*;
import java.util.*;
import static app.util.RequestUtil.getSessionCurrentUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IndexController {
    private static final Logger logger = LogManager.getLogger();
    public static Route serveIndexPage = (Request request, Response response) -> {
        logger.info(LinkPath.Web.INDEX + " get request");
        logger.debug(LinkPath.Web.INDEX + " get request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        String callpage = (request.session().attribute("currentUser") == null) ? callpage = LinkPath.Template.INDEX : LinkPath.Template.INDEXLOGIN;
        
        return ViewUtil.render(request, model, callpage);
    };
}
