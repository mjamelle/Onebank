package app.login;

import app.db.User;
import app.user.UserController;
import app.util.*;
import spark.*;
import java.util.*;
import static app.util.RequestUtil.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController {
    private static final Logger LOGGER = LogManager.getLogger();

    public static Route serveLoginPage = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.LOGIN + " get request");
        LOGGER.debug(LinkPath.Web.LOGIN + " get request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        return ViewUtil.render(request, model, LinkPath.Template.LOGIN);
    };

    public static Route handleLoginPost = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.LOGIN + " post request");
        LOGGER.debug(LinkPath.Web.LOGIN + " post request : " + request.body());
        Map<String, Object> model = new HashMap<>();
        if (!UserController.authenticate(getQueryUsername(request), getQueryPassword(request))) {
            model.put("authenticationFailed", true);
            return ViewUtil.render(request, model, LinkPath.Template.LOGIN);
        }
        request.session().maxInactiveInterval(SystemConfig.getwebUserSessiontimeout()); //set user session timeout
        
        User user = User.getUserByUsername(request.queryParams("username"));
        request.session().attribute("currentUser", user);
        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        } else response.redirect(LinkPath.Web.INDEX);
        model.put("authenticationSucceeded", true);
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        LOGGER.info("User Login successful : "+ user.getDisplayName());
        return null; 
    };

    public static Route handleLogoutPost = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.LOGOUT + " post request");
        LOGGER.debug(LinkPath.Web.LOGOUT + " post request : " + request.body());
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        return null;
    };

    // The origin of the request (request.pathInfo()) is saved in the session so
    // the user can be redirected back after login
    public static void ensureAdminIsLoggedIn(Request request, Response response) {
        User user = request.session().attribute("currentUser");
        if (user == null || !user.isAdminprivilege()) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(LinkPath.Web.LOGIN);           
        }
    };

}