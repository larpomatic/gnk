<%@ page import="org.gnk.roletoperso.Role" %>
<%@ page import="org.gnk.roletoperso.Character" %>
<%@ page import="org.gnk.selectintrigue.Plot" %>
<%@ page import="org.gnk.gn.Gn" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'roleToPerso.css')}" type="text/css">
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'gn.label', default: 'GN')}"/>
    <title><g:message code="gn.edition"/></title>
</head>

<body>
<g:render template="../stepBarProgress/stepProgressBar" model="[currentStep='roleToPerso']"/>
<g:render template="subNav"/>
<div id="edit-plot" class="content scaffold-list" role="main">
    <legend><g:message code="roletoperso.roletopersoModule" default="RoleToPerso Module"/></legend>
    <g:link action="getBack" controller="selectIntrigue" id="${gnInstance.id}"
            class="btn btn-primary pull-right"><g:message code="default.back.label" default="Back"/></g:link>
    <g:render template="result"/>
</div>
</body>
</html>