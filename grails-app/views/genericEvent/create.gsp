<%@ page import="org.gnk.genericevent.GenericEvent" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'genericEvent.label', default: 'GenericEvent')}" />
		%{--<title><g:message code="default.create.label" args="[entityName]" /></title>--}%
		<title>Creer Evenement Générique</title>
	</head>
	<body>
		<a href="#create-genericEvent" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-genericEvent" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="alert alert-error" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${genericEventInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${genericEventInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="save" method="POST">
				<fieldset class="form-horizontal">
                    <g:render template="form" model="[genericEventInstance : genericEventInstance,
													  genericEventInstanceList : genericEventInstanceList,
													  TagInstanceList : TagInstanceList,
													  eventHasTagList : eventHasTagList,
													  canImplyTagList : canImplyTagList,
													  canImplyGenericEventList : canImplyGenericEventList]"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.create.label', default: 'Create')}" />
					<g:actionSubmit action="index" value="${message(code: 'default.back.label', default: 'Back')}"
									formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
