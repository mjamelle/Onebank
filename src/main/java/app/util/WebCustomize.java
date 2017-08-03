/*
 * Copyright (C) 2017 mjamelle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package app.util;

import lombok.*;

/**
 *
 * @author mjamelle
 */

public class WebCustomize {
@Getter             private static String companybackground = "../backgroundimages/alternative_image.jpg";
@Getter@Setter      private static boolean backgroundcustomized = false;
                    private static String companyname = "Onebank";
    
    public static String getCompanyname() {
        return companyname;
    }

    public static void setCompanyname(String companyname) {
        WebCustomize.companyname = companyname;
    }


    

    
    public static void setCompanyBackground(String setcompanybackground) {
        if (setcompanybackground != null && !setcompanybackground.equals("../assets/images/bank-albom.png")) {
            companybackground = setcompanybackground; 
        }
                
    }
    
}
