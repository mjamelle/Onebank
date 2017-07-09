package app.util;

import lombok.*;

public class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web {
        @Getter public static final String SLASH = "/";
        @Getter public static final String INDEX = "/index/";
        @Getter public static final String CONTACT = "/contact/";
        @Getter public static final String CONSULTANT = "/consultant/";
        @Getter public static final String SPARKWIDGET = "/sparkwidget/:id/";
        @Getter public static final String BOTMESSAGE = "/webhook/messages";
        @Getter public static final String BOTROOMS = "/webhook/rooms";
        @Getter public static final String REST = "/rest/config";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String ADMINUSER = "/admin/";
        @Getter public static final String ADMINREPORT = "/report/";
        @Getter public static final String ADMINDESIGN = "/design/";
        @Getter public static final String ADMINLISTUSERS = "/rest/listusers/";

    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String CONTACT = "/velocity/contact/contact.vm";
        public final static String CONSULTANT = "/velocity/consultant/consultant.vm";
        public final static String SPARKWIDGET = "/velocity/consultant/sparkwidget.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String ADMINUSER = "/velocity/admin/manageuser.vm";
        public final static String ADMINREPORT = "/velocity/admin/report.vm";
        public final static String ADMINDESIGN = "/velocity/admin/design.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
    }

}
