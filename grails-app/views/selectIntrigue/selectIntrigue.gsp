<%@ page import="org.gnk.selectintrigue.Plot"%>
<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'gn.label', default: 'GN')}" />
<title><g:message code="gn.edition" /></title>
<link rel="stylesheet" href="<g:resource dir="css" file="selectIntrigue.css" />" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-datetimepicker.min.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'dhtmlxcalendar.css')}" type="text/css">
</head>
<body>
    <g:javascript src="redactIntrigue/bootstrap-datetimepicker.min.js"/>
    <g:javascript src="redactIntrigue/bootstrap-datetimepicker.fr.js"/>
    <g:javascript src="selectIntrigue/dhtmlxcalendar.js"/>
    <g:javascript src="selectIntrigue/selectIntrigue.js"/>

	<div id="edit-plot" class="content scaffold-list" role="main">
        <g:if test="${!screenStep || screenStep == '0'}">
            <g:render template="step0_createGn"/>
        </g:if>
        <g:if test="${screenStep == '1'}">
            <g:render template="../stepBarProgress/stepProgressBar" model="[currentStep='selectIntrigue']"/>
            <h1><g:message code="selectintrigue.selectintrigueModule" default="SelectIntrigue Module"/></h1>
            <g:render template="/selectIntrigue/step1_result" />
        </g:if>
	</div>
</body>
</html>
