<%@ page import="org.gnk.resplacetime.GenericResource" %>
<%@ page import="org.gnk.resplacetime.Resource" %>

<div id="create-resource" class="content scaffold-create" role="main">
	<legend>Création d'une nouvelle ressource générique </legend>

	<g:hasErrors bean="${genericResourceInstance}">
	<ul class="errors" role="alert">
		<g:eachError bean="${resourceInstance}" var="error">
		<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
		</g:eachError>
	</ul>
	</g:hasErrors>
	
	<g:form action="save" >
		<form class="form-inline">
			<div class="row">
     			<div class="span3">Nom : <g:textField name="code" maxlength="45" value="${genericResourceInstance?.code}"/></div>
     			<div class="span4">Description : <g:textField name="comment" maxlength="100" value="${genericResourceInstance?.comment}"/></div>
   			</div>
  			<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.add')}" />
		</form>
	</g:form>
</div>