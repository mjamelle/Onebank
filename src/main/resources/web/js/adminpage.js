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

    var file;
    var selectedUserRecord = null;

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
                    title: 'Bild',
                    edit: false,
                    create: false
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
                        selectedUserRecord = $(this).data('record');
                        if ((selectedUserRecord === null) || (selectedUserRecord.photolink === null)) {
                            document.getElementById("userimage").src = '../assets/images/person.jpeg';  
                        } else {
                            document.getElementById("userimage").src = selectedUserRecord.photolink;
                        }
                    });
                };
            },
        //refresh image view and define default image after insertes a new user
            rowInserted: function () {
                document.getElementById("userimage").src = '../assets/images/person.jpeg';
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
    }
   
       
    function FileDragHover(event) {
        event.stopPropagation();
        event.preventDefault();
        //e.target.className = (e.type == "dragover" ? "hover" : "");
    }
    
    function handleDropEvent(event) {
        event.stopPropagation();
        event.preventDefault();
        if (event.dataTransfer.files.length > 0) {
           file = event.dataTransfer.files[0];
           uploadFile(file);
        }
    }
    
    function uploadFile(file) {
        if (selectedUserRecord == null) {
            document.getElementById('status').innerHTML = 'Warnung - erst den Benutzer auswählen';
            return;
        }
        if (!isfiletypeimg(file)) {
            return;
        } 
        var fileID = selectedUserRecord.id;  //  TBD
        var fileName = 'userimage_id_' + fileID + '.' + file.name.split('.').pop();
        var xhr = new XMLHttpRequest();    // den AJAX Request anlegen
        xhr.upload.addEventListener("progress", handleProgress);
        xhr.addEventListener("load", handleComplete);
        xhr.addEventListener("error", handleError);
        xhr.open('POST', '/rest/uploaduserimage' + '?id=' + fileID + '&filename=' + fileName);

        var formdata = new FormData();    // Anlegen eines FormData Objekts zum Versenden unserer Datei
        formdata.append('uploaded_file', file);  // Anhängen der Datei an das Objekt
        xhr.send(formdata);    // Absenden des Requests
        
        selectedUserRecord.photolink = 'userimages/' + fileName;
        $('#UserTableContainer').jtable('updateRecord', {
            record: {
                id: selectedUserRecord.id,
                givenName: selectedUserRecord.givenName,
                surName: selectedUserRecord.surName,
                username: selectedUserRecord.username,
                password: selectedUserRecord.password,
                photolink: selectedUserRecord.photolink,
                email: selectedUserRecord.email,
                function: selectedUserRecord.function,
                jabber_use: selectedUserRecord.jabber_use,
                spark_use: selectedUserRecord.spark_use,
                adminprivilege: selectedUserRecord.adminprivilege
            }
        });
    }
    
    function handleComplete(event) {       
        file = null;
        //user image renewal
        document.getElementById("userimage").src = '../assets/images/person.jpeg';
        document.getElementById("userimage").src = selectedUserRecord.photolink;  
    }
 
    function handleError(event) {
        document.getElementById('status').innerHTML = 'Warnung - Upload fehlgechlagen';
    }

    function handleProgress(event) {
        var progress = Math.round(100 / file.size * event.loaded);  // Füge den Fortschritt des aktuellen Uploads temporär dem gesamten hinzu
        document.getElementById('status').innerHTML = 'Datei Upload : ' + progress + '%';
    }
    
    function isfiletypeimg(file) {
        if (file.size > 61440) {
            document.getElementById('status').innerHTML = 'Warnung - Filegröße über 60KB Limit!';
            return false;
        }
        if ((file.type === 'image/png') || (file.type === 'image/gif') || (file.type === 'image/jpeg')) {
            return true;
        } else {
            document.getElementById('status').innerHTML = 'Warnung - Falsches Fileformat, wähle eine Imagedatei';
            return false;   
        }
    }
});