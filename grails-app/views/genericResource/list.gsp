
<%@ page import="org.gnk.resplacetime.GenericResource" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'genericResource.label', default: 'GenericResource')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
    <g:render template="/tag/subNav" />
    <div id="list-resource" class="content scaffold-list" role="main">
        <fieldset>
            <h2><g:message code="adminRef.GenResource.title" /></h2>
        </fieldset>

        <div>
            <g:render template="../infosAndErrors" />
        </div>

        <g:render template="addGenericResource" />
        <g:render template="addTagToGenericResource" />
        <!--g:render template="addResourceToUnivers" /-->
        <g:render template="tableGenericResources" />
        <div class="pagination">
            <g:paginate total="${genericResourceInstanceTotal}" />
        </div>
    </div>
	</body>
</html>
