<%@ page import="groovy.json.JsonBuilder; org.codehaus.groovy.grails.web.json.JSONObject; org.gnk.roletoperso.RoleHasRelationWithRole; org.gnk.roletoperso.Role" %>
<%@ page import="org.gnk.roletoperso.Role"%>
<%@ page import="org.gnk.roletoperso.Character"%>
<%@ page import="org.gnk.selectintrigue.Plot"%>
<%@ page import="org.gnk.gn.Gn"%>

<!DOCTYPE html>
<html>
<head>
    <g:javascript library='jquery' />
    <meta name="layout" content="main">
    <title><g:message code="navbar.publication" /></title>
</head>
<body>
<g:render template="../stepBarProgress/stepProgressBar" model="[currentStep='publication']"/>
<h1><g:message code="publication.label" default="Publication Module"/></h1>
<br><br>
%{--<g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right"><g:message code="default.back.label" default="Back"/></g:link>--}%
<div class="row-fluid" id="hidTest">
    <div class="span4">
        <form id="exportWordButton" action="${g.createLink(controller:'publication', action:'publication')}" method="POST">
            <input type="hidden" value="${gnId}" name="gnId"/>
            <input id="templateWordSelect" type="hidden" value="${universName}" name="templateWordSelect"/>
            <input id="imgsrc" type="hidden" value="" name="imgsrc"/>
            <input id="jsoncharlist" type="hidden" value="${jsoncharlist}" name="jsoncharlist"/>
            <button id="WordButtonPublication" class="btn" type="submit" style="visibility: visible; display: block; margin-bottom: 10px;" ><i class="icon-ok-sign"></i> Exporter en fichier Word</button>
            <INPUT type="checkbox" id="IncludeInterpretationAdvice" name="IncludeInterpretationAdvice" value="false"/> Inclure les conseils d'interprétation
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
    <g:set var="counter" value="${1}" />
    <g:each in="${relationjsonlist}" var="reljson">
        <g:hiddenField id="relationjson${counter}" name="relationjson${counter}" value="${reljson}"/>
        <g:set var="counter" value="${counter + 1}" />
    </g:each>
<br><br>
        <FORM>
            <INPUT type="checkbox" id="IncludeGraphRelation" value="false"> Inclure les graphes relationnels "Vous connaissez...
        </FORM>
</div><div id="pubAlertContainer">
</div>
<div class="row-fluid" id="RelationGraphContainer" style="display: none">
    <div class="span12" id="Relations">
        <div class="panel panel-default">
            <div style="overflow: auto; height:500px" id="container">
                <g:hiddenField id="relationjson0" name="relationjson0" value="${globalrelationjson}"/>
                <div id="infovis">
                </div>
                <g:render template="/publication/relationGraph"></g:render>
            </div>
            <div class="legend">
            </div>
        </br>
        </div>
    </div>
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





<h2><u>${title} - Synthèse pour les organisateurs</u></h2>
<i>${subtitle}</i>


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

<table>
    <tr>
        <td>
    <h3>Synthèse des personnages du GN</h3>

<TABLE BORDER="1" CELLPADDING="10">
    <TR>
        <TH> NOM - Prenom </TH>
        <TH> Nb PIP Total </TH>
        <TH> Type </TH>
        <TH> Sexe </TH>
        <TH> Age </TH>
        <TH> Relations </TH>
    </TR>
    <g:each in="${charactersList}" var="c">
        <TR>
            <TH> ${c.lastname} ${c.firstname} </TH>
            <TD> ${c.nbPIP} </TD>
            <TD> ${c.type} </TD>
            <TD> ${c.gender} </TD>
            <TD> ${c.getAge()} </TD>
            <TD>

        <g:each in="${c.getRelations(true)?.keySet()}" var="rela">

                    <g:set var="characterRel"
                           value="${gnInstance?.getCharacterContainingRole(rela.getterRole1())?.lastname?.encodeAsHTML()}
                           ${gnInstance?.getCharacterContainingRole(rela.getterRole1())?.firstname?.encodeAsHTML()}"/>

                    ${((RoleHasRelationWithRole) rela).getterRole1().code?.encodeAsHTML()} : ${characterRel}
            <br>

                    <g:set var="characterRel"
                           value="${gnInstance?.getCharacterContainingRole(rela.getterRole2())?.lastname}
                           ${gnInstance?.getCharacterContainingRole(rela.getterRole2())?.firstname}"/>
                    ${((RoleHasRelationWithRole) rela).getterRole2().code?.encodeAsHTML()} : ${characterRel}
            <br>
                    ${((RoleHasRelationWithRole) rela).description?.encodeAsHTML()}
            <br>
                    (${((RoleHasRelationWithRole) rela).getterRoleRelationType().name})
            <br><br>
        </g:each>
            </TD>
        </TR>
    </g:each>
</TABLE>
        </td>
        <g:form mehtod="post">
            <td>
                <label for="gnDescription"><g:message
                        code="selectintrigue.step0.gnDescription"
                        default=" Gn's description"/></label>
                <g:textArea name="gnDescription" value="${gnInstance?.description_text}"/>
            </td>
            <td>
                <g:actionSubmit value="${message(code: 'default.button.save.label', default: 'Save')}"/>
            </td>
        </g:form>
    </tr>
</table>


<script type="text/javascript">
    document.getElementById('RelationGraphContainer').style.display ="none";
    $("#IncludeGraphRelation").click(function(){
        if(document.getElementById("IncludeGraphRelation").checked == true) {
            document.getElementById('RelationGraphContainer').style.display = "";
            document.getElementById('relationGraphLoader').style.display = "";
            document.getElementById("WordButtonPublication").disabled = true;
            document.getElementById("IncludeGraphRelation").disabled = true;

            initGraph("relationjson", "infovis", "0"); // TODO : virer le loader une fois finit
        } else {
            document.getElementById("imgsrc").value = null;
            document.getElementById('RelationGraphContainer').style.display ="none";
            document.getElementById('relationGraphLoader').style.display ="none";
            document.getElementById("WordButtonPublication").disabled = false;
            document.getElementById("IncludeGraphRelation").disabled = false;
        }
    });
</script>
</body>



</html>