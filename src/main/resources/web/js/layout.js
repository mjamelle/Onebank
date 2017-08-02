
$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();

    // send locale choose to actual web page
    $('#locale_de').click(function() {
        $.post( "?locale=de");
        location.reload();
    });      
    $('#locale_en').click(function() {
        $.post( "?locale=en");
        location.reload();
    });
      
      // send logout request
    $('#logout_request').click(function() {
        $.post( "/logout");
        location.reload();
      });
});
