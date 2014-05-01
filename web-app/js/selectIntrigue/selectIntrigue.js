/**
 * Created by Alexandre on 29/03/14.
 */

$(function(){
    var oldSameMainstream = {id: "", value: ""};

    $(".radioEvenemential").click(function() {
        $('.selectedEvenemential').val($(this).val());
    });
    $(".radioMainstream").click(function() {
        $('.selectedMainstream').val($(this).val());
        var id = $(this).val();
        $('input[name*="plot_status_"]').removeAttr("disabled");
        $('input[name="plot_status_'+ oldSameMainstream.id +'"][value="' + oldSameMainstream.value + '"]').click();
        oldSameMainstream.id = id;
        oldSameMainstream.value = $('input[name="plot_status_'+ id +'"]:checked').val();
        $('input[name="plot_status_'+ id +'"][value="2"]').click();
        $('input[name="plot_status_'+ id +'"][value="1"]').attr("disabled", "disabled");
        $('input[name="plot_status_'+ id +'"][value="3"]').attr("disabled", "disabled");
    });

    $(".radioEvenemential").first().prop("checked", true);
    $(".radioMainstream").first().click();

    $('.selectedEvenemential').val($(".radioEvenemential").first().val());
    $('.selectedMainstream').val($(".radioMainstream").first().val());

    $('.moreEvenemential').click(function() {
        //on enleve le tr de titre et le tr du bouton
//        var nb = $(".evenemential-table tr:visible").size() + 4; // test pour 5
        var nb = $(".evenemential-table tr:visible").size(); //test pour 1
        $(".evenemential-table tr").show();
        $('.evenemential-table tr:not(:last-child):nth-child(n+'+nb+')').css("display", "none");
    });
    $('.moreMainstream').click(function() {
        //on enleve le tr de titre et le tr du bouton
//        var nb = $(".mainstream-table tr:visible").size() + 4; // test pour 5
        var nb = $(".mainstream-table tr:visible").size(); //test pour 1
        $(".mainstream-table tr").show();
        $('.mainstream-table tr:not(:last-child):nth-child(n+'+nb+')').css("display", "none");
    });

    $('.modal-body li').each(function() {
        toggle($('input[type="checkbox"]', $(this)).attr("id"), $(".tagWeight input", $(this)).attr("id"));
    });

    $('.chooseTag').click(function() {
        $('input', $(this).parent().prev()).val(101);
    });
    $('.banTag').click(function() {
        $('input', $(this).parent().next()).val(-101);
    });
});

function toggle(checkboxID, toggleID) {
    var checkbox = $("#" + checkboxID);
    var toggle = $("#" + toggleID);

    if (!checkbox.prop("checked")) {
        toggle.attr("disabled", "disabled");
        toggle.parent().prev().addClass("invisible");
        toggle.parent().next().addClass("invisible");
    }
    else {
        toggle.removeAttr("disabled");
        toggle.parent().prev().removeClass("invisible");
        toggle.parent().next().removeClass("invisible");
    }
}

