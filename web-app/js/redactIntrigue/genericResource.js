$(function(){
    updateResource();

    $('input[name="resourceIsClue"]').click(function() {
        var form = $(this).parent().parent().parent();
        $('.clueRow', form).toggleClass("hidden");
    });

    //ajoute une nouvelle ressource dans la base
    $('.insertResource').click(function() {
        var form = $('form[name="newResourceForm"]');
        var comment = $('#resourceRichTextEditor', form).html();
        comment = transformDescription(comment);
        $('.commentContent', form).val(comment);
        var description = $('#clueRichTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.iscreate) {
                    createNotification("success", "Création réussie.", "Votre ressource a bien été ajouté.");
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
                    initModifyTag();
                    stopClosingDropdown();
                    appendEntity("resource", data.genericResource.code, "important", "", data.genericResource.id);
                    initQuickObjects();
                    var nbGenericResources = parseInt($('.resourceLi .badge').html()) + 1;
                    $('.resourceLi .badge').html(nbGenericResources);
                    updateResource();
                    $('form[name="updateResource_' + data.genericResource.id + '"] .btnFullScreen').click(function() {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
                    var spanList = $('.richTextEditor span.label-default').filter(function() {
                        return $(this).text() == data.genericResource.code;
                    });
                    spanList.each(function() {
                        $(this).removeClass("label-default").addClass("label-important");
                    });
                    $('.resourceSelector li[data-id=""]').each(function() {
                        if ($("a", $(this)).html().trim() == data.genericResource.code + ' <i class="icon-warning-sign"></i>') {
                            $(this).remove();
                        }
                    });
                    updateAllDescription($.unique(spanList.closest("form")));
                }
                else {
                    createNotification("danger", "création échouée.", "Votre ressource n'a pas pu être ajoutée, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre ressource n'a pas pu être ajoutée, une erreur s'est produite.");
            }
        })
    });
});

// modifie une ressource dans la base
function updateResource() {
    $('.updateResource').click(function() {
        var genericResourceId = $(this).attr("data-id");
        var form = $('form[name="updateResource_' + genericResourceId + '"]');
        var comment = $('#resourceRichTextEditor' + genericResourceId, form).html();
        comment = transformDescription(comment);
        $('.commentContent', form).val(comment);
        var description = $('#clueRichTextEditor' + genericResourceId, form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.isupdate) {
                    createNotification("success", "Modifications réussies.", "Votre ressource a bien été modifiée.");
                    $('.resourceScreen .leftMenuList a[href="#resource_' + data.object.id + '"]').html(data.object.name);
                    $('.resourceSelector li[data-id="' + data.object.id + '"] a').html(data.object.name);
                    $('.eventScreen tbody td[data-id="'+data.object.id+'"]').html(data.object.name);
                    initializeTextEditor();
                    $('.richTextEditor span.label-important').each(function() {
                        if ($(this).html() == data.object.oldname) {
                            $(this).html(data.object.name);
                        }
                    });
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre ressource n'a pas pu être modifiée, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Modifications échouées.", "Votre ressource n'a pas pu être modifiée, une erreur s'est produite.");
            }
        })
    });
}

// supprime une ressource dans la base
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
        createNotification("danger", "suppression impossible.", "Votre ressource est utilisée dans certaines descriptions."
            + " Veuillez supprimer l'utilisation de cet ressource dans les descriptions avant de supprimer l'entité ressource.");
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
                    $('.eventScreen td[data-id="'+object.attr("data-id")+'"]').parent().remove();
                    $('.richTextEditor span.label-important').each(function() {
                       if ($(this).html() == name) {
                           $(this).remove();
                       }
                    });
                    createNotification("success", "Supression réussie.", "Votre ressource a bien été supprimée.");
                }
                else {
                    createNotification("danger", "suppression échouée.", "Votre ressource n'a pas pu être supprimée, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "suppression échouée.", "Votre ressource n'a pas pu être supprimée, une erreur s'est produite.");
            }
        });
    }
}

//vide le formulaire d'ajout de ressource
function emptyGenericResourceForm() {
    $('form[name="newResourceForm"] input[type="text"]').val("");
    $('form[name="newResourceForm"] textarea').val("");
    $('form[name="newResourceForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newResourceForm"] .chooseTag').parent().addClass("invisible");
    $('form[name="newResourceForm"] .banTag').parent().addClass("invisible");
    $('form[name="newResourceForm"] .tagWeightInput').val(50);
    $('form[name="newResourceForm"] .tagWeightInput').attr('disabled','disabled');
    $('form[name="newResourceForm"] .search-query').val("");
    $('form[name="newResourceForm"] .modalLi').show();
    $('form[name="newResourceForm"] .clueRow').addClass("hidden");
    $('form[name="newResourceForm"] #resourceRichTextEditor').html("");
}

// créé un tab-pane de la nouvelle ressource
function createNewGenericResourcePanel(data) {
    Handlebars.registerHelper('toLowerCase', function(value) {
        return new Handlebars.SafeString(value.toLowerCase());
    });
    var audaciousFn;
    Handlebars.registerHelper('ifCond', function(v1, v2, options) {
        if(v1 === v2) {
            return options.fn(this);
        }
        return options.inverse(this);
    });
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
        value = value.replace(/>/g, '</span>');
        value = value.replace(/<l:/g, '<span class="label label-warning" contenteditable="false">');
        value = value.replace(/<o:/g, '<span class="label label-important" contenteditable="false">');
        value = value.replace(/<i:/g, '<span class="label label-success" contenteditable="false">');
        value = value.replace(/<u:/g, '<span class="label label-default" contenteditable="false">');
        return new Handlebars.SafeString(value);
    });
    var template = Handlebars.templates['templates/redactIntrigue/genericResourcePanel'];
    var context = {
        genericResource: data.genericResource,
        genericResourceTagList: data.genericResourceTagList
    };
    var html = template(context);
    $('.resourceScreen > .tab-content').append(html);
    var plotFullscreenEditable = $('.plotScreen .fullScreenEditable').first();
    $('.btn-group', plotFullscreenEditable).clone().prependTo('#resource_' + data.genericResource.id + ' .fullScreenEditable');
    $("#newResource #resourceRolePossessor option").clone().appendTo('form[name="updateResource_' + data.genericResource.id + '"] #resourceRolePossessor');
    $("#newResource #resourceRoleFrom option").clone().appendTo('form[name="updateResource_' + data.genericResource.id + '"] #resourceRoleFrom');
    $("#newResource #resourceRoleTo option").clone().appendTo('form[name="updateResource_' + data.genericResource.id + '"] #resourceRoleTo');
    $('form[name="updateResource_' + data.genericResource.id + '"] #resourceRolePossessor option[value="'+ data.genericResource.possessedByRoleId +'"]').attr("selected", "selected");
    $('form[name="updateResource_' + data.genericResource.id + '"] #resourceRoleFrom option[value="'+ data.genericResource.fromRoleId +'"]').attr("selected", "selected");
    $('form[name="updateResource_' + data.genericResource.id + '"] #resourceRoleTo option[value="'+ data.genericResource.toRoleId +'"]').attr("selected", "selected");
    for (var key in data.genericResource.tagList) {
        $('#resourceTagsModal_' + data.genericResource.id + " #resourceTags" + data.genericResource.id + "_" + data.genericResource.tagList[key].id).attr('checked', 'checked');
        $('#resourceTagsModal_' + data.genericResource.id + " #resourceTagsWeight" + data.genericResource.id + "_" + data.genericResource.tagList[key].id).val(data.genericResource.tagList[key].weight);
    }
    $('#resourceTagsModal_' + data.genericResource.id + ' li').each(function() {
        hideTags($('input[type="checkbox"]', $(this)).attr("id"), $(".tagWeight input", $(this)).attr("id"));
    });

    $('.chooseTag').click(function() {
        $('input', $(this).parent().prev()).val(101);
    });

    $('.banTag').click(function() {
        $('input', $(this).parent().next()).val(-101);
    });
    $('.eventScreen tbody').each(function() {
        var roleId = $(this).attr("data-role");
        $(this).append('<tr><td data-id="'+data.genericResource.id+'">'+data.genericResource.code+
            '</td><td><input type="number" name="quantity'+roleId+'_'+data.genericResource.id+'" value=""/></td></tr>');
    });
}