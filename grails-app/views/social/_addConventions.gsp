<%@ page import="org.gnk.naming.Convention" %>
%{--<%@ page import="org.gnk.tag.TagFamily" %>--}%
<div id="create-tag" class="content scaffold-create" role="main">
    <legend>${message(code: 'adminRef.social.conventionRule.newConvention')}</legend>
    <g:form action="save" >
        <form class="form-inline">
            <div class="row">
                <div class="span4">${message(code: 'adminRef.social.conventionRule.conventionName')} : <g:textField name="name" maxlength="45" value="${tagInstance?.name}"/></div>
            </div>
            <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.add')}" />
        </form>
    </g:form>
</div>
