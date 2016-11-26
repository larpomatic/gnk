<%@ page import="org.gnk.resplacetime.Place" %>
<%@ page import="org.gnk.admin.right" %>
<%@ page import="org.gnk.resplacetime.GenericPlace" %>
<div id="create-resource" class="content scaffold-create" role="main">
    <legend>Création d'un nouveau lieu</legend>


    <g:hasErrors bean="${placeInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${placeInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

    <g:form action="save" >
        <form class="form-inline">
            <div class="row">
                <div class="span3">Nom : <g:textField name="name" maxlength="45" value="${placeInstance?.name}"/></div>
                <div class="span3.5">Description : <g:textField name="desc" maxlength="100" value="${placeInstance?.description}"/></div>
                <div class="span2.5">
                    <g:select
                            name="gender_select"
                            from="${Place.genders}"
                            noSelection="['':'-Choix du genre-']"/>
                </div>
                <div class="span3">
                    <g:select
                            name="genericPlace_select"
                            optionKey="id"
                            optionValue="code"
                            from="${GenericPlace.list()}"
                            noSelection="['':'-Lieu générique-']"/>
                </div>
            </div>
            <g:hasRights lvlright="${right.REFMODIFY.value()}">
                <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.add')}" />
            </g:hasRights>
        </form>
    </g:form>
</div>