/**
 * Created by Alexandre on 29/03/14.
 */

$(function(){
    $(".radioEvenemential").click(function() {
        $('.selectedEvenemential').val($(this).val());
    });

    $(".radioEvenemential").first().prop("checked", true);

    $('.selectedEvenemential').val($(".radioEvenemential").first().val());

    $('.moreEvenemential').click(function() {
        var nb = $(".evenemential-table tr:visible").size() + 4; //on enleve le tr de titre et le tr du bouton
        $(".evenemential-table tr").show();
        $('.evenemential-table tr:not(:last-child):nth-child(n+'+nb+')').css("display", "none");
    });
});

