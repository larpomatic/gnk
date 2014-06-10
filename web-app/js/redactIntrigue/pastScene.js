$(function(){
    initPastSceneRelative();

    // modifie une scène passée dans la base
    $('.updatePastScene').click(function() {
        var pastsceneId = $(this).attr("data-id");
        var form = $('form[name="updatePastScene_' + pastsceneId + '"]');
        var description = $('.richTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.isupdate) {
                    createNotification("success", "Modifications réussies.", "Votre scène passée a bien été modifiée.");
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre scène passée n'a pas pu être modifiée, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Modifications échouées.", "Votre scène passée n'a pas pu être modifiée, une erreur s'est produite.");
            }
        })
    });

    //ajoute une nouvelle scène passée dans la base
    $('.insertPastScene').click(function() {
        var form = $('form[name="newPastSceneForm"]');
        var description = $('.richTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.iscreate) {
                    createNotification("success", "Création réussie.", "Votre scène passée a bien été ajoutée.");
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiPastScene'];
                    var context = {
                        pastsceneId: String(data.pastscene.id),
                        pastsceneTitle: data.pastscene.title
                    };
                    var html = template(context);
                    $('.pastSceneScreen > ul').append(html);
                    initConfirm();
                    initDeleteButton();
                    emptyPastSceneForm();
                    createNewPastScenePanel(data);
                    initSearchBoxes();
                    initPastSceneRelative();
                    $('.datetimepicker').datetimepicker({
                        language: 'fr',
                        pickSeconds: false
                    });
                    $('.btnFullScreen').click(function() {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
                    var nbPastScenes = parseInt($('.pastScenesLi .badge').html()) + 1;
                    $('.pastScenesLi .badge').html(nbPastScenes);
                }
                else {
                    createNotification("danger", "création échouée.", "Votre scène passée n'a pas pu être ajoutée, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre scène passée n'a pas pu être ajoutée, une erreur s'est produite.");
            }
        })
    });
});

function initPastSceneRelative() {
    //change l'unité de temps sur les pastScenes
    $('.pastSceneRelativeTimeUnit').click(function() {
        $(".relativeTimeMessage", $(this).parent().parent().prev()).html($(this).html());
        var unit = $(this).attr("data-unitTime");
        $(".pastSceneRelativeUnit", $(this).parent().parent().parent().parent()).val(unit);
    });

    //bascule en mode temps relatif sur les pastscenes
    $('.relativeButton').click(function() {
        $('.pastSceneRelative', $(this).parent().parent()).removeClass("hidden");
        $('.pastSceneAbsolute', $(this).parent().parent()).addClass('hidden');
    });

    //bascule en mode temps absolu sur les pastscenes
    $('.absoluteButton').click(function() {
        $('.pastSceneAbsolute', $(this).parent().parent()).removeClass("hidden");
        $('.pastSceneRelative', $(this).parent().parent()).addClass('hidden');
    });
}

// supprime une scène passée dans la base
function removePastScene(object) {
    var liObject = object.parent();
    $.ajax({
        type: "POST",
        url: object.attr("data-url"),
        dataType: "json",
        success: function(data) {
            if (data.isDelete) {
                liObject.remove();
                var nbPastScenes = parseInt($('.pastScenesLi .badge').html()) - 1;
                $('.pastScenesLi .badge').html(nbPastScenes);
                $('.addPastScene').trigger("click");
                $('input[name="placePastScene_' + data.pastsceneId + '"]', 'ul[class*="placePastScene"]').parent().remove();
                $('.roleScreen div[id="collapsePastScene-' + data.pastsceneId + '"]').parent().remove();
                $('.roleScreen div[id*="rolePastScenesModal"] .accordion').each(function() {
                    var roleId = $(this).attr("id");
                    roleId = roleId.replace("accordionPastScene", "");
                    $('.roleScreen div[id="collapsePastScene' + roleId + "-" + data.pastsceneId + '"]').parent().remove();
                });
                $('select[name="pastScenePredecessor"] option[value="' + data.pastsceneId + '"]').remove();
                createNotification("success", "Supression réussie.", "Votre scène passée a bien été supprimée.");
            }
            else {
                createNotification("danger", "suppression échouée.", "Votre scène passée n'a pas pu être supprimée, une erreur s'est produite.");
            }
            return false;
        },
        error: function() {
            createNotification("danger", "suppression échouée.", "Votre scène passée n'a pas pu être supprimée, une erreur s'est produite.");
        }
    })
}

//vide le formulaire d'ajout d'une scène passée
function emptyPastSceneForm() {
    $('form[name="newPastSceneForm"] input[type="text"]').val("");
    $('form[name="newPastSceneForm"] input[type="number"]').val("");
    $('form[name="newPastSceneForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newPastSceneForm"] #pastScenePlace option[value="null"]').attr("selected", "selected");
    $('form[name="newPastSceneForm"] #pastScenePredecessor option[value="null"]').attr("selected", "selected");
    $('form[name="newPastSceneForm"] #pastSceneRichTextEditor').html("");
    $('form[name="newPastSceneForm"] .relativeTimeMessage').html("Année");
}

// créé un tab-pane de la nouvelle scène passée
function createNewPastScenePanel(data) {
    Handlebars.registerHelper('encodeAsHtml', function(value) {
        value = value.replace(/<l:/g, '<span class="label label-warning" contenteditable="false">');
        value = value.replace(/<o:/g, '<span class="label label-important" contenteditable="false">');
        value = value.replace(/<i:/g, '<span class="label label-success" contenteditable="false">');
        value = value.replace(/>/g, '</span>');
        return new Handlebars.SafeString(value);
    });
    Handlebars.registerHelper('unitTimeConverter', function(value) {
        if (value == "Y") {
            value = "Année";
        }
        if (value == "M") {
            value = "Mois";
        }
        if (value == "d") {
            value = "Jour"
        }
        if (value == "h") {
            value = "Heure";
        }
        if (value == "m") {
            value = "Minute";
        }
        return new Handlebars.SafeString(value);
    });
    var template = Handlebars.templates['templates/redactIntrigue/pastScenePanel'];
    var context = {
        pastscene: data.pastscene
    };
    var html = template(context);
    $('.pastSceneScreen > .tab-content').append(html);
    var plotFullscreenEditable = $('.plotScreen .fullScreenEditable').first();
    $('.btn-group', plotFullscreenEditable).clone().prependTo('#pastScene_' + data.pastscene.id + ' .fullScreenEditable');
    $("#newPastScene #pastScenePlace option").clone().appendTo('#pastScene_' + data.pastscene.id + ' #pastScenePlace');
    $("#newPastScene #pastScenePredecessor option").clone().appendTo('#pastScene_' + data.pastscene.id + ' #pastScenePredecessor');
    $('select[name="pastScenePredecessor"]').append('<option value="' + data.pastscene.id + '">' + data.pastscene.title + '</option>');
    if (data.pastscene.pastscenePredecessorId) {
        $('#pastScene_' + data.pastscene.id + ' #pastScenePredecessor option[value="'+ data.pastscene.pastscenePredecessorId +'"]').attr("selected", "selected");
    }
    if (data.pastscene.pastscenePlaceId) {
        $('#pastScene_' + data.pastscene.id + ' #pastScenePlace option[value="'+ data.pastscene.pastscenePlaceId +'"]').attr("selected", "selected");
    }
    $('ul[class*="placePastScene"]').append('<li class="modalLi" data-name="' + data.pastscene.title + '">' +
        '<input type="checkbox" name="placePastScene_' + data.pastscene.id + '" id="placePastScene_' + data.pastscene.id + '">' +
        data.pastscene.title +
        '</li>');
    $('.roleScreen div[id*="rolePastScenesModal"] .accordion').each(function() {
        var roleId = $(this).attr("id");
        roleId = roleId.replace("accordionPastScene", "");
        template = Handlebars.templates['templates/redactIntrigue/addPastSceneInRole'];
        context = {
            pastsceneId: data.pastscene.id,
            pastsceneTitle: data.pastscene.title,
            roleId: roleId
        };
        html = template(context);
        $(this).append(html);
    });
}