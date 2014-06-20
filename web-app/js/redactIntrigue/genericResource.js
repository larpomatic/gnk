$(function(){
    // modifie un objet dans la base
    $('.updateResource').click(function() {
        var genericResourceId = $(this).attr("data-id");
        var form = $('form[name="updateResource_' + genericResourceId + '"]');
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.isupdate) {
                    createNotification("success", "Modifications réussies.", "Votre objet a bien été modifié.");
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre objet n'a pas pu être modifié, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Modifications échouées.", "Votre objet n'a pas pu être modifié, une erreur s'est produite.");
            }
        })
    });

    //ajoute un nouvel objet dans la base
    $('.insertResource').click(function() {
        var form = $('form[name="newResourceForm"]');
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.iscreate) {
                    createNotification("success", "Création réussie.", "Votre objet a bien été ajouté.");
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiGenericResource'];
                    var context = {
                        resourceId: String(data.genericResource.id),
                        resourceName: data.genericResource.code
                    };
                    var html = template(context);
                    $('.resourceScreen > ul').append(html);
                    initConfirm();
                    initDeleteButton();
                    emptyGenericResourceForm();
                    createNewGenericResourcePanel(data);
                    initSearchBoxes();
                    appendEntity("resource", data.genericResource.code, "warning", "", data.genericResource.id);
                    var nbGenericResources = parseInt($('.resourceLi .badge').html()) + 1;
                    $('.resourceLi .badge').html(nbGenericResources);
                }
                else {
                    createNotification("danger", "création échouée.", "Votre objet n'a pas pu être ajouté, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre objet n'a pas pu être ajouté, une erreur s'est produite.");
            }
        })
    });
});


// supprime un objet dans la base
function removeResource(object) {
    var liObject = object.parent();
    $('.resourceSelector li[data-id="' + object.attr("data-id") + '"]').remove();
    $.ajax({
        type: "POST",
        url: object.attr("data-url"),
        dataType: "json",
        success: function(data) {
            if (data.object.isdelete) {
                liObject.remove();
                var nbGenericResources = parseInt($('.resourceLi .badge').html()) - 1;
                $('.resourceLi .badge').html(nbGenericResources);
                $('.addResource').trigger("click");
                createNotification("success", "Supression réussie.", "Votre objet a bien été supprimé.");
            }
            else {
                createNotification("danger", "suppression échouée.", "Votre objet n'a pas pu être supprimé, une erreur s'est produite.");
            }
        },
        error: function() {
            createNotification("danger", "suppression échouée.", "Votre objet n'a pas pu être supprimé, une erreur s'est produite.");
        }
    })
}

//vide le formulaire d'ajout d'objet
function emptyGenericResourceForm() {
    $('form[name="newResourceForm"] input[type="text"]').val("");
    $('form[name="newResourceForm"] textarea').val("");
    $('form[name="newResourceForm"] input[type="checkbox"]').attr('checked', false);
}

// créé un tab-pane du nouvel objet
function createNewGenericResourcePanel(data) {
    Handlebars.registerHelper('toLowerCase', function(value) {
        return new Handlebars.SafeString(value.toLowerCase());
    });
    var template = Handlebars.templates['templates/redactIntrigue/genericResourcePanel'];
    var context = {
        genericResource: data.genericResource,
        genericResourceTagList: data.genericResourceTagList
    };
    var html = template(context);
    $('.resourceScreen > .tab-content').append(html);
    for (var key in data.genericResource.tagList) {
        $('#resourceTagsModal_' + data.genericResource.id + " #resourceTags_" + data.genericResource.tagList[key]).attr('checked', 'checked');
    }
}