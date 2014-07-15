$(function(){
    updateRole();

    //ajoute un nouveau role dans la base
    $('.insertRole').click(function() {
        var form = $('form[name="newRoleForm"]');
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
                    createNotification("success", "Création réussie.", "Votre rôle a bien été ajouté.");
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiRole'];
                    var context = {
                        roleId: String(data.role.id),
                        roleName: data.role.code
                    };
                    var html = template(context);
                    $('.roleScreen > ul').append(html);
                    updateRoleRelation(data);
                    initConfirm();
                    initDeleteButton();
                    emptyRoleForm();
                    createNewRolePanel(data);
                    initSearchBoxes();
                    appendEntity("role", data.role.code, "success", "", data.role.id);
                    var nbRoles = parseInt($('.roleLi .badge').html()) + 1;
                    $('.roleLi .badge').html(nbRoles);
                    updateRole();
                    $('form[name="updateRole_' + data.role.id + '"] .btnFullScreen').click(function() {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
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
                    createNotification("success", "Modifications réussies.", "Votre rôle a bien été modifié.");
                    $('.roleScreen .leftMenuList a[href="#role_' + data.object.id + '"]').html(data.object.name);
                    $('.relationScreen .leftMenuList a[href="#roleRelation_' + data.object.id + '"]').html(data.object.name);
                    $('select[name="relationFrom"] option[value="' + data.object.id + '"]').html(data.object.name);
                    $('select[name="relationTo"] option[value="' + data.object.id + '"]').html(data.object.name);
                    $('select[name="resourceRolePossessor"] option[value="' + data.object.id + '"]').html(data.object.name);
                    $('select[name="resourceRoleFrom"] option[value="' + data.object.id + '"]').html(data.object.name);
                    $('select[name="resourceRoleTo"] option[value="' + data.object.id + '"]').html(data.object.name);
                    $('.relationScreen .accordion-group span[data-roleId="' + data.object.id + '"]').html('Vers: ' + data.object.name);
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
    var isRolePresentInDescriptions = false;
    $('.richTextEditor span.label-success').each(function() {
        if ($(this).html() == name) {
            isRolePresentInDescriptions = true;
        }
    });
    if (isRolePresentInDescriptions) {
        createNotification("danger", "suppression impossible.", "Votre rôle est utilisé dans certaines descriptions."
            + " Veuillez supprimer l'utilisation de ce rôle dans les descriptions avant de supprimer l'entité rôle.");
    }
    else {
        $.ajax({
            type: "POST",
            url: object.attr("data-url"),
            dataType: "json",
            success: function(data) {
                if (data.object.isdelete) {
                    liObject.remove();
                    $('select[name="relationFrom"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="relationTo"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="resourceRolePossessor"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="resourceRoleFrom"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('select[name="resourceRoleTo"] option[value="' + object.attr("data-id") + '"]').remove();
                    $('.relationScreen .leftMenuList a[href="#roleRelation_' + object.attr("data-id") + '"]').parent().remove();
                    $('.relationScreen #roleRelation_' + object.attr("data-id")).remove();
                    $('.relationScreen .accordion-group[data-roleTo="' + object.attr("data-id") + '"]').remove();
                    var nbRoles = parseInt($('.roleLi .badge').html()) - 1;
                    $('.roleLi .badge').html(nbRoles);
                    $('.addRole').trigger("click");
                    $('.roleSelector li[data-id="' + object.attr("data-id") + '"]').remove();
                    $('.richTextEditor span.label-success').each(function() {
                        if ($(this).html() == name) {
                            $(this).remove();
                        }
                    });
                    $('.numberRelation').html($('.relationScreen .accordion-group').size());
                    createNotification("success", "Supression réussie.", "Votre rôle a bien été supprimé.");
                }
                else {
                    createNotification("danger", "suppression échouée.", "Votre rôle n'a pas pu être supprimé, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "suppression échouée.", "Votre rôle n'a pas pu être supprimé, une erreur s'est produite.");
            }
        });
    }
}

//vide le formulaire d'ajout de role
function emptyRoleForm() {
    $('form[name="newRoleForm"] input[type="text"]').val("");
    $('form[name="newRoleForm"] input[type="number"]').val("");
    $('form[name="newRoleForm"] textarea').val("");
    $('form[name="newRoleForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newRoleForm"] #roleType option[value="PJ"]').attr("selected", "selected");
    $('form[name="newRoleForm"] .chooseTag').parent().addClass("invisible");
    $('form[name="newRoleForm"] .banTag').parent().addClass("invisible");
    $('form[name="newRoleForm"] .tagWeightInput').val(50);
    $('form[name="newRoleForm"] .tagWeightInput').attr('disabled','disabled');
    $('form[name="newRoleForm"] .search-query').val("");
    $('form[name="newRoleForm"] .modalLi').show();
    $('form[name="newRoleForm"] #roleRichTextEditor').html("");
}

// créé un tab-pane du nouveau role
function createNewRolePanel(data) {
    Handlebars.registerHelper('toLowerCase', function(value) {
        return new Handlebars.SafeString(value.toLowerCase());
    });
    var audaciousFn;
    Handlebars.registerHelper('recursive', function(children, options) {
        var out = '';
        if (options.fn !== undefined) {
            audaciousFn = options.fn;
        }
        children.forEach(function(child){
            out = out + audaciousFn(child);
        });
        return out;
    });
    Handlebars.registerHelper('encodeAsHtml', function(value) {
        value = value.replace(/<l:/g, '<span class="label label-warning" contenteditable="false">');
        value = value.replace(/<o:/g, '<span class="label label-important" contenteditable="false">');
        value = value.replace(/<i:/g, '<span class="label label-success" contenteditable="false">');
        value = value.replace(/>/g, '</span>');
        return new Handlebars.SafeString(value);
    });
    var template = Handlebars.templates['templates/redactIntrigue/rolePanel'];
    var context = {
        role: data.role,
        roleTagList: data.roleTagList
    };
    var html = template(context);
    $('.roleScreen > .tab-content').append(html);
    var plotFullscreenEditable = $('.plotScreen .fullScreenEditable').first();
    $('.btn-group', plotFullscreenEditable).clone().prependTo('#role_' + data.role.id + ' .fullScreenEditable');
    $('#role_' + data.role.id + ' #roleType option[value="'+ data.role.type +'"]').attr("selected", "selected");
    for (var key in data.role.tagList) {
        $('#roleTagsModal_' + data.role.id + " #roleTags" + data.role.id + "_" + data.role.tagList[key].id).attr('checked', 'checked');
        $('#roleTagsModal_' + data.role.id + " #roleTagsWeight" + data.role.id + "_" + data.role.tagList[key].id).val(data.role.tagList[key].weight);
    }
    $('#roleTagsModal_' + data.role.id + ' li').each(function() {
        hideTags($('input[type="checkbox"]', $(this)).attr("id"), $(".tagWeight input", $(this)).attr("id"));
    });

    $('.chooseTag').click(function() {
        $('input', $(this).parent().prev()).val(101);
    });

    $('.banTag').click(function() {
        $('input', $(this).parent().next()).val(-101);
    });
}

function updateRoleRelation(data) {
    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiRoleRelation'];
    var context = {
        roleId: String(data.role.id),
        roleName: data.role.code
    };
    var html = template(context);
    $('.relationScreen > ul').append(html);
    $('select[name="relationFrom"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('select[name="relationTo"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('select[name="resourceRolePossessor"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('select[name="resourceRoleFrom"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('select[name="resourceRoleTo"]').append('<option value="' + data.role.id + '">' + data.role.code + '</option>');
    $('.relationScreen .tab-content').append('<div class="tab-pane" id="roleRelation_'+data.role.id+'">'
    + '<div class="accordion" id="accordionRelation'+data.role.id+'"></div></div>');
}