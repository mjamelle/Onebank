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
@Getter             private static String companydefaultbackground = "../assets/images/bank-albom.png";
@Getter             private static String companyalternativebackground = "../backgroundimages/alternative_image.jpg";
@Getter             private static String companyactualbackground = "../assets/images/bank-albom.png";
@Getter             private static String companyname = "Onebank";
@Getter             private static boolean backgroundcustomized = false;
@Getter@Setter      private static boolean companynamecustomized = false;

   
    public static void setCompanyname(String companyname) {
        if (companyname != null) {
            WebCustomize.companyname =companyname;
            if (!companyname.equals("Onebank")) companynamecustomized = true;
            else companynamecustomized = false;
        }            
    }    

    
    public static void setBackgroundcustomized(String iscompanybackground) {
        if (iscompanybackground != null) {
          if (iscompanybackground.equals("true")) { 
            companyactualbackground = companyalternativebackground;
            backgroundcustomized = true;
          } else {
            companyactualbackground = companydefaultbackground;
            backgroundcustomized = false;
          }
        }
                
    }
    
}
