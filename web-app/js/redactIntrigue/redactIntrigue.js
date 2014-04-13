$(function(){

    function initConfirm() {
        $('[data-toggle="confirmation-popout"]').confirmation({
            popout: true,
            btnOkLabel: "Oui",
            btnCancelLabel: "Non",
            btnOkClass: "btn-success",
            btnCancelClass: "btn-danger",
            onConfirm:function() {
                var objectButton = $(this).closest("div.popover").prev()
                if (objectButton.attr("data-object") == "role") {
                    removeRole(objectButton);
                }
            }
        });
    }

    initConfirm();

    function initDeleteButton() {
        $('.leftMenuList a').click(function() {
            $('.leftMenuList button').css("right", "-40px");
            var button = $(this).next();
            button.css("right", "0px");
        });
    }

    initDeleteButton();

    function removeRole(object) {
        var liObject = object.parent();
        $.ajax({
            type: "POST",
            url: object.attr("data-url"),
            dataType: "json",
            success: function(data) {
                if (data.object.isdelete) {
                    liObject.remove();
                    $('.addRole').trigger("click");
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

    $('.insertRole').click(function() {
        var form = $('form[name="newRoleForm"]');
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.iscreate) {
                    createNotification("success", "Création réussie.", "Votre rôle a bien été ajouté.");
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiItem'];
                    var context = {
                        roleId: String(data.object.id),
                        roleName: data.object.name
                    };
                    var html = template(context);
                    $('.roleScreen > ul').append(html);
                    initConfirm();
                    initDeleteButton();
                    emptyForm();
                    // TODO créer un autre template pour générer le nouveau panel
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

    function emptyForm() {
        $('form[name="newRoleForm"] input').val("");
        $('form[name="newRoleForm"] textarea').val("");
        $('form[name="newRoleForm"] input[type="checkbox"]').attr('checked', false);
    }
});