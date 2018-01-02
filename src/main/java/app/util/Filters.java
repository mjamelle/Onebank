package app.util;

import spark.*;
import static app.util.RequestUtil.*;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Filters {
    @Getter private static int webrequests = 0;
    private static final Logger LOGGER = LogManager.getLogger();

    // If a user manually manipulates paths and forgets to add
    // a trailing slash, redirect the user to the correct path
    public static Filter addTrailingSlashes = (Request request, Response response) -> {
        //logger.info("UserAgent :" + request.userAgent());
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
    };
    
    
    public static Filter forwardtoIndex = (Request request, Response response) -> {
        response.redirect("/index"); 
    };
    

    // Locale change can be initiated from any page
    // The locale is extracted from the request and saved to the user's session
    public static Filter handleLocaleChange = (Request request, Response response) -> {
        if (getQueryLocale(request) != null) {
            request.session().attribute("locale", getQueryLocale(request));
            response.redirect(request.pathInfo());
        }
    };

    // Enable GZIP for all responses
    public static Filter addGzipHeader = (Request request, Response response) -> {
        response.header("Content-Encoding", "gzip");
    };
    
    public static Filter countSessions = (Request request, Response response) -> {
        if (request.session().isNew()) webrequests++;
    };

}
