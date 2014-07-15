<%@ page import="org.gnk.selectintrigue.Plot"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'plot.label', default: 'Plot')}" />
<title><g:message code="default.create.label"
		args="[entityName]" /></title>
</head>
<body>
	<a href="#create-plot" class="skip" tabindex="-1"><g:message
			code="default.link.skip.label" default="Skip to content&hellip;" /></a>
	<div class="nav" role="navigation">
		<ul>
			<li><a class="home" href="${createLink(uri: '/')}"><g:message
						code="default.home.label" /></a></li>
			<li><g:link class="list" action="list">
					<g:message code="default.list.label" args="[entityName]" />
				</g:link></li>
		</ul>
	</div>
	<div id="create-plot" class="content scaffold-create" role="main">
		<h1>
			<g:message code="redactintrigue.generalDescription.createNewPlot" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${plotInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${plotInstance}" var="error">
					<li
						<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
							error="${error}" /></li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form action="saveAndEdit">
			<div class="fieldcontain ${hasErrors(bean: plotInstance, field: 'name', 'error')} required">
				<label for="name">
                    <g:message code="redactintrigue.generalDescription.plotName" default="Name" />
                    <span class="required-indicator">*</span>
				</label>
				<g:textField name="name" value="${plotInstance.name}" required="" />
			</div>
			<fieldset class="buttons">
				<g:submitButton name="create" class="saveAndEdit"
					value="${message(code: 'default.button.create.label', default: 'Create')}" />
			</fieldset>
		</g:form>
	</div>
</body>
</html>
