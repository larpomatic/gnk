$(function(){
    updatePastScene();

    //ajoute une nouvelle scène passée dans la base
    $('.insertPastScene').click(function() {
        var form = $('form[name="newPastSceneForm"]');
        var description = $('#pastSceneRichTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var title = $('#pastSceneTitleRichTextEditor', form).html();
        title = transformDescription(title);
        $('.titleContent', form).val(title);
        $('div[id*="roleHasPastSceneTitleRichTextEditor"]', form).each(function() {
            $(".titleContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $('div[id*="roleHasPastSceneRichTextEditor"]', form).each(function() {
            $(".descriptionContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
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
                    stopClosingDropdown();
                    initPastSceneRelative();
                    initQuickObjects();
                    $('form[name="updatePastScene_' + data.pastscene.id + '"] .btnFullScreen').click(function() {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
                    var nbPastScenes = parseInt($('.pastScenesLi .badge').html()) + 1;
                    $('.pastScenesLi .badge').html(nbPastScenes);
                    updatePastScene();
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

function updatePastScene() {
    // modifie une scène passée dans la base
    $('.updatePastScene').click(function() {
        var pastsceneId = $(this).attr("data-id");
        var form = $('form[name="updatePastScene_' + pastsceneId + '"]');
        var description = $('#pastSceneRichTextEditor' + pastsceneId, form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var title = $('#pastSceneTitleRichTextEditor' + pastsceneId, form).html();
        title = transformDescription(title);
        $('.titleContent', form).val(title);
        $('div[id*="roleHasPastSceneTitleRichTextEditor"]', form).each(function() {
            $(".titleContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $('div[id*="roleHasPastSceneRichTextEditor"]', form).each(function() {
            $(".descriptionContent", $(this).closest(".tab-pane")).val(transformDescription($(this).html()));
        });
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.isupdate) {
                    createNotification("success", "Modifications réussies.", "Votre scène passée a bien été modifiée.");
                    $('.pastSceneScreen .leftMenuList a[href="#pastScene_' + data.object.id + '"]').html($('<div/>').text(data.object.name).html());
                    $('select[name="pastScenePredecessor"] option[value="' + data.object.id + '"]').html($('<div/>').text(data.object.name).html());

                    <!--###PASTSCENECHANGE-->
                    $('.roleScreen a[data-pastsceneId="' + data.object.id + '"]').html($('<div/>').text(
                        "En " + data.object.year + " le " + data.object.day + " " + data.object.month + " à " + data.object.hour + "h "
                            + data.object.minute + " - " + data.object.name
                    ).html());

                    $('#pastsceneRolesModal' + data.object.id + ' div[id*="roleHasPastSceneTitleRichTextEditor"]', form).each(function() {
                        var roleId = $('.titleContent', $(this).closest(".tab-pane")).attr("name").replace("roleHasPastSceneTitle", "");
                        if ($(this).html() == "") {
                            $('a[href="#pastsceneRole' + roleId + "_" + data.object.id + '"]', form).parent().removeClass("alert-success");
                            $('.roleScreen a[href="#collapsePastScene' + roleId + '-' + data.object.id + '"]').parent().removeClass("alert-success");
                            $('.roleScreen #collapsePastScene' + roleId + '-' + data.object.id + ' input[type="text"]').val("");
                            $('.roleScreen #collapsePastScene' + roleId + '-' + data.object.id + ' textarea').html("");
                        }
                        else {
                            $('a[href="#pastsceneRole' + roleId + "_" + data.object.id + '"]', form).parent().addClass("alert-success");
                            $('.roleScreen a[href="#collapsePastScene' + roleId + '-' + data.object.id + '"]').parent().addClass("alert-success");
                            $('.roleScreen #collapsePastScene' + roleId + '-' + data.object.id + ' input[type="text"]').val(
                                $('input[name="roleHasPastSceneTitle' + roleId + '"]', $(this).closest(".tab-pane")).val()
                            );
                            $('.roleScreen #collapsePastScene' + roleId + '-' + data.object.id + ' textarea').html(
                                $('input[name="roleHasPastSceneDescription' + roleId + '"]', $(this).closest(".tab-pane")).val()
                            );
                        }
                    });
                    initializeTextEditor();
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
    $('form[name="newPastSceneForm"] .richTextEditor').html("");
}

// créé un tab-pane de la nouvelle scène passée
function createNewPastScenePanel(data) {
    Handlebars.registerHelper('encodeAsHtml', function(value) {
        value = value.replace(/>/g, '</span>');
        value = value.replace(/<l:/g, '<span class="label label-warning" contenteditable="false">');
        value = value.replace(/<o:/g, '<span class="label label-important" contenteditable="false">');
        value = value.replace(/<i:/g, '<span class="label label-success" contenteditable="false">');
        value = value.replace(/<u:/g, '<span class="label label-default" contenteditable="false">');
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
    $('select[name="pastScenePredecessor"]').append('<option value="' + data.pastscene.id + '">' + $('<div/>').text(data.pastscene.title).html() + '</option>');
    if (data.pastscene.pastscenePredecessorId) {
        $('#pastScene_' + data.pastscene.id + ' #pastScenePredecessor option[value="'+ data.pastscene.pastscenePredecessorId +'"]').attr("selected", "selected");
    }
    if (data.pastscene.pastscenePlaceId) {
        $('#pastScene_' + data.pastscene.id + ' #pastScenePlace option[value="'+ data.pastscene.pastscenePlaceId +'"]').attr("selected", "selected");
    }
    $('ul[class*="placePastScene"]').append('<li class="modalLi" data-name="' + $('<div/>').text(data.pastscene.title).html() + '">' +
        '<input type="checkbox" name="placePastScene_' + data.pastscene.id + '" id="placePastScene_' + data.pastscene.id + '">' +
        $('<div/>').text(data.pastscene.title).html() +
        '</li>');
    $.each(data.pastscene.roleList, function(i, item) {
        var roleId = item.roleId;
        template = Handlebars.templates['templates/redactIntrigue/addPastSceneInRole'];
        context = {
            pastscene: data.pastscene,
            role: item
        };
        html = template(context);
        $('.roleScreen div[id*="rolePastScenesModal"] #accordionPastScene' + roleId).append(html);
    });
}