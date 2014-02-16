
<%@ page import="org.gnk.tag.Univers" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'univers.label', default: 'Univers')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
        <g:render template="/tag/subNav" />
		<div id="list-univers" class="content scaffold-list" role="main">
            <fieldset>
                <h2><g:message code="adminRef.univers" /></h2>
            </fieldset>
			
			<g:render template="../infosAndErrors" />
			<g:render template="addUnivers" />
			<!-- g:render template="addTagToUnivers" /> Can universes have tags ? -->
			<g:render template="tableUnivers" />
			
			<div class="pagination">
				<g:paginate total="${universInstanceTotal}" />
			</div>

		</div>
	</body>
</html>
