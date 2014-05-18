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

    initRadioPlots();

    //Si il n'y a pas d'intrigue mainstream sur un gn mainstream alors on alerte l'utilisateur
    notifyNoMainstream();

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

    $('.radioEvenemential:checked').closest("tr").prependTo(".evenemential-table tbody");
    $('.radioMainstream:checked').closest("tr").prependTo(".mainstream-table tbody");

    $('.modal-body li').each(function() {
        toggle($('input[type="checkbox"]', $(this)).attr("id"), $(".tagWeight input", $(this)).attr("id"));
    });

    $('.chooseTag').click(function() {
        $('input', $(this).parent().prev()).val(101);
    });
    $('.banTag').click(function() {
        $('input', $(this).parent().next()).val(-101);
    });

    $('.gnSubmitForm').submit(function() {
        $('input').removeClass("redBorder");
        var $gnPIPMin = $('#gnPIPMin');
        var $gnPIPCore = $('#gnPIPCore');
        var $gnPIPMax = $('#gnPIPMax');
        if (parseInt($gnPIPMin.val()) >= parseInt($gnPIPMax.val())) {
           $gnPIPMin.addClass("redBorder");
           createNotification("danger", "Erreur !", "PIPMin doit être strictement inférieur à PIPMax.");
           return false;
        }
        if (parseInt($gnPIPCore.val()) >= parseInt($gnPIPMax.val())) {
            $gnPIPCore.addClass("redBorder");
            createNotification("danger", "Erreur !", "PIPCore doit être strictement inférieur à PIPMax.");
            return false;
        }
        if (parseInt($gnPIPCore.val()) < parseInt($gnPIPMin.val())) {
            $gnPIPCore.addClass("redBorder");
            createNotification("danger", "Erreur !", "PIPCore doit être supérieur ou égal à PIPMin.");
            return false;
        }
        return true;
    });

    $('.roleToPersoFrom').submit(function() {
        if ($('input[name*="plot_status_"][value="2"]:checked').size() > 0) {
            createNotification("warning", "Attention !", "Vous devez d'abord relancer le gn pour comptabiliser vos intrigues éliminées.");
            return false;
        }
        return true;
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

function initRadioPlots() {
    if ($('input[name="selected_evenemential"]:checked').size() == 0) {
        $(".radioEvenemential").first().prop("checked", true);
    }
    if ($('input[name="selected_mainstream"]:checked').size() == 0) {
        $(".radioMainstream").first().click();
    }
}

function notifyNoMainstream() {
    if (($('.mainstream-table:visible').size() != 0) && ($('.mainstream-table tbody tr:not(:last-child)').size() == 0)) {
        createNotification("warning", "Attention !", "Aucune intrigue mainstream n'a été trouvée.");
    }
}