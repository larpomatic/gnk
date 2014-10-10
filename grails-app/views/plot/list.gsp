<%@ page import="org.gnk.selectintrigue.Plot" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'plot.label', default: 'Plot')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
        <g:render template="/tag/subNav" />
		<div id="list-plot" class="content scaffold-list" role="main">
            <fieldset>
                <h2><g:message code="AdminRef.plot.title" /></h2>
            </fieldset>
			<legend>Création d'une nouvelle intrigue</legend>
			<div class="btn btn">
				<g:link controller="redactIntrigue" class="create" action="create">Création d'une nouvelle intrigue</g:link>
			</div>
			
			<div>
				<g:render template="../infosAndErrors" />
			</div>
			
			
			<g:render template="addTagToPlot" />
			%{--<g:render template="addPlotToUnivers" />--}%
			<g:render template="tablePlots"></g:render>
			
			<div class="pagination">
				<g:paginate total="${plotInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
