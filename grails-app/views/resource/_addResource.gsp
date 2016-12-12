<%@ page import="org.gnk.resplacetime.Resource" %>
<%@ page import="org.gnk.resplacetime.GenericResource" %>
<%@ page import="org.gnk.admin.right" %>

<div id="create-resource" class="content scaffold-create" role="main">
    <legend>${message(code: 'adminRef.resource.creation.title')}</legend>


    <g:hasErrors bean="${resourceInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${resourceInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

    <g:form action="save">
        <form class="form-inline">
            <div class="row">
                <div class="span3">${message(code: 'default.name')} : <g:textField name="name" maxlength="45"
                                                      value="${resourceInstance?.name}"/></div>

                <div class="span3.5">Description : <g:textField name="desc" maxlength="100"
                                                                value="${resourceInstance?.description}"/></div>

                <div class="span2.5">
                    <g:select
                            name="gender_select"
                            from="${Resource.genders}"
                            noSelection="['': '-Choix du genre-']"/>
                </div>

                <!--
                <div class="span3">
                    <g:select
                            name="genericResource_select"
                            optionKey="id"
                            optionValue="code"
                            from="${GenericResource.list()}"
                            noSelection="['': '-Ressource générique-']"/>
                </div>
                -->

            </div>
            <g:hasRights lvlright="${right.REFMODIFY.value()}">
                <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.add')}"/>
            </g:hasRights>
        </form>
    </g:form>
</div>