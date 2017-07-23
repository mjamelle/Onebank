package app.contact;

import app.util.*;
import spark.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactController {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Route serveContactPage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.CONTACT + " get request");
        LOGGER.debug(LinkPath.Web.CONTACT + " get request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, LinkPath.Template.CONTACT);
    };
}
