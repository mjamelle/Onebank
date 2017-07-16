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
    //document.getElementById('markostest').setAttribute('img', '../assets/images/mjamelle-120x160.jpg');
    var marko = "../assets/images/mjamelle-120x160.jpg";
    
        $('#UserTableContainer').jtable({
            title: 'Auflistung',
            paging: true, //Enable paging
            pageSize: 10, //Set page size (default: 10)
            selecting: true, //Enable selecting
            //sorting: true, //Enable sorting
            //defaultSorting: 'surName ASC', //Set default sorting
            actions: {
                listAction: '/rest/listusers',
                createAction: '/rest/createuser',
                updateAction: '/rest/updateuser',
                deleteAction: '/rest/deleteuser'
            },
            fields: {
                id: {
                    key: true,
                    create: false,
                    list: false
                },
                givenName: {
                    title: 'Vorname',
                    width: '15%'
                },
                surName: {
                    title: 'Nachname',
                    width: '15%'
                },
                username: {
                    list: false,
                    title: 'Username'
                },
                password: {
                    list: false,
                    title: 'Password',
                    type: 'password'
                },
                photolink: {
                    list: false,
                    title: 'Bild'
                },
                email: {
                    title: 'Email/URI',
                    width: '20%'
                },
                function: {
                    list: true,
                    title: 'Bezeichnung',
                    width: '50%'
                },
                jabber_use: {
                    title: 'Jabber',
                    list: false,
                    type:  'checkbox',
                    values: { 'false': '', 'true': '' },
                    defaultValue: 'false'
                },
                spark_use: {
                    title: 'Spark',
                    list: false,
                    type:  'checkbox',
                    values: { 'false': '', 'true': '' },
                    defaultValue: 'false'
                },
                adminprivilege: {
                    title: 'Admin',
                    list: false,
                    type:  'checkbox',
                    values: { 'false': '', 'true': '' },
                    defaultValue: 'false'
                }
            },
        //Register to selectionChanged event to hanlde events
            selectionChanged: function () {
              var $selectedRows = $('#UserTableContainer').jtable('selectedRows');
                if ($selectedRows.length > 0) {
                    //Show selected rows
                    $selectedRows.each(function () {
                        var record = $(this).data('record');
                        document.getElementById("userimage").src = record.photolink; //      innerHTML = 'Demo';  
                    });
                };
            }
        });   	
    $('#UserTableContainer').jtable('load');
    //var $row = $('#UserTableContainer').jtable('getRowByKey', 1);
    //$('#UserTableContainer').jtable('selectRows', $row);
    //$('#UserTableContainer').jtable('selectRows', {
    //    key: 1
    //});


    /*
    filedrag.js - HTML5 File Drag & Drop demonstration
    Featured on SitePoint.com
    Developed by Craig Buckler (@craigbuckler) of OptimalWorks.net
    */
    (function() {

            // getElementById
            function $id(id) {
                    return document.getElementById(id);
            }


            // output information
            function Output(msg) {
                    var m = $id("messages");
                    m.innerHTML =  m.innerHTML + msg;
            }


            // file drag hover
            function FileDragHover(e) {
                    e.stopPropagation();
                    e.preventDefault();
                    e.target.className = (e.type == "dragover" ? "hover" : "");
            }


            // file selection
            function FileSelectHandler(e) {

                    // cancel event and hover styling
                    FileDragHover(e);

                    // fetch FileList object
                    var files = e.target.files || e.dataTransfer.files;

                    // process all File objects
                    for (var i = 0, f; f = files[i]; i++) {
                            ParseFile(f);
                    }

            }


            // output file information
            function ParseFile(file) {

                    Output(
                            "<p>File information: <strong>" + file.name +
                            "</strong> type: <strong>" + file.type +
                            "</strong> size: <strong>" + file.size +
                            "</strong> bytes</p>"
                    );

            }


            // initialize
            function Init() {

                    var fileselect = $id("fileselect"),
                            filedrag = $id("userimage"),
                            submitbutton = $id("submitbutton");

                    // file select
                    fileselect.addEventListener("change", FileSelectHandler, false);

                    // is XHR2 available?
                    var xhr = new XMLHttpRequest();
                    if (xhr.upload) {

                            // file drop
                            filedrag.addEventListener("dragover", FileDragHover, false);
                            filedrag.addEventListener("dragleave", FileDragHover, false);
                            filedrag.addEventListener("drop", FileSelectHandler, false);
                            //filedrag.style.display = "block";

                            // remove submit button
                            //submitbutton.style.display = "none";
                    }

            }

            // call initialization file
            if (window.File && window.FileList && window.FileReader) {
                    Init();
            }


    })();

});