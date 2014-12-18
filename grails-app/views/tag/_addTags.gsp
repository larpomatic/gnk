<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.admin.right" %>
%{--<%@ page import="org.gnk.tag.TagFamily" %>--}%
<div id="create-tag" class="content scaffold-create" role="main">
	<legend>${message(code: 'adminRef.tag.newTag')}</legend>
	<g:form action="save" >
		<form class="form-inline">
			<div class="row">
                <div class="span4">${message(code: 'adminRef.tag.tagName')} : <g:textField name="name" maxlength="45" value="${tagInstance?.name}"/></div>
                <div class="span3">
                %{--<g:select--}%
                  %{--name="TagFamily_select"--}%
                  %{--optionKey="id"--}%
                  %{--optionValue="value"--}%
                  %{--from="${TagFamily.list()}"--}%
                  %{--noSelection="['':'-Choix de la famille de tag-']"/>--}%
                </div>
   			</div>
            <g:hasRights lvlright="${right.REFMODIFY.value()}">
			<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.add')}" />
            </g:hasRights>

		</form>
	</g:form>
</div>
