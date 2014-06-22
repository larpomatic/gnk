$(function(){
    updateRole();

    //ajoute un nouveau role dans la base
    $('.insertRole').click(function() {
        var form = $('form[name="newRoleForm"]');
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.iscreate) {
                    createNotification("success", "Création réussie.", "Votre rôle a bien été ajouté.");
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiRole'];
                    var context = {
                        roleId: String(data.role.id),
                        roleName: data.role.code
                    };
                    var html = template(context);
                    $('.roleScreen > ul').append(html);
                    initConfirm();
                    initDeleteButton();
                    emptyRoleForm();
                    createNewRolePanel(data);
                    initSearchBoxes();
                    appendEntity("role", data.role.code, "success", "", data.role.id);
                    var nbRoles = parseInt($('.roleLi .badge').html()) + 1;
                    $('.roleLi .badge').html(nbRoles);
                    updateRole();
                }
                else {
                    createNotification("danger", "création échouée.", "Votre rôle n'a pas pu être ajouté, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre rôle n'a pas pu être ajouté, une erreur s'est produite.");
            }
        })
    });
});

function updateRole() {
    // modifie un role dans la base
    $('.updateRole').click(function() {
        var roleId = $(this).attr("data-id");
        var form = $('form[name="updateRole_' + roleId + '"]');
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.isupdate) {
                    createNotification("success", "Modifications réussies.", "Votre rôle a bien été modifié.");
                    $('.roleScreen .leftMenuList a[href="#role_' + data.object.id + '"]').html(data.object.name);
                    $('.relationScreen .leftMenuList a[href="#roleRelation_' + data.object.id + '"]').html(data.object.name);
                    $('.roleSelector li[data-id="' + data.object.id + '"] a').html(data.object.name);
                    $('.richTextEditor span.label-success').each(function() {
                        if ($(this).html() == data.object.oldname) {
                            $(this).html(data.object.name);
                        }
                    });
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre rôle n'a pas pu être modifié, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Modifications échouées.", "Votre rôle n'a pas pu être modifié, une erreur s'est produite.");
            }
        })
    });
}

// supprime un role dans la base
function removeRole(object) {
    var liObject = object.parent();
    var name = $.trim($("a", liObject).html());
    $.ajax({
        type: "POST",
        url: object.attr("data-url"),
        dataType: "json",
        success: function(data) {
            if (data.object.isdelete) {
                liObject.remove();
                var nbRoles = parseInt($('.roleLi .badge').html()) - 1;
                $('.roleLi .badge').html(nbRoles);
                $('.addRole').trigger("click");
                $('.roleSelector li[data-id="' + object.attr("data-id") + '"]').remove();
                $('.richTextEditor span.label-success').each(function() {
                    if ($(this).html() == name) {
                        $(this).remove();
                    }
                });
                createNotification("success", "Supression réussie.", "Votre rôle a bien été supprimé.");
            }
            else {
                createNotification("danger", "suppression échouée.", "Votre rôle n'a pas pu être supprimé, une erreur s'est produite.");
            }
        },
        error: function() {
            createNotification("danger", "suppression échouée.", "Votre rôle n'a pas pu être supprimé, une erreur s'est produite.");
        }
    })
}

//vide le formulaire d'ajout de role
function emptyRoleForm() {
    $('form[name="newRoleForm"] input[type="text"]').val("");
    $('form[name="newRoleForm"] input[type="number"]').val("");
    $('form[name="newRoleForm"] textarea').val("");
    $('form[name="newRoleForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newRoleForm"] #roleType option[value="PJ"]').attr("selected", "selected");
}

// créé un tab-pane du nouveau role
function createNewRolePanel(data) {
    Handlebars.registerHelper('toLowerCase', function(value) {
        return new Handlebars.SafeString(value.toLowerCase());
    });
    var template = Handlebars.templates['templates/redactIntrigue/rolePanel'];
    var context = {
        role: data.role,
        roleTagList: data.roleTagList
    };
    var html = template(context);
    $('.roleScreen > .tab-content').append(html);
    $('#role_' + data.role.id + ' #roleType option[value="'+ data.role.type +'"]').attr("selected", "selected");
    for (var key in data.role.tagList) {
        $('#roleTagsModal_' + data.role.id + " #roleTags_" + data.role.tagList[key]).attr('checked', 'checked');
    }
}