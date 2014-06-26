$(function(){
    updatePlace();

    //ajoute un nouveau lieu dans la base
    $('.insertPlace').click(function() {
        var form = $('form[name="newPlaceForm"]');
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
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
                    appendEntity("place", data.genericPlace.code, "warning", "", data.genericPlace.id);
                    var nbGenericPlaces = parseInt($('.placeLi .badge').html()) + 1;
                    $('.placeLi .badge').html(nbGenericPlaces);
                    updatePlace();
                }
                else {
                    createNotification("danger", "création échouée.", "Votre lieu n'a pas pu être ajouté, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre lieu n'a pas pu être ajouté, une erreur s'est produite.");
            }
        })
    });
});

function updatePlace() {
    // modifie un lieu dans la base
    $('.updatePlace').click(function() {
        var genericPlaceId = $(this).attr("data-id");
        var form = $('form[name="updatePlace_' + genericPlaceId + '"]');
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.isupdate) {
                    createNotification("success", "Modifications réussies.", "Votre lieu a bien été modifié.");
                    $('.placeScreen .leftMenuList a[href="#place_' + data.object.id + '"]').html(data.object.name);
                    $('.placeSelector li[data-id="' + data.object.id + '"] a').html(data.object.name);
                    $('select[name="eventPlace"] option[value="' + data.object.id + '"]').html(data.object.name);
                    $('select[name="pastScenePlace"] option[value="' + data.object.id + '"]').html(data.object.name);
                    $('.richTextEditor span.label-warning').each(function() {
                        if ($(this).html() == data.object.oldname) {
                            $(this).html(data.object.name);
                        }
                    });
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre lieu n'a pas pu être modifié, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Modifications échouées.", "Votre lieu n'a pas pu être modifié, une erreur s'est produite.");
            }
        })
    });
}

// supprime un lieu dans la base
function removePlace(object) {
    var liObject = object.parent();
    var name = $.trim($("a", liObject).html());
    var isPlacePresentInDescriptions = false;
    $('.richTextEditor span.label-warning').each(function() {
        if ($(this).html() == name) {
            isPlacePresentInDescriptions = true;
        }
    });
    if (isPlacePresentInDescriptions) {
        createNotification("danger", "suppression impossible.", "Votre lieu est utilisé dans certaines descriptions."
            + " Veuillez supprimer l'utilisation de ce lieu dans les descriptions avant de supprimer l'entité lieu.");
    }
    else {
        $.ajax({
            type: "POST",
            url: object.attr("data-url"),
            dataType: "json",
            success: function(data) {
                if (data.object.isdelete) {
                    liObject.remove();
                    var nbGenericPlaces = parseInt($('.placeLi .badge').html()) - 1;
                    $('.placeLi .badge').html(nbGenericPlaces);
                    $('.addPlace').trigger("click");
                    $('.placeSelector li[data-id="' + object.attr("data-id") + '"]').remove();
                    $('.richTextEditor span.label-warning').each(function() {
                        if ($(this).html() == name) {
                            $(this).remove();
                        }
                    });
                    createNotification("success", "Supression réussie.", "Votre lieu a bien été supprimé.");
                }
                else {
                    createNotification("danger", "suppression échouée.", "Votre lieu n'a pas pu être supprimé, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "suppression échouée.", "Votre lieu n'a pas pu être supprimé, une erreur s'est produite.");
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
}

// créé un tab-pane du nouveau lieu
function createNewGenericPlacePanel(data) {
    Handlebars.registerHelper('toLowerCase', function(value) {
        return new Handlebars.SafeString(value.toLowerCase());
    });
    var template = Handlebars.templates['templates/redactIntrigue/genericPlacePanel'];
    var context = {
        genericPlace: data.genericPlace,
        genericPlaceTagList: data.genericPlaceTagList
    };
    var html = template(context);
    $('.placeScreen > .tab-content').append(html);
    for (var key in data.genericPlace.tagList) {
        $('#placeTagsModal_' + data.genericPlace.id + " #placeTags_" + data.genericPlace.tagList[key]).attr('checked', 'checked');
    }
//    for (var key1 in data.genericPlace.eventList) {
//        if (data.genericPlace.eventList[key1].eventCheck) {
//            $('#placeEventsModal_' + data.genericPlace.id + " #placeEvent_" + data.genericPlace.eventList[key1].eventId).attr('checked', 'checked');
//        }
//    }
//    for (var key2 in data.genericPlace.pastsceneList) {
//        if (data.genericPlace.pastsceneList[key2].pastsceneCheck) {
//            $('#placePastScenesModal_' + data.genericPlace.id + " #placePastScene_" + data.genericPlace.pastsceneList[key2].pastsceneId).attr('checked', 'checked');
//        }
//    }
    $('select[name="pastScenePlace"]').append('<option value="' + data.genericPlace.id + '">' + data.genericPlace.code + '</option>');
    $('select[name="eventPlace"]').append('<option value="' + data.genericPlace.id + '">' + data.genericPlace.code + '</option>');
}