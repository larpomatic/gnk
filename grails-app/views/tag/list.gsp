<%@ page import="org.gnk.tag.Tag; org.gnk.admin.right" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>${message(code: 'adminRef.tag.title')}</title>
	</head>
	<body>
        <g:render template="subNav" />
		<div id="list-tag" class="content scaffold-list" role="main">
            <fieldset>
                <h2><g:message code="adminRef.tag.title"/></h2>
            </fieldset>

			<g:render template="../infosAndErrors" />
            <g:hasRights lvlright="${right.REFMODIFY.value()}">
			    <g:render template="addTags" />
			    <g:render template="addTagIntoFamily" />
            </g:hasRights>
			<g:render template="tableChildrenTags" model="[listTagParent : listTagParent]" />
			
			<div class="pagination">
				<g:paginate total="${tagInstanceList.totalCount}" />
			</div>
		</div>
	</body>
</html>







