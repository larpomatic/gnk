<%@ page import="org.gnk.resplacetime.Place" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'place.label', default: 'Place')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<g:render template="/tag/subNav" />
<div id="list-tag" class="content scaffold-list" role="main">
    <fieldset>
        <h2><g:message code="adminRef.genericPlace.title"/></h2>
    </fieldset>

    <g:render template="../infosAndErrors" />
    <g:render template="addGenericPlaces" />
    <g:render template="tableGenericPlaces" />

    <div class="pagination">
        <g:paginate total="${genericPlaceInstanceTotal}" />
    </div>
</div>
</body>
</html>
