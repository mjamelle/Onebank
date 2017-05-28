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

  (function(document, script) {
  var bubbleScript = document.createElement(script);
  e = document.getElementsByTagName(script)[0];
  bubbleScript.async = true;
  bubbleScript.CiscoAppId =  'cisco-chat-bubble-app';
  bubbleScript.DC = 'produs1.ciscoccservice.com';
  bubbleScript.orgId = '0910066f-d089-4edd-b2f3-5d62b691e7bf';
  bubbleScript.templateId = '11c13360-436d-11e7-a5c5-05f7f4ec4979';
  bubbleScript.src = 'https://bubble.produs1.ciscoccservice.com/bubble.js';
  bubbleScript.type = 'text/javascript';
  bubbleScript.setAttribute('charset', 'utf-8');
  e.parentNode.insertBefore(bubbleScript, e);
  })(document, 'script');

