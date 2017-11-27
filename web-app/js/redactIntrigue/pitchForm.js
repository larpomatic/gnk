
//Mettre à jour le titre de la description
function edit(elt)
{
    var id_elt = elt.getAttribute('id');
    $("#"+ id_elt).editable({
        success: function (response, newValue) {
            updateName(newValue, elt);
        }
    });
}

//Le sommaire est mis à jour en fonction de la valeur de la description
function updateName(newValue, elt){
    var id_render = elt.id.split('_');
    document.getElementById('refDesc_' + id_render[1]).textContent =  newValue;
    document.getElementById('render_' + id_render[1]).getElementsByClassName('pitchDescriptionTitle')[0].setAttribute('value', newValue);
}

//Vérifie que le type "Introduction" n'est selectionné qu'une seule fois
function verifyType(elt)
{
    var id_introduction = elt.id.split('_');
    if (id_introduction[1] == introduction.desc_id) {
        if (elt.value != "Introduction") {
            introduction.desc_id = -1;
            introduction.bool = false;
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
                    introduction.bool = true;
                    introduction.desc_id = id_introduction[1];
                }
            }
    }
    update_modified();
}

//Associe le ritchTexteEditor de la description avec une hiddenvalue pour récupérer la description en back
function update_text(elt)
{
    console.log(elt.textContent);
    var zo = elt.innerHTML;
    //var description = transformDescription(elt); // element a convertir
    var description = transformDescription(zo);
    var id_render = elt.id.split('_');
    document.getElementById('render_' + id_render[1]).getElementsByClassName('description_text')[0].setAttribute('value', description);
    console.log((document.getElementById('render_' + id_render[1]).getElementsByClassName('description_text')[0]).value);

    update_modified();
}

function update_modified()
{
    is_modified = true;
}
