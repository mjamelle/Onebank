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
@Getter    private static String companybackground = "../assets/images/bank-albom.png";
@Getter    private static boolean backgroundcustomized;
@Getter    private static String companyname = "Onebank";
@Getter    private static boolean companynamecustomized;
    
    public static void setCompanyName(String setcompanyname) {
        if (!setcompanyname.equals("Onebank")) {
            companyname = setcompanyname; 
            companynamecustomized = true;
        } else {
            companynamecustomized = false;
        }
                
    }  
    public static void setCompanyBackground(String setcompanybackground) {
        if (!setcompanybackground.equals("../assets/images/bank-albom.png")) {
            companybackground = setcompanybackground; 
            backgroundcustomized = true;
        } else {
            backgroundcustomized = false;
        }
                
    }
    
}
