<%@ page import="org.gnk.selectintrigue.Plot"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
th,td {
	padding: 0.3em;
	padding-left: 0.5em;
	margin: 0.3em;
}

.tab-pane.active {
	height: 350pt;
}

.time
{
    width: 55pt;
    margin-left: 0pt;
}

.date
{
    margin-right: 0pt;
    width: 95pt;
}
</style>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'gn.label', default: 'GN')}" />
<title><g:message code="gn.edition" /></title>
</head>
<body>
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
