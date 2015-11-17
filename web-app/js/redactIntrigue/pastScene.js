$(function(){
    updatePastScene();

    //ajoute une nouvelle scène passée dans la base
    $('.insertPastScene').click(function() {
        save++;
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
                    initQuickObjects();
                    initSpanLabel('.leftMenuList .spanLabel[href="#pastScene_' + data.pastscene.id + '"]');
                    initSpanLabel('roleScreen .spanLabel[data-pastsceneid="' + data.pastscene.id + '"]');
                    $('form[name="updatePastScene_' + data.pastscene.id + '"] .btnFullScreen').click(function() {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
                    var nbPastScenes = parseInt($('.pastScenesLi .badge').html()) + 1;
                    $('.pastScenesLi .badge').html(nbPastScenes);
                    updatePastScene();
                    updateSave();
                }
                else {
                    createNotification("danger", "création échouée.", "Votre scène passée n'a pas pu être ajoutée, une erreur s'est produite.");
                    updateSave();
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre scène passée n'a pas pu être ajoutée, une erreur s'est produite.");
                updateSave();
            }
        })
    });
});

function updatePastScene() {
    // modifie une scène passée dans la base
    $('.updatePastScene').click(function() {
        save++;
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
                    createNotification("success", "Modifications réussies.", "Votre scène passée :  "+ title+" a bien été modifiée.");
                    $('.pastSceneScreen .leftMenuList a[href="#pastScene_' + data.object.id + '"]').html(convertHTMLRegisterHelper(data.object.name));
                    $('select[name="pastScenePredecessor"] option[value="' + data.object.id + '"]').html($('<div/>').text(data.object.name).html());
                    var resDate = "";
                    var globalList = buildDateList(data.object);
                    resDate = buildRelativeString(globalList, data.object, resDate);
                    if (globalList.relativeList.length != 0 && globalList.absoluteList.length != 0) {
                        resDate += ", ";
                    }
                    resDate = buildAbsoluteString(globalList, resDate, data.object);
                    $('.roleScreen a[data-pastsceneId="' + data.object.id + '"]').html(
                        resDate + " - " + convertHTMLRegisterHelper(data.object.name)
                    );
                    $('#pastsceneRolesModal' + data.object.id + ' div[id*="roleHasPastSceneTitleRichTextEditor"]', form).each(function() {
                        var roleId = $('.titleContent', $(this).closest(".tab-pane")).attr("name").replace("roleHasPastSceneTitle", "");
                        if ($(this).html() == "") {
                            $('a[href="#pastsceneRole' + roleId + "_" + data.object.id + '"]', form).parent().removeClass("alert-success");
                            $('.roleScreen a[href="#collapsePastScene' + roleId + '-' + data.object.id + '"]').parent().removeClass("alert-success");
                            $('.roleScreen #collapsePastScene' + roleId + '-' + data.object.id + ' .textTitle').html("");
                            $('.roleScreen #collapsePastScene' + roleId + '-' + data.object.id + ' .richTextEditor:not(.textTitle)').html("");
                        }
                        else {
                            $('a[href="#pastsceneRole' + roleId + "_" + data.object.id + '"]', form).parent().addClass("alert-success");
                            $('.roleScreen a[href="#collapsePastScene' + roleId + '-' + data.object.id + '"]').parent().addClass("alert-success");
                            $('.roleScreen #collapsePastScene' + roleId + '-' + data.object.id + ' .textTitle').html(
                                convertHTMLRegisterHelper($('input[name="roleHasPastSceneTitle' + roleId + '"]', $(this).closest(".tab-pane")).val())
                            );
                            $('.roleScreen #collapsePastScene' + roleId + '-' + data.object.id + ' .richTextEditor:not(.textTitle)').html(
                                convertHTMLRegisterHelper($('input[name="roleHasPastSceneDescription' + roleId + '"]', $(this).closest(".tab-pane")).val())
                            );
                        }
                    });
                    initializeTextEditor();
                    updateSave();
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre scène passée : *"+ title +"*  n'a pas pu être modifiée, une erreur s'est produite.");
                    updateSave();
                }
            },
            error: function() {
                createNotification("danger", "Modifications échouées.", "Votre scène passée : *"+ title +"* n'a pas pu être modifiée, une erreur s'est produite.");
                updateSave();
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
        value = convertHTMLRegisterHelper(value);
        return new Handlebars.SafeString(value);
    });
    Handlebars.registerHelper('ifNull', function(value, options) {
        if(value) {
            return options.fn(this);
        }
        return options.inverse(this);
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
    Handlebars.registerHelper('pastSceneTime', function(pastscene) {
        var res = "";
        var globalList = buildDateList(pastscene);
        res = buildRelativeString(globalList, pastscene, res);
        if (globalList.relativeList.length != 0 && globalList.absoluteList.length != 0) {
            res += ", ";
        }
        res = buildAbsoluteString(globalList, res, pastscene);
        res += " - ";
        return res;
    });
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
    initializeTextEditor();
//    initializePopover();
}

function buildAbsoluteString(globalList, res, pastscene) {
    var first = true;
    for (key in globalList.absoluteList) {
        if (!first && globalList.absoluteList[key] != "Minute") {
            res += ", "
        }
        first = false;
        if (globalList.absoluteList[key] == "Year") {
            res += "en " + pastscene.Year;
        }
        else if (globalList.absoluteList[key] == "Day") {
            res += "le " + pastscene.Day;
        }
        else if (globalList.absoluteList[key] == "Month") {
            res += "en " + pastscene.MonthLetters;
        }
        else if (globalList.absoluteList[key] == "Hour") {
            res += "à " + pastscene.Hour + "h"
        }
        else if (globalList.absoluteList[key] == "Minute") {
            res += pastscene.Minute;
        }
    }
    return res;
}

function buildRelativeString(globalList, pastscene, res) {
    var first = true;
    if (globalList.relativeList.length != 0)  {
        res += "Il y a ";
    }
    for (key in globalList.relativeList) {
        if (!first) {
            res += ", "
        }
        first = false;
        if (globalList.relativeList[key] == "Year") {
            res += pastscene.Year + " ans"
        }
        else if (globalList.relativeList[key] == "Day") {
            res += pastscene.Day + " jours"
        }
        else if (globalList.relativeList[key] == "Month") {
            res += pastscene.Month + " mois"
        }
        else if (globalList.relativeList[key] == "Hour") {
            res += pastscene.Hour + " heures"
        }
        else if (globalList.relativeList[key] == "Minute") {
            res += pastscene.Minute + " minutes"
        }
    }
    return res;
}

function buildDateList(pastscene) {
    var globalList = {};
    globalList.relativeList = [];
    globalList.absoluteList = [];
    if (pastscene.isAbsoluteYear && pastscene.Year != null) {
        globalList.absoluteList.push("Year");
    }
    else if (pastscene.Year != null) {
        globalList.relativeList.push("Year");
    }
    if (pastscene.isAbsoluteDay && pastscene.Day != null) {
        globalList.absoluteList.push("Day");
    }
    else if (pastscene.Day != null) {
        globalList.relativeList.push("Day");
    }
    if (pastscene.isAbsoluteMonth && pastscene.Month != null) {
        globalList.absoluteList.push("Month");
    }
    else if (pastscene.Month != null) {
        globalList.relativeList.push("Month");
    }
    if (pastscene.isAbsoluteHour && pastscene.Hour != null) {
        globalList.absoluteList.push("Hour");
    }
    else if (pastscene.Hour != null) {
        globalList.relativeList.push("Hour");
    }
    if (pastscene.isAbsoluteMinute && pastscene.Minute != null) {
        globalList.absoluteList.push("Minute");
    }
    else if (pastscene.Minute != null) {
        globalList.relativeList.push("Minute");
    }
    return globalList;
}