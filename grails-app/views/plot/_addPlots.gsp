<%@ page import="org.gnk.selectintrigue.Plot" %>
<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'plot.label', default: 'Plot')}" />
</head>

<div id="create-plot" class="content scaffold-create" role="main">
	<h1><g:message code="default.create.label" args="[entityName]" /></h1>

	<g:hasErrors bean="${plotInstance}">
	<ul class="errors" role="alert">
		<g:eachError bean="${plotInstance}" var="error">
		<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
		</g:eachError>
	</ul>
	</g:hasErrors>
	<g:form action="save" >
		<fieldset class="form">
			<g:render template="form"/>
		</fieldset>
		<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
	</g:form>
</div>
