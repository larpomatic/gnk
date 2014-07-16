$(function(){
    initConfirm();

    initDeleteButton();

    initSearchBoxes();

    // ajout du nombre de relation
    $('.numberRelation').html($('.relationScreen .accordion-group').size());

    // charge les datetimepickers
    $('.datetimepicker').datetimepicker({
        language: 'fr',
        pickSeconds: false
    });

    // empeche les dropdown de se fermer lorsqu'on focus un input
    $('.dropdown-menu input, .dropdown-menu label').click(function(e) {
        e.stopPropagation();
    });

    //permet d'ajouter rapidement une ressource, lieu ou role
    $(".leftMenuList input, .inputOther").keypress(function(e) {
        if(e.which == 13) {
            var entity = $(this).attr("data-entity");
            var badgeWarning = '<i class="icon-warning-sign"></i>';
            appendEntity(entity, $(this).val(), $(this).attr("data-label"), badgeWarning);
            $(this).val('');
            return false;
        }
    });

    initializeTextEditor();

    // on ajoute la description d'un plot dans le champ hidden correspondant
    $('.savePlotForm').submit(function() {
        var description = $('#plotRichTextEditor', this).html();
        description = transformDescription(description);
        $('.descriptionContent', this).val(description);
        var pitchOrga = $('#plotRichTextEditorPitchOrga', this).html();
        pitchOrga = transformDescription(pitchOrga);
        $('.pitchOrgaContent', this).val(pitchOrga);
        var pitchPj = $('#plotRichTextEditorPitchPj', this).html();
        pitchPj = transformDescription(pitchPj);
        $('.pitchPjContent', this).val(pitchPj);
        var pitchPnj = $('#plotRichTextEditorPitchPnj', this).html();
        pitchPnj = transformDescription(pitchPnj);
        $('.pitchPnjContent', this).val(pitchPnj);
    });

    // mode plein ecran
    $('.btnFullScreen').click(function() {
        $(this).parent().parent().toggleClass("fullScreenOpen");
    });

    bgenScroll();

    initModifyTag();
});

//active le bouton "modifier" des fenêtres de tag
function initModifyTag() {
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
//    setTimeout(function() {
//        if (location.hash) {
//            window.scrollTo(0, 0);
//        }
//    }, 100);
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
        description = description.replace(/\n/g, '<br>');
        description = description.replace(/&lt;l:/g, '<span class="label label-warning" contenteditable="false">');
        description = description.replace(/&lt;o:/g, '<span class="label label-important" contenteditable="false">');
        description = description.replace(/&lt;i:/g, '<span class="label label-success" contenteditable="false">');
        description = description.replace(/&gt;/g, '</span>');
        $(this).html(description);
    });
}

// on remplace les span html dans une description par des balises
function transformDescription(description) {
    description = description.replace(/<div>/g, '\n');
    description = description.replace(/<\/div>/g, '\n');
    description = description.replace(/<br>/g, '\n');
    description = description.replace(/<span class="label label-warning" contenteditable="false">/g, '<l:');
    description = description.replace(/<span class="label label-important" contenteditable="false">/g, '<o:');
    description = description.replace(/<span class="label label-success" contenteditable="false">/g, '<i:');
    description = description.replace(/<\/span>/g, '>');
    description = description.replace(/&nbsp;/g, ' ');
    description = description.replace(/&lt;l:/g, '<l:');
    description = description.replace(/&lt;o:/g, '<o:');
    description = description.replace(/&lt;i:/g, '<i:');
    description = description.replace(/&gt;/g, '>');
    return description;
}