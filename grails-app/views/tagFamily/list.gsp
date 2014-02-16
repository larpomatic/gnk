<%@ page import="org.gnk.tag.TagFamily" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="adminRef.tagFamily.title"/></title>
	</head>
	<body>
        <g:render template="/tag/subNav" />
		<div id="list-tagFamily" class="content scaffold-list" role="main">
            <fieldset>
                <h2><g:message code="adminRef.tagFamily.title"/></h2>
            </fieldset>
			
			<g:render template="../infosAndErrors" />
			<g:render template="addTagFamily" />
			<g:render template="tableTagFamilies" />
			
			<div class="pagination">
				<g:paginate total="${tagFamilyInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
