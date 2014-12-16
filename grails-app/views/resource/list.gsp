
<%@ page import="org.gnk.resplacetime.Resource; org.gnk.admin.right" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'resource.label', default: 'Resource')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<g:render template="/tag/subNav" />
		<div id="list-resource" class="content scaffold-list" role="main">
            <fieldset>
                <h2><g:message code="adminRef.resource.title" /></h2>
            </fieldset>
			
			<div>
				<g:render template="../infosAndErrors" />
			</div>

			<g:render template="addResource" />
			<g:render template="addTagToResource" />
			<g:render template="tableResources" model="[resourceInstanceList : resourceInstanceList]"/>
			<div class="pagination">
				<g:paginate total="${resourceInstanceTotal}" />
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
