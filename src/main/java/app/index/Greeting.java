/*
 * Copyright (C) 2018 mjamelle
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
package app.index;

import java.time.LocalDateTime;


/**
 *
 * @author mjamelle
 */
public class Greeting {
    LocalDateTime  time; 

    public Greeting() {
        this.time = LocalDateTime.now();
    }
    
    public String getGreeting() {
        String greeting = "Test";
        int i = time.getHour();
        
        if (i>=0 && i<=11) greeting = "Guten Morgen";
        if (i>=12 && i<=17) greeting = "Guten Tag";
        if (i>=18 && i<=23) greeting = "Guten Abend";
        return greeting;
    }
}
