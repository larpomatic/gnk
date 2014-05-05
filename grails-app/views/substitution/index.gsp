<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <link href="${resource(dir: 'css', file: 'substitution.css')}" type="text/css" rel="stylesheet"/>
    <title><g:message code="substitution.title" /></title>

</head>
<body>
<h1>Substitution</h1>

<g:render template="gnInformation" />

<div class="row-fluid">
    <div class="span4"><h3 class="cap">Formulaire de Substitution</h3></div>
    <div class="span2"><a id="validateSubButton" class="btn"><i class="icon-ok-sign"></i> VALIDER</a></div>
    <div class="span3">
    <form id="exportPersoCSVSubButton" action="${g.createLink(controller:'publication', action:'publicationCSV')}" method="POST">
        <input type="hidden" value="${gnInfo.dbId}" name="gnId"/>
        <input type="hidden" value="personnage" name="csvType" />
        <button id="" class="btn" type="submit" style="visibility: visible"><i class="icon-ok-sign"></i> EXPORTER PERSONNAGE.CSV</button>
    </form>
    </div>
    <div class="span3">
    <form id="exportJoueurCSVSubButton" action="${g.createLink(controller:'publication', action:'publicationCSV')}" method="POST">
        <input type="hidden" value="${gnInfo.dbId}" name="gnId"/>
        <input type="hidden" value="joueur" name="csvType" />
        <button id="" class="btn" type="submit" style="visibility: visible"><i class="icon-ok-sign"></i> EXPORTER JOUEUR.CSV</button>
    </form>
    </div>
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
            <g:render template="places" />
        </div>
        <div class="tab-pane" id="tab4">
            <g:render template="dates" />
        </div>
    </div>
</div>

<g:javascript src="substitution/sub.js" />

<script type="text/javascript">
    $(document).ready(function() {
        // Export CSV
        $("#exportPersoCSVSubButton").submit(function(){
            return true;
            var isCharsReady = prepareCharsJSONForValidation(charsJSON);
            var isResourcesReady = prepareResourcesJSONForValidation(resourcesJSON);
            var isPlacesReady = preparePlacesJSONForValidation(placesJSON);
            var isDatesReady = prepareDatesJSONForValidation(datesJSON);
            if (isCharsReady && isResourcesReady && isPlacesReady && isDatesReady) {
                return true;
            } else {
                addAlert("subAlertContainer", "alert alert-error", "Erreur",
                        "La substitution doit être complète pour être validée.");
                return false;
            }
        });
        $("#exportJoueurCSVSubButton").submit(function(){

            var isCharsReady = prepareCharsJSONForValidation(charsJSON);
            var isResourcesReady = prepareResourcesJSONForValidation(resourcesJSON);
            var isPlacesReady = preparePlacesJSONForValidation(placesJSON);
            var isDatesReady = prepareDatesJSONForValidation(datesJSON);
            if (isCharsReady && isResourcesReady && isPlacesReady && isDatesReady) {
                return true;
            } else {
                addAlert("subAlertContainer", "alert alert-error", "Erreur",
                        "La substitution doit être complète pour être validée.");
                return false;
            }
        });

        // Validate substitution
        $("#validateSubButton").click( function(){
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
                form.attr({method: "POST", action: "${g.createLink(controller:'substitution', action:'validateSubstitution')}"});
                var inputJSON = $("<input>");
                inputJSON.attr({type: "hidden", name: "subJSON", value: JSON.stringify(subJSON)});
                form.append(inputJSON);
                $("body").append(form);
                form.submit();
            }
            else {
                addAlert("subAlertContainer", "alert alert-error", "Erreur",
                        "La substitution doit être complète pour être validée.")
            }
        });
    });
</script>

</body>
</html>
