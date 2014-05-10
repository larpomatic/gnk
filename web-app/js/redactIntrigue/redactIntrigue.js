$(function(){
    initConfirm();

    initDeleteButton();

    initSearchBoxes();

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
                    var template = Handlebars.templates['templates/redactIntrigue/LeftMenuLiItem'];
                    var context = {
                        roleId: String(data.role.id),
                        roleName: data.role.code
                    };
                    var html = template(context);
                    $('.roleScreen > ul').append(html);
                    initConfirm();
                    initDeleteButton();
                    emptyForm();
                    createNewRolePanel(data);
                    initSearchBoxes();
                    appendEntity("role", data.role.code, "success", "");
                    var nbRoles = parseInt($('.roleLi .badge').html()) + 1;
                    $('.roleLi .badge').html(nbRoles);
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

    // charge les datetimepickers
    $('.datetimepicker').datetimepicker({
        language: 'fr',
        pickSeconds: false
    });

    //permet d'ajouter rapidement un objet, lieu ou role
    $(".leftMenuList input").keypress(function(e) {
        if(e.which == 13) {
            var entity = $(this).attr("data-entity");
            var badgeWarning = '<i class="icon-warning-sign"></i>';
            appendEntity(entity, $(this).val(), $(this).attr("data-label"), badgeWarning);
            $(this).val('');
        }
    });

    initializeTextEditor();

    $('.savePlotForm').submit(function() {
//        initializeTextEditor();
        var description = $('#plotRichTextEditor').html();
        description = transformDescription(description);
//        var escaped = $("div.someClass").text(someHtmlString).html();
        $('input[type="hidden"][name="plotDescription"]').val(description);
    });

});

//Recherche de tags
function initSearchBoxes() {
    $('.search-query').keyup(function() {
        var content = $(this).attr("data-content");
        var value = $(this).val().toLowerCase();
        if (value == "") {
            $('.' + content + ' li').show();
        }
        else {
            $('.' + content + ' li').hide();
            $('.' + content + ' li[data-name*="'+value+'"]').show();
        }
    });
}

//vide le formulaire d'ajout de role
function emptyForm() {
    $('form[name="newRoleForm"] input[type="text"]').val("");
    $('form[name="newRoleForm"] input[type="number"]').val("");
    $('form[name="newRoleForm"] textarea').val("");
    $('form[name="newRoleForm"] input[type="checkbox"]').attr('checked', false);
    $('form[name="newRoleForm"] #roleType option[value="PJ"]').attr("selected", "selected");

}

// initialise les popups de confirmation
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
            if (objectButton.attr("data-object") == "resource") {
//                removeResource(objectButton);
            }
            if (objectButton.attr("data-object") == "place") {
//                removePlace(objectButton);
            }
        }
    });
}

// ajoute une entité dans la vue intrigue
function appendEntity(entity, value, label, flag) {
    var template = Handlebars.templates['templates/redactIntrigue/addEntityLiElement'];
    var context = {
        entityName: value,
        entityLabel: label,
        flag: flag
    };
    var html = template(context);
    $('.' + entity + 'Selector').append(html);
    createNotification("info", "ajout réussi.", "Votre entité a bien été ajoutée, vous pourrez la compléter ultérieurement.");
}

//evite de descendre quand on clique sur un bouton du menu
function bgenScroll() {
    if (window.pageYOffset != null) {
        st = window.pageYOffset + '';
    }
    if (document.body.scrollWidth != null) {
        if (document.body.scrollTop) {
            st = document.body.scrollTop;
        }
        st = document.documentElement.scrollTop;
    }
    setTimeout('window.scroll(0,st)', 10);
}

//desactive le weight des tags
function toggle(checkboxID, toggleID) {
    var checkbox = document.getElementById(checkboxID);
    var toggle = document.getElementById(toggleID);
    updateToggle = toggle.disabled = !checkbox.checked;
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

// supprime un role dans la base
function removeRole(object) {
    var liObject = object.parent();
    $('.roleSelector [data-id="' + object.attr("data-id") + '"]').remove();
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

//initialise les boutons de suppression d'entité
function initDeleteButton() {
    $('.leftMenuList a').click(function() {
        $('.leftMenuList button').css("right", "-40px");
        var button = $(this).next();
        button.css("right", "0px");
    });
}


//Function to insert text in div editable for the description rich editor
function pasteHtmlAtCaret(html) {
    var sel, range;
    if (window.getSelection) {
        // IE9 and non-IE
        sel = window.getSelection();
        if (sel.getRangeAt && sel.rangeCount) {
            range = sel.getRangeAt(0);
            range.deleteContents();

            // Range.createContextualFragment() would be useful here but is
            // only relatively recently standardized and is not supported in
            // some browsers (IE9, for one)
            var el = document.createElement("div");
            el.innerHTML = html;
            var frag = document.createDocumentFragment(), node, lastNode;
            while ((node = el.firstChild)) {
                lastNode = frag.appendChild(node);
            }
            range.insertNode(frag);

            // Preserve the selection
            if (lastNode) {
                range = range.cloneRange();
                range.setStartAfter(lastNode);
                range.collapse(true);
                sel.removeAllRanges();
                sel.addRange(range);
            }
        }
    } else if (document.selection && document.selection.type != "Control") {
        // IE < 9
        document.selection.createRange().pasteHTML(html);
    }
}

//Dernière position du curseur dans l'éditeur
var carretPos = null;

//On sauvegarde la position du curseur lorsque l'éditeur perd le focus
function saveCarretPos(editorName) {
    var caretOffset = 0;
    var element = document.getElementById(editorName);
    var doc = element.ownerDocument || element.document;
    var win = doc.defaultView || doc.parentWindow;
    var sel;
    if (typeof win.getSelection != "undefined") {
        var range = win.getSelection().getRangeAt(0);
        var preCaretRange = range.cloneRange();
        preCaretRange.selectNodeContents(element);
        preCaretRange.setEnd(range.endContainer, range.endOffset);
        caretOffset = preCaretRange.toString().length;
    } else if ((sel = doc.selection) && sel.type != "Control") {
        var textRange = sel.createRange();
        var preCaretTextRange = doc.body.createTextRange();
        preCaretTextRange.moveToElementText(element);
        preCaretTextRange.setEndPoint("EndToEnd", textRange);
        caretOffset = preCaretTextRange.text.length;
    }
    carretPos = window.getSelection().getRangeAt(0); //caretOffset;
    focusedNode = document.activeElement;
    document.getElementById("printHere").innerText = carretPos;

}

// Avant d'insert l'objet on remet le curseur à l'endroit sauvegardé
function setCarretPos() {
    var sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(carretPos);
}

function initializeTextEditor() {
    $('.richTextEditor').each(function() {
        var description = $(this).html();
        description = description.replace(/&lt;l:/g, '<span class="label label-warning" contenteditable="false">');
        description = description.replace(/&lt;n:/g, '<span class="label label-important" contenteditable="false">');
        description = description.replace(/&lt;i:/g, '<span class="label label-success" contenteditable="false">');
        description = description.replace(/&gt;/g, '</span>');
        $(this).html(description);
    });
}

function transformDescription(description) {
    description = description.replace(/<span class="label label-warning" contenteditable="false">/g, '<l:');
    description = description.replace(/<span class="label label-important" contenteditable="false">/g, '<n:');
    description = description.replace(/<span class="label label-success" contenteditable="false">/g, '<i:');
    description = description.replace(/<\/span>/g, '>');
    description = description.replace(/&nbsp;/g, ' ');
    return description;
}