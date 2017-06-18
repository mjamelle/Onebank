package app.consultant;

import app.contact.*;
import app.db.User;
import app.util.*;
import spark.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsultantController {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Route serveConsultantPage = (Request request, Response response) -> {
        LOGGER.info("/consultant/ request");
        LOGGER.debug("/consultant/ request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        model.put("users", User.all());
        return ViewUtil.render(request, model, Path.Template.CONSULTANT);
    };
}
