<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="substitution.title"/></title>
    <link type="text/css" rel="stylesheet" href="${resource(dir: 'css', file: 'substitution.css')}"/>
    <link type="text/css" rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-datetimepicker.min.css')}"/>
    <link type="text/css" rel="stylesheet" href="${resource(dir: 'css', file: 'dhtmlxcalendar.css')}"/>
</head>

<body>

<g:render template="../stepProgressBar"/>


<g:hiddenField name="gnId" value="${gnId}"/>


<div class="form-actions">
    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
    <g:message code="default.back.label" default="Back"/>
</g:link>
    <button id="publication" onclick="return publicationAccess()" class="btn btn-primary" action="index">
        ${message(code: 'navbar.publication', default: 'Publication')}</button>
</div>


<h1>Substitution</h1>

<g:render template="gnInformation"/>

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
            <g:render template="characters"/>
        </div>

        <div class="tab-pane" id="tab2">
            <g:render template="resources"/>
        </div>

        <div class="tab-pane" id="tab3">
            <g:render template="places" model="[placeList: placeList, gnId: gnId]"/>
        </div>

        <div class="tab-pane" id="tab4">
            <g:render template="dates"/>
        </div>
    </div>
</div>

<g:javascript src="substitution/sub.js"/>


<script type="text/javascript">
    function publicationAccess() {
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

            // Form creation and submit
            var form = $("<form>");
            form.attr({
                method: "POST",
                action: "${g.createLink(controller:'substitution', action:'validateSubstitution')}"
            });
            var inputJSON = $("<input>");
            inputJSON.attr({type: "hidden", name: "subJSON", value: JSON.stringify(subJSON)});
            form.append(inputJSON);
            $("body").append(form);
            form.submit();
            return true
        }
        else {
            addAlert("subAlertContainer", "alert alert-error", "Erreur",
                    "La substitution doit être complète pour être validée.")
            return false
        }
    }
</script>

</body>
</html>
