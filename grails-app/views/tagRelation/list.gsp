<%@ page import="org.gnk.tag.TagRelation" %>
<%@ page import="org.gnk.tag.Tag" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tagRelation.label', default: 'TagRelation')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<g:render template="/tag/subNav" />
		<div id="list-resource" class="content scaffold-list" role="main">
            <fieldset>
                <h2>Administration des relations entre tags</h2>
            </fieldset>
			
			<div>
				<g:render template="../infosAndErrors" />
			</div>
			
			<g:render template="addRelation" />
			<g:render template="tableRelations" model="[listTagParent : listTagParent]" />

            <div class="pagination">
                <g:paginate total="${tagRelationInstanceList.totalCount}" />
            </div>
			
		</div>
	</body>
</html>
