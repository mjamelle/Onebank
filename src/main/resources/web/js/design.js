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

    // send locale choose to actual web page
    $('#company_text').change(function() {
        $.post( "/rest/designcustom?companyname=" + this.value);
    });
    $('#backgroundimagedefault').click(function() {
        $.post( "/rest/designcustom?backgroundcustomized=false");
    });
    $('#backgroundimagecustom').click(function() {
        $.post( "/rest/designcustom?backgroundcustomized=true");
    });
    
    // file upload handling------------------------------------------
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

        if (!isfiletypeimg(file)) {
            return;
        } 
        var xhr = new XMLHttpRequest();    // den AJAX Request anlegen
        xhr.upload.addEventListener("progress", handleProgress);
        xhr.addEventListener("load", handleComplete);
        xhr.addEventListener("error", handleError);
        xhr.open('POST', '/rest/uploadbackgroundimage');

        var formdata = new FormData();    // Anlegen eines FormData Objekts zum Versenden unserer Datei
        formdata.append('uploaded_file', file);  // Anhängen der Datei an das Objekt
        xhr.send(formdata);    // Absenden des Requests
    }
    
    function handleComplete(event) {       
        file = null;
        //user image renewal
        var d = new Date();
        $("#backgroundimage").attr("src", "../backgroundimages/alternative_image.jpg?"+d.getTime());
        $('#inputbackgroundimage').click();
        $('#status').text('Upload erfolgreich. Achtung - dein Browser cached! Gib ihn 20 Sekunden...');
    }
 
    function handleError(event) {
        $('#status').text('Warnung - Upload fehlgechlagen');
    }

    function handleProgress(event) {
        var progress = Math.round(100 / file.size * event.loaded);  // Füge den Fortschritt des aktuellen Uploads temporär dem gesamten hinzu
        $('#status').text('Datei Upload : ' + progress + '%');
    }
    
    function isfiletypeimg(file) {
        if (file.size > 11000000) {
            $('#status').text('Warnung - Filegröße über 1MB Limit!');
            return false;
        }
        if ((file.type === 'image/png') || (file.type === 'image/gif') || (file.type === 'image/jpeg')) {
            return true;
        } else {
            $('#status').text('Warnung - Falsches Fileformat, wähle eine Imagedatei');
            return false;   
        }
    }
});

