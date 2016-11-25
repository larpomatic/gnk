var template = desc_number - 1;
$(document).ready(function() {
    console.log("template value : idDescription_" + template);
    $('#idDescription_' + template).editable({
        success: function(response, newValue) {
            updateName(newValue);
        }
    });
    //activate_update();
});

function updateName(elt){
    console.log("template value : " + template);
    $("#titleRender_" + template).remove();
    var new_elt = '<li class="list-group-item" id="titleRender_' + template + '">' + elt + '</li>';
    //$("#titleRender_").id = "titleRender" + template;
    $('#overview').append(new_elt);
    //console.log($('#description_0').editable('getValue', true));
}

function updatePlotPitchOrga(elt)
{
    console.log("test");
}

function verifyType(elt)
{
    var id_introduction = elt.id.split('_');
    console.log("value : " + introduction.desc_id);
    if (id_introduction[1] == introduction.desc_id) {
        if (elt.value != "Introduction") {
            introduction.desc_id = -1;
            introduction.bool = false;
            console.log("Pas Introduction ici")
        }
    }
    else {
            if (elt.value == "Introduction")
            {
                if (introduction.bool) {
                    createNotification("danger", "Impossible de sélectionner ce type", "Le type introduction ne peut être sélectionné que pour une seule description");
                    elt.value = "Contexte du GN";
                }
                else {
                    console.log('Introduction ici et en plus true');
                    introduction.bool = true;
                    introduction.desc_id = id_introduction[1];
                }
            }
    }
    console.log("VerifyType : " + introduction.bool);
    //activate_update();
}

function activate_update()
{
    if (introduction.bool)
        document.getElementById('update').isDisabled = false;
    else
        document.getElementById('update').isDisabled = true;
}
