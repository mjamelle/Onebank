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
  
//Name der Kunden-Support-Vorlage: Onebank
//Name der Organisation: mjamelle.webexsandbox.co
  (function(document, script) {
  var bubbleScript = document.createElement(script);
  e = document.getElementsByTagName(script)[0];
  bubbleScript.async = true;
  bubbleScript.CiscoAppId =  'cisco-chat-bubble-app';
  bubbleScript.DC = 'produs1.ciscoccservice.com';
  bubbleScript.orgId = 'ced45728-4485-4c4f-8206-2ad6779be802';
  bubbleScript.templateId = '9aca0e40-fbac-11e8-a413-af0e771e9299';
  bubbleScript.src = 'https://bubble.produs1.ciscoccservice.com/bubble.js';
  bubbleScript.type = 'text/javascript';
  bubbleScript.setAttribute('charset', 'utf-8');
  e.parentNode.insertBefore(bubbleScript, e);
  })(document, 'script');
