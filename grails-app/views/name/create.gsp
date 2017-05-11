<%@ page import="org.gnk.genericevent.GenericEvent" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'Name.label', default: 'Name')}" />
		<title>Creer nouveau Nom</title>
	</head>
	<body>
		<a href="#create-Name" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="create-genericEvent" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[name]" /></h1>
			<g:if test="${flash.message}">
			<div class="alert alert-error" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${NameInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${NameInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="save" method="POST">
				<fieldset class="form-horizontal">
                    <g:render template="form" model="[NameInstance : NameInstance,
													  NameInstanceList : NameInstanceList,
													  TagInstanceList : TagInstanceList,
													  NameHasTagList : NameHasTagList]"/>

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
