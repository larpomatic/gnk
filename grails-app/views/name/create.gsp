<%@ page import="org.gnk.naming.Name" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'Name.label', default: 'Name')}" />
		<title>Création Patronyme</title>
	</head>
	<body>
		<div id="create-name">
			<h1>Création Patronyme</h1>
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
				<fieldset class="button">
					<g:actionSubmit class="btn btn-primary" action="update" value="Création" />
					<g:actionSubmit class="btn btn-primary" action="index" value="${message(code: 'default.back.label', default: 'Back')}"
									formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Etes vous sûr de vouloir quitter la fenêtre?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
