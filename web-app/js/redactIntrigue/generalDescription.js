var nb_render = 0;
var clone;
var desc_number = 0;
var introduction = {
   bool: false,
   desc_id : -1
};

//Charge les descriptions de l'intrigue
$(document).ready(function() {
    var j = document.getElementById('render_' + desc_number);
    if (j != null)
        $('.list-group-item').remove();
    while (j != null) {
        if (j.getElementsByClassName('desc_type')[0].value == "Introduction")
        {
            introduction.bool = true;
            introduction.desc_id = desc_number;
        }
        $('#overview').append('<li class="list-group-item" id="titleRender_' + desc_number + '"> <a id="refDesc_' + desc_number + '"href="#idDescription_' + desc_number + '">' +  document.getElementById('render_' + desc_number).getElementsByClassName('pitchDescriptionTitle')[0].value + '</a> </li>')
        desc_number++;
        nb_render++;
        j = document.getElementById('render_' + desc_number);
    }
});

//ajoute une description
function addDescription() {
    clone = $("#render_0").clone();
    clone.attr('id', "render_"+ desc_number);
    console.log(clone.find('.switch'));//.switch en paramètre

    $('#desc_wrapper').append(clone);
    initialise_description(desc_number);
    $('#overview').append('<li class="list-group-item" id="titleRender_' + desc_number + '"> <a id="refDesc_' + desc_number + '"href="#idDescription_' + desc_number + '">Description </a> </li>');
    update_id(desc_number, desc_number);
    desc_number = desc_number + 1;
    ++nb_render;
}

//Supprime une description
function deleteDescription(elt){
    if (nb_render > 1) {
        var new_desc = desc_number - 1;
        var pred_desc;
        var id_parent = elt.parentElement.parentElement.parentElement.id.split('_');
        elt.parentElement.parentElement.parentElement.remove();
        document.getElementById('titleRender_' + id_parent[1]).remove();
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
    else
        createNotification("danger", "Impossible de supprimer cette description", "Vous devez avoir au moins une description dans votre intrigue");
    update_modified();
}

//Modifie l'id des différents champs du formulaire de description pour distinguer les valeurs récupérées dans le controlleur
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
    document.getElementById('render_' + render).getElementsByClassName('pitchDescriptionTitle')[0].setAttribute('name', "titleDescription_" + description);
    document.getElementById('titleRender_' + render).id = "titleRender_" + description;
    document.getElementById('refDesc_' + render).href = "#idDescription_" + description;
    document.getElementById('refDesc_' + render).id = "refDesc_" + description;

}

//Lors de l'ajout d'une description, réinitialise les paramètres
function initialise_description(render)
{
    document.getElementById('render_' + render).getElementsByClassName('text-left richTextEditor editable')[0].textContent = "";
    document.getElementById('render_' + render).getElementsByClassName('description_text')[0].textContent = "";
    document.getElementById('render_' + render).getElementsByClassName('editable editable-click')[0].textContent = "Description";
    document.getElementById('render_' + render).getElementsByClassName('desc_type')[0].value = "Contexte du GN";
}

//Vérifie si le type introduction est déja utilisé dans les descriptions chargées
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


