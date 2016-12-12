var nb_render = 1;
var clone;
var desc_number = 0;
var introduction = {
   bool: false,
   desc_id : -1
};

//add a new description
$(document).ready(function() {
    var j = document.getElementById('render_' + desc_number);
    while (j != null) {
        console.log(j);
        if (j.getElementsByClassName('desc_type')[0].value == "Introduction")
        {
            introduction.bool = true;
            introduction.desc_id = desc_number;
        }
        desc_number++;
        nb_render++;
        j = document.getElementById('render_' + desc_number);
        console.log(desc_number);
    }
});


function addDescription() {
    clone = $("#render_0").clone();
    clone.attr('id', "render_"+ desc_number);
//        $('<div id="new_render2"/>').appendTo($('#new_render'));
    $('#desc_wrapper').append(clone);
    initialise_description(desc_number);
    update_id(desc_number, desc_number);
    $('#overview').append('<li class="list-group-item" id="titleRender_' + desc_number + '">Description</li>');
    desc_number = desc_number + 1;
    ++nb_render;
    console.log("template value 2 : " + template);
}

function deleteDescription(elt){
    console.log("id du parent : " + elt.parentElement.parentElement.parentElement.id);
    if (nb_render > 1) {
        var new_desc = desc_number - 1;
        console.log("new_desc : " + new_desc);
        var pred_desc;
        var id_parent = elt.parentElement.parentElement.parentElement.id.split('_');
        elt.parentElement.parentElement.parentElement.remove();
        while (new_desc > id_parent[1])
        {
            pred_desc = new_desc - 1;
            update_id(new_desc, pred_desc);
            update_introduction(new_desc, id_parent[1]);
            document.getElementById('render_' + new_desc).id = "render_" + pred_desc;
            new_desc--;
        }
        --nb_render;
        --desc_number;
    }
    update_modified();
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
    document.getElementById('render_' + render).getElementsByClassName('desc_type')[0].id = "idType_" + description;
    document.getElementById('render_' + render).getElementsByClassName('editable editable-click')[0].id = "idDescription_" + description;
    console.log("pitch Description : " + document.getElementById('render_' + render).getElementsByClassName('pitchDescription')[0].name);
}

function initialise_description(render)
{
    document.getElementById('render_' + render).getElementsByClassName('pitchOrga')[0].setAttribute('value', "false");
    document.getElementById('render_' + render).getElementsByClassName('pitchPj')[0].setAttribute('value', false);
    document.getElementById('render_' + render).getElementsByClassName('pitchPnj')[0].bool = false;
    console.log("initialisation de la description");
    document.getElementById('render_' + render).getElementsByClassName('text-left richTextEditor editable')[0].setAttribute('value', "Entrez votre description ici");
    document.getElementById('render_' + render).getElementsByClassName('desc_type')[0].setAttribute('value', "contexte du GN");

}

function update_introduction(render, desc_remove){
    id_introduction = document.getElementById('render_' + render).getElementsByClassName('desc_type')[0].id;
    if (id_introduction > introduction.desc_id)
        introduction.desc_id--;
    if (desc_remove == introduction.desc_id)
    {
        introduction.desc_id = -1;
        introduction.bool = false;
    }
}

function updateIntrigue()
{
    console.log("Update intrigue");
    $("#savePlotForm").submit();
}

function reset_ismodified()
{
    is_modified = false;
}

