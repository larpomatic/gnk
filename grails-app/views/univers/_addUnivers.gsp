<%@ page import="org.gnk.tag.Univers" %>

<div id="create-univers" class="content scaffold-create" role="main" xmlns="http://www.w3.org/1999/html">
	<legend><g:message code="adminRef.univers.create"/></legend>

	<g:hasErrors bean="${universInstance}">
	<ul class="errors" role="alert">
		<g:eachError bean="${universInstance}" var="error">
		<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
		</g:eachError>
	</ul>
	</g:hasErrors>
	<g:form action="save" >
		<div class="control-group fieldcontain ${hasErrors(bean: universInstance, field: 'name', 'error')} ">
			<label class="control-label" for="name">
				<g:message code="adminRef.univers.name" default="Name" />
			</label>
            <div class="controls">
			    <g:textField name="name" maxlength="45" value="${universInstance?.name}"/>
            </div>
		</div>
		<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
	</g:form>
</div>
