<%@ page import="org.gnk.naming.Name" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="Name" />
    %{--<title><g:message code="default.edit.label" args="[entityName]" /></title>--}%
    <title>Editer Patronyme</title>
</head>
<body>
<div id="edit-Name" class="content scaffold-edit" role="main">
    <h1>Edition Patronyme </h1>
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
    %{--<g:form action="edit">--}%
    <g:form>
        <form>

            <g:hiddenField name="NameId" value="${NameInstance?.id}" />
            <g:hiddenField name="NameVersion" value="${NameInstance?.version}" />
            <fieldset class="form-inline">
                <g:render template="form" model="[NameInstance : NameInstance,
                                                  NameInstanceList : NameInstanceList,
                                                  TagInstanceList : TagInstanceList,
                                                  NameHasTagList : NameHasTagList]"/>
            </fieldset>
            <fieldset class="buttons">
                <g:actionSubmit class="save" action="update" value="Edition" />
                <g:actionSubmit action="index" value="${message(code: 'default.back.label', default: 'Back')}"
                                formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </fieldset>
        </form>
    </g:form>
</div>
</body>
</html>
