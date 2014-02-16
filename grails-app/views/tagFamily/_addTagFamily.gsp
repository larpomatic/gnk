<%@ page import="org.gnk.tag.TagFamily" %>

<div id="create-tagFamily" class="content scaffold-create" role="main">
	<legend><g:message code="adminRef.tagFamily.addTitle" /></legend>
	<g:form action="save">
		<form class="form-inline">
            <div class="row">
            	<div class="span2">
	                <label class="control-label" for="name">
	                    <g:message code="adminRef.tagFamily.name" default="Name" />
	                </label>
                </div>
                <div class="span2.5">
	                <div class="controls">
	                    <g:textField name="name" maxlength="45" value="${tagFamilyInstance?.value}"/>
	                </div>
                </div>
                <div class="span1.5">
                    Applicable sur des :
                </div>
                <div class="span0.5">
                    Rôles
                    <g:checkBox name="isRoleTag" value="${false}" />
                </div>
                <div class="span0.5">
                    Intrigues
                    <g:checkBox name="isPlotTag" value="${false}" />
                </div>
                <div class="span0.5">
                    Ressources
                    <g:checkBox name="isResourceTag" value="${false}" />
                </div>
            </div>
			<g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.add')}" />
		</form>
	</g:form>
</div>