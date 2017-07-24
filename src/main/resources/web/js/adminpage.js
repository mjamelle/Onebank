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
                    title: 'Jabber Berater',
                    list: false,
                    type:  'checkbox',
                    values: { 'false': '', 'true': '' },
                    defaultValue: 'false'
                },
                spark_use: {
                    title: 'Spark Berater',
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
    


//var filelist = [];  // Ein Array, das alle hochzuladenden Files enthält
var fileSize = 0; // Enthält die Größe der Dateien
//var totalProgress = 0; // Enthält den aktuellen Gesamtfortschritt
//var currentUpload = null; // Enthält die Datei, die aktuell hochgeladen wird
    
    
    	// call initialization file
    if (window.File && window.FileList && window.FileReader) {
            init();
    }
        
    function init() {
        document.getElementById('uploadzone').addEventListener('drop', handleDropEvent, false);
        document.getElementById('uploadzone').addEventListener("dragover", FileDragHover, false);
        document.getElementById('uploadzone').addEventListener("dragleave", FileDragHover, false);

        console.log("Drop Initialized");
    }
    
    $("#uploadzone").click(function() {
        console.log("Test");
    });
            
    // file drag hover
    function FileDragHover(e) {
            e.stopPropagation();
            e.preventDefault();
            e.target.className = (e.type == "dragover" ? "hover" : "");
    }
    
    function handleDropEvent(event) {
        event.stopPropagation();
        event.preventDefault();
        console.log("Drop Event : "+event.dataTransfer.files.length);   
        
        if (event.dataTransfer.files.length > 0) {
           fileSize += event.dataTransfer.files[0].size; 
        }
        uploadFile(event.dataTransfer.files[0]);
    }
    
 
    function uploadFile(file) {
        var xhr = new XMLHttpRequest();    // den AJAX Request anlegen
        xhr.upload.addEventListener("progress", handleProgress);
        xhr.addEventListener("load", handleComplete);
        xhr.addEventListener("error", handleError);
        xhr.open('POST', '/rest/uploaduserimage');    // Angeben der URL und des Requesttyps

        var formdata = new FormData();    // Anlegen eines FormData Objekts zum Versenden unserer Datei
        formdata.append('uploaded_file', file);  // Anhängen der Datei an das Objekt
        xhr.send(formdata);    // Absenden des Requests
    }
    
    function handleComplete(event) {
        fileSize = 0;
    }
 
    function handleError(event) {
        alert("Upload failed");    // Die Fehlerbehandlung kann natürlich auch anders aussehen
    }

    function handleProgress(event) {
        var progress = Math.round(100 / fileSize * event.loaded);  // Füge den Fortschritt des aktuellen Uploads temporär dem gesamten hinzu
        document.getElementById('progress').innerHTML = 'Upload Fortschritt: ' + progress + '%';
    }
    
});