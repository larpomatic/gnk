<%@ page import="org.gnk.selectintrigue.Plot"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="<g:resource dir="css" file="selectIntrigue.css" />" type="text/css">
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'gn.label', default: 'GN')}" />
<title><g:message code="gn.edition" /></title>
</head>
<body>
    <g:javascript src="selectIntrigue/selectIntrigue.js"/>
    <g:render template="subNav"/>
	<div id="edit-plot" class="content scaffold-list" role="main">
		<legend><g:message code="selectintrigue.selectintrigueModule" default="SelectIntrigue Module"/></legend>

				<g:if test="${!screenStep || screenStep == '0'}">
					<g:render template="step0_createGn" />
				</g:if>
				<g:if test="${screenStep == '1'}">
					<g:render template="step1_result" />
				</g:if>			
	</div>
</body>
</html>
