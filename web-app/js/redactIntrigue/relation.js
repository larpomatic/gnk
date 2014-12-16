$(function(){
    updateRelation();

    //ajoute une nouvelle relation dans la base
    $('.insertRelation').click(function() {
        var form = $('form[name="newRelationForm"]');
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
                    createNotification("success", "Création réussie.", "Votre relation a bien été ajoutée.");
                    createNewRelationPanel(data);
                    initConfirm();
                    emptyRelationForm();
                    stopClosingDropdown();
                    $('.numberRelation').html($('.relationScreen .accordion-group').size());
                    initQuickObjects();
                    updateRelation();
                }
                else {
                    createNotification("danger", "création échouée.", "Votre relation n'a pas pu être ajoutée, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "création échouée.", "Votre relation n'a pas pu être ajoutée, une erreur s'est produite.");
            }
        })
    });
});

// modifie une relation dans la base
function updateRelation() {
    $('.updateRelation').click(function() {
        var roleFromIdRelation = $(this).attr("data-roleFromId");
        var oldRoleToId = $(this).attr("data-oldRoleToId");
        var wasBijective = $(this).attr("data-wasBijective");
        var form = $(this).parent();
        var description = $('.richTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var updateButton = $(this);
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.isupdate) {
                    Handlebars.registerHelper('encodeAsHtml', function(value) {
                        value = convertHTMLRegisterHelper(value);
                        return new Handlebars.SafeString(value);
                    });
                    if ((roleFromIdRelation == data.relation.RoleFromId.toString())) {
                            $('#accordionRelation' + oldRoleToId + ' .accordion-group[data-relation="' + data.relation.id + '"]').remove();
                        var template = Handlebars.templates['templates/redactIntrigue/relationPanel'];
                        var roleFromId = data.relation.RoleFromId;
                        var roleToId = data.relation.RoleToId;
                        var roleFromCode = data.relation.RoleFromCode;
                        var roleToCode = data.relation.RoleToCode;
                        data.relation.RoleFromId = roleToId;
                        data.relation.RoleToId = roleFromId;
                        data.relation.RoleFromCode = roleToCode;
                        data.relation.RoleToCode = roleFromCode;
                        var context = {
                            relation: data.relation,
                            myRelation: false
                        };
                        var html = template(context);
                        $('.relationScreen #accordionRelation' + data.relation.RoleFromId).append(html);
                        $("#newRelation #relationType option").clone().appendTo('#accordionRelation' + data.relation.RoleFromId + ' .accordion-group[data-relation="' + data.relation.id + '"] #relationType');
                        $("#newRelation #relationTo option").clone().appendTo('#accordionRelation' + data.relation.RoleFromId + ' .accordion-group[data-relation="' + data.relation.id + '"] #relationTo');
                        $('#accordionRelation' + data.relation.RoleFromId + ' .accordion-group[data-relation="' + data.relation.id + '"] #relationType option[value="'+ data.relation.RoleRelationTypeId +'"]').attr("selected", "selected");
                        $('#collapseRelation'+data.relation.RoleFromId+'-'+data.relation.id+' #relationTo option[value="'+ data.relation.RoleToId +'"]').attr("selected", "selected");
                        $('#accordionRelation' + data.relation.RoleToId + ' .accordion-group[data-relation="' + data.relation.id + '"] .accordion-heading a').html(data.relation.RoleRelationTypeName);
                        var image;
                        if (data.relation.isBijective) {
                            image = '<img src="/gnk/static/images/redactIntrigue/relations/doubleArrow.png">';
                        }
                        else {
                            image = '<img src="/gnk/static/images/redactIntrigue/relations/rightArrow.png">';
                        }
                        $('#accordionRelation' + data.relation.RoleToId + ' .accordion-group[data-relation="' + data.relation.id + '"] .accordion-heading .text-center').html(image + " " + data.relation.RoleFromCode);
                        updateButton.attr("data-wasBijective", "true");
                        initializeTextEditor();
                        initConfirm();
                        updateRelation();
                    }
                    else {
                        $('#accordionRelation' + oldRoleToId + ' .accordion-group[data-relation="' + data.relation.id + '"]').remove();
                        var template = Handlebars.templates['templates/redactIntrigue/relationPanel'];
                        var context = {
                            relation: data.relation,
                            myRelation: true
                        };
                        var html = template(context);
                        $('.relationScreen #accordionRelation' + data.relation.RoleFromId).append(html);
                        $("#newRelation #relationType option").clone().appendTo('#accordionRelation' + data.relation.RoleFromId + ' .accordion-group[data-relation="' + data.relation.id + '"] #relationType');
                        $("#newRelation #relationTo option").clone().appendTo('#accordionRelation' + data.relation.RoleFromId + ' .accordion-group[data-relation="' + data.relation.id + '"] #relationTo');
                        $('#accordionRelation' + data.relation.RoleFromId + ' .accordion-group[data-relation="' + data.relation.id + '"] #relationType option[value="'+ data.relation.RoleRelationTypeId +'"]').attr("selected", "selected");
                        $('#collapseRelation'+data.relation.RoleFromId+'-'+data.relation.id+' #relationTo option[value="'+ data.relation.RoleToId +'"]').attr("selected", "selected");
                        $('#accordionRelation' + data.relation.RoleToId + ' .accordion-group[data-relation="' + data.relation.id + '"] .accordion-heading a').html(data.relation.RoleRelationTypeName);
                        if (data.relation.isBijective) {
                            image = '<img src="/gnk/static/images/redactIntrigue/relations/doubleArrow.png">';
                        }
                        else {
                            image = '<img src="/gnk/static/images/redactIntrigue/relations/leftArrow.png">';
                        }
                        $('#accordionRelation' + data.relation.RoleToId + ' .accordion-group[data-relation="' + data.relation.id + '"] .accordion-heading .text-center').html("<span>" + image + "</span> " + data.relation.RoleFromCode);
                        updateButton.attr("data-wasBijective", "true");
                        initConfirm();
                        updateRelation();
                        initializePopover();
                    }
                    var plotFullscreenEditable = $('.plotScreen .fullScreenEditable').first();
                    $('.btn-group', plotFullscreenEditable).clone().prependTo('#accordionRelation' + data.relation.RoleFromId + ' .accordion-group[data-relation="' + data.relation.id + '"]' + ' .fullScreenEditable');
                    $('.numberRelation').html($('.relationScreen .accordion-group').size());
                    initQuickObjects();
                    stopClosingDropdown();
                    $('#accordionRelation' + data.relation.RoleFromId + ' .accordion-group[data-relation="' + data.relation.id + '"]' + ' .btnFullScreen').click(function() {
                        $(this).parent().parent().toggleClass("fullScreenOpen");
                    });
                    createNotification("success", "Modifications réussies.", "Votre relation a bien été modifiée.");
                }
                else {
                    createNotification("danger", "Modifications échouées.", "Votre relation n'a pas pu être modifiée, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Modifications échouées.", "Votre relation n'a pas pu être modifiée, une erreur s'est produite.");
            }
        })
    });
}

// supprime une relation dans la base
function removeRelation(object) {
    $.ajax({
        type: "POST",
        url: object.attr("data-url"),
        dataType: "json",
        success: function(data) {
            if (data.object.isdelete) {
                $('.accordion-group[data-relation="' + data.object.oldId + '"]').remove();
                $('.numberRelation').html($('.relationScreen .accordion-group').size());
                createNotification("success", "Supression réussie.", "Votre relation a bien été supprimée.");
            }
            else {
                createNotification("danger", "suppression échouée.", "Votre relation n'a pas pu être supprimée, une erreur s'est produite.");
            }
            return false;
        },
        error: function() {
            createNotification("danger", "suppression échouée.", "Votre relation n'a pas pu être supprimée, une erreur s'est produite.");
        }
    })
}

//vide le formulaire d'ajout d'une relation
function emptyRelationForm() {
    $('form[name="newRelationForm"] input[type="text"]').val("");
    $('form[name="newRelationForm"] input[type="number"]').val("");
    $('form[name="newRelationForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newRelationForm"] textarea').val("");
    $('form[name="newRelationForm"] #relationType option:first').attr("selected", "selected");
    $('form[name="newRelationForm"] #relationFrom option:first').attr("selected", "selected");
    $('form[name="newRelationForm"] #relationTo option:first').attr("selected", "selected");
    $('form[name="newRelationForm"] #relationRichTextEditor').html("");
}

// créé un accordion-group de la nouvelle relation
function createNewRelationPanel(data) {
    Handlebars.registerHelper('encodeAsHtml', function(value) {
        value = convertHTMLRegisterHelper(value);
        return new Handlebars.SafeString(value);
    });
    var template = Handlebars.templates['templates/redactIntrigue/relationPanel'];
    var context = {
        relation: data.relation,
        myRelation: true
    };
    var html = template(context);
    $('.relationScreen #accordionRelation' + data.relation.RoleFromId).append(html);
    var plotFullscreenEditable = $('.plotScreen .fullScreenEditable').first();
    $('.btn-group', plotFullscreenEditable).clone().prependTo('form[name="updateRelation' + data.relation.RoleFromId + '_' + data.relation.id + '"] .fullScreenEditable');
    $('form[name="updateRelation' + data.relation.RoleFromId + '_' + data.relation.id + '"] .btnFullScreen').click(function() {
        $(this).parent().parent().toggleClass("fullScreenOpen");
    });
//    if (data.relation.isBijective) {
        var roleFromId = data.relation.RoleFromId;
        var roleToId = data.relation.RoleToId;
        var roleFromCode = data.relation.RoleFromCode;
        var roleToCode = data.relation.RoleToCode;
        data.relation.RoleFromId = roleToId;
        data.relation.RoleToId = roleFromId;
        data.relation.RoleFromCode = roleToCode;
        data.relation.RoleToCode = roleFromCode;
        context = {
            relation: data.relation,
            myRelation: false
        };
        html = template(context);
        $('.relationScreen #accordionRelation' + data.relation.RoleFromId).append(html);
        $('.btn-group', plotFullscreenEditable).clone().prependTo('form[name="updateRelation' + data.relation.RoleFromId + '_' + data.relation.id + '"] .fullScreenEditable');
        $('form[name="updateRelation' + data.relation.RoleFromId + '_' + data.relation.id + '"] .btnFullScreen').click(function() {
            $(this).parent().parent().toggleClass("fullScreenOpen");
        });
//    }
    $("#newRelation #relationType option").clone().appendTo('.accordion-group[data-relation="' + data.relation.id + '"] #relationType');
    $("#newRelation #relationTo option").clone().appendTo('.accordion-group[data-relation="' + data.relation.id + '"] #relationTo');
    $('#collapseRelation'+data.relation.RoleToId+'-'+data.relation.id+' #relationFrom option[value="'+ data.relation.RoleToId +'"]').attr("selected", "selected");
    $('#collapseRelation'+data.relation.RoleToId+'-'+data.relation.id+' #relationTo option[value="'+ data.relation.RoleFromId +'"]').attr("selected", "selected");
//    if (data.relation.isBijective) {
        $('#collapseRelation'+data.relation.RoleFromId+'-'+data.relation.id+' #relationFrom option[value="'+ data.relation.RoleFromId +'"]').attr("selected", "selected");
        $('#collapseRelation'+data.relation.RoleFromId+'-'+data.relation.id+' #relationTo option[value="'+ data.relation.RoleToId +'"]').attr("selected", "selected");
//    }
    $('.accordion-group[data-relation="' + data.relation.id + '"] #relationType option[value="'+ data.relation.RoleRelationTypeId +'"]').attr("selected", "selected");

    initializePopover();
}