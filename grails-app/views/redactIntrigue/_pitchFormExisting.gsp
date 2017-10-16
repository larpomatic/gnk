<style type="text/css">
.span6 {
    position: relative;
    bottom: 30px;
    right: 150px;
}

.span11 {
    width: auto;
    position: relative;
    left: 280px;
    margin-right: 100px;
}

.span13 {
    position: relative;
    left: 280px;
    margin-right: 70px;
}

select {
    font-size: 16px !important;
    width: 200px;
    outline: none;
}

.button_template {
    position: relative;
    right: 250px;
}

.buttonDelete {
    position: relative;
    left: 900px;
    width: 150px;
    padding-top: 20px;
    padding-bottom: 60px;
}

</style>

<div class="fullScreenEditable" id="new_render">
    <g:hiddenField name="pitchDescription_${description.idDescription}" class="pitchDescription"
                   value="idDescription_${description.idDescription}"/>
    <g:hiddenField name="titleDescription_${description.idDescription}" class="pitchDescriptionTitle"
                   value="${description.title}"/>
    <div class="test">
        <a href="#" id="idDescription_${description.idDescription}" data-type="text" data-pk="1" data-url=""
           data-title="Entrer le titre de la description" class="editable editable-click"
           onmouseover="edit(this)">${description.title}</a>
        <span class="glyphicon glyphicon-heart" aria-hidden="true"></span>
    </div>

    <div class="row formRow">
        <div class="span11">
            <label for="pitchOrga">
                <g:message code="redactintrigue.generalDescription.pitchOrga" default="pitchOrga"/>
            </label>
        </div>

        <div class="span13">
            <g:checkBox class="pitchOrga" name="pitchOrga_${description.idDescription}"
                        id="idPitchOrga_${description.idDescription}" checked="${description.isOrga}"
                        onchange="update_modified()"/>
        </div>

        <div class="span11">
            <label for="pitchPj">
                <g:message code="redactintrigue.generalDescription.pitchPj" default="pitchPj"/>
            </label>
        </div>

        <div class="span13">
            <g:checkBox class="pitchPj" id="idPitchPj_${description.idDescription}"
                        name="pitchPj_${description.idDescription}" checked="${description.isPj}"
                        onchange="update_modified()"/>
        </div>

        <div class="span11">
            <label for="pitchPnj">
                <g:message code="redactintrigue.generalDescription.pitchPnj" default="pitchPnj"/>
            </label>
        </div>

        <div class="span13">
            <g:checkBox class="pitchPnj" id="idPitchPnj_${description.idDescription}"
                        name="pitchPnj_${description.idDescription}" checked="${description.isPnj}"
                        onchange="update_modified()"/>
        </div>

        <div class="span6" id="type">
            <g:select class="desc_type" name='desc_type' id="idType_${description.idDescription}"
                      value="${description.type}" onchange="verifyType(this)"
                      from="${['Introduction', 'Contexte du GN', 'Univers du GN', 'Informations lues dans la presse récemment', 'Points de règles', 'Personnalités connues', 'Divers']}"/>
        </div>
    </div>

    <div class="button_template">
        <g:render template="dropdownButtons"/>
    </div>
    <!-- Editor -->
<<<<<<< Updated upstream
   <div name="plotRichTextEditor" id="idPlotRichTextEditor_${description.idDescription}" value="${description.pitch}" contenteditable="true" class="text-left richTextEditor editable" onblur="saveCarretPos($(this).attr('id'))">
        ${description.pitch}
    </div>
    <div id="idDescriptionText_${description.idDescription}" contenteditable="true" class="text-left richTextEditor editable"  onblur="saveCarretPos($(this).attr('id'), this)">
=======
    <!--<div name="plotRichTextEditor" id="idPlotRichTextEditor_${description.idDescription}" value="${description.pitch}" contenteditable="true" class="text-left richTextEditor editable" onblur="saveCarretPos($(this).attr('id'))">
        ${description.pitch}
    </div>-->
    <div id="idDescriptionText_${description.idDescription}" contenteditable="true"
         class="text-left richTextEditor editable" onblur="saveCarretPos($(this).attr('id'), this)">
>>>>>>> Stashed changes
        ${description.pitch}
    </div>
    <g:hiddenField name="description_text" class="description_text"
                   id="idDescriptionTextHide_${description.idDescription}" value="${description.pitch}"/>
    <div class="buttonDelete">
        <div type="button" class="btn btn-danger" onclick="deleteDescription(this)">Supprimer la description</div>
    </div>
</div>