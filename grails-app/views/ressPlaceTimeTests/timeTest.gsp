<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>${message(code: 'ressplacetime.time.test.title')}</title>
	</head>
	<body>
        <g:render template="subNav" />
		<div id="list-tag" class="content scaffold-list" role="main">
            <fieldset>
                <h2><g:message code="ressplacetime.time.test.title"/></h2>
            </fieldset>
		</div>

    <g:remoteLink action="timeTestForPastscene" update="resultForPastsceneTest">Tester Time sur des Pastscenes</g:remoteLink>
    <div id="resultForPastsceneTest"></div>
    <br/>
    <g:remoteLink action="timeTestForEvent" update="resultForEventTest">Tester Time sur des Events</g:remoteLink>
    <div id="resultForEventTest"></div>
	</body>
</html>
