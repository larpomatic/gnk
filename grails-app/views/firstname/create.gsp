<%@ page import="org.gnk.naming.Firstname" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'Name.label', default: 'Firstname')}" />
		<title>Creer nouveau Prénom</title>
	</head>
	<body>
		<a href="#create-Firstname" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-genericEvent" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[firstname]" /></h1>
			<g:if test="${flash.message}">
			<div class="alert alert-error" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${FirstnameInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${FirstnameInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="save" method="POST">
				<fieldset class="form-horizontal">
                    <g:render template="form" model="[FirstnameInstance : FirstnameInstance,
													  FirstnameInstanceList : FirstnameInstanceList,
													  TagInstanceList : TagInstanceList,
													  FirstnameHasTagList : FirstnameHasTagList]"/>

				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="Création" />
					<g:actionSubmit action="index" value="${message(code: 'default.back.label', default: 'Back')}"
									formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
