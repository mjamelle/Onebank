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
        logger.info("/index/ request");
        logger.debug("/index/ request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.INDEX);
    };
}
