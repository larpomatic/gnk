<script>

    //On set le content au chargement
    window.onload = function () {
        document.getElementById("richTextEditor").innerHTML = "Et c'est alors que&nbsp;<span class=\"label label-success\" contenteditable=\"false\">Han Solo</span>&nbsp;entra sur la&nbsp;<span class=\"label label-warning\" contenteditable=\"false\">Death Star</span>&nbsp;armé d'une&nbsp;<span class=\"label label-important\" contenteditable=\"false\">Pokéball</span>&nbsp;pour tuer&nbsp;<span class=\"label label-success\" contenteditable=\"false\">Dark Vador</span>."
    } //FIXME

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
    function saveCarretPos() {
        var caretOffset = 0;
        var element = document.getElementById("richTextEditor");
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
</script>
<!-- navbar des différentes catégories d'objets insérables -->
<div class="btn-group">
    <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
        Personnage <span class="caret"></span>
    </button>
    <ul class="dropdown-menu roleSelector">
        <g:each in="${plotInstance.roles}" status="i5" var="role">
            <li data-id="${role.id}">
                <a onclick="setCarretPos(); pasteHtmlAtCaret('<span class=&quot;label label-success&quot; contenteditable=&quot;false&quot;>${role.code}</span>'); return false;">
                    ${role.code}
                </a>
            </li>
        </g:each>
    </ul>
</div>

<div class="btn-group">
    <button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">
        Lieu <span class="caret"></span>
    </button>
    <ul class="dropdown-menu placeSelector">
        <li><a onclick="setCarretPos();
        pasteHtmlAtCaret('<span class=&quot;label label-warning&quot; contenteditable=&quot;false&quot;>Death Star</span>');
        return false;">Death Star</a></li>
        <li><a onclick="setCarretPos();
        pasteHtmlAtCaret('<span class=&quot;label label-warning&quot; contenteditable=&quot;false&quot;>Coruscant</span>');
        return false;">Coruscant</a></li>
        <li><a onclick="setCarretPos();
        pasteHtmlAtCaret('<span class=&quot;label label-warning&quot; contenteditable=&quot;false&quot;>Hoth</span>');
        return false;">Hoth</a></li>
    </ul>
</div>

<div class="btn-group">
    <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">
        Objet <span class="caret"></span>
    </button>
    <ul class="dropdown-menu resourceSelector">
        <li><a onclick="setCarretPos();
        pasteHtmlAtCaret('<span class=&quot;label label-important&quot; contenteditable=&quot;false&quot;>Light Saber</span>');
        return false;">Light Saber</a></li>
        <li><a onclick="setCarretPos();
        pasteHtmlAtCaret('<span class=&quot;label label-important&quot; contenteditable=&quot;false&quot;>Pokéball</span>');
        return false;">Pokéball</a></li>
        <li><a onclick="setCarretPos();
        pasteHtmlAtCaret('<span class=&quot;label label-important&quot; contenteditable=&quot;false&quot;>X-Wing</span>');
        return false;">X-Wing</a></li>
    </ul>
</div>

<!-- Editor -->
<div id="richTextEditor" contenteditable="true" class="text-left" onblur="saveCarretPos()"
     style="margin-top:15px; padding:5px; height:200px; overflow:auto; border:solid 1px #808080; -moz-border-radius:20px 0;
     -webkit-border-radius:20px 0; border-radius:20px 0; margin-bottom: 10px;">
    Entrez votre description
</div>