<%@ page import="org.gnk.selectintrigue.Plot"%>
<!DOCTYPE html>
<html>
<head>

<meta name="layout" content="main">
<g:set var="entityName"
	value="${plotInstance.name}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-datetimepicker.min.css')}" type="text/css">
<link rel="stylesheet" href="${resource(dir: 'css', file: 'redactIntrigue.css')}" type="text/css">
</head>
<body>

<g:javascript src="redactIntrigue/bootstrap-confirmation.js"/>
<g:javascript src="redactIntrigue/bootstrap-datetimepicker.min.js"/>
<g:javascript src="redactIntrigue/bootstrap-datetimepicker.fr.js"/>
<g:javascript src="redactIntrigue/redactIntrigue.js"/>
<g:javascript src="redactIntrigue/role.js"/>
<g:javascript src="redactIntrigue/event.js"/>
	%{--<div class="navbar navbar-inverse">--}%
		%{--<div class="navbar-inner">--}%
			%{--<a class="brand" href="#"> ${message(code: 'navbar.redactIntrigue')} :--}%
			%{--</a>--}%
			%{--<ul class="nav">--}%
				%{--<li class="active"><g:link class="edit" action="edit" id="${plotInstance?.id}"--}%
						%{--params="[screenStep: 0]">--}%
						%{--${message(code: 'redactintrigue.tabs.generalDescription')}--}%
					%{--</g:link></li>--}%
				%{--<li><g:link class="edit" action="edit" id="${plotInstance?.id}"--}%
						%{--params="[screenStep: 1]">--}%
						%{--${message(code: 'redactintrigue.tabs.roles')}--}%
					%{--</g:link></li>--}%
				%{--<li><g:link class="edit" action="edit" id="${plotInstance?.id}"--}%
						%{--params="[screenStep: 2]">--}%
						%{--${message(code: 'redactintrigue.tabs.relations')}--}%
					%{--</g:link></li>--}%
				%{--<li><g:link class="edit" action="edit" id="${plotInstance?.id}"--}%
						%{--params="[screenStep: 3]">--}%
						%{--${message(code: 'redactintrigue.tabs.places')}--}%
					%{--</g:link></li>--}%
				%{--<li><g:link class="edit" action="edit" id="${plotInstance?.id}"--}%
						%{--params="[screenStep: 4]">--}%
						%{--${message(code: 'redactintrigue.tabs.objects')}--}%
					%{--</g:link></li>--}%
				%{--<li><g:link class="edit" action="edit" id="${plotInstance?.id}"--}%
						%{--params="[screenStep: 5]">--}%
						%{--${message(code: 'redactintrigue.tabs.textualClues')}--}%
					%{--</g:link></li>--}%
				%{--<li><g:link class="edit" action="edit" id="${plotInstance?.id}"--}%
						%{--params="[screenStep: 6]">--}%
						%{--${message(code: 'redactintrigue.tabs.pastScenes')}--}%
					%{--</g:link></li>--}%
				%{--<li><g:link class="edit" action="edit" id="${plotInstance?.id}"--}%
						%{--params="[screenStep: 7]">--}%
						%{--${message(code: 'redactintrigue.tabs.events')}--}%
					%{--</g:link></li>--}%
			%{--</ul>--}%
		%{--</div>--}%
	%{--</div>--}%

	<div id="edit-plot" class="content scaffold-list">
		<h1>
			<g:message code="default.edit.label" args="[entityName]" />
		</h1>
        <g:render template="testMenuSlide" />
				%{--<g:if test="${screenStep == 0}">--}%
					%{--<g:render template="generalDescriptionForm" />--}%
                    %{----}%
                    %{--<g:render template="testMenuModal" />--}%
				%{--</g:if>--}%
				%{--<g:if test="${screenStep == 1}">--}%
					%{--<g:render template="rolesForm" />--}%
				%{--</g:if>--}%
				%{--<g:if test="${screenStep == 2}">--}%
					%{--<g:render template="relationsForm" />--}%
				%{--</g:if>--}%
				%{--<g:if test="${screenStep == 3}">--}%
					%{--<g:render template="placesForm" />--}%
				%{--</g:if>--}%
				%{--<g:if test="${screenStep == 4}">--}%
					%{--<g:render template="objectForm" />--}%
				%{--</g:if>--}%
				%{--<g:if test="${screenStep == 5}">--}%
					%{--<g:render template="textualCluesForm" />--}%
				%{--</g:if>--}%
				%{--<g:if test="${screenStep == 6}">--}%
					%{--<g:render template="pastScenesForm" />--}%
				%{--</g:if>--}%
				%{--<g:if test="${screenStep == 7}">--}%
					%{--<g:render template="eventsForm" />--}%
				%{--</g:if>--}%
                %{--<g:if test="${screenStep == 8}">   <!-- FIXME TO Remove -->--}%
                    %{--<g:render template="richTextEditor" />--}%
                %{--</g:if>--}%
			
	</div>
</body>
</html>
