<style type="text/css">
.span6 {
    position: relative;
    bottom: 30px;
    right: 200px;
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
    width: 150px;
    outline: none;
}

.render
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
.buttonAdd {
    position: relative;
    left: 500px;
    width: 150px;
    padding-top: 20px;
    padding-bottom: 60px;
}
.span14{
    text-align: left;
    float: left;
    margin-right: 1000px;
}
</style>
<div class="">
    <h3>Contexte</h3>
</div>
<div class="fullScreenEditable">
    <div class="row formRow">
        <div class="span11">
        <label for="pitchOrga">
<g:message code="redactintrigue.generalDescription.pitchOrga" default="pitchOrga"/>
</label>
</div>

<div class="span13">
    <g:checkBox name="pitchOrga" id="pitchOrga" checked="${plotInstance.isMainstream}"/>
</div>

<div class="span11">
    <label for="pitchPj">
        <g:message code="redactintrigue.generalDescription.pitchPj" default="pitchPj"/>
    </label>
</div>

<div class="span13">
    <g:checkBox name="pitchPj" id="pitchPj" checked="${plotInstance.isEvenemential}"/>
</div>

<div class="span11">
    <label for="pitchPnj">
        <g:message code="redactintrigue.generalDescription.pitchPnj" default="pitchPnj"/>
    </label>
</div>

<div class="span13">
    <g:checkBox id="pitchPnj" name="pitchPnj" checked="${plotInstance.isDraft}"/>
</div>
<div class="span6" id="descriptionType">
    <g:select name='type' id="type" noSelection="['':'-Choose a type-']"
              from="${['contexte du GN', 'univers du GN', 'informations lues dans la presse récemment', 'points de règles', 'personnalités connues', 'divers']}"/>
</div>
</div>
<div class="render">
    <g:render template="dropdownButtons" />
</div>
<!-- Editor -->
<div id="plotRichTextEditor" contenteditable="true" class="text-left richTextEditor editable" onblur="saveCarretPos($(this).attr('id'))">
    ${plotInstance.description?.encodeAsHTML()}
</div>
    <div class="buttonDelete">
        <div type="button" class="btn btn-danger">Supprimer la description</div>
    </div>
</div>