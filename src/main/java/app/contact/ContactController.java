package app.contact;

import app.index.*;
import app.util.*;
import spark.*;
import java.util.*;
import static app.Application.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactController {
    private static final Logger logger = LogManager.getLogger();
    public static Route serveContactPage = (Request request, Response response) -> {
        logger.info("/contact/ request");
        logger.debug("/contact/ request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.CONTACT);
    };
}
