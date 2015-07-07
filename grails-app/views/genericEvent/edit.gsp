<%@ page import="org.gnk.genericevent.GenericEvent" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'genericEvent.label', default: 'GenericEvent')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="edit-genericEvent" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${genericEventInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${genericEventInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
                <form class="form-horizontal">

				<g:hiddenField name="id" value="${genericEventInstance?.id}" />
				<g:hiddenField name="version" value="${genericEventInstance?.version}" />
				<fieldset class="form">
					<g:render template="form" model="[genericEventInstance : genericEventInstance]"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
                </form>
            </g:form>
		</div>
	</body>
</html>
