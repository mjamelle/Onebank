package app.login;

import app.db.User;
import app.user.UserController;
import app.util.*;
import spark.*;
import java.util.*;
import static app.util.RequestUtil.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;


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
            LOGGER.info("User Login failed : " + getQueryUsername(request));
            return ViewUtil.render(request, model, LinkPath.Template.LOGIN);
        } else {
            request.session().maxInactiveInterval(SystemConfig.getwebUserSessiontimeout()); //set user session timeout

            User user = User.getUserByUsername(request.queryParams("username"));
            request.session().attribute("currentUser", user);
            /*
            if (getQueryLoginRedirect(request) != null) {
                response.redirect(getQueryLoginRedirect(request));
            } else response.redirect(LinkPath.Web.INDEX);
            */
            model.put("authenticationSucceeded", true);
            model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
            if (user.getOauthAccessToken() == null) {
               response.redirect(oauthrequest(user)); 
            } else {
                oauthRefreshToken(user);
                response.redirect(LinkPath.Web.INDEX);
            }

              
            LOGGER.info("User Login successful : "+ user.getDisplayName());
        }
        return ""; 
    };

    public static Route handleLogoutPost = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.LOGOUT + " post request");
        LOGGER.debug(LinkPath.Web.LOGOUT + " post request : " + request.body());
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        return "";
    };
    
    public static Route handleOAuthresponse = (Request request, Response response) -> {
        LOGGER.info(LinkPath.Web.RESTSPARKOAUTH + " get request");
        LOGGER.debug(LinkPath.Web.RESTSPARKOAUTH + " get request : " + request.body());
        LOGGER.info("Received OAuth Response Key :  " + request.queryParams("code"));
        LOGGER.info("Received OAuth State Key :  " + request.queryParams("state"));
        User user = User.getUserByUsername(request.queryParams("state"));
        oauthCodeToAccessToken(request.queryParams("code"), user);
        response.redirect(LinkPath.Web.INDEX);
        return "";
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
    
    // this methode forms the oauth request URL 
    private static String oauthrequest (User user) {
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .authorizationLocation(SystemConfig.getOauthAuthorizationLocation().toString())
                    .setClientId(SystemConfig.getOauthClientId())
                    .setRedirectURI(SystemConfig.getOauthRedirectURI().toString())
                    .setResponseType("code")
                    .setState(user.getUsername())
                    .setScope("spark:all")
                    .buildQueryMessage();
            
            LOGGER.info("Build oauth Request for Spark api  : " + request.getLocationUri());
            return request.getLocationUri();
        } catch (OAuthSystemException ex) {
            LOGGER.error(ex);
        }
        return "";
    }
    
    private static String oauthCodeToAccessToken (String code, User user) {
        try {
            OAuthClientRequest request = OAuthClientRequest
                .tokenLocation(SystemConfig.getOauTokenLocation().toString())
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(SystemConfig.getOauthClientId())
                .setClientSecret(SystemConfig.getOauthClientSecret())
                .setRedirectURI(SystemConfig.getOauthRedirectURI().toString())    
                .setCode(code)
                .buildQueryMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);
            // Save Token into user database 
            user.setOauthAccessToken(oAuthResponse.getAccessToken());
            user.setOauthRefreshToken(oAuthResponse.getRefreshToken());
            user.updateOauth();
            
            LOGGER.info("oauthCodeToAccessToken User " + user.getUsername() + " updated  AccessToken :  " + oAuthResponse.getAccessToken()
                    + "  + RefreshToken  : " + oAuthResponse.getRefreshToken());
            
        } catch (OAuthSystemException ex) {
            LOGGER.error(ex);
        } catch (Exception e) {
            LOGGER.error(e);
        } return "";
    }

    private static String oauthRefreshToken (User user) {
        try {
            OAuthClientRequest request = OAuthClientRequest
                .tokenLocation(SystemConfig.getOauTokenLocation().toString())
                .setGrantType(GrantType.REFRESH_TOKEN)
                .setClientId(SystemConfig.getOauthClientId())
                .setClientSecret(SystemConfig.getOauthClientSecret())
                .setRefreshToken(user.getOauthRefreshToken())
                .buildQueryMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);
            // Update Token in User Database 
            user.setOauthAccessToken(oAuthResponse.getAccessToken());
            user.updateOauth();
            
            LOGGER.info("oauthRefreshToken User " + user.getUsername() + " updated  AccessToken :  " + oAuthResponse.getAccessToken()
                    + "  + RefreshToken  : " + oAuthResponse.getRefreshToken());
            
        } catch (OAuthSystemException ex) {
            LOGGER.error(ex);
        } catch (Exception e) {
            LOGGER.error(e);
        } return "";
    }

}