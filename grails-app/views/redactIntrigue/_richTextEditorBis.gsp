<script>
    //On set le content au chargement
//    window.onload = function () {
//        document.getElementById("richTextEditor").innerHTML = "Et c'est alors que&nbsp;<span class=\"label label-success\" contenteditable=\"false\">Han Solo</span>&nbsp;entra sur la&nbsp;<span class=\"label label-warning\" contenteditable=\"false\">Death Star</span>&nbsp;armé d'une&nbsp;<span class=\"label label-important\" contenteditable=\"false\">Pokéball</span>&nbsp;pour tuer&nbsp;<span class=\"label label-success\" contenteditable=\"false\">Dark Vador</span>."
//    } //FIXME
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
    $
</div>