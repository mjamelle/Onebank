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

$(document).ready(function () {
        $('#UserTableContainer').jtable({
            title: 'Liste',
            actions: {
                listAction: '/rest/listusers/',
                createAction: '/rest/createuser/',
                updateAction: '/rest/updateuser/',
                deleteAction: '/rest/deleteuser/'
            },
            fields: {
                id: {
                    key: true,
                    create: false,
                    list: false
                },
                givenName: {
                    title: 'Vorname',
                    width: '10%'
                },
                surName: {
                    title: 'Nachname',
                    width: '10%'
                },
                username: {
                    list: false,
                    title: 'Username'
                },
                password: {
                    list: false,
                    title: 'Password',
                    type: 'password',
                    width: '10%'
                },
                photolink: {
                    list: false,
                    title: 'Bild'
                },
                email: {
                    title: 'Email',
                    width: '10%'
                },
                function: {
                    list: false,
                    title: 'Bezeichnung'
                },
                jabber_use: {
                    title: 'Jabber',
                    type:  'checkbox',
                    values: { 'false': '', 'true': '' },
                    defaultValue: 'false'
                },
                spark_use: {
                    title: 'Spark',
                    type:  'checkbox',
                    values: { 'false': '', 'true': '' },
                    defaultValue: 'false'
                },
                adminprivilege: {
                    title: 'Admin',
                    type:  'checkbox',
                    values: { 'false': '', 'true': '' },
                    defaultValue: 'false'
                }  
            }
        });   	
    $('#UserTableContainer').jtable('load');
    });
