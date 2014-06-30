
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="navbar.publication" /></title>
</head>
<body>
<h1><u>${title}</u></h1>
<i>${subtitle}</i>>
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
        <TH> Indication(s) personnage </TH>
    </TR>
    <g:each in="${charactersList}" var="c">
        <TR>
            <TH> ${c.lastname} ${c.firstname} </TH>
            <TD> ${c.nbPIP} </TD>
            <TD> ${c.type} </TD>
            <TD> ${c.gender} </TD>
            <TD> ${c.getCharacterAproximateAge()} </TD>
            <TD> ${} </TD>
        </TR>
    </g:each>
</TABLE>



</body>
</html>