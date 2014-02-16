<%@ page import="org.gnk.resplacetime.GenericPlace" %>

<div id="create-resource" class="content scaffold-create" role="main">
    <legend>Création d'un nouveau lieu générique</legend>


    <g:hasErrors bean="${genericPlaceInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${genericPlaceInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

    <g:form action="save" >
        <form class="form-inline">
            <div class="row">
                <div class="span3">Code : <g:textField name="code" maxlength="45" value="${genericPlaceInstance?.code}"/></div>
                <div class="span3.5">Commentaire : <g:textField name="comment" maxlength="100" value="${genericPlaceInstance?.comment}"/></div>
            </div>
            <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.add')}" />
        </form>
    </g:form>
</div>