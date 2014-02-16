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
</style>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'gn.label', default: 'GN')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
	<div id="edit-plot" class="content scaffold-list" role="main">
		<h2>
			DTD
		</h2>

<pre>${gnDTD}</pre>	

<g:link action="selectIntrigue" id="${gnInstanceId}"  params="[screenStep: 1, gnDTD: gnDTD]">Run SelectIntrigue</g:link>
	</div>
</body>
</html>


