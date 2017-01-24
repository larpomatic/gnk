<style type="text/css">
.span6 {
    position: relative;
    bottom: 30px;
    right: 150px;
}
.span11
{
    width: auto;
    position: relative;
    left: 280px;
    margin-right: 100px;
}
.span13
{
    position: relative;
    left: 280px;
    margin-right: 70px;
}
select {
    font-size: 16px !important;
    width: 200px;
    outline: none;
}

.button_template
{
    position: relative;
    right: 250px;
}
.buttonDelete
{
    position : relative;
    left: 900px;
    width : 150px;
    padding-top:20px;
    padding-bottom: 60px;
}

</style>
<div class="fullScreenEditable" id="new_render">
    <g:hiddenField name="pitchDescription_0" class="pitchDescription" value="idDescription_0"/>
    <g:hiddenField name="titleDescription_0" class="pitchDescriptionTitle" value="Description"/>
    <div class="test">
        <a href="#" id="idDescription_0" data-type="text" data-pk="1" data-url="" data-title="Entrer le titre de la description" class="editable editable-click" onmouseover="edit(this)">Description</a>
        <span class="glyphicon glyphicon-heart" aria-hidden="true"></span>
    </div>
    <div class="row formRow">
        <div class="span11">
            <label for="pitchOrga">
                <g:message code="redactintrigue.generalDescription.pitchOrga" default="pitchOrga"/>
            </label>
        </div>

        <div class="span13">
            <g:checkBox class="pitchOrga" name="pitchOrga_0" id="idPitchOrga_0" onchange="update_modified()"/>
        </div>

        <div class="span11">
            <label for="pitchPj">
                <g:message code="redactintrigue.generalDescription.pitchPj" default="pitchPj"/>
            </label>
        </div>

        <div class="span13">
            <g:checkBox class="pitchPj" id="idPitchPj_0" name="pitchPj_0" onchange="update_modified()"/>
        </div>

        <div class="span11">
            <label for="pitchPnj">
                <g:message code="redactintrigue.generalDescription.pitchPnj" default="pitchPnj"/>
            </label>
        </div>

        <div class="span13">
            <g:checkBox class="pitchPnj" id="idPitchPnj_0" name="pitchPnj_0" onchange="update_modified()"/>
        </div>
        <div class="span6" id="type">
            <g:select class="desc_type" name='desc_type' id="idType_0" value="Contexte du GN" onchange="verifyType(this)"
                      from="${['Introduction', 'Contexte du GN', 'Univers du GN', 'Informations lues dans la presse récemment', 'Points de règles', 'Personnalités connues', 'Divers']}"/>
        </div>
    </div>
    <div class="button_template">
        <g:render template="dropdownButtons" />
    </div>
    <div id="idDescriptionText_0" contenteditable="true" class="text-left richTextEditor editable"  onblur="update_text(this)">
    </div>
    <g:hiddenField name="description_text" class="description_text" id="idDescriptionTextHide_0" value="Description"/>
    <div class="buttonDelete">
        <div type="button" class="btn btn-danger" onclick="deleteDescription(this)">Supprimer la description</div>
    </div>
</div>
<script type="text/javascript">
    var template = desc_number - 1;
    var template2 = desc_number;
    $(document).ready(function() {
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

    });
</script>