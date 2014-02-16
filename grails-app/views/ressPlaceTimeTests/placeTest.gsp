<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>${message(code: 'ressplacetime.place.test.title')}</title>
    </head>
    <body>
    <g:render template="subNav" />
    <div id="list-tag" class="content scaffold-list" role="main">
        <fieldset>
            <h2><g:message code="ressplacetime.place.test.title"/></h2>
        </fieldset>
    </div>
    <g:remoteLink action="placeTestWithoutBannedItems" update="resultWithoutBannedItemsTest">Tester Place sans Lock</g:remoteLink>
    <div id="resultWithoutBannedItemsTest"></div>
    <br/>
    <g:remoteLink action="placeTestWithBannedItems" update="resultWithBannedItemsTest">Tester Place avec Lock</g:remoteLink>
    <div id="resultWithBannedItemsTest"></div>
    </body>
</html>