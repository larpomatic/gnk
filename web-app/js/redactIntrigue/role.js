$(function(){
    updatePJG();
    updateRole();

    //ajoute un nouveau role dans la base
    $('.insertRole').click(function() {
        save++;
        if ($('form[name="newRoleForm"] select[name="roleType"]').val() == "STF") {
            $('form[name="newRoleForm"] input[name="roleCode"]').val("Staff");
        }
        var form = $('form[name="newRoleForm"]');
        var description = $('.richTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var pjg_tot = 0;
        $(' input[name="rolePJGP"]').each(function(){
            pjg_tot += parseInt($(this).val());
        });
        if ($('form[name="newRoleForm"] input[name="rolePJGP"]').val() < 0 || parseInt(pjg_tot) > 100)
        {
            createNotification("danger", "Création échouée.", "Votre rôle n'a pas pu être ajouté, le total des PJG est de "+pjg_tot+" et doit être strictement positif, compris entre 0 et 100%.");
            updateSave();
        }

        else {
            if (parseInt(pjg_tot)<100 && parseInt(pjg_tot) > 0)
            {
                createNotification("info", "Attention", "Le total de PJG ( "+pjg_tot +") est inférieur à 100");
            }
            $.ajax({
                type: "POST",
                url: form.attr("data-url"),
                data: form.serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.iscreate) {
                        setTimeout(function(){
                            createNotification("success", "Création réussie.", "Votre rôle a bien été ajouté.");
                        },2000);
                        var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiRole'];
                        var context = {
                            roleId: String(data.role.id),
                            roleName: data.role.code
                        };
                        var html = template(context);
                        $('.roleScreen > ul').append(html);
                        updateRoleRelation(data);
                        initConfirm();
                        updatePJG();
                        initDeleteButton();
                        emptyRoleForm();
                        createNewRolePanel(data);
                        initSearchBoxes();
                        initModifyTag();
                        stopClosingDropdown();
                        if (data.role.type != "STF") {
                            appendEntity("role", data.role.code, "success", "", data.role.id);
                        }
                        var nbRoles = parseInt($('.roleLi .badge').html()) + 1;
                        $('.roleLi .badge').html(nbRoles);
                        initQuickObjects();
                        updateRole();
                        $('form[name="updateRole_' + data.role.id + '"] .btnFullScreen').click(function () {
                            $(this).parent().parent().toggleClass("fullScreenOpen");
                        });
                        var spanList = $('.richTextEditor span.label-default').filter(function () {
                            return $(this).text() == data.role.code;
                        });
                        spanList.each(function () {
                            $(this).removeClass("label-default").addClass("label-success");
                        });
                        $('.roleSelector li[data-id=""]').each(function () {
                            if ($("a", $(this)).html().trim() == data.role.code + ' <i class="icon-warning-sign"></i>') {
                                $(this).remove();
                            }
                        });
                        updateAllDescription($.unique(spanList.closest("form")));
                        updateSave();
                    }
                    else {

                          createNotification("danger", "Création échouée.", "Votre rôle n'a pas pu être ajouté, une erreur s'est produite.");

                        updateSave();
                    }
                },
                error: function () {
                        createNotification("danger", "Création échouée.", "Votre rôle n'a pas pu être ajouté, une erreur s'est produite.");
                    updateSave();
                }
            })
        }
    });
});
function getRole(data)
{
    var form = $('form[name="newRoleForm"]');
    var role= document.getElementById(roleId).valueOf();
    var roleId = $(this).attr("data-id");
    var roles = $('form[name="newRoleForm' + roleId + '"] input[name="roleCode"]').val();
    var context = {
        roleId: String(data.role.id),
        roleName: data.role.code
    };
    $.ajax({
        type: "POST",
        url: form.attr("data-url"),
        data: form.serialize(),
        dataType: "json",
        success: function (data) {
            if (data.object.ischecked) {
                createNotification("success", "Création réussie", "l'objet existe bien, et il est bien appelé !")
            }


        },
        error: function()
        {
            createNotification("danger", "votre objet n'est pas bien utilisé" , "veuillez vérifier les appels que vous avez effectué !")
        }
    })
}

function updateRole() {
    // modifie un role dans la base
    $('.updateRole').click(function() {
        save++;
        var roleId = $(this).attr("data-id");
        var roleName = $('form[name="updateRole_' + roleId + '"] input[name="roleCode"]').val();
        var roleType = $('form[name="updateRole_' + roleId + '"] select[name="roleType"]').val();
        if (roleType == "STF") {
            $('form[name="updateRole_' + roleId + '"] input[name="roleCode"]').val("Staff");
        }
        if (($('.richTextEditor span.label-success:contains("' + roleName + '")').size() > 0) && (roleType == "STF")) {
            createNotification("danger", "Création échouée.", "Ce rôle ne peut pas être staff car il est présent dans des descriptions.");
            updateSave();
        }
        else if (($('.relationScreen .accordion-heading span[data-roleid="'+roleId+'"]').size() > 0) && (roleType == "STF")) {
            createNotification("danger", "Création échouée.", "Ce rôle ne peut pas être staff car il possède des relations.");
            updateSave();
        }
        else {
            if (roleType == "STF") {
                $('.roleSelector li[data-id="'+roleId+'"]').remove();
                $('select[name="relationFrom"] option[value="' + roleId + '"]').remove();
                $('select[name="relationTo"] option[value="' + roleId + '"]').remove();
            }
            else if ($('.roleSelector li[data-id="'+roleId+'"]').size() == 0) {
                appendEntity("role", roleName, "success", "", roleId);
                $('select[name="relationFrom"]').append('<option value="' + roleId + '">' + roleName + '</option>');
                $('select[name="relationTo"]').append('<option value="' + roleId + '">' + roleName + '</option>');
            }
            var form = $('form[name="updateRole_' + roleId + '"]');
            var description = $('.richTextEditor', form).html();
            description = transformDescription(description);

            $('.descriptionContent', form).val(description);
            var pjgp_tot=0;
            $(' input[name="rolePJGP"]').each(function(){

                    if (parseInt($(this).val()) && roleId != undefined && $(this).attr("data-id")) {
                        pjgp_tot += parseInt($(this).val());
                    }

            });
            if (parseInt($('form[name="updateRole_' + roleId + '"] input[name="rolePJGP"]').val()) < 0 || parseInt(pjgp_tot) > 100)
            {
                createNotification("danger", "Modifications échouées.", "le total des PJG est de "+pjgp_tot+" et doit être strictement positif, compris entre 0 et 100%");
                updateSave();
            }
            else {
                if (parseInt(pjgp_tot)<100 && parseInt(pjgp_tot) != 0)
                {
                    createNotification("info", "Attention", "Le total de PJG ( "+pjgp_tot +") est inférieur à 100");
                }
                $.ajax({
                    type: "POST",
                    url: form.attr("data-url"),
                    data: form.serialize(),
                    dataType: "json",
                    success: function (data) {
                        if (data.object.isupdate) {
                            //delay is here to prevent the first notification to disappear to quickly
                            setTimeout(function(){
                                createNotification("success", "Modifications réussies.", "Votre rôle a bien été modifié.");
                            },2000);
                            $('form[name="updateRole_' + roleId + '"] select[name="roleType"] option').removeAttr("selected");
                            $('form[name="updateRole_' + roleId + '"] select[name="roleType"] option[value="' + data.object.type + '"]').attr("selected", "selected");
                            initializeTextEditor();
                            $('.roleScreen .leftMenuList a[href="#role_' + data.object.id + '"]').html(data.object.name);
                            $('.relationScreen .leftMenuList a[href="#roleRelation_' + data.object.id + '"]').html(data.object.name);
                            $('.pastSceneScreen a[href*="#pastsceneRole' + data.object.id + '"]').html(data.object.name);
                            $('.eventScreen a[href*="#eventRole' + data.object.id + '"]').html(data.object.name);
                            $('select[name="relationFrom"] option[value="' + data.object.id + '"]').html(data.object.name);
                            $('select[name="relationTo"] option[value="' + data.object.id + '"]').html(data.object.name);
                            $('select[name="resourceRolePossessor"] option[value="' + data.object.id + '"]').html(data.object.name);
                            $('select[name="resourceRoleFrom"] option[value="' + data.object.id + '"]').html(data.object.name);
                            $('select[name="resourceRoleTo"] option[value="' + data.object.id + '"]').html(data.object.name);
                            $('.relationScreen .accordion-group span[data-roleId="' + data.object.id + '"] span').each(function () {
                                var relationImage = $(this).html();
                                $(this).parent().html(relationImage + " " + data.object.name);
                            });
                            $('.roleSelector li[data-id="' + data.object.id + '"] a').html(data.object.name);
                            $('span.label-success').each(function () {
                                if ($(this).html().trim() == data.object.oldname) {
                                    $(this).html(data.object.name);
                                }
                            });
                            updateSave();
                        }
                        else {
                                createNotification("danger", "Modifications échouées.", "Votre rôle : *"+ $('form[name="updateRole_' + roleId + '"] input[name="roleCode"]').val()+"*  n'a pas pu être modifié, une erreur s'est produite.");
                                updateSave();}
                    },
                    error: function () {
                            createNotification("danger", "Modifications échouées.", "Votre rôle : *"+ $('form[name="updateRole_' + roleId + '"] input[name="roleCode"]').val()+"*  n'a pas pu être modifié, une erreur s'est produite.");
                            updateSave();
                    }
                })
            }
        }
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
        createNotification("danger", "Suppression impossible.", "Votre rôle est utilisé dans certaines descriptions."
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
                    $('.pastSceneScreen a[href*="#pastsceneRole' + object.attr("data-id") +'"]').parent().remove();
                    $('.pastSceneScreen div[id*="pastsceneRole' + object.attr("data-id") + '"]').remove();
                    $('.eventScreen a[href*="#eventRole' + object.attr("data-id") +'"]').parent().remove();
                    $('.eventScreen div[id*="eventRole' + object.attr("data-id") + '"]').remove();
                    var nbRoles = parseInt($('.roleLi .badge').html()) - 1;
                    $('.roleLi .badge').html(nbRoles);
                    $('.addRole').trigger("click");
                    $('.roleSelector li[data-id="' + object.attr("data-id") + '"]').remove();
                    $('form[name="updateRole_' + object.attr("data-id") + '"]').remove();
                    $('.richTextEditor span.label-success').each(function() {
                        if ($(this).html() == name) {
                            $(this).remove();
                        }
                    });
                    $('.numberRelation').html($('.relationScreen .accordion-group').size());
                    createNotification("success", "Supression réussie.", "Votre rôle a bien été supprimé.");
                }
                else {
                    createNotification("danger", "Suppression échouée.", "Votre rôle n'a pas pu être supprimé, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Suppression échouée.", "Votre rôle n'a pas pu être supprimé, une erreur s'est produite.");
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
    $('form[name="newRoleForm"] .pjgp_new').hide();
    $('form[name="newRoleForm"] input[name="rolePJGP"]').val(parseInt("0"));
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
        value = convertHTMLRegisterHelper(value);
        return new Handlebars.SafeString(value);
    });
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
    $('.pastSceneScreen div[id*="pastsceneRolesModal"]').each(function() {
        var pastsceneId = $(this).attr("id");
        pastsceneId = pastsceneId.replace("pastsceneRolesModal", "");
        template = Handlebars.templates['templates/redactIntrigue/addRoleInPastScene'];
        context = {
            roleId: data.role.id,
            roleCode: data.role.code,
            pastsceneId: pastsceneId
        };
        html = template(context);
        $(".tab-content", $(this)).append(html);
        $(".leftUl", $(this)).append('<li><a href="#pastsceneRole'+data.role.id+'_'+pastsceneId+'" data-toggle="tab">'+data.role.code+'</a></li>')
    });
    $('.btn-group', plotFullscreenEditable).clone().prependTo('.pastSceneScreen div[id*="pastsceneRole' + data.role.id + '"] .fullScreenEditable');

    $('.pastSceneScreen div[id*="pastsceneRole' + data.role.id + '"] .fullScreenEditable .btnFullScreen').click(function() {
        $(this).parent().parent().toggleClass("fullScreenOpen");
    }); 
    $('.eventScreen div[id*="eventRolesModal"]').each(function() {
        var eventId = $(this).attr("id");
        eventId = eventId.replace("eventRolesModal", "");
        $(".leftUl", $(this)).append('<li><a href="#eventRole'+data.role.id+'_'+eventId+'" data-toggle="tab">'+data.role.code+'</a></li>');
        template = Handlebars.templates['templates/redactIntrigue/addRoleInEvent'];
        context = {
            roleId: data.role.id,
            roleCode: data.role.code,
            eventId: eventId,
            resourceList: data.role.resourceList
        };
        html = template(context);
        $(".tab-content:first", $(this)).append(html);
//        $('.eventScreen #eventRolesModal tbody').first().clone().appendTo($('#eventRole'+data.role.id+'_'+eventId+' table', $(this)));
    });
    $('.btn-group', plotFullscreenEditable).clone().prependTo('.eventScreen div[id*="eventRole' + data.role.id + '"] .fullScreenEditable');

    $('.eventScreen div[id*="eventRole' + data.role.id + '"] .fullScreenEditable .btnFullScreen').click(function() {
        $(this).parent().parent().toggleClass("fullScreenOpen");
    });
    initializePopover();
    updatePJG();
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

// Hide or Show the PJG %
function updatePJG(){
    var new_pjg = $('.pjgp_new')

    new_pjg.hide();
    $('select[name="roleType"]').each(function(){
        if ($(this).val() == "PJG") {
            $(this).parent().next().show();
            $(this).parent().next().next().show();
        }
        else{
            $(this).parent().next().hide();
            $(this).parent().next().next().hide();
        }
    });
    $('form[name="newRoleForm"] select[name="roleType"]').change(function(){
        var pjg_tota = tot();
        $.when(tot()).done(function(pjg_tota){
            if ($('form[name="newRoleForm"] select[name="roleType"]').val() == "PJG")
            {
                new_pjg.show();
                $('form[name="newRoleForm"] input[name="rolePJGP"]').val(parseInt(100 - pjg_tota));
            }
            else {
                $('form[name="newRoleForm"] input[name="rolePJGP"]').val(parseInt("0"));
                new_pjg.hide();
            }
        });
    });

    $('select[name="roleType"]').change(function(){
        var role_id = $(this).attr("data-id");
        var pjg_tot = tot();
            if (role_id){
                if ($(this).val() == "PJG") {
                    $(this).parent().next().show();
                    $(this).parent().next().next().show();
                    $('input[name="rolePJGP"]').each(function () {
                        if (role_id == $(this).attr("data-id")){
                            $(this).val(parseInt(100 - pjg_tot));
                        }
                    });
                }
                else{
                    $('input[name="rolePJGP"]').each(function () {
                        if (role_id == $(this).attr("data-id")){
                            $(this).val(parseInt("0"));
                        }
                    });
                    $(this).parent().next().hide();
                    $(this).parent().next().next().hide();;
                }
            }
    });
}
function tot (){
    var pjg_tota = 0;
    $(' input[name="rolePJGP"]').each(function(){
        var role_id = $(this).attr("data-id");
        if (role_id){
            pjg_tota += parseInt($(this).val());
        }
    });
    return pjg_tota;
}