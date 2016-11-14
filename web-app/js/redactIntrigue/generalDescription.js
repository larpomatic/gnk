var nb_render = 1;
var clone;
var desc_number = 0;

//add a new description
$(document).ready(function() {
    var j = document.getElementById('render_' + nb_render);
    while (j != null) {
        console.log(j);
        desc_number++;
        nb_render++;
        j = document.getElementById('render_' + nb_render);
    }
});


function addDescription() {
    clone = $("#render_0").clone();
    desc_number = desc_number + 1;
    clone.attr('id', "render_"+ desc_number);
//        $('<div id="new_render2"/>').appendTo($('#new_render'));
    $('#desc_wrapper').append(clone);
    update_id(desc_number, desc_number);
    $('#overview').append('<li class="list-group-item" id="titleRender_' + desc_number + '">Description</li>');
    ++nb_render;
    console.log("template value 2 : " + template);
}
function deleteDescription(elt){
    console.log("id du parent : " + elt.parentElement.parentElement.parentElement.id);
    if (nb_render > 1) {
        var new_desc = desc_number;
        var pred_desc;
        var id_parent = elt.parentElement.parentElement.parentElement.id.split('_');
        console.log("Id : " + id_parent[1]);
        elt.parentElement.parentElement.parentElement.remove();
        while (new_desc > id_parent[1])
        {
            pred_desc = new_desc - 1;
            update_id(new_desc, pred_desc);
            document.getElementById('render_' + new_desc).id = "render_" + pred_desc;
            new_desc--;
        }
        --nb_render;
        --desc_number;
    }
}

function update_id(render, description) {
    document.getElementById('render_' + render).getElementsByClassName('pitchOrga')[0].id = "idPitchOrga_" + description;
    document.getElementById('render_' + render).getElementsByClassName('pitchPj')[0].id = "idPitchPj_" + description;
    document.getElementById('render_' + render).getElementsByClassName('pitchPnj')[0].id = "idPitchPnj_" + description;
    document.getElementById('render_' + render).getElementsByClassName('pitchOrga')[0].setAttribute('name', "pitchOrga_" + description);
    document.getElementById('render_' + render).getElementsByClassName('pitchPj')[0].setAttribute('name', "pitchPj_" + description);
    document.getElementById('render_' + render).getElementsByClassName('pitchPnj')[0].setAttribute('name', "pitchPnj_" + description);
    document.getElementById('render_' + render).getElementsByClassName('pitchDescription')[0].setAttribute('value', "idDescription_" + description);
    document.getElementById('render_' + render).getElementsByClassName('pitchDescription')[0].setAttribute('name', "pitchDescription_" + description);
    document.getElementById('render_' + render).getElementsByClassName('text-left richTextEditor editable')[0].id = "idDescriptionText_" + description;
    document.getElementById('render_' + render).getElementsByClassName('type')[0].id = "idType_" + description;
    document.getElementById('render_' + render).getElementsByClassName('editable editable-click')[0].id = "idDescription_" + description;
    console.log("pitch Description : " + document.getElementById('render_' + render).getElementsByClassName('pitchDescription')[0].name);
}

