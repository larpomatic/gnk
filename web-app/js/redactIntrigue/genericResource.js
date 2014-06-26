$(function(){
    updateResource();

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
                    appendEntity("resource", data.genericResource.code, "important", "", data.genericResource.id);
                    var nbGenericResources = parseInt($('.resourceLi .badge').html()) + 1;
                    $('.resourceLi .badge').html(nbGenericResources);
                    updateResource();
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

// modifie un objet dans la base
function updateResource() {
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
                    $('.resourceScreen .leftMenuList a[href="#resource_' + data.object.id + '"]').html(data.object.name);
                    $('.resourceSelector li[data-id="' + data.object.id + '"] a').html(data.object.name);
                    $('.richTextEditor span.label-important').each(function() {
                        if ($(this).html() == data.object.oldname) {
                            $(this).html(data.object.name);
                        }
                    });
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
}

// supprime un objet dans la base
function removeResource(object) {
    var liObject = object.parent();
    var name = $.trim($("a", liObject).html());
    var isResourcePresentInDescriptions = false;
    $('.richTextEditor span.label-important').each(function() {
        if ($(this).html() == name) {
            isResourcePresentInDescriptions = true;
        }
    });
    if (isResourcePresentInDescriptions) {
        createNotification("danger", "suppression impossible.", "Votre objet est utilisé dans certaines descriptions."
            + " Veuillez supprimer l'utilisation de cet objet dans les descriptions avant de supprimer l'entité objet.");
    }
    else {
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
                    $('.resourceSelector li[data-id="' + object.attr("data-id") + '"]').remove();
                    $('.richTextEditor span.label-important').each(function() {
                       if ($(this).html() == name) {
                           $(this).remove();
                       }
                    });
                    createNotification("success", "Supression réussie.", "Votre objet a bien été supprimé.");
                }
                else {
                    createNotification("danger", "suppression échouée.", "Votre objet n'a pas pu être supprimé, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "suppression échouée.", "Votre objet n'a pas pu être supprimé, une erreur s'est produite.");
            }
        });
    }
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