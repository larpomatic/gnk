<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>${message(code: 'ressplacetime.resource.test.title')}</title>
	</head>
	<body>
        <g:render template="subNav" />
		<div id="list-tag" class="content scaffold-list" role="main">
            <fieldset>
                <h2><g:message code="ressplacetime.resource.test.title"/></h2>
            </fieldset>
		</div>

    <g:remoteLink action="resourceTestWithoutBannedItems" update="resultWithoutBannedItemsTest">Tester Resource sans Lock</g:remoteLink>
    <div id="resultWithoutBannedItemsTest"></div>
    <br/>
    <g:remoteLink action="resourceTestWithBannedItems" update="resultWithBannedItemsTest">Tester Resource avec Lock</g:remoteLink>
    <div id="resultWithBannedItemsTest"></div>
	</body>
</html>