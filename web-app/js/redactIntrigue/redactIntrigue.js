$(function(){
    var element;

    $('body').keydown(function(e) {
       keyhandler(e);
    });

    initConfirm();

    initDeleteButton();

    initSearchBoxes();

    // ajout du nombre de relation
    $('.numberRelation').html($('.relationScreen .accordion-group:not(.leftRelation)').size());

    // charge les datetimepickers
    $('.datetimepicker').datetimepicker({
        language: 'fr',
        pickSeconds: false
    });

    stopClosingDropdown();

    initQuickObjects();

    initializeTextEditor();

    initializeClosingPopover();

    // on supprimer le role staff des select dans l'onglet relation
    $('select[name="relationTo"] option').each(function() {
        if ($(this).html().toLowerCase() == "staff") {
            $(this).remove();
        }
    });
    $('select[name="relationFrom"] option').each(function() {
        if ($(this).html().toLowerCase() == "staff") {
            $(this).remove();
        }
    });

    // on ajoute la description d'un plot dans le champ hidden correspondant
    $('.updatePlot').click(function() {
        if (($('.richTextEditor span.label-default').size() > 0) && ($('#isDraft:checked').size() == 0)) {
            createNotification("danger", "Enregistrement échoué.", "Votre intrigue comporte des éléments non enregistrés en base de données, enregistrez les ou passer l'intrigue en mode brouillon pour pouvoir continuer.");
            return false;
        }
        var form = $('.savePlotForm');
        var description = $('#plotRichTextEditor', form).html();
        description = transformDescription(description);
        $('.descriptionContent', form).val(description);
        var pitchOrga = $('#plotRichTextEditorPitchOrga', form).html();
        pitchOrga = transformDescription(pitchOrga);
        $('.pitchOrgaContent', form).val(pitchOrga);
        var pitchPj = $('#plotRichTextEditorPitchPj', form).html();
        pitchPj = transformDescription(pitchPj);
        $('.pitchPjContent', form).val(pitchPj);
        var pitchPnj = $('#plotRichTextEditorPitchPnj', form).html();
        pitchPnj = transformDescription(pitchPnj);
        $('.pitchPnjContent', form).val(pitchPnj);
        $.ajax({
            type: "POST",
            url: form.attr("data-url"),
            data: form.serialize(),
            dataType: "json",
            success: function(data) {
                if (data.object.isupdate) {
                    initializeTextEditor();
                    createNotification("success", "Modifications réussies.", "Votre intrigue a bien été modifiée.");

                }
                else {
                    createNotification("danger", "Modification échouée.", "Votre intrigue n'a pas pu être ajoutée, une erreur s'est produite.");
                }
            },
            error: function() {
                createNotification("danger", "Modification échouée.", "Votre intrigue n'a pas pu être ajoutée, une erreur s'est produite.");
            }
        })

    });

    // mode plein ecran
    $('.btnFullScreen').click(function() {
        $(this).parent().parent().toggleClass("fullScreenOpen");
    });

    bgenScroll();

    initModifyTag();

    initSpanCreation();

    initSpanLabel('.spanLabel');
});

//add span on labels like "<i:role>"
function initSpanLabel(element) {
    $(element).each(function() {
        var description = $(this).html();
        while (description.length != 0 && (description[0] == '\n' ||
            description[0] == ' ' || description[0] == '\r')) {
            description = description.substring(1, description.length)
        }
        while (description.length != 0 && (description[description.length - 1] == '\n' ||
            description[description.length - 1] == ' ' || description[description.length - 1] == '\r')) {
            description = description.substring(0, description.length - 1)
        }
        description = "<div>" + convertDescription(description) + "</div>";
        var html = $(description);
        $("span br", html).remove();
        description = html.html();
        $(this).html(description);
    });
}

//insert html span into textEditors
function initSpanCreation() {
    $(".buttonRichTextEditor").unbind('click');
    $('.buttonRichTextEditor').click(function() {
        setCarretPos();
        if ($(this).closest("ul").hasClass("roleSelector")) {
            pasteHtmlAtCaret('<span class="label label-success" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">' + $(this).html().trim() + '</span>');
        }
        else if ($(this).closest("ul").hasClass("placeSelector")) {
            pasteHtmlAtCaret('<span class="label label-warning" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">' + $(this).html().trim() + '</span>');
        }
        else if ($(this).closest("ul").hasClass("resourceSelector")) {
            pasteHtmlAtCaret('<span class="label label-important" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">' + $(this).html().trim() + '</span>');
        }
        initializePopover();
        return false;
    });
}

function initQuickObjects() {
    //permet d'ajouter rapidement une ressource, lieu ou role
    $(".leftMenuList input, .inputOther").keypress(function(e) {
        if(e.which == 13) {
            var entity = $(this).attr("data-entity");
            var badgeWarning = '<i class="icon-warning-sign"></i>';
            appendEntity(entity, $(this).val(), "default", badgeWarning);
            $(this).val('');
            return false;
        }
    });
}

// empeche les dropdown de se fermer lorsqu'on focus un input
function stopClosingDropdown() {
    $('.dropdown-menu input, .dropdown-menu label').click(function(e) {
        e.stopPropagation();
    });
}

//active le bouton "modifier" des fenêtres de tag
function initModifyTag() {
    $('.modifyTag').unbind("click");
    $('.modifyTag').click(function() {
        if ($(this).hasClass("push")) {
            $('.modal-body li', $(this).parent().parent()).hide();
            $('.modal-body li input[type="checkbox"]:checked', $(this).parent().parent()).parent().parent().show();
            $('.modal-body li input[type="checkbox"]:checked', $(this).parent().parent()).parents("*").show();
        }
        else {
            $('.modal-body li', $(this).parent().parent()).show();
        }
        $(this).toggleClass("push");
        $(this).toggleClass("active");
    });
}

//Recherche de tags
function initSearchBoxes() {
    $('.search-query').keyup(function() {
        var content = $(this).attr("data-content");
        var value = $(this).val().toLowerCase();
        if (value == "") {
            $('.' + content + ' li').show();
        }
        else {
            $("." + content + " li").hide();
            var children = $('.' + content + ' li[data-name*="'+value+'"]');
            children.show();
            children.parents("*").show();
        }
    });
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
            if (objectButton.attr("data-object") == "event") {
                removeEvent(objectButton);
            }
            if (objectButton.attr("data-object") == "pastScene") {
                removePastScene(objectButton);
            }
            if (objectButton.attr("data-object") == "place") {
                removePlace(objectButton);
            }
            if (objectButton.attr("data-object") == "resource") {
                removeResource(objectButton);
            }
            if (objectButton.attr("data-object") == "relation") {
                removeRelation(objectButton);
            }
            if (objectButton.attr("data-object") == "plot") {
                $.ajax({
                   type: "POST",
                   url: objectButton.attr("data-url"),
                   dataType: "json",
                   success: function() {
                        window.location.href = objectButton.attr("data-redirect");
                   },
                    error: function() {
                        createNotification("danger", "suppression échouée.", "L'intrigue n'a pas pu être supprimée.");
                    }
                });
            }
            return false;
        }
    });
}

// ajoute une entité dans la vue intrigue
function appendEntity(entity, value, label, flag, id) {
    var template = Handlebars.templates['templates/redactIntrigue/addEntityLiElement'];
    var context = {
        entityName: value,
        entityLabel: label,
        flag: flag,
        id: id
    };
    var html = template(context);
    $(html).insertBefore('.' + entity + 'Selector li:last-child');
    if (flag != "") {
        createNotification("info", "ajout réussi.", "Votre entité a bien été ajoutée, vous pourrez la compléter ultérieurement.");
    }
    initSpanCreation();
}

//evite de descendre quand on clique sur un bouton du menu
function bgenScroll(e) {
    if (window.pageYOffset != null) {
        st = window.pageYOffset + '';
    }
    if (document.body.scrollWidth != null) {
        if (document.body.scrollTop) {
            st = document.body.scrollTop;
        }
        st = document.documentElement.scrollTop;
    }
    setTimeout('window.scroll(0,st)', 50);
}

//desactive le weight des tags
function toggle(checkboxID, toggleID) {
    var checkbox = document.getElementById(checkboxID);
    var toggle = document.getElementById(toggleID);
    updateToggle = toggle.disabled = !checkbox.checked;
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

// on remplace les balise de la description par des span html
function initializeTextEditor() {
    $('.richTextEditor').each(function() {
        var description = $(this).html();
        while (description.length != 0 && (description[0] == '\n' ||
            description[0] == ' ' || description[0] == '\r')) {
            description = description.substring(1, description.length)
        }
        while (description.length != 0 && (description[description.length - 1] == '\n' ||
            description[description.length - 1] == ' ' || description[description.length - 1] == '\r')) {
            description = description.substring(0, description.length - 1)
        }
        description = "<div>" + convertDescription(description) + "</div>";
        var html = $(description);
        $("span br", html).remove();
        description = html.html();
        $(this).html(description);
    });
    initializePopover();
}

function convertDescription(description) {
    description = description.replace(/\n/g, '<br>');
    description = description.replace(/&lt;L:/g, '&lt;l:');
    description = description.replace(/&lt;I:/g, '&lt;i:');
    description = description.replace(/&lt;O:/g, '&lt;o:');
    description = description.replace(/&lt;l:Art:/g, '<span class="label label-warning" data-tag="Art" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;l:Nom:/g, '<span class="label label-warning" data-tag="Nom" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;l:Par:/g, '<span class="label label-warning" data-tag="Par" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;l:Pos:/g, '<span class="label label-warning" data-tag="Pos" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;l:none:/g, '<span class="label label-warning" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;l:M#/g, '<span class="label label-success" data-tag="M#');
    description = description.replace(/&lt;o:Art:/g, '<span class="label label-important" data-tag="Art" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;o:Nom:/g, '<span class="label label-important" data-tag="Nom" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;o:Par:/g, '<span class="label label-important" data-tag="Par" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;o:Pos:/g, '<span class="label label-important" data-tag="Pos" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;o:Cont:/g, '<span class="label label-important" data-tag="Cont" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;o:none:/g, '<span class="label label-important" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;o:M#/g, '<span class="label label-success" data-tag="M#');
    description = description.replace(/&lt;i:Pre:/g, '<span class="label label-success" data-tag="Pre" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;i:inif:/g, '<span class="label label-success" data-tag="inif" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;i:Pat:/g, '<span class="label label-success" data-tag="Pat" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;i:Age:/g, '<span class="label label-success" data-tag="Age" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;i:none:/g, '<span class="label label-success" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;i:M#/g, '<span class="label label-success" data-tag="M#');
    description = description.replace(/&lt;l:/g, '<span class="label label-warning" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;o:/g, '<span class="label label-important" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;i:/g, '<span class="label label-success" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&lt;u:/g, '<span class="label label-default" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/;:/g, ';" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/&gt;/g, '</span>');

    return description;
}

// on remplace les span html dans une description par des balises

function transformDescription(description) {
    description = "<div>" + description + "</div>";
    var html = $(description);
    $("span:not(.label)", html).contents().unwrap();
    description = html.html();
    description = description.replace(/<div>/g, '\n');
    description = description.replace(/<\/div>/g, '\n');
    description = description.replace(/<br>/g, '\n');
    description = description.replace(/<span class="label label-warning" data-tag="/g, '<l:');
    description = description.replace(/<span class="label label-important" data-tag="/g, '<o:');
    description = description.replace(/<span class="label label-success" data-tag="/g, '<i:');
    description = description.replace(/<span class="label label-default" data-tag="/g, '<u:');
    description = description.replace(/" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">/g, ':');
    description = description.replace(/" data-toggle="popover" data-original-title="Choix balise" title="" contenteditable="false">/g, ':');
    description = description.replace(/<\/span>/g, '>');
    description = description.replace(/&nbsp;/g, ' ');
    description = description.replace(/&lt;l:/g, '<l:');
    description = description.replace(/&lt;o:/g, '<o:');
    description = description.replace(/&lt;i:/g, '<i:');
    description = description.replace(/&lt;u:/g, '<u:');
    description = description.replace(/&gt;/g, '>');
    return description;
}

//update all these forms
function updateAllDescription(formlist) {
    formlist.each(function() {
        $('input[name="Update"]', $(this)).trigger("click");
    });
}

function initializePopover() {
    var spanPopover = '<div class="specialTag"><button class="btn btn-small btn-primary" data-tag="Art">Article</button>' +
        '<button class="btn btn-small btn-primary" data-tag="Nom">Nominatif</button></div>' +
        '<div class="specialTag"><button class="btn btn-small btn-primary" data-tag="Par">Particule</button>' +
        '<button class="btn btn-small btn-primary" data-tag="Pos">Possessif</button></div>' +
        '<div class="specialTag"><button class="btn btn-small btn-primary" data-tag="Per">Perso</button></div>' +
        '<div class="MFfields"><input type="text" placeholder="Masculin"/><input type="text" placeholder="Féminin"/></div>' +
        '<div class="specialTag"><button class="btn btn-success btn-small none" data-tag="none">Aucune</button></div>';
        '<div class="specialTag"><button class="btn btn-success btn-small none" data-tag="Cont">Contenu</button></div>';
    $('.richTextEditor .label[contenteditable="false"]:not(.label-success)').popover({
        html: 'true',
        placement: 'bottom',
        content: spanPopover,
        container: "body",
        delay: { "show": 0, "hide": 0 }
    });
    var spanPopoverRole = '<div class="specialTag"><button class="btn btn-small btn-primary" data-tag="Pre">Prénom</button>' +
        '<button class="btn btn-small btn-primary" data-tag="Pat">Patronyme</button></div>' +
        '<div class="specialTag"><button class="btn btn-success btn-small none" data-tag="inif">Initiale prénom</button></div>'   +
        '<div class="specialTag"><button class="btn btn-small btn-primary" data-tag="Age">Âge</button>' +
        '<button class="btn btn-small btn-primary" data-tag="Per">Perso</button></div>' +
        '<div class="MFfields"><input type="text" placeholder="Masculin"/><input type="text" placeholder="Féminin"/></div>' +
        '<div class="specialTag"><button class="btn btn-success btn-small none" data-tag="none">Aucune</button></div>';
    $('.richTextEditor .label[contenteditable="false"].label-success').popover({
        html: 'true',
        placement: 'bottom',
        content: spanPopoverRole,
        container: "body",
        delay: { "show": 0, "hide": 0 }
    });



    $('.richTextEditor .label[contenteditable="false"]').on("shown.bs.popover", function () {
        if ($('.popover').size() > 1) {
            $(this).popover('hide');
        }
        else {
            element = $(this);
            $('.popover button').removeClass("btn-success").addClass("btn-primary");
            $('.popover button[data-tag="' + $(element).attr("data-tag") + '"]').addClass("btn-success");
            if ($('.popover button.btn-success').size() == 0) {
                $('.popover button[data-tag="Per"]').addClass('btn-success').removeClass("btn-primary");
                var tag = $(element).attr("data-tag");
                var maleName = tag.match("M#(.*);F#");
                var femaleName = tag.match("F#(.*);");
                $('.popover input[placeholder="Masculin"]').val(maleName[1]);
                $('.popover input[placeholder="Féminin"]').val(femaleName[1]);
            }
            $('.specialTag button').click(function() {
                $("button", $(this).closest(".popover-content")).removeClass("btn-success").addClass("btn-primary");
                $(this).removeClass("btn-primary").addClass("btn-success");
                $(element).attr("data-tag", $(this).attr("data-tag"));
            });
        }
    });
}

function initializeClosingPopover() {
    $('body').mousedown(function (e) {
        if ($('.popover').size() != 0) {
            if (typeof element !== "undefined" && !$(element).is(e.target) && $(element).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                if ($(element).attr("data-tag") == "Per" || $(element).attr("data-tag").match("^M#")) {
                    var newtag = "M#" + $('.popover input[placeholder="Masculin"]').val() + ";F#" + $('.popover input[placeholder="Féminin"]').val() + ";";
                    $(element).attr("data-tag", newtag);
                }
                $(element).popover('hide');
            }
        }
    });
}


// gère la suppression du backspace
function keyhandler(e) {
    var key = e.keyCode;
    if (key == 8) {
        // fix backspace bug in FF
        // https://bugzilla.mozilla.org/show_bug.cgi?id=685445
        var selection = window.getSelection();
        if (!selection.isCollapsed || !selection.rangeCount) {
            return;
        }
        var curRange = selection.getRangeAt(selection.rangeCount - 1);
        if (curRange.commonAncestorContainer.nodeType == 3 && curRange.startOffset > 0) {
            // we are in child selection. The characters of the text node is being deleted
            return;
        }

        var range = document.createRange();

        referenceNodes = document.getElementsByClassName("editable");
        // referenceNode = document.getElementsByClassName("editable");
        for (var i = 0; i < referenceNodes.length; i++) {
            var referenceNode = referenceNodes[i];
            console.log(referenceNode);
            if (selection.anchorNode != referenceNode) {
                // selection is in character mode. expand it to the whole editable field
                range.selectNodeContents(referenceNode);
                range.setEndBefore(selection.anchorNode);
            } else if (selection.anchorOffset > 0) {
                range.setEnd(referenceNode, selection.anchorOffset);
            } else {
                // reached the beginning of editable field
                return;
            }
            range.setStart(referenceNode, range.endOffset - 1);

            var previousNode = range.cloneContents().lastChild


            if (previousNode && previousNode.contentEditable == 'false') {
                // this is some rich content, e.g. smile. We should help the user to delete it
                range.deleteContents();
                event.preventDefault();
            }
        }
    }
}


/*
$('.editable').on('keydown', function (event) {
        alert('test');
    if (window.getSelection && event.which == 8) { // backspace
        // fix backspace bug in FF
        // https://bugzilla.mozilla.org/show_bug.cgi?id=685445
        var selection = window.getSelection();
        if (!selection.isCollapsed || !selection.rangeCount) {
            return;
        }

        var curRange = selection.getRangeAt(selection.rangeCount - 1);
        if (curRange.commonAncestorContainer.nodeType == 3 && curRange.startOffset > 0) {
            // we are in child selection. The characters of the text node is being deleted
            return;
        }

        var range = document.createRange();
        if (selection.anchorNode != this) {
            // selection is in character mode. expand it to the whole editable field
            range.selectNodeContents(this);
            range.setEndBefore(selection.anchorNode);
        } else if (selection.anchorOffset > 0) {
            range.setEnd(this, selection.anchorOffset);
        } else {
            // reached the beginning of editable field
            return;
        }
        range.setStart(this, range.endOffset - 1);


        var previousNode = range.cloneContents().lastChild;
        if (previousNode && previousNode.contentEditable == 'false') {
            // this is some rich content, e.g. smile. We should help the user to delete it
            range.deleteContents();
            event.preventDefault();
        }
    }
});
*/



// désactive le backspace

/*
function keyhandler(e) {
    var key = e.keyCode
    if (key == 8)
    {
        var d = e.srcElement || e.target;
        if ($(d).hasClass("richTextEditor")) {
 $('#editfraction').remove();
        }
    }
}

*/



function convertHTMLRegisterHelper(description) {
    description = description.replace(/\n/g, '<br>');
    description = description.replace(/<L:/g, '<l:');
    description = description.replace(/<I:/g, '<i:');
    description = description.replace(/<O:/g, '<o:');
    description = description.replace(/<l:Art:/g, '<span class="label label-warning" data-tag="Art" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<l:Nom:/g, '<span class="label label-warning" data-tag="Nom" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<l:Par:/g, '<span class="label label-warning" data-tag="Par" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<l:Pos:/g, '<span class="label label-warning" data-tag="Pos" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<l:none:/g, '<span class="label label-warning" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<l:M#/g, '<span class="label label-success" data-tag="M#');
    description = description.replace(/<o:Art:/g, '<span class="label label-important" data-tag="Art" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<o:Nom:/g, '<span class="label label-important" data-tag="Nom" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<o:Par:/g, '<span class="label label-important" data-tag="Par" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<o:Pos:/g, '<span class="label label-important" data-tag="Pos" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<o:Cont:/g, '<span class="label label-important" data-tag="Cont" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<o:none:/g, '<span class="label label-important" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<i:Pre:/g, '<span class="label label-success" data-tag="Pre" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<i:inif:/g, '<span class="label label-success" data-tag="inif" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<i:Pat:/g, '<span class="label label-success" data-tag="Pat" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<i:Age:/g, '<span class="label label-success" data-tag="Age" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<i:none:/g, '<span class="label label-success" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<i:M#/g, '<span class="label label-success" data-tag="M#');
    description = description.replace(/<l:/g, '<span class="label label-warning" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<o:/g, '<span class="label label-important" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<i:/g, '<span class="label label-success" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/<u:/g, '<span class="label label-default" data-tag="none" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/:/g, '" contenteditable="false" data-toggle="popover" data-original-title="Choix balise" title="">');
    description = description.replace(/>/g, '</span>');
    return description;
}
