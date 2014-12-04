
<!DOCTYPE html>
<html>
<head>
    <g:javascript library='jquery' />
    <meta name="layout" content="main">
    <title><g:message code="navbar.publication" /></title>
</head>
<body>

<br><br>
<g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right"><g:message code="default.back.label" default="Back"/></g:link>
<div class="row-fluid" id="hidTest">
    <div class="span4">
        <form id="exportWordButton" action="${g.createLink(controller:'publication', action:'publication')}" method="POST">
            <input type="hidden" value="${gnId}" name="gnId"/>
            <input id="templateWordSelect" type="hidden" value="${universName}" name="templateWordSelect"/>
            <input id="imgsrc" type="hidden" value="" name="imgsrc"/>
            <button class="btn" type="submit" style="visibility: visible"><i class="icon-ok-sign"></i> Exporter en fichier Word</button>
        </form>
    </div>

    <div class="span4">
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
<br><br>
        <FORM>
            <INPUT type="checkbox" id="IncludeGraphRelation" value="true"> Inclure les graphes relationnels "Vous connaissez..."
        </FORM>

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

<div class="row-fluid" id="RelationGraphContainer" >
    <div class="span12" id="Relations">
        <div class="panel panel-default">
            <div style="overflow: auto; height:500px;" id="container">
                <g:hiddenField id="relationjson" name="relationjson" value="${relationjson}"/>
                <div id="infovis">
                </div>
                <div id="right-container">
                    <div id="inner-details"></div>
                </div>
                <g:render template="/publication/relationGraph"></g:render>
            </div>
            <div class="legend">
            </div>
        </br>
        </div>
    </div>
</div>
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
    document.getElementById('RelationGraphContainer').style.display ="none";
    $("#IncludeGraphRelation").click(function(){
        if(document.getElementById("IncludeGraphRelation").checked == true) {
        document.getElementById('RelationGraphContainer').style.display = "";
        html2canvas($("#RelationGraphContainer"),
                {
                    onrendered: function(canvas)
                    {
                        var img = canvas.toDataURL("image/png");
                        document.getElementById('imgsrc').value = img;
                    }
                });
        } else {
            document.getElementById("imgsrc").value = null;
            document.getElementById('RelationGraphContainer').style.display ="none";
        }
    });
</script>
</body>



</html>