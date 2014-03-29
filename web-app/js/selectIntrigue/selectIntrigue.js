/**
 * Created by Alexandre on 29/03/14.
 */

$(function(){
    $(".radioEvenemential").click(function() {
        $('.selectedEvenemential').val($(this).val());
    });

    $(".radioEvenemential").first().prop("checked", true);
});

