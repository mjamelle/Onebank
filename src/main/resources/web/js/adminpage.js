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

var filelist = [];  // Ein Array, das alle hochzuladenden Files enthält
var totalSize = 0; // Enthält die Gesamtgröße aller hochzuladenden Dateien
var totalProgress = 0; // Enthält den aktuellen Gesamtfortschritt
var currentUpload = null; // Enthält die Datei, die aktuell hochgeladen wird

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
            //e.target.className = (e.type == "dragover" ? "hover" : "");
    }
    
    function handleDropEvent(event) {
        event.stopPropagation();
        event.preventDefault();
        console.log("Drop Event : "+event.dataTransfer.files.length);
        

        // event.dataTransfer.files enthält eine Liste aller gedroppten Dateien
        for (var i = 0; i < event.dataTransfer.files.length; i++) {
            filelist.push(event.dataTransfer.files[i]);  // Hinzufügen der Datei zur Uploadqueue
            totalSize += event.dataTransfer.files[i].size;  // Hinzufügen der Dateigröße zur Gesamtgröße
        }
        startNextUpload();
    }
    
    function startNextUpload()
{
    if (filelist.length) { // Überprüfen, ob noch eine Datei hochzuladen ist
        currentUpload = filelist.shift();  // nächste Datei zwischenspeichern
        uploadFile(currentUpload);  // Upload starten
    }
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
        totalProgress += currentUpload.size;  // Füge die Größe dem Gesamtfortschritt hinzu
        startNextUpload(); // Starte den Upload der nächsten Datei
    }
 
    function handleError(event) {
        alert("Upload failed");    // Die Fehlerbehandlung kann natürlich auch anders aussehen
        totalProgress += currentUpload.size;  // Die Datei wird dem Progress trotzdem hinzugefügt, damit die Prozentzahl stimmt
        startNextUpload();  // Starte den Upload der nächsten Datei
    }

    function handleProgress(event) {
        var progress = totalProgress + event.loaded;  // Füge den Fortschritt des aktuellen Uploads temporär dem gesamten hinzu
        document.getElementById('progress').innerHTML = 'Aktueller Fortschritt: ' + (progress / totalSize) + '%';
    }
    
});