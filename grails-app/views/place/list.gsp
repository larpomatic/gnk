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
                <h2><g:message code="adminRef.place.title"/></h2>
            </fieldset>

            <g:render template="../infosAndErrors" />
            <g:render template="addPlaces" />
            <g:render template="addTagToPlace" />
            <g:render template="tablePlaces" model="[placeInstanceList : placeInstanceList]" />

        <div class="pagination">
            <g:paginate total="${placeInstanceTotal}" />
        </div>
		</div>
    <script type="application/javascript">
        $(function(){
            $("#listTable").DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/French.json"
                }
            });
        });
    </script>
	</body>
</html>
