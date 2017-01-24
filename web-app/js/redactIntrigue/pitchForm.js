var template = desc_number - 1;
var template2 = desc_number;
$(document).ready(function() {
    console.log("template value : idDescription_" + template);
    console.log("template value 2 : idDescription_" + template2);
    console.log("nbrender value : " + nb_render);
    console.log("desc_number value : " + desc_number);
});
function edit(elt)
{
    var id_elt = elt.getAttribute('id');
    console.log("Edit : " + id_elt);
    $("#"+ id_elt).editable({
        success: function (response, newValue) {
            console.log("it's work");
            updateName(newValue, elt);
        }
    });
    activate_update();
}

function updateName(newValue, elt){
    console.log("template value : " + template);
    var id_render = elt.id.split('_');
    console.log(document.getElementById('refDesc_' + id_render[1]).textContent);
    document.getElementById('refDesc_' + id_render[1]).textContent =  newValue;
    document.getElementById('render_' + id_render[1]).getElementsByClassName('pitchDescriptionTitle')[0].setAttribute('value', newValue);
    //$("#titleRender_" + template).remove();
    //var new_elt = '<li class="list-group-item" id="titleRender_' + template + '">' + newValue + '</li>';
    //$("#titleRender_").id = "titleRender" + template;
    //$('#overview').append(new_elt);
    //console.log($('#description_0').editable('getValue', true));
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
    update_modified();
}

function activate_update()
{
    if (introduction.bool)
        document.getElementById('update').isDisabled = false;
    else
        document.getElementById('update').isDisabled = true;
}

function update_modified()
{
    is_modified = true;
}