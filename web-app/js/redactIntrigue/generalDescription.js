var nb_render = 1;
var clone;
var desc_number = 0;

//add a new description
function addDescription() {
    clone = $("#render_0").clone();
    desc_number = desc_number + 1;
    clone.attr('id', "render_"+ desc_number);
//        $('<div id="new_render2"/>').appendTo($('#new_render'));
    $('#desc_wrapper').append(clone);
    update_id(clone);
    $('#overview').append('<li class="list-group-item" id="titleRender_' + desc_number + '">Description</li>');
    ++nb_render;
    console.log("template value 2 : " + template);
}
function deleteDescription(elt){
    console.log(elt.parentElement.parentElement.parentElement);
    if (nb_render > 1) {
        elt.parentElement.parentElement.parentElement.remove();
        --nb_render;
    }
}

function update_id() {
    var render = desc_number;
    console.log("render value : " + render);
    document.getElementById('render_' + render).getElementsByClassName('pitchOrga')[0].id = "idPitchOrga_" + desc_number;
    document.getElementById('render_' + render).getElementsByClassName('pitchPj')[0].id = "idPitchPj_" + desc_number;
    document.getElementById('render_' + render).getElementsByClassName('pitchPnj')[0].id = "idPitchPnj_" + desc_number;
    document.getElementById('render_' + render).getElementsByClassName('text-left richTextEditor editable')[0].id = "idPlotRichTextEditor_" + desc_number;
    document.getElementById('render_' + render).getElementsByClassName('type')[0].id = "idType_" + desc_number;
    document.getElementById('render_' + render).getElementsByClassName('editable editable-click')[0].id = "idDescription_" + desc_number;
    //console.log(document.getElementById('render_3').getElementsByClassName('pitchOrga')[0].id );
}

