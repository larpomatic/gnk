$(function () {
    updatePlace();

    //ajoute un nouveau lieu dans la base
    $('.insertPlace').click(function () {
        save++;
        var form = $('form[name="newPlaceForm"]');
        var description = $('.richTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var mandatoryTagFound = false;
        var data = form.serialize();
        var data_tbl = data.split("&");
        var tags = ["501", "502", "503", "504", "506", "507", "508", "509", "510"]
        for (var key in data_tbl) {
            for (var tag in tags) {
                if (data_tbl[key].indexOf("placeTagsWeight_" + tags[tag]) != -1) {
                    mandatoryTagFound = true;
                }
            }
        }
        if (false == mandatoryTagFound) {
            createNotification("danger", "Création échouée.", "Il est nécessaire de choisir au moins un tag de la famille de 'Lieu Superficie'");
            updateSave();
            return;
        }

        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function (data) {
                if (data.iscreate) {
                    createNotification("success", "Création réussie.", "Votre lieu a bien été ajouté.");
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiGenericPlace'];
                    var context = {
                        placeId: String(data.genericPlace.id),
                        placeName: data.genericPlace.code
                    };
                    var html = template(context);
                    $('.placeScreen > ul').append(html);
                    initConfirm();
                    initDeleteButton();
                    emptyGenericPlaceForm();
                    createNewGenericPlacePanel(data);
                    initSearchBoxes();
                    initModifyTag();
                    stopClosingDropdown();
                    appendEntity("place", data.genericPlace.code, "warning", "", data.genericPlace.id);
                    initQuickObjects();
                    var nbGenericPlaces = parseInt($('.placeLi .badge').html()) + 1;
                    $('.placeLi .badge').html(nbGenericPlaces);
                    updatePlace()
                    $('form[name="updatePlace_' + data.genericPlace.id + '"] .btnFullScreen').click(function () {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
                    var spanList = $('.richTextEditor span.label-default').filter(function () {
                        return $(this).text() == data.genericPlace.code;
                    });
                    spanList.each(function () {
                        $(this).removeClass("label-default").addClass("label-warning");
                    });
                    $('.placeSelector li[data-id=""]').each(function () {
                        if ($("a", $(this)).html().trim() == data.genericPlace.code + ' <i class="icon-warning-sign"></i>') {
                            $(this).remove();
                        }
                    });
                    updateAllDescription($.unique(spanList.closest("form")));
                    updateSave();
                }
                else {
                    createNotification("danger", "création échouée.", "Votre lieu n'a pas pu être ajouté, une erreur s'est produite.");
                    updateSave();
                }
            },
            error: function () {
                createNotification("danger", "création échouée.", "Votre lieu n'a pas pu être ajouté, une erreur s'est produite.");
                updateSave();
            }
        })
    });

    // add link to add 10 best places
    getBestPlace();
});

function updatePlace() {
    // modifie un lieu dans la base
    $('.updatePlace').click(function () {
        save++;
        var genericPlaceId = $(this).attr("data-id");
        var form = $('form[name="updatePlace_' + genericPlaceId + '"]');
        var description = $('.richTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var mandatoryTagFound = false;
        var data = form.serialize();
        var data_tbl = data.split("&");
        var tags = ["501", "502", "503", "504", "506", "507", "508", "509", "510"]
        for (var key in data_tbl) {
            for (var tag in tags) {
                if (data_tbl[key].indexOf("placeTagsWeight_" + tags[tag]) != -1) {
                    mandatoryTagFound = true;
                }
            }
        }
        if (false == mandatoryTagFound) {
            createNotification("danger", "Modifications échouées.", "Il est nécessaire de choisir au moins un tag de la famille de 'Lieu Superficie' pour le lieu *"+ $('form[name="updatePlace_' + genericPlaceId + '"] input[name="placeCode"]').val()+"* ");
            updateSave();
            return;
        }

        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function (data) {
                if (data.object.isupdate) {
                    createNotification("success", "Modifications réussies.", "Votre lieu : *"+ $('form[name="updatePlace_' + genericPlaceId + '"] input[name="placeCode"]').val()+"* a bien été modifié.");
                    $('.placeScreen .leftMenuList a[href="#place_' + data.object.id + '"]').html(data.object.name);
                    $('.placeSelector li[data-id="' + data.object.id + '"] a').html(data.object.name);
                    $('select[name="eventPlace"] option[value="' + data.object.id + '"]').html(data.object.name);
                    $('select[name="pastScenePlace"] option[value="' + data.object.id + '"]').html(data.object.name);
                    initializeTextEditor();
                    $('span.label-warning').each(function () {
                        if ($(this).html().trim() == data.object.oldname) {
                            $(this).html(data.object.name);
                        }
                    });
                    updateSave();
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre lieu : *"+ $('form[name="updatePlace_' + genericPlaceId + '"] input[name="placeCode"]').val()+"* n'a pas pu être modifié, une erreur s'est produite.");
                    updateSave();
                }
            },
            error: function () {
                createNotification("danger", "Modifications échouées.", "Votre lieu : *"+ $('form[name="updatePlace_' + genericPlaceId + '"] input[name="placeCode"]').val()+"* n'a pas pu être modifié, une erreur s'est produite.");
                updateSave();
            }
        })
    });
}

// supprime un lieu dans la base
function removePlace(object) {
    var liObject = object.parent();
    var name = $.trim($("a", liObject).html());
    var isPlacePresentInDescriptions = false;
    $('.richTextEditor span.label-warning').each(function () {
        if ($(this).html() == name) {
            isPlacePresentInDescriptions = true;
        }
    });
    if (isPlacePresentInDescriptions) {
        createNotification("danger", "suppression impossible.", "Votre lieu : "+ $('form[name="updatePlace_' + genericPlaceId + '"] input[name="placeCode"]').val()+" est utilisé dans certaines descriptions."
            + " Veuillez supprimer l'utilisation de ce lieu dans les descriptions avant de supprimer l'entité lieu.");
    }
    else {
        $.ajax({
            type: "POST",
            url: object.attr("data-url"),
            dataType: "json",
            success: function (data) {
                if (data.object.isdelete) {
                    liObject.remove();
                    $('select[name="pastScenePlace"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="eventPlace"] option[value="' + object.attr("data-id") + '"]').remove();
                    var nbGenericPlaces = parseInt($('.placeLi .badge').html()) - 1;
                    $('.placeLi .badge').html(nbGenericPlaces);
                    $('.addPlace').trigger("click");
                    $('.placeSelector li[data-id="' + object.attr("data-id") + '"]').remove();
                    $('.richTextEditor span.label-warning').each(function () {
                        if ($(this).html().trim() == name) {
                            $(this).remove();
                        }
                    });
                    createNotification("success", "Supression réussie.", "Votre lieu :  "+ $('form[name="updatePlace_' + genericPlaceId + '"] input[name="placeCode"]').val()+" a bien été supprimé.");
                }
                else {
                    createNotification("danger", "Suppression échouée.", "Votre lieu :  "+ $('form[name="updatePlace_' + genericPlaceId + '"] input[name="placeCode"]').val()+" n'a pas pu être supprimé, une erreur s'est produite.");
                }
            },
            error: function () {
                createNotification("danger", "Suppression échouée.", "Votre lieu :  "+ $('form[name="updatePlace_' + genericPlaceId + '"] input[name="placeCode"]').val()+" n'a pas pu être supprimé, une erreur s'est produite.");
            }
        });
    }
}

//vide le formulaire d'ajout de lieu
function emptyGenericPlaceForm() {
    $('form[name="newPlaceForm"] input[type="text"]').val("");
    $('form[name="newPlaceForm"] input[type="number"]').val("");
    $('form[name="newPlaceForm"] textarea').val("");
    $('form[name="newPlaceForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newPlaceForm"] input[type="radio"]').attr('checked', false);
    $('form[name="newPlaceForm"] input[type="radio"][value="1"]').attr('checked', 'checked');
    $('form[name="newPlaceForm"] .chooseTag').parent().addClass("invisible");
    $('form[name="newPlaceForm"] .banTag').parent().addClass("invisible");
    $('form[name="newPlaceForm"] .tagWeightInput').val(50);
    $('form[name="newPlaceForm"] .tagWeightInput').attr('disabled', 'disabled');
    $('form[name="newPlaceForm"] .search-query').val("");
    $('form[name="newPlaceForm"] .modalLi').show();
    $('form[name="newPlaceForm"] #placeRichTextEditor').html("");
}

// créé un tab-pane du nouveau lieu
function createNewGenericPlacePanel(data) {
    Handlebars.registerHelper('toLowerCase', function (value) {
        return new Handlebars.SafeString(value.toLowerCase());
    });
    var audaciousFn;
    Handlebars.registerHelper('ifCond', function (v1, v2, options) {
        if (v1 === v2) {
            return options.fn(this);
        }
        return options.inverse(this);
    });
    Handlebars.registerHelper('recursive', function (children, options) {
        var out = '';
        if (options.fn !== undefined) {
            audaciousFn = options.fn;
        }
        children.forEach(function (child) {
            out = out + audaciousFn(child);
        });
        return out;
    });
    Handlebars.registerHelper('encodeAsHtml', function (value) {
        value = convertHTMLRegisterHelper(value);
        return new Handlebars.SafeString(value);
    });
    var template = Handlebars.templates['templates/redactIntrigue/genericPlacePanel'];
    var context = {
        genericPlace: data.genericPlace,
        genericPlaceTagList: data.genericPlaceTagList
    };
    var html = template(context);
    $('.placeScreen > .tab-content').append(html);
    var plotFullscreenEditable = $('.plotScreen .fullScreenEditable').first();
    $('.btn-group', plotFullscreenEditable).clone().prependTo('#place_' + data.genericPlace.id + ' .fullScreenEditable');
    for (var key in data.genericPlace.tagList) {
        $('#placeTagsModal_' + data.genericPlace.id + " #placeTags" + data.genericPlace.id + "_" + data.genericPlace.tagList[key].id).attr('checked', 'checked');
        $('#placeTagsModal_' + data.genericPlace.id + " #placeTagsWeight" + data.genericPlace.id + "_" + data.genericPlace.tagList[key].id).val(data.genericPlace.tagList[key].weight);
    }
    $('#placeTagsModal_' + data.genericPlace.id + ' li').each(function () {
        hideTags($('input[type="checkbox"]', $(this)).attr("id"), $(".tagWeight input", $(this)).attr("id"));
    });

    $('.chooseTag').click(function () {
        $('input', $(this).parent().prev()).val(101);
    });

    $('.banTag').click(function () {
        $('input', $(this).parent().next()).val(-101);
    });
    $('select[name="pastScenePlace"]').append('<option value="' + data.genericPlace.id + '">' + data.genericPlace.code + '</option>');
    $('select[name="eventPlace"]').append('<option value="' + data.genericPlace.id + '">' + data.genericPlace.code + '</option>');

    initializePopover();
}

// function to get 10 best places depending of tags
function getBestPlace()
{
    var cont = $('#listContainer');

    $('#newbestPlace').click(function() {
        var br = document.createElement("br");
        cont.empty();
        cont.append("Les meilleures places sont les lieux qui correspondent le mieux aux caractéristiques de votre univers.\n");
        cont.append(br);
        cont.append("Ces caractéristiques sont choisies à l'aide de tags.");
        cont.append("Pour choisir les tags, cliquez sur le bouton \"Choisir tags\".");
    });

    $('.bestPlace').click(function() {
        var url = $('#urlBestPlace').data('url');
        var form_name = $(this).data('form');
        var form = $('form[name=' + form_name + ']');
        cont.empty();

        $.ajax({
            type: "POST",
            url: url,
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                $.each(data.object.json, function(i,v){
                    var h5 = document.createElement("H4");
                    var node = document.createTextNode(i);
                    h5.appendChild(node);
                    cont.append(h5);
                    var ul = document.createElement("ul");
                    if (v.length == 1)
                    {
                        //charger les best places
                        cont.append("Pas de meilleure place !");
                    }
                    for(j = 0; j < v.length; j++)
                    {
                        var li = document.createElement("li");
                        li.innerHTML = v[j];
                        ul.appendChild(li);
                    }
                    cont.append(ul);
                });
                $('.placeLoader').css('display', 'none');
            },
            error: function() {
                $('.placeLoader').css('display', 'none');
                createNotification("danger", "Recherche échouée.", "Impossible de déterminer les 10 meilleurs places correspondant à vos critères.");
            }
        })
    });
}