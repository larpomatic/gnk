<%@ page import="org.gnk.tag.Tag" %>
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
			<g:render template="addTags" />
			<g:render template="addTagIntoFamily" />
			<g:render template="tableTags" />
			
			<div class="pagination">
				<g:paginate total="${tagInstanceList.totalCount}" />
			</div>
		</div>
	</body>
</html>










