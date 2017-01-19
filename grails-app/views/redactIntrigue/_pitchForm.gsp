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
    <div class="test">
        <a href="#" id="idDescription_0" data-type="text" data-pk="1" data-url="" data-title="Entrer le titre de la description" class="editable editable-click">Description</a>
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
        <g:textArea name="description_text" id="idDescriptionText_0" class="text-left richTextEditor editable" value="Insérer votre description" onchange="update_modified()"/>
    <div class="buttonDelete">
        <div type="button" class="btn btn-danger" onclick="deleteDescription(this)">Supprimer la description</div>
    </div>
</div>
<script type="text/javascript">
    var template = desc_number;
    $(document).ready(function() {
        console.log("template value : idDescription_" + template);
        //console.log("desc_number value : idDescription_" + desc_number);
        $('#idDescription_' + template).editable({
            success: function(response, newValue) {
                updateName(newValue);
            }
        });
    });

    function updateName(elt){
        console.log("template value : " + template);
        //$("#titleRender_" + template).setAttribute('value', elt);
        document.getElementById('titleRender_' + template).setAttribute('value', elt);
        //var new_elt = '<li class="list-group-item" id="titleRender_' + template + '">' + elt + '</li>';
        //$("#titleRender_").id = "titleRender" + template;
        //$('#overview').append(new_elt);
        //console.log($('#description_0').editable('getValue', true));
    }
</script>