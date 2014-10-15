
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="navbar.publication" /></title>
</head>
<body>

<br><br>
<div class="row-fluid">
    <div class="span3">
        <form id="exportPersoCSVSubButton" action="${g.createLink(controller:'publication', action:'publication')}" method="POST">
            <input type="hidden" value="${gnId}" name="gnId"/>
            <input id="templateWordSelect" type="hidden" value="${universName}" name="templateWordSelect"/>
            <button class="btn" type="submit" style="visibility: visible"><i class="icon-ok-sign"></i> Exporter en fichier Word</button>
        </form>
    </div>

    Template Word :
    <select class="bold" isempty="false" onchange="document.getElementById('templateWordSelect').value = this.value">
        <g:each in="${templateWordList}" var="templateWord">
            <g:if test="${universName == templateWord}">
                <option value="${templateWord}" selected>${templateWord}</option>
            </g:if>
            <g:else>
                <option value="${templateWord}">${templateWord}</option>
            </g:else>
        </g:each>
    </select>

</div>
<div class="row-fluid">
    <div class="span3">
        <form id="exportPersoCSVSubButton" action="${g.createLink(controller:'publication', action:'publicationCSV')}" method="POST">
            <input type="hidden" value="${gnId}" name="gnId"/>
            <input type="hidden" value="personnage" name="csvType" />
            <button class="btn" type="submit" style="visibility: visible"><i class="icon-ok-sign"></i> Exporter Personnage.CSV</button>
        </form>
    </div>
    <div class="span3">
        <form id="exportJoueurCSVSubButton" action="${g.createLink(controller:'publication', action:'publicationCSV')}" method="POST">
            <input type="hidden" value="${gnId}" name="gnId"/>
            <input type="hidden" value="joueur" name="csvType" />
            <button class="btn" type="submit" style="visibility: visible"><i class="icon-ok-sign"></i> Exporter Joueur.CSV</button>
        </form>
    </div>
    <button class="btn" disabled><i class="icon-film"></i> Afficher Simulation</button>
</div>





<h1><u>${title}</u></h1>
<i>${subtitle}</i>
<h2>Synthèse pour les organisateurs : </h2>


<h3>Synthèse des pitchs des Intrigues du GN</h3>
${GNinfo1}<br>${GNinfo2}<br>${msgCharacters}
<div hidden="true">${mybool = 'true'}</div>
<g:each in="${pitchOrgaList}" var="pitchOrga">
    <g:if test="${mybool == 'true'}">
        <h4>${pitchOrga}</h4>
        <div hidden="true">${mybool = 'false'}</div>
    </g:if>
    <g:else>
        ${pitchOrga}
        <div hidden="true">${mybool = 'true'}</div>
    </g:else>
</g:each>


<h3>Synthèse des personnages du GN</h3>
<TABLE BORDER="1" CELLPADDING="10">
    <TR>
        <TH> NOM - Prenom </TH>
        <TH> Nb PIP Total </TH>
        <TH> Type </TH>
        <TH> Sexe </TH>
        <TH> Age </TH>
    </TR>
    <g:each in="${charactersList}" var="c">
        <TR>
            <TH> ${c.lastname} ${c.firstname} </TH>
            <TD> ${c.nbPIP} </TD>
            <TD> ${c.type} </TD>
            <TD> ${c.gender} </TD>
            <TD> ${c.getAge()} </TD>
        </TR>
    </g:each>
</TABLE>




<script type="text/javascript">
    $(document).ready(function() {
        // Export CSV
        $("#exportPersoCSVSubButton").submit(function(){
                return true;
        });
        $("#exportJoueurCSVSubButton").submit(function(){
                return true;
        });

        // Validate substitution
        $("#validateSubButton").click( function(){
                var subJSON = new Object();
                subJSON.gnDbId = ${gnId};
                subJSON.subCharacter = charsJSON.characters;
                subJSON.subResource = resourcesJSON.resources;
                subJSON.subPlace = placesJSON.places;
                subJSON.subDate = datesJSON;
                subJSON.templateWord = ${templateWordSelect}

                // Form creation and submit
                var form = $("<form>");
                form.attr({method: "POST", action: "${g.createLink(controller:'substitution', action:'validateSubstitution')}"});
                var inputJSON = $("<input>");
                inputJSON.attr({type: "hidden", name: "subJSON", value: JSON.stringify(subJSON)});
                inputJSON.attr({type : "hidden", name: "templateWordSelected", value: getSelectValue('templateWordSelect')})
                form.append(inputJSON);
                $("body").append(form);
                form.submit();
        });
    });
</script>



</body>
</html>