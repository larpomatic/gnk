<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <link href="${resource(dir: 'css', file: 'substitution.css')}" type="text/css" rel="stylesheet"/>
    <title><g:message code="substitution.title" /></title>

</head>
<body>

<!-- Hidden fields to store informations for GN-Constantes !-->
<!-- <input type="hidden" id="placeListWithoutConstantsId" name="placeListWithoutConstants" value="${placeListWithoutConstants}"> !-->
<div hidden id="placeListWithoutConstantsId" name="placeListWithoutConstants">
    <g:each id="loopPlaceWithoutConstantsList" status="i" in="${placeListWithoutConstants}" var="place"  >
        <div id="${place.id}"></div>
    </g:each>
</div>
<div hidden id="usedGnConstantsId" name="usedGnConstants">
    <g:each id="loopUsedConstantsList" status="i" in="${usedGnConstants}" var="gnConstant"  >
        <div id="${gnConstant}"></div>
    </g:each>
</div>
<!-- <input type="hidden" id="usedGnConstantsId" name="usedGnConstants" value="${usedGnConstants}"> !-->




<h1>Substitution</h1>


    <g:hiddenField name="gnId" value="${gnId}"/>
    <div class="form-actions">
    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right"><g:message code="default.back.label" default="Back"/></g:link>
    <button id="publication" onclick="return publicationAccess()" class="btn btn-primary" action="index">
    ${message(code: 'navbar.publication', default: 'Publication')}</button>
    </div>


<g:render template="gnInformation" />

<div class="row-fluid">
    <div class="span4"><h3 class="cap">Formulaire de Substitution</h3></div>
</div>

<div id="subAlertContainer">
</div>

<div class="tabbable" style="margin-bottom: 50px;">
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tab1" data-toggle="tab">Personnages</a></li>
        <li><a href="#tab2" data-toggle="tab">Ressources</a></li>
        <li><a href="#tab3" data-toggle="tab">Lieux</a></li>
        <li><a href="#tab4" data-toggle="tab">Dates</a></li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="tab1">
            <g:render template="characters" />
        </div>
        <div class="tab-pane" id="tab2">
            <g:render template="resources"/>
        </div>
        <div class="tab-pane" id="tab3">
            <g:render template="places" model="[placeList:placeList, gnId : gnId]"/>
        </div>
        <div class="tab-pane" id="tab4">
            <g:render template="dates" />
        </div>
    </div>
</div>

<g:javascript src="substitution/sub.js" />

<script type="text/javascript">



    function publicationAccess()
    {
            var isCharsReady = prepareCharsJSONForValidation(charsJSON);
            var isResourcesReady = prepareResourcesJSONForValidation(resourcesJSON);
            var isPlacesReady = preparePlacesJSONForValidation(placesJSON);
            var isDatesReady = prepareDatesJSONForValidation(datesJSON);
        if (isCharsReady && isResourcesReady && isPlacesReady && isDatesReady) {
            // SubJSON construction
            var subJSON = new Object();
            subJSON.gnDbId = ${gnInfo.dbId};
            subJSON.subCharacter = charsJSON.characters;
            subJSON.subResource = resourcesJSON.resources;
            subJSON.subPlace = placesJSON.places;
            subJSON.subDate = datesJSON;

            var placeListWithoutConstantsVar = document.getElementById('placeListWithoutConstantsId');
            var placeLWC_Elems = placeListWithoutConstantsVar.childNodes;
            var placeLWC_Array = new Array();
            for (var i=0; i < placeLWC_Elems.length; i++) {
                if (placeLWC_Elems[i].nodeName && placeLWC_Elems[i].nodeName.toLowerCase() === 'div' ) {
                    placeLWC_Array.push(placeLWC_Elems[i].id);
                }
            }

            var placeUsedGnConstantsVar = document.getElementById('usedGnConstantsId');
            var placeUGC_Elems = placeUsedGnConstantsVar.childNodes;
            var placeUGC_Array = new Array();
            for (var i=0; i < placeUGC_Elems.length; i++) {
                if (placeUGC_Elems[i].nodeName && placeUGC_Elems[i].nodeName.toLowerCase() === 'div' ) {
                    placeUGC_Array.push(placeUGC_Elems[i].id);
                }
            }

            subJSON.placeListWithoutConstants = placeLWC_Array;
            subJSON.usedGnConstants = placeUGC_Array;

            // Form creation and submit
            var form = $("<form>");
            form.attr({method: "POST", action: "${g.createLink(controller:'substitution', action:'validateSubstitution')}"});
            var inputJSON = $("<input>");
            inputJSON.attr({type: "hidden", name: "subJSON", value: JSON.stringify(subJSON)});
            form.append(inputJSON);
            $("body").append(form);
            form.submit();
            return true
        }
        else
        {
            addAlert("subAlertContainer", "alert alert-error", "Erreur",
                    "La substitution doit être complète pour être validée.")
            return false
        }
    }
</script>

</body>
</html>
