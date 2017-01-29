<%@ page import="org.gnk.roletoperso.Role" %>
<%@ page import="org.gnk.roletoperso.Character" %>
<%@ page import="org.gnk.selectintrigue.Plot" %>
<%@ page import="org.gnk.gn.Gn" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'life.css')}" type="text/css">
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'gn.label', default: 'GN')}"/>
    %{--<title><g:message code="gn.edition" /></title>--}%
    <title>Vie des personnages</title>
</head>

<body>
<div id="edit-plot" class="content scaffold-list" role="main">
    <g:render template="../stepBarProgress/stepProgressBar" model="[currentStep = 'life']"/>

    <h1><g:message code="life.label" default="Life Module"/></h1>
    <g:render template="result"
              model="[gnInstance: gnInstance, characterListLife: characterListLife, PHJList: PHJList]"/>
</div>
</body>
</html>